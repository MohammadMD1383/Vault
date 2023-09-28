package ir.mmd.androidDev.vault.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ir.mmd.androidDev.vault.R
import ir.mmd.androidDev.vault.ui.theme.VaultTheme

@Composable
fun PreviewDialog(
	key: String,
	content: String,
	onDismissRequest: () -> Unit,
	onCopyRequest: () -> Unit,
	onEditRequest: () -> Unit,
	onDeleteRequest: () -> Unit
) = Dialog(
	onDismissRequest = onDismissRequest,
) {
	Surface(Modifier.clip(RoundedCornerShape(24.dp))) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(8.dp),
			modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp)
		) {
			Text(key, textAlign = TextAlign.Center)
			Divider()
			Text(content, textAlign = TextAlign.Center)
			Divider()
			Row {
				TextButton(
					onClick = onCopyRequest,
					modifier = Modifier.weight(1f)
				) {
					IconText(Icons.Rounded.ContentCopy, stringResource(R.string.text_copy))
				}
				
				TextButton(
					onClick = onEditRequest,
					modifier = Modifier.weight(1f)
				) {
					IconText(Icons.Rounded.Edit, stringResource(R.string.text_edit))
				}
				
				TextButton(
					onClick = onDeleteRequest,
					modifier = Modifier.weight(1f)
				) {
					IconText(Icons.Rounded.DeleteOutline, stringResource(R.string.text_delete))
				}
			}
		}
	}
}

@Preview
@Composable
private fun LightPreview() {
	VaultTheme(darkTheme = false) {
		Surface {
			PreviewDialog("key", "content", {}, {}, {}, {})
		}
	}
}

@Preview
@Composable
private fun DarkPreview() {
	VaultTheme(darkTheme = true) {
		Surface {
			PreviewDialog("key", "content", {}, {}, {}, {})
		}
	}
}