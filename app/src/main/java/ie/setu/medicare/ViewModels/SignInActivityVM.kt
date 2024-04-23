package ie.setu.medicare.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ie.setu.medicare.Model.Category
import ie.setu.medicare.Model.CategoryList
import ie.setu.medicare.Model.Users
import ie.setu.medicare.View.SignUpActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject


class SignInActivityVM : ViewModel() {

    ///private val _user = MutableLiveData<Users>()
    ///val user: LiveData<Users> = _user

    val database = Firebase.database

    fun userValidation(email: String,password: String,isGoogleLogin: Boolean, callback: (Boolean,Users?) -> Unit) {
       /// val database = Firebase.database
        val usersRef = database.getReference("users")

        // Query the database to check if the email exists
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Check if any user has the same email
                    if (dataSnapshot.exists()) {
                        // Email exists, retrieve user info
                        val user = dataSnapshot.children.first().getValue(Users::class.java)
                        if (user != null) {
                            println("SANTHOSH User exists:")
                            if (isGoogleLogin)
                            {
                                callback(true, user)
                            }
                            else {
                                if (user.password == password) {
                                    println("SANTHOSH Name: ${user.name}")
                                    println("SANTHOSH Email: ${user.email}")
                                    println("SANTHOSH Type: ${user.type}")
                                    println("SANTHOSH Password: ${user.password}")
                                    // Do something with user information
                                    callback(true, user)
                                } else {
                                    callback(false, null)
                                }
                            }
                        } else {
                            println("SANTHOSH Email does not exist")
                            callback(false,null)
                        }

                    } else {
                        // Email does not exist
                        callback(false,null)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    println("Error reading database: ${databaseError.toException()}")
                    callback(false,null) // Return null if an error occurs
                }
            }
        )
    }
    fun creatingAnNewUser(name:String,email:String,place:String,phone:String,cat:String,catName:String,pass:String,type:Int, callback: (Boolean,String?) -> Unit) {
        // Get a reference to your Firebase database
        println("SANTHOSH BUTTON REG AAAA")

        val myRef = database.getReference("users")

        val userId = myRef.push().key
        val user = SignUpActivity.User(
            id = "$userId",
            name = "$name",
            email = "$email",
            phone = "$phone",
            type = type,
            password = "$pass",
            catId = "$cat",
            catName = "$catName",
            place = "$place"
        )

        // Push the user data to the database under the generated key
        userId?.let {
            println("SANTHOSH BUTTON REG BBBB")
            myRef.child(userId).setValue(user)
                .addOnSuccessListener {
                    // Data successfully written
                    println("SANTHOSH BUTTON REG CCCCCC")
                    println("SANTHOSH Data successfully written to the database.")
                    callback(true,userId)
                }
                .addOnFailureListener { e ->
                    // Failed to write data
                    println("SANTHOSH BUTTON REG DDDDDD")
                    println("SANTHOSH Error writing data to the database: $e")
                    callback(false,"")
                }
        } ?: run {
            println("SANTHOSH BUTTON REG EEEEEEE")
            println("SANTHOSH Error generating unique key.")
        }
    }

    fun insertCategory(category: Category, callback: (Boolean) -> Unit) {
        val categoriesRef: DatabaseReference = database.getReference("categories")
        val categoryId = category.catId
        categoriesRef.child(categoryId).setValue(category)
            .addOnSuccessListener {
                // Successfully inserted
                callback(true)
            }
            .addOnFailureListener {
                // Failed to insert
                callback(false)
            }
    }

    fun getCategories(callback: (List<CategoryList>?) -> Unit) {
        val categoriesRef: DatabaseReference = database.getReference("categories")
        categoriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryList = mutableListOf<CategoryList>()
                for (categorySnapshot in snapshot.children) {
                    val catId = categorySnapshot.child("catId").getValue(String::class.java)
                    val catName = categorySnapshot.child("catName").getValue(String::class.java)
                    if (catId != null && catName != null) {
                        categoryList.add(CategoryList(catId, catName,false,false))
                    }
                }
                callback(categoryList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                callback(null)
            }
        })
    }

    fun deleteCategory(catId: String, callback: (Boolean) -> Unit) {
        val categoriesRef: DatabaseReference = database.getReference("categories")
        // Locate the category with the given catId in the categoriesRef
        categoriesRef.child(catId).removeValue()
            .addOnSuccessListener {
                // Successfully deleted
                callback(true)
            }
            .addOnFailureListener {
                // Failed to delete
                callback(false)
            }
    }

    fun editCategory(catId: String, updatedCategoryName: String, callback: (Boolean) -> Unit) {
        // Reference the category item in the database using catId
        val categoriesRef: DatabaseReference = database.getReference("categories")
        val categoryRef = categoriesRef.child(catId)

        // Convert updated category to a map of properties to update
        val updates = mapOf(
            "catId" to catId,
            "catName" to updatedCategoryName
        )

        // Update the category in the database
        categoryRef.updateChildren(updates)
            .addOnSuccessListener {
                // Update successful
                callback(true)
            }
            .addOnFailureListener {
                // Update failed
                callback(false)
            }
    }

    fun getDrList(callback: (List<Users>?) -> Unit) {
        // Query to retrieve items with type = 1
        val usersRef: DatabaseReference = database.getReference("users")
        val query = usersRef.orderByChild("type").equalTo(1.0) // Specify type as double

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val itemList = mutableListOf<Users>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Users::class.java)
                    item?.let {
                        itemList.add(it)
                    }
                }
                callback(itemList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                callback(null)
            }
        })
    }

}