package com.marioloncar.core.network

private const val DEBUG_URL = "https://api-pub.bitfinex.com/v2"
private const val RELEASE_URL = "https://api-pub.bitfinex.com/v2"

val BASE_URL = if (BuildConfig.DEBUG) DEBUG_URL else RELEASE_URL
