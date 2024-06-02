package com.example.pizzasatpovo.database
import com.example.pizzasatpovo.data.dataModel.Pizza
import com.example.pizzasatpovo.data.dataModel.ResponseData
import com.example.pizzasatpovo.data.dataModel.RetrievedPizza
import com.example.pizzasatpovo.data.dataModel.Topping
import com.example.pizzasatpovo.data.dataModel.UserData
import com.example.pizzasatpovo.database.sign_in.GoogleAuthUiClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FavouritesManager(private val googleAuthUiClient: GoogleAuthUiClient) {

    private val auth = Firebase.auth

    suspend fun addFavourite(pizzaName: String): ResponseData<Boolean>? = auth.currentUser?.run {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(uid)
        val user = googleAuthUiClient.retrieveUserData() ?: userRef.get().await().toObject(UserData::class.java) ?: return ResponseData()

        val pizzaRef = db.collection("pizzas").document(pizzaName)
        if (!pizzaRef.get().await().exists()) return ResponseData(false, "Pizza doesn't exist")

        user.favourites = (user.favourites ?: arrayListOf()).apply { add(pizzaRef) }
        userRef.set(user).await()
        return ResponseData(true, "Pizza added successfully")
    }

    suspend fun removeFavourite(pizzaName: String): ResponseData<Boolean>? = auth.currentUser?.run {
        val db = Firebase.firestore
        val userRef = db.collection("users").document(uid)
        val user = googleAuthUiClient.retrieveUserData() ?: userRef.get().await().toObject(UserData::class.java) ?: return ResponseData()

        val pizzaRef = db.collection("pizzas").document(pizzaName)
        if (!pizzaRef.get().await().exists()) return ResponseData(false, "Pizza doesn't exist")

        user.favourites?.removeAll { it.id == pizzaRef.id }
        userRef.set(user).await()
        return ResponseData(true, "Pizza removed successfully")
    }

    suspend fun retrieveFavourites(): ResponseData<ArrayList<RetrievedPizza>>? = auth.currentUser?.run {
        val user = googleAuthUiClient.retrieveUserData() ?: return ResponseData(message = "Error fetching the user")
        val favourites: ArrayList<RetrievedPizza> = arrayListOf()
        user.favourites?.forEach { pizzaDocument ->
            val pizzaDB = pizzaDocument.get().await().toObject(Pizza::class.java) ?: return@forEach
            val toppings = arrayListOf<Topping>().apply {
                pizzaDB.toppings?.forEach { documentRef ->
                    add(documentRef.get().await().toObject(Topping::class.java) ?: return@forEach)
                }
            }
            favourites.add(RetrievedPizza(name = pizzaDB.name, toppings = toppings, image = pizzaDB.image))
        }
        return ResponseData(true, "Pizza retrieved successfully", favourites)
    }
}
