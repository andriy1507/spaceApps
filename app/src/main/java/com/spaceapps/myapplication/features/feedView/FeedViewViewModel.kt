package com.spaceapps.myapplication.features.feedView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.FEED_ID
import com.spaceapps.myapplication.models.remote.feeds.FeedFullResponse
import com.spaceapps.myapplication.network.FeedsApi
import com.spaceapps.myapplication.utils.launch
import com.spaceapps.myapplication.utils.request
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewViewModel @Inject constructor(
    private val feedsApi: FeedsApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        getFeed()
    }

    val feed = MutableLiveData<FeedFullResponse>()

    private val feedId = savedStateHandle.get<Int>(FEED_ID)
        ?: throw IllegalArgumentException("Feed ID couldn't be null")

    private fun getFeed() = launch {
        request { feedsApi.getFeedById(feedId) }
            .onSuccess { feed.postValue(it) }
            .onError { Timber.e(it) }
    }
}
