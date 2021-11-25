package com.example.nbs;

import static android.widget.Toast.LENGTH_LONG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

public class StepperActivity extends AppCompatActivity implements StepperLayout.StepperListener{

    private final String TAG = StepperActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Into stepperActivity.");
        setContentView(R.layout.activity_stepper);
        StepperLayout mStepperLayout = findViewById(R.id.stepper);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completed)  {
        SharedPreferences sp = getSharedPreferences("com.example.nbs", MODE_PRIVATE);
        JSONObject finalJson = new JSONObject();

        try {
            finalJson.put("step1", sp.getString("step1_json", ""))
                    .put("step2", sp.getString("step2_json", ""))
                    .put("step3", sp.getString("step3_json", ""))
                    .put("step4", sp.getString("step4_json",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, finalJson.toString());
        finish();
    }

    @Override
    public void onReturn() {
        finish();
    }

    @Override
    public void onStepSelected(int newStepPosition) {
    }

    @Override
    public void onError(VerificationError verificationError) {
        Toast.makeText(this, "Kļūda! -> " + verificationError.getErrorMessage(), LENGTH_LONG).show();
    }
}