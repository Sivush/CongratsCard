package com.sivush.lasyd.gifwithtextapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by lasyd on 20.08.2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{


    private Context context;

    int selected_position = -1;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView imageItem;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            imageItem = (ImageView)itemView.findViewById(R.id.gif_image);
        }
    }

    List<ImageCard> persons;

    RVAdapter(Context context, List<ImageCard> persons){
        this.persons = persons;
        this.context=context;

    }
    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public RVAdapter.PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, viewGroup, false);

        RVAdapter.PersonViewHolder pvh = new RVAdapter.PersonViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(final RVAdapter.PersonViewHolder personViewHolder, final int i) {

        personViewHolder.itemView.setBackgroundColor(selected_position == i ? Color.parseColor("#A60000") : Color.TRANSPARENT);

        Glide.with(context)
                .load(persons.get(i).photoId)
                .asBitmap()
                .centerCrop()
                .crossFade(250)
                .placeholder(R.drawable.lower)
                .error(R.drawable.lower)
                .into(personViewHolder.imageItem);
        //при касании выбор картинки
        personViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == RecyclerView.NO_POSITION) return;
                notifyItemChanged(selected_position);
                selected_position = personViewHolder.getLayoutPosition();
                notifyItemChanged(selected_position);
                SharedPreferences sharedPreferences1 = context.getSharedPreferences("SHARED_PREF_NAME", Context.MODE_PRIVATE);
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString("UriCh", persons.get(i).photoId);
                editor.apply();
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}