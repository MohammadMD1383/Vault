package ir.mmd.androidDev.vault

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ir.mmd.androidDev.vault.ui.theme.VaultTheme

@Composable
fun SettingsPage() {
	Box(Modifier.fillMaxSize(), Alignment.Center) {
		Text("SETTINGS PAGE")
	}
}

@Preview
@Composable
private fun LightPreview() {
	VaultTheme(darkTheme = false) {
		SettingsPage()
	}
}

@Preview
@Composable
private fun DarkPreview() {
	VaultTheme(darkTheme = true) {
		SettingsPage()
	}
}
