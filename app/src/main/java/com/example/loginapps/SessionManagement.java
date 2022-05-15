package com.example.loginapps;
import android.content.SharedPreferences;
import android.content.Context;

public class SessionManagement {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY  = "session_user";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences("SHARED_PREF_NAME",context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void saveSession(Users user){
        //save session of user whenever user is login
        int id = user.getId();
        editor.putInt(SESSION_KEY,id).commit();
    }


    public int getSession(){
        return sharedPreferences.getInt(SESSION_KEY,-1);

    }

    public void removeSession(){
        editor.putInt(SESSION_KEY, -1).commit();
    }

}
