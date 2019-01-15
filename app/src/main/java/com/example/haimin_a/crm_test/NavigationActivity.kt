package com.example.haimin_a.crm_test

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.haimin_a.crm_test.nav_fragments.UserProfileFragment
import com.example.haimin_a.crm_test.nav_fragments.replaceFragment
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.nav_header_navigation.*
import kotlinx.android.synthetic.main.nav_header_navigation.view.*
import org.jetbrains.anko.startActivity

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)

        nav_view.getHeaderView(0).user_name.text = "sfjdsk"
        nav_view.getHeaderView(0).user_email.text = "akisai.azu@gmail.com"
        //nav_view.getHeaderView(0).user_image.setImageResource("test")

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        val toggle = ActionBarDrawerToggle(
            this, navigation_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        navigation_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
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

        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_settings -> {
                startActivity<SettingsActivity>()
            }
            R.id.nav_info -> {
                /*supportFragmentManager.beginTransaction()
                    .replace(R.id.navigation_layout, UserProfileFragment.newInstanse(), "sasat").commit()*/
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
}
