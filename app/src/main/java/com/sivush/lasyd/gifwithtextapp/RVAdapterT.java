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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RVAdapterT extends RecyclerView.Adapter<RVAdapterT.PersonViewHolder>{


    private Context context;

    int selected_position = -1;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        ImageView personPhoto;




        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cardview);
            personName = (TextView)itemView.findViewById(R.id.tvTitle);
            personPhoto = (ImageView)itemView.findViewById(R.id.ivImage);

        }
    }

    List<ChoiceCard> persons;

    RVAdapterT(Context context, List<ChoiceCard> persons){
        this.persons = persons;
        this.context=context;

    }
    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card, viewGroup, false);

        PersonViewHolder pvh = new PersonViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {

            personViewHolder.personName.setText(persons.get(i).name);
            personViewHolder.itemView.setBackgroundColor(selected_position == i ? Color.parseColor("#A60000") : Color.TRANSPARENT);
            int id = context.getResources().getIdentifier(persons.get(i).photoId, "drawable", context.getPackageName());

          Glide.with(context)
                        .load(id)
                        .apply(new RequestOptions().override(400,400).transforms(new CenterCrop(), new RoundedCorners(20)))
                        .into(personViewHolder.personPhoto);


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
                    editor.putInt("GalleryName", i);
                    editor.apply();
                }
            });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
