package ir.mmd.androidDev.vault.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ir.mmd.androidDev.vault.R
import ir.mmd.androidDev.vault.ui.theme.VaultTheme

@Composable
fun ConfirmDialog(
	text: String,
	onConfirm: () -> Unit,
	onDismissRequest: () -> Unit
) = Dialog(
	onDismissRequest = onDismissRequest
) {
	Surface(Modifier.clip(RoundedCornerShape(24.dp))) {
		Column(
			verticalArrangement = Arrangement.spacedBy(24.dp),
			modifier = Modifier.padding(24.dp, 24.dp, 24.dp, 8.dp)
		) {
			Text(text)
			
			Row {
				TextButton(
					onClick = onConfirm,
					modifier = Modifier.weight(1f)
				) {
					Text(stringResource(R.string.text_okay))
				}
				
				TextButton(
					onClick = onDismissRequest,
					modifier = Modifier.weight(1f)
				) {
					Text(stringResource(R.string.text_cancel))
				}
			}
		}
	}
}

@Preview
@Composable
private fun LightPreview() {
	VaultTheme(darkTheme = false) {
		ConfirmDialog("test dialog", {}) {}
	}
}

@Preview
@Composable
private fun DarkPreview() {
	VaultTheme(darkTheme = true) {
		ConfirmDialog("test dialog", {}) {}
	}
}