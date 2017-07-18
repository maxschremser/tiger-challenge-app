package com.schremser.challenger.providers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by bluemax on 11.07.17.
 */
public class VCAPHelper {
    static String VCAP_SERVICES = System.getenv("VCAP_SERVICES");

    public static JsonObject getCloudCredentials(String serviceName) {
        if(VCAP_SERVICES == null){
            return null;
        }
        //Convert VCAP_SERVICES String to JSON
        JsonObject obj = (JsonObject) new JsonParser().parse(VCAP_SERVICES);
        Map.Entry<String, JsonElement> dbEntry = null;
        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();

        // Look for the VCAP key that holds the service info
        for (Map.Entry<String, JsonElement> eachEntry : entries) {
            if (eachEntry.getKey().toLowerCase().contains(serviceName)) {
                dbEntry = eachEntry;
                break;
            }
        }
        if (dbEntry == null) {
            System.out.println("VCAP_SERVICES: Could not find " + serviceName);
            return null;
        }

        obj = (JsonObject) ((JsonArray) dbEntry.getValue()).get(0);
        System.out.println("VCAP_SERVICES: Found " + (String) dbEntry.getKey());

        return (JsonObject) obj.get("credentials");
    }

    public static JsonObject getLocalCredentials(String fileName) {
        InputStream inputStream = VCAPHelper.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonObject obj = (JsonObject) new JsonParser().parse(reader);
        return obj;
    }

}