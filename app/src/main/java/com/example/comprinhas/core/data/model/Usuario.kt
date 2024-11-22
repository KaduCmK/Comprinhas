package com.example.comprinhas.core.data.model

import com.google.firebase.auth.FirebaseUser
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val uid: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null
) {
    constructor(usuarioFirebase: FirebaseUser) : this(
        uid = usuarioFirebase.uid,
        photoUrl = usuarioFirebase.photoUrl.toString(),
        displayName = usuarioFirebase.displayName ?: "",
        email = usuarioFirebase.email ?: ""
    )
}
