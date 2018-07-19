package srsen.martin.infinum.co.hw3_and_on;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Episode implements Parcelable {

    private String name;
    private String description;
    private int season;
    private int episode;
    private Uri imageUri;

    public Episode(String name, String description, int season, int episode, Uri imageUri){
        this.name = name;
        this.description = description;
        this.season = season;
        this.episode = episode;
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    public Episode(Parcel in){
        this.name = in.readString();
        this.description = in.readString();
        this.season = in.readInt();
        this.episode = in.readInt();
        this.imageUri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(season);
        dest.writeInt(episode);
        dest.writeParcelable((Parcelable) imageUri, flags);
    }
}
