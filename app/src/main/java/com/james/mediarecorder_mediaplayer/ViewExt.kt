package com.james.mediarecorder_mediaplayer

import android.view.View


inline fun View.onClick(noinline block:()->Unit){
    this.setOnClickListener { block() }
}