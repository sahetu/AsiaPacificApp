package asiapacific.app;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

    @Multipart
    @POST("updateProfileImage.php")
    Call<UpdateProfileImageData> updateProfileImageData(
            @Part("userid") RequestBody userId,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("contact") RequestBody contact,
            @Part("password") RequestBody dob,
            @Part("gender") RequestBody gender,
            @Part MultipartBody.Part image);

}
