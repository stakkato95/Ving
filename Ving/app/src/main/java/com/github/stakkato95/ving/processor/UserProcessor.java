package com.github.stakkato95.ving.processor;

import android.support.annotation.NonNull;

import com.github.stakkato95.util.MultiValueMap;
import com.github.stakkato95.ving.api.Api;
import com.github.stakkato95.ving.bo.City;
import com.github.stakkato95.ving.bo.User;
import com.github.stakkato95.ving.loader.DataLoader;
import com.github.stakkato95.ving.source.VkDataSource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Artyom on 20.01.2015.
 */
public class UserProcessor implements Processor<InputStream, User[]> {

    private MultiValueMap<Integer, Integer> mCitiesMap = new MultiValueMap<>();
    private StringBuilder mIdBatch;

    @Override
    public User[] process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray jsonArray = new JSONObject(string).getJSONArray(Api.JSON_RESPONSE);
        User[] users = new User[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            users[i] = new User(jsonArray.getJSONObject(i));
            users[i].createFullName();
        }

        Map<Long, String> relativesMap = new LinkedHashMap<>();
        mIdBatch = new StringBuilder();
        JSONArray relativesJSONArray = users[0].getRelativesJSONArray();

        if (relativesJSONArray != null && relativesJSONArray.length() != 0) {
            for (int i = 0; i < relativesJSONArray.length(); i++) {
                JSONObject relative = relativesJSONArray.getJSONObject(i);
                long relativeId = relative.getLong(Api._ID);
                if (relativeId > 0) {
                    mIdBatch.append(relativeId).append(",");
                    relativesMap.put(relativeId, relative.getString(Api.USER_RELATIVE_TYPE));
                }
            }

            User[] relatives = DataLoader.getDataDirectly(Api.getUser() + mIdBatch + "&" + Api.FIELD_USER_FIELDS + Api._PHOTO_100 + "," + Api._SEX, new VkDataSource(), new UserProcessor());

            for (User relative : relatives) {
                relative.createFullName();
                String relativeType = relativesMap.get(relative.getId());
                if (relativeType.equals(Api.USER_RELATIVE_SIBLING)) {
                    if (relative.getSex() == Api.USER_SEX_MAN) {
                        relative.setRelativeType(Api.USER_RELATIVE_BROTHER);
                    } else {
                        relative.setRelativeType(Api.USER_RELATIVE_SISTER);
                    }
                } else {
                    if (relative.getSex() == Api.USER_SEX_MAN) {
                        relative.setRelativeType(Api.USER_RELATIVE_FATHER);
                    } else {
                        relative.setRelativeType(Api.USER_RELATIVE_MOTHER);
                    }
                }
            }
            users[0].setRelatives(relatives);
        }

        City[] cities;
        JSONArray schoolsJSONArray = users[0].getSchoolsJSONArray();
        if (schoolsJSONArray != null && schoolsJSONArray.length() != 0) {
            cities = getEducationInstitutionInfo(schoolsJSONArray);
            users[0].createSchools(mCitiesMap, cities);
        }

        mCitiesMap.clear();
        JSONArray universitiesJSONArray = users[0].getUniversitiesJSONArray();
        if (universitiesJSONArray != null && universitiesJSONArray.length() != 0) {
            cities = getEducationInstitutionInfo(universitiesJSONArray);
            users[0].createUniversities(mCitiesMap, cities);
        }

        users[0].createRelation();
        users[0].createPolitical();
        users[0].createPeopleMain();
        users[0].createLifeMain();
        users[0].createAttitudeToSmoking();
        users[0].createAttitudeToAlcohol();
        return users;
    }

    private City[] getEducationInstitutionInfo(@NonNull JSONArray institutionsArray) throws Exception {
        mIdBatch.setLength(0);
        for (int i = 0; i < institutionsArray.length(); i++) {
            JSONObject university = institutionsArray.optJSONObject(i);
            int cityId = university.optInt(Api._CITY);
            mIdBatch.append(cityId).append(",");
            mCitiesMap.put(cityId, i);
        }
        return DataLoader.getDataDirectly(Api.getCity() + mIdBatch, new VkDataSource(), new CityProcessor());
    }

}
