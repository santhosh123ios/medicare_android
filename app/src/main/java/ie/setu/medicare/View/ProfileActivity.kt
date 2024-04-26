package ie.setu.medicare.View

import android.Manifest
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import ie.setu.medicare.Model.Users
import ie.setu.medicare.R
import ie.setu.medicare.ViewModels.SignInActivityVM

class ProfileActivity : AppCompatActivity() {

    private val storage = Firebase.storage // Initialize Firebase Storage
    private val storageRef = storage.reference // Reference to storage root
    // Initialize Firestore (if you want to save metadata)
    private val db = Firebase.firestore
    private lateinit var profile_img: ImageView
    private lateinit var locationTxt: TextView
    private val viewModel: SignInActivityVM by viewModels()
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var nameEt: EditText
    private lateinit var heightEt: EditText
    private lateinit var weightEt: EditText
    private lateinit var dobEt: EditText

    private lateinit var gentRadioGroups:RadioGroup
    private lateinit var gentRdBtn1:RadioButton
    private lateinit var gentRdBtn2:RadioButton
    var genValue = "male"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        uiModeManager.nightMode = UiModeManager.MODE_NIGHT_YES // Enable dark mode

        sharedPreferences = getSharedPreferences("auth_pref", Context.MODE_PRIVATE)
        profile_img = findViewById(R.id.profile_img)

        val nameTextView: TextView = findViewById(R.id.name)
        val emailTextView: TextView = findViewById(R.id.email)
        locationTxt = findViewById(R.id.location_text)

        // Retrieve the user's name and email from shared preferences
        val name = sharedPreferences.getString("name", "User Name")
        val email = sharedPreferences.getString("email", "user@example.com")
        loadImage()
        // Set the name and email in the header view
        nameTextView.text = name
        emailTextView.text = email
        val back_button = findViewById<ImageView>(R.id.back_button)
        val update_lat_and_log = findViewById<Button>(R.id.update_lat_and_log)
        val submit_btn = findViewById<Button>(R.id.submit_btn)
        val switch_mode = findViewById<Switch>(R.id.switch_mode)
        var mode = sharedPreferences.getBoolean("dark_mode",false)
        if (mode)
        {
            switch_mode.isChecked = true
        }
        else
        {
            switch_mode.isChecked = false
        }
        switch_mode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The switch is ON (feature enabled)
                sharedPreferences.edit().putBoolean("dark_mode", true).apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                // The switch is OFF (feature disabled)
                sharedPreferences.edit().putBoolean("dark_mode", false).apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        back_button.setOnClickListener {
            finish()
        }

        nameEt = findViewById(R.id.nameEdit)
        heightEt = findViewById(R.id.height_id)
        weightEt = findViewById(R.id.weight_id)
        dobEt = findViewById(R.id.dob_id)

        gentRadioGroups = findViewById(R.id.rendRadioGroup)
        gentRdBtn1 = findViewById(R.id.male_radio_btn)
        gentRdBtn2 = findViewById(R.id.female_radio_btn)

        gentRadioGroups.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (radio) {
                gentRdBtn1 -> {
                    // some code
                    genValue = "male"
                }
                gentRdBtn2 -> {
                    // some code
                    genValue = "female"
                }
            }
        }

        submit_btn.setOnClickListener {
            if (nameEt.text.isNotEmpty() && heightEt.text.isNotEmpty() && weightEt.text.isNotEmpty()&& dobEt.text.isNotEmpty()) {
                updateUser()
            }
            else
            {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        update_lat_and_log.setOnClickListener {
            // Request location permission
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission()
            } else {
                getCurrentLocation()
            }
        }
        val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Handle the result of the image picker
            uri?.let {
                // Perform any actions with the image URI
                uploadImageToStorage(it)
            }
        }
        profile_img.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        val type = sharedPreferences.getInt("type", 0)
        if (type == 1)
        {
            // Initialize FusedLocationProviderClient
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            requestLocationPermission()

            locationTxt.visibility = View.VISIBLE
            update_lat_and_log.visibility = View.VISIBLE
        }
        else
        {
            locationTxt.visibility = View.GONE
            update_lat_and_log.visibility = View.GONE
        }
        getUserDetails()
    }

    private fun getUserDetails()
    {
        val id = sharedPreferences.getString("id", "id").toString()
        viewModel.getUserData(id) {user ->

            if (user != null) {
                nameEt.setText(user.name)
                heightEt.setText(user.height)
                weightEt.setText(user.weight)
                dobEt.setText(user.dob)
                if (user.gent == "female")
                {
                    val radioButtonId = R.id.female_radio_btn
                    gentRadioGroups.check(radioButtonId)
                }
                else
                {
                    val radioButtonId = R.id.male_radio_btn
                    gentRadioGroups.check(radioButtonId)
                }
            }
        }
    }

    private fun updateUser() {
        val id = sharedPreferences.getString("id", "id").toString()
        viewModel.updateUserDetails(id,nameEt.text.toString(),heightEt.text.toString(),weightEt.text.toString(),dobEt.text.toString(),genValue) { isSuccess ->
            if (isSuccess) {
                // Update successful
                println("Profile updated successfully.")
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Update failed
                println("Failed to update Profile.")
                Toast.makeText(this, "Failed to  update Profile.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToStorage(imageUri: Uri) {
        // Create a reference to the location in storage
        val imageRef = storageRef.child("images/${imageUri.lastPathSegment}")

        // Upload the image
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Image uploaded successfully
                // Get the download URL to save in Firestore
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imgString = downloadUri.toString()
                    sharedPreferences.edit().putString("image", imgString).apply()
                    println("SANTHOSH OK : "+imgString)
                    saveImageMetadataToFirestore(imgString)
                }
            }
            .addOnFailureListener {
                // Handle the error
                println("SANTHOSH ERROR : "+it.message)
                Toast.makeText(this, "Image upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageMetadataToFirestore(imageUrl: String) {

        loadImage()
        val id = sharedPreferences.getString("id", "id").toString()
        viewModel.updateUserProfile(id, imageUrl) { isSuccess ->
            if (isSuccess) {
                // Update successful
                println("profile image updated successfully.")
                Toast.makeText(this, "Image metadata saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Update failed
                println("Failed to update slot.")
                Toast.makeText(this, "Failed to  update profile image.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadImage() {

        val imageUrl = sharedPreferences.getString("image", "no_url").toString()
        if (imageUrl != "no_url" && imageUrl != "") {
            Glide.with(profile_img.context)
                .load(imageUrl)
                .into(profile_img)
        }
    }

    private fun requestLocationPermission() {
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted, get location
                //getCurrentLocation()
            } else {
                // Permissions denied, handle the case
                Toast.makeText(this, "Location permissions are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentLocation() {
        // Ensure permissions are granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not already granted
            requestLocationPermission()
            return
        }

        // Retrieve the last known location
        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val location = task.result
                if (location != null) {
                    // Latitude and longitude from the location
                    val latitude = location.latitude
                    val longitude = location.longitude

                    locationTxt.text = "Latitude: $latitude, Longitude: $longitude"
                    // Use the latitude and longitude as needed
                    locationUpdate("$latitude","$longitude")

                } else {
                    Toast.makeText(this, "Unable to retrieve location", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun locationUpdate(latitude: String,longitude: String) {

        loadImage()
        val id = sharedPreferences.getString("id", "id").toString()
        viewModel.updateDrLocation(id, latitude,longitude) { isSuccess ->
            if (isSuccess) {
                // Update successful
                println("Location updated successfully.")
                Toast.makeText(this, "Location updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Update failed
                println("Failed to update slot.")
                Toast.makeText(this, "Failed to  update profile image.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}