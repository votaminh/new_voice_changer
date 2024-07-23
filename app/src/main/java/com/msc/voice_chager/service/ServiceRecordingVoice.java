package com.msc.voice_chager.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.audio.DtsUtil;
import com.msc.voice_chager.R;
import com.msc.voice_chager.domain.layer.RecordingModel;
import com.msc.voice_chager.utils.FileMethods;

import java.io.File;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;


public class ServiceRecordingVoice extends Service {
    private static final String extraAcvtivityStarter = "com.vijay.voice.changer.EXTRA_ACTIVITY_STARTER";
    private static final String logTag = "SCHEDULED_RECORDER_TAG";
    public static int onCallsCreate;
    public static int onCallsDestroy;
    public static int onCallsStartCommands;
    private final String classNameSimple = getClass().getSimpleName();
    private boolean isRecording = false;
    private boolean isResumeRecording = false;
    public long mElapsedMillis = 0;
    private String strFileName = null;
    private String strFilePath = null;
    public TimerTask timerTask = null;
    public MediaRecorder mediaRecorder = null;
    private final IBinder myBinder = new LocalBinder();
    public OnRecordingStatusChangedListener onRecordingStatusChangedListener = null;


    public interface OnRecordingStatusChangedListener {
        void onAmplitudeInfo(int i);

        void onPauseRecording();

        void onResumeRecording();

        void onSkipRecording();

        void onStartedRecording();

        void onStopRecording(RecordingModel recordingModel);

        void onTimerChanged(int i);
    }

    static long cusMethod(ServiceRecordingVoice serviceRecordingVoice, long j) {
        long j2 = serviceRecordingVoice.mElapsedMillis + j;
        serviceRecordingVoice.mElapsedMillis = j2;
        return j2;
    }

    public static Intent makeIntent(Context context, boolean z) {
        Intent intent = new Intent(context.getApplicationContext(), ServiceRecordingVoice.class);
        intent.putExtra(extraAcvtivityStarter, z);
        return intent;
    }


    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public ServiceRecordingVoice getService() {
            return ServiceRecordingVoice.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.myBinder;
    }

    public void setOnRecordingStatusChangedListener(OnRecordingStatusChangedListener onRecordingStatusChangedListener) {
        this.onRecordingStatusChangedListener = onRecordingStatusChangedListener;
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        onCallsStartCommands++;
        intent.getBooleanExtra(extraAcvtivityStarter, false);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        onCallsCreate++;
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        onCallsDestroy++;
        super.onDestroy();
        if (this.mediaRecorder != null) {
            recordingStop();
        }
        if (this.onRecordingStatusChangedListener != null) {
            this.onRecordingStatusChangedListener = null;
        }
    }

    public void startRecording(int i) {
        try {
            startForeground(2, notificationCreate());
            setFileNamePath();
            MediaRecorder mediaRecorder = new MediaRecorder();
            this.mediaRecorder = mediaRecorder;
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(this.strFilePath);
            mediaRecorder.setMaxDuration(i);
            mediaRecorder.setAudioChannels(1);
            mediaRecorder.setAudioSamplingRate(44100);
            mediaRecorder.setAudioEncodingBitRate(DtsUtil.DTS_MAX_RATE_BYTES_PER_SECOND);
            mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mediaRecorder2, int i2, int i3) {
                    if (i2 == 800) {
                        ServiceRecordingVoice.this.recordingStop();
                    }
                }
            });
            this.mediaRecorder.prepare();
            this.mediaRecorder.start();
            this.isRecording = true;
            this.isResumeRecording = true;
            timerStart();
        } catch (Exception e) {
            Log.e(logTag, this.classNameSimple + " - startRecording(): prepare() failed" + e.toString());
        }
        OnRecordingStatusChangedListener onRecordingStatusChangedListener = this.onRecordingStatusChangedListener;
        if (onRecordingStatusChangedListener != null) {
            onRecordingStatusChangedListener.onStartedRecording();
        }
    }

    private void setFileNamePath() {
        this.strFileName = "Voice_effect_" + System.currentTimeMillis();
        this.strFilePath = FileMethods.getMainDirPath(this) + "/" + this.strFileName + ".mp3";
    }

    private void timerStart() {
        Timer timer = new Timer();
        this.mElapsedMillis = 0L;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (ServiceRecordingVoice.this.timerTask == null) {
                    cancel();
                }
                ServiceRecordingVoice.cusMethod(ServiceRecordingVoice.this, 100L);
                if (ServiceRecordingVoice.this.onRecordingStatusChangedListener != null) {
                    ServiceRecordingVoice.this.onRecordingStatusChangedListener.onTimerChanged(((int) ServiceRecordingVoice.this.mElapsedMillis) / 1000);
                }
                if (ServiceRecordingVoice.this.onRecordingStatusChangedListener == null || ServiceRecordingVoice.this.mediaRecorder == null) {
                    return;
                }
                try {
                    ServiceRecordingVoice.this.onRecordingStatusChangedListener.onAmplitudeInfo(ServiceRecordingVoice.this.mediaRecorder.getMaxAmplitude());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.timerTask = timerTask;
        timer.scheduleAtFixedRate(timerTask, 100L, 100L);
    }

    public void recordingStop() {
        try {
            this.mediaRecorder.stop();
            this.mediaRecorder.release();
            this.isRecording = false;
            this.isResumeRecording = false;
            this.mediaRecorder = null;
            TimerTask timerTask = this.timerTask;
            if (timerTask != null) {
                timerTask.cancel();
                this.timerTask = null;
            }
            Log.e("rec---", "recordingStop: strFileName :  " + this.strFileName);
            Log.e("rec---", "recordingStop: strFilePath :  " + this.strFilePath);
            Log.e("rec---", "recordingStop: mElapsedMillis :  " + this.mElapsedMillis);
            RecordingModel recordingModel = new RecordingModel(this.strFileName, this.strFilePath, this.mElapsedMillis, System.currentTimeMillis(), 0);
            OnRecordingStatusChangedListener onRecordingStatusChangedListener = this.onRecordingStatusChangedListener;
            if (onRecordingStatusChangedListener != null) {
                onRecordingStatusChangedListener.onStopRecording(recordingModel);
            }
            if (this.onRecordingStatusChangedListener == null) {
                stopSelf();
            }
            stopForeground(true);
        } catch (Exception e) {
            PrintStream printStream = System.out;
            printStream.println("RecordingService.stopRecording e = " + e);
        }
    }

    public void fileRecordSkip() {
        try {
            this.mediaRecorder.stop();
            this.mediaRecorder.release();
            this.isRecording = false;
            this.isResumeRecording = false;
            this.mediaRecorder = null;
            this.mElapsedMillis = 0L;
            OnRecordingStatusChangedListener onRecordingStatusChangedListener = this.onRecordingStatusChangedListener;
            if (onRecordingStatusChangedListener != null) {
                onRecordingStatusChangedListener.onSkipRecording();
            }
            TimerTask timerTask = this.timerTask;
            if (timerTask != null) {
                timerTask.cancel();
                this.timerTask = null;
            }
            if (this.strFilePath != null) {
                File file = new File(this.strFilePath);
                if (file.exists()) {
                    file.delete();
                }
            }
            if (this.onRecordingStatusChangedListener == null) {
                stopSelf();
            }
            stopForeground(true);
        } catch (Exception e) {
            PrintStream printStream = System.out;
            printStream.println("RecordingService.skipFileRecord e = " + e);
        }
    }

    public void pauseRecording() {
        try {
            this.mediaRecorder.pause();
            this.isResumeRecording = false;
            OnRecordingStatusChangedListener onRecordingStatusChangedListener = this.onRecordingStatusChangedListener;
            if (onRecordingStatusChangedListener != null) {
                onRecordingStatusChangedListener.onPauseRecording();
            }
            TimerTask timerTask = this.timerTask;
            if (timerTask != null) {
                timerTask.cancel();
                this.timerTask = null;
            }
        } catch (Exception e) {
            PrintStream printStream = System.out;
            printStream.println("RecordingService.pauseRecording e = " + e);
        }
    }

    public void resumeRecording() {
        try {
            this.mediaRecorder.resume();
            this.isResumeRecording = true;
            OnRecordingStatusChangedListener onRecordingStatusChangedListener = this.onRecordingStatusChangedListener;
            if (onRecordingStatusChangedListener != null) {
                onRecordingStatusChangedListener.onResumeRecording();
            }
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (ServiceRecordingVoice.this.timerTask == null) {
                        cancel();
                    }
                    ServiceRecordingVoice.cusMethod(ServiceRecordingVoice.this, 100L);
                    if (ServiceRecordingVoice.this.onRecordingStatusChangedListener != null) {
                        ServiceRecordingVoice.this.onRecordingStatusChangedListener.onTimerChanged(((int) ServiceRecordingVoice.this.mElapsedMillis) / 1000);
                    }
                    if (ServiceRecordingVoice.this.onRecordingStatusChangedListener == null || ServiceRecordingVoice.this.mediaRecorder == null) {
                        return;
                    }
                    try {
                        ServiceRecordingVoice.this.onRecordingStatusChangedListener.onAmplitudeInfo(ServiceRecordingVoice.this.mediaRecorder.getMaxAmplitude());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            this.timerTask = timerTask;
            timer.scheduleAtFixedRate(timerTask, 100L, 100L);
        } catch (Exception e) {
            PrintStream printStream = System.out;
            printStream.println("RecordingService.resumeRecording e = " + e);
        }
    }

    private Notification notificationCreate() {
        return new NotificationCompat.Builder(getApplicationContext(), Build.VERSION.SDK_INT >= 26 ? notiChannelCreate() : "").setSmallIcon(R.drawable.ic_mic_white_36dp).setContentTitle(getString(R.string.notification_recording)).setOngoing(true).build();
    }

    private String notiChannelCreate() {
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("recording_service", "Recording Service", NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setLightColor(-16776961);
            notificationChannel.setLockscreenVisibility(0);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);

        }
        return "recording_service";
    }

    public boolean isRecording() {
        return this.isRecording;
    }

    public boolean isResumeRecording() {
        return this.isResumeRecording;
    }
}
