package com.example.androideasybussro.services;



import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.androideasybussro.constants.MessageCodes;
import com.example.androideasybussro.constants.RawTimeFilterKeys;
import com.example.androideasybussro.models.BoughtTrip;
import com.example.androideasybussro.models.OnGetDataListener;
import com.example.androideasybussro.models.Route;
import com.example.androideasybussro.state.ContextEnum;
import com.example.androideasybussro.state.Globe;
import com.google.android.gms.tasks.Tasks;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BoughtTripsService {
    Globe store = Globe.getGlobe();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String boughtItemsKey = "bought_trips";


    public void addBoughtRoute(BoughtTrip route, final OnGetDataListener<MessageCodes, MessageCodes> onData) {
        String id = db.collection(boughtItemsKey).document().getId();


        db.collection(boughtItemsKey).document(id).set(route).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                onData.onSuccess(MessageCodes.SUCCESS);
            }
        });
    }

    private void getFirebaseFilterByQuery(RawTimeFilterKeys filter, final OnGetDataListener<QuerySnapshot, MessageCodes> onData) {
        long currentTimestamp = System.currentTimeMillis();

        if(filter == RawTimeFilterKeys.FUTURE){
            db.collection(boughtItemsKey).whereGreaterThan("dateTimestamp", currentTimestamp).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    onData.onSuccess(task.getResult());
                }
            });
        }else if(filter == RawTimeFilterKeys.PAST){
            db.collection(boughtItemsKey).whereLessThan("dateTimestamp", currentTimestamp).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    onData.onSuccess(task.getResult());
                }
            });
        }else{
            db.collection(boughtItemsKey).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    onData.onSuccess(task.getResult());
                }
            });
        }
    };

    public void getAllBoughtItemsLabels(RawTimeFilterKeys filter, final OnGetDataListener<List<String>, MessageCodes> onResult) {
        String currentUserID = (String) store.getContext(ContextEnum.AUTHENTICATED_USER).getState("USERNAME");
        List<String> toBeReturned = new ArrayList<>();


        getFirebaseFilterByQuery(filter, new OnGetDataListener<QuerySnapshot, MessageCodes>() {
            @Override
            public void onStart() {}

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(QuerySnapshot data) {
                List<DocumentSnapshot> documents = data.getDocuments();
                for (DocumentSnapshot document : documents) {

                    String userID =  (String) document.getData().get("userID");
                    if(userID.equals(currentUserID)){
                        long serverDate = (long) document.getData().get("dateTimestamp");

                        String currentLabel = (String) document.getData().get("label");

                        String parsedLocalDate = Instant.ofEpochMilli(serverDate).atZone(ZoneId.systemDefault()).toLocalDate().toString();
                        toBeReturned.add("["+parsedLocalDate+ "]"+currentLabel);
                    }
                }
                onResult.onSuccess(toBeReturned);
            }

            @Override
            public void onFailed(MessageCodes error) {}
        });
    };
}
