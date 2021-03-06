package kwasilewski.marketplace.util;

public final class AppConstants {

    //URL
    public static final String BASE_URL = "http://10.0.2.2:8080/rest/";

    //HINT
    public static final String HINTS_PATH = "hints";
    public static final String PROVINCES_PATH = "provinces";

    //USER
    public static final String REGISTER_PATH = "register";
    public static final String LOGIN_PATH = "login";
    public static final String TOKEN_PATH = "token";
    public static final String USER_PATH = "user";
    public static final String USER_PASSWORD_PATH = "user/password";

    //AD
    public static final String AD_PATH = "ads/{id}";
    public static final String ADS_PATH = "ads";
    public static final String AD_USER_PATH = "user/ads/{id}";
    public static final String ADS_USER_PATH = "user/ads";
    public static final String AD_STATUS_PATH = "user/ads/{id}/status";
    public static final String AD_REFRESH_PATH = "user/ads/{id}/refresh";
    public static final String FAVOURITE_PATH = "ads/{id}/favourite";
    public static final String FAVOURITES_PATH = "user/favourites";

    //SharedPreferences
    public static final String SHARED_PREF_NAME = "kwasilewski.marketplace.sharedPreferences";
    public static final String SHARED_PREF_TOKEN = "token";
    public static final String SHARED_PREF_USER = "user";

    public static final String MODE_KEY = "mode";
    public static final String AD_ID_KEY = "adId";
    public static final String AD_POS_KEY = "position";

    public static final String TITLE_KEY = "title";
    public static final String PRICE_MIN_KEY = "priceMin";
    public static final String PRICE_MAX_KEY = "priceMax";
    public static final String PROVINCE_KEY = "prvId";
    public static final String CATEGORY_KEY = "catId";
    public static final String SUBCATEGORY_KEY = "sctId";

    public static final int MAX_PHOTOS = 10;
    public static final int MAX_RESULTS = 6;

    public static final int LOGIN_CODE = 1;
    public static final int FILTER_CODE = 2;
    public static final int REMOVABLE_CODE = 3;

    public static final int MODE_NORMAL = 0;
    public static final int MODE_ACTIVE = 1;
    public static final int MODE_INACTIVE = 2;
    public static final int MODE_FAVOURITE = 3;

}
