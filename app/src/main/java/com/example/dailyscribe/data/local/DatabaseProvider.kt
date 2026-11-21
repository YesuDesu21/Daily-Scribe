package com.example.dailyscribe.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.dailyscribe.data.local.security.PassphraseManager
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = buildEncryptedDatabase(context.applicationContext)
            INSTANCE = instance
            instance
        }
    }

    private fun buildEncryptedDatabase(context: Context): AppDatabase {
        SQLiteDatabase.loadLibs(context)
        val passphrase = PassphraseManager(context).getOrGeneratePassphrase()
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "daily_scribe_encrypted.db"
        )
            .openHelperFactory(factory)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Insert seed data using SQL to avoid depending on INSTANCE being fully initialized
                    try {
                        db.beginTransaction()
                        val seedVerses = BibleSeeder.getInitialVerses()
                        for (verse in seedVerses) {
                            db.execSQL(
                                "INSERT OR REPLACE INTO bible_verses (id, translationId, book, chapter, verse, text) VALUES (?, ?, ?, ?, ?, ?)",
                                arrayOf<Any>(verse.id, verse.translationId, verse.book, verse.chapter, verse.verse, verse.text)
                            )
                        }
                        db.setTransactionSuccessful()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        try {
                            db.endTransaction()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            })
            .fallbackToDestructiveMigration()
            .build()
    }
}
