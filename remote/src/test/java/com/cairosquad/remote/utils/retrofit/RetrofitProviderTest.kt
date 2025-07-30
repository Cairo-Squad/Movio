package com.cairosquad.remote.utils.retrofit

import org.junit.Assert.assertNotNull
import org.junit.Test

class RetrofitProviderTest {

    @Test
    fun `provideRetrofit should return a Retrofit instance`() {

        val retrofit = retrofitProvider()

        assertNotNull(retrofit)
    }
}