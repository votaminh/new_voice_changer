package com.msc.voice_chager.domain.layer;

import android.os.Parcel;
import android.os.Parcelable;

import com.msc.voice_chager.utils.ExternalSyntex;

import kotlin.jvm.internal.Intrinsics;


public final class RecordingModel implements Parcelable {
    public static final Parcelable.Creator<RecordingModel> CREATOR = new Creator();
    private int fav;
    private long length;
    private String name;
    private String path;
    private long timeAdded;

    @Override
    public int describeContents() {
        return 0;
    }


    public static final class Creator implements Parcelable.Creator<RecordingModel> {

        @Override
        public final RecordingModel createFromParcel(Parcel parcel) {
            Intrinsics.checkNotNullParameter(parcel, "parcel");
            return new RecordingModel(parcel.readString(), parcel.readString(), parcel.readLong(), parcel.readLong(), parcel.readInt());
        }


        @Override
        public final RecordingModel[] newArray(int i) {
            return new RecordingModel[i];
        }
    }

    public final RecordingModel copy(String name, String path, long j, long j2, int i) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(path, "path");
        return new RecordingModel(name, path, j, j2, i);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RecordingModel) {
            RecordingModel recordingModel = (RecordingModel) obj;
            return Intrinsics.areEqual(this.name, recordingModel.name) && Intrinsics.areEqual(this.path, recordingModel.path) && this.length == recordingModel.length && this.timeAdded == recordingModel.timeAdded && this.fav == recordingModel.fav;
        }
        return false;
    }

    public int hashCode() {
        return (((((((this.name.hashCode() * 31) + this.path.hashCode()) * 31) + ExternalSyntex.custSyntex(this.length)) * 31) + ExternalSyntex.custSyntex(this.timeAdded)) * 31) + this.fav;
    }

    public String toString() {
        return "RecordingModel(name=" + this.name + ", path=" + this.path + ", length=" + this.length + ", timeAdded=" + this.timeAdded + ", fav=" + this.fav + ')';
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        Intrinsics.checkNotNullParameter(out, "out");
        out.writeString(this.name);
        out.writeString(this.path);
        out.writeLong(this.length);
        out.writeLong(this.timeAdded);
        out.writeInt(this.fav);
    }

    public RecordingModel(String str, String str2, long j, long j2, int i) {
        this.name = str;
        this.path = str2;
        this.length = j;
        this.timeAdded = j2;
        this.fav = i;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.name = str;
    }

    public final String getPath() {
        return this.path;
    }

    public final void setPath(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.path = str;
    }

    public final long getLength() {
        return this.length;
    }

    public final void setLength(long j) {
        this.length = j;
    }
}
