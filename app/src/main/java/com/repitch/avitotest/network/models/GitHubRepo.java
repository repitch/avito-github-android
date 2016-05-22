package com.repitch.avitotest.network.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by repitch on 21.05.16.
 */
public class GitHubRepo extends GitHubBaseObject {

    @SerializedName("name")
    public String name; // name

    @SerializedName("description")
    public String description; // description

    @SerializedName("forks_count")
    public int forksCount; // forks_count
}
