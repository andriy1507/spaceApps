package com.spaceapps.myapplication.features.feedComments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.FEED_ID
import com.spaceapps.myapplication.network.FeedsApi
import com.spaceapps.myapplication.utils.launch
import com.spaceapps.myapplication.utils.request
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedCommentsViewModel @Inject constructor(
    private val feedsApi: FeedsApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        getComments()
    }

    private val feedId = savedStateHandle.get<Int>(FEED_ID)
        ?: throw IllegalArgumentException("Feed ID couldn't be null")

    private fun getComments() = launch {
        request { feedsApi.getCommentsPaginated(feedId = feedId) }
    }
}
