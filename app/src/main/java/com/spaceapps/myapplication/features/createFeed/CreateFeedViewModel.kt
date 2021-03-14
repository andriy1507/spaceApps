package com.spaceapps.myapplication.features.createFeed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.models.remote.feeds.FeedItemDto
import com.spaceapps.myapplication.models.remote.feeds.FeedRequest
import com.spaceapps.myapplication.network.FeedsApi
import com.spaceapps.myapplication.utils.NavDispatcher
import com.spaceapps.myapplication.utils.async
import com.spaceapps.myapplication.utils.request
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateFeedViewModel @Inject constructor(
    private val feedsApi: FeedsApi,
    private val navDispatcher: NavDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val title = savedStateHandle.getLiveData<String>("feedTitle")
    val text = savedStateHandle.getLiveData<String>("feedText")
    val events = MutableSharedFlow<CreateFeedEvent>()

    fun createFeed() = async {
        if (!isFeedValid()) return@async
        request {
            feedsApi.createFeed(
                FeedRequest(
                    title = title.value!!,
                    items = listOf(FeedItemDto(text.value!!))
                )
            )
        }.onSuccess { goBack() }.onError { Timber.e(it) }
    }

    fun onTitleEnter(input: String) = title.postValue(input)

    fun onTextEnter(input: String) = text.postValue(input)

    private suspend fun isFeedValid(): Boolean {
        val isTitleValid = !title.value.isNullOrBlank()
        val isTextValid = !title.value.isNullOrBlank()
        if (isTitleValid) events.emit(TitleInvalid)
        if (isTextValid) events.emit(TextInvalid)
        return isTitleValid && isTitleValid
    }

    private fun goBack() = navDispatcher.emit { popBackStack() }
}
