package com.spaceapps.myapplication.features.posts

sealed class PostsState
object PostsRequestError : PostsState()
object PostsRequestSuccess : PostsState()
