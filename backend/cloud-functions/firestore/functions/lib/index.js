'use-strict';
"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const functions = require('firebase-functions');
const firebaseAdmin = require('firebase-admin');
firebaseAdmin.initializeApp();
exports.likeAdded = functions.firestore.document("city/{cityId}/event/{eventId}/like/{likeId}").onCreate((snapshot, context) => {
    const like = snapshot.data();
    console.log("likeCreated", like);
    return Promise.all([snapshot.ref.parent.parent.get().then((eventSnapshot) => {
            let count = eventSnapshot.get("likeCount");
            console.log("likeCount", count);
            count++;
            return snapshot.ref.parent.parent.set({
                likeCount: count
            }, { merge: true });
        }), updateStoryLike(like.userRef, snapshot.ref.parent.parent.id, true, Date.now())]);
});
exports.likeRemoved = functions.firestore.document("city/{cityId}/event/{eventId}/like/{likeId}").onDelete((snapshot, context) => {
    const like = snapshot.data();
    console.log("likeRemoved", like);
    const eventRef = snapshot.ref.parent.parent;
    return Promise.all([eventRef.get().then((eventSnapshot) => {
            let count = eventSnapshot.get("likeCount");
            console.log("likeCount", count);
            count--;
            return eventRef.update({
                likeCount: count
            }, { merge: true });
        }), updateStoryLike(like.userRef, snapshot.ref.parent.parent.id, false, Date.now())]);
});
function updateStoryLike(userRef, eventRef, isLiked, timeStamp) {
    const storyRef = firebaseAdmin.firestore().collection("user/" + userRef + "/story").doc(eventRef);
    return storyRef.set({
        eventRef: eventRef,
        liked: isLiked,
        timestamp: timeStamp
    }, { merge: true });
}
//# sourceMappingURL=index.js.map