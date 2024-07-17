package com.msc.voice_chager.reactlibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.un4seen.bass.BASS;
import com.msc.voice_chager.R;
import com.msc.voice_chager.reactlibrary.basseffect.DBMediaListener;
import com.msc.voice_chager.reactlibrary.basseffect.MediaPlayerDb;
import com.msc.voice_chager.reactlibrary.constants.InterfaceVoiceChangerListener;
import com.msc.voice_chager.reactlibrary.dataMng.ParsingJsonObjects;
import com.msc.voice_chager.reactlibrary.object.ModelEffects;
import com.msc.voice_chager.reactlibrary.task.DatabaseTask;
import com.msc.voice_chager.reactlibrary.task.InterFaceCallBack;
import com.msc.voice_chager.reactlibrary.task.TaskListener;

import java.io.File;
import java.util.ArrayList;


public class ChangeEffectsModule {
    public static final String logTag = "VoiceChangerModule";
    private final Context context;
    private boolean isInitIs;
    private MediaPlayerDb mediaPlayerDb;
    private String strChangeVoiceName;
    public ArrayList<ModelEffects> modelEffects = new ArrayList<>();
    private String strAudioPath = null;
    public File fileOutputDirectory = null;
    public Integer indexPLaying = null;

    public ChangeEffectsModule(Context context) {
        this.context = context;
        setDBMedia(this.mediaPlayerDb);
        onInitAudioDevice();
    }

    public Integer getIndexPLaying() {
        return this.indexPLaying;
    }

    public void insertEffect(String str) {
        this.modelEffects.add(ParsingJsonObjects.jsonToObjectEffects(str));
    }

    public void insertEffect(ModelEffects effects) {
        this.modelEffects.add(effects);
    }

    public MediaPlayerDb getDBMedia() {
        return this.mediaPlayerDb;
    }

    public void setDBMedia(MediaPlayerDb mediaPlayerDb) {
        this.mediaPlayerDb = mediaPlayerDb;
    }

    public void show(String str, int i) {
        Toast.makeText(this.context, str, i).show();
    }

    public void setPath(String str) {
        this.strAudioPath = str;
    }

    public void setIndexPLaying(Integer num) {
        if (num != null) {
            this.indexPLaying = num;
        }
    }

    public void saveTheEffects(int i, String str, BaseCallback baseCallback) {
        onSaveEffect(this.modelEffects.get(i), str, baseCallback);
    }

    public void createOutputDir(Activity activity) {
        this.fileOutputDirectory = getDirectory(activity);
    }

    public void createDBMedia() {
        onCreateDBMedia();
    }

    public void effectPlay(int i) {
        Log.d(logTag, "audioPath: " + this.strAudioPath);
        String str = this.strAudioPath;
        if (str != null || !str.equals("")) {
            File file = new File(this.strAudioPath);
            if (!file.exists() || !file.isFile()) {
                toastShow("File not found exception");
            }
        }
        try {
            setIndexPLaying(Integer.valueOf(i));
            Log.e(logTag, "playEffect: " + this.modelEffects.get(i).getStrName());
            onEffectPlay(this.modelEffects.get(i));
        } catch (Exception unused) {
        }
    }

    private void onEffectPlay(ModelEffects modelEffects) {
        if (modelEffects.isPlayingBool()) {
            modelEffects.setPlayingBool(false);
            MediaPlayerDb mediaPlayerDb = this.mediaPlayerDb;
            if (mediaPlayerDb != null) {
                mediaPlayerDb.audioPause();
                return;
            }
            return;
        }
        onStateReset();
        modelEffects.setPlayingBool(true);
        MediaPlayerDb mediaPlayerDb2 = this.mediaPlayerDb;
        if (mediaPlayerDb2 != null) {
            mediaPlayerDb2.setPathMix(modelEffects.getStrPathMix());
            this.mediaPlayerDb.setMixNeed(modelEffects.isMixBool());
            this.mediaPlayerDb.audioPrepare();
            this.mediaPlayerDb.setThisReverse(modelEffects.isReverseBool());
            this.mediaPlayerDb.setPitchAudio(modelEffects.getIntPitch());
            this.mediaPlayerDb.compressorSet(modelEffects.getFloatCompressor());
            this.mediaPlayerDb.setRateAudio(modelEffects.getFloatRate());
            this.mediaPlayerDb.setEQ1Audio(modelEffects.getFloatyEcho1());
            this.mediaPlayerDb.setEQ2Audio(modelEffects.getFloatEq2());
            this.mediaPlayerDb.setEQ3Audio(modelEffects.getFloatEq3());
            this.mediaPlayerDb.phrserSet(modelEffects.getFloatPhaser());
            this.mediaPlayerDb.setWahAuto(modelEffects.getFloatAutoWah());
            this.mediaPlayerDb.setReverbAudio(modelEffects.getFloatReverb());
            this.mediaPlayerDb.setEffectEcho4(modelEffects.getFloatEcho4());
            this.mediaPlayerDb.setEchoAudio(modelEffects.getFloatEcho());
            this.mediaPlayerDb.setFilterQuad(modelEffects.getFloatFilter());
            this.mediaPlayerDb.setEffectFlang(modelEffects.getFloatFlange());
            this.mediaPlayerDb.chorusSet(modelEffects.getFloatChorus());
            this.mediaPlayerDb.setAmpli(modelEffects.getFloatAmplify());
            this.mediaPlayerDb.disortSet(modelEffects.getFloatDistort());
            this.mediaPlayerDb.rotateSet(modelEffects.getFloatRotate());
            this.mediaPlayerDb.audioStart();
        }
    }

    private void onInitAudioDevice() {
        if (this.isInitIs) {
            return;
        }
        this.isInitIs = true;
        if (!BASS.BASS_Init(-1, 44100, 0)) {
            new Exception("VoiceChangerModule Can't initialize device").printStackTrace();
            this.isInitIs = false;
            return;
        }
        String str = this.context.getApplicationInfo().nativeLibraryDir;
        try {
            BASS.BASS_PluginLoad(str + "/libbass_fx.so", 0);
            BASS.BASS_PluginLoad(str + "/libbassenc.so", 0);
            BASS.BASS_PluginLoad(str + "/libbassmix.so", 0);
            BASS.BASS_PluginLoad(str + "/libbasswv.so", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSaveEffect(ModelEffects modelEffects, final String str, final BaseCallback baseCallback) {
        if (this.mediaPlayerDb != null) {
            this.strChangeVoiceName = str + ".wav";
            startSaveEffect(modelEffects, new InterFaceCallBack() {
                @Override  
                public void onCallBackAction() {
                    File file = new File(ChangeEffectsModule.this.fileOutputDirectory, str);
                    if (file.exists()) {
                        ChangeEffectsModule.this.toastShow(String.format("Your voice path is %1$s", file.getAbsolutePath()));
                    }
                    baseCallback.onSuccess();
                }
            });
        }
    }

    private void startSaveEffect(final ModelEffects modelEffects, final InterFaceCallBack interFaceCallBack) {
        final File file = new File(this.fileOutputDirectory, this.strChangeVoiceName);
        final MediaPlayerDb mediaPlayerDb = new MediaPlayerDb(this.strAudioPath);
        new DatabaseTask(new TaskListener() {
            @Override  
            public void onPreExecuteTask() {
            }

            @Override  
            public void onDoInBackgroundTask() {
                if (mediaPlayerDb.initSolveToMedia()) {
                    mediaPlayerDb.setThisReverse(modelEffects.isReverseBool());
                    mediaPlayerDb.setPitchAudio(modelEffects.getIntPitch());
                    mediaPlayerDb.compressorSet(modelEffects.getFloatCompressor());
                    mediaPlayerDb.setRateAudio(modelEffects.getFloatRate());
                    mediaPlayerDb.setEQ1Audio(modelEffects.getFloatyEcho1());
                    mediaPlayerDb.setEQ2Audio(modelEffects.getFloatEq2());
                    mediaPlayerDb.setEQ3Audio(modelEffects.getFloatEq3());
                    mediaPlayerDb.phrserSet(modelEffects.getFloatPhaser());
                    mediaPlayerDb.setWahAuto(modelEffects.getFloatAutoWah());
                    mediaPlayerDb.setReverbAudio(modelEffects.getFloatReverb());
                    mediaPlayerDb.setEffectEcho4(modelEffects.getFloatEcho4());
                    mediaPlayerDb.setEchoAudio(modelEffects.getFloatEcho());
                    mediaPlayerDb.setFilterQuad(modelEffects.getFloatFilter());
                    mediaPlayerDb.setEffectFlang(modelEffects.getFloatFlange());
                    mediaPlayerDb.chorusSet(modelEffects.getFloatChorus());
                    mediaPlayerDb.setAmpli(modelEffects.getFloatAmplify());
                    mediaPlayerDb.disortSet(modelEffects.getFloatDistort());
                    mediaPlayerDb.rotateSet(modelEffects.getFloatRotate());
                    mediaPlayerDb.saveAsFile(file.getAbsolutePath());
                    mediaPlayerDb.audioRelease();
                }
            }

            @Override  
            public void onPostExecuteTask() {
                InterFaceCallBack interFaceCallBack2 = interFaceCallBack;
                if (interFaceCallBack2 != null) {
                    interFaceCallBack2.onCallBackAction();
                }
            }
        }).execute(new Void[0]);
    }

    private void onCreateDBMedia() {
        String str = this.strAudioPath;
        if (str != null || !str.equals("")) {
            MediaPlayerDb mediaPlayerDb = new MediaPlayerDb(this.strAudioPath);
            this.mediaPlayerDb = mediaPlayerDb;
            mediaPlayerDb.audioPrepare();
            this.mediaPlayerDb.setOnDBMediaListener(new DBMediaListener() {
                public void onMediaError() {
                }

                @Override  
                public void onMediaCompleteListener() {
                    ChangeEffectsModule.this.modelEffects.get(ChangeEffectsModule.this.indexPLaying.intValue()).setPlayingBool(false);
                    ChangeEffectsModule.this.setIndexPLaying(null);
                }
            });
            return;
        }
        toastShow("Media file not found!");
    }

    public void toastShow(String str) {
        Toast makeText = Toast.makeText(this.context, str, Toast.LENGTH_SHORT);
        makeText.setGravity(80, 0, 0);
        makeText.show();
    }

    public void onStateReset() {
        ArrayList<ModelEffects> arrayList = this.modelEffects;
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        for (int i = 0; i < this.modelEffects.size(); i++) {
            if (this.modelEffects.get(i).isPlayingBool()) {
                this.modelEffects.get(i).setPlayingBool(false);
            }
        }
    }

    private File getDirectory(Activity activity) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + activity.getResources().getString(R.string.app_name) + "/" + InterfaceVoiceChangerListener.recordedFolderName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
