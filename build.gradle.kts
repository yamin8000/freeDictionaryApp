/*
 *     freeDictionaryApp/freeDictionaryApp
 *     build.gradle.kts Copyrighted by Yamin Siahmargooei at 2024/5/9
 *     build.gradle.kts Last modified at 2024/5/5
 *     This file is part of freeDictionaryApp/freeDictionaryApp.
 *     Copyright (C) 2024  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.compose.plugin) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.hilt) apply false
}

tasks.register("sortLibs") {
    doFirst {
        val tomlFile = File("$projectDir${File.separator}gradle${File.separator}libs.versions.toml")
        val lines = tomlFile.readLines()
        var versions = mutableListOf<String>()
        var libraries = mutableListOf<String>()
        var plugins = mutableListOf<String>()

        var isVersion = false
        var isLibrary = false
        var isPlugin = false
        for (line in lines) {
            if (line.isNotBlank()) {
                if (line == "[versions]") {
                    isVersion = true
                    isLibrary = false
                    isPlugin = false
                    continue
                }
                if (line == "[libraries]") {
                    isVersion = false
                    isLibrary = true
                    isPlugin = false
                    continue
                }
                if (line == "[plugins]") {
                    isVersion = false
                    isLibrary = false
                    isPlugin = true
                    continue
                }

                if (isVersion) {
                    versions.add(line)
                }
                if (isLibrary) {
                    libraries.add(line)
                }
                if (isPlugin) {
                    plugins.add(line)
                }
            }
        }

        versions = versions.sorted().toMutableList()
        libraries = libraries.sorted().toMutableList()
        plugins = plugins.sorted().toMutableList()

        tomlFile.writeText(
            buildString {
                appendLine("[versions]")
                versions.forEach { appendLine(it) }
                appendLine()
                appendLine("[libraries]")
                libraries.forEach { appendLine(it) }
                appendLine()
                appendLine("[plugins]")
                plugins.forEach { appendLine(it) }
            }.trim()
        )
    }
}