package com.fangmingdong.androiddemo.jobschedule;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.fangmingdong.androiddemo.R;
import com.fangmingdong.androiddemo.jobschedule.net.GPRSJobService;
import com.fangmingdong.androiddemo.jobschedule.net.NoNetJobService;
import com.fangmingdong.androiddemo.jobschedule.net.WifiJobService;
import com.fangmingdong.androiddemo.jobschedule.time.DelayJobService;

import static android.app.job.JobInfo.NETWORK_TYPE_UNMETERED;

public class JobScheduleActivity extends AppCompatActivity {


    private TextView mViewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_schedule);


        mViewById = (TextView) findViewById(R.id.tv_job);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String gprs = sharedPreferences.getString(GPRSJobService.SP_KEY, "");
        String noNet = sharedPreferences.getString(NoNetJobService.SP_KEY, "");
        String wifi = sharedPreferences.getString(WifiJobService.SP_KEY, "");

        mViewById.setText(gprs + "\r\n" +
                noNet + "\r\n" +
                wifi);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startWifiJob(View view) {
        JobScheduler jobScheduler = (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);


        // net enable
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            ComponentName gprsJobService = new ComponentName(this, GPRSJobService.class);
            int jobId3 = 12322;
            JobInfo gprsJobInfo = null;
            gprsJobInfo = new JobInfo.Builder(jobId3, gprsJobService)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NOT_ROAMING)   // net enable
                    .build();
            jobScheduler.schedule(gprsJobInfo);
        }


        // wifi connect
        ComponentName jobService = new ComponentName(this, WifiJobService.class);
        int jobId = 123;
        JobInfo jobInfo = new JobInfo.Builder(jobId, jobService)
                .setRequiredNetworkType(NETWORK_TYPE_UNMETERED) // wifi / 免费网络
                .build();

        jobScheduler.schedule(jobInfo);


        // net disable
//        ComponentName noNetJobService = new ComponentName(this, NoNetJobService.class);
//        int jobId2 = 1232;
//        JobInfo noNetJobInfo = new JobInfo.Builder(jobId2, noNetJobService)
//                .setRequiredNetworkType(NETWORK_TYPE_NONE) // wifi / 免费网络
//                .build();
//        jobScheduler.schedule(noNetJobInfo);


        // start delay job
        ComponentName delayjobService = new ComponentName(this, DelayJobService.class);
        JobInfo delayJob = new JobInfo.Builder(2, delayjobService)
                .setMinimumLatency(3000)
                .build();
        jobScheduler.schedule(delayJob);
    }
}
