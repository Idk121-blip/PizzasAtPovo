package com.example.pizzasatpovo.data.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.model.RealTimeOrder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class NotificationViewModel : ViewModel() {

    private val channelId = "basic_notifications"
    @SuppressLint("StaticFieldLeak")
    private var context: Context? = null

    // Map to keep track of listeners and their respective references
    private val listenerMap = mutableMapOf<DatabaseReference, ValueEventListener>()
    private val listenerMapNumberRequest = mutableMapOf<DatabaseReference, Int>()

    fun setContext(newContext: Context) {
        context = newContext
        createNotificationChannel()
    }

    fun addListenerForSpecificDocuments(documentRefs: List<DatabaseReference>) {
        for (documentRef in documentRefs) {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    handleDatabaseChange(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Document has been deleted, documento eliminato")
                }
            }
            documentRef.addValueEventListener(listener)
            listenerMap[documentRef] = listener // Store the listener
            listenerMapNumberRequest[documentRef] = 0
        }
    }

    private fun handleDatabaseChange(snapshot: DataSnapshot) {
        val order = snapshot.getValue(RealTimeOrder::class.java)
        if (order != null) {
            if (order.completed) {
                sendNotification(context!!, "Il tuo ordine è pronto: ${order.pizzaName}", Random.nextInt(100, 300))
                snapshot.ref.removeValue() // Delete the document after processing
                // Remove the listener after deleting the document
                val listener = listenerMap[snapshot.ref]
                if (listener != null) {
                    snapshot.ref.removeEventListener(listener)
                    listenerMap.remove(snapshot.ref)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Basic Notifications"
            val descriptionText = "Channel for basic notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(context: Context, message: String, notificationId: Int) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.margherita)
            .setContentTitle("Notifica ordine")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                return
            }
            notify(notificationId, builder.build())
        }
    }
}



