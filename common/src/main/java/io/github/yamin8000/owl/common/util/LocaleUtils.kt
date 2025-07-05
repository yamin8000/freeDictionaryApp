package io.github.yamin8000.owl.common.util

object LocaleUtils {

    fun isRtlChar(char: Char): Boolean = when (Character.getDirectionality(char)) {
        Character.DIRECTIONALITY_RIGHT_TO_LEFT,
        Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC,
        Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING,
        Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE,
            -> true

        else -> false
    }
}