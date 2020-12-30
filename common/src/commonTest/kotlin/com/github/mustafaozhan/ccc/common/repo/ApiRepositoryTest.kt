/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.base.BaseRepositoryTest
import com.github.mustafaozhan.ccc.common.data.api.ApiRepository
import com.github.mustafaozhan.ccc.common.data.error.EmptyParameterException
import com.github.mustafaozhan.ccc.common.di.getDependency
import com.github.mustafaozhan.ccc.common.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ApiRepositoryTest : BaseRepositoryTest<ApiRepository>() {

    override val repository: ApiRepository by lazy {
        koin.getDependency(ApiRepository::class)
    }

    @Test
    fun getRatesByBaseViaApiParameterCanNotBeEmpty() = runTest {
        repository.getRatesByBaseViaApi("").execute({}, {
            assertTrue(it is EmptyParameterException)
        })
    }

    @Test
    fun getRatesByBaseViaBackendParameterCanNotBeEmpty() = runTest {
        repository.getRatesByBaseViaBackend("").execute({}, {
            assertTrue(it is EmptyParameterException)
        })
    }
}
