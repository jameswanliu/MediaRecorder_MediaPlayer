package com.james.mediarecorder_mediaplayer

import android.app.Application
import android.content.ContextWrapper


private lateinit var INSTANCE:MediaApplication

class MediaApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}


object MediaContext:ContextWrapper(INSTANCE)

