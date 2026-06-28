package io.github.yamin8000.owl.search.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val text_to_speech: ImageVector
    get() {
        if (_text_to_speech != null) {
            return _text_to_speech!!
        }
        _text_to_speech =
            ImageVector.Builder(
                name = "text_to_speech",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(4f, 22f)
                        quadTo(3.18f, 22f, 2.59f, 21.41f)
                        reflectiveQuadTo(2f, 20f)
                        verticalLineTo(4f)
                        quadTo(2f, 3.17f, 2.59f, 2.59f)
                        reflectiveQuadTo(4f, 2f)
                        horizontalLineToRelative(8.15f)
                        lineToRelative(-2f, 2f)
                        horizontalLineTo(4f)
                        verticalLineTo(20f)
                        horizontalLineTo(15f)
                        verticalLineTo(18f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(2f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(15f, 22f)
                        horizontalLineTo(4f)
                        close()
                        moveTo(6f, 18f)
                        verticalLineTo(16f)
                        horizontalLineToRelative(7f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(6f)
                        close()
                        moveTo(6f, 15f)
                        verticalLineTo(13f)
                        horizontalLineToRelative(5f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(6f)
                        close()
                        moveToRelative(9f, 0f)
                        lineTo(11f, 11f)
                        horizontalLineTo(8f)
                        verticalLineTo(6f)
                        horizontalLineToRelative(3f)
                        lineTo(15f, 2f)
                        verticalLineTo(15f)
                        close()
                        moveToRelative(2f, -3.05f)
                        verticalLineTo(5.05f)
                        quadToRelative(0.9f, 0.52f, 1.45f, 1.42f)
                        quadTo(19f, 7.38f, 19f, 8.5f)
                        reflectiveQuadToRelative(-0.55f, 2.02f)
                        quadTo(17.9f, 11.43f, 17f, 11.95f)
                        close()
                        moveToRelative(0f, 4.3f)
                        verticalLineToRelative(-2.1f)
                        quadToRelative(1.75f, -0.63f, 2.88f, -2.16f)
                        reflectiveQuadTo(21f, 8.5f)
                        reflectiveQuadTo(19.88f, 5.01f)
                        reflectiveQuadTo(17f, 2.85f)
                        verticalLineTo(0.75f)
                        quadToRelative(2.6f, 0.67f, 4.3f, 2.81f)
                        reflectiveQuadTo(23f, 8.5f)
                        reflectiveQuadToRelative(-1.7f, 4.94f)
                        reflectiveQuadTo(17f, 16.25f)
                        close()
                    }
                }
                .build()
        return _text_to_speech!!
    }

private var _text_to_speech: ImageVector? = null
