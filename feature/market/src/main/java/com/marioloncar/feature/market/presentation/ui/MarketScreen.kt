@file:Suppress("TooManyFunctions", "UndocumentedPublicFunction")

package com.marioloncar.feature.market.presentation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marioloncar.core.ui.theme.CryptoMarketplaceTheme
import com.marioloncar.feature.market.R
import com.marioloncar.feature.market.presentation.MarketUiAction
import com.marioloncar.feature.market.presentation.MarketUiState
import com.marioloncar.feature.market.presentation.MarketViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MarketScreen(
    marketViewModel: MarketViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {

    val uiState by marketViewModel.uiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
        tonalElevation = 5.dp
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(
                    title = stringResource(uiState.title),
                    searchQuery = marketViewModel.searchQuery,
                    onQueryTextChange = {
                        marketViewModel.onActionInvoked(
                            MarketUiAction.OnSearchInput(searchQuery = it)
                        )
                    }
                )
            },
            content = { innerPadding ->
                ContentList(
                    tickersUiState = uiState.tickers,
                    modifier = Modifier.padding(paddingValues = innerPadding)
                )
            }
        )
    }
}

@Composable
fun TopBar(
    title: String,
    searchQuery: String,
    onQueryTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TopAppBar(title = { Text(text = title) })
        SearchBar(searchQuery = searchQuery, onQueryTextChange = onQueryTextChange)
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onQueryTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = searchQuery,
        onValueChange = { onQueryTextChange(it) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.spacing_xlarge))
            .clip(shape = CircleShape)
            .background(color = MaterialTheme.colorScheme.background),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(id = R.string.search),
                tint = MaterialTheme.colorScheme.outline
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onQueryTextChange("") }) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(id = R.string.clear),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.outline,
            disabledTextColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = stringResource(id = R.string.placeholder_search),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardActions = KeyboardActions {
            focusManager.clearFocus()
        }
    )
}

@Composable
fun ContentList(tickersUiState: MarketUiState.Tickers, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        when (tickersUiState) {
            is MarketUiState.Tickers.Content -> {
                if (tickersUiState.tickers.isEmpty()) {
                    GenericMessage(text = stringResource(R.string.empty_list))
                } else {
                    TickerList(tickersData = tickersUiState.tickers)
                }
            }

            is MarketUiState.Tickers.Error -> {
                GenericMessage(text = "Error: ${stringResource(tickersUiState.message)}")
            }

            MarketUiState.Tickers.Loading -> {
                ProgressIndicator()
            }
        }
    }
}

@Composable
private fun TickerList(tickersData: List<MarketUiState.TickerData>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(space = dimensionResource(R.dimen.spacing_large)),
        modifier = Modifier
            .fillMaxSize()
            .padding(all = dimensionResource(R.dimen.spacing_xlarge))
    ) {
        items(tickersData) {
            TickerItem(tickerData = it)
        }
    }
}

@Composable
fun ProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun GenericMessage(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Text(text = text, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun TickerItem(tickerData: MarketUiState.TickerData, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .padding(all = dimensionResource(R.dimen.spacing_large)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = cryptoImages.random()),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.padding(all = dimensionResource(R.dimen.spacing_medium)))
        Column {
            Text(
                text = tickerData.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacing_small)),
            )
            if (tickerData.isEarnYield) {
                YieldItem()
                Spacer(modifier = Modifier.padding(all = dimensionResource(R.dimen.spacing_small)))
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            FlippingBidContent(bid = tickerData.bid)
            DailyChange(dailyChange = tickerData.dailyChange, isEarnYield = tickerData.isEarnYield)
        }
    }
}

@Composable
private fun FlippingBidContent(bid: String) {
    AnimatedContent(
        targetState = bid,
        transitionSpec = {
            if (targetState > initialState) {
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            } else {
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> height } + fadeOut()
            }.using(
                SizeTransform(clip = false)
            )
        },
        label = "Flip bid"
    ) { animatedBid ->
        Text(
            text = animatedBid,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DailyChange(dailyChange: String, isEarnYield: Boolean) {
    Text(
        text = dailyChange,
        style = MaterialTheme.typography.labelMedium,
        color = if (isEarnYield) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.error
        }
    )
}

@Composable
fun YieldItem(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.earn_yield),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
            .padding(all = dimensionResource(R.dimen.spacing_small))
    )
}

private val cryptoImages = listOf(
    R.drawable.crypto_1,
    R.drawable.crypto_2,
    R.drawable.crypto_3
)

@Preview(showBackground = true)
@Composable
fun MarketScreenPreview() {
    CryptoMarketplaceTheme {
        MarketScreen()
    }
}
