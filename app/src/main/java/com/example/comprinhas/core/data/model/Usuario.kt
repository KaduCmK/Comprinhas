package com.example.comprinhas.core.data.model

import com.example.comprinhas.list.data.model.ShoppingItem
import com.google.firebase.auth.FirebaseUser
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val uid: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
) {
    constructor(firebaseUser: FirebaseUser) : this(
        uid = firebaseUser.uid,
        photoUrl = firebaseUser.photoUrl.toString(),
        displayName = firebaseUser.displayName ?: "",
        email = firebaseUser.email ?: ""
    )
}
