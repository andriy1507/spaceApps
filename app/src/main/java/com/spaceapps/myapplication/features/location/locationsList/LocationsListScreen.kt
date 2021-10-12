package com.spaceapps.myapplication.features.location.locationsList

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun LocationsListScreen(vm: LocationsListViewModel) {
    val pagingData by vm.locations.collectAsState(initial = emptyFlow())
    val locations = pagingData.collectAsLazyPagingItems()
    val searchQuery by vm.searchQuery.observeAsState()
    LazyColumn {
        item {
            TextField(value = searchQuery.orEmpty(), onValueChange = vm::onSearchQueryEnter)
        }
        items(locations) {
            Text(text = it?.name.orEmpty())
        }
    }
}
