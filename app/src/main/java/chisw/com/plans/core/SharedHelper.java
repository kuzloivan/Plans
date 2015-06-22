package chisw.com.plans.core;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    public static final String APP_PREFERENCES = "plans";
    public static final String APP_PREFERENCE_MEDIA_WAY = "path";
    public static final String APP_PREFERENCE_LOGIN = "login";
    public static final String APP_PREFERENCE_PASSWORD = "pass";
    public static final String APP_PREFERENCES_VIBRATION_ON = "vibration_on";
    public static final String APP_PREFERENCES_NOTIFICATION_ON = "notification_on";
    public static final String APP_PREFERENCES_DURATION_SONG = "duration_song";
    public static final String APP_PREFERENCES_USER_SONG = "user_song";

    private SharedPreferences sharedPreferences;

    public SharedHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getDefaultMediaWay() {
        return sharedPreferences.getString(APP_PREFERENCE_MEDIA_WAY, "");
    }

    public void setDefaultMediaWay(String appPreferencesMediaWay) {
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
        sharedPreferences.edit().clear().apply();
    }

    // ******   Save Settings   *******
    public void setVibrationOn(boolean appPreferencesVibrationOn) {
        sharedPreferences.edit().putBoolean(APP_PREFERENCES_VIBRATION_ON, appPreferencesVibrationOn).apply();
    }

    public boolean getVibrationOn() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_VIBRATION_ON, true);
    }
/*    public void setVibrationOn(String appPreferencesVibrationOn) {
        sharedPreferences.edit().putString(APP_PREFERENCES_VIBRATION_ON, appPreferencesVibrationOn).apply();
    }

    public String getVibrationOn() {
        return sharedPreferences.getString(APP_PREFERENCES_VIBRATION_ON, "");
    }*/
    public void setNotificationOn(boolean appPreferencesNotificationOn) {
        sharedPreferences.edit().putBoolean(APP_PREFERENCES_NOTIFICATION_ON, appPreferencesNotificationOn).apply();
    }

    public boolean getNotificationOn() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_NOTIFICATION_ON, true);
    }

    public void setDurationSong(String appPreferencesDurationSong) {
        sharedPreferences.edit().putString(APP_PREFERENCES_DURATION_SONG, appPreferencesDurationSong).apply();
    }

    public String getDurationSong() {
        return sharedPreferences.getString(APP_PREFERENCES_DURATION_SONG, "15");
    }

    public void setUserAudio(boolean appPreferencesUserAudio) {
        sharedPreferences.edit().putBoolean(APP_PREFERENCES_USER_SONG, appPreferencesUserAudio).apply();
    }

    public boolean getUserSong() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_USER_SONG, false);
    }
    // *********   Settings  **********
}
