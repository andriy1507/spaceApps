package com.spaceapps.myapplication.repositories

import com.spaceapps.myapplication.local.CommentsDao
import com.spaceapps.myapplication.models.CommentEntity
import com.spaceapps.myapplication.models.CommentResponse
import com.spaceapps.myapplication.network.PostsApi
import com.spaceapps.myapplication.utils.EntityMapper
import javax.inject.Inject

class CommentsRepository @Inject constructor(
    private val api: PostsApi,
    private val dao: CommentsDao,
    private val mapper: EntityMapper<CommentResponse, CommentEntity>
) {

    fun getCommentsForPost(postId: Long) = dao.getCommentsByPostId(postId)

    suspend fun getComments(postId: Long) {
        api.getComments(postId).let { response ->
            dao.saveAll(*response.map { mapper.remoteToDomain(it) }.toTypedArray())
        }
    }

    suspend fun createComment(postId: Long, text: String) {
        api.addComment(postId, text)
    }

}