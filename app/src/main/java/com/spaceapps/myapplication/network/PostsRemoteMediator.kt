package com.spaceapps.myapplication.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.spaceapps.myapplication.INIT_PAGE_NUMBER
import com.spaceapps.myapplication.local.PostsDao
import com.spaceapps.myapplication.models.PostEntity
import com.spaceapps.myapplication.models.PostResponse
import com.spaceapps.myapplication.repositories.PostsRepository
import com.spaceapps.myapplication.utils.EntityMapper
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostsRemoteMediator (
    private val repository: PostsRepository
) : RemoteMediator<Int, PostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> INIT_PAGE_NUMBER
            LoadType.PREPEND -> state.anchorPosition?.minus(1) ?: INIT_PAGE_NUMBER
            LoadType.APPEND -> state.anchorPosition ?: INIT_PAGE_NUMBER
        }
        val size =
            if (page == INIT_PAGE_NUMBER) state.config.initialLoadSize else state.config.pageSize
        return when (val response = request { repository.loadPosts(page = page, size = size) }) {
            is Success -> {
                val end = response.data.content.size < state.config.pageSize
                MediatorResult.Success(endOfPaginationReached = end)
            }
            is Error -> MediatorResult.Error(response.error)
        }
    }
}