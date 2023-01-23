package com.example.coursework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectUrl {

    public String retrieveURL(String strUrl) throws IOException {
        String urlData = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(strUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(5000);

            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            urlData = stringBuffer.toString();
            bufferedReader.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{

            inputStream.close();
            httpURLConnection.disconnect();
        }
        return urlData;
    }
}

