package com.james.mediarecorder_mediaplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.james.mediarecorder_mediaplayer.MediaPlayerHelper.STATE_COMPLETE
import com.james.mediarecorder_mediaplayer.MediaPlayerHelper.STATE_NORMAL
import com.james.mediarecorder_mediaplayer.MediaPlayerHelper.STATE_PAUSE
import com.james.mediarecorder_mediaplayer.MediaPlayerHelper.STATE_PLAYING
import com.james.mediarecorder_mediaplayer.MediaPlayerHelper.STATE_RECORD
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private var currentState = STATE_NORMAL
    private var isPlaying = false
    private var isRecord = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermission()

        iv_action.onClick {
            changeState()
        }

        iv_delete.onClick {
            changeNormalState()
            val file = File(MediaRecorderHelper.playpath)
            file.delete()
        }

        iv_save.onClick {
            changeNormalState()
        }
    }


    private fun changeNormalState(){
        currentState = STATE_NORMAL
        chronometer.base = SystemClock.elapsedRealtime()
        MediaPlayerHelper.release()
        MediaRecorderHelper.stopAndRelease()
        rl_bottom.visibility = View.GONE
        iv_action.setImageResource(R.mipmap.btn_clue_audio)
    }


    private fun changeState() {
        when (currentState) {
            STATE_NORMAL -> {
                currentState = STATE_RECORD
            }
            STATE_RECORD -> {
                currentState = STATE_COMPLETE
            }
            STATE_COMPLETE -> {
                currentState = STATE_PLAYING
            }
            STATE_PLAYING -> {
                currentState = STATE_PAUSE
            }
            STATE_PAUSE -> {
                currentState = STATE_PLAYING
            }
        }
        changeUiByState(currentState)
    }


    private fun changeUiByState(state: Int) {
        when (state) {
            STATE_NORMAL -> {
                isRecord = false
                isPlaying = false
                MediaPlayerHelper.resume()
                iv_action.setImageResource(R.mipmap.btn_clue_audio)
            }
            STATE_RECORD -> {
                iv_action.setImageResource(R.mipmap.pause)
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
                isRecord = true
                waveView.visibility = View.VISIBLE
                MediaRecorderHelper.startRecorder()
            }
            STATE_COMPLETE -> {
                iv_action.setImageResource(R.mipmap.icon_audio_state_uploaded)
                chronometer.stop()
                waveView.visibility = View.INVISIBLE
                rl_bottom.visibility = View.VISIBLE
                MediaRecorderHelper.stopAndRelease()
                isRecord = false
            }
            STATE_PLAYING -> {
                MediaPlayerHelper.resume()
                isPlaying = true
                iv_action.setImageResource(R.mipmap.icon_audio_state_uploaded_play)
                MediaPlayerHelper.playsound(MediaRecorderHelper.playpath, MediaPlayer.OnCompletionListener {
                    currentState = STATE_COMPLETE
                    changeState()
                })
            }
            STATE_PAUSE -> {
                MediaPlayerHelper.pause()
                isPlaying = true
                iv_action.setImageResource(R.mipmap.icon_audio_state_uploaded)
            }
        }
    }


    private fun requestPermission() {
        XXPermissions.with(this)
            .permission(
                Permission.Group.STORAGE, arrayOf(
                    Permission.WRITE_EXTERNAL_STORAGE, Permission.RECORD_AUDIO
                )
            )
            .request(object : OnPermission {
                override fun hasPermission(
                    granted: List<String>,
                    isAll: Boolean
                ) = Unit

                override fun noPermission(
                    denied: List<String>,
                    quick: Boolean
                ) = Unit
            })
    }




    override fun onDestroy() {
        if(isRecord){
            chronometer.stop()
            MediaRecorderHelper.stopAndRelease()
        }
        if(isPlaying)MediaPlayerHelper.release()
        super.onDestroy()
    }
}
