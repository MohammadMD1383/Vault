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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ir.mmd.androidDev.vault.model.AppSettings
import ir.mmd.androidDev.vault.ui.component.Select
import ir.mmd.androidDev.vault.ui.component.TextSwitch
import ir.mmd.androidDev.vault.ui.theme.Typography
import ir.mmd.androidDev.vault.ui.theme.VaultTheme

@Composable
fun SettingsPage(navController: NavController) {
	val context = LocalContext.current
	var authenticationEnabled by remember { mutableStateOf(AppSettings.authenticationEnabled) }
	var authenticationExpiresOnPause by remember { mutableStateOf(AppSettings.authenticationExpiresOnPause) }
	var closePreviewAfterCopy by remember { mutableStateOf(AppSettings.closePreviewAfterCopy) }
	var theme by remember { mutableStateOf(AppSettings.theme) }
	
	Column(Modifier.fillMaxSize()) {
		Text(
			text = stringResource(R.string.text_settings),
			style = Typography.headlineMedium,
			modifier = Modifier.padding(24.dp)
		)
		
		Select(
			text = stringResource(R.string.text_theme),
			options = AppSettings.Theme.entries,
			selectedOption = theme,
			onSelectionChange = { theme = it }
		)
		
		TextSwitch(
			text = stringResource(R.string.settings_enable_auth),
			checked = authenticationEnabled,
			onCheckedChange = { authenticationEnabled = it },
			modifier = Modifier.fillMaxWidth()
		)
		
		TextSwitch(
			text = stringResource(R.string.settings_enable_re_auth),
			checked = authenticationExpiresOnPause,
			onCheckedChange = { authenticationExpiresOnPause = it },
			enabled = authenticationEnabled,
			modifier = Modifier.fillMaxWidth()
		)
		
		TextSwitch(
			text = stringResource(R.string.settings_close_preview_after_copy),
			checked = closePreviewAfterCopy,
			onCheckedChange = { closePreviewAfterCopy = it },
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
					AppSettings.authenticationEnabled = authenticationEnabled
					AppSettings.authenticationExpiresOnPause = authenticationExpiresOnPause
					AppSettings.closePreviewAfterCopy = closePreviewAfterCopy
					AppSettings.theme = theme
					AppSettings.save(context)
					navController.popBackStack()
				}
			) {
				Text(stringResource(R.string.text_save))
			}
			
			OutlinedButton(
				modifier = Modifier.weight(1f),
				onClick = { navController.popBackStack() }
			) {
				Text(stringResource(R.string.text_cancel))
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
