package com.example.dailyscribe.data.local.security

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom

@Suppress("DEPRECATION")
class PassphraseManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "daily_scribe_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getOrGeneratePassphrase(): ByteArray {
        val existingPassphrase = sharedPreferences.getString(KEY_PASSPHRASE, null)
        return if (existingPassphrase != null) {
            Base64.decode(existingPassphrase, Base64.NO_WRAP)
        } else {
            val randomBytes = ByteArray(32)
            SecureRandom().nextBytes(randomBytes)
            val encoded = Base64.encodeToString(randomBytes, Base64.NO_WRAP)
            sharedPreferences.edit().putString(KEY_PASSPHRASE, encoded).apply()
            randomBytes
        }
    }

    companion object {
        private const val KEY_PASSPHRASE = "db_sqlcipher_passphrase"
    }
}
