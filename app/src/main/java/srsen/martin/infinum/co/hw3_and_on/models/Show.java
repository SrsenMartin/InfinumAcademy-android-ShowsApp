package srsen.martin.infinum.co.hw3_and_on.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity
public class Show {

    @SerializedName("_id")
    @PrimaryKey
    @NonNull
    private String ID;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("imageUrl")
    @ColumnInfo(name = "imageUrl")
    private String imageUrl;

    @SerializedName("likesCount")
    @ColumnInfo(name = "likes")
    private int likes;

    @SerializedName("description")
    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "likeStatus")
    private Boolean liked;

    public Show(@NonNull String ID, String title, String imageUrl, int likes, String description) {
        this.ID = ID;
        this.title = title;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.description = description;
    }

    public String getID() {
        return ID;
    }

    public void setID(@NonNull String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }
}