package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    String Sinfo="";
    Double temp,maxt,mint;
    Boolean status=false;
    public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {
            String result="";
            URL url;
            HttpURLConnection c=null;
            try{
                url=new URL(params[0]);
                c=(HttpURLConnection)url.openConnection();
                InputStream inputStream=c.getInputStream();
                InputStreamReader reader=new InputStreamReader(inputStream);
                int data=reader.read();
                while(data!=-1)
                {
                    char current=(char)data;
                    result=result+current;
                    data=reader.read();
                }
                return result;

            }
            catch(Exception e){
                e.printStackTrace();
                return "FAILED";

            }
        }

    }
    
    public void onPress(View view)
    {
        status=true;
        String s=null;
        buttons();
        Button b;
        b= (Button) findViewById(R.id.temperature);
        b.setAlpha(1f);
        b= (Button) findViewById(R.id.mint);
        b.setAlpha(1f);
        b= (Button) findViewById(R.id.maxt);
        b.setAlpha(1f);
        int x=0;
        TextView city=(TextView) findViewById(R.id.city);
        DownloadTask task=new DownloadTask();
        String result=null;
        try {
            result=task.execute("https://api.openweathermap.org/data/2.5/weather?appid=3db243a99d95c2fb26f2ef85d09a7a5f&q="+city.getText().toString()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TextView t=(TextView) findViewById(R.id.Info);
        Log.i("Result",result);
        try {
            JSONObject j=new JSONObject(result);
            String WeatherInfo=j.getString("weather");
            String w=j.getString("main");
            JSONObject js;
            JSONArray arr=new JSONArray(WeatherInfo);
            for(int i=0;i<arr.length();i++)
            {
                js=arr.getJSONObject(i);
                t.setText("Description:"+js.getString("description"));

            }
            w=w+",";
            char[] c=w.toCharArray();
            int i=0,m;
            System.out.println(c.length);
            while(i<c.length)
            {
                if(c[i]==':')
                {
                    m=i;
                    while(c[m]!=',')
                    {
                        Sinfo=Sinfo+c[m];
                        m++;
                    }
                }
                i++;
            }
            moredata();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void clear(View view)
    {
        TextView t=(TextView) findViewById(R.id.Info);
        t.setText("");
        t=(TextView) findViewById(R.id.city);
        t.setText("");
        t=(TextView) findViewById(R.id.temp);
        t.setText("");
        t=(TextView) findViewById(R.id.maxtemp);
        t.setText("");
        t=(TextView) findViewById(R.id.mintemp);
        t.setText("");
        t=(TextView) findViewById(R.id.pressure);
        t.setText("");
        t=(TextView) findViewById(R.id.humidity);
        t.setText("");
        status=false;
        buttons();

    }
    public void moredata()
    {
        System.out.println(Sinfo);
        Sinfo=Sinfo+":";
        String str="";
        TextView t;
        int count=0,m;
        char []c=Sinfo.toCharArray();
        for(int i=0;i<c.length-1;i++)
        {
            if(c[i]==':')
            {
                count++;
                m=i+1;
                while(c[m]!=':')
                {
                    str=str+c[m];
                    m++;
                }
                switch(count)
                {
                    case 1:
                    {
                        t=(TextView) findViewById(R.id.temp);
                        t.setText(str);
                        temp=Double.parseDouble(str);
                    }
                    break;
                    case 3:
                    {
                        t=(TextView) findViewById(R.id.mintemp);
                        t.setText(str);
                        mint=Double.parseDouble(str);
                    }
                    break;
                    case 4:
                    {
                        t=(TextView) findViewById(R.id.maxtemp);
                        t.setText(str);
                        maxt=Double.parseDouble(str);
                    }
                    break;
                    case 5:
                    {
                        t=(TextView) findViewById(R.id.pressure);
                        t.setText(str);
                    }
                    break;
                    case 6:
                    {
                        t=(TextView) findViewById(R.id.humidity);
                        char[] a=str.toCharArray();
                        for(int n=0;n<str.length();n++)
                        {
                            if(a[n]=='}')
                            {
                                a[n]=' ';
                            }
                        }
                        str=new String(a);
                        t.setText(str);
                    }
                    break;
                }
                str="";
            }
        }
        Sinfo="";
    }
    public void tempchange(View view)
    {
        if(status==true)
        {
            Button b=(Button) view;
            TextView t = null;
            int x=Integer.valueOf((String) b.getTag());
            if(x==0)
            {
                t=(TextView) findViewById(R.id.temp);
            }
            if(x==1)
            {
                t=(TextView) findViewById(R.id.mintemp);
            }
            if(x==2)
            {
                t=(TextView) findViewById(R.id.maxtemp);
            }
            double d=Double.parseDouble((String) t.getText());
            String s=(String) b.getText();
            Log.i("asd",s);
            if(s.equals("K"))
            {
                d=d-273.15;
                b.setText("C");
            }
            if(s.equals("C"))
            {
                d=((d*9)/5)+32;
                b.setText("F");
            }
            if(s.equals("F"))
            {
                d=d-32;
                d=d*5/9;
                d=d+273.15;
                b.setText("K");
            }
            t.setText(String.format("%.2f",d));

        }
    }

    public void buttons()
    {
        Button b;
        b= (Button) findViewById(R.id.temperature);
        b.setText("K");
        b.setAlpha(0.1f);
        b= (Button) findViewById(R.id.mint);
        b.setText("K");
        b.setAlpha(0.1f);
        b= (Button) findViewById(R.id.maxt);
        b.setText("K");
        b.setAlpha(0.1f);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
