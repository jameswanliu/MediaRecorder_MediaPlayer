package com.james.mediarecorder_mediaplayer

import android.media.MediaRecorder
import java.io.File
import java.util.*

object MediaRecorderHelper {
    private var mediaRecorder: MediaRecorder? = null
    private val file = MediaContext.externalCacheDir

    var playpath = ""

    fun startRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.let {
            it.setAudioSource(MediaRecorder.AudioSource.MIC)//进入 initialied 状态
            it.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)//设置输出格式为3gp 进入 DataSourceConfigured状态
            it.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)//进入 DataSourceConfigured 后 可以设置 AudioEncoder setOutputFile 等属性
            try {
                val files = File(file?.path,generateFileName())
                playpath = files.absolutePath
                it.setOutputFile(playpath)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            it.prepare()
            it.start()
        }
    }



    fun stopAndRelease(){
        mediaRecorder?.let {
            it.stop()
            it.release()
        }

        mediaRecorder = null
    }


    private fun generateFileName(): String {
        return UUID.randomUUID().toString() + ".3gp"
    }

}