package ie.setu.medicare.View

import AdminActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.setu.medicare.R

class SplashScreen : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPreferences = getSharedPreferences("auth_pref", Context.MODE_PRIVATE)
        // Check if the user is already authenticated
        if (isLoggedIn()) {
            // User is signed in, navigate to home page
            var type = sharedPreferences.getInt("type",0)
            when (type) {
                0 ->  navigateToHome()
                1 -> navigateToDRHome()
                3 -> navigateToAdminHome()
                else -> { // Note the block
                    print("user type is not available")
                }
            }
        } else {
            // User is not signed in, navigate to login page
            navigateToLogin()
        }
    }

    private fun isLoggedIn(): Boolean {
        // Read authentication status from SharedPreferences
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

//    private fun navigateToHome() {
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish() // Optional: finish the current activity so the user can't navigate back
//
//    }

    private fun navigateToLogin() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish() // Optional: finish the current activity so the user can't navigate back
    }
    // Call this method when the user logs in
    private fun loginUser() {
        // Update authentication status in SharedPreferences
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }

    // Call this method when the user logs out
    private fun logoutUser() {
        // Update authentication status in SharedPreferences
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
    }

    private fun navigateToHome() {
        val intent = Intent(this, PatientActivity::class.java)
        startActivity(intent)
        finish() // Optional: finish the current activity so the user can't navigate back
    }
    private fun navigateToDRHome() {
        val intent = Intent(this, DrActivity::class.java)
        startActivity(intent)
        finish() // Optional: finish the current activity so the user can't navigate back
    }
    private fun navigateToAdminHome() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
        finish() // Optional: finish the current activity so the user can't navigate back
    }
}