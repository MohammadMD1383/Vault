package ir.mmd.androidDev.vault.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.mmd.androidDev.vault.ui.theme.VaultTheme

@Composable
fun SearchField(
	searchTerm: MutableState<String>,
	onDismissRequest: () -> Unit
) {
	val focusRequester = remember { FocusRequester() }
	
	BackHandler(searchTerm.value.isNotEmpty()) {
		searchTerm.value = ""
	}
	
	OutlinedTextField(
		value = searchTerm.value,
		onValueChange = { searchTerm.value = it },
		placeholder = { Text("Search ...") },
		colors = OutlinedTextFieldDefaults.colors(
			unfocusedBorderColor = Color.Transparent,
			focusedBorderColor = Color.Transparent
		),
		leadingIcon = {
			AnimatedContent(targetState = searchTerm.value.isEmpty(), label = "Back & Clear") {
				if (it) {
					IconButton(onClick = onDismissRequest) {
						Icon(Icons.Rounded.ArrowBack, "Back")
					}
				} else {
					IconButton(onClick = { searchTerm.value = "" }) {
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
	val searchTerm = remember { mutableStateOf("") }
	VaultTheme(darkTheme = false) {
		Surface {
			SearchField(searchTerm) {}
		}
	}
}

@Preview
@Composable
private fun DarkPreview() {
	val searchTerm = remember { mutableStateOf("") }
	VaultTheme(darkTheme = true) {
		Surface {
			SearchField(searchTerm) {}
		}
	}
}