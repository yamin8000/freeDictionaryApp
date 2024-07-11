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
    val kotlinVersion = "2.0.0"
    id("com.android.application") version "8.5.0" apply false
    id("com.android.library") version "8.5.1" apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.compose") version kotlinVersion apply false
    id("com.google.devtools.ksp") version "$kotlinVersion-1.0.22"
}