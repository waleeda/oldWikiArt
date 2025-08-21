package com.example.wikiart.notifications

class NotificationRepository(private val dao: NotificationDao) {
    val notifications = dao.getAll()

    suspend fun insert(notification: Notification) {
        dao.insert(notification)
    }
}
