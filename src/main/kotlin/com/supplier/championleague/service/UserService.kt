package main.kotlin.com.supplier.championleague.service

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Firestore
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.database.FirebaseDatabase
import jakarta.enterprise.context.ApplicationScoped
import main.kotlin.com.supplier.championleague.model.User
import main.kotlin.com.supplier.championleague.repositories.UserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ApplicationScoped
class UserService (val userRepository: UserRepository) {

    //private val db: Firestore = FirestoreClient.getFirestore()

    fun postUser(uid: String, name: String, email: String): Map<String, Any>? {
        val data = mapOf("name" to name, "email" to email)
        val user: User = User(null, name, email)
        userRepository.createUser(user)
        return getUser(uid)
    }

    fun getUsers(): ArrayList<Map<String, Any>>? {
        // val userSnapshots = db.collection("users").get().get()
        // val userSnapshot:ArrayList<Map<String, Any>>? = userRepository.getUsers()
        // val users: List<DocumentSnapshot> = userSnapshots.toList()

        //val users: List<User> = userSnapshots.toList()
        // val list: ArrayList<Map<String, Any>> = arrayListOf()
        // userSnapshot?.forEach {
        //     println(it)
        //     list.add(it)
        // }
        // return if (userSnapshot.size()>0) list else null
        return userRepository.getUsers()
    }

    fun getUser(uid: String): Map<String, Any>? {
        // val userSnapshot = db.collection("users").document(uid).get().get()
        return userRepository.getUser(uid)
    }
}