package com.example.pizzasatpovo.presentation.sing_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.example.pizzasatpovo.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.concurrent.CancellationException

class GoogleAuthUiClient (private val context: Context, private val oneTapClient: SignInClient) {
    private val auth= Firebase.auth

    //Potrebbe volerci tempi per fare il signin quindi suspend
    suspend fun signIn(): IntentSender?{
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        }catch (e: Exception){
            if (e is CancellationException) {
                throw e
            }
            null
        }
        return result?.pendingIntent?.intentSender //intendenr con le information del client
    }

    suspend fun signInWithIntent(intent: Intent):SignInResult{
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken= credential.googleIdToken
        val googleCredentials= GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user= auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                null
            )
        }catch (e: Exception) {
            if (e is CancellationException) throw e
            SignInResult(null, e.message)
        }
    }

    suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }catch (e: Exception){
            if (e is CancellationException) throw e

        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run{
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }
    private fun buildSignInRequest(): BeginSignInRequest{
        return BeginSignInRequest.builder().
        setGoogleIdTokenRequestOptions(
            GoogleIdTokenRequestOptions.builder().
            setSupported(true).
            setFilterByAuthorizedAccounts(false).
            setServerClientId(context.getString(R.string.web_client_id)).
            build())
            .setAutoSelectEnabled(true) //automaticamente entra se ha un account
            .build()
    }
}