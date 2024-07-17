package com.msc.voice_chager.reactlibrary.task;

import android.os.AsyncTask;


public class DatabaseTask extends AsyncTask<Void, Void, Void> {
    private TaskListener mDownloadListener;

    public DatabaseTask(TaskListener taskListener) {
        this.mDownloadListener = taskListener;
    }

    @Override  
    public void onPreExecute() {
        TaskListener taskListener = this.mDownloadListener;
        if (taskListener != null) {
            taskListener.onPreExecuteTask();
        }
    }

    @Override  
    public Void doInBackground(Void... voidArr) {
        TaskListener taskListener = this.mDownloadListener;
        if (taskListener == null) {
            return null;
        }
        taskListener.onDoInBackgroundTask();
        return null;
    }

    @Override  
    public void onPostExecute(Void r1) {
        TaskListener taskListener = this.mDownloadListener;
        if (taskListener != null) {
            taskListener.onPostExecuteTask();
        }
    }
}
