package com.spaceapps.myapplication.features.posts

sealed class PostsEvent

object InitState : PostsEvent()
object PostsRequestFailed : PostsEvent()
object PostsLoading : PostsEvent()
object NoMoreDataAvailable : PostsEvent()
class ShowError(msg: String) : PostsEvent()
class ShowInfo(msg: String) : PostsEvent()