package com.spaceapps.myapplication.features.feedsList

import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.FEED_ID
import com.spaceapps.myapplication.PAGING_SIZE
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.models.remote.feeds.FeedFullResponse
import com.spaceapps.myapplication.models.remote.feeds.FeedShortResponse
import com.spaceapps.myapplication.network.FeedsApi
import com.spaceapps.myapplication.utils.NavDispatcher
import com.spaceapps.myapplication.utils.async
import com.spaceapps.myapplication.utils.plusAssign
import com.spaceapps.myapplication.utils.request
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedsListViewModel @Inject constructor(
    private val feedsApi: FeedsApi,
    private val navDispatcher: NavDispatcher
) : ViewModel() {

    private var page = 0
    private var isFetchAvailable = true
    private var isMoreDataAvailable = true
    val events = MutableSharedFlow<FeedsListEvent>()

    val feeds = MutableLiveData<List<FeedShortResponse>>()

    init {
        loadFeeds()
    }

    fun deleteFeed(feedId: Int) = async {
        request { feedsApi.deleteFeed(feedId = feedId) }
            .onSuccess {
                val newFeeds = feeds.value?.toMutableList()
                newFeeds?.removeIf { it.id == feedId }
                feeds.postValue(newFeeds.orEmpty())
            }
            .onError { Timber.e(it) }
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
        if (!isFetchAvailable or !isMoreDataAvailable) return@async
        events.emit(PaginationLoading)
        request {
            isFetchAvailable = false
            feedsApi.getFeedsPaginated(
                page = page,
                size = PAGING_SIZE
            )
        }.onSuccess {
            feeds += it.data
            isMoreDataAvailable = !it.isLast
            page++
            isFetchAvailable = true
        }.onError {
            isFetchAvailable = true
        }
        events.emit(InitialEvent)
    }
}
