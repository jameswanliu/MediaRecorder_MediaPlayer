package com.james.mediarecorder_mediaplayer

import android.media.AudioManager
import android.media.MediaPlayer


/**
 *
 * MeidaPlayer 的状态变化 https://ws4.sinaimg.cn/large/006tNc79ly1g2fzvgbw4kg30ih0ml0t3.gif
 *
 *
 */
object MediaPlayerHelper {

    const val STATE_NORMAL = 0
    const val STATE_RECORD = 1
    const val STATE_COMPLETE = 2
    const val STATE_PLAYING = 3
    const val STATE_PAUSE = 4

    private var mediaPlayer: MediaPlayer? = null

    var ispause = false

    fun playsound(path: String) {
        mediaPlayer?.let {
            playsound(path, null)
        }?.let {
            mediaPlayer = MediaPlayer()
        }

    }


    fun playsound(path: String, onCompletionListener: MediaPlayer.OnCompletionListener?) {
        mediaPlayer = MediaPlayer()

        mediaPlayer?.let { it ->
            it.setOnErrorListener(MediaPlayer.OnErrorListener { _, _, _ ->
                it.reset()
                return@OnErrorListener false
            })
            it.setAudioStreamType(AudioManager.STREAM_MUSIC)
            onCompletionListener?.let { its ->
                it.setOnCompletionListener(its)
            }
            it.setDataSource(path)//prepare 之前一定要 设置播放资源
            it.prepare()
            it.start()
        }
    }


    fun pause() {
        mediaPlayer?.let {
            it.isPlaying?.let { its ->
                it.pause()
                ispause = true
            }
        }
    }


    fun resume() {
        mediaPlayer?.let {
            if (ispause) {
                it.start()
                ispause = false
            }
        }
    }


    fun release() {
        mediaPlayer?.let {
            it.release()
            ispause = true
        }
    }

}