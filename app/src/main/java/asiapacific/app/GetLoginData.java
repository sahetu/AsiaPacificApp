package asiapacific.app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLoginData {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("UserDetails")
    @Expose
    public UserDetails userDetails;

    public class UserDetails {
        @SerializedName("userid")
        @Expose
        public String userid;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("contact")
        @Expose
        public String contact;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("created_date")
        @Expose
        public String createdDate;
    }
}
