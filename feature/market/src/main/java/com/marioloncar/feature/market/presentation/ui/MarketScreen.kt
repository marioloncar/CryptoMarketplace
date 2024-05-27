package com.marioloncar.feature.market.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
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
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Toolbar(
                    title = stringResource(uiState.title),
                    searchQuery = marketViewModel.searchQuery,
                    onQueryTextChange = {
                        marketViewModel.onActionInvoked(
                            MarketUiAction.OnSearchInput(it)
                        )
                    }
                )
            },
            content = { innerPadding ->
                ContentList(uiState.tickers, modifier = Modifier.padding(innerPadding))
            }
        )
    }
}

@Composable
fun Toolbar(
    title: String,
    searchQuery: String,
    onQueryTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        TopAppBar(
            title = { Text(text = title) },
            modifier = modifier
        )
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
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(horizontal = 12.dp)
            .border(1.dp, Color.LightGray, CircleShape),
        keyboardActions = KeyboardActions {
            focusManager.clearFocus()
        }
    )
}

@Composable
fun ContentList(tickersUiState: MarketUiState.Tickers, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        when (tickersUiState) {
            is MarketUiState.Tickers.Content -> {
                if (tickersUiState.tickers.isEmpty()) {
                    GenericMessage(text = stringResource(R.string.empty_list))
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(tickersUiState.tickers) {
                            TickerItem(tickerData = it)
                        }
                    }
                }
            }

            is MarketUiState.Tickers.Error -> {
                GenericMessage(text = "Error: ${stringResource(tickersUiState.message)}")
            }

            MarketUiState.Tickers.Loading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
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
            Text(text = text)

        }
    }
}

@Composable
fun TickerItem(tickerData: MarketUiState.TickerData, modifier: Modifier = Modifier) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = modifier.fillMaxWidth().heightIn(min = 100.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Text(
                    text = tickerData.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp),
                )
                if (tickerData.isEarnYield) {
                    YieldItem()
                    Spacer(modifier = Modifier.padding(4.dp))
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = tickerData.bid,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tickerData.dailyChange,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (tickerData.isEarnYield) Color.Green else Color.Red
                )
            }
        }
    }
}

@Composable
fun YieldItem(modifier: Modifier = Modifier) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceTint,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.earn_yield),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.inverseOnSurface,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MarketScreenPreview() {
    CryptoMarketplaceTheme {
        MarketScreen()
    }
}
