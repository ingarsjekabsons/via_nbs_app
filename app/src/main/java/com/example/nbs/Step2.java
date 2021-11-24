package com.example.nbs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class Step2 extends Fragment implements Step {
    private final String TAG = Step2.class.getName();
    @Override
    public VerificationError verifyStep() {
        Log.i(TAG, "verifyStep");
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
        return inflater.inflate(R.layout.step2, container, false);
    }
}