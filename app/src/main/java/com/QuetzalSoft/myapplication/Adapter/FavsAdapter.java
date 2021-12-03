package com.QuetzalSoft.myapplication.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.QuetzalSoft.myapplication.ApiModels.AllFavsResponse;
import com.QuetzalSoft.myapplication.ApiModels.FavResponse;
import com.QuetzalSoft.myapplication.ApiModels.IsFav;
import com.QuetzalSoft.myapplication.ApiModels.QuotesInfoResponse;
import com.QuetzalSoft.myapplication.ApiModels.StreakCountResponse;
import com.QuetzalSoft.myapplication.ApiModels.response;
import com.QuetzalSoft.myapplication.MainActivity;
import com.QuetzalSoft.myapplication.R;
import com.QuetzalSoft.myapplication.Utils.Constant;
import com.QuetzalSoft.myapplication.Utils.user_data;
import com.QuetzalSoft.myapplication.operationsRetrofitApi.ApiClicent;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.QuetzalSoft.myapplication.Utils.Constant.audio;

public class FavsAdapter extends RecyclerView.Adapter<FavsAdapter.ViewHolder> {

    TextView empty;
    Context context;
    List<response> list;
    MediaPlayer mediaPlayer;
    ProgressDialog pb;

    public FavsAdapter(Context context, List<response> list, ProgressDialog pb, TextView empty) {
        this.context = context;
        this.list = list;
        this.pb = pb;
        this.empty = empty;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favs_recycler_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Handler handler = new Handler();
        final int delay = 400; // 1000 milliseconds == 1 second
        final int[] tag = {0};
        final int[] play = {0};
        final int[] streak = {0};
        tag[0] = 1;
        int duration;
        String quote_id;
        quote_id = list.get(position).getQuote_id();
        holder.quote.setText(list.get(position).getName());
        holder.author.setText("~" + list.get(position).getAuthor_name());

         holder.addtofav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.show();
                if(tag[0] == 0) {
                    Call<FavResponse> call = ApiClicent.getInstance().getApi().add_fav(user_data.uid,Integer.parseInt(quote_id),"true");
                    call.enqueue(new Callback<FavResponse>() {
                        @Override
                        public void onResponse(Call<FavResponse> call, Response<FavResponse> response) {
                            if(response.body()!=null){
                                if(response.body().isStatus()){
                                    pb.dismiss();
                                    tag[0] = 1;
                                    holder.addtofav.setImageResource(R.drawable.ic_favorite);
                                    Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<FavResponse> call, Throwable t) {
                            Log.e("addfav", "onFailure: "+t.getMessage() );
                            Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            pb.dismiss();

                        }
                    });
                }
                else {
                    pb.show();
                    Call<FavResponse> call = ApiClicent.getInstance().getApi().remove_fav(user_data.uid,Integer.parseInt(quote_id));
                    call.enqueue(new Callback<FavResponse>() {
                        @Override
                        public void onResponse(Call<FavResponse> call, Response<FavResponse> response) {
                            if(response.body()!=null){
                                if(response.body().isStatus()){
                                    tag[0] = 0;
                                    holder.addtofav.setImageResource(R.drawable.ic_add_to_fav);
                                    Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                                    pb.dismiss();
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    if(list.isEmpty()){
                                        empty.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FavResponse> call, Throwable t) {
                            Log.e("addfav", "onFailure: "+t.getMessage() );
                            Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            pb.dismiss();

                        }
                    });
                }
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                String shareBody = "Quote: "+holder.quote.getText();
                /*The type of the content is text, obviously.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                /*Fire!*/
                context.startActivity(Intent.createChooser(intent,"Share"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton addtofav, share;
        TextView quote, author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addtofav = itemView.findViewById(R.id.addtofav);
            quote = itemView.findViewById(R.id.quote);
            author = itemView.findViewById(R.id.author);
            share = itemView.findViewById(R.id.share);
        }
    }
}
