package com.github.stakkato95.ving.processor;

import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.bo.City;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Artyom on 06.02.2015.
 */
public class CityProcessor implements Processor<InputStream, City[]> {

    @Override
    public City[] process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray jsonArray = new JSONObject(string).getJSONArray(Api.JSON_RESPONSE);

        City[] cities = new City[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            cities[i] = new City(jsonArray.getJSONObject(i));
        }

        return cities;
    }

}
