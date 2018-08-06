package com.wellcome.firestore

import com.google.cloud.firestore.Firestore
import com.wellcome.configuration.message.*
import com.wellcome.configuration.utils.LoggerHandler
import kotlinx.coroutines.experimental.newSingleThreadContext
import wellcome.common.mpp.core.FirebaseConstants
import wellcome.common.mpp.entity.LocalityData
import wellcome.common.mpp.entity.UserData

interface FirestoreService {
    suspend fun saveNewLocality(message: CreateLocalityMessage): FirestoreRpcReturnMessage
    suspend fun saveNewUser(message: CreateUserMessage): FirestoreRpcReturnMessage

}

class FirestoreServiceImpl(private val db: Firestore,
                           private val logger: LoggerHandler) : FirestoreService {

    private val firestoreContext by lazy {
        newSingleThreadContext("FirestoreContext")
    }

    override suspend fun saveNewLocality(message: CreateLocalityMessage): FirestoreRpcReturnMessage {
        val locality = LocalityData(message.firestoreRef, message.name, message.timezoneId)
        val savedState =
            db.collection(FirebaseConstants.LOCALITY).document(message.firestoreRef)
                .setValue(firestoreContext, locality).await()
        return when (savedState) {
            is FirestoreSuccess -> {
                logger.info("successfully saved locality ${message.name} ${savedState.writeResult}")
                LocalityCreatedMessage
            }
            is FirestoreError   -> {
                logger.error("error saving locality ${message.name} ${savedState.exception.message}")
                LocalityNotCreatedMessage
            }
        }
    }

    override suspend fun saveNewUser(message: CreateUserMessage): FirestoreRpcReturnMessage {
        val user = UserData(message.googleUid,
            message.firestoreRef,
            message.name,
            message.photoUrl,
            message.email,
            message.localityFirestoreRef,
            message.localityName)
        val savedState =
            db.collection(FirebaseConstants.USER).document(message.firestoreRef).setValue(firestoreContext, user)
                .await()
        return when (savedState) {
            is FirestoreSuccess -> {
                logger.info("successfully saved user ${user.name} ${savedState.writeResult}")
                UserCreatedMessage
            }
            is FirestoreError   -> {
                logger.error("error saving user ${user.name} ${savedState.exception.message}")
                UserNotCreatedMessage
            }
        }
    }

}