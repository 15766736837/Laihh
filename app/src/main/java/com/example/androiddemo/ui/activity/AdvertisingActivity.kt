package com.example.androiddemo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.androiddemo.R
import com.example.androiddemo.app.BaseActivity
import com.example.androiddemo.app.MainActivity
import com.example.androiddemo.db.DBHelper
import com.example.androiddemo.ui.login.LoginActivity
import com.example.androiddemo.utils.AdvertisingManage

class AdvertisingActivity : BaseActivity(), View.OnClickListener {
    private val advertisingManage by lazy { AdvertisingManage() }
    private val btnNeglect by lazy { findViewById<Button>(R.id.btnNeglect) }

    override fun initEvent() {
        btnNeglect.setOnClickListener(this)
        advertisingManage.setAdvertisingManageListener(object : AdvertisingManage.AdvertisingManageListener{
            override fun timing(second: Int) {
                btnNeglect.text = "跳过${second}s"
            }

            override fun enterMainActivity() {
                goMainAct()
            }

        })
    }

    private fun goMainAct() {
        val userBean = DBHelper.getInstance(this).queryUser(1)
        var intent: Intent = if (userBean != null) {
            Intent(this, MainActivity::class.java)
        }else{
            Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    override fun initView() {
    }

    override fun getLayoutId() = R.layout.activity_advertising

    override fun initContent(savedInstanceState: Bundle?) {
        lifecycle.addObserver(advertisingManage)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnNeglect -> goMainAct()
        }
    }

}