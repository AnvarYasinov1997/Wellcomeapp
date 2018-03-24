package com.mistreckless.support.wellcomeapp.data.rxfirebase

import com.google.firebase.firestore.*
import io.reactivex.*
import io.reactivex.disposables.Disposables

class RxSetValue<T : Any>(private val ref: DocumentReference, private val value: T) :
    CompletableOnSubscribe {
    override fun subscribe(e: CompletableEmitter) {
        ref.set(value).addOnCompleteListener {
            if (!e.isDisposed) {
                if (it.isSuccessful) {
                    e.onComplete()
                } else {
                    e.onError(it.exception!!)
                }
            }
        }
    }
}

class RxGetValue<T>(private val ref: DocumentReference, private val clazz: Class<T>) :
    SingleOnSubscribe<T> {
    override fun subscribe(e: SingleEmitter<T>) = with(ref.get()) {
        addOnSuccessListener {
            if (!e.isDisposed)
                e.onSuccess(it.toObject(clazz))
        }
        addOnFailureListener {
            if (!e.isDisposed)
                e.onError(it)
        }
        Unit
    }

}

class RxAddValue<T : Any>(private val ref: CollectionReference, private val value: T) :
    CompletableOnSubscribe {
    override fun subscribe(e: CompletableEmitter) {
        ref.add(value).addOnCompleteListener {
            if (!e.isDisposed)
                if (it.isSuccessful) e.onComplete() else e.onError(it.exception!!)
        }
    }

}

class RxQuery<T>(private val query: Query, private val clazz: Class<T>) :
    SingleOnSubscribe<List<T>> {
    override fun subscribe(e: SingleEmitter<List<T>>) {
        query.get()
            .addOnSuccessListener {
                if (!e.isDisposed)
                    e.onSuccess(if (it.documents.isNotEmpty()) it.toObjects(clazz) else emptyList())
            }
            .addOnFailureListener {
                if (!e.isDisposed)
                    e.onError(it)
            }
    }
}

class RxDocumentObserver<T>(
    private val documentReference: DocumentReference,
    private val options: DocumentListenOptions,
    private val clazz: Class<T>
) : ObservableOnSubscribe<T> {
    override fun subscribe(e: ObservableEmitter<T>) {
        val listener =
            EventListener<DocumentSnapshot> { documentSnapshot, firebaseFirestoreException ->
                if (!e.isDisposed) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val value = documentSnapshot.toObject(clazz)
                        if (value == null) e.onError(Exception("cannot parse class " + clazz.canonicalName))
                        else e.onNext(value)
                    } else e.onError(
                        firebaseFirestoreException ?: Exception("firebase firestore exception")
                    )
                }
            }
        val registration = documentReference.addSnapshotListener(options, listener)
        e.setDisposable(Disposables.fromAction { registration.remove() })
    }
}