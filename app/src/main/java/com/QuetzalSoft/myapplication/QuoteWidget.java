package com.QuetzalSoft.myapplication;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.QuetzalSoft.myapplication.ApiModels.QuotesInfoResponse;
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

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
/*
        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        ImageView imageView = appWidgetManager.getAppWidgetIds(widgetText)
        // Construct the RemoteViews object
//        ImageView imageView = view.findViewById(R.id.appwidget_text);

//        views.setTextViewText(R.id.appwidget_text, widgetText);

        Log.e("bitmap", "updateAppWidget: "+MainActivity.bitmap);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quote_widget);
        // Instruct the widget manager to update the widget
        Call<ShowQuotesResponse> call = ApiClicent.getInstance().getApi().show_quotes(user_data.uid);
        call.enqueue(new Callback<ShowQuotesResponse>() {
            @Override
            public void onResponse(Call<ShowQuotesResponse> call, Response<ShowQuotesResponse> response) {
//                Log.e("show_quotes", "onResponse: "+response.body().getIs_fav().get(0).getQuote_id());
                if (response.body() != null) {
                    if (response.body().isStatus()) {
                        ShowQuotesResponse response1 = response.body();
                        Log.i("appwidget_text", "onResponse: "+response.body().getResponse().get(0).getName());
                        views.setTextViewText(R.id.appwidget_text,response.body().getResponse().get(0).getName());

                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    } else {
                        Log.e("show_quotes", "onResponse2: " + response.body().getMessage());

                    }
                }
            }
            @Override
            public void onFailure(Call<ShowQuotesResponse> call, Throwable t) {

            }
        });*/

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
      /*  // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.e("show_quotes", "enabled: running");
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quote_widget);

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }*/
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