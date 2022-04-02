// https://cloud.google.com/firestore/docs/query-data/get-data#java

package com.example.androideasybussro.services;

import com.example.androideasybussro.constants.MessageCodes;
import com.example.androideasybussro.models.OnGetDataListener;
import com.example.androideasybussro.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutionException;
/*
*
*   This is a simple college project, I'am just keeping the username and password as firebase fields.
*
* */

public class AuthService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String usersCollection = "users";

    public void registerUser(User userAuthData, final OnGetDataListener<MessageCodes, MessageCodes> registerListener ) throws ExecutionException, InterruptedException {
        try{
            this.getUser(userAuthData.username,  new OnGetDataListener<User, MessageCodes>() {
                @Override
                public void onStart() { registerListener.onStart(); }
                @Override
                public void onSuccess(User data) {
                    registerListener.onFailed(MessageCodes.ALREADY_EXISTS);
                }

                @Override
                public void onFailed(MessageCodes error) {
                    if(error == MessageCodes.NOT_FOUND){
                        db.collection(usersCollection).document(userAuthData.username).set(userAuthData);
                        registerListener.onSuccess(MessageCodes.SUCCESS);
                    }
                    registerListener.onFailed(MessageCodes.SOMETHING_IS_WRONG);
                }
            });
        }catch(RuntimeException e){
            System.out.println("error" + e.getMessage());
            registerListener.onFailed(MessageCodes.SOMETHING_IS_WRONG);
        }
    }

    public void loginUser(User userAuthData, final OnGetDataListener<MessageCodes, MessageCodes> loginListener) throws ExecutionException, InterruptedException {
        try{
            this.getUser(userAuthData.username, new OnGetDataListener<User, MessageCodes>() {

                @Override
                public void onStart() {
                    loginListener.onStart();
                }

                @Override
                public void onSuccess(User data) {
                    if(userAuthData.password.equals(((User) data).password)){
                        loginListener.onSuccess(MessageCodes.SUCCESS);
                    }else{
                        loginListener.onFailed(MessageCodes.INVALID_PASSWORD);
                    }
                }

                @Override
                public void onFailed(MessageCodes error) {
                    if(error == MessageCodes.NOT_FOUND){
                        loginListener.onFailed(MessageCodes.NOT_FOUND);
                    }
                    loginListener.onFailed(MessageCodes.SOMETHING_IS_WRONG);
                }
            });

        }catch(RuntimeException e){
            System.out.println("error" + e.getMessage());
            loginListener.onFailed(MessageCodes.SOMETHING_IS_WRONG);
        }
    }

    public void getUser(String username,final OnGetDataListener<User,MessageCodes > listener ) throws RuntimeException, ExecutionException, InterruptedException {
        listener.onStart();

        DocumentReference docRef = db.collection(usersCollection).document(username);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    User foundUser = new User((String) document.getData().get("username"), (String) document.getData().get("password"));
                    listener.onSuccess(foundUser);
                } else {
                    listener.onFailed(MessageCodes.NOT_FOUND);
                }
            } else {
                listener.onFailed(MessageCodes.SOMETHING_IS_WRONG);
            }
        });
    }

}
