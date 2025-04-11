package com.sksingh.devthreads.data
import android.util.Log
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

class FirebaseService {
    private val db = FirebaseDatabase.getInstance("https://petland-6a672-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .getReference("HealthcareTest")

    suspend fun getQuestionsForPet(pet: String): Map<String, List<String>> {
        val snapshot = db.child(pet).get().await()
        val result = mutableMapOf<String, List<String>>()

        for (category in snapshot.children) {
            val questions = category.children.mapNotNull { it.getValue(String::class.java) }
            result[category.key ?: ""] = questions
        }
        return result
    }
}
