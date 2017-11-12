package com.orangex.amazingfellow.features.homepage.recent.pulling;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.orangex.amazingfellow.features.homepage.recent.pulling.data.RecentDataHelper;

public class PullingJobService extends JobService {
    private static final String TAG ="datui "+ PullingJobService.class.getSimpleName();
    public PullingJobService() {
    }
    
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(TAG, "onStartJob: ");
        RecentDataHelper.doPullingJob(RecentDataHelper.TYPE_REFRESH);
        return false;
    }
    
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG, "onStopJob: ");
        return false;
    }
}
