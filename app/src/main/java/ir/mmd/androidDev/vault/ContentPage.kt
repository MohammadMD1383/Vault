package ir.mmd.androidDev.vault

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import ir.mmd.androidDev.vault.ui.theme.Typography
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import ir.mmd.androidDev.vault.util.rememberNavigationData
import ir.mmd.androidDev.vault.util.returnWithNavigationResult

@Composable
fun ContentPage(navController: NavController) {
	val isEditMode = remember { navController.currentBackStackEntry?.arguments?.getString("mode") == "edit" }
	val model = navController.rememberNavigationData<Pair<String, String>>("data")
	var key by remember { mutableStateOf(if (isEditMode) model!!.first else "") }
	var content by remember { mutableStateOf(if (isEditMode) model!!.second else "") }
	var keyHasProblem by remember { mutableStateOf(false) }
	var contentHasProblem by remember { mutableStateOf(false) }
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		Text(
			text = if (isEditMode) "Edit Item" else "New Item",
			style = Typography.headlineMedium,
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp, 8.dp, 8.dp, 16.dp)
		)
		
		OutlinedTextField(
			value = key,
			onValueChange = {
				key = it
				keyHasProblem = it.isBlank()
			},
			leadingIcon = { Icon(Icons.Rounded.VpnKey, "Key") },
			trailingIcon = if (keyHasProblem) {
				{ Icon(Icons.Rounded.ErrorOutline, "Error", tint = MaterialTheme.colorScheme.error) }
			} else null,
			supportingText = if (keyHasProblem) {
				{ Text("Key cannot be empty") }
			} else null,
			label = { Text("Key") },
			readOnly = isEditMode,
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
			trailingIcon = if (contentHasProblem) {
				{ Icon(Icons.Rounded.ErrorOutline, "Error", tint = MaterialTheme.colorScheme.error) }
			} else null,
			supportingText = if (contentHasProblem) {
				{ Text("Content cannot be empty") }
			} else null,
			isError = contentHasProblem,
			singleLine = false,
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f)
		)
		
		Row(
			horizontalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 16.dp)
		) {
			Button(
				modifier = Modifier.weight(1f),
				onClick = {
					keyHasProblem = key.isBlank()
					contentHasProblem = content.isEmpty()
					
					if (keyHasProblem || contentHasProblem) {
						return@Button
					}
					
					navController.returnWithNavigationResult(if (isEditMode) "edit" else "new", key to content)
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
