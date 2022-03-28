package com.example.androideasybussro.services;



import com.example.androideasybussro.constants.RawTimeFilterKeys;
import com.example.androideasybussro.models.BoughtTrip;
import com.google.android.gms.tasks.Tasks;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.concurrent.ExecutionException;

public class BoughtTripsService {
//    Globe store = Globe.getGlobe();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String boughtItemsKey = "bought_trips";


    public void addBoughtRoute(BoughtTrip route) throws ExecutionException, InterruptedException {
        String id = db.collection(boughtItemsKey).document().getId();
        Void event = Tasks.await(db.collection(boughtItemsKey).document(id).set(route));
    }

    private QuerySnapshot getFirebaseFilterByQuery(RawTimeFilterKeys filter) throws ExecutionException, InterruptedException {
        long currentTimestamp = System.currentTimeMillis();

        if(filter == RawTimeFilterKeys.FUTURE){
            return Tasks.await(db.collection(boughtItemsKey).whereGreaterThan("dateTimestamp", currentTimestamp).get()) ;
        }else if(filter == RawTimeFilterKeys.PAST){
            return Tasks.await(db.collection(boughtItemsKey).whereLessThan("dateTimestamp", currentTimestamp).get());
        }else{
            return Tasks.await(db.collection(boughtItemsKey).get());
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public List<String> getAllBoughtItemsLabels(RawTimeFilterKeys filter) throws ExecutionException, InterruptedException {
//        String currentUserID = (String) store.getContext(ContextEnum.AUTHENTICATED_USER).getState("USERNAME");
//        List<String> toBeReturned = new ArrayList<>();
//
//        List<DocumentSnapshot> documents = getFirebaseFilterByQuery(filter).getDocuments();
//
//        for (DocumentSnapshot document : documents) {
//
//            String userID =  (String) document.getData().get("userID");
//            if(userID.equals(currentUserID)){
//                long serverDate = (long) document.getData().get("dateTimestamp");
//
//                String currentLabel = (String) document.getData().get("label");
//
//                String parsedLocalDate = Instant.ofEpochMilli(serverDate).atZone(ZoneId.systemDefault()).toLocalDate().toString();
//                toBeReturned.add("["+parsedLocalDate+ "]"+currentLabel);
//            }
//
//
//        }
//
//        return toBeReturned;
//    }
}
