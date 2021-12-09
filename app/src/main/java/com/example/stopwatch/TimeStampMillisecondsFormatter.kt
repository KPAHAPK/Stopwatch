package com.example.stopwatch

class TimeStampMillisecondsFormatter {

    fun format(timestamp: Long): String {
        val millisecondsFormatter =
            (timestamp % 1000).pad(DEFAULT_TIME.substringAfterLast(":").length)
        val seconds = timestamp / 1000
        val secondFormatter = (seconds % 60).pad(
            DEFAULT_TIME
                .substringAfter(":")
                .substringBefore(":")
                .length)
        val minutes = seconds / 60
        val minuteFormatter = (minutes % 60).pad(DEFAULT_TIME.substringBefore(":").length)
        val hours = minutes / 60
        return if (hours > 0) {
            val hoursFormatter = hours.pad(DEFAULT_TIME.substringBefore(":").length)
            "$hoursFormatter:$minuteFormatter:$secondFormatter"
        } else {
            "$minuteFormatter:$secondFormatter:$millisecondsFormatter"
        }

    }

    private fun Long.pad(length: Int): String =
        this.toString().padStart(length, '0')

    companion object {
        const val DEFAULT_TIME = "00:00:000"
    }
}