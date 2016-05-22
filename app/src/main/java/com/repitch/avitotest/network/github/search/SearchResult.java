package com.repitch.avitotest.network.github.search;

import com.google.gson.annotations.SerializedName;
import com.repitch.avitotest.network.models.GitHubRepo;

import java.util.List;

/**
 * Created by repitch on 21.05.16.
 */
public abstract class SearchResult {

    @SerializedName("total_count")
    public int totalCount;

    @SerializedName("incomplete_results")
    public boolean incompleteResults;
}
