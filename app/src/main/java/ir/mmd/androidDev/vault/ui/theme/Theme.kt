package ir.mmd.androidDev.vault.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import ir.mmd.androidDev.vault.model.AppSettings

private val darkColorScheme = darkColorScheme()
private val lightColorScheme = lightColorScheme()

@Composable
fun VaultTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	dynamicColor: Boolean = true,
	content: @Composable () -> Unit
) {
	val context = LocalContext.current
	val dynamic = dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
	val dark = if (dynamic) dynamicDarkColorScheme(context) else darkColorScheme
	val light = if (dynamic) dynamicLightColorScheme(context) else lightColorScheme
	val colorScheme = when (AppSettings.theme) {
		AppSettings.Theme.Dark -> dark
		AppSettings.Theme.Light -> light
		AppSettings.Theme.SystemDefault -> if (darkTheme) dark else light
	}
	
	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			window.statusBarColor = colorScheme.surface.toArgb()
			WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
		}
	}
	
	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}