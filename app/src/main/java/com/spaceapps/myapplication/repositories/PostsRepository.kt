package com.spaceapps.myapplication.repositories

import androidx.lifecycle.LiveData
import com.spaceapps.myapplication.INIT_PAGE_NUMBER
import com.spaceapps.myapplication.PAGING_SIZE
import com.spaceapps.myapplication.local.PostsDao
import com.spaceapps.myapplication.models.*
import com.spaceapps.myapplication.network.PostsApi
import com.spaceapps.myapplication.utils.EntityMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepository @Inject constructor(
    private val api: PostsApi,
    private val dao: PostsDao,
    private val mapper: EntityMapper<PostResponse, PostEntity>
) {

    val posts: LiveData<List<PostEntity>>
        get() = dao.getAllPosts()

    suspend fun loadPosts(
        page: Int = INIT_PAGE_NUMBER,
        size: Int = PAGING_SIZE,
        @PostsSorting
        sorting: String = CREATED
    ): PaginationResponse<PostResponse> {
        return api.getAllPosts(
            page = page,
            size = size,
            sort = sorting
        ).also { response ->
            dao.saveAll(*response.content.map(mapper::remoteToDomain).toTypedArray())
        }
    }

    suspend fun searchPosts(
        query: String,
        page: Int = 0,
        size: Int = 5,
        sorting: String = CREATED
    ): PaginationResponse<PostSearchResponse> {
        return api.searchPosts(
            query = query,
            page = page,
            size = size,
            sort = sorting
        )
    }

    suspend fun createPost(title: String, text: String) {
        val response = api.createPost(PostRequest(0, title, text))
        dao.save(mapper.remoteToDomain(response))
    }

    suspend fun likePost(postId: Long): PostEntity {
        return mapper.remoteToDomain(api.likePost(postId)).also { dao.save(it) }
    }

    suspend fun unlikePost(postId: Long): PostEntity {
        return mapper.remoteToDomain(api.unlikePost(postId)).also { dao.save(it) }
    }
}
