package io.github.yamin8000.owl.search.ui.components.texts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ShortText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.yamin8000.owl.common.ui.components.SpeakableRippleTextWithIcon

@Composable
internal fun WordText(
    modifier: Modifier = Modifier,
    word: String
) {
    SpeakableRippleTextWithIcon(
        modifier = modifier,
        text = word,
        imageVector = Icons.AutoMirrored.TwoTone.ShortText,
    )
}