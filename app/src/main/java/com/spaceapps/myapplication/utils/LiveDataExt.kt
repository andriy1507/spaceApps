package com.spaceapps.myapplication.utils

import androidx.lifecycle.MutableLiveData

operator fun <T> MutableLiveData<Collection<T>>.plusAssign(newItems: List<T>) {
    val items = value?.toMutableList() ?: mutableListOf()
    items += newItems
    postValue(items)
}

operator fun <T> MutableLiveData<Collection<T>>.minusAssign(item: T) {
    val items = value?.toMutableList()
    items?.remove(item)
    postValue(items)
}

operator fun <T> MutableLiveData<Collection<T>>.plusAssign(item: T) {
    val items = value?.toMutableList() ?: mutableListOf()
    items.add(item)
    postValue(items)
}

operator fun <T> MutableLiveData<Collection<T>>.set(index: Int, item: T) {
    val items = value.orEmpty().toMutableList()
    if (items.lastIndex < index) items.add(item) else items[index] = item
    postValue(items)
}

operator fun <T> MutableLiveData<Collection<T>>.set(index: Int, newItems: Collection<T>) {
    val items = value.orEmpty().toMutableList()
    if (items.lastIndex < index) items.addAll(newItems) else items.addAll(index, newItems)
    postValue(items)
}

operator fun <T> MutableLiveData<List<T>>.get(index: Int): T? {
    val items = value ?: return null
    if (items.lastIndex < index) return null
    return items[index]
}
