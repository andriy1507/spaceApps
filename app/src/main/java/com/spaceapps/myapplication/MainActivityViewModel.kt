package com.spaceapps.myapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {

    val bottomBarVisible = MutableLiveData(false)

    fun hideBottomBar() = bottomBarVisible.postValue(false)

    fun showBottomBar() = bottomBarVisible.postValue(true)
}
