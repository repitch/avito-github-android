package com.repitch.avitotest.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.repitch.avitotest.Constants;
import com.repitch.avitotest.network.github.GitRestInterface;
import com.repitch.avitotest.network.github.search.SearchRepositoriesResult;
import com.repitch.avitotest.network.github.search.SearchUsersResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by repitch on 21.05.16.
 */
public class RFManagerGitHub {

    private static RFManagerGitHub sRFManager;
    private Retrofit mRetrofit;
    private GitRestInterface mRestInterface;

    public RFManagerGitHub() {
        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.GITHUB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mRestInterface = mRetrofit.create(GitRestInterface.class);
    }

    public static RFManagerGitHub getInstance() {
        if (sRFManager == null) {
            sRFManager = new RFManagerGitHub();
        }
        return sRFManager;
    }

    private Call<SearchRepositoriesResult> mCurSearchRepositoriesCall = null;

    public static void searchRepositories(String query,
                               Callback<SearchRepositoriesResult> callback) {
        Call<SearchRepositoriesResult> call = getInstance().mCurSearchRepositoriesCall;
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
        call = getInstance().mRestInterface.searchRepos(query, null, null);
        call.enqueue(callback);
    }

    private Call<SearchUsersResult> mCurSearchUsersCall = null;

    public static void searchUsers(String query,
                               Callback<SearchUsersResult> callback) {
        Call<SearchUsersResult> call = getInstance().mCurSearchUsersCall;
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
        call = getInstance().mRestInterface.searchUsers(query, null, null);
        call.enqueue(callback);
    }

}
