package com.github.stakkato95.ving.api;

import com.github.stakkato95.ving.R;

/**
 * Created by Artyom on 20.11.2014.
 */
public class Api {

    public static final String BASE_PATH = "https://api.vk.com/method/";
    public static final String VERSION_VALUE = "5.8";
    public static final String VERSION_PARAM = "v";

    public static final String JSON_RESPONSE = "response";
    public static final String JSON_ITEMS = "items";

    public static final String ONE_INTERLOCUTOR_DIALOG = " ... ";
    public static final String SORT_ORDER_HINTS = "hints";
    public static final String EMPTY_STRING = "";
    public static final int STRING_RESOURCE_UNDEFINED = 0;

    public static final int GET_COUNT = 30;
    public static final int MESSAGE_ROUTE_IN = 0;
    public static final int MESSAGE_ROUTE_OUT = 1;
    public static final int MESSAGE_STATE_READ = 1;
    public static final int MESSAGE_STATE_UNREAD = 0;
    public static final int MESSAGE_PREVIEW_LENGTH = 35;

    public static final int USER_PROFILE_REQUEST = 1;
    public static final int USER_OFFLINE = 0;
    public static final int USER_ONLINE = 1;
    public static final int USER_ONLINE_MOBILE = 2;
    public static final int USER_SEX_MAN = 2;
    public static final int USER_SEX_WOMAN = 1;
    public static final int USER_SEX_UNDEFINED = 0;
    public static final int USER_ONLINE_STRING = R.string.user_online;
    public static final int USER_STATE = R.string.user_state;

    public static final int USER_RELATION_UNDEFINED = 0;
    public static final int USER_RELATION_NOT_MARRIED = 1;
    public static final int USER_RELATION_HAS_FRIEND = 2;
    public static final int USER_RELATION_BETROTHED = 3;
    public static final int USER_RELATION_MARRIED = 4;
    public static final int USER_RELATION_COMPLICACY = 5;
    public static final int USER_RELATION_ACTIVELY_SEARCHING = 6;
    public static final int USER_RELATION_ENAMORED = 7;
    public static final int USER_RELATION_NOT_MARRIED_MAN = R.string.relation_not_married_man;
    public static final int USER_RELATION_NOT_MARRIED_WOMAN = R.string.relation_not_married_woman;
    public static final int USER_RELATION_HAS_FRIEND_MAN = R.string.relation_has_friend_man;
    public static final int USER_RELATION_HAS_FRIEND_WOMAN = R.string.relation_has_friend_woman;
    public static final int USER_RELATION_BETROTHED_MAN = R.string.relation_betrothed_man;
    public static final int USER_RELATION_BETROTHED_WOMAN = R.string.relation_betrothed_woman;
    public static final int USER_RELATION_MARRIED_MAN = R.string.relation_married_man;
    public static final int USER_RELATION_MARRIED_WOMAN = R.string.relation_married_woman;
    public static final int USER_RELATION_COMPLICACY_STRING = R.string.relation_complicacy;
    public static final int USER_RELATION_ACTIVELY_SEARCHING_STRING = R.string.relation_actively_searching;
    public static final int USER_RELATION_ENAMORED_MAN = R.string.relation_enamored_man;
    public static final int USER_RELATION_ENAMORED_WOMAN = R.string.relation_enamored_woman;

    public static final int USER_POLITICAL_COMMUNIST = 1;
    public static final int USER_POLITICAL_SOCIALIST = 2;
    public static final int USER_POLITICAL_MODERATE = 3;
    public static final int USER_POLITICAL_LIBERAL = 4;
    public static final int USER_POLITICAL_CONSERVATIVE = 5;
    public static final int USER_POLITICAL_MONARCHICAL = 6;
    public static final int USER_POLITICAL_ULTRACONSERVATIVE = 7;
    public static final int USER_POLITICAL_INDIFFERENT = 8;
    public static final int USER_POLITICAL_LIBERTARIAN = 9;
    public static final int USER_POLITICAL_COMMUNIST_STRING = R.string.political_communist;
    public static final int USER_POLITICAL_SOCIALIST_STRING  = R.string.political_socialist;
    public static final int USER_POLITICAL_MODERATE_STRING  = R.string.political_moderate;
    public static final int USER_POLITICAL_LIBERAL_STRING  = R.string.political_liberal;
    public static final int USER_POLITICAL_CONSERVATIVE_STRING  = R.string.political_conservative;
    public static final int USER_POLITICAL_MONARCHICAL_STRING  = R.string.political_monarchical;
    public static final int USER_POLITICAL_ULTRACONSERVATIVE_STRING  = R.string.political_ultraconservative;
    public static final int USER_POLITICAL_INDIFFERENT_STRING  = R.string.political_indifferent;
    public static final int USER_POLITICAL_LIBERTARIAN_STRING  = R.string.political_libertarian;

    public static final int USER_PEOPLE_MAIN_MIND = 1;
    public static final int USER_PEOPLE_MAIN_KINDNESS = 2;
    public static final int USER_PEOPLE_MAIN_BEAUTY = 3;
    public static final int USER_PEOPLE_MAIN_POWER = 4;
    public static final int USER_PEOPLE_MAIN_COURAGE = 5;
    public static final int USER_PEOPLE_MAIN_HUMOR = 6;
    public static final int USER_PEOPLE_MAIN_MIND_STRING = R.string.people_main_mind;
    public static final int USER_PEOPLE_MAIN_KINDNESS_STRING  = R.string.people_main_kindness;
    public static final int USER_PEOPLE_MAIN_BEAUTY_STRING  = R.string.people_main_beauty;
    public static final int USER_PEOPLE_MAIN_POWER_STRING  = R.string.people_main_power;
    public static final int USER_PEOPLE_MAIN_COURAGE_STRING  = R.string.people_main_courage;
    public static final int USER_PEOPLE_MAIN_HUMOR_STRING  = R.string.people_main_humor;

    public static final int USER_LIFE_MAIN_FAMILY = 1;
    public static final int USER_LIFE_MAIN_CAREER = 2;
    public static final int USER_LIFE_MAIN_ENTERTAINMENT = 3;
    public static final int USER_LIFE_MAIN_SCIENCE = 4;
    public static final int USER_LIFE_MAIN_WORLD_PERFECTION = 5;
    public static final int USER_LIFE_MAIN_SELF_DEVELOPMENT = 6;
    public static final int USER_LIFE_MAIN_BEAUTY = 7;
    public static final int USER_LIFE_MAIN_GLORY = 8;
    public static final int USER_LIFE_MAIN_FAMILY_STRING = R.string.life_main_family;
    public static final int USER_LIFE_MAIN_CAREER_STRING = R.string.life_main_career;
    public static final int USER_LIFE_MAIN_ENTERTAINMENT_STRING = R.string.life_main_entertainment;
    public static final int USER_LIFE_MAIN_SCIENCE_STRING = R.string.life_main_science;
    public static final int USER_LIFE_MAIN_WORLD_PERFECTION_STRING = R.string.life_main_world_perfection;
    public static final int USER_LIFE_MAIN_SELF_DEVELOPMENT_STRING = R.string.life_main_self_development;
    public static final int USER_LIFE_MAIN_BEAUTY_STRING = R.string.life_main_beauty;
    public static final int USER_LIFE_MAIN_GLORY_STRING = R.string.life_main_glory;

    public static final int USER_HARMFUL_HABIT_ATTITUDE_SHARPLY_NEGATIVE = 1;
    public static final int USER_HARMFUL_HABIT_ATTITUDE_NEGATIVE = 2;
    public static final int USER_HARMFUL_HABIT_ATTITUDE_NEUTRAL = 3;
    public static final int USER_HARMFUL_HABIT_ATTITUDE_COMPROMISE = 4;
    public static final int USER_HARMFUL_HABIT_ATTITUDE_POSITIVE = 5;
    public static final int USER_HARMFUL_HABIT_ATTITUDE_SHARPLY_NEGATIVE_STRING = R.string.harmful_habit_attitude_sharply_negative;
    public static final int USER_HARMFUL_HABIT_ATTITUDE_NEGATIVE_STRING = R.string.harmful_habit_attitude_negative;
    public static final int USER_HARMFUL_HABIT_ATTITUDE_NEUTRAL_STRING = R.string.harmful_habit_attitude_neutral;
    public static final int USER_HARMFUL_HABIT_ATTITUDE_COMPROMISE_STRING = R.string.harmful_habit_attitude_compromise;
    public static final int USER_HARMFUL_HABIT_ATTITUDE_POSITIVE_STRING = R.string.harmful_habit_attitude_positive;

    public static final String USER_RELATIVE_TYPE = "type";
    public static final String USER_RELATIVE_SIBLING = "sibling";
    public static final String USER_RELATIVE_PARENT = "parent";
    public static final int USER_RELATIVE_BROTHER= R.string.info_relative_brother;
    public static final int USER_RELATIVE_SISTER= R.string.info_relative_sister;
    public static final int USER_RELATIVE_FATHER = R.string.info_relative_father;
    public static final int USER_RELATIVE_MOTHER = R.string.info_relative_mother;

    public static final int PHOTOS_SORT_CHRONOLOGICAL = 0;
    public static final int PHOTOS_SORT_ANTICHRONOLOGICAL = 1;
    public static final int PHOTOS_SPECIAL_SIZES = 1;

    public static final String FIELD_USER_IDS = "user_ids=";
    public static final String FIELD_MESSAGE_CHAT_ID = "chat_id=";
    public static final String FIELD_MESSAGE_USER_ID = "user_id=";
    public static final String FIELD_OWNER_ID = "owner_id=";
    public static final String FIELD_ALBUM_ID = "album_id=";
    public static final String FIELD_CITY_IDS = "city_ids=";

    public static final String FIELD_USER_FIELDS = "fields=";
    public static final String FIELD_FRIENDS_SORT_ORDER = "order=";
    public static final String FIELD_PHOTOS_SORT_ORDER = "rev=";
    public static final String FIELD_MESSAGE_PREVIEW = "preview_length=";
    public static final String FIELD_COUNT = "count=";
    public static final String FIELD_OFFSET = "offset=";
    public static final String FIELD_MESSAGE = "message=";
    public static final String FIELD_PHOTO_SIZES = "photo_sizes=";

    public static final String SCHOOL = "Школа";
    public static final String OBJECT_FIELD_TITLE = "title";
    public static final String OBJECT_FIELD_NAME = "name";
    public static final String OBJECT_FIELD_TYPE_STR = "type_str";

    public static final String _ID = "id";
    public static final String _PHOTO_100 = "photo_100";
    public static final String _PHOTO_200 = "photo_200";
    public static final String _PHOTO_MAX = "photo_max";
    public static final String _LAST_SEEN = "last_seen";
    public static final String _ONLINE = "online";
    public static final String _SEX = "sex";
    public static final String _BIRTHDAY = "bdate";
    public static final String _UNIVERSITIES = "universities";
    public static final String _OCCUPATION = "occupation";
    public static final String _RELATION = "relation";
    public static final String _PERSONAL = "personal";
    public static final String _STATUS = "status";
    public static final String _COUNTERS = "counters";

    //PROFILE
    public static final String _CITY = "city";
    public static final String _HOME_TOWN = "home_town";
    public static final String _COUNTRY = "country";
    public static final String _CONTACTS = "contacts";
    public static final String _SKYPE = "skype";
    public static final String _SITE = "site";
    public static final String _RELATIVES = "relatives";
    public static final String _SCHOOLS = "schools";


    public static final String _ACTIVITIES = "activities";
    public static final String _INTERESTS = "interests";
    public static final String _MUSIC = "music";
    public static final String _MOVIES = "movies";
    public static final String _TV = "tv";
    public static final String _BOOKS = "books";
    public static final String _GAMES = "games";
    public static final String _ABOUT = "about";
    public static final String _QUOTES = "quotes";

    public static final String _ALBUM_WALL = "wall";
    public static final String _ALBUM_PROFILE = "profile";
    public static final String _ALBUM_SAVED = "saved";

    private static final String FRIENDS_GET = BASE_PATH + "friends.get?" + FIELD_FRIENDS_SORT_ORDER + SORT_ORDER_HINTS + "&" + FIELD_USER_FIELDS + _PHOTO_100 + "," + _ONLINE + "," + _STATUS;
    private static final String MESSAGES_GET_DIALOGS = BASE_PATH + "messages.getDialogs?" + FIELD_MESSAGE_PREVIEW + MESSAGE_PREVIEW_LENGTH;
    private static final String MESSAGES_GET_HISTORY = BASE_PATH + "messages.getHistory?";
    private static final String USERS_GET = BASE_PATH + "users.get?" + FIELD_USER_IDS;
    private static final String PHOTOS_GET = BASE_PATH + "photos.get?" + FIELD_OWNER_ID;
    private static final String CITY_GET = BASE_PATH + "database.getCitiesById?" + FIELD_CITY_IDS;

    private static final String MESSAGES_SEND = BASE_PATH + "messages.send?";

    public static String getFriends() {
        return FRIENDS_GET;
    }

    public static String getDialogs() {
        return MESSAGES_GET_DIALOGS;
    }

    public static String getUser() {
        return USERS_GET;
    }

    public static String getDialogHistory() {
        return MESSAGES_GET_HISTORY;
    }

    public static String getPhotos() {
        return PHOTOS_GET;
    }

    public static String getCity() {
        return CITY_GET;
    }

    public static String sendMessage() {
        return MESSAGES_SEND;
    }

}