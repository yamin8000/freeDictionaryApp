package io.github.yamin8000.owl.search.ui.components.texts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.RecordVoiceOver
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.yamin8000.owl.common.ui.components.SpeakableRippleTextWithIcon

@Composable
internal fun PronunciationText(
    modifier: Modifier = Modifier,
    pronunciation: String,
    word: String,
) {
    SpeakableRippleTextWithIcon(
        modifier = modifier,
        text = pronunciation,
        ttsText = word,
        imageVector = Icons.TwoTone.RecordVoiceOver,
    )
}