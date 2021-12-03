package com.QuetzalSoft.myapplication.operationsRetrofitApi;


import com.QuetzalSoft.myapplication.ApiModels.AllFavsResponse;
import com.QuetzalSoft.myapplication.ApiModels.AllStreakResponse;
import com.QuetzalSoft.myapplication.ApiModels.FavResponse;
import com.QuetzalSoft.myapplication.ApiModels.ForgetPasswordResponse;
import com.QuetzalSoft.myapplication.ApiModels.LoginResponse;
import com.QuetzalSoft.myapplication.ApiModels.QuotesInfoResponse;
import com.QuetzalSoft.myapplication.ApiModels.ShowQuotes;
import com.QuetzalSoft.myapplication.ApiModels.ShowQuotesResponse;
import com.QuetzalSoft.myapplication.ApiModels.SignupResponse;
import com.QuetzalSoft.myapplication.ApiModels.StreakCountResponse;
import com.QuetzalSoft.myapplication.Utils.Constant;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    //////////////////////Start Email_Login///////////////

    //    @FormUrlEncoded // annotation used in POST type requests
    @FormUrlEncoded
    @POST(Constant.login_user)
    Call<LoginResponse> performLogin(
            @Field("email") String user_email,
            @Field("password") String user_password
    );

    ///////////////////End Email_login////////////////////
    @FormUrlEncoded
    @POST(Constant.register_user)
    Call<SignupResponse> register_user(
            @Field("email")
                    String user_email,
            @Field("password")
                    String user_password,
            @Field("username")
                    String username,
            @Field("address")
                    String user_address,
            @Field("phone_number")
                    String user_phone_number
    );

    @FormUrlEncoded
    @POST(Constant.forget_password)
    Call<ForgetPasswordResponse> forgetpassword(
            @Field("email")
                    String user_email
    );


    @FormUrlEncoded
    @POST(Constant.add_to_favourites)
    Call<FavResponse> add_fav(
            @Field("user_id") int user_id,
            @Field("quote_id") int quote_id,
            @Field("is_fav") String is_fav
    );

    @FormUrlEncoded
    @POST(Constant.remove_to_favourites)
    Call<FavResponse> remove_fav(
            @Field("user_id") int user_id,
            @Field("quote_id") int quote_id);


    @FormUrlEncoded
    @POST(Constant.all_favourites)
    Call<AllFavsResponse> all_fav(
            @Field("user_id") int user_id);

    //Call<AllFavsResponse>
    @FormUrlEncoded
    @POST(Constant.all_favourites)
    Observable<AllFavsResponse> all_fav_new(
            @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST(Constant.streak_count)
    Call<StreakCountResponse> streak_count(
            @Field("user_id") int user_id,
            @Field("quote_id") int quote_id,
            @Field("streak_count") int streak_count
    );

    @FormUrlEncoded
    @POST(Constant.show_all_streak)
    Call<AllStreakResponse> show_all_streak(
            @Field("quote_id") int quote_id,
            @Field("user_id") int user_id
    );

    @FormUrlEncoded
    @POST(Constant.quote_info)
    Call<QuotesInfoResponse> quote_info(
            @Field("user_id") int user_id,
            @Field("quote_id") int quote_id
    );


    @FormUrlEncoded
    @POST(Constant.show_quotes)
    Call<ShowQuotesResponse> show_quotes(
            @Field("user_id")
                    int user_id
    );

//    @FormUrlEncoded
//    @POST(Constants.getAllCarts)
//    Call<CartResponse> getCart(
//            @Field("user_id") int userid
//    ) ;


    @GET(Constant.show_quotes)
    Call<ShowQuotes> getQuotes();
}
