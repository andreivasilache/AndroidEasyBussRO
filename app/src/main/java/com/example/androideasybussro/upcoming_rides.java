package com.example.androideasybussro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.androideasybussro.constants.MessageCodes;
import com.example.androideasybussro.constants.RawTimeFilterKeys;
import com.example.androideasybussro.models.OnGetDataListener;
import com.example.androideasybussro.services.BoughtTripsService;
import com.example.androideasybussro.state.Globe;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link upcoming_rides#newInstance} factory method to
 * create an instance of this fragment.
 */
public class upcoming_rides extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Globe store = Globe.getGlobe();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public upcoming_rides() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment upcoming_rides.
     */
    // TODO: Rename and change types and number of parameters
    public static upcoming_rides newInstance(String param1, String param2) {
        upcoming_rides fragment = new upcoming_rides();
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


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ListView listElements = getView().findViewById(R.id.listElements);
        System.out.println("onViewCreated");
        BoughtTripsService boughtTripsService = new BoughtTripsService();
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.route_view, R.id.textRouteView);
        listElements.setAdapter(adapter);



        boughtTripsService.getAllBoughtItemsLabels(RawTimeFilterKeys.FUTURE, new OnGetDataListener<List<String>, MessageCodes>() {
            @Override
            public void onStart() { }

            @Override
            public void onSuccess(List<String> data) {
                if(data.size() > 0){
                    adapter.addAll(data);
                }else{
                    adapter.add("Nu au fost gasite curse");
                }
            }

            @Override
            public void onFailed(MessageCodes error) { }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming_rides, container, false);
    }
}