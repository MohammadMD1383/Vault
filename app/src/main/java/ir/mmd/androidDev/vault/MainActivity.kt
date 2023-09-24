package ir.mmd.androidDev.vault

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.mmd.androidDev.vault.model.AppSettings
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import java.util.concurrent.Executors

class MainActivity : FragmentActivity() {
	private val items = mutableStateMapOf(
		"credential" to "SECRET",
		"bank number" to "515645687",
		"bank cvv2" to "566",
		"bank password" to "PAsko*99d",
		"bank pin" to "1114",
		"my credential" to "cisaji jacsj iajco siajs",
		"credit card" to "6038 6636 3366 3325",
		"credit 2" to "515445464",
		"credit another" to "5545454",
		"another credit" to "najhsdhaisdhiahsd",
		"new credit" to "jasidjasjdia"
	)
	
	private val executor = Executors.newSingleThreadExecutor()
	private val promptInfo by lazy {
		BiometricPrompt.PromptInfo
			.Builder()
			.apply {
				setTitle("Open Vault")
				setSubtitle("Prove this is you")
				setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
			}
			.build()
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		// splash screen
		installSplashScreen()
		
		// load data
		AppSettings.load(this)
		
		// paint
		super.onCreate(savedInstanceState)
		setContent {
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
}

@Composable
private fun MainComponent(items: SnapshotStateMap<String, String>) {
	val navController = rememberNavController()
	
	Surface(Modifier.fillMaxSize()) {
		NavHost(navController = navController, startDestination = "home") {
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
						"settings" -> slideInHorizontally { it }
						"content/{mode}" -> slideInVertically { -it }
						else -> null
					}
				},
				exitTransition = {
					when (targetState.destination.route) {
						"welcome" -> fadeOut()
						"settings" -> slideOutHorizontally { it }
						"content/{mode}" -> slideOutVertically { -it }
						else -> null
					}
				}
			) { HomePage(navController, items/* todo */) }
			
			composable(
				route = "settings",
				enterTransition = {
					when (initialState.destination.route) {
						"welcome" -> fadeIn()
						"home" -> slideInHorizontally { -it }
						else -> null
					}
				},
				exitTransition = {
					when (targetState.destination.route) {
						"welcome" -> fadeOut()
						"home" -> slideOutHorizontally { -it }
						else -> null
					}
				}
			) { SettingsPage(navController) }
			
			composable(
				route = "content/{mode}",
				enterTransition = {
					when (initialState.destination.route) {
						"welcome" -> fadeIn()
						"home" -> slideInVertically { it }
						else -> null
					}
				},
				exitTransition = {
					when (targetState.destination.route) {
						"welcome" -> fadeOut()
						"home" -> slideOutVertically { it }
						else -> null
					}
				}
			) { ContentPage(navController) }
		}
	}
	
	// LifecycleResumeEffect(Unit) {
	// 	navController.navigate("welcome") {
	// 		launchSingleTop = true
	// 	}
	//
	// 	onPauseOrDispose { }
	// }
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
