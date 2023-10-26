package ir.mmd.androidDev.vault

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.DialogProperties
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import ir.mmd.androidDev.vault.model.AppSettings
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.concurrent.Executors

class MainActivity : FragmentActivity() {
	companion object {
		private const val ITEMS_ID = "ir.mmd.androidDev.vault.MainActivity.items"
	}
	
	private val exportDataLauncher = registerForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) {
		it ?: return@registerForActivityResult
		contentResolver.openOutputStream(it)?.use { outputStream ->
			openFileInput(ITEMS_ID).use { inputStream ->
				inputStream.copyTo(outputStream)
			}
		}
	}
	
	private val importLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) {
		it ?: return@registerForActivityResult
		contentResolver.openInputStream(it)?.use { inputStream ->
			openFileOutput(ITEMS_ID, MODE_PRIVATE).use { outputStream ->
				inputStream.copyTo(outputStream)
			}
		}
		
		load()
	}
	
	private val items = mutableStateMapOf<String, String>()
	private val executor = Executors.newSingleThreadExecutor()
	private val promptInfo by lazy {
		BiometricPrompt.PromptInfo
			.Builder()
			.apply {
				setTitle(getString(R.string.auth_title))
				setSubtitle(getString(R.string.auth_sub_title))
				setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
			}
			.build()
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		// splash screen
		installSplashScreen()
		
		// load data
		AppSettings.load(this)
		this.load()
		
		// paint
		super.onCreate(savedInstanceState)
		setContent {
			BackHandler {
				finishAfterTransition()
			}
			
			VaultTheme {
				MainComponent(items)
			}
		}
	}
	
	fun authenticate(onSuccess: () -> Unit) {
		BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
			override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
				runOnUiThread(onSuccess)
			}
		}).authenticate(promptInfo)
	}
	
	fun importData() {
		importLauncher.launch(arrayOf("*/*"))
	}
	
	fun exportData() {
		exportDataLauncher.launch("vault.bak")
	}
	
	fun load() {
		try {
			openFileInput(ITEMS_ID).use { file ->
				ObjectInputStream(file).use { stream ->
					(stream.readObject() as Map<String, String>).forEach { (key, content) ->
						items[key] = content
					}
				}
			}
		} catch (ignored: FileNotFoundException) {
		}
	}
	
	fun save() {
		openFileOutput(ITEMS_ID, MODE_PRIVATE).use { file ->
			ObjectOutputStream(file).use { stream ->
				stream.writeObject(mutableMapOf<String, String>().apply {
					items.toMap().forEach { (key, content) ->
						set(key, content)
					}
				})
			}
		}
	}
}

@Composable
private fun MainComponent(items: SnapshotStateMap<String, String>) {
	val navController = rememberNavController()
	val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
	
	Surface(Modifier.fillMaxSize()) {
		NavHost(navController = navController, startDestination = if (AppSettings.authenticationEnabled) "welcome" else "home") {
			composable(
				route = "welcome",
				enterTransition = { fadeIn() },
				exitTransition = { fadeOut() }
			) { WelcomePage(navController) }
			
			composable(
				route = "home",
				enterTransition = {
					when (initialState.destination.route) {
						"welcome" -> fadeIn()
						"settings" -> slideInHorizontally { if (rtl) -it else it }
						else -> null
					}
				},
				exitTransition = {
					when (targetState.destination.route) {
						"welcome" -> fadeOut()
						"settings" -> slideOutHorizontally { if (rtl) -it else it }
						else -> null
					}
				}
			) { HomePage(navController, items/* todo */) }
			
			composable(
				route = "settings",
				enterTransition = {
					when (initialState.destination.route) {
						"welcome" -> fadeIn()
						"home" -> slideInHorizontally { if (rtl) it else -it }
						else -> null
					}
				},
				exitTransition = {
					when (targetState.destination.route) {
						"welcome" -> fadeOut()
						"home" -> slideOutHorizontally { if (rtl) it else -it }
						else -> null
					}
				}
			) { SettingsPage(navController) }
			
			dialog(
				route = "content/{mode}",
				dialogProperties = DialogProperties(
					dismissOnBackPress = false,
					dismissOnClickOutside = false,
					decorFitsSystemWindows = false,
					usePlatformDefaultWidth = false
				)
			) { ContentPage(navController) }
		}
	}
	
	LifecycleResumeEffect(Unit) {
		if (AppSettings.authenticationEnabled && AppSettings.authenticationExpiresOnPause) {
			navController.navigate("welcome") {
				launchSingleTop = true
			}
		}
		
		onPauseOrDispose { }
	}
}

@Preview
@Composable
private fun LightPreview() {
	val items = remember { mutableStateMapOf("credential" to "SECRET") }
	VaultTheme(darkTheme = false) {
		MainComponent(items)
	}
}

@Preview
@Composable
private fun DarkPreview() {
	val items = remember { mutableStateMapOf("credential" to "SECRET") }
	VaultTheme(darkTheme = true) {
		MainComponent(items)
	}
}
