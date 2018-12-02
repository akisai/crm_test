package com.example.haimin_a.crm_test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.haimin_a.crm_test.rest_client.Operations
import com.example.haimin_a.crm_test.rest_client.URLBuilder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
import java.net.URL

class SignInActivity : AppCompatActivity() {

    val RC_SIGN_IN: Int = 1
    private val REST_URL: String = "http://192.168.1.125:8080"//getString(R.string.rest_url)
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initGoogleOption()
        firebaseAuth = FirebaseAuth.getInstance()
        setupUI()
    }

    fun getUrl() = REST_URL

    private fun initGoogleOption() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun setupUI() {
        btn_sign_in.setOnClickListener {
            signInGoogle()
        }
        sign_in_btn.setOnClickListener {
            signIn()
        }
        registration.setOnClickListener {
            regisration()
        }
    }

    private fun regisration() {
        startActivity(RegistartionActivity.getRegistrationIntent(this))
    }

    private fun signIn() {
        val loginR = login.text.toString()
        val passwordR = password.text.toString()
        val md5 = DigestUtils.md5Hex(passwordR)
        if (loginR.isNullOrEmpty()) {
            login.setText("kek")
            longToast("Not found login")
        } else if (passwordR.isNullOrEmpty()) {
            longToast("Not found password")
        } else {
            doAsync {
                val result = URL(URLBuilder.build(REST_URL, Operations.findUser.toString(), loginR, md5)).readText()
                uiThread {
                    Log.d("Request", result)
                    if (result.toBoolean()) {
                        longToast("Done")
                    } else {
                        longToast("Error")
                    }
                }
            }
        }
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                longToast("Google sign failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                startActivity(HomeActivity.getLaunchIntent(this))
            } else {
                longToast("Google sign failed")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser
        if (user != null) {
            startActivity(HomeActivity.getLaunchIntent(this))
            finish()
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        fun getRegistrationIntent(from: Context) = Intent(from, RegistartionActivity::class.java)
    }
}
