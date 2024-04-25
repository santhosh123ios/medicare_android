package ie.setu.medicare.View



import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.setu.medicare.databinding.ActivitySignInBinding
//import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ie.setu.medicare.Model.Users
import ie.setu.medicare.R
import ie.setu.medicare.ViewModels.SignInActivityVM

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    //private lateinit var viewModel: SignInActivityVM
    private val viewModel: SignInActivityVM by viewModels()
    private lateinit var sharedPreferences: SharedPreferences


//    data class User(
//        val name: String = "",
//        val email: String = "",
//        val phone: String = "",
//        val type: Int = 0,
//        val password: String = "",
//        val catId: String = "",
//        val place: String = ""
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("auth_pref", Context.MODE_PRIVATE)
        configureGoogleSignIn()
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)

        // Set user data using ViewModel
//        viewModel.setUser("John Doe", "john@example.com")
//        // Observe changes in ViewModel and update UI
//        viewModel.user.observe(this, { user ->
//            //nameTextView.text = user.name
//            ///emailTextView.text = user.email
//        })

        ////GOOGLE LOGIN
        signInButton.setOnClickListener {
            signIn()
        }

        binding.textView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                viewModel.userValidation(email,pass,false) { status,user ->
                    if (status)
                    {
                        Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show()
                        if (user != null) {
                            loginUser(user)
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(user:Users) {
        // Update authentication status in SharedPreferences
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("name", user.name).apply()
        sharedPreferences.edit().putString("email", user.email).apply()
        sharedPreferences.edit().putInt("type", user.type).apply()
        sharedPreferences.edit().putString("id", user.id).apply()
        sharedPreferences.edit().putString("image", user.image).apply()

        when ( user.type) {
            0 ->  navigateToHome()
            1 -> navigateToDRHome()
            3 -> navigateToAdminHome()
            else -> { // Note the block
                print("user type is not available")
            }
        }
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

    ////GOOGLE LOGIN
    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("SANTHOSH onActivityResult A")
        super.onActivityResult(requestCode, resultCode, data)
        println("SANTHOSH onActivityResult B")
        if (requestCode == RC_SIGN_IN) {
            println("SANTHOSH onActivityResult C")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        println("SANTHOSH handleSignInResult A")
        try {
            println("SANTHOSH handleSignInResult B")
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            if (account != null)
            {
                val emailToCheck = "santhosh@gmail.com"+account.email
                val password = ""
                viewModel.userValidation(emailToCheck,password,true) { status,user ->
                    if (status)
                    {
                        Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show()
                        if (user != null) {
                            loginUser(user)
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "This user not available, please register", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {

            }
        } catch (e: ApiException) {
            println("SANTHOSH handleSignInResult C : "+e.statusCode)
            // The ApiException status code indicates the detailed failure reason.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, "signIn failed, please try again", Toast.LENGTH_SHORT).show()
        }
    }
}