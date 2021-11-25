package com.example.nbs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.auth0.android.jwt.JWT;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import net.openid.appauth.AuthState;

import org.json.JSONException;
import org.json.JSONObject;

public class Step1 extends Fragment implements Step {
    private final String TAG = Step1.class.getName();
    private SharedPreferences sp;

    private JSONObject json;

    private EditText name, surname, pk, dzd, realAddr, legalAddr, phone;
    private JWT idToken;

    @Override
    public VerificationError verifyStep() {
        Log.i(TAG, "verifyStep");

        json = new JSONObject();
        try {
            json.put("name", name.getText().toString())
                    .put("surname", surname.getText().toString())
                    .put("pk", pk.getText().toString())
                    .put("born", dzd.getText().toString())
                    .put("realAddress", realAddr.getText().toString())
                    .put("legalAddress", legalAddr.getText().toString())
                    .put("phone", phone.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, json.toString());
        sp.edit().putString("step1_json",  json.toString()).apply();

        return null;
    }

    @Override
    public void onSelected() {
        Log.i(TAG, "onSelected");
    }

    @Override
    public void onError(@NonNull VerificationError error) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step1, container, false);
        name = v.findViewById(R.id.step1_name);
        surname = v.findViewById(R.id.step1_surname);
        pk = v.findViewById(R.id.step1_nationalId);
        dzd = v.findViewById(R.id.step1_born);
        realAddr = v.findViewById(R.id.step1_real_address);
        legalAddr = v.findViewById(R.id.step1_legal_address);
        phone = v.findViewById(R.id.editTextPhone);

        Log.i(TAG, "onCreateView() ....");

        if (idToken != null) {
            name.setText(idToken.getClaim("given_name").asString());
            name.setEnabled(false);
            surname.setText(idToken.getClaim("family_name").asString());
            surname.setEnabled(false);
        }
        return  v;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        sp = this.getActivity().getSharedPreferences("com.example.nbs", Context.MODE_PRIVATE);

        String stateJson = sp.getString("authState", null);

        if (stateJson != null) {
            Log.i(TAG, "We got auth state from shared prefs.");
            try {
                AuthState s = AuthState.jsonDeserialize(stateJson);

                if (s.isAuthorized()) {
                    String tkn = s.getIdToken();
                    assert tkn != null;

                    idToken = new JWT(tkn);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Failed to parse AuthState from JSON");
            }
        }
    }

}