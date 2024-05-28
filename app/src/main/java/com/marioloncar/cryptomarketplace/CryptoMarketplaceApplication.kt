@file:Suppress("UndocumentedPublicClass")

package com.marioloncar.cryptomarketplace

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.marioloncar.core.network.state.NetworkStatePublisher
import com.marioloncar.core.network.di.networkModule
import com.marioloncar.core.util.di.utilModule
import com.marioloncar.data.tickers.di.tickersDataModule
import com.marioloncar.feature.market.di.marketModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class CryptoMarketplaceApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // Crashlytics tree
        }

        startKoin {
            androidContext(this@CryptoMarketplaceApplication)
            modules(
                networkModule,
                utilModule,
                tickersDataModule,
                marketModule,
            )
        }

        initConnectivityManager()
    }

    private fun initConnectivityManager() {
        val networkStatePublisher = get<NetworkStatePublisher>()
        val connectivityManager: ConnectivityManager = getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val initial = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            ?: false

        networkStatePublisher.publishHasNetworkConnection(initial)

        connectivityManager.registerDefaultNetworkCallback(
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    networkStatePublisher.publishHasNetworkConnection(true)
                }

                override fun onLost(network: Network) {
                    networkStatePublisher.publishHasNetworkConnection(false)
                }

                override fun onUnavailable() {
                    networkStatePublisher.publishHasNetworkConnection(false)
                }
            }
        )
    }
}
