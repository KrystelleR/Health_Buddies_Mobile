package com.varsitycollege.xbcad.healthbuddies

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView


class breathingpage : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var playButton: Button
    private lateinit var countdownTextView: TextView
    private lateinit var stopButton: Button
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_breathingpage)


        videoView = findViewById(R.id.videoView)
        playButton = findViewById(R.id.playButton)
        countdownTextView = findViewById(R.id.countdownTextView)
        stopButton = findViewById(R.id.stopButton)

        videoView.setVideoPath("android.resource://" + packageName + "/" + R.raw.breathingvid)

        // Set an OnCompletionListener to restart the video when it finishes
        videoView.setOnCompletionListener { mp ->
            mp.start()
        }

        playButton.setOnClickListener {
            playButton.visibility = View.INVISIBLE
            countdownTextView.visibility = View.VISIBLE

            // Start a countdown before playing the video
            object : CountDownTimer(4000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    countdownTextView.text = "Ready? $secondsRemaining"
                }

                override fun onFinish() {
                    countdownTextView.visibility = View.INVISIBLE
                    stopButton.visibility = View.VISIBLE
                    startVideoPlayback()
                }
            }.start()
        }

        stopButton.setOnClickListener {
            stopVideoPlayback()
            stopButton.visibility = View.INVISIBLE
            playButton.visibility = View.VISIBLE
        }
    }

    private fun startVideoPlayback() {
        videoView.requestFocus()
        videoView.start()
    }

    private fun stopVideoPlayback() {
        videoView.stopPlayback()
    }
}