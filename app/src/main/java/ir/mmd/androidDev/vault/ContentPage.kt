package ir.mmd.androidDev.vault

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ir.mmd.androidDev.vault.ui.theme.VaultTheme

@Composable
fun ContentPage(navController: NavController) {
	var key by remember { mutableStateOf("") }
	var content by remember { mutableStateOf("") }
	var keyHasProblem by remember { mutableStateOf(false) }
	var contentHasProblem by remember { mutableStateOf(false) }
	
	Surface {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
		) {
			OutlinedTextField(
				value = key,
				onValueChange = {
					key = it
					keyHasProblem = it.isBlank()
				},
				leadingIcon = { Icon(Icons.Rounded.VpnKey, "Key") },
				label = { Text("Key") },
				isError = keyHasProblem,
				singleLine = true,
				modifier = Modifier.fillMaxWidth()
			)
			
			OutlinedTextField(
				value = content,
				onValueChange = {
					content = it
					contentHasProblem = it.isEmpty()
				},
				label = { Text("Content") },
				isError = contentHasProblem,
				singleLine = false,
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f)
			)
			
			Row(
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 8.dp)
			) {
				Button(
					modifier = Modifier.weight(1f),
					onClick = {
						if (keyHasProblem || contentHasProblem) {
							return@Button
						}
						
						navController.previousBackStackEntry?.savedStateHandle?.set("new", key to content)
						navController.popBackStack()
					}
				) {
					Text("Save")
				}
				
				OutlinedButton(
					modifier = Modifier.weight(1f),
					onClick = { navController.popBackStack() }
				) {
					Text("Cancel")
				}
			}
		}
	}
}

@Preview
@Composable
private fun LightPreview() {
	VaultTheme(darkTheme = false) {
		ContentPage(rememberNavController())
	}
}

@Preview
@Composable
private fun DarkPreview() {
	VaultTheme(darkTheme = true) {
		ContentPage(rememberNavController())
	}
}
