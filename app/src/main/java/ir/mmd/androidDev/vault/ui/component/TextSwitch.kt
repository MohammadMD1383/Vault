package ir.mmd.androidDev.vault.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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

@Composable
fun TextSwitch(
	text: String,
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true
) {
	val interactionSource = remember { MutableInteractionSource() }
	val textColor by animateColorAsState(MaterialTheme.colorScheme.onSurface.copy(if (enabled) 1f else 0.5f), label = "textColor")
	
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
		modifier = modifier.then(Modifier
			.clickable(interactionSource, indication = rememberRipple()) {
				if (enabled) onCheckedChange(!checked)
			}
			.padding(vertical = 12.dp, horizontal = 24.dp)
		)
	) {
		Text(
			text = text,
			color = textColor,
			modifier = Modifier.weight(1f, fill = false)
		)
		
		Spacer(modifier = Modifier.width(8.dp))
		
		Switch(
			checked = checked,
			onCheckedChange = null,
			enabled = enabled,
			thumbContent = {
				if (checked) {
					Icon(
						imageVector = Icons.Rounded.Check,
						contentDescription = stringResource(R.string.text_enabled),
						modifier = Modifier.size(SwitchDefaults.IconSize)
					)
				} else {
					Icon(
						imageVector = Icons.Rounded.Close,
						contentDescription = stringResource(R.string.text_disabled),
						modifier = Modifier.size(SwitchDefaults.IconSize)
					)
				}
			}
		)
	}
	
}

@Preview
@Composable
private fun LightPreview() {
	var b by remember { mutableStateOf(false) }
	
	VaultTheme(darkTheme = false) {
		Surface {
			TextSwitch("switch 1", b, { b = it }, Modifier.fillMaxWidth())
		}
	}
}

@Preview
@Composable
private fun DarkPreview() {
	var b by remember { mutableStateOf(false) }
	
	VaultTheme(darkTheme = true) {
		Surface {
			TextSwitch("switch 1", b, { b = it }, Modifier.fillMaxWidth())
		}
	}
}