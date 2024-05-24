@file:Suppress("UndocumentedPublicClass")

package com.marioloncar.cryptomarketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.marioloncar.core.ui.theme.CryptoMarketplaceTheme
import com.marioloncar.feature.users.presentation.ui.UsersScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoMarketplaceTheme {
                UsersScreen()
            }
        }
    }
}
