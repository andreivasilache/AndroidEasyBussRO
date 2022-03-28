// https://cloud.google.com/firestore/docs/query-data/get-data#java

package com.example.androideasybussro.services;

import com.example.androideasybussro.constants.MessageCodes;
import com.example.androideasybussro.models.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutionException;

public class AuthService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String usersCollection = "users";

    public MessageCodes registerUser(User userAuthData) throws ExecutionException, InterruptedException {
        try{
            Object res = this.getUser(userAuthData.username);
            if(res == MessageCodes.NOT_FOUND){
                Tasks.await(db.collection(usersCollection).document(userAuthData.username).set(userAuthData)) ;
                return MessageCodes.SUCCESS;
            }else if(res instanceof User){
                return MessageCodes.ALREADY_EXISTS;
            }else{
                return MessageCodes.SOMETHING_IS_WRONG;
            }
        }catch(RuntimeException e){
            return MessageCodes.SOMETHING_IS_WRONG;
        }
    }

    public MessageCodes loginUser(User userAuthData) throws ExecutionException, InterruptedException {

        try{
            Object res = this.getUser(userAuthData.username);
            if(res == MessageCodes.NOT_FOUND){
                return MessageCodes.NOT_FOUND;
            }else if(res instanceof User){
                if(userAuthData.password.equals(((User) res).password)){
                    return MessageCodes.SUCCESS;
                }else{
                    return MessageCodes.INVALID_PASSWORD;
                }
            }else{
                return MessageCodes.SOMETHING_IS_WRONG;
            }
        }catch(RuntimeException e){
            return MessageCodes.SOMETHING_IS_WRONG;
        }
    }

    public Object getUser(String username) throws RuntimeException, ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(usersCollection).document(username);
        DocumentSnapshot future = Tasks.await(docRef.get());

        if (future.exists()) {
            return new User((String) future.getData().get("username"), (String) future.getData().get("password"));
        } else {
            return MessageCodes.NOT_FOUND;
        }
    }
}
