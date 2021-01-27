package com.spaceapps.myapplication.models

sealed class PostsState
object PostsRequestError : PostsState()
object PostsRequestSuccess : PostsState()