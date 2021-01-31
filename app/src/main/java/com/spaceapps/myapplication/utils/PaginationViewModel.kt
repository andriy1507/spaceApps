package com.spaceapps.myapplication.utils

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.network.PostsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * This is sample class for pagination.
 * It should not be used in real implementation,
 * but here has been shown
 * recommended way for pagination implementation.
 * */

@HiltViewModel
class PaginationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postsApi: PostsApi
) : ViewModel() {

    init {
        fetchData()
    }

    val list = savedStateHandle.getLiveData<List<String>>("dataList")

    private var isFetchAllowed = true
    private var isMoreDataAvailable = true
    private var page = 0

    fun fetchData() = async {
        if (!isFetchAllowed or !isMoreDataAvailable) return@async
        isFetchAllowed = false
        val response = request {
            postsApi.getAllPosts(
                page, PAGE_SIZE
            ).content
        }
        response.onError { sendRequestFailed(); return@async }
        response.onSuccess { data ->
            if (data.size < PAGE_SIZE) {
                isMoreDataAvailable = false
                return@async
            } else {
                val newData = list.value.orEmpty().toMutableList().apply {
                    addAll(data.map { it.toString() })
                }
                list.postValue(newData)
                page++
                isFetchAllowed = true
            }
        }
    }

    private fun sendRequestFailed() = Unit
}
