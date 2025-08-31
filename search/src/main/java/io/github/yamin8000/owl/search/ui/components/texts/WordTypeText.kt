package io.github.yamin8000.owl.search.ui.components.texts

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Category
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.owl.common.ui.components.SpeakableRippleTextWithIcon
import io.github.yamin8000.owl.strings.R

@Composable
internal fun WordTypeText(
    modifier: Modifier = Modifier,
    type: String,
    onDoubleClick: ((String) -> Unit)? = null
) {
    SpeakableRippleTextWithIcon(
        modifier = modifier,
        text = type,
        imageVector = Icons.TwoTone.Category,
        title = stringResource(R.string.type),
        onDoubleClick = onDoubleClick,
    )
}