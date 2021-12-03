package com.QuetzalSoft.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.QuetzalSoft.myapplication.Services.NotifyService;

public class Settings extends AppCompatActivity {
    ImageButton back;
    Switch enable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back = findViewById(R.id.back);
        enable = findViewById(R.id.switch1);

        //get data in prefs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String show_noti = prefs.getString("show_noti", "");

            if(NotifyService.show_noti==1){
                enable.setChecked(true);
            }
            else{
                enable.setChecked(false);
            }
        //set data in prefs
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        SharedPreferences.Editor editor = preferences.edit();
        enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    NotifyService.show_noti = 1;
                    editor.putString("show_noti","yes");
                    editor.apply();
                }
                else{
                    NotifyService.show_noti = 0;
                    editor.putString("show_noti","no");
                    editor.apply();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}