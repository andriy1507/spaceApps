package com.spaceapps.myapplication.features.createFeed

import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.network.FeedsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateFeedViewModel @Inject constructor(
    private val feedsApi: FeedsApi
) : ViewModel()
