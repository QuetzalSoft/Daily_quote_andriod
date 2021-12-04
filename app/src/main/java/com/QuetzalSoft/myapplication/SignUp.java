package com.QuetzalSoft.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.QuetzalSoft.myapplication.ApiModels.LoginResponse;
import com.QuetzalSoft.myapplication.ApiModels.SignupResponse;
import com.QuetzalSoft.myapplication.Utils.user_data;
import com.QuetzalSoft.myapplication.operationsRetrofitApi.ApiClicent;
import com.github.ybq.android.spinkit.SpinKitView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    TextView logintext, CreateNewAccountBtn2;
    EditText usernamedit, emailedit, passedit, numberedit, addressedit;
    SpinKitView spinKitView;
    ImageView eye;//passedit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        eye = findViewById(R.id.eye);
        passedit = findViewById(R.id.passedit);

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passedit.getTransformationMethod().getClass().getSimpleName().equals("PasswordTransformationMethod")) {
                    passedit.setTransformationMethod(new SingleLineTransformationMethod());
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24);
                    eye.setImageDrawable(drawable);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.invisible);
                    eye.setImageDrawable(drawable);
                    passedit.setTransformationMethod(new PasswordTransformationMethod());
                }
                passedit.setSelection(passedit.getText().length());
            }
        });


        spinKitView = findViewById(R.id.Register_spin_kit);
        logintext = findViewById(R.id.LoginText);
        usernamedit = findViewById(R.id.usernamedit);
        emailedit = findViewById(R.id.emailedit);
        numberedit = findViewById(R.id.numberedit);
        addressedit = findViewById(R.id.addressedit);
        CreateNewAccountBtn2 = findViewById(R.id.CreateNewAccountBtn2);

        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, Login.class));
                finish();
            }
        });

        CreateNewAccountBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(usernamedit.getText())) {
                    usernamedit.setError("Please Enter Username");
                } else if (TextUtils.isEmpty(emailedit.getText())) {
                    emailedit.setError("Please Enter Email");
                } else if (TextUtils.isEmpty(passedit.getText())) {
                    passedit.setError("Please Enter Password");
                } else if (passedit.length() < 8) {
                    passedit.setError("Password must be of 8 characters");
                } else {
                    user_data.show_dialog(SignUp.this);
                    Call<SignupResponse> call = ApiClicent.getInstance().getApi().register_user(emailedit.getText().toString().trim(),
                            passedit.getText().toString().trim(),
                            usernamedit.getText().toString().trim(),
                            "address",
                            "0"
                    );

                    call.enqueue(new Callback<SignupResponse>() {
                        @Override
                        public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    if (response.body().isStatus()) {

                                        SignupResponse.SignUpModel loginresponse = response.body().getResponse();
                                        user_data.uid = loginresponse.getId();
                                        user_data.email = loginresponse.getEmail();
                                        user_data.username = loginresponse.getUsername();
                                        user_data.address = loginresponse.getAddress();
                                        user_data.phone_number = loginresponse.getPhone_number();

                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("username", user_data.username);
                                        editor.putString("uid", loginresponse.getId() + "");
                                        editor.putString("address", loginresponse.getAddress() + "");
                                        editor.putString("phone_number", loginresponse.getPhone_number() + "");
                                        editor.putString("show_noti", "no");
                                        editor.apply();
                                        user_data.dismiss_dialog();
                                        Toast.makeText(SignUp.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUp.this, MainActivity.class));
                                        finish();

                                    } else if (!response.body().isStatus()) {
                                        user_data.dismiss_dialog();
                                        Toast.makeText(SignUp.this,
                                                response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<SignupResponse> call, Throwable t) {
                            Toast.makeText(SignUp.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            user_data.dismiss_dialog();

                        }
                    });
                }
            }
        });
    }
}