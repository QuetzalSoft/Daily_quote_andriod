package com.QuetzalSoft.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.QuetzalSoft.myapplication.ApiModels.AllFavsResponse;
import com.QuetzalSoft.myapplication.ApiModels.AllStreakResponse;
import com.QuetzalSoft.myapplication.ApiModels.FavResponse;
import com.QuetzalSoft.myapplication.ApiModels.ShowQuotes;
import com.QuetzalSoft.myapplication.ApiModels.ShowQuotesResponse;
import com.QuetzalSoft.myapplication.ApiModels.QuotesInfoResponse;
import com.QuetzalSoft.myapplication.ApiModels.StreakCountResponse;
import com.QuetzalSoft.myapplication.ApiModels.response;
import com.QuetzalSoft.myapplication.Broadcasts.MyNotification;
import com.QuetzalSoft.myapplication.Services.NotifyService;
import com.QuetzalSoft.myapplication.Utils.Constant;
import com.QuetzalSoft.myapplication.Utils.user_data;
import com.QuetzalSoft.myapplication.operationsRetrofitApi.ApiClicent;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.QuetzalSoft.myapplication.Utils.Constant.audio;
import static com.QuetzalSoft.myapplication.Utils.Constant.streak_count;

@SuppressLint("CheckResult")
public class MainActivity extends AppCompatActivity {

    int currentposition;
    int tag = 0;
    int play = 0;
    int duration;
    int quote_id;
    int streak;
    public static Uri uri;
    List<response> is_fav;
    ProgressBar pb;
    ImageButton addtofav, menu, playbtn, share;
    TextView badge, all_streak_badge;
    TextView toolbar_title, play_lesson_time_start, play_lesson_time_end, quote, author;
    MediaPlayer mediaPlayer;
    ImageView quotebg1;
    SeekBar seekbar;
    private ArrayList<ShowQuotes> showQuotesArrayList;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showQuotesArrayList = new ArrayList<>();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (isServiceRunning()) {
        } else {
            startService(new Intent(MainActivity.this, NotifyService.class));
        }

        //View
        pb = findViewById(R.id.pb);
        addtofav = findViewById(R.id.addtofav);
        //all_streak_badge = findViewById(R.id.all_streak_badge);
        toolbar_title = findViewById(R.id.toolbar_title);
        play_lesson_time_start = findViewById(R.id.play_lesson_time_start);
        play_lesson_time_end = findViewById(R.id.play_lesson_time_end);
        playbtn = findViewById(R.id.play);
        share = findViewById(R.id.share);
        menu = findViewById(R.id.menu);
        quotebg1 = findViewById(R.id.quotebg);
        quote = findViewById(R.id.quote);
        author = findViewById(R.id.author);
        //badge = findViewById(R.id.badge);
        seekbar = findViewById(R.id.seekbar);
        pb.setVisibility(View.VISIBLE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = preferences.edit();

        String show_noti = preferences.getString("show_noti", "");

        if (show_noti != null) {
            if (show_noti.equals("yes")) {
                NotifyService.show_noti = 1;
            } else {
                NotifyService.show_noti = 0;
            }
        }
        Log.e("user_data", "onCreate: " + user_data.uid + " , " + user_data.username);

        //Show Quote
        //For Image Uploading
        Call<ShowQuotes> showQuotesCall = ApiClicent.getInstance().getApi().getQuotes();
        showQuotesCall.enqueue(new Callback<ShowQuotes>() {
            @Override
            public void onResponse(Call<ShowQuotes> call, Response<ShowQuotes> response) {
                Log.i("MyChecking ", "" + response.isSuccessful());
                if (response.isSuccessful()) {
                    showQuotesArrayList.add(response.body());
                    Glide.with(getApplication())
                            .load(Constant.images + response.body().getResponse().get(0).getImage())
                            .into(quotebg1);

                    uri = Uri.parse(audio + response.body().getResponse().get(0).getAudioLesson());
                    Log.i("uri", "onResponse: " + uri);
                    mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
                    duration = mediaPlayer.getDuration();
                    seekbar.setMax(duration);

                    currentposition = mediaPlayer.getCurrentPosition();
                    play_lesson_time_start.setText(convertFormat(currentposition));
                    play_lesson_time_end.setText(convertFormat(duration));
                }
            }

            @Override
            public void onFailure(Call<ShowQuotes> call, Throwable t) {
                Log.i("onFailure ", " ShowQuotes " + t.getMessage());
            }
        });

        //End

        //Update Widget
        Intent intent = new Intent(this, QuoteWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), QuoteWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

     /*   Call<ShowQuotesResponse> call = ApiClicent.getInstance().getApi().show_quotes(user_data.uid);
        call.enqueue(new Callback<ShowQuotesResponse>() {
            @Override
            public void onResponse(Call<ShowQuotesResponse> call, Response<ShowQuotesResponse> response) {
//                Log.e("show_quotes", "onResponse: "+response.body().getIs_fav().get(0).getQuote_id());
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        ShowQuotesResponse response1 = response.body();
                        pb.setVisibility(View.GONE);
                        setNotificationAlarm(MainActivity.this);
                        quote_id = response1.getResponse().get(0).getId();
                        quote.setText(response1.getResponse().get(0).getName());
                        int color = Color.parseColor("#" + response1.getResponse().get(0).getColor_code());
                        quote.setTextColor(color);
                        seekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        seekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        author.setText("~" + response1.getResponse().get(0).getAuthor_name());
                       *//* Picasso.get().load(Constant.images + response1.getResponse().get(0).getImage())
                                .into(quotebg, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        BitmapDrawable drawable = (BitmapDrawable) quotebg.getDrawable();
                                        bitmap = drawable.getBitmap();
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });*//*

                        uri = Uri.parse(audio + response1.getResponse().get(0).getAudio_lesson());
                        Log.e("uri", "onResponse: " + uri);
                        Log.e("show_quote", "onResponse: " + quote_id);

                        mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
                        duration = mediaPlayer.getDuration();
                        seekbar.setMax(duration);
                        int currentposition = mediaPlayer.getCurrentPosition();
                        play_lesson_time_start.setText(convertFormat(currentposition));
                        play_lesson_time_end.setText(convertFormat(duration));

                        //check quote streak
                        Call<QuotesInfoResponse> call3 = ApiClicent.getInstance().getApi().quote_info(user_data.uid, quote_id);
                        call3.enqueue(new Callback<QuotesInfoResponse>() {
                            @Override
                            public void onResponse(Call<QuotesInfoResponse> call, Response<QuotesInfoResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().isStatus()) {

                                        streak = Integer.parseInt(response.body().getResponse().getStreak_count());
                                        if (streak >= 3) {
                                            Log.e("streak", "onResponse: " + streak);
                                            badge.setText(streak + "");
                                            badge.setVisibility(View.VISIBLE);
                                        } else {
                                            badge.setText(streak + "");
                                        }
                                        //getting all streak
                                        Call<AllStreakResponse> call4 = ApiClicent.getInstance().getApi().show_all_streak(quote_id, user_data.uid);
                                        call4.enqueue(new Callback<AllStreakResponse>() {
                                            @Override
                                            public void onResponse(Call<AllStreakResponse> call, Response<AllStreakResponse> response) {
                                                Log.e("all_streak", "onResponse: " + response.body().getResponse().size());
                                                if (response.body() != null) {
                                                    for (int i = 0; i < response.body().getResponse().size(); i++) {
                                                        all_streak_badge.setText(response.body().getResponse().get(i).getStreak_count());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<AllStreakResponse> call, Throwable t) {

                                            }
                                        });

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<QuotesInfoResponse> call, Throwable t) {
                                Log.e("strak", "onFailure: " + t.getMessage());
                            }
                        });
//                        editor.putString("q_id",response1.getResponse().get(0).getId()+"");
//                        editor.putString("q_name",response1.getResponse().get(0).getName());
//                        editor.putString("q_audio",audio + response1.getResponse().get(0).getAudio_lesson());
//                        editor.putString("q_author","~" + response1.getResponse().get(0).getAuthor_name());
//                        editor.putString("q_image",Constant.images + response1.getResponse().get(0).getImage());
//                        editor.putString("q_streak",streak+"");
//                        editor.putString("q_color_code",response1.getResponse().get(0).getColor_code());
//                        editor.putString("q_call","false");
//                        editor.apply();

                    } else {
                        Log.e("show_quotes", "onResponse1: " + response.body().getMessage());
                    }
                } else {
//                    Log.e("show_quotes", "onResponse2: "+response.body().getMessage() );
                    pb.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ShowQuotesResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);

            }
        });
*/


        //calling show quotes api
//        if(!time.equalsIgnoreCase("") && !date.equalsIgnoreCase(""))
//        {
//            if(date.equals(currentDate)){
//                if(preferences.getString("q_call","").equalsIgnoreCase("true")){
//                    Log.e("call_true", "onCreate: " );
//
//                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
//                try {
//                    Date currenttime = parser.parse(currentTime);
//                    Date calltime = parser.parse("1:00:00");
//                    if(currenttime.after(calltime)) {
//                              Log.e("time_before", "onCreate: "+currentTime );
//
//
////                        Call<ShowQuotesResponse> show_quotescall = ApiClicent.getInstance().getApi().show_quotes(user_data.uid);
////                        show_quotescall.enqueue(new Callback<ShowQuotesResponse>() {
////                            @Override
////                            public void onResponse(Call<ShowQuotesResponse> call, Response<ShowQuotesResponse> response) {
//////                Log.e("show_quotes", "onResponse: "+response.body().getIs_fav().get(0).getQuote_id());
////                                if (response.body()!=null) {
////                                    if (response.body().isStatus()) {
////                                        ShowQuotesResponse response1 = response.body();
////                                        pb.setVisibility(View.GONE);
////                                        setNotificationAlarm(MainActivity.this);
////                                        quote_id = response1.getResponse().get(0).getId();
////                                        quote.setText(response1.getResponse().get(0).getName() );
////                                        author.setText("~" + response1.getResponse().get(0).getAuthor_name());
////                                       int color =  Color.parseColor("#"+response1.getResponse().get(0).getColor_code());
////                                            quote.setTextColor(color);
////                                        seekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
////                                        seekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
////                                        Picasso.get().load(Constant.images + response1.getResponse().get(0).getImage())
////                                                .into(quotebg, new com.squareup.picasso.Callback() {
////                                                    @Override
////                                                    public void onSuccess() {
////                                                        BitmapDrawable drawable = (BitmapDrawable) quotebg.getDrawable();
////                                                        bitmap = drawable.getBitmap();
////                                                    }
////
////                                                    @Override
////                                                    public void onError(Exception e) {
////
////                                                    }
////                                                });
////
////                                        uri = Uri.parse(audio + response1.getResponse().get(0).getAudio_lesson());
////                                        Log.e("uri", "onResponse: "+uri );
////                                        Log.e("show_quote", "onResponse: "+quote_id );
////
////
////                                        mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
////                                        duration = mediaPlayer.getDuration();
////                                        int currentposition = mediaPlayer.getCurrentPosition();
////                                        play_lesson_time_start.setText(convertFormat(currentposition));
////                                        play_lesson_time_end.setText(convertFormat(duration));
////
////                                        //check quote streak
////                                        Call<QuotesInfoResponse> call3 = ApiClicent.getInstance().getApi().quote_info(user_data.uid, quote_id);
////                                        call3.enqueue(new Callback<QuotesInfoResponse>() {
////                                            @Override
////                                            public void onResponse(Call<QuotesInfoResponse> call, Response<QuotesInfoResponse> response) {
////                                                if (response.body() != null) {
////                                                    if (response.body().isStatus()) {
////
////                                                        streak = Integer.parseInt(response.body().getResponse().getStreak_count());
////                                                        if (streak >= 3) {
////                                                            Log.e("streak", "onResponse: "+streak );
////                                                            badge.setText(streak+"");
////                                                            badge.setVisibility(View.VISIBLE);
////                                                        } else {
////                                                            badge.setText(streak+"");
////                                                        }
////                                                        //getting all streak
////                                                        Call<AllStreakResponse> call4 = ApiClicent.getInstance().getApi().show_all_streak(quote_id,user_data.uid);
////                                                        call4.enqueue(new Callback<AllStreakResponse>() {
////                                                            @Override
////                                                            public void onResponse(Call<AllStreakResponse> call, Response<AllStreakResponse> response) {
////                                                                Log.e("all_streak", "onResponse: "+response.body().getResponse().size());
////                                                                if(response.body()!=null){
////                                                                    for (int i = 0; i<response.body().getResponse().size();i++) {
////                                                                        all_streak_badge.setText(response.body().getResponse().get(i).getStreak_count());
////                                                                    }
////                                                                }
////                                                            }
////
////                                                            @Override
////                                                            public void onFailure(Call<AllStreakResponse> call, Throwable t) {
////
////                                                            }
////                                                        });
////
////                                                    }
////                                                }
////                                            }
////
////                                            @Override
////                                            public void onFailure(Call<QuotesInfoResponse> call, Throwable t) {
////                                                Log.e("strak", "onFailure: "+t.getMessage());
////                                            }
////                                        });
////
////                                        editor.putString("q_id",response1.getResponse().get(0).getId()+"");
////                                        editor.putString("q_name",response1.getResponse().get(0).getName());
////                                        editor.putString("q_audio",audio + response1.getResponse().get(0).getAudio_lesson());
////                                        editor.putString("q_author","~" + response1.getResponse().get(0).getAuthor_name());
////                                        editor.putString("q_image",Constant.images + response1.getResponse().get(0).getImage());
////                                        editor.putString("q_streak",streak+"");
////                                        editor.putString("q_color_code",response1.getResponse().get(0).getColor_code());
////                                        editor.putString("q_call","false");
////                                        editor.apply();
////                                    }
////                                    else{
////                                        Log.e("show_quotes", "onResponse1: "+response.body().getMessage() );
////                                    }
////                                }
////                                else{
//////                    Log.e("show_quotes", "onResponse2: "+response.body().getMessage() );
////                                    pb.setVisibility(View.GONE);
////
////                                }
////                            }
////
////                            @Override
////                            public void onFailure(Call<ShowQuotesResponse> call, Throwable t) {
////                                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
////                                pb.setVisibility(View.GONE);
////
////                            }
////                        });
////                        editor.putString("q_call", "false");
////                        editor.putString("date", currentDate);
////                        editor.putString("time", "1:00:00");
////                        editor.apply();
//
//                    }
//                    else {
//                        Log.e("else_pref", "onCreate: " );
//                       String q_id = preferences.getString("q_id","");
//                       if(!q_id.equalsIgnoreCase("")){
//                           quote_id = Integer.parseInt(preferences.getString("q_id",""));
//                           quote.setText(preferences.getString("q_name",""));
//                           int color =  Color.parseColor("#"+preferences.getString("q_color_code",""));
//                           quote.setTextColor(color);
//                           seekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                           seekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                           author.setText(preferences.getString("q_author",""));
//                           Picasso.get().load(preferences.getString("q_image",""))
//                                   .into(quotebg);
//
//                           uri = Uri.parse(preferences.getString("q_audio",""));
//                           badge.setText(preferences.getString("q_streak",""));
//                           Log.e("uri", "onResponse: "+uri );
//                           Log.e("show_quote", "onResponse: "+quote_id );
//
//
//                           mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
//                           duration = mediaPlayer.getDuration();
//                           int currentposition = mediaPlayer.getCurrentPosition();
//                           play_lesson_time_start.setText(convertFormat(currentposition));
//                           play_lesson_time_end.setText(convertFormat(duration));
//                           pb.setVisibility(View.GONE);
//                       }
//                    }
//
//
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                }
//                else {
//                    Log.e("else_pref2", "onCreate: " );
//                    String q_id = preferences.getString("q_id","");
//                    if(!q_id.equalsIgnoreCase("")){
//                        quote_id = Integer.parseInt(preferences.getString("q_id",""));
//                        quote.setText(preferences.getString("q_name",""));
//                        int color = Color.parseColor("#"+preferences.getString("q_color_code",""));
//                        seekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                        seekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                        quote.setTextColor(color);
//                        author.setText(preferences.getString("q_author",""));
//                        Picasso.get().load(preferences.getString("q_image",""))
//                                .into(quotebg);
//
//                        uri = Uri.parse(preferences.getString("q_audio",""));
//                        badge.setText(preferences.getString("q_streak",""));
//                        Log.e("uri", " "+uri );
//                        Log.e("show_quote", " "+quote_id );
//
//
//                        mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
//                        duration = mediaPlayer.getDuration();
//                        int currentposition = mediaPlayer.getCurrentPosition();
//                        play_lesson_time_start.setText(convertFormat(currentposition));
//                        play_lesson_time_end.setText(convertFormat(duration));
//                        editor.putString("date",currentDate);
//                        editor.apply();
//                        pb.setVisibility(View.GONE);
//
//                    }
//                }
//            }
//            else {
//                if(preferences.getString("q_call","").equalsIgnoreCase("true")){
//                    Log.e("call_true", "onCreate: " );
//
//                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
//                    try {
//                        Date currenttime = parser.parse(currentTime);
//                        Date calltime = parser.parse("1:00:00");
//                        if(currenttime.after(calltime)) {
//                            Log.e("time_before", "onCreate: "+currentTime );
//
//
//
//                            Call<ShowQuotesResponse> call = ApiClicent.getInstance().getApi().show_quotes(user_data.uid);
//                            call.enqueue(new Callback<ShowQuotesResponse>() {
//                                @Override
//                                public void onResponse(Call<ShowQuotesResponse> call, Response<ShowQuotesResponse> response) {
////                Log.e("show_quotes", "onResponse: "+response.body().getIs_fav().get(0).getQuote_id());
//                                    if (response.body()!=null) {
//                                        if (response.body().isStatus()) {
//                                            ShowQuotesResponse response1 = response.body();
//                                            pb.setVisibility(View.GONE);
//                                            setNotificationAlarm(MainActivity.this);
//                                            quote_id = response1.getResponse().get(0).getId();
//                                            quote.setText(response1.getResponse().get(0).getName());
//                                            int color = Color.parseColor("#"+response1.getResponse().get(0).getColor_code());
//                                            quote.setTextColor(color);
//                                            seekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                                            seekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                                            author.setText("~" + response1.getResponse().get(0).getAuthor_name());
//                                            Picasso.get().load(Constant.images + response1.getResponse().get(0).getImage())
//                                                    .into(quotebg, new com.squareup.picasso.Callback() {
//                                                        @Override
//                                                        public void onSuccess() {
//                                                            BitmapDrawable drawable = (BitmapDrawable) quotebg.getDrawable();
//                                                            bitmap = drawable.getBitmap();
//                                                        }
//
//                                                        @Override
//                                                        public void onError(Exception e) {
//
//                                                        }
//                                                    });
//
//                                            uri = Uri.parse(audio + response1.getResponse().get(0).getAudio_lesson());
//                                            Log.e("uri", "onResponse: "+uri );
//                                            Log.e("show_quote", "onResponse: "+quote_id );
//
//
//                                            mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
//                                            duration = mediaPlayer.getDuration();
//                                            int currentposition = mediaPlayer.getCurrentPosition();
//                                            play_lesson_time_start.setText(convertFormat(currentposition));
//                                            play_lesson_time_end.setText(convertFormat(duration));
//
//                                            //check quote streak
//                                            Call<QuotesInfoResponse> call3 = ApiClicent.getInstance().getApi().quote_info(user_data.uid, quote_id);
//                                            call3.enqueue(new Callback<QuotesInfoResponse>() {
//                                                @Override
//                                                public void onResponse(Call<QuotesInfoResponse> call, Response<QuotesInfoResponse> response) {
//                                                    if (response.body() != null) {
//                                                        if (response.body().isStatus()) {
//
//                                                            streak = Integer.parseInt(response.body().getResponse().getStreak_count());
//                                                            if (streak >= 3) {
//                                                                Log.e("streak", "onResponse: "+streak );
//                                                                badge.setText(streak+"");
//                                                                badge.setVisibility(View.VISIBLE);
//                                                            } else {
//                                                                badge.setText(streak+"");
//                                                            }
//                                                            //getting all streak
//                                                            Call<AllStreakResponse> call4 = ApiClicent.getInstance().getApi().show_all_streak(quote_id,user_data.uid);
//                                                            call4.enqueue(new Callback<AllStreakResponse>() {
//                                                                @Override
//                                                                public void onResponse(Call<AllStreakResponse> call, Response<AllStreakResponse> response) {
//                                                                    Log.e("all_streak", "onResponse: "+response.body().getResponse().size());
//                                                                    if(response.body()!=null){
//                                                                        for (int i = 0; i<response.body().getResponse().size();i++) {
//                                                                            all_streak_badge.setText(response.body().getResponse().get(i).getStreak_count());
//                                                                        }
//                                                                    }
//                                                                }
//
//                                                                @Override
//                                                                public void onFailure(Call<AllStreakResponse> call, Throwable t) {
//
//                                                                }
//                                                            });
//
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onFailure(Call<QuotesInfoResponse> call, Throwable t) {
//                                                    Log.e("strak", "onFailure: "+t.getMessage());
//                                                }
//                                            });
//                                            editor.putString("q_id",response1.getResponse().get(0).getId()+"");
//                                            editor.putString("q_name",response1.getResponse().get(0).getName());
//                                            editor.putString("q_audio",audio + response1.getResponse().get(0).getAudio_lesson());
//                                            editor.putString("q_author","~" + response1.getResponse().get(0).getAuthor_name());
//                                            editor.putString("q_image",Constant.images + response1.getResponse().get(0).getImage());
//                                            editor.putString("q_streak",streak+"");
//                                            editor.putString("q_color_code",response1.getResponse().get(0).getColor_code());
//                                            editor.putString("q_call","false");
//                                            editor.putString("date",currentDate);
//                                            editor.apply();
//                                        }
//                                        else{
//                                            Log.e("show_quotes", "onResponse1: "+response.body().getMessage() );
//                                        }
//                                    }
//                                    else{
////                    Log.e("show_quotes", "onResponse2: "+response.body().getMessage() );
//                                        pb.setVisibility(View.GONE);
//
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<ShowQuotesResponse> call, Throwable t) {
//                                    Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    pb.setVisibility(View.GONE);
//
//                                }
//                            });
////                            editor.putString("q_call", "false");
////                            editor.putString("date", currentDate);
////                            editor.putString("time", "1:00:00");
////                            editor.apply();
//
//                        }
//                        else {
//                            Log.e("else_pref", "onCreate: " );
//                            String q_id = preferences.getString("q_id","");
//                            if(!q_id.equalsIgnoreCase("")){
//                                quote_id = Integer.parseInt(preferences.getString("q_id",""));
//                                quote.setText(preferences.getString("q_name",""));
//                                int color = Color.parseColor("#"+preferences.getString("q_color_code",""));
//                                quote.setTextColor(color);
//                                seekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                                seekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                                author.setText(preferences.getString("q_author",""));
//                                Picasso.get().load(preferences.getString("q_image",""))
//                                        .into(quotebg);
//
//                                uri = Uri.parse(preferences.getString("q_audio",""));
//                                badge.setText(preferences.getString("q_streak",""));
//                                Log.e("uri", "onResponse: "+uri );
//                                Log.e("show_quote", "onResponse: "+quote_id );
//
//
//                                mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
//                                duration = mediaPlayer.getDuration();
//                                int currentposition = mediaPlayer.getCurrentPosition();
//                                play_lesson_time_start.setText(convertFormat(currentposition));
//                                play_lesson_time_end.setText(convertFormat(duration));
//
//                            }
//                        }
//
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
//                    Log.e("else_pref2", "onCreate: " );
//                    Call<ShowQuotesResponse> call = ApiClicent.getInstance().getApi().show_quotes(user_data.uid);
//                    call.enqueue(new Callback<ShowQuotesResponse>() {
//                        @Override
//                        public void onResponse(Call<ShowQuotesResponse> call, Response<ShowQuotesResponse> response) {
////                Log.e("show_quotes", "onResponse: "+response.body().getIs_fav().get(0).getQuote_id());
//                            if (response.body()!=null) {
//                                if (response.body().isStatus()) {
//                                    ShowQuotesResponse response1 = response.body();
//                                    pb.setVisibility(View.GONE);
//                                    setNotificationAlarm(MainActivity.this);
//                                    quote_id = response1.getResponse().get(0).getId();
//                                    quote.setText(response1.getResponse().get(0).getName());
//                                    int color = Color.parseColor("#"+response1.getResponse().get(0).getColor_code());
//                                    quote.setTextColor(color);
//                                    seekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                                    seekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                                    author.setText("~" + response1.getResponse().get(0).getAuthor_name());
//                                    Picasso.get().load(Constant.images + response1.getResponse().get(0).getImage())
//                                            .into(quotebg, new com.squareup.picasso.Callback() {
//                                                @Override
//                                                public void onSuccess() {
//                                                    BitmapDrawable drawable = (BitmapDrawable) quotebg.getDrawable();
//                                                    bitmap = drawable.getBitmap();
//                                                }
//
//                                                @Override
//                                                public void onError(Exception e) {
//
//                                                }
//                                            });
//
//                                    uri = Uri.parse(audio + response1.getResponse().get(0).getAudio_lesson());
//                                    Log.e("uri", "onResponse: "+uri );
//                                    Log.e("show_quote", "onResponse: "+quote_id );
//
//
//                                    mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
//                                    duration = mediaPlayer.getDuration();
//                                    int currentposition = mediaPlayer.getCurrentPosition();
//                                    play_lesson_time_start.setText(convertFormat(currentposition));
//                                    play_lesson_time_end.setText(convertFormat(duration));
//
//                                    //check quote streak
//                                    Call<QuotesInfoResponse> call3 = ApiClicent.getInstance().getApi().quote_info(user_data.uid, quote_id);
//                                    call3.enqueue(new Callback<QuotesInfoResponse>() {
//                                        @Override
//                                        public void onResponse(Call<QuotesInfoResponse> call, Response<QuotesInfoResponse> response) {
//                                            if (response.body() != null) {
//                                                if (response.body().isStatus()) {
//
//                                                    streak = Integer.parseInt(response.body().getResponse().getStreak_count());
//                                                    if (streak >= 3) {
//                                                        Log.e("streak", "onResponse: "+streak );
//                                                        badge.setText(streak+"");
//                                                        badge.setVisibility(View.VISIBLE);
//                                                    } else {
//                                                        badge.setText(streak+"");
//                                                    }
//                                                    //getting all streak
//                                                    Call<AllStreakResponse> call4 = ApiClicent.getInstance().getApi().show_all_streak(quote_id,user_data.uid);
//                                                    call4.enqueue(new Callback<AllStreakResponse>() {
//                                                        @Override
//                                                        public void onResponse(Call<AllStreakResponse> call, Response<AllStreakResponse> response) {
//                                                            Log.e("all_streak", "onResponse: "+response.body().getResponse().size());
//                                                            if(response.body()!=null){
//                                                                for (int i = 0; i<response.body().getResponse().size();i++) {
//                                                                    all_streak_badge.setText(response.body().getResponse().get(i).getStreak_count());
//                                                                }
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onFailure(Call<AllStreakResponse> call, Throwable t) {
//
//                                                        }
//                                                    });
//
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(Call<QuotesInfoResponse> call, Throwable t) {
//                                            Log.e("strak", "onFailure: "+t.getMessage());
//                                        }
//                                    });
//                                    editor.putString("q_id",response1.getResponse().get(0).getId()+"");
//                                    editor.putString("q_name",response1.getResponse().get(0).getName());
//                                    editor.putString("q_audio",audio + response1.getResponse().get(0).getAudio_lesson());
//                                    editor.putString("q_author","~" + response1.getResponse().get(0).getAuthor_name());
//                                    editor.putString("q_image",Constant.images + response1.getResponse().get(0).getImage());
//                                    editor.putString("q_streak",streak+"");
//                                    editor.putString("q_color_code",response1.getResponse().get(0).getColor_code());
//                                    editor.putString("q_call","false");
//                                    editor.putString("date",currentDate);
//                                    editor.apply();
//                                }
//                                else{
//                                    Log.e("show_quotes", "onResponse1: "+response.body().getMessage() );
//                                }
//                            }
//                            else{
////                    Log.e("show_quotes", "onResponse2: "+response.body().getMessage() );
//                                pb.setVisibility(View.GONE);
//
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ShowQuotesResponse> call, Throwable t) {
//                            Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//                            pb.setVisibility(View.GONE);
//
//                        }
//                    });
//                }
//            }
//        }
//        else{
//            editor.putString("q_call","false");
//            editor.putString("date",currentDate);
//            editor.putString("time",currentTime);
//            editor.apply();
//            Call<ShowQuotesResponse> call = ApiClicent.getInstance().getApi().show_quotes(user_data.uid);
//            call.enqueue(new Callback<ShowQuotesResponse>() {
//                @Override
//                public void onResponse(Call<ShowQuotesResponse> call, Response<ShowQuotesResponse> response) {
////                Log.e("show_quotes", "onResponse: "+response.body().getIs_fav().get(0).getQuote_id());
//                    if (response.body()!=null) {
//                        if (response.body().isStatus()) {
//                            ShowQuotesResponse response1 = response.body();
//                            pb.setVisibility(View.GONE);
//                            setNotificationAlarm(MainActivity.this);
//                            quote_id = response1.getResponse().get(0).getId();
//                            quote.setText(response1.getResponse().get(0).getName());
//                            int color = Color.parseColor("#"+response1.getResponse().get(0).getColor_code());
//                            quote.setTextColor(color);
//                            seekbar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                            seekbar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
//                            author.setText("~" + response1.getResponse().get(0).getAuthor_name());
//                            Picasso.get().load(Constant.images + response1.getResponse().get(0).getImage())
//                                    .into(quotebg, new com.squareup.picasso.Callback() {
//                                        @Override
//                                        public void onSuccess() {
//                                            BitmapDrawable drawable = (BitmapDrawable) quotebg.getDrawable();
//                                            bitmap = drawable.getBitmap();
//                                        }
//
//                                        @Override
//                                        public void onError(Exception e) {
//
//                                        }
//                                    });
//
//                            uri = Uri.parse(audio + response1.getResponse().get(0).getAudio_lesson());
//                            Log.e("uri", "onResponse: "+uri );
//                            Log.e("show_quote", "onResponse: "+quote_id );
//
//                            mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
//                            duration = mediaPlayer.getDuration();
//                            seekbar.setMax(duration);
//                            int currentposition = mediaPlayer.getCurrentPosition();
//                            play_lesson_time_start.setText(convertFormat(currentposition));
//                            play_lesson_time_end.setText(convertFormat(duration));
//
//                            //check quote streak
//                            Call<QuotesInfoResponse> call3 = ApiClicent.getInstance().getApi().quote_info(user_data.uid, quote_id);
//                            call3.enqueue(new Callback<QuotesInfoResponse>() {
//                                @Override
//                                public void onResponse(Call<QuotesInfoResponse> call, Response<QuotesInfoResponse> response) {
//                                    if (response.body() != null) {
//                                        if (response.body().isStatus()) {
//
//                                            streak = Integer.parseInt(response.body().getResponse().getStreak_count());
//                                            if (streak >= 3) {
//                                                Log.e("streak", "onResponse: "+streak );
//                                                badge.setText(streak+"");
//                                                badge.setVisibility(View.VISIBLE);
//                                            } else {
//                                                badge.setText(streak+"");
//                                            }
//                                            //getting all streak
//                                            Call<AllStreakResponse> call4 = ApiClicent.getInstance().getApi().show_all_streak(quote_id,user_data.uid);
//                                            call4.enqueue(new Callback<AllStreakResponse>() {
//                                                @Override
//                                                public void onResponse(Call<AllStreakResponse> call, Response<AllStreakResponse> response) {
//                                                    Log.e("all_streak", "onResponse: "+response.body().getResponse().size());
//                                                    if(response.body()!=null){
//                                                        for (int i = 0; i<response.body().getResponse().size();i++) {
//                                                            all_streak_badge.setText(response.body().getResponse().get(i).getStreak_count());
//                                                        }
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onFailure(Call<AllStreakResponse> call, Throwable t) {
//
//                                                }
//                                            });
//
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<QuotesInfoResponse> call, Throwable t) {
//                                    Log.e("strak", "onFailure: "+t.getMessage());
//                                }
//                            });
//                            editor.putString("q_id",response1.getResponse().get(0).getId()+"");
//                            editor.putString("q_name",response1.getResponse().get(0).getName());
//                            editor.putString("q_audio",audio + response1.getResponse().get(0).getAudio_lesson());
//                            editor.putString("q_author","~" + response1.getResponse().get(0).getAuthor_name());
//                            editor.putString("q_image",Constant.images + response1.getResponse().get(0).getImage());
//                            editor.putString("q_streak",streak+"");
//                            editor.putString("q_color_code",response1.getResponse().get(0).getColor_code());
//                            editor.putString("q_call","false");
//                            editor.apply();
//
//                        }
//                        else{
//                            Log.e("show_quotes", "onResponse1: "+response.body().getMessage() );
//                        }
//                    }
//                    else{
////                    Log.e("show_quotes", "onResponse2: "+response.body().getMessage() );
//                        pb.setVisibility(View.GONE);
//
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ShowQuotesResponse> call, Throwable t) {
//                    Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//                    pb.setVisibility(View.GONE);
//
//                }
//            });
//        }

        final Handler handler = new Handler();
        final int delay = 100; // 1000 milliseconds == 1 second
        Runnable runnable = new Runnable() {
            public void run() {
                int currentposition = mediaPlayer.getCurrentPosition();
                Log.i("mediaPlayerChecking ", "" + currentposition);
                handler.postDelayed(this, delay);
                Log.i("currentpos", "run: " + currentposition);
                Log.i("duration", "run: " + duration);
                seekbar.setMax(duration);
                seekbar.setProgress(currentposition);
                if (mediaPlayer.isPlaying() && duration != currentposition) {
                    play_lesson_time_start.setText(convertFormat(currentposition));
                    play_lesson_time_end.setText(convertFormat(duration));
                } else if (!mediaPlayer.isPlaying()) {
                    if (duration <= currentposition) {
                       /* Call<StreakCountResponse> call4 = ApiClicent.getInstance().getApi()
                                .streak_count(user_data.uid, quote_id, 1);
                        call4.enqueue(new Callback<StreakCountResponse>() {
                            @Override
                            public void onResponse(Call<StreakCountResponse> call, Response<StreakCountResponse> response) {
                                if (response.body().isStatus()) {
//                                    Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    Call<QuotesInfoResponse> call3 = ApiClicent.getInstance().getApi().quote_info(user_data.uid, quote_id);
                                    call3.enqueue(new Callback<QuotesInfoResponse>() {
                                        @Override
                                        public void onResponse(Call<QuotesInfoResponse> call, Response<QuotesInfoResponse> response) {
                                            if (response.body() != null) {
                                                if (response.body().isStatus()) {

                                                    streak = Integer.parseInt(response.body().getResponse().getStreak_count());
                                                    if (streak >= 3) {
                                                        Log.e("streak", "onResponse: " + streak);
                                                        badge.setText(streak + "");
                                                        badge.setVisibility(View.VISIBLE);
                                                    } else {
                                                        badge.setText(streak + "");
                                                    }
                                                    //getting all streak
                                                    Call<AllStreakResponse> call4 = ApiClicent.getInstance().getApi().show_all_streak(quote_id, user_data.uid);
                                                    call4.enqueue(new Callback<AllStreakResponse>() {
                                                        @Override
                                                        public void onResponse(Call<AllStreakResponse> call, Response<AllStreakResponse> response) {
                                                            Log.e("all_streak", "onResponse: " + response.body().getResponse().size());
                                                            if (response.body() != null) {
                                                                for (int i = 0; i < response.body().getResponse().size(); i++) {
                                                                    all_streak_badge.setText(response.body().getResponse().get(i).getStreak_count());
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<AllStreakResponse> call, Throwable t) {

                                                        }
                                                    });

                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<QuotesInfoResponse> call, Throwable t) {
                                            Log.e("strak", "onFailure: " + t.getMessage());
                                        }
                                    });


                                }
                            }

                            @Override
                            public void onFailure(Call<StreakCountResponse> call, Throwable t) {

                            }
                        });*/
//                                Log.e("Streak", "run: +"+streak);
                        playbtn.setImageResource(R.drawable.ic_play);
                        play = 0;
                    }
                }
                if (play == 0) {
                    handler.removeCallbacks(this);
                }
            }
        };

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                String shareBody = showQuotesArrayList.get(0).getResponse().get(0).getName() + " - "
                        + showQuotesArrayList.get(0).getResponse().get(0).getAuthorName();
                /*The type of the content is text, obviously.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                /*Fire!*/
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("PlayChecking ", "" + play);
                if (play == 0) {
                    play = 1;
                    //handler playing here on button click
                    handler.postDelayed(runnable, delay);
                    //handler end
                    playbtn.setImageResource(R.drawable.ic_pause);
                    int currentposition = mediaPlayer.getCurrentPosition();
                    play_lesson_time_start.setText(convertFormat(currentposition));
                    play_lesson_time_end.setText(convertFormat(duration));
                    mediaPlayer.start();

                } else {
                    play = 0;
                    playbtn.setImageResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_Setting) {
                            startActivity(new Intent(MainActivity.this, Settings.class));
                        } else if (item.getItemId() == R.id.menu_fav) {
                            startActivity(new Intent(MainActivity.this, Favourites.class));
                        } else if (item.getItemId() == R.id.menu_logout) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("username");
                            editor.remove("email");
                            editor.remove("address");
                            editor.remove("phone_number");
                            editor.remove("uid");
                            editor.apply();
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            handler.removeCallbacks(runnable);
                            startActivity(new Intent(MainActivity.this, Login.class));
                            finish();
                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        addtofav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                if (tag == 0) {
                    Log.i("Checking ", " User_data id " + user_data.uid);
                    Log.i("Checking ", " quote_id " + quote_id);
                    Log.i("Checking ", " quote_id " + showQuotesArrayList.get(0).getResponse().get(0).getId());
                    Call<FavResponse> call = ApiClicent
                            .getInstance()
                            .getApi()
                            .add_fav(user_data.uid,
                                    showQuotesArrayList.get(0).getResponse().get(0).getId(),
                                    "true");
                    call.enqueue(new Callback<FavResponse>() {
                        @Override
                        public void onResponse(Call<FavResponse> call, Response<FavResponse> response) {
                            if (response.body() != null) {

                                //Log.i("Checking ", "" + response.body().isStatus());
                                if (response.body().isStatus()) {
                                    pb.setVisibility(View.GONE);
                                    tag = 1;
                                    addtofav.setImageResource(R.drawable.ic_favorite);
                                    Toast.makeText(MainActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<FavResponse> call, Throwable t) {
                            Log.e("addfav", "onFailure: " + t.getMessage());
                            Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);

                        }
                    });
                } else {
                    pb.setVisibility(View.VISIBLE);
                    Log.i("3Checking ", "" + showQuotesArrayList.get(0).getResponse().get(0).getId());
                    Call<FavResponse> call = ApiClicent.getInstance().getApi().remove_fav(user_data.uid, showQuotesArrayList.get(0).getResponse().get(0).getId());
                    call.enqueue(new Callback<FavResponse>() {
                        @Override
                        public void onResponse(Call<FavResponse> call, Response<FavResponse> response) {
                            if (response.body() != null) {
                                if (response.body().isStatus()) {
                                    tag = 0;
                                    addtofav.setImageResource(R.drawable.ic_add_to_fav);
                                    Toast.makeText(MainActivity.this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(View.GONE);

                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<FavResponse> call, Throwable t) {
                            Log.e("addfav", "onFailure: " + t.getMessage());
                            Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);

                        }
                    });
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == duration) {
                    seekBar.setProgress(0);
                    play_lesson_time_start.setText(convertFormat(currentposition));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Start
        tag = 0;
        addtofav.setImageResource(R.drawable.ic_add_to_fav);
        Observable<AllFavsResponse> observable = ApiClicent.getInstance().getApi().all_fav_new(user_data.uid);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AllFavsResponse>() {
                    @Override
                    public void accept(AllFavsResponse response) throws Exception {
                        is_fav = response.getResponse();
                        if (!is_fav.isEmpty()) {
                            for (int i = 0; i < is_fav.size(); i++) {
                                Log.i("MyChecking ", "  " + i);
                                String value1 = (is_fav.get(i).getQuote_id());
                                Log.i("MyChecking ", " " + showQuotesArrayList);
                                int data = showQuotesArrayList.get(i).getResponse().get(i).getId();
                                String value2 = String.valueOf(data);
                                if (value1.equals(value2)) {
                                    tag = 1;
                                    addtofav.setImageResource(R.drawable.ic_favorite);
                                    break;
                                }
                            }
                        } else {
                            Log.i("1Checking ", " " + is_fav.isEmpty());
                        }

                        Log.i("Mobile ", " " + quote_id);
                        Log.i("Mobile ", " " + user_data.uid);
                        Call<QuotesInfoResponse> call3 = ApiClicent.getInstance()
                                .getApi().quote_info(user_data.uid, quote_id);
                        call3.enqueue(new Callback<QuotesInfoResponse>() {
                            @Override
                            public void onResponse(Call<QuotesInfoResponse> call, Response<QuotesInfoResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().isStatus()) {
                                        streak = Integer.parseInt(response.body().getResponse().getStreak_count());
                                        Log.i("Mobile ", " " + streak);

                                        if (streak >= 3) {
                                            Log.e("streak", "onResponse: " + streak);
                                            //badge.setText(streak + "");
                                            //badge.setVisibility(View.VISIBLE);
                                        } else {
                                            //badge.setText(streak + "");
                                        }
                                        //getting all streak
                                        Call<AllStreakResponse> call4 = ApiClicent.getInstance().getApi().show_all_streak(quote_id, user_data.uid);
                                        call4.enqueue(new Callback<AllStreakResponse>() {
                                            @Override
                                            public void onResponse(Call<AllStreakResponse> call, Response<AllStreakResponse> response) {
                                                Log.e("all_streak", "onResponse: " + response.body().getResponse().size());
                                                if (response.body() != null) {
                                                    for (int i = 0; i < response.body().getResponse().size(); i++) {
                                                        all_streak_badge.setText(response.body().getResponse().get(i).getStreak_count());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<AllStreakResponse> call, Throwable t) {

                                            }
                                        });

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<QuotesInfoResponse> call, Throwable t) {
                                Log.e("strak", "onFailure: " + t.getMessage());
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("onFailure ", "" + throwable.getMessage());
                    }
                });
    }

    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }

    private void setNotificationAlarm(Context context) {
        Intent intent = new Intent();
        intent.setAction("ok");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, pendingIntent);
        Log.e("ME", "Alarm started");
    }

    boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotifyService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}