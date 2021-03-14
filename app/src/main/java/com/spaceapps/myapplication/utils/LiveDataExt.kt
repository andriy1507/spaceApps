package com.spaceapps.myapplication.utils

import androidx.lifecycle.MutableLiveData

operator fun <T> MutableLiveData<List<T>>.plusAssign(newItems: List<T>) {
    val items = value?.toMutableList() ?: mutableListOf()
    items += newItems
    postValue(items)
}

operator fun <T> MutableLiveData<List<T>>.minusAssign(item: T) {
    val items = value?.toMutableList()
    items?.remove(item)
    postValue(items)
}

operator fun <T> MutableLiveData<List<T>>.plusAssign(item: T) {
    val items = value?.toMutableList() ?: mutableListOf()
    items.add(item)
    postValue(items)
}
