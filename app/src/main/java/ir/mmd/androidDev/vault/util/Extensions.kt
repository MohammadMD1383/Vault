package ir.mmd.androidDev.vault.util

import android.annotation.SuppressLint
import android.util.LayoutDirection
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.text.layoutDirection
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import java.util.Locale

@Composable
fun PaddingValues.add(
	start: Dp = 0.dp,
	top: Dp = 0.dp,
	end: Dp = 0.dp,
	bottom: Dp = 0.dp
) = LocalLayoutDirection.current.let {
	PaddingValues(
		calculateStartPadding(it) + start,
		calculateTopPadding() + top,
		calculateEndPadding(it) + end,
		calculateBottomPadding() + bottom
	)
}

@SuppressLint("ComposableNaming")
@Composable
fun <T> NavController.onNavigationResult(vararg keys: String, block: (key: String, T) -> Unit) {
	val target = currentBackStackEntry!!.destination
	
	keys.forEach { key ->
		currentBackStackEntryAsState().value?.let { entry ->
			if (entry.destination == target) {
				entry.savedStateHandle.remove<T>(key)?.let {
					block(key, it)
				}
			}
		}
	}
}

@SuppressLint("ComposableNaming")
fun <T> NavController.returnWithNavigationResult(key: String, value: T) {
	previousBackStackEntry?.savedStateHandle?.set(key, value)
	popBackStack()
}

@Composable
fun <T> NavController.rememberNavigationData(key: String): T? {
	return remember { currentBackStackEntry?.savedStateHandle?.get(key) }
}

fun Modifier.autoMirror() = if (Locale.getDefault().layoutDirection == LayoutDirection.RTL) {
	scale(scaleX = -1f, scaleY = 1f)
} else this

fun NavController.navigateToHomePopWelcome() {
	navigate("home") {
		launchSingleTop = true
		popUpTo("welcome") {
			inclusive = true
		}
	}
}
