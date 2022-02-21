package com.spaceapps.myapplication.features.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.navigation.Screens.NotificationView
import com.spaceapps.myapplication.core.repositories.notifications.NotificationsRepository
import com.spaceapps.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val navigator: Navigator,
    private val repository: NotificationsRepository
) : ViewModel() {

    private val _events = MutableSharedFlow<NotificationsEvent>()
    val events: SharedFlow<NotificationsEvent>
        get() = _events.asSharedFlow()
    val notifications = repository.getNotifications().flow

    fun goNotificationView(id: Int, title: String) = navigator.emit {
        it.navigate(NotificationView.createRoute(id, title))
    }

    fun goBack() = navigator.emit { it.navigateUp() }

    fun deleteNotification(id: Int) = viewModelScope.launch {
        repository.deleteNotification(id)
    }
}
