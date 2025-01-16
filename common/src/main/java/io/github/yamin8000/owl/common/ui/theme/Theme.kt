/*
 *     freeDictionaryApp/freeDictionaryApp.common.main
 *     Theme.kt Copyrighted by Yamin Siahmargooei at 2024/8/18
 *     Theme.kt Last modified at 2024/8/17
 *     This file is part of freeDictionaryApp/freeDictionaryApp.common.main.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.common.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.common.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import kotlin.random.Random

val lightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    inversePrimary = md_theme_light_inversePrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    tertiary = md_theme_light_tertiary,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    surfaceTint = md_theme_light_surfaceTint,
    inverseSurface = md_theme_light_inverseSurface,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onErrorContainer = md_theme_light_onErrorContainer,
    outline = md_theme_light_outline,
)


val darkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
)

@Composable
fun PreviewTheme(
    isDarkTheme: Boolean = Random.nextBoolean(),
    isOledTheme: Boolean = Random.nextBoolean(),
    isDynamicColor: Boolean = Random.nextBoolean(),
    content: @Composable () -> Unit
) {
    Column(
        content = {
            Text(
                text = "isDark $isDarkTheme"
            )
            Text(
                text = "isOledTheme $isOledTheme"
            )
            Text(
                text = "isDynamicColor $isDynamicColor"
            )
            Box(
                modifier = Modifier
                    .height(Sizes.Large)
                    .fillMaxWidth()
                    .background(Color.Red)
            )
            AppTheme(
                isDarkTheme,
                isOledTheme,
                true,
                isDynamicColor,
                content
            )
        }
    )
}

@Preview(
    showBackground = true,
)
annotation class MyPreview

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isOledTheme: Boolean = false,
    isPreviewing: Boolean = false,
    isDynamicColor: Boolean,
    content: @Composable () -> Unit
) {
    val isDynamicColorReadyDevice = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    var colors = when {
        isDynamicColorReadyDevice && isDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        isDynamicColorReadyDevice && !isDarkTheme -> dynamicLightColorScheme(LocalContext.current)
        isDarkTheme -> darkColors
        else -> lightColors
    }

    if (isDarkTheme && isOledTheme) {
        colors = colors.copy(
            onPrimary = colors.onPrimary.darken(),
            primaryContainer = colors.primaryContainer.darken(),
            onSecondary = colors.onSecondary.darken(),
            secondaryContainer = colors.secondaryContainer.darken(),
            onTertiary = colors.onTertiary.darken(),
            tertiaryContainer = colors.tertiaryContainer.darken(),
            background = Color(0xFF000000),
            surface = Color(0xFF000000),
            surfaceVariant = Color(0xFF000000),
            inverseOnSurface = Color(0xFF000000),
            inversePrimary = colors.inversePrimary.darken(),
            surfaceTint = colors.surfaceTint.darken(),
        )
    }

    if (!isPreviewing) {
        val activity = LocalView.current.context as Activity
        SideEffect {
            val wic = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            wic.isAppearanceLightStatusBars = !isDarkTheme
            wic.isAppearanceLightNavigationBars = !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

private fun Color.darken(): Color {
    return copy(alpha, red / 5, green / 5, blue / 5)
}

@Suppress("unused")
private fun Color.lighten(): Color {
    return copy(alpha, red + (1f - red) / 2, green + (1f - green) / 2, blue + (1f - blue) / 2)
}