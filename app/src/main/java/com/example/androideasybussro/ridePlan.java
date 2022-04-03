package com.example.androideasybussro;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.androideasybussro.constants.MessageCodes;
import com.example.androideasybussro.constants.RawTimeFilterKeys;
import com.example.androideasybussro.models.BoughtTrip;
import com.example.androideasybussro.models.OnGetDataListener;
import com.example.androideasybussro.models.Route;
import com.example.androideasybussro.models.RouteDistance;
import com.example.androideasybussro.models.Station;
import com.example.androideasybussro.services.BoughtTripsService;
import com.example.androideasybussro.services.RouteDistancesService;
import com.example.androideasybussro.services.RoutesService;
import com.example.androideasybussro.state.ContextEnum;
import com.example.androideasybussro.state.Globe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ridePlan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ridePlan extends Fragment {

    Globe store = Globe.getGlobe();
    Vector<Route> storeRoutes = (Vector<Route>) store.getContext(ContextEnum.AVAIlABLE_ROUTES).getState("BUSS-ROUTES");
    Map<String, Station> allStations = RoutesService.getAllStations(storeRoutes);

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ridePlan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
admn     * @param param2 Parameter 2.
     * @return A new instance of fragment ridePlan.
     */
    // TODO: Rename and change types and number of parameters
    public static ridePlan newInstance(String param1, String param2) {
        ridePlan fragment = new ridePlan();
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



    void prepopulateInputs(Spinner fromDropdown,ArrayAdapter fromDropdownList,  Spinner toDropdown,ArrayAdapter toDropdownList, DatePicker datePicker) {
        fromDropdownList.addAll(allStations.keySet());
        toDropdownList.addAll(allStations.keySet());


        if (!fromDropdownList.isEmpty()) {
            fromDropdown.setSelection(0, true);
        }
        if (!toDropdownList.isEmpty()) {
            toDropdown.setSelection(1, true);
        }

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        datePicker.updateDate(year, month, day);
    }


    public void initialize(Spinner fromDropdown,ArrayAdapter fromDropdownList,  Spinner toDropdown,ArrayAdapter toDropdownList, DatePicker datePicker) {
        prepopulateInputs( fromDropdown, fromDropdownList, toDropdown, toDropdownList, datePicker);

        fromDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toDropdownList.clear();
                toDropdownList.addAll(allStations.keySet());

                String selectedValue = (String) fromDropdownList.getItem(i);

                toDropdownList.remove(selectedValue);

                if (!toDropdownList.isEmpty()) {
                    toDropdown.setSelection(0, true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


    }
    HashMap<String,Route> lastFoundRoutesAfterSearch = null;
    RouteDistancesService routeDistancesService = new RouteDistancesService();
    HashMap<String, RouteDistance> storeRouteDistances = (HashMap<String, RouteDistance>) store.getContext(ContextEnum.ROUTE_DISTANCES).getState("DISTANCES");
    String currentUserID = (String) store.getContext(ContextEnum.AUTHENTICATED_USER).getState("USERNAME");


    public void onViewCreated(View view, Bundle savedInstanceState) {
        Button search = getView().findViewById(R.id.searchBtn);
        Button buy = getView().findViewById(R.id.buy);

        Spinner fromDropdown = getView().findViewById(R.id.from);
        Spinner toDropdown = getView().findViewById(R.id.to);
        Spinner searchResults = getView().findViewById(R.id.results);
        DatePicker datePicker = getView().findViewById(R.id.datePicker);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        final ArrayAdapter fromDropdownList = new ArrayAdapter(getActivity(), R.layout.route_view, R.id.textRouteView);
        fromDropdown.setAdapter(fromDropdownList);

        final ArrayAdapter toDropdownList = new ArrayAdapter(getActivity(), R.layout.route_view, R.id.textRouteView);
        toDropdown.setAdapter(toDropdownList);

        final ArrayAdapter searchResultsList = new ArrayAdapter(getActivity(), R.layout.route_view, R.id.textRouteView);
        searchResults.setAdapter(searchResultsList);


        initialize(fromDropdown, fromDropdownList, toDropdown, toDropdownList, datePicker);

        search.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                searchResultsList.clear();

                String fromSelectedStation = (String) fromDropdown.getSelectedItem().toString();
                String toSelectedStation = (String) toDropdown.getSelectedItem().toString();

                HashMap<String,Route> foundRoutesRaw = RoutesService.searchForRoute(storeRoutes, fromSelectedStation, toSelectedStation);
                Collection<Route> foundRoutes = foundRoutesRaw.values();
                List<String> toBeDisplayed = new ArrayList<>();

                if(foundRoutes.size() > 0){
                    lastFoundRoutesAfterSearch = foundRoutesRaw;
                    for(Route currentRoute: foundRoutes){
                        String concatonatedStationNames = "";
                        int currentRouteDistance = routeDistancesService.getDistanceOfRoutes(currentRoute.stations, storeRouteDistances);
                        for(Station currentStation: currentRoute.stations){
                            concatonatedStationNames = concatonatedStationNames + ((concatonatedStationNames == "" ? "["+currentRouteDistance+"km - "+currentRouteDistance*0.75+"RON] " : " - ") +currentStation.stationName + "("+ currentStation.arrivalTime+")");
                        }
                        toBeDisplayed.add(concatonatedStationNames);
                    }
                }else{
                    toBeDisplayed.add("Nu au fost gasite rezultate");
                    lastFoundRoutesAfterSearch = null;
                }

                searchResultsList.addAll(toBeDisplayed);
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedItemIndex = searchResults.getSelectedItemPosition();
                String selectedValue = searchResults.getSelectedItem().toString();

                Object selectedItemHashKey = lastFoundRoutesAfterSearch.keySet().toArray()[selectedItemIndex];
                Route selectedRoute = lastFoundRoutesAfterSearch.get(selectedItemHashKey);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());

                BoughtTrip boughtTrip = new BoughtTrip(selectedValue,calendar.getTimeInMillis(), selectedRoute.stations, currentUserID);

                BoughtTripsService boughtTripsService = new BoughtTripsService();
                boughtTripsService.addBoughtRoute(boughtTrip, new OnGetDataListener<MessageCodes, MessageCodes>() {
                    @Override
                    public void onStart() { }

                    @Override
                    public void onSuccess(MessageCodes data) {
                        Navigation.findNavController(view).navigate(R.id.upcoming_rides);
                    }

                    @Override
                    public void onFailed(MessageCodes error) {}
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_plan, container, false);
    }
}