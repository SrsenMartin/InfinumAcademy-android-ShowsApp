package srsen.martin.infinum.co.hw3_and_on.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity
public class Comment {

    @SerializedName("_id")
    @PrimaryKey
    @NonNull
    private String ID;

    @SerializedName("text")
    @ColumnInfo(name = "text")
    private String text;

    @SerializedName("userEmail")
    @ColumnInfo(name = "email")
    private String email;

    @SerializedName("episodeId")
    @ColumnInfo(name = "episodeId")
    private String episodeId;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }
}
