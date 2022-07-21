package com.example.androiddemo.utils

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 启动页管理器
 */
class AdvertisingManage : DefaultLifecycleObserver {
    private var advertisingManageListener: AdvertisingManageListener? = null
    //定时器
    private var countDownTimer: CountDownTimer? = object : CountDownTimer(5000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            advertisingManageListener?.timing((millisUntilFinished / 1000).toInt())
        }

        override fun onFinish() {
            advertisingManageListener?.enterMainActivity()
        }

    }

    fun setAdvertisingManageListener(listener: AdvertisingManageListener){
        advertisingManageListener = listener
    }

    override fun onStart(owner: LifecycleOwner) {
        countDownTimer?.start()

    }

    override fun onDestroy(owner: LifecycleOwner) {
        countDownTimer?.cancel()
        countDownTimer = null
    }

    interface AdvertisingManageListener{
        //计时
        fun timing(second: Int)
        //计时结束,进入主页面
        fun enterMainActivity()
    }
}