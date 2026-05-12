package com.example.nammashaale.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val schoolName: String = "",
    val email: String = ""
)

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private val usersCollection = firestore.collection("users")

    suspend fun saveUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            usersCollection.document(profile.uid).set(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(uid: String): Result<UserProfile?> {
        return try {
            val snapshot = usersCollection.document(uid).get().await()
            Result.success(snapshot.toObject(UserProfile::class.java))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
