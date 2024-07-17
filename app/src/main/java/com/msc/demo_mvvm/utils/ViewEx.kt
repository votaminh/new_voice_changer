package com.msc.demo_mvvm.utils

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat

object ViewEx {
    fun ImageView.tintColor(color : Int){
        this.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }
    fun ImageView.tintColorRes(colorRes : Int){
        this.setColorFilter(ContextCompat.getColor(this.context, colorRes), PorterDuff.Mode.SRC_IN)
    }

    fun View.gone(){
        visibility = View.GONE
    }

    fun View.invisible(){
        visibility = View.INVISIBLE
    }

    fun View.visible(){
        visibility = View.VISIBLE
    }
}