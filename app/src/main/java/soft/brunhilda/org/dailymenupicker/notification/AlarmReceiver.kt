package soft.brunhilda.org.dailymenupicker.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import soft.brunhilda.org.dailymenupicker.MainActivity
import soft.brunhilda.org.dailymenupicker.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_CODE = 100
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        val intentToRepeat  = Intent(p0, MainActivity::class.java)
        intentToRepeat.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intentToRepeat.putExtra("fromNotification", true)

        val pendingIntent = PendingIntent.getActivity(p0, NOTIFICATION_CODE, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT)
        val repeatedNotification = buildNotification(p0, pendingIntent).build()

        val notificationManager = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_CODE, repeatedNotification)
    }

    /**
     * TODO build notification
     */
    private fun buildNotification(p0: Context?, intentToClick: PendingIntent): NotificationCompat.Builder {
        return NotificationCompat.Builder(p0!!)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("V agende máš jedlo, si hladný?")
                .setContentText("V agende máš naplánované jedlo. Je to:")
                .setContentIntent(intentToClick) // after click, show main activity
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("Svieckova na smotane \n" +
                                "Hovadzie na prirodno \n" +
                                "Bravcove na prirodno \n" +
                                "Kuracie na prirodno \n" +
                                "Tofu na prirodno \n" +
                                "hrachova kasa \n" +
                                "Time is " + Calendar.getInstance().time))
    }
}