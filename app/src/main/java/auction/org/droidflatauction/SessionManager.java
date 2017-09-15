package auction.org.droidflatauction;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * Created by nazmul on 6/6/2017.
 */

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    //public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_IDENTITY = "identity";
    public static final String KEY_SESSION_ID = "sessionId";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    //public static final String KEY_USER_ID = "userId";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String identity, String sessionId){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing email in pref
        editor.putString(KEY_IDENTITY, identity);
        editor.putString(KEY_SESSION_ID, sessionId);
        //editor.putInt(KEY_USER_ID, userId);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MemberDashboard.class);
            // Closing all the Activities
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    /*public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        //user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }*/

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        //call server to logout

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, NonMemberHome.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getSessionId()
    {
        return pref.getString(KEY_SESSION_ID, null);
    }

    public void setSessionId(String sessionId)
    {
        editor.putString(KEY_SESSION_ID, sessionId);
        // commit changes
        editor.commit();
    }

    public int getUserId()
    {
        return pref.getInt(KEY_USER_ID, 0);
    }

    public void setUserId(int userId)
    {
        editor.putInt(KEY_USER_ID, userId);
        // commit changes
        editor.commit();
    }

    public String getEmail()
    {
        return pref.getString(KEY_EMAIL, null);
    }

    public void setEmail(String email)
    {
        editor.putString(KEY_EMAIL, email);
        // commit changes
        editor.commit();
    }

    public String getPassword()
    {
        return pref.getString(KEY_PASSWORD, null);
    }

    public void setPassword(String password)
    {
        editor.putString(KEY_PASSWORD, password);
        // commit changes
        editor.commit();
    }
}
