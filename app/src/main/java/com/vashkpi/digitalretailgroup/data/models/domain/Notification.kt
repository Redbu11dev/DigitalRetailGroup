package com.vashkpi.digitalretailgroup.data.models.domain

import android.os.Parcelable
import com.vashkpi.digitalretailgroup.utils.capitalizeWords
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Notification (
    val notification_id: String,
    val date: String,
    val title: String,
    val text: String,
    val read: Boolean
): Parcelable

fun String.formatDateToNotificationDate(): String {
    if (this.length != 14) {
        return "???"
    }
    else {
        val year = subSequence(0, 4)
        val month = subSequence(4, 6)
        val day = subSequence(6, 8)

        val hours = subSequence(8, 10)
        val minutes = subSequence(10, 12)
        val seconds = subSequence(12, 14)

        //val date = "$year-$month-$day $hours:$minutes:$seconds"

        val then = Calendar.getInstance()
        then.set(
            year.toString().toInt(),
            month.toString().toInt() - 1, //because Java's calendar month starts from 0
            day.toString().toInt() + 0,
            hours.toString().toInt(),
            minutes.toString().toInt(),
            seconds.toString().toInt()
        )
        then.timeZone = TimeZone.getTimeZone("UTC")

        val now = Calendar.getInstance()
        now.timeZone = TimeZone.getTimeZone("UTC")

        val yesterday = Calendar.getInstance()
        yesterday.timeZone = TimeZone.getTimeZone("UTC")
        yesterday.add(Calendar.DAY_OF_MONTH, -1)

        val dayBeforeYesterday = Calendar.getInstance()
        dayBeforeYesterday.timeZone = TimeZone.getTimeZone("UTC")
        dayBeforeYesterday.add(Calendar.DAY_OF_MONTH, -2)

//    Timber.d("THEN D/M/Y: ${then.get(Calendar.DAY_OF_MONTH)}/${then.get(Calendar.MONTH)}/${then.get(Calendar.YEAR)}")
//    Timber.d("NOW D/M/Y: ${now.get(Calendar.DAY_OF_MONTH)}/${now.get(Calendar.MONTH)}/${now.get(Calendar.YEAR)}")
//    Timber.d("Y D/M/Y: ${yesterday.get(Calendar.DAY_OF_MONTH)}/${yesterday.get(Calendar.MONTH)}/${yesterday.get(Calendar.YEAR)}")
//    Timber.d("BY D/M/Y: ${dayBeforeYesterday.get(Calendar.DAY_OF_MONTH)}/${dayBeforeYesterday.get(Calendar.MONTH)}/${dayBeforeYesterday.get(Calendar.YEAR)}")

        return when {
            //сегодня
            (then.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)
                    && then.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                    && then.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                    ) -> {
                SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
                    //timeZone = TimeZone.getTimeZone("UTC")
                }.format(then.timeInMillis)
            }

            //вчера
            (then.get(Calendar.DAY_OF_MONTH) == yesterday.get(Calendar.DAY_OF_MONTH)
                    && then.get(Calendar.MONTH) == yesterday.get(Calendar.MONTH)
                    && then.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR)
                    ) -> {
                "Вчера"
            }

            //позавчера
            (then.get(Calendar.DAY_OF_MONTH) == dayBeforeYesterday.get(Calendar.DAY_OF_MONTH)
                    && then.get(Calendar.MONTH) == dayBeforeYesterday.get(Calendar.MONTH)
                    && then.get(Calendar.YEAR) == dayBeforeYesterday.get(Calendar.YEAR)
                    ) -> {
                "Позавчера"
            }

            //другое
            else -> {
                SimpleDateFormat("dd MMMM yyyy  HH:ss", Locale.getDefault()).apply {
                    //timeZone = TimeZone.getTimeZone("UTC")
                }.format(then.timeInMillis).capitalizeWords()
            }
        }
    }
}