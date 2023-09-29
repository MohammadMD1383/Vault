package ir.mmd.androidDev.vault.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ir.mmd.androidDev.vault.R

@Composable
fun translate(str: String): String {
	val resourceId = R.string::class.java.fields.find { it.name == "tr_$str" }?.get(null) as Int?
	return if (resourceId != null) stringResource(resourceId) else str
}