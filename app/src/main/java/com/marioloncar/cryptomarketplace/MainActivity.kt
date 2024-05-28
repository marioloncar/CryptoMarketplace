@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")

package com.marioloncar.cryptomarketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.marioloncar.core.ui.theme.CryptoMarketplaceTheme
import com.marioloncar.feature.market.presentation.ui.MarketScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoMarketplaceTheme {
                MarketScreen()
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun TickerItemPreview() {
    CryptoMarketplaceTheme { MarketScreen() }
}
