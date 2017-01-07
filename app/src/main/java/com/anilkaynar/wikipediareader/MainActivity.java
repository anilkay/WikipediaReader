package com.anilkaynar.wikipediareader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    public static String convert(String cevir) {
        String dondur;
        dondur=cevir.replace(" ","%20");
        Log.e("Convert",dondur);

        dondur=dondur.trim();
        return dondur;
    }
    String content=" ";
    String basliim=" ";
    String quer="a";
    StringBuilder result2;
    TextView textView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       final Wiki a= new Wiki();
         textView= (TextView) findViewById(R.id.icerik);
        editText=(EditText) findViewById(R.id.tekis);
        final TextView ter= (TextView) findViewById(R.id.baslik);
        //Button button2=(Button)findViewById(R.id.ara);
        Button button=(Button) findViewById(R.id.buton);
         if (!quer.equalsIgnoreCase("a")) {
             a.execute();
         }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textView.setText(content);
                ter.setText(basliim);
            }
        });

        try {
           // textView.setText(content);
            //ter.setText(basliim);
        } catch (Exception e) {
          Log.e("Json",e.toString());
        }
    }
    public void ara(View v) {
        String araa=String.valueOf(editText.getText())+"";
        editText.getText().clear();
        araa=convert(araa);
        quer=araa;
        Wiki b= new Wiki();
        b.execute();
    }
    public void  onResume(Bundle savedInstance) {
        try {
            Log.i("Resume",content);
            JSONObject jsonObject= new JSONObject(content);
            Log.e("Resume2",jsonObject.toString());
            textView.setText(content);

        } catch (Exception e) {
            Log.e("Json",e.toString());
        }
    }
private class Wiki extends AsyncTask<Void,Void,Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        try {
        //URL url= new URL("https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&titles=donald%20Trump&format=json");
            URL url=new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles="+quer);
        HttpURLConnection connection= (HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream in= connection.getInputStream();
            result2 = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader reader5 = new BufferedReader(reader);
            String line = reader5.readLine();
            StringBuilder builder=new StringBuilder();
            while (line != null) {
                builder.append(line);
                line = reader5.readLine();
            }
            content=builder.toString();
            JSONObject baseJsonResponse = new JSONObject(content);
            JSONObject geri=baseJsonResponse.getJSONObject("query");
            JSONObject pages=geri.getJSONObject("pages");
            JSONArray pa=pages.names();
            String pageid= pa.getString(0);
            JSONObject icerik= pages.getJSONObject(pageid);
            String title=icerik.getString("title");
            content=icerik.getString("extract");
            basliim=title;
            //Log.e("Asasa",title);
            //Log.e("Asasa2",content);
            //Log.e("Asynctask",baseJsonResponse.toString());
            //Log.e("Asynctask2",geri.toString());
            //Log.e("Asynctask3",pages.toString());
            //Log.e("Asynctask4",icerik.toString());

        } catch (Exception e) {
            Log.e("MainActivity",e.toString());
        }
        return null;
    }
}
}
