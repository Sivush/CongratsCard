package com.sivush.lasyd.gifwithtextapp;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

public class MainActivity extends AppCompatActivity implements VerticalStepperForm, DialogGallery.OnAddGalleryListener {

    VerticalStepperFormLayout verticalStepperForm;
    ProgressDialog progressDialog;

    //dialog gallery
    ImageView ChoiceImage;
    int GalleryId=1;

    String[] GalDog;
    String[] GalBirthday;
    String[] GalFlowers;
    String[] GalCozy;
    String[] GalSeason;
    String[] GalCat;
    String[] GalLove;
    String[] GalAlcohol;
    String[] GalFun;

    //gallery choice step
    List<ChoiceCard> galleries;
    String[] GalleryTypes;
    String[][]GalleryLinks;

    //gallery step
    RecyclerView rvGallery;

    //wishes step
    TextView tv;
    String[] mWishes;

    //result
    EditText ResultText;
    ImageView CardImage;
    String CardUri;
    String textwishes;


    RVAdapter RVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] mySteps = {"Выберите раздел", "Выбрать гифку", "Пожелания","Открытка"};

        GalleryTypes = new String[]{"birthday","flowers","cozy","season","cat","dog","love","alcohol","fun","cat"};
        galleries = new ArrayList<>();
        galleries.add(new ChoiceCard("День Рождения", "birthday"));
        galleries.add(new ChoiceCard("Цветы",  "flowers"));
        galleries.add(new ChoiceCard("Уют",  "cozy"));
        galleries.add(new ChoiceCard("Сезон",  "season"));
        galleries.add(new ChoiceCard("Кошки",  "cat"));
        galleries.add(new ChoiceCard("Собаки",  "dog"));
        galleries.add(new ChoiceCard("Романтика",  "love"));
        galleries.add(new ChoiceCard("Алкоголь",  "alcohol"));
        galleries.add(new ChoiceCard("Веселье",  "fun"));

        GalBirthday=getResources().getStringArray(R.array.birthday);
        GalFlowers=getResources().getStringArray(R.array.flowers);
        GalCozy=getResources().getStringArray(R.array.cozy);
        GalSeason=getResources().getStringArray(R.array.season);
        GalCat=getResources().getStringArray(R.array.cat);
        GalDog=getResources().getStringArray(R.array.dog);
        GalLove=getResources().getStringArray(R.array.love);
        GalAlcohol=getResources().getStringArray(R.array.alcohol);
        GalFun=getResources().getStringArray(R.array.fun);

        GalleryLinks=new String[][]{GalBirthday,GalFlowers,GalCozy,GalSeason,GalCat,GalDog,GalLove,GalAlcohol,GalFun};

        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);

        // Finding the view
        verticalStepperForm = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);

        // Setting up and initializing the form
        VerticalStepperFormLayout.Builder.newInstance(verticalStepperForm, mySteps, this, this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .materialDesignInDisabledSteps(false)
                .displayBottomNavigation(false) // It is true by default, so in this case this line is not necessary
                .init();

    }

    @Override
    public View createStepContentView(int stepNumber) {
        View view = null;
        switch (stepNumber) {
            case 0:
                view = createGalleryChoice();
                break;
            case 1:
                view = createGalleryStep();
                break;
            case 2:
                view = createWishesStep();
                break;
            case 3:
                view = createCardStep();
        }
        return view;
    }


    private View createGalleryChoice() {

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout choice_gallery=(LinearLayout) inflater.inflate(R.layout.gallery_choice_step,null,false);
        ChoiceImage=(ImageView)choice_gallery.findViewById(R.id.ivImage);
        Button ButTest=(Button)choice_gallery.findViewById(R.id.button2);
        ButTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DialogGallery dialogFragment = new DialogGallery ();
                dialogFragment.show(fm, "Dialog Fragment");
            }
        });

        return choice_gallery;
    }

    private View createGalleryStep() {
        // Here we generate programmatically the view that will be added by the system to the step content layout
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout search_gallery=(LinearLayout) inflater.inflate(R.layout.gallery_search,null,false);
        rvGallery=(RecyclerView)search_gallery.findViewById(R.id.rvJson);
        LinearLayoutManager llmJ = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvGallery.setLayoutManager(llmJ);
       /* RVAdapter = new RVAdapter(this,initializeArray(GalleryLinks[GalleryId]));
        rvJson.setAdapter(RVAdapter);*/
        return search_gallery;
    }

    private View createWishesStep(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout wishes_list=(LinearLayout) inflater.inflate(R.layout.wishes_step,null,false);

        Button btnWIsh = (Button) wishes_list.findViewById(R.id.btn);
        tv = (TextView) wishes_list.findViewById(R.id.tv);

        btnWIsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                mWishes = getResources().getStringArray(R.array.wishes);
                final boolean[] checkedItems = new boolean[mWishes.length];
                final List<String> wishesList = Arrays.asList(mWishes);

                builder.setMultiChoiceItems(mWishes,checkedItems , new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        checkedItems[which] = isChecked;
                        String currentItem = wishesList.get(which);

                    }
                });

                builder.setCancelable(false);
                builder.setTitle("Желаю.....");

                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when click positive button
                        tv.setText("Желаю \n");
                        for (int i = 0; i<checkedItems.length; i++){
                            boolean checked = checkedItems[i];
                            if (checked) {
                                tv.setText(tv.getText() + wishesList.get(i)+", " + "\n");
                            }
                        }
                        if (tv.getText().toString().endsWith(", \n")){
                            tv.setText(tv.getText().toString().substring(0, tv.getText().toString().length() - 3) + "! ");

                            String str = tv.getText().toString();
                            int ind = str.lastIndexOf(", ");
                            if( ind>=0 )
                                str = new StringBuilder(str).replace(ind, ind+1," и ").toString();

                            tv.setText(str);

                        }
                    }
                });

                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when click the negative button
                    }
                });
                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });


        return wishes_list;
    }

    private View createCardStep(){
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        LinearLayout card_layout=(LinearLayout) inflater.inflate(R.layout.card_step,null,false);
        ResultText=(EditText)card_layout.findViewById(R.id.result_text);
        CardImage=(ImageView)card_layout.findViewById(R.id.result_image);
        return card_layout;
    }

    @Override
    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case 0:
                verticalStepperForm.setActiveStepAsCompleted();
                break;
            case 1:
                checkGallery();
                verticalStepperForm.setActiveStepAsCompleted();
                break;
            case 2:
                checkUri();
                // As soon as the phone number step is open, we mark it as completed in order to show the "Continue"
                // button (We do it because this field is optional, so the user can skip it without giving any info)
                verticalStepperForm.setStepAsCompleted(2);
                // In this case, the instruction above is equivalent to:
                // verticalStepperForm.setActiveStepAsCompleted();
                break;
            case 3:
                checkUri();
                checkName();
                verticalStepperForm.setActiveStepAsCompleted();
                break;
        }
    }

    private void checkName() {
        textwishes=tv.getText().toString().replace("\n", "").replace("\r", "");
        ResultText.setText(textwishes);
    }

    public void checkUri(){

        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_NAME", MODE_PRIVATE);
        String url = sharedPreferences.getString("UriCh","Not Available");
        Log.d("TTT", url);
        if(url != null){
            CardUri=url;
            Glide.with(this)
                    .load(CardUri)
                    .asGif()
                    .override(250, 250)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .crossFade(250)
                    .placeholder(R.drawable.lower)
                    .into(CardImage);
        }
    }


    @Override
    public void sendData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.vertical_form_stepper_form_sending_data_message));
        executeDataSending();
    }

    private void executeDataSending() {

        SendCard sendCard=new SendCard(this,CardUri,ResultText.getText().toString());
    }


    @Override
    public void onAddGallerySubmit(int id) {
        ChoiceImage.setVisibility(View.VISIBLE);
        int i = getResources().getIdentifier(galleries.get(id).photoId, "drawable", getPackageName());
        Glide.with(this)
                .load(i)
                .asGif()
                .override(250, 250)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .placeholder(R.drawable.question_mark)
                .error(R.drawable.lower)
                .into(ChoiceImage);
        checkGallery();
    }

    public void checkGallery(){

        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_NAME", MODE_PRIVATE);
        int i = sharedPreferences.getInt("GalleryName",0);
        GalleryId=i;
        RVAdapter = new RVAdapter(this,initializeArray(GalleryLinks[GalleryId]));
        rvGallery.setAdapter(RVAdapter);
        RVAdapter.notifyDataSetChanged();
    }

    //вызывается адаптер для галереи
    private void initializeAdapter(RecyclerView rv, List<ImageCard> imagesT) {
        RVAdapter adapter = new RVAdapter(this, imagesT);
        rv.setAdapter(adapter);

    }


    //!for gallery случайно упорядочиваются картинки
    private List<ImageCard> initializeArray(String[] Gallery) {
        List<ImageCard> imagesRes=new ArrayList<>();

        for(String temp:Gallery){
            imagesRes.add(new ImageCard(temp));
        }
        return imagesRes;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }

}
