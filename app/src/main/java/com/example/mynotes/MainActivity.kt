package com.example.mynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.mynotes.LoginActivity.Companion.EXTRA_NAME
import com.example.mynotes.LoginActivity.Companion.PHOTO
import com.example.mynotes.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        profileImageView = binding.accountPhoto
        auth = FirebaseAuth.getInstance()
        val photoUrl = intent.getStringExtra("photoUrl")
        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .transform(CircleCrop())
                .override(100, 100)
                .into(profileImageView)
        }

        binding.username.text = intent.getStringExtra(EXTRA_NAME)
        binding.logout.setOnClickListener{
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}