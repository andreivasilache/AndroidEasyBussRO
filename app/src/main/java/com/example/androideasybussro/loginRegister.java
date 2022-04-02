package com.example.androideasybussro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.androideasybussro.constants.MessageCodes;
import com.example.androideasybussro.models.OnGetDataListener;
import com.example.androideasybussro.models.User;
import com.example.androideasybussro.services.AuthService;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link loginRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loginRegister extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public loginRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment loginRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static loginRegister newInstance(String param1, String param2) {
        loginRegister fragment = new loginRegister();
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
        Button loginBtn = getView().findViewById(R.id.loginBtn);
        Button registerBtn = getView().findViewById(R.id.registerBtn);

        loginBtn.setOnClickListener(view1 -> {
            AuthService authService = new AuthService();
            EditText usernameField = getView().findViewById(R.id.email);
            EditText passwordField = getView().findViewById(R.id.password);

            User loggedUser = new User(usernameField.getText().toString(), passwordField.getText().toString());

            try {
                authService.loginUser(loggedUser, new OnGetDataListener<MessageCodes, MessageCodes>() {
                    @Override
                    public void onStart() { }

                    @Override
                    public void onSuccess(MessageCodes data) {
                        System.out.println(data);
                    }

                    @Override
                    public void onFailed(MessageCodes error) {
                        System.out.println(error);
                    }
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        registerBtn.setOnClickListener(view1 -> {
            AuthService authService = new AuthService();

            EditText usernameField = getView().findViewById(R.id.email);
            EditText passwordField = getView().findViewById(R.id.password);

            User loggedUser = new User(usernameField.getText().toString(), passwordField.getText().toString());

            try {
                authService.registerUser(loggedUser, new OnGetDataListener<MessageCodes, MessageCodes>() {
                    @Override
                    public void onStart() { }

                    @Override
                    public void onSuccess(MessageCodes data) {
                        System.out.println(data);
                    }

                    @Override
                    public void onFailed(MessageCodes error) {
                        System.out.println(error);
                    }
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_register, container, false);
    }
}