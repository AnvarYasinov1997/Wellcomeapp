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
                e.onSuccess(it.toObject(clazz)!!)
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

class RxDocumentObserver<T : Any>(
    private val documentReference: DocumentReference,
    private val options: DocumentListenOptions,
    private val clazz: Class<T>
) : ObservableOnSubscribe<DocumentState<T>> {
    override fun subscribe(e: ObservableEmitter<DocumentState<T>>) {
        val listener =
            EventListener<DocumentSnapshot> { documentSnapshot, exception ->
                if (!e.isDisposed) when {
                    documentSnapshot != null && exception != null -> e.onNext(
                        DocumentError(
                            exception,
                            documentSnapshot.reference
                        )
                    )
                    documentSnapshot != null && !documentSnapshot.exists() -> e.onNext(
                        DocumentRemoved(
                            documentSnapshot.reference
                        )
                    )
                    documentSnapshot != null -> {
                        val value = documentSnapshot.toObject(clazz)
                        if (value == null) e.onNext(
                            DocumentError(
                                Exception("cannot parse class " + clazz.canonicalName),
                                documentSnapshot.reference
                            )
                        )
                        else e.onNext(
                            DocumentModified(
                                value,
                                documentSnapshot.metadata,
                                documentSnapshot.reference
                            )
                        )
                    }
                }
            }
        val registration = documentReference.addSnapshotListener(options, listener)
        e.setDisposable(Disposables.fromAction(registration::remove))
    }
}

class RxQueryObserver<T : Any>(
    private val query: Query,
    private val options: QueryListenOptions,
    private val clazz: Class<T>
) : ObservableOnSubscribe<DocumentState<T>> {
    override fun subscribe(e: ObservableEmitter<DocumentState<T>>) {
        val listener = EventListener<QuerySnapshot> { querySnapshot, exception ->
            if (!e.isDisposed && querySnapshot != null) when {
                exception != null -> {
                }
                else -> querySnapshot.documentChanges.forEach {
                    when (it.type) {
                        DocumentChange.Type.ADDED -> e.onNext(
                            DocumentAdded(
                                it.document.toObject(
                                    clazz
                                ), querySnapshot.metadata, it.document.reference
                            )
                        )
                        else -> {
                        } //do nothing
                    }
                }
            }
        }
        val registration = query.addSnapshotListener(options, listener)
        e.setDisposable(Disposables.fromAction(registration::remove))
    }

}