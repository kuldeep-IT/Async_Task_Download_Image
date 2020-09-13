package com.example.asynctaskdownloadimage;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Task task = new Task(MainActivity.this);
                task.execute("https://i.ytimg.com/vi/N1tk45_WhZ0/hqdefault.jpg");

            }
        });

    }

    private class Task extends AsyncTask<String,Integer,Bitmap>
    {

        Context context;
        ProgressDialog progressDialog;

        public Task(Context context) {
            this.context=context;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();




            progressDialog.setTitle("Downloading...");
            progressDialog.setMax(10);
            progressDialog.setMessage("please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                    cancel(true);

                }
            });

            progressDialog.show();


        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            String stringUrl = strings[0];
            Bitmap bitmap = null;

            try {
                URL url = new URL(stringUrl);
                InputStream inputStream = url.openStream();
                bitmap =BitmapFactory.decodeStream(inputStream);


                for (int i=0;i<=10;i++)
                {
                    Thread.sleep(1000);
                    publishProgress(i);
                    Log.i("Task","perform i" + i);
                }



            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();


            }

            return bitmap;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {

            int value = values[0];
            progressDialog.setProgress(value);


            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);


            imageView.setImageBitmap(bitmap);
            progressDialog.dismiss();
        }
    }


}
