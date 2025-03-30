package main.kotlin.com.supplier.championleague.repositories

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.database.FirebaseDatabase
import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.model.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ApplicationScoped
class UserRepository {
    private val db: Firestore = FirestoreClient.getFirestore()
    private val userCollection = db.collection("users")
    // private val collection = FirebaseDatabase.getInstance().reference.child("users")

/*    fun addUser(uid: String, name: String, email: String): Map<String, Any>? {
        val userRef = db.collection("users").document(uid)
        val data = mapOf("name" to name, "email" to email)
        println(data)
        userRef.set(data)
        return getUser(uid)
    }*/

    @OptIn(ExperimentalUuidApi::class)
    fun createUser(user: User): User {
        val userId = user.id ?: (Uuid.random() as String)  // 🔹 Generate ID if not provided
        val newUser = user.copy(id = userId)
        userCollection.add(newUser).get() // 🔹 Save to Firebase
        return newUser  // Return saved user
    }

    fun updateUser(user: User): User {
        val userId: String? = user.id
        val documentReference : DocumentReference = userId ?.let { userCollection.document(it) }!!
        if (documentReference!=null) {
            documentReference.set(user)
        }
        return user
    }

    fun getUsers(): ArrayList<Map<String, Any>>? {
        val userSnapshots = userCollection.get().get()
        val users: List<DocumentSnapshot> = userSnapshots.toList()
        val list: ArrayList<Map<String, Any>> = arrayListOf()
        users.forEach {
            list.add(it.data!!)
        }
        return if (userSnapshots.size()>0) list else null
    }

    fun getUser(uid: String): Map<String, Any>? {
        val userSnapshot = userCollection.document(uid).get().get()
        return if (userSnapshot.exists()) userSnapshot.data else null
    }

    fun removeUser(uid: String): Boolean {
        val documentReference: DocumentReference = userCollection.document(uid)
        documentReference.delete()
        return true
    }
}