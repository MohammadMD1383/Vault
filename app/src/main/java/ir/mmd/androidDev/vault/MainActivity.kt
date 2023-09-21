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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import java.util.concurrent.Executors

class MainActivity : FragmentActivity() {
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
		installSplashScreen()
		super.onCreate(savedInstanceState)
		setContent {
			VaultTheme {
				MainComponent()
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
private fun MainComponent() {
	val navController = rememberNavController()
	
	Surface(Modifier.fillMaxSize()) {
		NavHost(navController = navController, startDestination = "welcome") {
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
						"content" -> slideInVertically { -it }
						else -> null
					}
				},
				exitTransition = {
					when (targetState.destination.route) {
						"welcome" -> fadeOut()
						"settings" -> slideOutHorizontally { it }
						"content" -> slideOutVertically { -it }
						else -> null
					}
				}
			) { HomePage(navController) }
			
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
			) { SettingsPage() }
			
			composable(
				route = "content",
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
			) { ContentPage() }
		}
	}
	
	LifecycleResumeEffect(Unit) {
		onPauseOrDispose {
			navController.navigate("welcome") {
				launchSingleTop = true
			}
		}
	}
}

@Preview
@Composable
private fun LightPreview() {
	VaultTheme(darkTheme = false) {
		MainComponent()
	}
}

@Preview
@Composable
private fun DarkPreview() {
	VaultTheme(darkTheme = true) {
		MainComponent()
	}
}