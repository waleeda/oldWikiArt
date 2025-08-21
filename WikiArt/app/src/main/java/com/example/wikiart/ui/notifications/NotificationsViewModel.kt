package com.example.wikiart.ui.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.wikiart.notifications.Notification
import com.example.wikiart.notifications.NotificationDatabase
import com.example.wikiart.notifications.NotificationRepository

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NotificationRepository
    val notifications: LiveData<List<Notification>>

    init {
        val dao = NotificationDatabase.getDatabase(application).notificationDao()
        repository = NotificationRepository(dao)
        notifications = repository.notifications
    }
}
