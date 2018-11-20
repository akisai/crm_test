package com.example.haimin_a.crm_test

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupUI()
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, SignInActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    private fun setupUI() {
        btn_sign_out.setOnClickListener {
            singOut()
        }
    }

    private fun singOut() {
        startActivity(SignInActivity.getLaunchIntent(this))
        finish()
    }
}
