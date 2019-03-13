package soft.brunhilda.org.dailymenupicker.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import soft.brunhilda.org.dailymenupicker.MainActivity
import soft.brunhilda.org.dailymenupicker.R

class AlarmReceiver : BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_CODE = 100
        const val NOTIFICATION_CHANNEL_ID = "soft.brumhilda.org.dailymenupicker"
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        val intentToRepeat  = Intent(p0, MainActivity::class.java)
        intentToRepeat.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intentToRepeat.putExtra("fromNotification", true)

        val importance = NotificationManager.IMPORTANCE_LOW
        val pendingIntent = PendingIntent.getActivity(p0, NOTIFICATION_CODE, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT)
        val repeatedNotification = buildNotification(p0, pendingIntent).build()

        val notificationManager = p0?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT)

            // Configure the notification channel.
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }


        //notificationManager.notify(NOTIFICATION_CODE, repeatedNotification)
    }

    /**
     * TODO build notification
     */
    private fun buildNotification(p0: Context?, intentToClick: PendingIntent): NotificationCompat.Builder {

        return NotificationCompat.Builder(p0!!, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_daily_menu_picker)
                .setColor(Color.RED)
                .setContentTitle("V agende máš jedlo, si hladný?")
                .setContentText("V agende máš naplánované jedlo. Je to:")
                .setContentIntent(intentToClick) // after click, show main activity
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("Sviečková na smotane \n" +
                                "Hovädzie na prírodno \n" +
                                "Bravčové na prírodno \n" +
                                "Kuracie na prírodno \n" +
                                "Tofu na paprike \n" +
                                "Hrachová kaša s klobásou \n"
                                // + "Time is " + Calendar.getInstance().time
                        ))
    }
}