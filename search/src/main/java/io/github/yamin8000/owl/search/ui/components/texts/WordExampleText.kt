package io.github.yamin8000.owl.search.ui.components.texts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.TextSnippet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.HighlightText
import io.github.yamin8000.owl.common.ui.components.SpeakableRippleTextWithIcon
import io.github.yamin8000.owl.strings.R

@Composable
internal fun WordExampleText(
    modifier: Modifier = Modifier,
    word: String,
    example: String,
    onDoubleClick: ((String) -> Unit)? = null
) {
    SpeakableRippleTextWithIcon(
        modifier = modifier,
        text = example,
        imageVector = Icons.AutoMirrored.TwoTone.TextSnippet,
        title = stringResource(R.string.example),
        content = { HighlightText(fullText = example, highlightedText = word) },
        onDoubleClick = onDoubleClick,
    )
}