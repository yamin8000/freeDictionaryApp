package io.github.yamin8000.owl.search.ui.components.texts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ShortText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.HighlightText
import io.github.yamin8000.owl.common.ui.components.SpeakableRippleTextWithIcon
import io.github.yamin8000.owl.strings.R

@Composable
internal fun WordDefinitionText(
    modifier: Modifier = Modifier,
    word: String,
    definition: String,
    onDoubleClick: ((String) -> Unit)? = null
) {
    SpeakableRippleTextWithIcon(
        modifier = modifier,
        text = definition,
        imageVector = Icons.AutoMirrored.TwoTone.ShortText,
        title = stringResource(R.string.definition),
        content = { HighlightText(fullText = definition, highlightedText = word) },
        onDoubleClick = onDoubleClick,
    )
}