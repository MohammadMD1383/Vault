package ir.mmd.androidDev.vault

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ir.mmd.androidDev.vault.model.AppSettings
import ir.mmd.androidDev.vault.ui.theme.Typography
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import ir.mmd.androidDev.vault.util.navigateToHomePopWelcome

@Composable
fun WelcomePage(navController: NavController) {
	val activity = LocalContext.current as MainActivity
	val authenticate = remember {
		{
			try {
				activity.authenticate {
					if (!navController.popBackStack()) {
						navController.navigateToHomePopWelcome()
					}
				}
			} catch (ignored: Exception) {
				AppSettings.authenticationEnabled = false
				AppSettings.save(activity)
				navController.navigateToHomePopWelcome()
			}
		}
	}
	
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
					text = stringResource(R.string.welcome_to_vault),
					style = Typography.headlineLarge
				)
				
				Icon(
					Icons.Rounded.Fingerprint,
					stringResource(R.string.text_fingerprint),
					Modifier
						.size(120.dp)
						.clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
							authenticate()
						},
				)
				
				Text(
					text = stringResource(R.string.text_touch_to_auth),
					style = Typography.labelSmall
				)
			}
		}
	}
	
	LaunchedEffect(Unit) {
		authenticate()
	}
}

@Preview
@Composable
private fun LightPreview() {
	VaultTheme(darkTheme = false) {
		WelcomePage(rememberNavController())
	}
}

@Preview
@Composable
private fun DarkPreview() {
	VaultTheme(darkTheme = true) {
		WelcomePage(rememberNavController())
	}
}
