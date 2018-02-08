package com.fangmingdong.androiddemo.jobschedule.net;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.database.Observable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class WifiJobService extends JobService {
    private static final String TAG = WifiJobService.class.getSimpleName();
    private Observable<String> mObservable;
    public static final String SP_KEY = "WifiJobService onStartJob";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: ");

        SharedPreferences sp = getApplication().getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor onStartJob = sp.edit().putString(SP_KEY, "wifi start");
        onStartJob.commit();

//        mObservable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext("short");
//            }
//        });
//
//        mObservable.observeOn(Schedulers.io())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<String>() {
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        mD = d;
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        LoggerUtils.d("receive " + s);
//                        jobFinished(params, false); // 主动结束任务
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

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
