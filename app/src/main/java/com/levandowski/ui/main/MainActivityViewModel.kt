package com.levandowski.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.levandowski.StuffChatApplication
import com.levandowski.data.FirebaseManager
import com.levandowski.data.FirebaseManager.Companion.COLLECTION_CHAT
import com.levandowski.data.FirebaseManager.Companion.COLLECTION_USER
import com.levandowski.data.preferences.AppPreferencesHelper
import com.levandowski.data.repository.UserRepository
import com.levandowski.model.User
import kotlinx.coroutines.*
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {

    @Inject
    lateinit var manager: FirebaseManager

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var appPreferencesHelper: AppPreferencesHelper

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Default + viewModelJob)

    init {
        StuffChatApplication.applicationComponent.inject(this)
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }

    fun addUserFirebase(userFirebase: FirebaseUser) {
        buildUser(userFirebase)?.let { user ->
            manager.add(COLLECTION_USER, user.token, user).addOnSuccessListener {
               saveLocalData(user)
            }.addOnFailureListener {
                Log.d("addUserFirebase: ", it.message)
            }
        }
    }

    fun  deleteUserFirebase() = getToken()?.let {
        manager.deleteAllDocumentSubCollection(
            COLLECTION_USER, it, COLLECTION_CHAT
        )
    }

    private fun addUserRepository(user: User) = uiScope.launch {
            userRepository.insertTask(user)
        }

    fun removeUserRepository() = uiScope.launch {
        getToken()?.let { userRepository.deleteTask(it) }
    }

    private fun buildUser(user: FirebaseUser): User? {
        user.getIdToken(true).addOnSuccessListener {

            it.token?.let { token ->
                return@let User().apply { user.run {
                        this@apply.token = token
                        this@apply.email = email.toString()
                        this@apply.name = displayName.toString()
                        this@apply.phone = phoneNumber.toString()
                        this@apply.photoURL = photoUrl.toString()
                        this@apply.timeStamp = it.authTimestamp
                    }
                }
            }
        }.addOnFailureListener {
            Log.d("buildUser(): ", it.message)
        }
        return null
    }

    fun getToken() = appPreferencesHelper.token

    private fun saveLocalData(user: User) {
        addUserRepository(user)
        savePreferences(user)
    }

    private fun savePreferences(user: User) =
        appPreferencesHelper.apply {
            this.id = user.id
            this.token = user.token
            this.authTimeStamp = user.timeStamp
    }

    private fun removePreferences() {}
}
