package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.lang.InterruptedException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity {

    int locationOfCorrectAnswer=0;
    int chosenImage=0;
    ImageView imageView;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    String [] answers = new String[4];

    ArrayList<String> celebURLs = new ArrayList<>();
    ArrayList<String> chosenNames = new ArrayList<>();


    //DOWNLOADING THE IMAGES FROM THE INTERNET
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


    //CODE TO DOWNLOAD THE CONTENTS OF A URL
    public static class DownloadImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url ;
            HttpURLConnection urlConnection;
            StringBuilder result = new StringBuilder();

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(inputStream);

                int data = isr.read();
                while(data!=-1){

                    char current = (char)data;
                    result.append(current);
                    data = isr.read();

                }
                return result.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    //CODE TO ALWAYS UPDATE IMAGES AND BUTTON CONTENTS ON CLICK
    public void updateStatus(){

            Random random = new Random();
            chosenImage = random.nextInt(celebURLs.size());
            ImageDownloader imageDownloader = new ImageDownloader();
            Bitmap celebPics;

            try {
                celebPics = imageDownloader.execute(celebURLs.get(chosenImage)).get();
                imageView.setImageBitmap(celebPics);
            }
            catch (Exception e) {
              e.printStackTrace();
            }

            locationOfCorrectAnswer = random.nextInt(4);
            int incorrectAnswer;
            for(int i=0;i<4;i++){
                if(i==locationOfCorrectAnswer){
                    answers[i] = chosenNames.get(chosenImage);
                }else{
                    incorrectAnswer = random.nextInt(celebURLs.size());
                    while (incorrectAnswer==chosenImage){
                        incorrectAnswer = random.nextInt(celebURLs.size());
                    }
                    answers[i] = chosenNames.get(incorrectAnswer);
                }
                button1.setText(String.format("%s",answers[0]));
                button2.setText(String.format("%s",answers[1]));
                button3.setText(String.format("%s",answers[2]));
                button4.setText(String.format("%s",answers[3]));


            }





    }


    //CHECKING TO SEE IF THE CLICKED BUTTON MATCHES THE PERSON IN THE IMAGE
    public void guessCelebrity(View view){

        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){

            Toast("Correct");
        }
        else{
            Toast( "Incorrect" + chosenNames.get(chosenImage)) ;
        }
        updateStatus();
    }


    // METHOD TO REDUCE USE OF TOAST
    public void Toast(String message){

        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        DownloadImage image = new DownloadImage();
        String result ;

        try {
            result = image.execute("http://www.posh24.se/kandisar").get();

            //GETTING THE SPECIFIC CONTENT WE NEED USING THE REGEX METHOD
            String [] celebrityNames = result.split("<div class=\"sidebarContainer\">");
            //<div class=\"sidebarContainer\">
            Pattern p = Pattern.compile("alt=\"(.*?)\"");
            Matcher m = p.matcher(celebrityNames[0]);
            while(m.find()){

                //ASSIGNING THE DOWNLOADED NAMES TO THE NAMES ARRAY
                chosenNames.add(m.group(1));
            }

            p = Pattern.compile(" <img src=\"(.*?)\"");
            m = p.matcher(celebrityNames[0]);
            while(m.find()){

                //ASSIGNING THE DOWNLOADED IMAGES TO THE IMAGES ARRAY
                celebURLs.add(m.group(1));
            }}
        catch(InterruptedException | ExecutionException e){e.printStackTrace();}
        updateStatus();
    }

    }

