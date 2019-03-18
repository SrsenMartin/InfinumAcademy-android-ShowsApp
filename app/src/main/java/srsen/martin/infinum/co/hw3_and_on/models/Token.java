package srsen.martin.infinum.co.hw3_and_on.models;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("token")
    private String token;

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}
