package com.example.haimin_a.crm_test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupUI()
    }

    private fun setupUI() {
        btn_sign_out.setOnClickListener {
            singOut()
        }
    }

    private fun singOut() {
        startActivity(intentFor<SignInActivity>().newTask().clearTask())
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}
