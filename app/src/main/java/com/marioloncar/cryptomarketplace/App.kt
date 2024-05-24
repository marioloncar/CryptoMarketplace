@file:Suppress("UndocumentedPublicClass")

package com.marioloncar.cryptomarketplace

import android.app.Application
import com.marioloncar.core.network.di.networkModule
import com.marioloncar.data.tickers.di.tickersDataModule
import com.marioloncar.feature.market.di.marketModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                tickersDataModule,
                marketModule,
            )
        }
    }
}
