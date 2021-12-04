package com.QuetzalSoft.myapplication;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.QuetzalSoft.myapplication.ApiModels.QuotesInfoResponse;
import com.QuetzalSoft.myapplication.ApiModels.ShowQuotes;
import com.QuetzalSoft.myapplication.ApiModels.ShowQuotesResponse;
import com.QuetzalSoft.myapplication.MainActivity;
import com.QuetzalSoft.myapplication.R;
import com.QuetzalSoft.myapplication.Services.NotifyService;
import com.QuetzalSoft.myapplication.Utils.Constant;
import com.QuetzalSoft.myapplication.Utils.user_data;
import com.QuetzalSoft.myapplication.operationsRetrofitApi.ApiClicent;
import com.google.android.gms.common.internal.IAccountAccessor;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.QuetzalSoft.myapplication.Utils.Constant.audio;

/**
 * Implementation of App Widget functionality.
 */
public class QuoteWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.e("show_quotes", "enabled: running");
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quote_widget);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }


    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quote_widget);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        // Instruct the widget manager to update the widget
        Call<ShowQuotes> call = ApiClicent.getInstance().getApi().getQuotes();
        call.enqueue(new Callback<ShowQuotes>() {
            @Override
            public void onResponse(Call<ShowQuotes> call, Response<ShowQuotes> response) {
                Log.i("Widget  ", "" + response.body().getResponse().get(0).getName());
                Log.i("Widget  ", "" + response.body().getResponse().get(0).getAuthorName());
                if (response.body() != null) {
                    views.setTextViewText(R.id.appwidget_text,
                            response.body().getResponse().get(0).getName() + " - " +
                                    response.body().getResponse().get(0).getAuthorName());

                    appWidgetManager.updateAppWidget(appWidgetId, views);
                } else {
                    Log.e("show_quotes", "onResponse2: " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ShowQuotes> call, Throwable t) {

            }
        });
    }


    @Override
    public void onEnabled(Context context) {
        Log.e("show_quotes", "enabled: running2");
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}