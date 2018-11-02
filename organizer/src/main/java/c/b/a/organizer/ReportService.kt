package c.b.a.organizer

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

/**
 *  Створено oor 19.09.2018.
 */
const val NOTIFICATION_ID = 1
const val CHANNEL_ID = 2
class ReportService : IntentService("ReportService") {
    override fun onHandleIntent(intent: Intent?) {
        val builder = NotificationCompat.Builder(this)
        builder.setSmallIcon(R.drawable.organizer)
        builder.setContentTitle("Органайзер")
        builder.setContentText("У Вас залишились незакінчені завдання!")
        builder.priority = NotificationCompat.PRIORITY_HIGH

        val reportIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0,
                reportIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        val notificationCompat = builder.build()

        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(NOTIFICATION_ID, notificationCompat)
    }
}