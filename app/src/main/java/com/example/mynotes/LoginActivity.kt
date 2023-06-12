package com.example.mynotes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.mynotes.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider



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

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("36633178363-cttcfdlhqtk2gtrefbt29gl2bollnmhf.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

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

        // Delay the sign-in logic by 5 seconds
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

//    override fun onStart() {
//        super.onStart()
//        val currentUser: FirebaseUser? = auth.currentUser
//        if (currentUser != null) {
//            // User is already signed in
//            goToMainActivity()
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
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

//    private fun updateUI(account: GoogleSignInAccount?) {
//        if (account != null) {
//            // User is signed in, you can perform further actions here
//            val displayName = account.displayName
//            val email = account.email
//
//            // Sign in with Firebase
//            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//            auth.signInWithCredential(credential)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        val user = auth.currentUser
//                        Toast.makeText(this, "Welcome, ${user?.displayName}!", Toast.LENGTH_SHORT).show()
//                        goToMainActivity()
//                    } else {
//                        // Sign in failed
//                        Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        } else {
//            // User is signed out
//            Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
//        }
//    }
    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            // User is signed in, you can perform further actions here
            val displayName = account.displayName
            val email = account.email

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_NAME, displayName)
            startActivity(intent)
            finish()

            Toast.makeText(this, "Welcome, $displayName!", Toast.LENGTH_SHORT).show()
        } else {
            // User is signed out
            Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        const val RC_SIGN_IN = 9001
        const val EXTRA_NAME = "EXTRA NAME"
    }

//    private fun goToMainActivity() {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
}