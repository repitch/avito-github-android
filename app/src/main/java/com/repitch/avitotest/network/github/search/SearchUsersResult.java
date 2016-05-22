package com.repitch.avitotest.network.github.search;

import com.google.gson.annotations.SerializedName;
import com.repitch.avitotest.network.models.GitHubRepo;
import com.repitch.avitotest.network.models.GitHubUser;

import java.util.List;

/**
 * Created by repitch on 21.05.16.
 */
public class SearchUsersResult extends SearchResult {

    @SerializedName("items")
    public List<GitHubUser> items;

}
