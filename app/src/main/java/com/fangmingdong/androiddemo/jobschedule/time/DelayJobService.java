package com.fangmingdong.androiddemo.jobschedule.time;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DelayJobService extends JobService {
    private static final String TAG = DelayJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");

        Toast.makeText(this, "DelayJobService start", Toast.LENGTH_SHORT).show();
        return false; // return true: 如果有异步任务， false：如果没有异步任务
    }

    /**
     * 任务被动结束的时候调用
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: ");

        return true; // 返回true表示你希望对该任务重新进行调度，同样需要遵守退避策略；返回false表示你希望放弃该任务
    }

}
