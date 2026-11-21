package com.example.dailyscribe.presentation.journal

import android.content.Context
import android.content.Intent
import com.example.dailyscribe.domain.model.JournalEntry

enum class ShareMode {
    VERBATIM,
    AI_SUMMARY
}

object SocialShareHelper {

    fun shareEntry(
        context: Context,
        entry: JournalEntry,
        mode: ShareMode = ShareMode.VERBATIM
    ) {
        val shareText = when (mode) {
            ShareMode.VERBATIM -> buildVerbatimShareText(entry)
            ShareMode.AI_SUMMARY -> buildAiSummaryShareText(entry)
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, entry.title)
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        val chooserIntent = Intent.createChooser(sendIntent, "Share Journal Entry")
        context.startActivity(chooserIntent)
    }

    private fun buildVerbatimShareText(entry: JournalEntry): String {
        return buildString {
            append("📜 ").append(entry.title).append("\n\n")
            append(entry.content).append("\n\n")

            if (!entry.scriptureRef.isNull_or_blank_safe()) {
                append("📖 Scripture: ").append(entry.scriptureRef).append("\n")
            }

            if (entry.tags.isNotEmpty()) {
                val hashtags = entry.tags.joinToString(" ") { "#${it.replace(" ", "")}" }
                append("🏷️ ").append(hashtags).append("\n")
            }

            append("\n— Shared via Daily Scribe")
        }
    }

    private fun buildAiSummaryShareText(entry: JournalEntry): String {
        val snippet = if (entry.content.length > 160) {
            entry.content.take(160).trim() + "..."
        } else {
            entry.content.trim()
        }

        return buildString {
            append("✨ Daily Reflection: ").append(entry.title).append("\n\n")
            append("Key Thought: \"").append(snippet).append("\"\n\n")

            if (!entry.scriptureRef.isNull_or_blank_safe()) {
                append("📖 Scripture: ").append(entry.scriptureRef).append("\n\n")
            }

            append("— Shared via Daily Scribe")
        }
    }

    private fun String?.isNull_or_blank_safe(): Boolean {
        return this == null || this.trim().isEmpty()
    }
}
