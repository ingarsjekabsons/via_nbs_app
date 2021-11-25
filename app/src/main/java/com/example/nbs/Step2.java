package com.example.nbs;

import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.auth0.android.jwt.JWT;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import net.openid.appauth.AuthState;

import org.json.JSONException;
import org.json.JSONObject;

public class Step2 extends Fragment implements Step {
    private final String TAG = Step2.class.getName();

    private SharedPreferences sp;

    private EditText step2_proff, step2_proft, step2_zemf,
            step2_zemt, step2_rezf, step2_rezt, step2_aizsf,
            step2_aizst, step2_jaunf, step2_jaunt, step2_dienf,
            step2_dient, step2_last, step2_dienestapakape, step2_foreignNBS;

    private CheckBox cb;

    private void checkYears(String from, String to, String hint) throws Exception {
        int t, f;

        // not filled - OK
        if (from.isEmpty() && to.isEmpty()) {
            return;
        }

        try {
            f = Integer.parseInt(from);
            t = Integer.parseInt(to);
        } catch (Exception e) {
            throw new Exception("Nepareizi gada skaitļi");
        }

        if (f > t) {
            throw new Exception("Gads no nedrīkts būt lielāks par gads līdz. (" + hint + ")");
        }

        if (f < 1930 || f > 2021) {
            throw new Exception("Pārāk liels vai pārāk mazs gads no. (" + hint + ")");
        }

        if (t < 1980 || t > 2021) {
            throw new Exception("Pārāk liels vai pārāk mazs gads līdz. (" + hint + ")");
        }
    }

    @Override
    public VerificationError verifyStep() {
        Log.i(TAG, "verifyStep");
        JSONObject json = new JSONObject();

        try {
            checkYears(step2_proff.getText().toString(), step2_proft.getText().toString(),
                    "profesionālais dienests");
            checkYears(step2_zemf.getText().toString(), step2_zemt.getText().toString(),
                    "dienests zemessardzē");
            checkYears(step2_rezf.getText().toString(), step2_rezt.getText().toString(),
                    "rezervista apmācības");
            checkYears(step2_aizsf.getText().toString(), step2_aizst.getText().toString(),
                    "valsts aizsardzības mācība");
            checkYears(step2_jaunf.getText().toString(), step2_jaunt.getText().toString(),
                    "Jaunsardzes apmācība");
            checkYears(step2_dienf.getText().toString(), step2_dient.getText().toString(),
                    "obbligātais dienests");
        } catch (Exception e) {
            return new VerificationError(e.getMessage());
        }

        try {
            json.put("profDiensests", step2_proff.getText().toString() + "-" + step2_proft.getText().toString())
                    .put("dienZS", step2_zemf.getText().toString() + "-" + step2_zemt.getText().toString())
                    .put("rezervistaApm", step2_rezf.getText().toString() + "-" + step2_rezt.getText().toString())
                    .put("aizsardzApm", step2_aizsf.getText().toString() + "-" + step2_aizst.getText().toString())
                    .put("jaunsardze", step2_jaunf.getText().toString() + "-" + step2_jaunt.getText().toString())
                    .put("milDiensts", step2_dienf.getText().toString() + "-" + step2_dient.getText().toString())
                    .put("lastDienests", step2_last.getText().toString())
                    .put("pakape", step2_dienestapakape.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (cb.isChecked()) {
            try {
                json.put("foreignNBS", step2_foreignNBS.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, json.toString());
        sp.edit().putString("step2_json", json.toString()).apply();
        return null;
    }

    @Override
    public void onSelected() {
        Log.i(TAG, "onSelected");
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(getActivity().getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step2, container, false);
        step2_proff = v.findViewById(R.id.step2_proff);
        step2_proft = v.findViewById(R.id.step2_proft);
        step2_zemf = v.findViewById(R.id.step2_zemf);
        step2_zemt = v.findViewById(R.id.step2_zemt);
        step2_rezf = v.findViewById(R.id.step2_rezf);
        step2_rezt = v.findViewById(R.id.step2_rezt);
        step2_aizsf = v.findViewById(R.id.step2_aizsf);
        step2_aizst = v.findViewById(R.id.step2_aizt);
        step2_jaunf = v.findViewById(R.id.step2_jaunf);
        step2_jaunt = v.findViewById(R.id.step2_jaunt);
        step2_dienf = v.findViewById(R.id.step2_dienf);
        step2_dient = v.findViewById(R.id.step2_dient);
        step2_last = v.findViewById(R.id.step2_last);
        step2_dienestapakape = v.findViewById(R.id.step2_diensestapakape);
        step2_foreignNBS = v.findViewById(R.id.step2_foreignNBS);
        step2_foreignNBS.setEnabled(false);

        cb = v.findViewById(R.id.step2_foreign);
        cb.setOnClickListener(vv -> {
            step2_foreignNBS.setEnabled(cb.isChecked());
        });
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        sp = this.getActivity().getSharedPreferences("com.example.nbs", Context.MODE_PRIVATE);
    }
}