// Start writing Firebase Functions
// https://firebase.google.com/docs/functions/typescript

import {DocumentSnapshot} from "firebase-functions/lib/providers/firestore";
import * as admin from "firebase-admin";
import DocumentReference = admin.firestore.DocumentReference;
import CollectionReference = FirebaseFirestore.CollectionReference;

'use-strict';

const functions = require('firebase-functions');
const firebaseAdmin = require('firebase-admin');

firebaseAdmin.initializeApp();


//
// exports.likeAdded = functions.firestore.document("city/{cityId}/event/{eventId}/like/{likeId}").onCreate((snapshot, context) => {
//     const like = snapshot.data();
//     console.log("likeCreated", like);
//     return Promise.all([snapshot.ref.parent.parent.get().then((eventSnapshot: DocumentSnapshot) => {
//         let count: number = eventSnapshot.get("likeCount");
//         console.log("likeCount", count);
//         count++;
//         return snapshot.ref.parent.parent.set({
//             likeCount: count
//         }, {merge: true})
//     }), updateStoryLike(like.userRef, snapshot.ref.parent.parent.id, true, Date.now())])
// });
//
// exports.likeRemoved = functions.firestore.document("city/{cityId}/event/{eventId}/like/{likeId}").onDelete((snapshot, context) => {
//     const like = snapshot.data();
//     console.log("likeRemoved", like);
//     const eventRef = snapshot.ref.parent.parent;
//     return Promise.all([eventRef.get().then((eventSnapshot: DocumentSnapshot) => {
//         let count: number = eventSnapshot.get("likeCount");
//         console.log("likeCount", count);
//         count--;
//         return eventRef.update({
//             likeCount: count
//         }, {merge: true})
//     }), updateStoryLike(like.userRef,snapshot.ref.parent.parent.id,false,Date.now())])
// });
//
// function updateStoryLike(userRef: String, eventRef: String, isLiked: boolean, timeStamp: number): Promise<any> {
//     const storyRef: DocumentReference = firebaseAdmin.firestore().collection("user/" + userRef + "/story").doc(eventRef);
//     return storyRef.set({
//         eventRef: eventRef,
//         liked: isLiked,
//         timestamp: timeStamp
//     }, {merge: true});
// }
