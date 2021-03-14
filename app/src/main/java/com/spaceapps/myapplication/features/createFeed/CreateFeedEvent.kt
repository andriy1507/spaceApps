package com.spaceapps.myapplication.features.createFeed

sealed class CreateFeedEvent
object TitleInvalid : CreateFeedEvent()
object TextInvalid : CreateFeedEvent()