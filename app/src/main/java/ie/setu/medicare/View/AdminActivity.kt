package ie.setu.medicare.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import ie.setu.medicare.R
import ie.setu.medicare.View.SplashScreen
import ie.setu.medicare.databinding.ActivityAdminBinding


class AdminActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAdminBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var navView: NavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("auth_pref", Context.MODE_PRIVATE)

        setSupportActionBar(binding.appBarAdmin.toolbar)

//        binding.appBarAdmin.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_admin)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_sign_out),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Set a listener for the NavigationView
        navView.setNavigationItemSelectedListener { menuItem ->
            // Handle item selection
            when (menuItem.itemId) {
                R.id.nav_sign_out -> {
                    // Handle sign-out action
                    handleSignOut()
                    true // Return true to indicate the event has been handled
                }
                R.id.nav_profile -> {
                    // Handle profile option
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true // Return true to indicate the event has been handled
                }
                else -> {
                    // Let the NavigationController handle other selections
                    navController.navigate(menuItem.itemId)
                    drawerLayout.closeDrawers()
                    true
                }
            }
        }

        // Call a method to set the navigation header's name and email
        setNavigationHeaderInfo(navView)
    }

    override fun onResume() {
        super.onResume()
        setNavigationHeaderInfo(navView)
    }

    // Method to handle the sign-out action
    private fun handleSignOut() {
        // Handle sign-out
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
        sharedPreferences.edit().putString("name", "").apply()
        sharedPreferences.edit().putString("email", "").apply()
        sharedPreferences.edit().putInt("type", 9).apply()
        sharedPreferences.edit().putString("id", "").apply()
        sharedPreferences.edit().putString("image", "").apply()
        val intent = Intent(this, SplashScreen::class.java)
        startActivity(intent)
        finish()
    }

    // Method to set the navigation header's name and email
    private fun setNavigationHeaderInfo(navView: NavigationView) {
        // Get a reference to the header view
        val headerView = navView.getHeaderView(0)

        val nameTextView: TextView = headerView.findViewById(R.id.name_header)
        val emailTextView: TextView = headerView.findViewById(R.id.email_header)
        var profileImg: ImageView = headerView.findViewById(R.id.imageView)

        // Retrieve the user's name and email from shared preferences
        val name = sharedPreferences.getString("name", "User Name")
        val email = sharedPreferences.getString("email", "user@example.com")

        // Set the name and email in the header view
        nameTextView.text = name
        emailTextView.text = email
        val imageUrl = sharedPreferences.getString("image", "no_url").toString()
        if (imageUrl != "no_url" && imageUrl != "") {
            Glide.with(profileImg.context)
                .load(imageUrl)
                .into(profileImg)
        }

        profileImg.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.admin, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_admin)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
