package com.example.vibing.services;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MyMemoryTranslationService {
    private static final String TAG = "MyMemoryTranslation";
    private static final String BASE_URL = "https://api.mymemory.translated.net/get";
    private Context context;

    public MyMemoryTranslationService(Context context) {
        this.context = context;
    }

    public interface TranslationCallback {
        void onSuccess(String translatedText);
        void onError(String errorMessage);
    }

    public void translateText(String textToTranslate, TranslationCallback callback) {
        if (textToTranslate == null || textToTranslate.trim().isEmpty()) {
            callback.onSuccess("");
            return;
        }

        Log.d(TAG, "Translating text: " + textToTranslate);

        new Thread(() -> {
            try {
                String encodedText = URLEncoder.encode(textToTranslate, StandardCharsets.UTF_8.toString());
                String urlString = BASE_URL + "?q=" + encodedText + "&langpair=en|fr";
                
                Log.d(TAG, "Translation URL: " + urlString);
                
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Translation API response code: " + responseCode);

                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "UTF-8"));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    String responseString = response.toString();
                    Log.d(TAG, "Translation API response: " + responseString);

                    // Parser la réponse JSON
                    String translatedText = parseTranslationResponse(responseString);
                    if (translatedText != null && !translatedText.equals(textToTranslate)) {
                        Log.d(TAG, "Translation successful: " + translatedText);
                        callback.onSuccess(translatedText);
                    } else {
                        Log.w(TAG, "Translation failed or returned same text, using original");
                        callback.onSuccess(textToTranslate); // Utiliser l'original si traduction échoue
                    }
                } else {
                    Log.e(TAG, "Translation API error: " + responseCode);
                    callback.onError("Erreur API: " + responseCode);
                }

                connection.disconnect();

            } catch (IOException e) {
                Log.e(TAG, "Translation network error: " + e.getMessage(), e);
                callback.onError("Erreur réseau: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Translation error: " + e.getMessage(), e);
                callback.onError("Erreur de traduction: " + e.getMessage());
            }
        }).start();
    }

    private String parseTranslationResponse(String jsonResponse) {
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
            
            if (jsonObject.has("responseData")) {
                JsonObject responseData = jsonObject.getAsJsonObject("responseData");
                if (responseData.has("translatedText")) {
                    String translatedText = responseData.get("translatedText").getAsString();
                    // Décoder les entités HTML
                    String decodedText = Html.fromHtml(translatedText, Html.FROM_HTML_MODE_LEGACY).toString();
                    Log.d(TAG, "Original: " + translatedText + " -> Decoded: " + decodedText);
                    return decodedText;
                }
            }
            
            Log.e(TAG, "Invalid translation response format: " + jsonResponse);
            return null;
            
        } catch (Exception e) {
            Log.e(TAG, "Error parsing translation response: " + e.getMessage(), e);
            return null;
        }
    }

    // Méthode utilitaire pour traduire une liste de textes
    public void translateTexts(String[] textsToTranslate, TranslationCallback[] callbacks) {
        for (int i = 0; i < textsToTranslate.length; i++) {
            final int index = i;
            translateText(textsToTranslate[i], new TranslationCallback() {
                @Override
                public void onSuccess(String translatedText) {
                    if (callbacks[index] != null) {
                        callbacks[index].onSuccess(translatedText);
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Log.w(TAG, "Translation failed for text " + index + ": " + errorMessage);
                    // Utiliser le texte original si la traduction échoue
                    if (callbacks[index] != null) {
                        callbacks[index].onSuccess(textsToTranslate[index]);
                    }
                }
            });
        }
    }
}