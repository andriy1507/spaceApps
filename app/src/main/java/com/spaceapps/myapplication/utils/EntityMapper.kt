package com.spaceapps.myapplication.utils

import com.spaceapps.myapplication.models.CommentEntity
import com.spaceapps.myapplication.models.CommentResponse
import com.spaceapps.myapplication.models.PostEntity
import com.spaceapps.myapplication.models.PostResponse
import javax.inject.Inject
import javax.inject.Singleton

interface EntityMapper<R, E> {

    fun remoteToDomain(e: R): E

    fun domainToRemote(d: E): R

}

@Singleton
class PostEntityMapper @Inject constructor() : EntityMapper<PostResponse, PostEntity> {
    override fun remoteToDomain(e: PostResponse): PostEntity {
        return PostEntity(
            id = e.id,
            title = e.title,
            text = e.text,
            created = e.created,
            isLiked = e.isLiked,
            likesCount = e.likesCount,
            commentsCount = e.commentsCount
        )
    }

    override fun domainToRemote(d: PostEntity): PostResponse {
        return PostResponse(
            id = d.id,
            title = d.title,
            text = d.text,
            created = d.created,
            isLiked = d.isLiked,
            likesCount = d.likesCount,
            commentsCount = d.commentsCount
        )
    }
}

@Singleton
class CommentEntityMapper @Inject constructor() : EntityMapper<CommentResponse, CommentEntity> {
    override fun remoteToDomain(e: CommentResponse): CommentEntity {
        return CommentEntity(
            id = e.id,
            text = e.text,
            postId = e.postId,
            userId = e.userId
        )
    }

    override fun domainToRemote(d: CommentEntity): CommentResponse {
        return CommentResponse(
            id = d.id,
            postId = d.postId,
            userId = d.userId,
            text = d.text
        )
    }
}