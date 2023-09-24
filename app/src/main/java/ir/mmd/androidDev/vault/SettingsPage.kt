package ir.mmd.androidDev.vault

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ir.mmd.androidDev.vault.model.AppSettings
import ir.mmd.androidDev.vault.ui.component.TextSwitch
import ir.mmd.androidDev.vault.ui.theme.Typography
import ir.mmd.androidDev.vault.ui.theme.VaultTheme

@Composable
fun SettingsPage(navController: NavController) {
	val context = LocalContext.current
	
	Column(Modifier.fillMaxSize()) {
		Text(
			text = "Settings",
			style = Typography.headlineMedium,
			modifier = Modifier.padding(24.dp)
		)
		
		TextSwitch(
			text = "Enable authentication",
			checked = AppSettings.authenticationEnabled,
			onCheckedChange = { AppSettings.authenticationEnabled = it },
			modifier = Modifier.fillMaxWidth()
		)
		
		TextSwitch(
			text = "Re-Authenticate when app loses focus",
			checked = AppSettings.authenticationExpiresOnPause,
			onCheckedChange = { AppSettings.authenticationExpiresOnPause = it },
			enabled = AppSettings.authenticationEnabled,
			modifier = Modifier.fillMaxWidth()
		)
		
		Spacer(modifier = Modifier.weight(1f))
		
		Row(
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier.padding(16.dp)
		) {
			Button(
				modifier = Modifier.weight(1f),
				onClick = {
					AppSettings.save(context)
					navController.popBackStack()
				}
			) {
				Text("Save")
			}
			
			OutlinedButton(
				modifier = Modifier.weight(1f),
				onClick = {
					AppSettings.reset(context)
					navController.popBackStack() }
			) {
				Text("Cancel")
			}
		}
	}
}

@Preview
@Composable
private fun LightPreview() {
	VaultTheme(darkTheme = false) {
		SettingsPage(rememberNavController())
	}
}

@Preview
@Composable
private fun DarkPreview() {
	VaultTheme(darkTheme = true) {
		SettingsPage(rememberNavController())
	}
}
