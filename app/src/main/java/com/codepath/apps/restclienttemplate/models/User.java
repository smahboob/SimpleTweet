package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public String id;
    public String name;
    public String screen_name;
    public String location;
    public String publicImageUrl;

    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.id = jsonObject.getString("id_str");
        user.name = jsonObject.getString("name");
        user.screen_name = jsonObject.getString("screen_name");
        user.location = jsonObject.getString("location");
        user.publicImageUrl = jsonObject.getString("profile_image_url_https");
        return user;
    }}
