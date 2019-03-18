package srsen.martin.infinum.co.hw3_and_on.models;

import com.google.gson.annotations.SerializedName;

public class MediaResponse {

    @SerializedName("_id")
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
