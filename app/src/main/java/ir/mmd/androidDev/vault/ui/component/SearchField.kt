package ir.mmd.androidDev.vault.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.mmd.androidDev.vault.ui.theme.VaultTheme

@Composable
fun SearchField(
	value: String,
	onValueChange: (String) -> Unit,
	onDismissRequest: () -> Unit
) {
	val focusRequester = remember { FocusRequester() }
	
	BackHandler(value.isNotEmpty()) {
		onValueChange("")
	}
	
	OutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		placeholder = { Text("Search ...") },
		colors = OutlinedTextFieldDefaults.colors(
			unfocusedBorderColor = Color.Transparent,
			focusedBorderColor = Color.Transparent
		),
		leadingIcon = {
			AnimatedContent(
				targetState = value.isEmpty(),
				label = "Back & Clear",
				transitionSpec = { scaleIn() togetherWith scaleOut() }
			) {
				if (it) {
					IconButton(onClick = onDismissRequest) {
						Icon(Icons.Rounded.ArrowBack, "Back")
					}
				} else {
					IconButton(onClick = { onValueChange("") }) {
						Icon(Icons.Rounded.Clear, "Clear")
					}
				}
			}
		},
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp)
			.focusRequester(focusRequester)
	)
	
	LaunchedEffect(Unit) {
		focusRequester.requestFocus()
	}
}

@Preview
@Composable
private fun LightPreview() {
	var searchTerm by remember { mutableStateOf("") }
	VaultTheme(darkTheme = false) {
		Surface {
			SearchField(searchTerm, { searchTerm = it }, {})
		}
	}
}

@Preview
@Composable
private fun DarkPreview() {
	var searchTerm by remember { mutableStateOf("") }
	VaultTheme(darkTheme = true) {
		Surface {
			SearchField(searchTerm, { searchTerm = it }, {})
		}
	}
}