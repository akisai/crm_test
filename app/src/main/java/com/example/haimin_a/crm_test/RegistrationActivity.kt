package com.example.haimin_a.crm_test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.haimin_a.crm_test.utils.buildPostParams
import com.example.haimin_a.crm_test.rest_client.Operations
import com.example.haimin_a.crm_test.rest_client.SaveUser
import com.example.haimin_a.crm_test.rest_client.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_registration.*
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.anko.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
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
            createNewUser()
        }
        supportFragmentManager.addOnBackStackChangedListener {
            startActivity<SignInActivity>()
            finish()
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
                val dialogReg = indeterminateProgressDialog("Registration in progress...")
                dialogReg.setCancelable(false)
                doAsync {
                    var exception = false
                    var response: String? = null
                    try {
                        val connection = URL(REST_URL + Operations.save.str)
                            .openConnection() as HttpURLConnection
                        buildPostParams(connection)
                        val json = Gson().toJson(SaveUser(newLogin, md5, LocalDateTime.now().toString()))
                        connection.outputStream.write(json.toByteArray())
                        response = connection.inputStream.bufferedReader().readText()
                        connection.disconnect()
                    } catch (e: Exception) {
                        exception = true
                    }
                    uiThread {
                        dialogReg.dismiss()
                        if (exception) {
                            alert("Connection error") {
                                title = "Registration failed"
                                yesButton {}
                            }.show()
                        } else {
                            if (!response.isNullOrEmpty()) {
                                val gson = Gson().fromJson(response, User::class.java)
                                if (!gson.login.isEmpty()) {
                                    longToast(response.toString())
                                    startActivity<SignInActivity>()
                                    finish()
                                }
                            } else
                                alert("Unknown error") {
                                    title = "Registration failed"
                                    yesButton {}
                                }.show()
                        }
                    }
                }
            }
        }

    }
}
