package com.sivush.lasyd.gifwithtextapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class DialogGallery extends DialogFragment {

    private OnAddGalleryListener callback;

    public interface OnAddGalleryListener {
        public void onAddGallerySubmit(int id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.callback = (OnAddGalleryListener) activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gallery_dialog, container, false);
        getDialog().setTitle("Выберете галерею");

        List<ChoiceCard> galleries = new ArrayList<>();
        galleries.add(new ChoiceCard("День Рождения", "birthday"));
        galleries.add(new ChoiceCard("Цветы",  "flowers"));
        galleries.add(new ChoiceCard("Уют",  "cozy"));
        galleries.add(new ChoiceCard("Кошки",  "cat"));
        galleries.add(new ChoiceCard("Собаки",  "dog"));
        galleries.add(new ChoiceCard("Романтика",  "love"));
        galleries.add(new ChoiceCard("Алкоголь",  "alcohol"));
        galleries.add(new ChoiceCard("Веселье",  "fun"));

        RecyclerView rvGif=(RecyclerView)rootView.findViewById(R.id.rvGif);
        GridLayoutManager llmJ  =new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL,false);
        rvGif.setLayoutManager(llmJ);
        RVAdapterT RVAdapterT = new RVAdapterT(getActivity(),galleries);
        rvGif.setAdapter(RVAdapterT);

        Button dismiss = (Button) rootView.findViewById(R.id.dismiss);
        Button ok = (Button) rootView.findViewById(R.id.Ok);
        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF_NAME", getActivity().MODE_PRIVATE);
                int id = sharedPreferences.getInt("GalleryName",-1);
                callback.onAddGallerySubmit(id);
                dismiss();
            }
        });

        return rootView;
    }
}