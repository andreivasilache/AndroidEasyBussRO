package com.example.androideasybussro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.androideasybussro.constants.MessageCodes;
import com.example.androideasybussro.constants.RawTimeFilterKeys;
import com.example.androideasybussro.models.OnGetDataListener;
import com.example.androideasybussro.models.Route;
import com.example.androideasybussro.models.Station;
import com.example.androideasybussro.services.BoughtTripsService;
import com.example.androideasybussro.services.RoutesService;
import com.example.androideasybussro.state.ContextEnum;
import com.example.androideasybussro.state.Globe;

import java.time.LocalDate;
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
     * @param param2 Parameter 2.
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
    Spinner fromDropdown = getView().findViewById(R.id.from);
    Spinner toDropdown = getView().findViewById(R.id.to);

    Button search = getView().findViewById(R.id.searchBtn);

    void prepopulateInputs(ArrayAdapter fromDropdownList, ArrayAdapter toDropdownList) {
        fromDropdownList.addAll(allStations.keySet());
        toDropdownList.addAll(allStations.keySet());


        if (!fromDropdownList.isEmpty()) {
            fromDropdown.setSelection(0, true);
        }
        if (fromDropdownList.size() >= 1) {
            toDropdown.getSelectionModel().select(1);
        }
        datePicker.setValue(LocalDate.now());
    }


    public void initialize() {
//        prepopulateInputs();
        System.out.println(currentUserID);

        fromDropdown.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                toDropdown.getItems().setAll(toDropdownList);
                String selectedValue = (String) fromDropdown.getItems().get((Integer) number2);
                toDropdown.getItems().remove(selectedValue);
                if (!toDropdown.getItems().isEmpty()) {
                    toDropdown.getSelectionModel().select(0);
                }
            }
        });
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {



//        ListView listElements = getView().findViewById(R.id.listPastElements);
//        System.out.println("onViewCreated");
//        BoughtTripsService boughtTripsService = new BoughtTripsService();
//        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.route_view, R.id.textRouteView);
//        listElements.setAdapter(adapter);


//        boughtTripsService.getAllBoughtItemsLabels(RawTimeFilterKeys.PAST, new OnGetDataListener<List<String>, MessageCodes>() {
//            @Override
//            public void onStart() { }
//
//            @Override
//            public void onSuccess(List<String> data) {
//                adapter.addAll(data);
//            }
//
//            @Override
//            public void onFailed(MessageCodes error) { }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_plan, container, false);
    }
}