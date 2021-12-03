package com.QuetzalSoft.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.QuetzalSoft.myapplication.ApiModels.ForgetPasswordResponse;
import com.QuetzalSoft.myapplication.Utils.user_data;
import com.QuetzalSoft.myapplication.operationsRetrofitApi.ApiClicent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPassword extends AppCompatActivity {

    TextView back;
    EditText forgotpassmail;
    Button forgotbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back = findViewById(R.id.back);
        forgotpassmail = findViewById(R.id.forgotpassmail);
        forgotbtn  = findViewById(R.id.ForgetBtn);

        forgotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(forgotpassmail.getText())) {
                forgotpassmail.setError("Please Enter Email");
                }
                else{
                    user_data.show_dialog(ForgetPassword.this);
                    Call<ForgetPasswordResponse> call = ApiClicent.getInstance().getApi().forgetpassword(forgotpassmail.getText().toString().trim());
                    call.enqueue(new Callback<ForgetPasswordResponse>() {
                        @Override
                        public void onResponse(Call<ForgetPasswordResponse> call, Response<ForgetPasswordResponse> response) {
                            if (response.body()!=null){
                                if(response.body().isStatus()){
                                    Toast.makeText(ForgetPassword.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    user_data.dismiss_dialog();
                                }
                                else{
                                    Toast.makeText(ForgetPassword.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    user_data.dismiss_dialog();

                                }
                            }
                            else{
                                user_data.dismiss_dialog();
                                Toast.makeText(ForgetPassword.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                            user_data.dismiss_dialog();
                            Toast.makeText(ForgetPassword.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPassword.this, Login.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForgetPassword.this, Login.class));
        finish();
    }
}