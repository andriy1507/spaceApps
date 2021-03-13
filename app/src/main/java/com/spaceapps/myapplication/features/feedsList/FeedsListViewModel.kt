package com.spaceapps.myapplication.features.feedsList

import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.network.FeedsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedsListViewModel @Inject constructor(
    private val feedsApi: FeedsApi
) : ViewModel()
