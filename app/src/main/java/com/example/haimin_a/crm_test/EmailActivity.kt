package com.example.haimin_a.crm_test

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_email.*


class EmailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)
        setFields()
    }

    private fun setFields() {
        buttonSend.setOnClickListener {
            sendEmail()
        }
    }

    private fun sendEmail() {

        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf("akisai.azu@gmail.com"))
        i.putExtra(Intent.EXTRA_SUBJECT, if(editTextSubject.text.isNullOrEmpty()) "Thanks" else editTextSubject.text.toString())
        i.putExtra(Intent.EXTRA_TEXT, if(editTextMessage.text.isNullOrEmpty()) "Great clinic" else editTextMessage.text.toString())
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
        }

    }
}
