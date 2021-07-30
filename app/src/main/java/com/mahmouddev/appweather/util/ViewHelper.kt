package com.mahmouddev.appweather.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object ViewHelper {
    fun TextView.drawableStart(drawable: Int) {
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }

    fun TextView.drawableEnd(drawable: Int) {
        this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
    }

    fun TextView.textColor(textColor: Int) {
        this.setTextColor(ContextCompat.getColor(this.context, textColor))
    }

//    fun TextView.textColor(textColor: Int,alpha: Int) {
//        this.setTextColor(ContextCompat.getColor(this.context, textColor))
//        this.textColors.withAlpha(alpha)
//    }


    fun TextView.backgroundTint(color: Int) {
        this.backgroundTintList = ContextCompat.getColorStateList(this.context, color)
    }

    fun TextView.backgroundDrawable(drawable: Int) {
        this.background = ContextCompat.getDrawable(this.context, drawable)
    }

    fun TextView.backgroundColor(color: Int) {
        this.setBackgroundColor(ContextCompat.getColor(this.context,color))
    }

    fun TextView.colorLink(color: Int) {
        this.setLinkTextColor(ContextCompat.getColor(this.context,color))
    }
    fun TextView.alpha(alpha: Float) {
        this.alpha = alpha
    }
    fun ImageView.tint(color: Int) {
        this.setColorFilter(ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN);
    }


    fun View.visible() {
        this.visibility = View.VISIBLE
    }

     fun View.gone() {
        this.visibility = View.GONE
    }


}