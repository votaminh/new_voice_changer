package com.msc.voice_chager.utils;


public final class ExternalSyntex {
    public static int custSyntex(long j) {
        return (int) (j ^ (j >>> 32));
    }
}
