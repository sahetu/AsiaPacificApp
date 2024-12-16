package asiapacific.app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCountryData {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("CountryData")
    @Expose
    public List<GetCountryResponse> countryData;

    public class GetCountryResponse {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("shortname")
        @Expose
        public String shortname;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("phonecode")
        @Expose
        public String phonecode;
    }
}
