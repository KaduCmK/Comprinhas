package com.example.comprinhas.auth.data

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.comprinhas.core.data.UsuarioService
import com.example.comprinhas.core.data.model.Usuario
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class AuthService @Inject constructor (
    private val usuariosService: UsuarioService,
    private val context: Context
) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun googleSignIn(): Flow<Result<AuthResult>> {
        val firebaseAuth = FirebaseAuth.getInstance()

        return callbackFlow {
            try {

                val ranNonce = UUID.randomUUID().toString()
                val bytes = ranNonce.toByteArray()
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(bytes)
                val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("600733064538-q8t5nvkqs4mdfhcbara67d794cdiamha.apps.googleusercontent.com")
                    .setNonce(hashedNonce)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                    usuariosService.setUsuario(Usuario(authResult.user!!))

                    trySend(Result.success(authResult))
                } else throw RuntimeException("Received an invalid credential type")
            } catch (e: GetCredentialCancellationException) {
                trySend(Result.failure(Exception("Sign-in was cancelled.")))
            } catch (e: Exception) {
                Log.e("AuthService", "Error signing in", e)
                trySend(Result.failure(e))
            }
            awaitClose { }
        }
    }

    fun signOut() = FirebaseAuth.getInstance().signOut()

    fun getSignedInUser() = FirebaseAuth.getInstance().currentUser
}