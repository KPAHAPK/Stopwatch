package com.example.stopwatch

class ElapsedTimeCalculator(
    private val timestampProvider: TimestampProvider
) {

    fun calculate(state:StopwatchState.Running): Long {
        val currentTimestamp = timestampProvider.getMilliseconds()
        val timePassedSinceStart = if (currentTimestamp > state.elapsedTime){
            currentTimestamp - state.elapsedTime
        } else {
            0
        }
        return timePassedSinceStart
    }

}
