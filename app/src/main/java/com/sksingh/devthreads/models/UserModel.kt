package com.sksingh.devthreads.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class  UserModel(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val bio: String = "",
    val username: String = "",
    val imageUrl: String = "",
    val uid: String = ""
)
