package com.example.androideasybussro.services;

import com.example.androideasybussro.models.RouteDistance;
import com.example.androideasybussro.models.Station;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RouteDistancesService {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String distanceService = "ROUTE_DISTANCES";


    public String getKeyNameFromDistance(RouteDistance routeDistance){
        return routeDistance.from+"-"+routeDistance.to;
    }

    public void addRouteDistance(RouteDistance routeDistance){
        String id = db.collection(distanceService).document().getId();
        db.collection(distanceService).document(getKeyNameFromDistance(routeDistance)).set(routeDistance);
    }

    public HashMap<String, RouteDistance> getAllDistances() throws ExecutionException, InterruptedException {
        HashMap<String, RouteDistance> toBeReturned = new HashMap<>();

        QuerySnapshot future= Tasks.await(db.collection(distanceService).get());
        List<DocumentSnapshot> documents = future.getDocuments();


        for (DocumentSnapshot document : documents) {
            String from = (String) document.getData().get("from");
            String to = (String) document.getData().get("to");
            int distance = ((Long) document.getData().get("distance")).intValue();

            toBeReturned.put(document.getId(), new RouteDistance(from, to, distance));
        }

        return toBeReturned;
    }

    public int getDistanceOfRoutes(List<Station> routes, HashMap<String, RouteDistance> distancesHash){
        int toBeReturned = 0;
        for(int i=0; i<routes.size(); i++){
            if(i-1 >= 0){
                String prevStationName = routes.get(i-1).stationName;
                String currentStationName = routes.get(i).stationName;
                toBeReturned += distancesHash.get(prevStationName + "-"+currentStationName).distance;
            }
        }
        return toBeReturned;
    }
}
