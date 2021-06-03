// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access Firestore.
const admin = require('firebase-admin');

const timestamp = admin.firestore.FieldValue.serverTimestamp();

admin.initializeApp();
// Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
 exports.helloWorld = functions.https.onRequest((request, response) => {
   functions.logger.info("Hello logs!", {structuredData: true});
      response.send("Hello from Firebase!");
 });

 function sliceDisplayName(fullName)  {
  return fullName.split(" ")
 }

 exports.addUserToFireStore = functions.auth.user().onCreate((user) => {
    functions.logger.info("New user created!");
    var usersRef = admin.firestore().collection("users");
    var userName = user.displayName
    var firstName = ""
    var lastName = ""
    if(userName != null){
     var names = sliceDisplayName(userName)
     if(names[0] != null)firstName = names[0]
     if(names[1] != null)lastName = names[1]
    }
    var userAvatar = user.photoURL
    if(userAvatar == null)userAvatar = ""

        return usersRef.doc(user.uid).set({
      firstName : firstName,
      lastName: lastName,
      emailAddress : user.email,
      avatar: user.photoURL,
      addressCity: "",
      addressPostCode: "",
      addressStreet: "",
      points: 0,  // 0 points IDK for what Ill use them yet. 
      reputation : 1000 ,
      accCreationTimestamp : timestamp
    });
  });
