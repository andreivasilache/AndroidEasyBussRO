package com.example.androideasybussro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.androideasybussro.constants.MessageCodes;
import com.example.androideasybussro.constants.RawTimeFilterKeys;
import com.example.androideasybussro.models.OnGetDataListener;
import com.example.androideasybussro.models.Route;
import com.example.androideasybussro.models.Station;
import com.example.androideasybussro.services.BoughtTripsService;
import com.example.androideasybussro.services.RoutesService;
import com.example.androideasybussro.state.ContextEnum;
import com.example.androideasybussro.state.Globe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ridesSchedule#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ridesSchedule extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ridesSchedule() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ridesSchedule.
     */
    // TODO: Rename and change types and number of parameters
    public static ridesSchedule newInstance(String param1, String param2) {
        ridesSchedule fragment = new ridesSchedule();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        Globe store = Globe.getGlobe();
        Vector<Route> storeRoutes = (Vector<Route>) store.getContext(ContextEnum.AVAIlABLE_ROUTES).getState("BUSS-ROUTES");
        List<String> toBeDisplayed = new ArrayList<>();

        ListView listElements = getView().findViewById(R.id.scheduleView);
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.route_view, R.id.textRouteView);
        listElements.setAdapter(adapter);

        if(storeRoutes.size() > 0){
            for(Route currentRoute: storeRoutes){
                String concatonatedStationNames = "";
                for(Object currentStation: currentRoute.stations){
                    String stationName = "";
                    String arrivalTime = "";
                    if(currentStation instanceof HashMap<?,?>){
                        HashMap<String, Object> currentStationHash =(HashMap<String, Object>) currentStation;
                        Station parsedStation = RoutesService.getStationFromHashMap(currentStationHash);
                        arrivalTime = parsedStation.arrivalTime;
                        stationName = parsedStation.stationName;
                    }
                    if(currentStation instanceof Station){
                        arrivalTime = ((Station) currentStation).arrivalTime;
                        stationName = ((Station) currentStation).stationName;
                    }

                    concatonatedStationNames = concatonatedStationNames + (concatonatedStationNames == "" ? "" : " - ") + stationName + "("+ arrivalTime+")";
                }
                toBeDisplayed.add(concatonatedStationNames);
            }
        }else{
            toBeDisplayed.add("Nu au fost gasite rute");
        }
        adapter.addAll(toBeDisplayed);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rides_schedule, container, false);
    }
}