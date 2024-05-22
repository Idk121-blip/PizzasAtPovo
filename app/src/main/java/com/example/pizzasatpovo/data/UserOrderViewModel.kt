package com.example.pizzasatpovo.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.input.key.Key
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pizzasatpovo.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserOrderViewModel : ViewModel( ) {

    private val CHANNEL_ID = "basic_notifications"
    private var context: Context?= null


    // Map to keep track of listeners and their respective references
    private val listenerMap = mutableMapOf<DatabaseReference, ValueEventListener>()
    private val listenerMapNumberRequest= mutableMapOf<DatabaseReference, Int>()




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
                    // Handle error
                }
            }
            documentRef.addValueEventListener(listener)
            listenerMap[documentRef] = listener // Store the listener
            listenerMapNumberRequest[documentRef] = 0;
        }
    }

    private fun handleDatabaseChange(snapshot: DataSnapshot) {


        val order = snapshot.getValue(RealTimeOrder::class.java)
        if (order != null) {
            if (order.completed){
                sendNotification(context!!, "Order Updated: ${order.pizzaName}", 123)
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
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context!!.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(context: Context, message: String, notificationId: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.margherita)
            .setContentTitle("Order Notification")
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
