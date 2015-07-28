package chisw.com.dayit.core;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {

    public static final String APP_PREFERENCES = "plans";
    public static final String APP_PREFERENCE_MEDIA_WAY = "path";
    public static final String APP_PREFERENCE_LOGIN = "login";
    public static final String APP_PREFERENCE_PASSWORD = "pass";
    public static final String APP_PREFERENCE_LAST_LOGIN = "lastLogin";
    public static final String APP_PREFERENCES_VIBRATION_ON = "vibration_on";
    public static final String APP_PREFERENCES_NOTIFICATION_ON = "notification_on";
    public static final String APP_PREFERENCES_DURATION_SONG = "duration_song";
    public static final String APP_PREFERENCES_USER_SONG = "user_song";
    public static final String APP_PREFERENCE_SYNCHRONIZATION = "sync_on";
    public static final String APP_PREFERENCE_USER_PHONE = "user_phone";
    public static final String APP_PREFERENCE_USER_EMAIL = "user_email";

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

    public String getCurrentLogin() {
        return sharedPreferences.getString(APP_PREFERENCE_LOGIN, "");
    }

    public void setCurrentLogin(String appPreferencesLogin) {
        sharedPreferences.edit().putString(APP_PREFERENCE_LOGIN, appPreferencesLogin).apply();
    }

    public String getLastLogin() {
        return sharedPreferences.getString(APP_PREFERENCE_LAST_LOGIN, "");
    }

    public void setLastLogin(String appPreferencesLastLogin) {
        sharedPreferences.edit().putString(APP_PREFERENCE_LAST_LOGIN, appPreferencesLastLogin).apply();
    }

    public String getUserPhone(){
        return sharedPreferences.getString(APP_PREFERENCE_USER_PHONE, "");
    }

    public void setUserPhone(String appPreferencesPhone){
        sharedPreferences.edit().putString(APP_PREFERENCE_USER_PHONE, appPreferencesPhone).apply();
    }

    //pass
    public String getUserPass() {
        return sharedPreferences.getString(APP_PREFERENCE_PASSWORD, "");
    }

    public void setUserPass(String appPreferencesPassword) {
        sharedPreferences.edit().putString(APP_PREFERENCE_PASSWORD, appPreferencesPassword).apply();
    }

    public void clearUserData() {
        sharedPreferences.edit().putString(APP_PREFERENCE_LOGIN, "").apply();
        sharedPreferences.edit().putString(APP_PREFERENCE_PASSWORD, "").apply();
        sharedPreferences.edit().putString(APP_PREFERENCE_USER_PHONE, "").apply();
    }

    // ******   Save Settings   *******
    public void setVibrationOn(boolean appPreferencesVibrationOn) {
        sharedPreferences.edit().putBoolean(APP_PREFERENCES_VIBRATION_ON, appPreferencesVibrationOn).apply();
    }

    public boolean getVibrationOn() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_VIBRATION_ON, true);
    }

    public void setNotificationOn(boolean appPreferencesNotificationOn) {
        sharedPreferences.edit().putBoolean(APP_PREFERENCES_NOTIFICATION_ON, appPreferencesNotificationOn).apply();
    }

    public boolean getNotificationOn() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_NOTIFICATION_ON, true);
    }

    public void setSynchronization(boolean appPreferenceSynchronization) {
        sharedPreferences.edit().putBoolean(APP_PREFERENCE_SYNCHRONIZATION, appPreferenceSynchronization).apply();
    }

    public boolean getSynchronization() {
        return sharedPreferences.getBoolean(APP_PREFERENCE_SYNCHRONIZATION, true);
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

    public String getEmailAddress() {
        return sharedPreferences.getString(APP_PREFERENCE_USER_EMAIL, "");
    }

    public void setEmailAddress(String appPreferenceEmailAddress) {
        sharedPreferences.edit().putString(APP_PREFERENCE_USER_EMAIL, appPreferenceEmailAddress).apply();
    }
}
