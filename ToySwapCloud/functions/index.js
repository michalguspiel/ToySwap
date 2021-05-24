// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access Firestore.
const admin = require('firebase-admin');
admin.initializeApp();
// Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
 exports.helloWorld = functions.https.onRequest((request, response) => {
   functions.logger.info("Hello logs!", {structuredData: true});
      response.send("Hello from Firebase!");
 });

 exports.addUserToFireStore = functions.auth.user().onCreate((user) => {
    functions.logger.info("New user created!");
    var usersRef = admin.firestore().collection("users");
    var userName = user.displayName
    var userAvatar = user.photoURL
    if(userAvatar == null)userAvatar = ""
    if(userName == null) userName = ""

        return usersRef.doc(user.uid).set({
      name: userName,
      emailAddress : user.email,
      avatar: user.photoURL,
      addressCity: "",
      addressPostCode: "",
      addressStreet: "",
      points: 0, // 0 points IDK for what Ill use them yet. 
    });
  });
