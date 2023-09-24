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
	private const val ID = "ir.mmd.androidDev.vault.model.AppSettings"
	
	var authenticationEnabled by mutableStateOf(false)
	var authenticationExpiresOnPause by mutableStateOf(false)
	
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
			(ObjectInputStream(context.openFileInput(ID)).readObject() as Model).let {
				authenticationEnabled = it.authenticationEnabled
				authenticationExpiresOnPause = it.authenticationExpiresOnPause
			}
		} catch (ignored: FileNotFoundException) {
		}
	}
	
	fun save(context: Context) {
		ObjectOutputStream(context.openFileOutput(ID, Context.MODE_PRIVATE)).writeObject(
			Model(
				authenticationEnabled,
				authenticationExpiresOnPause
			)
		)
	}
}