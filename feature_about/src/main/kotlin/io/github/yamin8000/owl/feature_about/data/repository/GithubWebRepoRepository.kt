/*
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main
 *     GithubWebRepoRepository.kt Copyrighted by Yamin Siahmargooei at 2025/11/16
 *     GithubWebRepoRepository.kt Last modified at 2025/11/16
 *     This file is part of freeDictionaryApp/freeDictionaryApp.feature_about.main.
 *     Copyright (C) 2025  Yamin Siahmargooei
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     freeDictionaryApp/freeDictionaryApp.feature_about.main is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with freeDictionaryApp.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.owl.feature_about.data.repository

import io.github.yamin8000.owl.feature_about.data.datasource.remote.GithubAPIs
import io.github.yamin8000.owl.feature_about.domain.Contributor
import io.github.yamin8000.owl.feature_about.domain.Release
import io.github.yamin8000.owl.feature_about.domain.Repository
import io.github.yamin8000.owl.feature_about.domain.repository.GithubRepoRepository

class GithubWebRepoRepository(
    private val api: GithubAPIs
) : GithubRepoRepository {

    override suspend fun getRepository(
        owner: String,
        repo: String
    ): Repository {
        return api.getRepository(owner, repo).domain()
    }

    override suspend fun getRepositoryContributors(
        owner: String,
        repo: String
    ): List<Contributor> {
        return api.repositoryContributors(owner, repo).map { it.domain() }
    }

    override suspend fun getReleases(
        owner: String,
        repo: String
    ): List<Release> {
        return api.releases(owner, repo).map { it.domain() }
    }
}