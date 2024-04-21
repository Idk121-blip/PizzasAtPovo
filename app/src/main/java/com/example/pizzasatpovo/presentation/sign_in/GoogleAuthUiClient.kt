package com.example.pizzasatpovo.presentation.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.UserData
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth
    private var userData:UserData?=null
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }

        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val firebaseUser = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = firebaseUser?.run {
                    var user: UserData? = null
                    val db = Firebase.firestore
                    val userRef = db.collection("users").document(uid)
                    userRef.get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                user= documentSnapshot.toObject(UserData::class.java)

                            } else {
                                user = UserData(
                                    name = displayName,
                                    credit = 50.0,
                                    image = photoUrl?.toString(),
                                )

                                db.collection("users").document(uid).set(user!!)

                            }
                        }
                        .addOnFailureListener { e ->
                            println(e)
                        }.await()
                    userData=user
                    println(userData)
                    println(user)
                    user
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }





    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        userData
    }
    suspend fun retrieveUserData(): UserData? = auth.currentUser?.run {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(uid)
        userData= userRef.get().await().toObject(UserData::class.java)
        userData
    }




    private fun getUserInformation(): Boolean? = auth.currentUser?.run {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(uid)
        val pizzaRef= db.collection("pizze").document("Margherita")
        db.runTransaction { transaction ->
            if (!transaction.get(userRef).exists()) {
                val user = UserData(
                    name = displayName,
                    credit = 50.0,
                    favourites = arrayListOf(pizzaRef),
                    image = photoUrl?.toString(),
                    orders = null
                )
                transaction.set(userRef, user)
            }
        }

        true
    }


    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}