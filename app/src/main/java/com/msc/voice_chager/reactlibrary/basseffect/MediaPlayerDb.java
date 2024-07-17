package com.msc.voice_chager.reactlibrary.basseffect;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.un4seen.bass.BASS;
import com.un4seen.bass.BASS_FX;
import com.un4seen.bass.BASSenc;
import com.un4seen.bass.BASSmix;
import com.msc.voice_chager.reactlibrary.constants.InterfaceVoiceChangerListener;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;


public class MediaPlayerDb implements DBMediaTypeConstant {
    private static final String logTag = "DBMediaPlayer";
    private int amplifyFx;
    private int autoEffectFx;
    private int bigQuedEffects;
    private int channelPlay;
    private int chorosEffectFx;
    private int compressorEffects;
    private int effectDisort;
    private int effectEQ2;
    private int effectEQ3;
    private int effectEcho;
    private int effectFlenger;
    private int effectPhaser;
    private int effectReverb;
    private int effectRotate;
    private int effextEQ1;
    private int effextEQ4;
    private ArrayList<Integer> integerArrayList;
    private boolean isMixNeed;
    private boolean isThisReverse;
    private DBMediaListener mediaListener;
    private final String strMediaPath;
    private String strMixPath;
    private int tempChanel;
    private int intCurrPosition = 0;
    private int intDuration = 0;
    private boolean isPlaying = false;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {  

        @Override  
        public void handleMessage(Message message) {
            MediaPlayerDb mediaPlayerDb = MediaPlayerDb.this;
            mediaPlayerDb.intCurrPosition = mediaPlayerDb.getChannelPos();
            MediaPlayerDb mediaPlayerDb2 = MediaPlayerDb.this;
            mediaPlayerDb2.intDuration = mediaPlayerDb2.getChanLength();
            if (!MediaPlayerDb.this.isThisReverse) {
                if (MediaPlayerDb.this.intCurrPosition >= MediaPlayerDb.this.intDuration) {
                    removeMessages(0);
                    if (MediaPlayerDb.this.mediaListener != null) {
                        MediaPlayerDb.this.mediaListener.onMediaCompleteListener();
                        return;
                    }
                    return;
                }
                sendEmptyMessageDelayed(0, 50L);
            } else if (MediaPlayerDb.this.intCurrPosition <= 0) {
                removeMessages(0);
                if (MediaPlayerDb.this.mediaListener != null) {
                    MediaPlayerDb.this.mediaListener.onMediaCompleteListener();
                }
            } else {
                sendEmptyMessageDelayed(0, 50L);
            }
        }
    };

    public MediaPlayerDb(String str) {
        this.strMediaPath = str;
    }

    public void setMixNeed(boolean z) {
        this.isMixNeed = z;
    }

    public void setPathMix(String str) {
        this.strMixPath = str;
    }

    public boolean audioPrepare() {
        String str = this.strMediaPath;
        if (str == null || str.equals("")) {
            return false;
        }
        if (this.strMediaPath.toLowerCase(Locale.getDefault()).endsWith(mp3Type) || this.strMediaPath.toLowerCase(Locale.getDefault()).endsWith(wavType) || this.strMediaPath.toLowerCase(Locale.getDefault()).endsWith(oggType) || this.strMediaPath.toLowerCase(Locale.getDefault()).endsWith(flacType) || this.strMediaPath.toLowerCase(Locale.getDefault()).endsWith(aacType) || this.strMediaPath.toLowerCase(Locale.getDefault()).endsWith(midType) || this.strMediaPath.toLowerCase(Locale.getDefault()).endsWith(wmaType)) {
            mediaInit();
            return true;
        }
        new Exception("DBMidiPlayer:can not support file format").printStackTrace();
        return false;
    }

    public void audioStart() {
        this.isPlaying = true;
        int i = this.channelPlay;
        if (i != 0) {
            BASS.BASS_ChannelPlay(i, false);
        }
        this.mHandler.sendEmptyMessage(0);
    }

    public void setPitchAudio(int i) {
        int i2 = this.channelPlay;
        if (i2 != 0) {
            BASS.BASS_ChannelSetAttribute(i2, 65537, i);
        }
    }

    public void setRateAudio(float f) {
        int i = this.channelPlay;
        if (i != 0) {
            BASS.BASS_ChannelSetAttribute(i, 65536, f);
        }
    }

    public void setReverbAudio(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.effectReverb == 0) {
                    this.effectReverb = BASS.BASS_ChannelSetFX(i, 8, 0);
                }
                if (this.effectReverb != 0) {
                    BASS.BASS_DX8_REVERB bass_dx8_reverb = new BASS.BASS_DX8_REVERB();
                    BASS.BASS_FXGetParameters(this.effectReverb, bass_dx8_reverb);
                    bass_dx8_reverb.fReverbMix = fArr[0];
                    bass_dx8_reverb.fReverbTime = fArr[1];
                    bass_dx8_reverb.fHighFreqRTRatio = fArr[2];
                    BASS.BASS_FXSetParameters(this.effectReverb, bass_dx8_reverb);
                    return;
                }
                return;
            }
            int i2 = this.effectReverb;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.effectReverb = 0;
            }
        }
    }

    public void setEchoAudio(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.effectEcho == 0) {
                    this.effectEcho = BASS.BASS_ChannelSetFX(i, 3, 0);
                }
                if (this.effectEcho != 0) {
                    BASS.BASS_DX8_ECHO bass_dx8_echo = new BASS.BASS_DX8_ECHO();
                    BASS.BASS_FXGetParameters(this.effectEcho, bass_dx8_echo);
                    bass_dx8_echo.fLeftDelay = fArr[0];
                    bass_dx8_echo.fRightDelay = fArr[1];
                    bass_dx8_echo.fFeedback = fArr[2];
                    if (fArr.length == 4) {
                        bass_dx8_echo.fWetDryMix = fArr[3];
                    }
                    BASS.BASS_FXSetParameters(this.effectEcho, bass_dx8_echo);
                    return;
                }
                return;
            }
            int i2 = this.effectEcho;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.effectEcho = 0;
            }
        }
    }

    public void setAmpli(float f) {
        int i = this.channelPlay;
        if (i != 0) {
            if (f != 0.0f) {
                if (this.amplifyFx == 0) {
                    this.amplifyFx = BASS.BASS_ChannelSetFX(i, 65544, 0);
                }
                if (this.amplifyFx != 0) {
                    BASS_FX.BASS_BFX_DAMP bass_bfx_damp = new BASS_FX.BASS_BFX_DAMP();
                    BASS.BASS_FXGetParameters(this.amplifyFx, bass_bfx_damp);
                    bass_bfx_damp.fGain = f;
                    BASS.BASS_FXSetParameters(this.amplifyFx, bass_bfx_damp);
                    return;
                }
                return;
            }
            int i2 = this.amplifyFx;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.amplifyFx = 0;
            }
        }
    }

    public void disortSet(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.effectDisort == 0) {
                    this.effectDisort = BASS.BASS_ChannelSetFX(i, 2, 0);
                }
                if (this.effectDisort != 0) {
                    BASS.BASS_DX8_DISTORTION bass_dx8_distortion = new BASS.BASS_DX8_DISTORTION();
                    BASS.BASS_FXGetParameters(this.effectDisort, bass_dx8_distortion);
                    bass_dx8_distortion.fEdge = fArr[0];
                    bass_dx8_distortion.fGain = fArr[1];
                    bass_dx8_distortion.fPostEQBandwidth = fArr[2];
                    bass_dx8_distortion.fPostEQCenterFrequency = fArr[3];
                    bass_dx8_distortion.fPreLowpassCutoff = fArr[4];
                    BASS.BASS_FXSetParameters(this.effectDisort, bass_dx8_distortion);
                    return;
                }
                return;
            }
            int i2 = this.effectDisort;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.effectDisort = 0;
            }
        }
    }

    public void chorusSet(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.chorosEffectFx == 0) {
                    this.chorosEffectFx = BASS.BASS_ChannelSetFX(i, BASS_FX.BASS_FX_BFX_CHORUS, 0);
                }
                if (this.chorosEffectFx != 0) {
                    BASS_FX.BASS_BFX_CHORUS bass_bfx_chorus = new BASS_FX.BASS_BFX_CHORUS();
                    BASS.BASS_FXGetParameters(this.chorosEffectFx, bass_bfx_chorus);
                    bass_bfx_chorus.fDryMix = fArr[0];
                    bass_bfx_chorus.fWetMix = fArr[1];
                    bass_bfx_chorus.fFeedback = fArr[2];
                    bass_bfx_chorus.fMinSweep = fArr[3];
                    bass_bfx_chorus.fMaxSweep = fArr[4];
                    bass_bfx_chorus.fRate = fArr[5];
                    BASS.BASS_FXSetParameters(this.chorosEffectFx, bass_bfx_chorus);
                    return;
                }
                return;
            }
            int i2 = this.chorosEffectFx;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.chorosEffectFx = 0;
            }
        }
    }

    public void setFilterQuad(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.bigQuedEffects == 0) {
                    this.bigQuedEffects = BASS.BASS_ChannelSetFX(i, 65555, 0);
                }
                if (this.bigQuedEffects != 0) {
                    BASS_FX.BASS_BFX_BQF bass_bfx_bqf = new BASS_FX.BASS_BFX_BQF();
                    BASS.BASS_FXGetParameters(this.bigQuedEffects, bass_bfx_bqf);
                    bass_bfx_bqf.lFilter = (int) fArr[0];
                    bass_bfx_bqf.fCenter = fArr[1];
                    bass_bfx_bqf.fBandwidth = fArr[2];
                    BASS.BASS_FXSetParameters(this.bigQuedEffects, bass_bfx_bqf);
                    return;
                }
                return;
            }
            int i2 = this.bigQuedEffects;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.bigQuedEffects = 0;
            }
        }
    }

    public void setEffectEcho4(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.effextEQ4 == 0) {
                    this.effextEQ4 = BASS.BASS_ChannelSetFX(i, 65556, 0);
                }
                if (this.effextEQ4 != 0) {
                    BASS_FX.BASS_BFX_ECHO4 bass_bfx_echo4 = new BASS_FX.BASS_BFX_ECHO4();
                    bass_bfx_echo4.fDryMix = (int) fArr[0];
                    bass_bfx_echo4.fWetMix = fArr[1];
                    bass_bfx_echo4.fFeedback = fArr[2];
                    bass_bfx_echo4.fDelay = fArr[3];
                    bass_bfx_echo4.bStereo = false;
                    BASS.BASS_FXSetParameters(this.effextEQ4, bass_bfx_echo4);
                    return;
                }
                return;
            }
            int i2 = this.effextEQ4;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.effextEQ4 = 0;
            }
        }
    }

    public void setEQ1Audio(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.effextEQ1 == 0) {
                    this.effextEQ1 = BASS.BASS_ChannelSetFX(i, 7, 0);
                }
                if (this.effextEQ1 != 0) {
                    BASS.BASS_DX8_PARAMEQ bass_dx8_parameq = new BASS.BASS_DX8_PARAMEQ();
                    bass_dx8_parameq.fCenter = fArr[0];
                    bass_dx8_parameq.fBandwidth = fArr[1];
                    bass_dx8_parameq.fGain = fArr[2];
                    BASS.BASS_FXSetParameters(this.effextEQ1, bass_dx8_parameq);
                    return;
                }
                return;
            }
            int i2 = this.effextEQ1;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.effextEQ1 = 0;
            }
        }
    }

    public void setEQ2Audio(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.effectEQ2 == 0) {
                    this.effectEQ2 = BASS.BASS_ChannelSetFX(i, 7, 0);
                }
                if (this.effectEQ2 != 0) {
                    BASS.BASS_DX8_PARAMEQ bass_dx8_parameq = new BASS.BASS_DX8_PARAMEQ();
                    bass_dx8_parameq.fCenter = fArr[0];
                    bass_dx8_parameq.fBandwidth = fArr[1];
                    bass_dx8_parameq.fGain = fArr[2];
                    BASS.BASS_FXSetParameters(this.effectEQ2, bass_dx8_parameq);
                    return;
                }
                return;
            }
            int i2 = this.effectEQ2;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.effectEQ2 = 0;
            }
        }
    }

    public void setEQ3Audio(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.effectEQ3 == 0) {
                    this.effectEQ3 = BASS.BASS_ChannelSetFX(i, 7, 0);
                }
                if (this.effectEQ3 != 0) {
                    BASS.BASS_DX8_PARAMEQ bass_dx8_parameq = new BASS.BASS_DX8_PARAMEQ();
                    bass_dx8_parameq.fCenter = fArr[0];
                    bass_dx8_parameq.fBandwidth = fArr[1];
                    bass_dx8_parameq.fGain = fArr[2];
                    BASS.BASS_FXSetParameters(this.effectEQ3, bass_dx8_parameq);
                    return;
                }
                return;
            }
            int i2 = this.effectEQ3;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.effectEQ3 = 0;
            }
        }
    }

    public void rotateSet(float f) {
        int i = this.channelPlay;
        if (i != 0) {
            if (f != 0.0f) {
                if (this.effectRotate == 0) {
                    this.effectRotate = BASS.BASS_ChannelSetFX(i, 65536, 0);
                }
                if (this.effectRotate != 0) {
                    BASS_FX.BASS_BFX_ROTATE bass_bfx_rotate = new BASS_FX.BASS_BFX_ROTATE();
                    BASS.BASS_FXGetParameters(this.effectRotate, bass_bfx_rotate);
                    bass_bfx_rotate.fRate = f;
                    BASS.BASS_FXSetParameters(this.effectRotate, bass_bfx_rotate);
                    return;
                }
                return;
            }
            int i2 = this.effectRotate;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.effectRotate = 0;
            }
        }
    }

    public void phrserSet(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.effectPhaser == 0) {
                    this.effectPhaser = BASS.BASS_ChannelSetFX(i, BASS_FX.BASS_FX_BFX_PHASER, 0);
                }
                if (this.effectPhaser != 0) {
                    BASS_FX.BASS_BFX_PHASER bass_bfx_phaser = new BASS_FX.BASS_BFX_PHASER();
                    BASS.BASS_FXGetParameters(this.effectPhaser, bass_bfx_phaser);
                    bass_bfx_phaser.fDryMix = fArr[0];
                    bass_bfx_phaser.fWetMix = fArr[1];
                    bass_bfx_phaser.fFeedback = fArr[2];
                    bass_bfx_phaser.fRate = fArr[3];
                    bass_bfx_phaser.fRange = fArr[4];
                    bass_bfx_phaser.fFreq = fArr[5];
                    BASS.BASS_FXSetParameters(this.effectPhaser, bass_bfx_phaser);
                    return;
                }
                return;
            }
            int i2 = this.effectPhaser;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.effectPhaser = 0;
            }
        }
    }

    public void compressorSet(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.compressorEffects == 0) {
                    this.compressorEffects = BASS.BASS_ChannelSetFX(i, 65553, 0);
                }
                if (this.effectPhaser != 0) {
                    BASS_FX.BASS_BFX_COMPRESSOR2 bass_bfx_compressor2 = new BASS_FX.BASS_BFX_COMPRESSOR2();
                    BASS.BASS_FXGetParameters(this.compressorEffects, bass_bfx_compressor2);
                    bass_bfx_compressor2.fGain = fArr[0];
                    bass_bfx_compressor2.fThreshold = fArr[1];
                    bass_bfx_compressor2.fRatio = fArr[2];
                    bass_bfx_compressor2.fAttack = fArr[3];
                    bass_bfx_compressor2.fRelease = fArr[4];
                    BASS.BASS_FXSetParameters(this.compressorEffects, bass_bfx_compressor2);
                    return;
                }
                return;
            }
            int i2 = this.compressorEffects;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.compressorEffects = 0;
            }
        }
    }

    public void setWahAuto(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.autoEffectFx == 0) {
                    this.autoEffectFx = BASS.BASS_ChannelSetFX(i, BASS_FX.BASS_FX_BFX_PHASER, 0);
                }
                if (this.autoEffectFx != 0) {
                    BASS_FX.BASS_BFX_PHASER bass_bfx_phaser = new BASS_FX.BASS_BFX_PHASER();
                    BASS.BASS_FXGetParameters(this.autoEffectFx, bass_bfx_phaser);
                    bass_bfx_phaser.fDryMix = fArr[0];
                    bass_bfx_phaser.fWetMix = fArr[1];
                    bass_bfx_phaser.fFeedback = fArr[2];
                    bass_bfx_phaser.fRate = fArr[3];
                    bass_bfx_phaser.fRange = fArr[4];
                    bass_bfx_phaser.fFreq = fArr[5];
                    BASS.BASS_FXSetParameters(this.autoEffectFx, bass_bfx_phaser);
                    return;
                }
                return;
            }
            int i2 = this.autoEffectFx;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.autoEffectFx = 0;
            }
        }
    }

    public void setEffectFlang(float[] fArr) {
        int i = this.channelPlay;
        if (i != 0) {
            if (fArr != null) {
                if (this.effectFlenger == 0) {
                    this.effectFlenger = BASS.BASS_ChannelSetFX(i, 4, 0);
                }
                if (this.effectFlenger != 0) {
                    try {
                        BASS.BASS_DX8_FLANGER bass_dx8_flanger = new BASS.BASS_DX8_FLANGER();
                        BASS.BASS_FXGetParameters(this.effectFlenger, bass_dx8_flanger);
                        bass_dx8_flanger.fWetDryMix = fArr[0];
                        bass_dx8_flanger.fDepth = fArr[1];
                        bass_dx8_flanger.fFeedback = fArr[2];
                        bass_dx8_flanger.fDelay = fArr[3];
                        bass_dx8_flanger.lPhase = (int) fArr[4];
                        if (fArr.length == 6) {
                            bass_dx8_flanger.fFrequency = fArr[5];
                        }
                        BASS.BASS_FXSetParameters(this.effectFlenger, bass_dx8_flanger);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
                return;
            }
            int i2 = this.effectFlenger;
            if (i2 != 0) {
                BASS.BASS_ChannelRemoveFX(i, i2);
                this.effectFlenger = 0;
            }
        }
    }

    public void audioPause() {
        if (!this.isPlaying) {
            new Exception("DBMediaPlayer pauseAudio:HanetMediaPlayer not init").printStackTrace();
            return;
        }
        int i = this.channelPlay;
        if (i != 0) {
            BASS.BASS_ChannelPause(i);
        }
    }

    public void audioRelease() {
        this.mHandler.removeMessages(0);
        int i = this.effectReverb;
        if (i != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i);
            this.effectReverb = 0;
        }
        int i2 = this.effectFlenger;
        if (i2 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i2);
            this.effectReverb = 0;
        }
        int i3 = this.effectEcho;
        if (i3 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i3);
            this.effectEcho = 0;
        }
        int i4 = this.bigQuedEffects;
        if (i4 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i4);
            this.bigQuedEffects = 0;
        }
        int i5 = this.amplifyFx;
        if (i5 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i5);
            this.amplifyFx = 0;
        }
        int i6 = this.effectDisort;
        if (i6 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i6);
            this.effectDisort = 0;
        }
        int i7 = this.chorosEffectFx;
        if (i7 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i7);
            this.chorosEffectFx = 0;
        }
        int i8 = this.effextEQ4;
        if (i8 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i8);
            this.effextEQ4 = 0;
        }
        int i9 = this.effextEQ1;
        if (i9 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i9);
            this.effextEQ1 = 0;
        }
        int i10 = this.effectEQ2;
        if (i10 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i10);
            this.effectEQ2 = 0;
        }
        int i11 = this.effectEQ3;
        if (i11 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i11);
            this.effectEQ3 = 0;
        }
        int i12 = this.effectRotate;
        if (i12 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i12);
            this.effectRotate = 0;
        }
        int i13 = this.effectPhaser;
        if (i13 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i13);
            this.effectPhaser = 0;
        }
        int i14 = this.autoEffectFx;
        if (i14 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i14);
            this.autoEffectFx = 0;
        }
        int i15 = this.compressorEffects;
        if (i15 != 0) {
            BASS.BASS_ChannelRemoveFX(this.channelPlay, i15);
            this.compressorEffects = 0;
        }
        this.isPlaying = false;
        BASS.BASS_StreamFree(this.channelPlay);
        BASS.BASS_StreamFree(this.tempChanel);
        ArrayList<Integer> arrayList = this.integerArrayList;
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<Integer> it = this.integerArrayList.iterator();
            while (it.hasNext()) {
                BASS.BASS_StreamFree(it.next().intValue());
            }
            this.integerArrayList.clear();
            this.integerArrayList = null;
        }
        this.tempChanel = 0;
        this.channelPlay = 0;
    }

    public void setOnDBMediaListener(DBMediaListener dBMediaListener) {
        this.mediaListener = dBMediaListener;
    }

    public int getIntDuration() {
        if (this.channelPlay != 0) {
            this.intDuration = getChanLength();
        }
        return this.intDuration;
    }

    public int getIntCurrPosition() {
        return this.intCurrPosition;
    }

    public void toSeek(int i) {
        if (!this.isPlaying) {
            new Exception("DBMediaPlayer seekTo:HanetMediaPlayer is not playing").printStackTrace();
            return;
        }
        this.intCurrPosition = i;
        seekToChannel(i);
    }

    private void mediaInit() {
        audioRelease();
        String str = this.strMediaPath;
        if (str != null || !str.equals("")) {
            if (!this.isMixNeed) {
                this.channelPlay = BASS.BASS_StreamCreateFile(this.strMediaPath, 0L, 0L, 2097152);
            } else {
                Log.d(logTag, "========>BASS_Error=" + BASS.BASS_ErrorGetCode());
                initMixToMedia(false);
            }
        }
        int i = this.channelPlay;
        if (i != 0) {
            if (!this.isMixNeed) {
                this.channelPlay = BASS_FX.BASS_FX_ReverseCreate(i, 2.0f, 2162688);
            }
            int i2 = this.channelPlay;
            if (i2 != 0) {
                BASS.BASS_ChannelGetInfo(i2, new BASS.BASS_CHANNELINFO());
                int BASS_FX_TempoCreate = BASS_FX.BASS_FX_TempoCreate(this.channelPlay, 65536);
                this.channelPlay = BASS_FX_TempoCreate;
                if (BASS_FX_TempoCreate == 0) {
                    Log.d(logTag, "========>BASS_Error=" + BASS.BASS_ErrorGetCode());
                    new Exception(logTag + " Couldnt create a resampled stream!").printStackTrace();
                    BASS.BASS_StreamFree(this.channelPlay);
                    return;
                }
                return;
            }
            Log.d(logTag, "========>BASS_Error=" + BASS.BASS_ErrorGetCode());
            new Exception(logTag + " Couldnt create a resampled stream!").printStackTrace();
            BASS.BASS_StreamFree(this.channelPlay);
            return;
        }
        Log.d(logTag, "========>BASS_Error=" + BASS.BASS_ErrorGetCode());
        new Exception(logTag + " Couldnt create a resampled stream!").printStackTrace();
        BASS.BASS_StreamFree(this.channelPlay);
    }

    private void initMixToMedia(boolean z) {
        File tempDirectory;
        String str;
        int BASS_Mixer_StreamCreate;
        int i;
        boolean BASS_Mixer_StreamAddChannelEx;
        if (!this.isMixNeed || (tempDirectory = getTempDirectory()) == null || !tempDirectory.isDirectory() || (str = this.strMixPath) == null || str.equals("")) {
            return;
        }
        File file = new File(tempDirectory, this.strMixPath);
        if (file.exists() && file.isFile() && (BASS_Mixer_StreamCreate = BASSmix.BASS_Mixer_StreamCreate(44100, 2, 2097152)) != 0) {
            int BASS_StreamCreateFile = BASS.BASS_StreamCreateFile(this.strMediaPath, 0L, 0L, 2097152);
            if (BASS_StreamCreateFile != 0) {
                boolean BASS_Mixer_StreamAddChannel = BASSmix.BASS_Mixer_StreamAddChannel(BASS_Mixer_StreamCreate, BASS_StreamCreateFile, 8388608);
                if (!BASS_Mixer_StreamAddChannel) {
                    BASS.BASS_StreamFree(BASS_Mixer_StreamCreate);
                    this.channelPlay = BASS_StreamCreateFile;
                    return;
                }
                this.channelPlay = BASS_Mixer_StreamCreate;
                this.tempChanel = BASS_StreamCreateFile;
                long BASS_ChannelSeconds2Bytes = BASS.BASS_ChannelSeconds2Bytes(BASS_Mixer_StreamCreate, 3.0d);
                long BASS_ChannelSeconds2Bytes2 = BASS.BASS_ChannelSeconds2Bytes(BASS_Mixer_StreamCreate, 1.0d);
                int chanLength = getChanLength();
                if (chanLength > 0) {
                    this.integerArrayList = new ArrayList<>();
                    long BASS_ChannelSeconds2Bytes3 = BASS.BASS_ChannelSeconds2Bytes(BASS_Mixer_StreamCreate, chanLength);
                    int i2 = 0;
                    while (i2 < chanLength) {
                        if (i2 % 3 == 0) {
                            long j = i2 * BASS_ChannelSeconds2Bytes2;
                            long j2 = j + BASS_ChannelSeconds2Bytes;
                            int BASS_StreamCreateFile2 = BASS.BASS_StreamCreateFile(file.getAbsolutePath(), 0L, 0L, 2097152);
                            if (BASS_StreamCreateFile2 != 0) {
                                if (j2 < BASS_ChannelSeconds2Bytes3) {
                                    i = i2;
                                    BASS_Mixer_StreamAddChannelEx = BASSmix.BASS_Mixer_StreamAddChannelEx(BASS_Mixer_StreamCreate, BASS_StreamCreateFile2, 8388608, j, BASS_ChannelSeconds2Bytes);
                                } else {
                                    i = i2;
                                    long j3 = BASS_ChannelSeconds2Bytes3 - j;
                                    BASS_Mixer_StreamAddChannelEx = j3 > 0 ? BASSmix.BASS_Mixer_StreamAddChannelEx(BASS_Mixer_StreamCreate, BASS_StreamCreateFile2, 8388608, j, j3) : BASS_Mixer_StreamAddChannel;
                                }
                                if (BASS_Mixer_StreamAddChannelEx) {
                                    BASS.BASS_StreamFree(BASS_StreamCreateFile2);
                                } else {
                                    this.integerArrayList.add(Integer.valueOf(BASS_StreamCreateFile2));
                                }
                                i2 = i + 1;
                            }
                        }
                        i = i2;
                        i2 = i + 1;
                    }
                    return;
                }
                return;
            }
            BASS.BASS_StreamFree(BASS_Mixer_StreamCreate);
        }
    }

    public boolean initSolveToMedia() {
        BASS.BASS_StreamFree(this.channelPlay);
        int BASS_StreamCreateFile = BASS.BASS_StreamCreateFile(this.strMediaPath, 0L, 0L, 2097152);
        this.channelPlay = BASS_StreamCreateFile;
        if (BASS_StreamCreateFile != 0) {
            if (!this.isMixNeed) {
                this.channelPlay = BASS_FX.BASS_FX_ReverseCreate(BASS_StreamCreateFile, 2.0f, 2097152);
            } else {
                initMixToMedia(true);
            }
            int i = this.channelPlay;
            if (i != 0) {
                int BASS_FX_TempoCreate = BASS_FX.BASS_FX_TempoCreate(i, 2097152);
                this.channelPlay = BASS_FX_TempoCreate;
                if (BASS_FX_TempoCreate == 0) {
                    new Exception(logTag + " Couldnt create a resampled stream!").printStackTrace();
                    BASS.BASS_StreamFree(this.channelPlay);
                    return false;
                }
                return true;
            }
            new Exception(logTag + " Couldnt create a resampled stream!").printStackTrace();
            BASS.BASS_StreamFree(this.channelPlay);
        }
        return false;
    }

    public void seekToChannel(int i) {
        int i2 = this.channelPlay;
        if (i2 != 0) {
            BASS.BASS_ChannelSetPosition(i2, BASS.BASS_ChannelSeconds2Bytes(i2, i), 0);
        }
    }

    public int getChannelPos() {
        double BASS_ChannelBytes2Seconds;
        int i = this.tempChanel;
        if (i != 0) {
            BASS_ChannelBytes2Seconds = BASS.BASS_ChannelBytes2Seconds(i, BASS.BASS_ChannelGetPosition(i, 0));
        } else {
            int i2 = this.channelPlay;
            if (i2 == 0) {
                return -1;
            }
            BASS_ChannelBytes2Seconds = BASS.BASS_ChannelBytes2Seconds(i2, BASS.BASS_ChannelGetPosition(i2, 0));
        }
        return (int) BASS_ChannelBytes2Seconds;
    }

    public int getChanLength() {
        double BASS_ChannelBytes2Seconds;
        int i = this.tempChanel;
        if (i != 0) {
            BASS_ChannelBytes2Seconds = BASS.BASS_ChannelBytes2Seconds(i, BASS.BASS_ChannelGetLength(i, 0));
        } else {
            int i2 = this.channelPlay;
            if (i2 == 0) {
                return 0;
            }
            BASS_ChannelBytes2Seconds = BASS.BASS_ChannelBytes2Seconds(i2, BASS.BASS_ChannelGetLength(i2, 0));
        }
        return (int) BASS_ChannelBytes2Seconds;
    }

    public void setThisReverse(boolean z) {
        this.isThisReverse = z;
        int i = this.channelPlay;
        if (i != 0) {
            int BASS_FX_TempoGetSource = BASS_FX.BASS_FX_TempoGetSource(i);
            BASS.FloatValue floatValue = new BASS.FloatValue();
            floatValue.value = 0.0f;
            BASS.BASS_ChannelGetAttribute(BASS_FX_TempoGetSource, BASS_FX.BASS_ATTRIB_REVERSE_DIR, floatValue);
            if (z) {
                BASS.BASS_ChannelSetAttribute(BASS_FX_TempoGetSource, BASS_FX.BASS_ATTRIB_REVERSE_DIR, -1.0f);
            } else {
                BASS.BASS_ChannelSetAttribute(BASS_FX_TempoGetSource, BASS_FX.BASS_ATTRIB_REVERSE_DIR, 1.0f);
            }
        }
    }

    public void saveAsFile(String str) {
        int i;
        int BASS_ChannelGetData;
        if (str == null || str.equals("") || (i = this.channelPlay) == 0 || BASSenc.BASS_Encode_Start(i, str, 262208, null, 0) == 0) {
            return;
        }
        try {
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(20000);
            do {
                BASS_ChannelGetData = BASS.BASS_ChannelGetData(this.channelPlay, allocateDirect, allocateDirect.capacity());
                if (BASS_ChannelGetData == -1) {
                    return;
                }
            } while (BASS_ChannelGetData != 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getTempDirectory() {
        try {
            if (Environment.getExternalStorageState().equals("mounted")) {
                File file = new File(Environment.getExternalStorageDirectory(), InterfaceVoiceChangerListener.recordedFolderName);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File file2 = new File(file, InterfaceVoiceChangerListener.folderTemp);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                return file2;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
