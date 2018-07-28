package srsen.martin.infinum.co.hw3_and_on.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

@Entity
public class Episode implements Parcelable {

    @SerializedName("_id")
    @PrimaryKey
    @NonNull
    private String id;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("description")
    @ColumnInfo(name = "description")
    private String description;

    @SerializedName("season")
    @ColumnInfo(name = "season")
    private String season;

    @SerializedName("episodeNumber")
    @ColumnInfo(name = "episode")
    private String episode;

    @SerializedName("imageUrl")
    @ColumnInfo(name = "imageUrl")
    private String imageUri;

    @SerializedName("showId")
    @ColumnInfo(name = "showId")
    private String showId;

    public Episode(String id, String title, String description, String season, String episode, String imageUri, String showId){
        this.title = title;
        this.description = description;
        this.season = season;
        this.episode = episode;
        this.imageUri = imageUri;
        this.showId = showId;

        if(id != null){
            this.id = id;
        }else{
            this.id = generateId();
        }
    }

    private String generateId(){
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSeason() {
        return season;
    }

    public String getEpisode() {
        return episode;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
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
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.season = in.readString();
        this.episode = in.readString();
        this.imageUri = in.readString();
        this.showId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(season);
        dest.writeString(episode);
        dest.writeString(imageUri);
        dest.writeString(showId);
    }
}
