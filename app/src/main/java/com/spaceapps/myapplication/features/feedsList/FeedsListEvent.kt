package com.spaceapps.myapplication.features.feedsList

sealed class FeedsListEvent
object InitialEvent : FeedsListEvent()
object PaginationLoading : FeedsListEvent()
