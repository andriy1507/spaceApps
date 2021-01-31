package com.spaceapps.myapplication.di

import com.spaceapps.myapplication.models.CommentEntity
import com.spaceapps.myapplication.models.CommentResponse
import com.spaceapps.myapplication.models.PostEntity
import com.spaceapps.myapplication.models.PostResponse
import com.spaceapps.myapplication.utils.CommentEntityMapper
import com.spaceapps.myapplication.utils.EntityMapper
import com.spaceapps.myapplication.utils.PostEntityMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MappersModule {

    @Binds
    fun bindPostMapper(impl: PostEntityMapper): EntityMapper<PostResponse, PostEntity>

    @Binds
    fun bindCommentMapper(impl: CommentEntityMapper): EntityMapper<CommentResponse, CommentEntity>
}
