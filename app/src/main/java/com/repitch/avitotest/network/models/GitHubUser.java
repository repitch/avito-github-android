package com.repitch.avitotest.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by repitch on 21.05.16.
 */
public class GitHubUser extends GitHubBaseObject {

    @SerializedName("login")
    public String login; // login

    @SerializedName("avatar_url")
    public String avatarUrl; // avatar_url
}
