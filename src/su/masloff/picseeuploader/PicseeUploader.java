/*
 * Copyright (c) 2018 Kirill Maslov
 * All Rights Reserved
 */

package su.masloff.picseeuploader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PicseeUploader {
    private static PicseeUploader instance = null;

    private static final String PICSEE_POST_URL = "http://picsee.net/upload.php";

    private PicseeUploader() {
    }

    public static PicseeUploader getInstance() {
        if (instance == null) {
            instance = new PicseeUploader();
        }

        return instance;
    }

    public String uploadFileByURL(String fileToUploadURL) {
        String uploadedFileURL = "";
        String urlParams = "remote[]=" + fileToUploadURL;
        String response = "";

        try {
            // create a new connection
            URL picseePostURL = new URL(PICSEE_POST_URL);
            HttpURLConnection conn = (HttpURLConnection) picseePostURL.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // send params to the server
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(urlParams);
            writer.flush();

            // get the response
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = reader.readLine()) != null) {
                response += line + "\n";
            }

            // close the connection
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        uploadedFileURL = parseResponse(response);

        return uploadedFileURL;
    }

    private String parseResponse(String response) {
        String url = "";

        try {
            BufferedReader reader = new BufferedReader(new StringReader(response));

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("links[4]")) {
                    url = getDataBetweenQuotes(line);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return url;
    }

    private String getDataBetweenQuotes(String line) {
        Pattern pattern = Pattern.compile("'([^']*)'");
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        return matcher.group(1);
    }
}
