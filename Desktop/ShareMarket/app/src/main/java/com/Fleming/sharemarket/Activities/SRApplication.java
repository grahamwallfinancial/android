package com.Fleming.sharemarket.Activities;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * Created by Toshiba- on 7/8/2017.
 */

public class SRApplication extends Application {

    private String mDirectory;

    @Override
    public void onCreate() {
        super.onCreate();

//        AVOSCloud.initialize(this, "73JTwkwUS8uLaNT3BVDEIvzo-gzGzoHsz", "mpKP52gojCfEf4TUj3j5JfiE");
//        AVAnalytics.enableCrashReport(this, true);

        createFolder();
    }

    public String createFolder(){
        mDirectory = Environment.getExternalStorageDirectory() + "/ScreenRecorder/";
        File file = new File(mDirectory);
        if(!file.exists()){
            file.mkdirs();
        }
        return mDirectory;
    }

    public String getDirectory(){
        return mDirectory;
    }

}