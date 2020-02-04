package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int locationOfCorrectAnswer;
    ImageView imageView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    /*String [] correctNames = {"Brian", "Kyole", "Richard", "Mary", "Mueni"};
    String [] incorrectNames = {"Ken","Martin","Andrew"};*/

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
        //update();

        DownloadImage image = new DownloadImage();
        String result;

        try {
            result = image.execute("http://www.posh24.se/kandisar").get();
            Log.i("Contents of UrL", result);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void guessCelebrity(View view){

       // update();


    }
    /*public void update(){




        Random rand = new Random();
        locationOfCorrectAnswer = rand.nextInt(4);
        //int incorrectAnswer = 0;
        for(int i=0;i<4;i++) {
            if(i==locationOfCorrectAnswer){
                for(String correct:correctNames){
               answer.add(correct);}}
            else{
                answer.addAll(Arrays.asList(incorrectNames));}
        }
        button1.setText(String.format("%s",answer.get(0)));
        button2.setText(String.format("%s",answer.get(1)));
        button3.setText(String.format("%s",answer.get(2)));
        button4.setText(String.format("%s",answer.get(3)));

        *//*for(String correct:correctNames)
            correct++;*//*

    }
*/
        public static class DownloadImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url ;
            HttpURLConnection urlConnection;
            String result = "";

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);

                int data = isr.read();
                while(data!=-1){

                    char current = (char)data;
                    result +=current;
                    data = isr.read();

                }
                return result;

            } catch (Exception e) {
               e.printStackTrace();
           }

            return null;
        }
    }

    }

