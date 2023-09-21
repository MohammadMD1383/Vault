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
fun ContentPage() {
	Box(modifier = Modifier.fillMaxSize(), Alignment.Center) {
		Text("CONTENT PAGE")
	}
}

@Preview
@Composable
private fun LightPreview() {
	VaultTheme(darkTheme = false) {
		ContentPage()
	}
}

@Preview
@Composable
private fun DarkPreview() {
	VaultTheme(darkTheme = true) {
		ContentPage()
	}
}
