package com.example.haimin_a.crm_test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.haimin_a.crm_test.rest_client.Operations
import kotlinx.android.synthetic.main.activity_registration.*
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.net.URL

class RegistrationActivity : AppCompatActivity() {

    lateinit var REST_URL: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        REST_URL = getString(R.string.rest_url)
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
        when {
            newLogin.isEmpty() -> longToast("Enter login")
            newPassword.isEmpty() -> longToast("Enter new password")
            newRepeatPassword.isEmpty() -> longToast("Repeat password")
            newPassword != newRepeatPassword -> longToast("Passwords not equals")
            else -> {
                val md5 = DigestUtils.md5Hex(newPassword)
                doAsync {
                    val result = URL(REST_URL + Operations.save.str).readText()
                    uiThread {
                        Log.d("Request", result)
                        if (result.toBoolean()) {
                            longToast("Done")
                        } else {
                            longToast("Error")
                        }
                    }
                }
                startActivity<SignInActivity>()
                finish()
            }
        }

    }
}
