package srsen.martin.infinum.co.hw3_and_on.models;

import com.google.gson.annotations.SerializedName;

public class Data<T> {

    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }

    public void setTokenData(T data) {
        this.data = data;
    }
}
