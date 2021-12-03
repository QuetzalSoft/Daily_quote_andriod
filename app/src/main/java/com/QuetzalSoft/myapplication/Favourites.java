package com.QuetzalSoft.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.QuetzalSoft.myapplication.Adapter.FavsAdapter;
import com.QuetzalSoft.myapplication.ApiModels.AllFavsResponse;
import com.QuetzalSoft.myapplication.ApiModels.response;
import com.QuetzalSoft.myapplication.Utils.user_data;
import com.QuetzalSoft.myapplication.operationsRetrofitApi.ApiClicent;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favourites extends AppCompatActivity {

    RecyclerView favs_recycler;
    List<response> list;
    ImageButton back;
    ProgressDialog pd;
    TextView empty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.show();
        back = findViewById(R.id.back);
        favs_recycler = findViewById(R.id.favs_recycler);
        favs_recycler.setLayoutManager(new LinearLayoutManager(this));
        empty = findViewById(R.id.empty);

        Call<AllFavsResponse> call = ApiClicent.getInstance().getApi().all_fav(user_data.uid);
        call.enqueue(new Callback<AllFavsResponse>() {
            @Override
            public void onResponse(Call<AllFavsResponse> call, Response<AllFavsResponse> response) {
                list = response.body().getResponse();
                if(list.isEmpty()){
                    empty.setVisibility(View.VISIBLE);
                }
                Log.e("all favs", "onResponse: "+response.body().getResponse());
                Log.e("all favs", "onResponse: "+response.body().getMessage());
                FavsAdapter adapter = new FavsAdapter(Favourites.this,list,pd,empty);
                favs_recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<AllFavsResponse> call, Throwable t) {
                Log.e("allfav", "onFailure: "+t.getMessage() );
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