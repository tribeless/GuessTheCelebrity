package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity {

    int locationOfCorrectAnswer;
    ImageView imageView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    /*String [] correctNames = {"Brian", "Kyole", "Richard", "Mary", "Mueni"};
    String [] incorrectNames = {"Ken","Martin","Andrew"};*/

    ArrayList<String> celebURLs = new ArrayList<>();
    ArrayList<String> chosenNames = new ArrayList<>();

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
            //<div class="sidebarContainer">

            String [] celebrityNames = result.split("<div class=\"sidebarContainer\">");
            Pattern p = Pattern.compile("alt=\"(.*?)\"");
            Matcher m = p.matcher(celebrityNames[0]);
            while(m.find()){

                chosenNames.add(m.group(1));
            }

             p = Pattern.compile(" src=\"(.*?)\"");
             m = p.matcher(celebrityNames[0]);
            while(m.find()){

                celebURLs.add(m.group(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Random random = new Random();
        locationOfCorrectAnswer = random.nextInt(celebURLs.size());
        ImageDownloader imageDownloader = new ImageDownloader();
        Bitmap celebPics;
        try {
            celebPics = imageDownloader.execute(celebURLs.get(locationOfCorrectAnswer)).get();
            imageView.setImageBitmap(celebPics);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void guessCelebrity(View view){

       // update();


    }

    public static class ImageDownloader extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String...urls){
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
                return null;
        }

    }

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
                    result += current;
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

