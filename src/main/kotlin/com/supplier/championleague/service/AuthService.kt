package main.kotlin.com.supplier.championleague.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AuthService {

    fun verifyToken(token: String): FirebaseToken? {
        return try {
            FirebaseAuth.getInstance().verifyIdToken(token)
        } catch (e: Exception) {
            null
        }
    }
}