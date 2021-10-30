package com.spaceapps.myapplication.features.notificationView

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.core.models.remote.notifications.NotificationFullResponse
import com.spaceapps.myapplication.core.repositories.notifications.NotificationsRepository
import com.spaceapps.myapplication.core.repositories.notifications.results.GetNotificationResult
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val repository: NotificationsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val notificationId = savedStateHandle.get<Int>("notificationId")
    val title = savedStateHandle.get<String>("title")
    private val _events = MutableSharedFlow<NotificationViewEvent>()
    val events: SharedFlow<NotificationViewEvent>
        get() = _events.asSharedFlow()
    val notification = savedStateHandle.getLiveData<NotificationFullResponse>("notification")

    init {
        getNotification()
    }

    private fun getNotification() = viewModelScope.launch {
        when (val result = repository.getNotificationById(notificationId!!)) {
            is GetNotificationResult.Success -> notification.postValue(result.notification)
            is GetNotificationResult.Error -> _events.emit(NotificationViewEvent.ShowSnackBar(R.string.unexpected_error))
        }
    }

    fun goBack() = navigationDispatcher.emit { it.navigateUp() }

    fun refresh() = getNotification()
}
