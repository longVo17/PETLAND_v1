package com.sksingh.devthreads

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.FirebaseDatabase
import com.sksingh.devthreads.navigations.NavGraph
import com.sksingh.devthreads.ui.theme.DevthreadsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DevthreadsTheme(true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
        testFirebaseConnection()
    }


private fun testFirebaseConnection() {
        val database = FirebaseDatabase.getInstance("https://devthreads-42de5-default-rtdb.asia-southeast1.firebasedatabase.app")
        val testRef = database.getReference("testConnection3")

        // Write data to Firebase
        testRef.setValue("Hello, Firebase! x")
            .addOnSuccessListener {
                Log.d("FirebaseTest", "Data written successfully!")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseTest", "Failed to write data: ${exception.message}")
            }

        // Read data from Firebase
        testRef.get().addOnSuccessListener { snapshot ->
            val value = snapshot.getValue(String::class.java)
            Log.d("FirebaseTest", "Data read successfully: $value")
        }.addOnFailureListener { exception ->
            Log.e("FirebaseTest", "Failed to read data: ${exception.message}")
        }
    }
}