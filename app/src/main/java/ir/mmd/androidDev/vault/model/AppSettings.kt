package ir.mmd.androidDev.vault.model

import android.content.Context
import java.io.FileNotFoundException
import java.io.InvalidClassException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

class AppSettings private constructor() : Serializable {
	companion object {
		private const val ID = "ir.mmd.androidDev.vault.model.AppSettings"
		private const val serialVersionUID: Long = 1
		
		private lateinit var instance: AppSettings
		
		var authenticationEnabled: Boolean
			get() = instance.authenticationEnabled
			set(value) {
				instance.authenticationEnabled = value
			}
		
		var authenticationExpiresOnPause
			get() = instance.authenticationExpiresOnPause
			set(value) {
				instance.authenticationExpiresOnPause = value
			}
		
		var closePreviewAfterCopy
			get() = instance.closePreviewAfterCopy
			set(value) {
				instance.closePreviewAfterCopy = value
			}
		
		fun load(context: Context) {
			try {
				context.openFileInput(ID).use { file ->
					ObjectInputStream(file).use { stream ->
						instance = stream.readObject() as AppSettings
					}
				}
			} catch (e: Exception) {
				when (e) {
					is FileNotFoundException,
					is ClassNotFoundException,
					is InvalidClassException -> {
						instance = AppSettings()
						save(context)
					}
					
					else -> throw e
				}
			}
		}
		
		fun save(context: Context) {
			context.openFileOutput(ID, Context.MODE_PRIVATE).use { file ->
				ObjectOutputStream(file).use { stream ->
					stream.writeObject(instance)
				}
			}
		}
	}
	
	@JvmField
	var authenticationEnabled = false
	
	@JvmField
	var authenticationExpiresOnPause = false
	
	@JvmField
	var closePreviewAfterCopy = true
	
	private fun readObject(inputStream: ObjectInputStream) {
		inputStream.readFields().let {
			authenticationEnabled = it["authenticationEnabled", false]
			authenticationExpiresOnPause = it["authenticationExpiresOnPause", false]
			closePreviewAfterCopy = it["closePreviewAfterCopy", true]
		}
	}
}
