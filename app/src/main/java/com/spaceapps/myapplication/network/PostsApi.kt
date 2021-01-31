package com.spaceapps.myapplication.network

import com.spaceapps.myapplication.INIT_PAGE_NUMBER
import com.spaceapps.myapplication.PAGING_SIZE
import com.spaceapps.myapplication.models.*
import retrofit2.http.*

interface PostsApi {

    @GET("/posts")
    suspend fun getAllPosts(
        @Query("page") page: Int = INIT_PAGE_NUMBER,
        @Query("size") size: Int = PAGING_SIZE,
        @PostsSorting
        @Query("sort") sort: String = CREATED
    ): PaginationResponse<PostResponse>

    @GET("/posts/search")
    suspend fun searchPosts(
        @Query("query") query: String,
        @Query("page") page: Int = INIT_PAGE_NUMBER,
        @Query("size") size: Int = PAGING_SIZE,
        @PostsSorting
        @Query("sort") sort: String = CREATED
    ): PaginationResponse<PostSearchResponse>

//    @GET("/posts/{postId}")
//    suspend fun getPost(): PostResponse

    @POST("/posts")
    suspend fun createPost(@Body post: PostRequest): PostResponse

//    @PUT("/posts/{postId}")
//    suspend fun editPost(): PostResponse
//
//    @DELETE("/posts/{postId}")
//    suspend fun deletePost(): PostResponse

    @PUT("/posts/like/{postId}")
    suspend fun likePost(@Path("postId") id: Long): PostResponse

    @DELETE("/posts/like/{postId}")
    suspend fun unlikePost(@Path("postId") id: Long): PostResponse

    @POST("/posts/comment/{postId}")
    suspend fun addComment(
        @Path("postId") postId: Long,
        @Query("text") text: String
    )

    @GET("/posts/comments/{postId}")
    suspend fun getComments(@Path("postId") postId: Long): List<CommentResponse>
//
//    @DELETE("/posts/comment/{postId}")
//    suspend fun deleteComment()
}
