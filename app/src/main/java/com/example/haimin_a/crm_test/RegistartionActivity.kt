package com.example.haimin_a.crm_test

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.haimin_a.crm_test.rest_client.Operations
import com.example.haimin_a.crm_test.rest_client.URLBuilder
import kotlinx.android.synthetic.main.activity_registartion.*
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
import java.net.URL

class RegistartionActivity : AppCompatActivity() {

    private val REST_URL: String = getString(R.string.rest_url)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registartion)
        setupUI()
    }

    fun setupUI() {
        registation_btn.setOnClickListener {
            createNewUser()
        }
    }

    private fun createNewUser() {
        val newLogin = new_login.text.toString()
        val newPassword = new_password.text.toString()
        val newRepeatPassword = repeat_new_password.text.toString()
        if (newLogin.isNullOrEmpty())
            longToast("Enter login")
        else if (newPassword.isNullOrEmpty())
            longToast("Enter new password")
        else if (newRepeatPassword.isNullOrEmpty())
            longToast("Repeat password")
        else if (newPassword != newRepeatPassword)
            longToast("Passwords not equals")
        else {
            val md5 = DigestUtils.md5Hex(newPassword)
            doAsync {
                val result = URL(URLBuilder.build(REST_URL, Operations.save.toString(), newLogin, md5)).readText()
                uiThread {
                    Log.d("Request", result)
                    if (result.toBoolean()) {
                        longToast("Done")
                    } else {
                        longToast("Error")
                    }
                }
            }
            startActivity(SignInActivity.getRegistrationIntent(this))
            finish()
        }

    }

    companion object {
        fun getRegistrationIntent(from: Context) = Intent(from, SignInActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}
