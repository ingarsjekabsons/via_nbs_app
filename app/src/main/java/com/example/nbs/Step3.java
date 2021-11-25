package com.example.nbs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

public class Step3 extends Fragment implements Step {
    private final String TAG = Step3.class.getName();

    private EditText izgl, spec, darba_pieredze, iekslietas;
    private RadioGroup izglLimenis;
    private CheckBox cb;

    private SharedPreferences sp;

    private String decodeLimenis(int i) {
        RadioButton b = getView().findViewById(i);
        return (String) b.getText();
    }

    @Override
    public VerificationError verifyStep() {
        Log.i(TAG, "verifyStep");

        JSONObject json = new JSONObject();
        try {
            json.put("izglitibasIestade", izgl.getText().toString())
                    .put("izglitibaseLimenis", decodeLimenis(izglLimenis.getCheckedRadioButtonId()))
                    .put("specialitate", spec.getText().toString())
                    .put("darbaPieredz", darba_pieredze.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (cb.isChecked()) {
            try {
                json.put("dienestsIekslietas", iekslietas.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, json.toString());
        sp.edit().putString("step3_json", json.toString()).apply();

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
        View v =  inflater.inflate(R.layout.step3, container, false);

        izgl = v.findViewById(R.id.step4_studprog);
        spec = v.findViewById(R.id.step3_spec);
        darba_pieredze = v.findViewById(R.id.step3_darba_pieredze);
        iekslietas = v.findViewById(R.id.step3_iekslietas);
        iekslietas.setEnabled(false);

        cb = v.findViewById(R.id.step3_checkbox);
        cb.setOnClickListener(vv -> {
            iekslietas.setEnabled(cb.isChecked());
        });

        izglLimenis = v.findViewById(R.id.step3_izgl);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        sp = this.getActivity().getSharedPreferences("com.example.nbs", Context.MODE_PRIVATE);
    }
}