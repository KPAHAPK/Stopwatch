package com.example.stopwatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stopwatch.databinding.ActivityStopwatchMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StopwatchMainActivity : AppCompatActivity() {

    private val timestampProvider = object: TimestampProvider{
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    private val scopeObservable = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val scopeObserver = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val stopwatchStateOrchestrator = StopwatchListOrchestrator(
        stopwatchStateHolder = StopwatchStateHolder(
            stopwatchStateCalculator = StopwatchStateCalculator(
                timestampProvider = timestampProvider,
                elapsedTimeCalculator = ElapsedTimeCalculator(timestampProvider)
            ), elapsedTimeCalculator = ElapsedTimeCalculator(timestampProvider),
            timeStampMillisecondsFormatter = TimeStampMillisecondsFormatter()
        ), scope = scopeObservable
    )

    private lateinit var binding : ActivityStopwatchMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopwatchMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        with(binding){
            scopeObserver.launch {
                stopwatchStateOrchestrator.ticker.collect() {
                    textTime.text = it
                }
            }
            buttonStart.setOnClickListener {
                stopwatchStateOrchestrator.start()
            }
            buttonPause.setOnClickListener{
                stopwatchStateOrchestrator.pause()
            }
            buttonStop.setOnClickListener{
                stopwatchStateOrchestrator.stop()
            }
        }
    }
}