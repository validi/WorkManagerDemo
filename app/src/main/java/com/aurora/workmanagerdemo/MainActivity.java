package com.aurora.workmanagerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.NetworkChannel;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
OneTimeWorkRequest oneTimeWorkRequest;
    PeriodicWorkRequest saveRequest;
TextView textView;
    public static final String KEY_COUNT_VALUE="key_count";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data data=new Data.Builder().putInt(KEY_COUNT_VALUE,1750).build();

        Constraints constraints=new Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build();

        oneTimeWorkRequest=new OneTimeWorkRequest.Builder(DemoWorker.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();


//         saveRequest =
//                new PeriodicWorkRequest.Builder
//                        (DemoWorker.class
//                                , 15, TimeUnit.SECONDS
//                                ,30,TimeUnit.SECONDS)
//                        // Constraints
//                        .setInputData(data)
//                        .setConstraints(constraints)
//                        .build();



        textView= (TextView) findViewById(R.id.textView);


        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo!=null) {
                            textView.setText(workInfo.getState().name());
                            if (workInfo.getState().isFinished()) {

                                Data data1 = workInfo.getOutputData();
                                String message = data1.getString(DemoWorker.KEY_WORKER);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    public void onClick(View view) {
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
        try {
           // WorkManager.getInstance(this).enqueue(saveRequest);

        }catch (Exception e){
            Log.i("MYTAG",e.getMessage());
        }

    }
}