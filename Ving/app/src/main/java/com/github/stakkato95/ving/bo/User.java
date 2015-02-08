package com.github.stakkato95.ving.bo;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.stakkato95.util.MultiValueMap;
import com.github.stakkato95.ving.R;
import com.github.stakkato95.ving.api.Api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Artyom on 20.01.2015.
 */
public class User extends JSONObjectWrapper {

    //other fields will be added after
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHOTO_100 = "photo_100";
    private static final String PHOTO_200 = "photo_200";
    private static final String PHOTO_MAX = "photo_max";
    private static final String LAST_SEEN = "last_seen";
    private static final String LAST_SEEN_TIME = "time";
    private static final String ID = "id";
    private static final String SEX = "sex";

    private static final String FULL_NAME = "full_name";
    private static final String STATUS = "status";
    private static final String ONLINE = "online";
    private static final String ONLINE_MOBILE = "online_mobile";

    private static final String COUNTERS = "counters";
    private static final String COUNTER_ALBUMS = "albums";
    private static final String COUNTER_VIDEOS = "videos";
    private static final String COUNTER_AUDIOS = "audios";
    private static final String COUNTER_PHOTOS = "photos";
    private static final String COUNTER_FRIENDS = "friends";
    private static final String COUNTER_MUTUAL_FRIENDS = "mutual_friends";
    private static final String COUNTER_FOLLOWERS = "followers";
    private static final String COUNTER_GIFTS = "gifts";

    private static final String BIRTHDAY = "bdate";
    private static final String HOME_TOWN = "home_town";
    private static final String RELATION = "relation";
    private static final String LANGS = "langs";
    private static final String SKYPE = "skype";
    private static final String SITE = "site";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String TITLE = "title";
    private static final String PHONE_MOBILE = "mobile_phone";
    private static final String PHONE = "home_phone";
    private static final String RELATIVES = "relatives";
    private static final String RELATIVE_TYPE = "relative_type";

    private static final String EDUCATION_INSTITUTION_NAME = "name";

    private static final String SCHOOLS = "schools";
    private static final String SCHOOL_TYPE = "school_type_str";
    private static final String SCHOOL_YEAR_GRADUATED = "year_graduated";
    private static final String SCHOOL_YEAR_FROM = "year_from";
    private static final String SCHOOL_YEAR_TO = "year_to";
    private static final String SCHOOL_CLASS = "class";
    private static final String SCHOOL_SPECIALITY = "speciality";
    private static final String SCHOOL_STRING = "Школа";
    private static final String SCHOOL_SPECIALITY_STRING = "Специальность: ";

    private static final String UNIVERSITIES = "universities";
    private static final String UNIVERSITY_GRADUATION = "graduation";
    private static final String UNIVERSITY_FACULTY = "faculty_name";
    private static final String UNIVERSITY_CHAIR = "chair_name";
    private static final String UNIVERSITY_EDUCATION_FORM = "education_form";
    private static final String UNIVERSITY_EDUCATION_STATUS = "education_status";
    private static final String UNIVERSITY_FACULTY_STRING = "Факультет: ";
    private static final String UNIVERSITY_CHAIR_STRING = "Кафедра: ";
    private static final String UNIVERSITY_EDUCATION_FORM_STRING = "Форма обучения: ";
    private static final String UNIVERSITY_EDUCATION_STATUS_STRING = "Статус: ";

    private static final String PERSONAL = "personal";
    private static final String POLITICAL = "political";
    private static final String RELIGION = "religion";
    private static final String PEOPLE_MAIN = "people_main";
    private static final String LIFE_MAIN = "life_main";
    private static final String SMOKING = "smoking";
    private static final String ALCOHOL = "alcohol";
    private static final String INSPIRED_BY = "inspired_by";

    private User[] mRelatives;
    private int mRelativeType;
    private List<String> mSchools;
    private List<String> mUniversities;

    public User(String jsonObject) {
        super(jsonObject);
    }

    public User(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected User(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFirstName() {
        return getString(FIRST_NAME);
    }

    public String getLastName() {
        return getString(LAST_NAME);
    }

    public String getPhoto100() {
        return getString(PHOTO_100);
    }

    public String getPhoto200() {
        return getString(PHOTO_200);
    }

    public String getPhotoMax() {
        return getString(PHOTO_MAX);
    }

    public String getFullName() {
        return getString(FULL_NAME);
    }

    public int getOnlineMode() {
        long isOnlineMobile = getLong(ONLINE_MOBILE);
        if (isOnlineMobile == 1) {
            return 2;
        }
        long isOnline = getLong(ONLINE);
        if (isOnline == 1) {
            return 1;
        }
        return 0;
    }

    public Long getId() {
        return getLong(ID);
    }

    public Long getSex() {
        return getLong(SEX);
    }


    public String getStatus() {
        return getString(STATUS);
    }

    public Long getLastSeen() {
        return getJSONObject(LAST_SEEN).optLong(LAST_SEEN_TIME);
    }

    public LinkedHashMap<Integer, String> getCountersMap() {
        JSONObject object = getJSONObject(COUNTERS);
        Map<Integer, String> countersMap = new LinkedHashMap<>();
        countersMap.put(R.string.counter_friends, object.optString(COUNTER_FRIENDS));
        countersMap.put(R.string.counter_mutual_friends, object.optString(COUNTER_MUTUAL_FRIENDS));
        countersMap.put(R.string.counter_followers, object.optString(COUNTER_FOLLOWERS));
        countersMap.put(R.string.counter_photos, object.optString(COUNTER_PHOTOS));
        countersMap.put(R.string.counter_albums, object.optString(COUNTER_ALBUMS));
        countersMap.put(R.string.counter_audios, object.optString(COUNTER_AUDIOS));
        countersMap.put(R.string.counter_videos, object.optString(COUNTER_VIDEOS));
        countersMap.put(R.string.counter_gifts, object.optString(COUNTER_GIFTS));
        return (LinkedHashMap<Integer, String>) countersMap;
    }


    //PROFILE
    public String getBirthday() {
        return getString(BIRTHDAY);
    }

    public String getHomeTown() {
        return getString(HOME_TOWN);
    }

    public int getRelation() {
        return getInt(RELATION);
    }

    public User[] getRelatives() {
        return mRelatives;
    }

    public String getLangs() {
        StringBuilder builder = new StringBuilder();
        try {
            JSONArray langsArray = getJSONObject(PERSONAL).getJSONArray(LANGS);
            for (int i = 0; i < langsArray.length(); i++) {
                if (i < langsArray.length() - 1) {
                    builder.append(langsArray.optString(i)).append(",");
                } else {
                    builder.append(langsArray.optString(i));
                }
            }
            return builder.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getCity() {
        JSONObject cityObject = getJSONObject(CITY);
        if (cityObject != null) {
            return cityObject.optString(TITLE);
        } else {
            return null;
        }
    }

    public String getCountry() {
        JSONObject countryObject = getJSONObject(COUNTRY);
        if (countryObject != null) {
            return countryObject.optString(TITLE);
        } else {
            return null;
        }
    }

    public String getPhoneMobile() {
        return getString(PHONE_MOBILE);
    }

    public String getPhone() {
        return getString(PHONE);
    }

    public String getSkype() {
        return getString(SKYPE);
    }

    public String getSite() {
        return getString(SITE);
    }

    public List<String> getSchools() {
        return mSchools;
    }

    public List<String> getUniversities() {
        return mUniversities;
    }

    public int getPolitical() {
        return getInt(POLITICAL);
    }

    public String getReligion() {
        JSONObject countryObject = getJSONObject(PERSONAL);
        if (countryObject != null) {
            return countryObject.optString(RELIGION);
        } else {
            return null;
        }
    }

    public int getLifeMain() {
        return getInt(LIFE_MAIN);
    }

    public int getPeopleMain() {
        return getInt(PEOPLE_MAIN);
    }

    public int getAttentionToSmoking() {
        return getInt(SMOKING);
    }

    public int getAttentionToAlcohol() {
        return getInt(ALCOHOL);
    }

    public String getInspiration() {
        JSONObject personalInfo = getJSONObject(INSPIRED_BY);
        if (personalInfo != null) {
            return personalInfo.optString(INSPIRED_BY);
        } else {
            return null;
        }
    }


    public void setRelativeType(int relativeType) {
        setField(RELATIVE_TYPE, relativeType);
    }

    public void setRelatives(User[] relatives) {
        mRelatives = new User[relatives.length];
        System.arraycopy(relatives, 0, mRelatives, 0, relatives.length);
    }



    public JSONArray getRelativesJSONArray() {
        return getJSONArray(RELATIVES);
    }

    public JSONArray getSchoolsJSONArray() {
        return getJSONArray(SCHOOLS);
    }

    public JSONArray getUniversitiesJSONArray() {
        return getJSONArray(UNIVERSITIES);
    }


    public void createFullName() {
        setField(FULL_NAME, getFirstName() + " " + getLastName());
    }

    public void createRelation() {
        int relationId = getInt(RELATION);
        int relationResource;
        switch (relationId) {
            case Api.USER_RELATION_ACTIVELY_SEARCHING:
                relationResource = Api.USER_RELATION_ACTIVELY_SEARCHING_STRING;
                break;
            case Api.USER_RELATION_BETROTHED:
                if (getSex() == Api.USER_SEX_MAN) {
                    relationResource = Api.USER_RELATION_BETROTHED_MAN;
                } else {
                    relationResource = Api.USER_RELATION_BETROTHED_WOMAN;
                }
                break;
            case Api.USER_RELATION_COMPLICACY:
                relationResource = Api.USER_RELATION_COMPLICACY_STRING;
                break;
            case Api.USER_RELATION_ENAMORED:
                if (getSex() == Api.USER_SEX_MAN) {
                    relationResource = Api.USER_RELATION_ENAMORED_MAN;
                } else {
                    relationResource = Api.USER_RELATION_ENAMORED_WOMAN;
                }
                break;
            case Api.USER_RELATION_HAS_FRIEND:
                if (getSex() == Api.USER_SEX_MAN) {
                    relationResource = Api.USER_RELATION_HAS_FRIEND_MAN;
                } else {
                    relationResource = Api.USER_RELATION_HAS_FRIEND_WOMAN;
                }
                break;
            case Api.USER_RELATION_MARRIED:
                if (getSex() == Api.USER_SEX_MAN) {
                    relationResource = Api.USER_RELATION_MARRIED_MAN;
                } else {
                    relationResource = Api.USER_RELATION_MARRIED_WOMAN;
                }
                break;
            case Api.USER_RELATION_NOT_MARRIED:
                if (getSex() == Api.USER_SEX_MAN) {
                    relationResource = Api.USER_RELATION_NOT_MARRIED_MAN;
                } else {
                    relationResource = Api.USER_RELATION_NOT_MARRIED_WOMAN;
                }
                break;
            default:
                relationResource = Api.USER_RELATION_UNDEFINED;
        }
        setField(RELATION, relationResource);
    }

    public void createSchools(MultiValueMap<Integer,Integer> indexesMap, City[] cities) {
        mSchools = new ArrayList<>();
        JSONArray schools = getJSONArray(SCHOOLS);
        StringBuilder schoolBuilder = new StringBuilder();
        for (City city : cities) {
            List<Integer> indexes = indexesMap.get(city.getId());
            for (Integer index : indexes) {
                JSONObject school = schools.optJSONObject(index);
                if (school.optString(SCHOOL_TYPE) != null && !(school.optString(SCHOOL_TYPE)).equals(Api.EMPTY_STRING)) {
                    schoolBuilder.append(school.optString(SCHOOL_TYPE));
                } else {
                    schoolBuilder.append(SCHOOL_STRING);
                }
                schoolBuilder.append(' ')
                        .append(school.optString(EDUCATION_INSTITUTION_NAME))
                        .append(' ');

                String graduated;
                if (!(graduated = school.optString(SCHOOL_YEAR_GRADUATED)).equals(Api.EMPTY_STRING)) {
                    schoolBuilder.append('\'')
                            .append(graduated.substring(2, 4));
                }

                schoolBuilder.append('\n')
                        .append(city.getName());

                String yearFrom;
                String yearTo;
                if (!(yearFrom = school.optString(SCHOOL_YEAR_FROM)).equals(Api.EMPTY_STRING) &&
                        !(yearTo = school.optString(SCHOOL_YEAR_TO)).equals(Api.EMPTY_STRING)) {
                    schoolBuilder.append(',')
                            .append(' ')
                            .append(yearFrom)
                            .append('-')
                            .append(yearTo);
                }

                String clazz;
                if (!(clazz = school.optString(SCHOOL_CLASS)).equals(Api.EMPTY_STRING)) {
                    schoolBuilder.append(' ')
                            .append('(')
                            .append(clazz)
                            .append(')');
                }

                String speciality;
                if ((speciality = school.optString(SCHOOL_SPECIALITY)) != null) {
                    schoolBuilder.append('\n')
                            .append(SCHOOL_SPECIALITY_STRING)
                            .append(speciality);
                }
                mSchools.add(schoolBuilder.toString());
            }
        }
    }

    public void createUniversities(MultiValueMap<Integer,Integer> indexesMap, City[] cities) {
        mUniversities = new ArrayList<>();
        JSONArray universities = getJSONArray(UNIVERSITIES);
        StringBuilder universityBuilder = new StringBuilder();
        for (City city : cities) {
            List<Integer> indexes = indexesMap.get(city.getId());
            for (Integer index : indexes) {
                JSONObject university = universities.optJSONObject(index);

                universityBuilder.append(university.optString(EDUCATION_INSTITUTION_NAME));

                String graduated;
                if (!(graduated = university.optString(UNIVERSITY_GRADUATION)).equals(Api.EMPTY_STRING)) {
                    universityBuilder.append('\'')
                            .append(graduated.substring(2, 4));
                }

                universityBuilder.append('\n');

                String faculty;
                if (!(faculty = university.optString(UNIVERSITY_FACULTY)).equals(Api.EMPTY_STRING)) {
                    universityBuilder.append(UNIVERSITY_FACULTY_STRING)
                            .append(faculty);
                }

                String chair;
                if (!(chair = university.optString(UNIVERSITY_CHAIR)).equals(Api.EMPTY_STRING)) {
                    universityBuilder.append('\n')
                            .append(UNIVERSITY_CHAIR_STRING)
                            .append(chair);
                }

                String educationForm;
                if (!(educationForm = university.optString(UNIVERSITY_EDUCATION_FORM)).equals(Api.EMPTY_STRING)) {
                    universityBuilder.append('\n')
                            .append(UNIVERSITY_EDUCATION_FORM_STRING)
                            .append(educationForm);
                }

                String educationStatus;
                if (!(educationStatus = university.optString(UNIVERSITY_EDUCATION_STATUS)).equals(Api.EMPTY_STRING)) {
                    universityBuilder.append('\n')
                            .append(UNIVERSITY_EDUCATION_STATUS_STRING)
                            .append(educationStatus);
                }
                mUniversities.add(universityBuilder.toString());
            }
        }
    }

    public void createPolitical() throws Exception {
        int politicalId;
        int politicalResource;

        JSONObject personalInfo = getJSONObject(PERSONAL);
        if (personalInfo != null) {
            politicalId = personalInfo.getInt(POLITICAL);

            switch (politicalId) {
                case Api.USER_POLITICAL_COMMUNIST:
                    politicalResource = Api.USER_POLITICAL_COMMUNIST_STRING;
                    break;
                case Api.USER_POLITICAL_SOCIALIST:
                    politicalResource = Api.USER_POLITICAL_SOCIALIST_STRING;
                    break;
                case Api.USER_POLITICAL_MODERATE:
                    politicalResource = Api.USER_POLITICAL_MODERATE_STRING;
                    break;
                case Api.USER_POLITICAL_LIBERAL:
                    politicalResource = Api.USER_POLITICAL_LIBERAL_STRING;
                    break;
                case Api.USER_POLITICAL_CONSERVATIVE:
                    politicalResource = Api.USER_POLITICAL_CONSERVATIVE_STRING;
                    break;
                case Api.USER_POLITICAL_MONARCHICAL:
                    politicalResource = Api.USER_POLITICAL_MONARCHICAL_STRING;
                    break;
                case Api.USER_POLITICAL_ULTRACONSERVATIVE:
                    politicalResource = Api.USER_POLITICAL_ULTRACONSERVATIVE_STRING;
                    break;
                case Api.USER_POLITICAL_INDIFFERENT:
                    politicalResource = Api.USER_POLITICAL_INDIFFERENT_STRING;
                    break;
                default:
                    politicalResource = Api.USER_POLITICAL_LIBERTARIAN_STRING;
            }
        } else {
            politicalResource = 0;
        }
        setField(POLITICAL, politicalResource);
    }

    public void createPeopleMain() throws Exception {
        int peopleMainId;
        int peopleMainResource;

        JSONObject personalInfo = getJSONObject(PERSONAL);
        if (personalInfo != null) {
            peopleMainId = personalInfo.getInt(PEOPLE_MAIN);

            switch (peopleMainId) {
                case Api.USER_PEOPLE_MAIN_MIND:
                    peopleMainResource = Api.USER_PEOPLE_MAIN_MIND_STRING;
                    break;
                case Api.USER_PEOPLE_MAIN_KINDNESS:
                    peopleMainResource = Api.USER_PEOPLE_MAIN_KINDNESS_STRING;
                    break;
                case Api.USER_PEOPLE_MAIN_BEAUTY:
                    peopleMainResource = Api.USER_PEOPLE_MAIN_BEAUTY_STRING;
                    break;
                case Api.USER_PEOPLE_MAIN_POWER:
                    peopleMainResource = Api.USER_PEOPLE_MAIN_POWER_STRING;
                    break;
                case Api.USER_PEOPLE_MAIN_COURAGE:
                    peopleMainResource = Api.USER_PEOPLE_MAIN_COURAGE_STRING;
                    break;
                default:
                    peopleMainResource = Api.USER_PEOPLE_MAIN_HUMOR_STRING;
            }
        } else {
            peopleMainResource = 0;
        }
        setField(PEOPLE_MAIN, peopleMainResource);
    }

    public void createLifeMain() throws Exception {
        int lifeMainId;
        int lifeMainResource;

        JSONObject personalInfo = getJSONObject(PERSONAL);
        if (personalInfo != null) {
            lifeMainId = personalInfo.getInt(LIFE_MAIN);

            switch (lifeMainId) {
                case Api.USER_LIFE_MAIN_FAMILY:
                    lifeMainResource = Api.USER_LIFE_MAIN_FAMILY_STRING;
                    break;
                case Api.USER_LIFE_MAIN_CAREER:
                    lifeMainResource = Api.USER_LIFE_MAIN_CAREER_STRING;
                    break;
                case Api.USER_LIFE_MAIN_ENTERTAINMENT:
                    lifeMainResource = Api.USER_LIFE_MAIN_ENTERTAINMENT_STRING;
                    break;
                case Api.USER_LIFE_MAIN_SCIENCE:
                    lifeMainResource = Api.USER_LIFE_MAIN_SCIENCE_STRING;
                    break;
                case Api.USER_LIFE_MAIN_WORLD_PERFECTION:
                    lifeMainResource = Api.USER_LIFE_MAIN_WORLD_PERFECTION_STRING;
                    break;
                case Api.USER_LIFE_MAIN_SELF_DEVELOPMENT:
                    lifeMainResource = Api.USER_LIFE_MAIN_SELF_DEVELOPMENT_STRING;
                    break;
                case Api.USER_LIFE_MAIN_BEAUTY:
                    lifeMainResource = Api.USER_LIFE_MAIN_BEAUTY_STRING;
                    break;
                default:
                    lifeMainResource = Api.USER_LIFE_MAIN_GLORY_STRING;
            }
        } else {
            lifeMainResource = 0;
        }
        setField(LIFE_MAIN, lifeMainResource);
    }

    public void createAttitudeToSmoking() throws Exception {
        int smokingAttentionResource;

        JSONObject personalInfo = getJSONObject(PERSONAL);
        if (personalInfo != null) {
            int smokingAttentionId = personalInfo.getInt(SMOKING);
            smokingAttentionResource = createAttitudeToHarmfulHabit(smokingAttentionId);
        } else {
            smokingAttentionResource = 0;
        }
        setField(SMOKING, smokingAttentionResource);
    }

    public void createAttitudeToAlcohol() throws Exception {
        int alcoholAttentionResource;

        JSONObject personalInfo = getJSONObject(PERSONAL);
        if (personalInfo != null) {
            int alcoholAttentionId = personalInfo.getInt(ALCOHOL);
            alcoholAttentionResource = createAttitudeToHarmfulHabit(alcoholAttentionId);
        } else {
            alcoholAttentionResource = 0;
        }
        setField(ALCOHOL, alcoholAttentionResource);
    }

    private int createAttitudeToHarmfulHabit(int HarmfulHabitAttentionId) {
        int harmfulHabitAttentionResource;

        switch (HarmfulHabitAttentionId) {
            case Api.USER_HARMFUL_HABIT_ATTITUDE_SHARPLY_NEGATIVE:
                harmfulHabitAttentionResource = Api.USER_HARMFUL_HABIT_ATTITUDE_SHARPLY_NEGATIVE_STRING;
                break;
            case Api.USER_HARMFUL_HABIT_ATTITUDE_NEGATIVE:
                harmfulHabitAttentionResource = Api.USER_HARMFUL_HABIT_ATTITUDE_NEGATIVE_STRING;
                break;
            case Api.USER_HARMFUL_HABIT_ATTITUDE_NEUTRAL:
                harmfulHabitAttentionResource = Api.USER_HARMFUL_HABIT_ATTITUDE_NEUTRAL_STRING;
                break;
            case Api.USER_HARMFUL_HABIT_ATTITUDE_COMPROMISE:
                harmfulHabitAttentionResource = Api.USER_HARMFUL_HABIT_ATTITUDE_COMPROMISE_STRING;
                break;
            default:
                harmfulHabitAttentionResource = Api.USER_HARMFUL_HABIT_ATTITUDE_POSITIVE_STRING;
        }
        return harmfulHabitAttentionResource;
    }

}