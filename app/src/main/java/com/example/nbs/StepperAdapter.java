package com.example.nbs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class StepperAdapter extends AbstractFragmentStepAdapter {

    private static final String CURRENT_STEP_POSITION_KEY = "STEP_POS_KEY";
    private final String TAG = StepperAdapter.class.getName();

    public StepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        Log.i(TAG, "createStep("+position+")");

        Bundle b = new Bundle();
        b.putInt(CURRENT_STEP_POSITION_KEY, position);

        if (position == 0) {
            final Step1 step = new Step1();
            step.setArguments(b);
            return step;
        } else if (position == 1) {
            final Step2 step = new Step2();
            step.setArguments(b);
            return step;
        } else if (position == 2) {
            final Step3 step = new Step3();
            step.setArguments(b);
            return step;
        } else {
            final Step4 step = new Step4();
            step.setArguments(b);
            return step;
        }
    }

    @Override
    public int getCount() {
        Log.i(TAG, "getCount()");
        return 4;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        Log.i(TAG, "getViewModel()");
        return new StepViewModel.Builder(context)
                .setTitle("THE TITLE")
                .create();
    }
}
