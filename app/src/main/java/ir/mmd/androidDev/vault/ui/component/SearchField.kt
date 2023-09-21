package ir.mmd.androidDev.vault.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
	searchTerm: MutableState<String>,
	onDismissRequest: () -> Unit
) {
	val focusRequester = remember { FocusRequester() }
	var dropDownMenuExpanded by remember { mutableStateOf(false) }
	var searchByKey by remember { mutableStateOf(true) }
	
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
		trailingIcon = {
			Box {
				IconButton(onClick = { dropDownMenuExpanded = true }) {
					AnimatedContent(targetState = searchByKey, label = "Key & Content") {
						if (it) {
							Icon(Icons.Rounded.VpnKey, "Key")
						} else {
							Icon(Icons.Rounded.Notes, "Content")
						}
					}
				}
				
				DropdownMenu(
					expanded = dropDownMenuExpanded,
					onDismissRequest = { dropDownMenuExpanded = false }
				) {
					Text("Search in:", Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp))
					
					DropdownMenuItem(
						text = { IconText(Icons.Rounded.VpnKey, "Key") },
						onClick = {
							dropDownMenuExpanded = false
							searchByKey = true
						}
					)
					
					DropdownMenuItem(
						text = { IconText(Icons.Rounded.Notes, "Content") },
						onClick = {
							dropDownMenuExpanded = false
							searchByKey = false
						}
					)
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