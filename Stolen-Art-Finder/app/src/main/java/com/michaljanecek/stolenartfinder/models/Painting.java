package com.michaljanecek.stolenartfinder.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;

import kotlin.UByteArray;


public class Painting implements Parcelable {

    Bitmap painting;
    String name;

    public Painting(Bitmap painting, FoundPaintingModel f) {
        this.painting = painting;

        if(f==null)
            return;

        this.name = f.getName();
    }

    // constructor for creating Painting from a parcel
    public Painting(Parcel parcel){

        // creating byteArray, loading byteArray to it and then cvt to Bitmap
        int byteArrayLen = parcel.readInt();
        byte [] byteArrayImage = new byte[byteArrayLen];
        parcel.readByteArray(byteArrayImage);
        painting = byteArrayToBitmap(byteArrayImage);

        name = parcel.readString();

    }

    //used when un-parceling our parcel (creating the object)
    public static final Parcelable.Creator<Painting> CREATOR = new Parcelable.Creator<Painting>(){

        @Override
        public Painting createFromParcel(Parcel parcel) {
            return new Painting(parcel);
        }

        @Override
        public Painting[] newArray(int size) {
            return new Painting[0];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }


    public Bitmap getPainting() {
        return painting;
    }

    public String getName() {

        if(name.equals("")){
            return "Unknown name";
        }

        return name;
    }


    //write object values to parcel for storage
    public void writeToParcel(Parcel dest, int flags){

        byte [] imageByteArray = bitmapToByteArray(painting);

        dest.writeInt(imageByteArray.length);
        dest.writeByteArray(imageByteArray);
        dest.writeString(name);

    }


    private Bitmap byteArrayToBitmap(byte[] image){

        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        return bmp;
    }

    private byte[] bitmapToByteArray (Bitmap image){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;

    }

}
