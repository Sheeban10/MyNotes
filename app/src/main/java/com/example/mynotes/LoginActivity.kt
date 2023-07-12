package com.example.mynotes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mynotes.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception


class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lottieAnimationView = binding.loading


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.signOut()

        auth = FirebaseAuth.getInstance()

        val signInButton = binding.btnGoogle
        signInButton.setOnClickListener {
            animateButton()

        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun animateButton() {
        // Start the Lottie animation
        lottieAnimationView.visibility = View.VISIBLE
        lottieAnimationView.playAnimation()

        // Disable the sign-in button
        binding.btnGoogle.isEnabled = false

        // Delay the sign-in logic
        Handler().postDelayed({
            // Stop the Lottie animation
            lottieAnimationView.visibility = View.INVISIBLE
            lottieAnimationView.cancelAnimation()

            // Enable the sign-in button
            binding.btnGoogle.isEnabled = true

            // Perform sign-in logic here
            signIn()
        }, 4500) // Delay of 5 seconds (5000 milliseconds)
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount){
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main){
                }
            }catch(e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already logged in, navigate to MainActivity
            navigateToMainActivity(currentUser)
        }
    }

    private fun navigateToMainActivity(user: FirebaseUser) {
        val displayName = user.displayName
        val photoUrl = user.photoUrl?.toString()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(EXTRA_NAME, displayName)
        intent.putExtra("photoUrl", photoUrl)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                account?.let {
                    googleAuthForFirebase(it)
                }
                // Signed in successfully, show authenticated UI.
                updateUI(account)
            } catch (e: ApiException) {
                // The ApiException status code indicates the error reason.
                // See the GoogleSignInStatusCodes class reference for more information.
                Log.w("Google Sign-In", "signInResult:failed code=" + e.statusCode)
                updateUI(null)
            }
        }
    }


    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            // User is signed in, you can perform further actions here
            val displayName = account.displayName
            val email = account.email
            val photoUrl = account.photoUrl.toString()


            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_NAME, displayName)
            intent.putExtra("photoUrl", photoUrl)
            startActivity(intent)
            finish()

            Toast.makeText(this, "Welcome, $displayName!", Toast.LENGTH_SHORT).show()
        } else {
            // User is signed out
            Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val RC_SIGN_IN = 9001
        const val EXTRA_NAME = "EXTRA NAME"
    }
}

