package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    int locationOfCorrectAnswer;
    ImageView imageView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    ArrayList<Object> answer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

    }

    public void guessCelebrity(View view){




    }
    public void update(){


        DownloadImage img = new DownloadImage();
        Bitmap newImg  ;

        try {
            newImg = img.execute("https://static1.therichestimages.com/wordpress/wp-content/uploads/2012/06/Michael-Bloomberg.jpg?").get();
            imageView.setImageBitmap(newImg);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String names="";

        Random rand = new Random();
        locationOfCorrectAnswer = rand.nextInt(4)+1;
        int incorrectAnswer = 0;
        for(int i=0;i<=4;i++) {
            if(i==locationOfCorrectAnswer)
                answer.add(1);
            else
                answer.add(incorrectAnswer);
        }

    }

        public static class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url ;

            try {
                url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    }

