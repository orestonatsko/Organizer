package c.b.a.organizer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 *  Створено oor 19.09.2018.
 */
class ReportReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val scheduledIntent = Intent(context, ReportService::class.java)
        context!!.startService(scheduledIntent)
    }
}