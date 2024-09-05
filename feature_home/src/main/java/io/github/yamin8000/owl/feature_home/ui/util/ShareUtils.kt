/*
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main
 *     ShareUtils.kt Copyrighted by Yamin Siahmargooei at 2024/8/19
 *     ShareUtils.kt Last modified at 2024/8/19
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_home.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_home.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_home.ui.util

import android.content.Context
import android.content.Intent
import io.github.yamin8000.owl.search.domain.model.Entry
import io.github.yamin8000.owl.strings.R

object ShareUtils {
    internal fun handleShareIntent(
        context: Context,
        entry: Entry
    ) {
        val text = createShareText(context, entry)

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    private fun createShareText(
        context: Context,
        entry: Entry
    ) = buildString {
        append("Word: ")
        append(entry.word)
        appendLine()
        append("Pronunciation(IPA): ")
        append(entry.phonetics.firstOrNull { it.text != null }?.text ?: "-")
        appendLine()
        appendLine()
        entry.meanings.forEachIndexed { index, (partOfSpeech, definitions, _, _) ->
            appendLine("${index + 1})")
            appendLine("Type: $partOfSpeech")
            definitions.take(5).forEach { (definition, example, synonyms, antonyms) ->
                appendLine("Definition: $definition")
                if (example != null)
                    appendLine("Example: $example")
                if (synonyms.isNotEmpty())
                    appendLine("Synonyms: ${synonyms.take(5).joinToString()}")
                if (antonyms.isNotEmpty())
                    appendLine("Antonyms: ${antonyms.take(5).joinToString()}")
                appendLine()
            }
            appendLine()
        }
        trim()
        appendLine(context.getString(R.string.this_text_generated_using_owl))
        appendLine(context.getString(R.string.github_source))
        appendLine(context.getString(R.string.this_text_extracted_from_free_dictionary))
        append(context.getString(R.string.free_dictionary_link))
    }
}