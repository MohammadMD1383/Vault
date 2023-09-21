package ir.mmd.androidDev.vault

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import ir.mmd.androidDev.vault.ui.theme.Typography
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import java.util.concurrent.Executors

class WelcomeActivity : FragmentActivity() {
	private val executor = Executors.newSingleThreadExecutor()
	private val biometricPrompt by lazy {
		BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
			override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
				startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
				finish()
			}
		})
	}
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
	
	override fun onStart() {
		super.onStart()
		authenticate()
	}
	
	fun authenticate() {
		biometricPrompt.authenticate(promptInfo)
	}
}

@Composable
private fun MainComponent() {
	val activity = LocalContext.current as WelcomeActivity
	
	Surface {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier.fillMaxSize()
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(24.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "Welcome to Vault",
					style = Typography.headlineLarge
				)
				
				Icon(
					Icons.Rounded.Fingerprint,
					"Fingerprint",
					Modifier
						.size(120.dp)
						.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { activity.authenticate() },
				)
				
				Text(
					text = "Touch the above icon to authenticate",
					style = Typography.labelSmall
				)
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
