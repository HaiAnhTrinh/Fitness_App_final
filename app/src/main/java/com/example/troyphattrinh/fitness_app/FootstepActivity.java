package com.example.troyphattrinh.fitness_app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FootstepActivity extends Fragment implements StepSensorBase.StepCallBack {
    private TextView mStepText;
    private StepSensorBase mStepSensor;
    private Button clearBtn;


    @Override
    public void Step(int stepNum) {
        mStepText.setText("Steps:" + stepNum);
        int criterion = 10000;
        if(stepNum == criterion){
            Noti.showNotification(this);
            criterion = 0;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_footstep, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clearBtn = view.findViewById(R.id.clearBtn);

        mStepText = view.findViewById(R.id.step_text1);
        mStepSensor = new StepSensorPedometer(getContext(), this);
        if (!mStepSensor.registerStep()) {
            Toast.makeText(getContext(), "Unavailable", Toast.LENGTH_SHORT).show();
        }

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepSensorPedometer.setIsRun(true);
                mStepText.setText("Steps: 0");
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mStepSensor.unregisterStep();
    }

}
