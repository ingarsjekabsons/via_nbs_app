package com.example.nbs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.stepstone.stepper.StepperLayout;

public class StepperActivity extends AppCompatActivity {

    private StepperLayout mStepperLayout;
    private String TAG = StepperActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Into stepperActivity.");
        setContentView(R.layout.activity_stepper);
        mStepperLayout = findViewById(R.id.stepper);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));
    }
}