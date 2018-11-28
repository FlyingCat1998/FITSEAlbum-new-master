package com.example.a8560p.fitsealbum;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class AlbumFolder implements Parcelable {

    private String name;
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();

    public AlbumFile GetNewestFile(){
        return mAlbumFiles.get(0);
    }

    public AlbumFolder() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<AlbumFile> getAlbumFiles() {
        return mAlbumFiles;
    }

    public void addAlbumFile(AlbumFile albumFile) {
        mAlbumFiles.add(albumFile);
    }

    protected AlbumFolder(Parcel in) {
        name = in.readString();
        mAlbumFiles = in.createTypedArrayList(AlbumFile.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(mAlbumFiles);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlbumFolder> CREATOR = new Creator<AlbumFolder>() {
        @Override
        public AlbumFolder createFromParcel(Parcel in) {
            return new AlbumFolder(in);
        }

        @Override
        public AlbumFolder[] newArray(int size) {
            return new AlbumFolder[size];
        }
    };
}