package com.marioloncar.core.network.di

import com.marioloncar.core.network.BuildConfig
import com.marioloncar.core.network.state.NetworkStatePublisher
import com.marioloncar.core.network.state.NetworkStatePublisherImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import timber.log.Timber

@ExperimentalSerializationApi
val networkModule = module {

    single<NetworkStatePublisher> { NetworkStatePublisherImpl() }

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }
    }

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(json = get())
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }

            install(Logging) {
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) = Timber.i(message)
                }
            }
        }
    }
}
