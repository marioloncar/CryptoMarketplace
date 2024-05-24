@file:Suppress("UndocumentedPublicClass")

package com.marioloncar.cryptomarketplace

import android.app.Application
import com.marioloncar.data.users.di.usersDataModule
import com.marioloncar.feature.users.di.usersModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                usersDataModule,
                usersModule,
            )
        }
    }
}
