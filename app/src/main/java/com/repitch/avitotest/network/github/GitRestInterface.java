package com.repitch.avitotest.network.github;

import com.repitch.avitotest.network.github.search.SearchRepositoriesResult;
import com.repitch.avitotest.network.github.search.SearchUsersResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by repitch on 21.05.16.
 */
public interface GitRestInterface {

    @GET("search/repositories")
    Call<SearchRepositoriesResult> searchRepos(@Query("q") String query,
                                                      @Query("sort") String sort,
                                                      @Query("order") String order);

    @GET("search/users")
    Call<SearchUsersResult> searchUsers(@Query("q") String query,
                                        @Query("sort") String sort,
                                        @Query("order") String order);
}
