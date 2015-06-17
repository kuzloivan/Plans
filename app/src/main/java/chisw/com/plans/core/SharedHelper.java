package chisw.com.plans.core;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    public static final String APP_PREFERENCES = "plans";
    public static final String APP_PREFERENCE_MEDIA_WAY = "path";
    public static final String APP_PREFERENCE_LOGIN = "login";
    public static final String APP_PREFERENCE_PASSWORD = "pass";

    private SharedPreferences sharedPreferences;

    public SharedHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getDefaultMediaWay () {
        return sharedPreferences.getString(APP_PREFERENCE_MEDIA_WAY, "");
    }

    public void setDefaultMediaWay (String appPreferencesMediaWay) {
        sharedPreferences.edit().putString(APP_PREFERENCE_MEDIA_WAY, appPreferencesMediaWay).apply();
    }

    public String getDefaultLogin() {
        return sharedPreferences.getString(APP_PREFERENCE_LOGIN, "");
    }

    public void setDefaultLogin(String appPreferencesLogin) {
        sharedPreferences.edit().putString(APP_PREFERENCE_LOGIN, appPreferencesLogin).apply();
    }
    //pass
    public String getDefaultPass() {
        return sharedPreferences.getString(APP_PREFERENCE_PASSWORD, "");
    }

    public void setDefaultPass(String appPreferencesPassword) {
        sharedPreferences.edit().putString(APP_PREFERENCE_PASSWORD, appPreferencesPassword).apply();
    }

    public void clearData() {
        sharedPreferences.edit().clear();
    }
}
