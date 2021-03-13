package com.spaceapps.myapplication.features.feedsList

import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.FEED_ID
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.models.remote.feeds.FeedResponse
import com.spaceapps.myapplication.network.FeedsApi
import com.spaceapps.myapplication.utils.NavDispatcher
import com.spaceapps.myapplication.utils.async
import com.spaceapps.myapplication.utils.request
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedsListViewModel @Inject constructor(
    private val feedsApi: FeedsApi,
    private val navDispatcher: NavDispatcher
) : ViewModel() {

    val feeds = MutableLiveData<List<FeedResponse>>()

    init {
        loadFeeds()
    }

    fun goCreateFeed() = navDispatcher.emit { navigate(R.id.goCreateFeed) }

    fun toggleFeedLike(feedId: Int) = async {
        request { feedsApi.toggleFeedLike(feedId = feedId) }
    }

    fun goComments(feedId: Int) = navDispatcher.emit {
        navigate(
            R.id.goComments,
            bundleOf(FEED_ID to feedId)
        )
    }

    fun goFeedView(feedId: Int) =
        navDispatcher.emit { navigate(R.id.goFeedView, bundleOf(FEED_ID to feedId)) }

    fun loadFeeds() = async {
        request { feedsApi.getFeedsPaginated() }.onSuccess {
            feeds.postValue(it.data)
        }
    }


}
