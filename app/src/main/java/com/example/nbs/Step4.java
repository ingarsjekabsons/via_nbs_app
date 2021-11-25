package com.example.nbs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

public class Step4 extends Fragment implements Step {
    private final String TAG = Step4.class.getName();

    private SharedPreferences sp;
    private EditText velamaStudijuProg;
    private RadioGroup infoFrom;
    private CheckBox visamPiekritu;

    @Override
    public VerificationError verifyStep() {
        JSONObject json = new JSONObject();

        if (!visamPiekritu.isChecked()) {
            return new VerificationError("Jums ir jāpiekrīt noteikumiem, lai pieteikumu pabeigtu!");
        }

        Button b = getView().findViewById(infoFrom.getCheckedRadioButtonId());

        try {
            json.put("velamaStudijuProgramma", velamaStudijuProg.getText().toString())
                    .put("iegutaInformacijaNo", (String) b.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        View v =  inflater.inflate(R.layout.step4, container, false);
        velamaStudijuProg = v.findViewById(R.id.step4_studprog);
        infoFrom = v.findViewById(R.id.step3_izgl);
        visamPiekritu = v.findViewById(R.id.step3_checkbox);

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        sp = this.getActivity().getSharedPreferences("com.example.nbs", Context.MODE_PRIVATE);
    }
}