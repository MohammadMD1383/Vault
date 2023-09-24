package ir.mmd.androidDev.vault.model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

object AppSettings {
	var authenticationEnabled by mutableStateOf(true)
	var authenticationExpiresOnPause by mutableStateOf(true)
	
	private var lastState: Model? = null
	
	private class Model(
		@JvmField val authenticationEnabled: Boolean,
		@JvmField val authenticationExpiresOnPause: Boolean
	) : Serializable {
		companion object {
			private const val serialVersionUID: Long = 1
		}
	}
	
	fun load(context: Context) {
		try {
			(ObjectInputStream(context.openFileInput(AppSettings::class.qualifiedName!!)).readObject() as Model).let {
				lastState = it
				authenticationEnabled = it.authenticationEnabled
				authenticationExpiresOnPause = it.authenticationExpiresOnPause
			}
		} catch (ignored: FileNotFoundException) {
		}
	}
	
	fun save(context: Context) {
		ObjectOutputStream(context.openFileOutput(AppSettings::class.qualifiedName!!, Context.MODE_PRIVATE)).writeObject(
			Model(
				authenticationEnabled,
				authenticationExpiresOnPause
			)
		)
	}
	
	fun reset(context: Context) {
		if (lastState == null) {
			load(context)
		} else {
			authenticationEnabled = lastState!!.authenticationEnabled
			authenticationExpiresOnPause = lastState!!.authenticationExpiresOnPause
		}
	}
}