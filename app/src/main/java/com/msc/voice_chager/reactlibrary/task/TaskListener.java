package com.msc.voice_chager.reactlibrary.task;


public interface TaskListener {
    void onDoInBackgroundTask();

    void onPostExecuteTask();

    void onPreExecuteTask();
}
