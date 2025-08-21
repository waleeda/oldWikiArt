package com.example.wikiart.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WikiArtFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title
        val body = message.notification?.body
        val notification = Notification(title = title, message = body)
        val dao = NotificationDatabase.getDatabase(applicationContext).notificationDao()
        val repository = NotificationRepository(dao)
        CoroutineScope(Dispatchers.IO).launch {
            repository.insert(notification)
        }
    }
}
