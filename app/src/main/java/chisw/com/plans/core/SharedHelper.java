package chisw.com.plans.core;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    public static final String appPreferences = "this is settings";
    public static final String APP_PREFERENCE_MEDIA_WAY = "default way";
    public static final String APP_PREFERENCE_LOGIN = "default login";

    private SharedPreferences sharedPreferences;

    public SharedHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(appPreferences, Context.MODE_PRIVATE);
    }

    public String getDefaultMediaWay () {
        return sharedPreferences.getString(APP_PREFERENCE_MEDIA_WAY, "String");
    }

    public void setDefaultMediaWay (String appPreferencesMediaWay) {
        sharedPreferences.edit().putString(APP_PREFERENCE_MEDIA_WAY, appPreferencesMediaWay).apply();
    }

    public String getDefaultLogin() {
        return sharedPreferences.getString(APP_PREFERENCE_LOGIN, "String");
    }

    public void setDefaultLogin(String appPreferencesLogin) {
        sharedPreferences.edit().putString(APP_PREFERENCE_LOGIN, appPreferencesLogin).apply();
    }
}
