package ir.mmd.androidDev.vault.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.mmd.androidDev.vault.R
import ir.mmd.androidDev.vault.ui.theme.VaultTheme
import ir.mmd.androidDev.vault.util.translate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> Select(
	text: String,
	options: List<T>,
	selectedOption: T,
	onSelectionChange: (T) -> Unit
) {
	var expanded by remember { mutableStateOf(false) }
	
	ExposedDropdownMenuBox(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)
	) {
		OutlinedTextField(
			readOnly = true,
			value = translate(selectedOption.toString()),
			onValueChange = {},
			label = { Text(text) },
			trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
			colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
			modifier = Modifier
				.menuAnchor()
				.fillMaxWidth()
		)
		
		ExposedDropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false }
		) {
			options.forEach {
				DropdownMenuItem(
					text = {
						Row(
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.SpaceBetween,
							modifier = Modifier.fillMaxWidth()
						) {
							Text(translate(it.toString()))
							
							if (selectedOption == it) {
								Icon(Icons.Rounded.Check, stringResource(R.string.text_selected_option))
							}
						}
					},
					onClick = {
						onSelectionChange(it)
						expanded = false
					}
				)
			}
		}
	}
}

@Preview
@Composable
private fun LightPreview() {
	VaultTheme(darkTheme = false) {
		Select("select", listOf(""), "") {}
	}
}

@Preview
@Composable
private fun DarkPreview() {
	VaultTheme(darkTheme = true) {
		Select("select", listOf(""), "") {}
	}
}