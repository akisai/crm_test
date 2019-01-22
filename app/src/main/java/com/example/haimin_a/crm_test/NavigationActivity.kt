package com.example.haimin_a.crm_test

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.haimin_a.crm_test.nav_fragments.*
import com.example.haimin_a.crm_test.rest_client.FindUserInfo
import com.example.haimin_a.crm_test.rest_client.Operations
import com.example.haimin_a.crm_test.rest_client.UserInfo
import com.example.haimin_a.crm_test.utils.getPostResponse
import com.example.haimin_a.crm_test.utils.processingResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.nav_header_navigation.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var REST_URL: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)
        setAccountInfo()

        fab.setOnClickListener {
            //onBackPressed()
        }


        val toggle = ActionBarDrawerToggle(
            this, navigation_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        navigation_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        nav_view.menu.findItem(R.id.nav_doctor).isVisible = false
    }

    override fun onBackPressed() {
        FirebaseAuth.getInstance().signOut()
        startActivity<SignInActivity>()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
        when {
            item.itemId == R.id.action_settings -> {
                startActivity<SettingsActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        if (fab.isOrWillBeHidden) {
            fab.show()
        }
        when (item.itemId) {
            R.id.nav_edit -> {
                // Handle the camera action
                replaceFragment(
                    ServiceFragment(),
                    true,
                    R.id.navigation_content
                )
                setTitle("Edit")
            }
            R.id.nav_procedure -> {
                replaceFragment(
                    TaskFragment(),
                    true,
                    R.id.navigation_content
                )
                setTitle("Choose procedure")
            }
            R.id.nav_doctor -> {

            }
            R.id.nav_find_us -> {
                replaceFragment(
                    MapFragment(),
                    true,
                    R.id.navigation_content
                )
                fab.hide()
                setTitle("Maps")
            }
            R.id.nav_settings -> {
                startActivity<SettingsActivity>()
                fab.hide()
            }
            R.id.nav_info -> {
                replaceFragment(
                    UserProfileFragment(),
                    true,
                    R.id.navigation_content
                )
                setTitle("About Us")
            }
        }

        navigation_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun setAccountInfo() {
        when {
            intent.getStringExtra("type") == "google" -> {
                nav_view.getHeaderView(0).user_name.text = intent.getStringExtra("name")
                nav_view.getHeaderView(0).user_email.text = intent.getStringExtra("email")
                Picasso.get().load(intent.getStringExtra("icon")).into(nav_view.getHeaderView(0).user_image)
            }
            intent.getStringExtra("type") == "sign" -> {
                getUserInfo(intent.getStringExtra("user")!!, this)
            }
        }
    }

    fun getUserInfo(id: String, context: Context) {
        REST_URL = getString(R.string.rest_url)
        val dialogLog = indeterminateProgressDialog("Get user info...", "")
        dialogLog.setCancelable(false)
        doAsync {
            val json = Gson().toJson(FindUserInfo(id.toLong()))
            val response = getPostResponse(REST_URL + Operations.userInfo.str, json)
            uiThread {
                dialogLog.dismiss()
                if (processingResponse(context, response, "Get user info failed", "Empty info")) {
                    val gson: UserInfo = Gson().fromJson(response, UserInfo::class.java)
                    when {
                        gson.name.isEmpty() && gson.surname.isEmpty() ->
                            nav_view.getHeaderView(0).user_name.text = "Ivan Ivanov"
                        gson.email.isEmpty() -> nav_view.getHeaderView(0).user_email.text = "IvanIvanov@gmail.com"
                        gson.pic.isEmpty() ->
                            Picasso.get().load(getString(R.string.google_pic)).into(
                                nav_view.getHeaderView(0)
                                    .user_image
                            )
                        //
                        else -> {
                            nav_view.getHeaderView(0).user_name.text =
                                    if (gson.name.isEmpty()) gson.surname else gson.name + " " + gson.surname
                            Picasso.get().load(gson.pic).into(nav_view.getHeaderView(0).user_image)
                            nav_view.getHeaderView(0).user_email.text = gson.email
                        }
                    }
                }
            }
        }
    }

}
