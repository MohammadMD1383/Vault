package ir.mmd.androidDev.vault

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ir.mmd.androidDev.vault.ui.theme.Typography
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import ir.mmd.androidDev.vault.util.rememberNavigationData

@Composable
fun ContentPage(navController: NavController) {
	val isEditMode = remember { navController.currentBackStackEntry?.arguments?.getString("mode") == "edit" }
	val model = navController.rememberNavigationData<Pair<String, String>>("data")
	var key by remember { mutableStateOf(if (isEditMode) model!!.first else "") }
	var content by remember { mutableStateOf(if (isEditMode) model!!.second else "") }
	var keyHasProblem by remember { mutableStateOf(false) }
	var contentHasProblem by remember { mutableStateOf(false) }
	var visible by remember { mutableStateOf(false) }
	
	LaunchedEffect(Unit) {
		visible = true
	}
	
	BackHandler {
		visible = false
	}
	
	Box(
		contentAlignment = Alignment.BottomCenter,
		modifier = Modifier
			.fillMaxSize()
			.statusBarsPadding()
			.navigationBarsPadding()
			.imePadding()
	) {
		AnimatedVisibility(
			visible = visible,
			enter = slideInVertically { it },
			exit = slideOutVertically { it }
		) {
			DisposableEffect(Unit) {
				onDispose { navController.popBackStack() }
			}
			
			Surface(Modifier.clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))) {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					modifier = Modifier
						.fillMaxWidth()
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
							.weight(1f, fill = false)
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
								
								navController.previousBackStackEntry?.savedStateHandle?.set(if (isEditMode) "edit" else "new", key to content)
								visible = false
							}
						) {
							Text("Save")
						}
						
						OutlinedButton(
							modifier = Modifier.weight(1f),
							onClick = { visible = false }
						) {
							Text("Cancel")
						}
					}
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
