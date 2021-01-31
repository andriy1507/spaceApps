package com.spaceapps.myapplication.features.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.spaceapps.myapplication.INIT_LOAD_SIZE
import com.spaceapps.myapplication.INIT_PAGE_NUMBER
import com.spaceapps.myapplication.PAGING_SIZE
import com.spaceapps.myapplication.local.PostsDao
import com.spaceapps.myapplication.models.PostSearchResponse
import com.spaceapps.myapplication.network.PostsRemoteMediator
import com.spaceapps.myapplication.repositories.PostsRepository
import com.spaceapps.myapplication.utils.async
import com.spaceapps.myapplication.utils.request
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: PostsRepository,
    private val dao: PostsDao
) : ViewModel() {

    private var searchJob: Job? = null

    @OptIn(ExperimentalPagingApi::class)
    val postsFlow = Pager(
        config = PagingConfig(
            pageSize = PAGING_SIZE,
            initialLoadSize = INIT_LOAD_SIZE
        ),
        initialKey = INIT_PAGE_NUMBER,
        remoteMediator = PostsRemoteMediator(repository),
        pagingSourceFactory = { dao.getPostsDataSource() }
    ).flow.cachedIn(viewModelScope)

    val events = MutableSharedFlow<PostsEvent>()
    val query = savedStateHandle.getLiveData<String>("searchQuery")
    val posts = repository.posts
    val searchResults = MutableLiveData<List<PostSearchResponse>>()
    val canLoad: Boolean
        get() = isFetchAllowed && isMoreDataAvailable

    private var isFetchAllowed = true
    private var isMoreDataAvailable = true
//    private var page = 0
//    val itemsLoaded: Int
//        get() = page * PAGING_SIZE

//    init {
//        fetchData()
//    }

    fun searchPosts() {
        searchJob?.cancel()
        searchJob = async {
            if (query.value.isNullOrBlank())
                searchResults.postValue(null)
            else {
                request { repository.searchPosts(query.value!!) }.onSuccess {
                    searchResults.postValue(it.content)
                }
            }
        }
    }
//
//    fun fetchData() = async {
//        if (!isMoreDataAvailable) sendNoMoreData()
//        if (isFetchAllowed && isMoreDataAvailable) {
//            isFetchAllowed = false
//            sendPostsLoading()
//            val response = request { repository.loadPosts(page) }
//            response.onError { isFetchAllowed = true; sendRequestFailed(); return@async }
//            response.onSuccess { data ->
//                sendInitState()
//                if (data.content.size < PAGING_SIZE) {
//                    sendNoMoreData()
//                    isMoreDataAvailable = false
//                } else {
//                    page++
//                    isFetchAllowed = true
//                }
//            }
//        }
//    }

//    private suspend fun sendNoMoreData() = events.emit(NoMoreDataAvailable)
//
//    private suspend fun sendInitState() = events.emit(InitState)
//
//    private suspend fun sendPostsLoading() = events.emit(PostsLoading)
//
//    private suspend fun sendRequestFailed() = events.emit(PostsRequestFailed)
}
