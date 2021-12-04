package com.QuetzalSoft.myapplication;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.QuetzalSoft.myapplication.ApiModels.LoginResponse;
import com.QuetzalSoft.myapplication.Utils.user_data;
import com.QuetzalSoft.myapplication.operationsRetrofitApi.ApiClicent;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    TextView loginbtn, ForgetPasswordText, CreateNewAccountBtn;
    EditText username, password, Password;
    ImageView eye;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        password = findViewById(R.id.Password);
        eye = findViewById(R.id.eye);


        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().getClass().getSimpleName().equals("PasswordTransformationMethod")) {
                    password.setTransformationMethod(new SingleLineTransformationMethod());
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_remove_red_eye_24);
                    eye.setImageDrawable(drawable);
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.invisible);
                    eye.setImageDrawable(drawable);
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }

                password.setSelection(password.getText().length());
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
        String name = preferences.getString("username", "empty");
        String email = preferences.getString("email", "empty");
        String address = preferences.getString("address", "empty");
        String phone = preferences.getString("phone_number", "empty");
        String uid = preferences.getString("uid", "empty");

        if (!name.equalsIgnoreCase("empty")) {
            user_data.uid = Integer.parseInt(uid);
            user_data.email = email;
            user_data.username = name;
            user_data.address = address;
            user_data.phone_number = phone;

            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        CreateNewAccountBtn = findViewById(R.id.CreateNewAccountBtn);
        loginbtn = findViewById(R.id.loginbtn);
        username = findViewById(R.id.userName1);
        ForgetPasswordText = findViewById(R.id.ForgetPasswordText);

        ForgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgetPassword.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText())) {
                    username.setError("Please Enter Username");
                } else if (TextUtils.isEmpty(password.getText())) {
                    password.setError("Please Enter Password");
                } else {
                    user_data.show_dialog(Login.this);
                    Call<LoginResponse> call = ApiClicent.getInstance()
                            .getApi().performLogin(username.getText().toString().trim()
                                    , password.getText().toString().trim());

                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            Log.e("response", "onResponse: " + response);
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    LoginResponse.loginresponse loginresponse = response.body().getResponse();
                                    user_data.uid = loginresponse.getId();
                                    user_data.email = loginresponse.getEmail();
                                    user_data.username = loginresponse.getUsername();
                                    user_data.address = loginresponse.getAddress();
                                    user_data.phone_number = loginresponse.getPhone_number();

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("username", username.getText().toString().trim());
                                    editor.putString("password", password.getText().toString().trim());
                                    editor.putString("uid", loginresponse.getId() + "");
                                    editor.putString("address", loginresponse.getAddress() + "");
                                    editor.putString("phone_number", loginresponse.getPhone_number() + "");
                                    editor.putString("show_noti", "no");
                                    editor.apply();
                                    Toast.makeText(Login.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    user_data.dismiss_dialog();
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    user_data.dismiss_dialog();
                                }
                            } else {
                                user_data.dismiss_dialog();
                                Toast.makeText(Login.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Log.e("failresponse", "onFailure: " + t.getMessage());
                            Toast.makeText(Login.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            user_data.dismiss_dialog();


                        }
                    });
                }
            }
        });
        CreateNewAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignUp.class));
                finish();
            }
        });
    }
}