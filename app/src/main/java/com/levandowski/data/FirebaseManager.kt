package com.levandowski.data

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.levandowski.R

class FirebaseManager {

    private var dataBase = FirebaseFirestore.getInstance()
    private var authUI = AuthUI.getInstance()
    private var auth = FirebaseAuth.getInstance()

    fun getIntentNewCredentials() =
        authUI.createSignInIntentBuilder().setLogo(R.mipmap.ic_launcher).build()

    fun getCurrentUser() = auth.currentUser

    fun getDataBase() = dataBase

    fun signOut(context: Context) = authUI.signOut(context)

    fun add(path: String, document: String, value: Any) =
        dataBase.collection(path).document(document).set(value)

    fun addSubCollection(path: String, document: String, nameChild: String, value: Any) =
        referenceSubCollection(path,document, nameChild).set(value)

    private fun referenceSubCollection(path: String, document:String, subCollection: String) =
        dataBase.collection(path).document(document).collection(subCollection).document()

    fun deleteAllDocumentSubCollection(path: String, document: String, subCollection: String) =
        dataBase.collection(path).document(document).collection(subCollection).document().delete()

    fun query(path: String)  = dataBase.collection(path).get()

    fun querySubCollection(path: String, document: String, subCollection: String) =
        dataBase.collection(path).document(document).collection(subCollection)
}