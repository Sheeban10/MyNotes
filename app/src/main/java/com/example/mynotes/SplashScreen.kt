package com.example.mynotes

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.mynotes.LoginActivity.Companion.EXTRA_NAME
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 2700

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        supportActionBar?.hide()
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed(     //used for delaying activity
            {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            },
            SPLASH_TIME_OUT
        )
    }

}