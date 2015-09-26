package com.blackboxstudios.rafael.rsspodcastplayer.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rafael on 24/07/2015.
 */
public class PodcastData implements Parcelable{
    private String title;
    private String id;
    private String image;
    private String description;
    private String url;
    private String subtitle;

    public PodcastData(String title, String id, String image, String description, String url, String subtitle) {
        super();
        this.title = title;
        this.id = id;
        this.image = image;
        this.description = description;
        this.url = url;
        this.subtitle = subtitle;
    }

    public PodcastData(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(id);
        dest.writeString(image);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(subtitle);
    }

    public static final Creator<PodcastData> CREATOR
            = new Creator<PodcastData>() {
        public PodcastData createFromParcel(Parcel in) {
            return new PodcastData(in);
        }

        public PodcastData[] newArray(int size) {
            return new PodcastData[size];
        }
    };

    public PodcastData(Parcel in) {

        ReadFromParcel(in);
    }

    private void ReadFromParcel(Parcel in) {
        title = in.readString();
        id = in.readString();
        image = in.readString();
        description = in.readString();
        url = in.readString();
        subtitle = in.readString();
    }
}
