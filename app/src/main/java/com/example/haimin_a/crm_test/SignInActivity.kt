package com.example.haimin_a.crm_test

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
import org.jetbrains.anko.*
import java.net.HttpURLConnection
import java.net.URL

class SignInActivity : AppCompatActivity() {

    val RC_SIGN_IN: Int = 1
    lateinit var REST_URL: String
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        REST_URL = getString(R.string.rest_url)
        initGoogleOption()
        firebaseAuth = FirebaseAuth.getInstance()
        setupUI()
    }

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
        startActivity(intentFor<RegistrationActivity>().newTask().clearTask())
    }

    private fun signIn() {
        val loginR = login.text.toString()
        val passwordR = password.text.toString()
        val md5 = DigestUtils.md5Hex(passwordR)
        when {
            loginR.isEmpty() -> longToast("Not found login")
            passwordR.isEmpty() -> longToast("Not found password")
            else -> {
                val dialog = indeterminateProgressDialog("This a progress dialog", "")
                dialog.setCancelable(false)
                doAsync {
                    var test: Boolean = false
                    val result: String? = null
                    try {
                        val connection = URL(
                            URLBuilder.build(
                                REST_URL,
                                Operations.findUser.toString(),
                                loginR,
                                md5
                            )
                        ).openConnection() as HttpURLConnection
                        connection.requestMethod = "POST"
                        connection.connectTimeout = 30000
                        connection.readTimeout = 30000
                        connection.doOutput = true
                        connection.connect()
                    } catch (e: Exception) {
                        test = true
                    }
                    uiThread { _ ->
                        dialog.hide()
                        if (test)
                            dialog.hide()
                        alert("Testing alerts") {
                            title = "Alert"
                            yesButton { }
                        }.show()
                    }
                }
                //dialog.hide()
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
                startActivity(intentFor<HomeActivity>().newTask().clearTask())
            } else {
                longToast("Google sign failed")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser
        if (user != null) {
            startActivity(intentFor<HomeActivity>().newTask().clearTask())
            finish()
        }
    }
}
