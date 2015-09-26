package com.blackboxstudios.rafael.rsspodcastplayer.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Rafael on 24/07/2015.
 */
public class MyPodcastList extends ArrayList<PodcastData> implements Parcelable {

    public MyPodcastList(){

    }

    public MyPodcastList(Parcel in){
        this.clear();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.clear();

        // First we have to read the list size
        int size = in.readInt();

        for (int i = 0; i < size; i++) {
            PodcastData podcastData = new PodcastData(in.readString(), in.readString(), in.readString(), in.readString(), in.readString(), in.readString());
            this.add(podcastData);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public final Creator<MyPodcastList> CREATOR = new Creator<MyPodcastList>() {
        public MyPodcastList createFromParcel(Parcel in) {
            return new MyPodcastList(in);
        }

        public MyPodcastList[] newArray(int size) {
            return new MyPodcastList[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();

        // We have to write the list size, we need him recreating the list
        dest.writeInt(size);

        for (int i = 0; i < size; i++) {
            PodcastData podcastData = this.get(i);

            dest.writeString(podcastData.getTitle());
            dest.writeString(podcastData.getId());
            dest.writeString(podcastData.getImage());
            dest.writeString(podcastData.getDescription());
            dest.writeString(podcastData.getUrl());
            dest.writeString(podcastData.getSubtitle());
        }
    }
}
