// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
// export const helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
'use-strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.likeAdded = functions.firestore.document("city/{cityId}/event/{eventId}/like/{likeId}").onCreate((snapshot, context) => {
    const like = snapshot.data();
    console.log("likeCreated", like);
    return snapshot.ref.parent.parent.set({
        likeCount: 88
    }, {merge: true})
});

// exports.likeTrigger = functions.firestore.document("city/{cityId}/event/{eventId}/like/{likeId}").onWrite((change, context) =>{
//  console.log("write",change.after.data())
// });
//
// exports.testUpdate = functions.firestore.document("city/{cityId}/event/{eventId}").onUpdate((snapshot, context) => {
//  console.log("update",snapshot.data())
// });
