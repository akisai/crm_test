package com.example.haimin_a.crm_test

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.haimin_a.crm_test.rest_client.Operations
import com.example.haimin_a.crm_test.rest_client.SaveUser
import com.example.haimin_a.crm_test.rest_client.User
import com.example.haimin_a.crm_test.utils.getPostResponse
import com.example.haimin_a.crm_test.utils.processingResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_registration.*
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.anko.*
import java.time.LocalDateTime

class RegistrationActivity : AppCompatActivity() {

    lateinit var REST_URL: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        REST_URL = getString(R.string.rest_url)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setupUI()
    }

    fun setupUI() {
        registation_btn.setOnClickListener {
            createNewUser(this)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            startActivity<SignInActivity>()
            finish()
        }
    }

    private fun createNewUser(context: Context) {
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
                val dialogReg = indeterminateProgressDialog("Registration in progress...")
                dialogReg.setCancelable(false)
                doAsync {
                    val json = Gson().toJson(SaveUser(newLogin, md5, LocalDateTime.now().toString()))
                    val response = getPostResponse(REST_URL + Operations.save.str, json)
                    uiThread {
                        dialogReg.dismiss()
                        if (processingResponse(context, response, "Registration failed", "Database error")) {
                            val gson = Gson().fromJson(response, User::class.java)
                            if (!gson.login.isEmpty()) {
                                startActivity<SignInActivity>()
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }
}

