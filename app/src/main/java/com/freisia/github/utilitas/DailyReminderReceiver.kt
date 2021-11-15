package com.freisia.github.utilitas

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.freisia.github.R
import com.freisia.github.ui.search.SearchActivity
import java.util.*

class DailyReminderReceiver : BroadcastReceiver() {

    companion object {
        private const val ID_TYPE_REPEAT_9AM = 100
    }
    override fun onReceive(context: Context, intent: Intent) {
        val message = context.getString(R.string.repeatnotif)
        val title = context.getString(R.string.app_name)
        val notifId = ID_TYPE_REPEAT_9AM
        showNotification(context,title,message,notifId)
    }

    private fun showNotification(context: Context,title: String, message : String, notifId: Int){
        val CHANNEL_ID = "Channel_0"
        val CHANNEL_NAME = "notificationReminder"
        val intent = Intent(context, SearchActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context, notifId,intent,0)
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context,CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setSound(alarmSound)
                    .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    fun setRepeatingReminder9AM(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminderReceiver::class.java)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,9)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)
        val now = Calendar.getInstance()
        if (calendar.get(Calendar.HOUR_OF_DAY) < now.get(Calendar.HOUR_OF_DAY)){
            calendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + 1)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, ID_TYPE_REPEAT_9AM,intent,0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
        Toast.makeText(context, R.string.repeat9AM, Toast.LENGTH_SHORT).show()
    }

    fun cancelReminder(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminderReceiver::class.java)
        val requestCode = ID_TYPE_REPEAT_9AM
        val pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, R.string.cancelrepeat, Toast.LENGTH_SHORT).show()
    }
}