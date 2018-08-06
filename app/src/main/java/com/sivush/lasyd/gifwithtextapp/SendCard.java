package com.sivush.lasyd.gifwithtextapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Telephony;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lasyd on 23.04.2018.
 */

public class SendCard {
    String EndUri;
    String EndText;
    Context context;

    public SendCard(Context context, String EndUri, String EndText){
        this.EndUri=EndUri;
        this.EndText=EndText;
        this.context=context;
        isPackageExisted();
    }

    String PackageSend;
    List<String> packageshas;
    Intent shareIntent;
    Intent shareIntent1;
    MyTask mt;
    Uri UriDel;

    //предлагает мессенджеры
    public void isPackageExisted(){
        List<ApplicationInfo> packages;
        PackageManager pm;
        ArrayList<String> packagesarr= new ArrayList<String>();
        //get sms package diff versions
        String defaultSmsPackageName;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
        } else {
            defaultSmsPackageName="vnd.android-dir/mms-sms";
        }
        packageshas = Arrays.asList("android.email","com.vkontakte.android", "com.viber.voip", "com.whatsapp","org.telegram.messenger");
        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);

        for(String pack:packageshas) {
            for (ApplicationInfo packageInfo : packages) {
                if (packageInfo.packageName.contains(pack)) {
                    packagesarr.add(packageInfo.packageName);
                }
            }
        }

        if (!packagesarr.isEmpty()) {
            String[] namesArr = packagesarr.toArray(new String[packagesarr.size()]);
            doSendButton(namesArr);
        }
        else {
            Toast.makeText(context, "На устройстве отсутствуют мессенджеры", Toast.LENGTH_LONG).show();
            //dialog
        };
    }

    //выбор мессенджеров
    public void doSendButton(final String[] ListAd) {

        final ArrayList<String> packages =new ArrayList<String>(Arrays.asList(ListAd));

        if (packages.size() > 1) {
            ArrayAdapter<String> adapter = new ChooserArrayAdapter(context, android.R.layout.select_dialog_item, android.R.id.text1, packages);
            new android.support.v7.app.AlertDialog.Builder(context)
                    .setTitle("Title")
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item ) {
                            invokeApplication(packages.get(item));
                        }
                    })
                    .show();
        } else if (packages.size() == 1) {
            invokeApplication(packages.get(0));
        }
    }

    void invokeApplication(String packageName) {
        if(packageName.contains("skype")||packageName.contains("vkontakte")||packageName.contains("whatsapp")||packageName.contains("telegram.messenger")||packageName.contains("android.contacts")||packageName.contains("android.email")){
            PackageSend=packageName;
            mt = new SendCard.MyTask();
            mt.execute(EndUri);
          /*  if (UriDel!=null) {
                File file = new File(UriDel.getPath());
                boolean deleted = file.delete();
                Log.d("NewL", "Erase " + deleted);
            }*/

        }
        else {
            prepareShareIntentLink(packageName,EndUri);
        }
    }

    //обработка гифки
    class MyTask extends AsyncTask<String, Void, Uri> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Uri doInBackground(String... params) {
            String SendURL = params[0];
            Uri bmpUri=null;

            try {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "shared_gif_shai" + System.currentTimeMillis() + ".gif");

                URL url = new URL(SendURL);
                URLConnection ucon = url.openConnection();
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] img = new byte[1024];
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baos.write(current);
                }
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
                is.close();
                bmpUri = Uri.fromFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmpUri;
        }

        @Override
        protected void onPostExecute(Uri result) {
            super.onPostExecute(result);

            prepareShareIntentImage(result, PackageSend);
            UriDel=result;

        }
    }

    //два типа отправки
    private void prepareShareIntentLink(String packageName, String Uri) {
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, EndText);
        shareIntent.putExtra(Intent.EXTRA_TEXT, Uri);
        Log.d("textSend", "postex" +EndText+" "+Uri);
        shareIntent.setType("text/plain");
        shareIntent.setPackage(packageName);
        context.startActivity(shareIntent);
    }

    private void prepareShareIntentImage(Uri bmp, String PackageSend) {

        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, EndText);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmp);
       // shareIntent.putExtra(Intent.EXTRA_STREAM, "https://upload.wikimedia.org/wikipedia/commons/2/2c/Rotating_earth_%28large%29.gif");
        shareIntent.setPackage(PackageSend);
        Log.d("textSend", "postex" +EndText);
       // shareIntent.setType("*/*");
        shareIntent.setType("application/vnd.my.package");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(shareIntent);

    }
}
