package com.rahul.mymovies

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rahul.mymovies.controller.HomeActivity

class SplashActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
