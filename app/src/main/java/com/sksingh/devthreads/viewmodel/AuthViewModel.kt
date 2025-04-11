package com.sksingh.devthreads.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sksingh.devthreads.models.UserModel
import com.sksingh.devthreads.utils.SharedPref
import java.util.UUID

class AuthViewModel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: MutableLiveData<FirebaseUser?> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    _firebaseUser.postValue(auth.currentUser)
                    getData(auth.currentUser!!.uid,context)
                } else {
                    _error.postValue(it.exception!!.message)
                    Log.d("Error", it.exception!!.message.toString())

                }
            }
    }

    private fun getData(uid: String,context: Context) {
        Log.d("FirebaseData", "Fetching data for UID: $uid")
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                Log.d("FirebaseData", "Raw snapshot: ${snapshot.value}")
                val userData = snapshot.getValue(UserModel::class.java)
                if (userData != null) {
                    SharedPref.storedata(
                        userData.name,
                        userData.email,
                        userData.username,
                        userData.imageUrl,
                        context,
                        userData.bio
                    )
                } else {
                    Toast.makeText(context, "Error Has Been found", Toast.LENGTH_SHORT).show()
                    Log.d("Error", "User data is null")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })


    }

    fun register(
        email: String,
        password: String,
        name: String,
        username: String,
        bio: String,
        imageUri: Uri?, // Có thể null
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    val uid = auth.currentUser?.uid

                    if (imageUri != null) {
                        saveImage(email, password, name, bio, username, imageUri, uid, context)
                    } else {
                        val defaultImageUrl = "https://bookvexe.vn/wp-content/uploads/2023/04/chon-loc-25-avatar-facebook-mac-dinh-chat-nhat_2.jpg" // link ảnh mặc định
                        saveData(email, password, name, bio, username, defaultImageUrl, uid, context)
                    }
                } else {
                    _error.postValue(it.exception!!.message)
                }
            }
    }


    private fun saveImage(
        email: String,
        password: String,
        name: String,
        bio: String,
        username: String,
        imageUri: Uri,
        uid: String?,
        context: Context
    ) {
        val newImageRef = storageRef.child("users/${UUID.randomUUID()}.jpg") // Move here

        val uploadTask = newImageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            newImageRef.downloadUrl.addOnSuccessListener {
                saveData(email, password, name, bio, username, it.toString(), uid, context)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Image upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            Log.e("UploadError", it.message.toString())
        }
    }


    private fun saveData(
        email: String,
        password: String,
        name: String,
        bio: String,
        username: String,
        imageUrl: String,
        uid: String?,
        context:Context
    ) {
        val firestoredb = Firebase.firestore
        val followerref = firestoredb.collection("followers").document(uid!!)
        val followingref = firestoredb.collection("following").document(uid!!)
        followingref.set(mapOf("followingIds" to listOf<String>()))
        followerref.set(mapOf("followersIds" to listOf<String>()))

        val userData = UserModel(email,password,name,bio,username,imageUrl,uid!!)

        userRef.child(uid!!).setValue(userData)
            .addOnSuccessListener {
                SharedPref.storedata(name,email,username,imageUrl, context,bio)
            }.addOnFailureListener {

            }
    }
     fun logout()
    {
        auth.signOut()
        _firebaseUser.postValue(null)
    }


}