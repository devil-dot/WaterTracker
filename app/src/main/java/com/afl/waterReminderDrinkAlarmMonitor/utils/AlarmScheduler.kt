package com.afl.waterReminderDrinkAlarmMonitor.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

object AlarmScheduler {


    /**
     * Schedules a single alarm
     */
    fun scheduleAlarm(context: Context, times: MutableList<Int>) {

        cancelAlarm(context)

        for (time in times) {

            // Set up the time to schedule the alarm
            val datetimeToAlarm = Calendar.getInstance()
            datetimeToAlarm.set(Calendar.HOUR_OF_DAY, time)
            datetimeToAlarm.set(Calendar.MINUTE, 0)
            datetimeToAlarm.set(Calendar.SECOND, 0)

            val pendingIntent = createPendingIntent(context,time)

            Log.d("database", "alarm is scheduled for $time")

            startAlarm(
                datetimeToAlarm,
                context,
                intent = pendingIntent!!
            )

        }


    }

    // yukaridaki fonksiyonun devami
    private fun startAlarm(datetimeToAlarm: Calendar, context: Context, intent: PendingIntent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        eger gecmis bir zaman secilirse ertesi gune notification kuruyor
        if (datetimeToAlarm.before(Calendar.getInstance())) {
            datetimeToAlarm.add(Calendar.DATE, 1)
        }

//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, datetimeToAlarm.timeInMillis, pendingIntent)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            datetimeToAlarm.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            intent
        )

//        Log.d("database", "start alarm is called ${datetimeToAlarm.time}")
    }

    // function to cancel all alarms
    fun cancelAlarm(context: Context ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)

        val times: MutableList<Int> = mutableListOf<Int>().apply {
            for (x in 7..26) {
                add(x)
            }
        }

        for (time in times) {
            val pendingIntent = PendingIntent.getBroadcast(context, time, intent, 0)

            alarmManager.cancel(pendingIntent)

//            Log.d("database", "alarm is canceled $pendingIntent")
        }


//        Log.d("database", "alarm is canceled")
    }

    /**
     * Creates a [PendingIntent] for the Alarm using the [ReminderData]
     *
     * @param context      current application context
     * @param reminderData ReminderData for the notification
     * @param day          String representation of the day
     */
    private fun createPendingIntent(context: Context,
                                    alarmHour: Int): PendingIntent? {
        // create the intent using a unique type
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)

        return PendingIntent.getBroadcast(context, alarmHour, intent, 0)
    }
}