package asiapacific.app;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("signup.php")
    Call<GetSignupData> getSignupData(
            @Field("name") String name,
            @Field("email") String email,
            @Field("contact") String contact,
            @Field("password") String password,
            @Field("gender") String gender
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<GetLoginData> getLoginData(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("updateProfile.php")
    Call<GetSignupData> getUpdateProfileData(
            @Field("userid") String userid,
            @Field("name") String name,
            @Field("email") String email,
            @Field("contact") String contact,
            @Field("password") String password,
            @Field("gender") String gender
    );

    @FormUrlEncoded
    @POST("deleteProfile.php")
    Call<GetSignupData> getDeleteProfileData(
            @Field("userid") String userid
    );

}
