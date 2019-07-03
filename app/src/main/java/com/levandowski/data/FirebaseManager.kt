package com.levandowski.data

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseManager @Inject constructor() {

    private var dataBase = FirebaseFirestore.getInstance()

    companion object {
        const val COLLECTION_CHAT = "chat"
        const val COLLECTION_USER = "user"
    }

    fun getDataBase() = dataBase

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
