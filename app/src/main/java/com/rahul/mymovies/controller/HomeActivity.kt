package com.rahul.mymovies.controller

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.rahul.mymovies.R
import com.rahul.mymovies.controller.fragments.MainActivityFragment
import com.rahul.mymovies.controller.fragments.MostPopularFragment
import com.rahul.mymovies.controller.fragments.TopRatedFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener{

    private var fragment: Fragment? = null
    private var fragmentClass: Class<*>? = null
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)



        fragmentClass = MainActivityFragment::class.java
        try{
            fragment = fragmentClass!!.newInstance() as Fragment
        }catch(e: Exception){
            e.printStackTrace()
        }


        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.mainContentSpace, fragment).commit()


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        fragmentClass = null
        var selectedPage = 1
        when (item.itemId) {
            R.id.nav_camera -> {
                selectedPage = 1
                fragmentClass = MainActivityFragment::class.java
            }
            R.id.nav_gallery -> {
                selectedPage = 2
                fragmentClass = MostPopularFragment::class.java
            }
            R.id.nav_slideshow -> {
                selectedPage = 3
                fragmentClass = TopRatedFragment::class.java
            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        if(currentPage != selectedPage){

            var startAnimation = R.anim.push_left_in
            var endAnimation = R.anim.push_left_out
            if(currentPage > selectedPage){
                startAnimation = R.anim.push_right_out
                endAnimation = R.anim.push_right_in
            }
            currentPage = selectedPage

            if(fragmentClass != null) {
                try {
                    fragment = fragmentClass!!.newInstance() as Fragment
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                supportFragmentManager.beginTransaction().
                        setCustomAnimations(startAnimation, endAnimation).
                        replace(R.id.mainContentSpace, fragment).commit()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun setTitleTo(title: String) {
        supportActionBar!!.title = title
    }


}
