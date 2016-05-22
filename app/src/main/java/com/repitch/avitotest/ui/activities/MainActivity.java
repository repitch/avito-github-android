package com.repitch.avitotest.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.repitch.avitotest.Constants;
import com.repitch.avitotest.R;
import com.repitch.avitotest.network.RFManagerGitHub;
import com.repitch.avitotest.network.github.search.SearchRepositoriesResult;
import com.repitch.avitotest.network.github.search.SearchUsersResult;
import com.repitch.avitotest.network.models.GitHubBaseObject;
import com.repitch.avitotest.network.models.GitHubRepo;
import com.repitch.avitotest.network.models.GitHubUser;
import com.repitch.avitotest.ui.adapters.ReposUsersAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    static final String LOG_TAG = "SearchGit";
    private RecyclerView mRvRepos;
    private ViewGroup mWrapPlaceholder, mWrapResults;
    private TextView mTxtSearchResult;
    private ProgressBar mProgressBar;
    private List<GitHubRepo> mRepos = null;
    private List<GitHubUser> mUsers = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViews();
        initViews();
    }

    private void findViews() {
        mRvRepos = (RecyclerView) findViewById(R.id.rvRepos);
        mWrapPlaceholder = (ViewGroup) findViewById(R.id.wrap_placeholder);
        mWrapResults = (ViewGroup) findViewById(R.id.wrap_results);
        mTxtSearchResult = (TextView) findViewById(R.id.txt_search_result);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void initViews() {
        ReposUsersAdapter reposUsersAdapter = new ReposUsersAdapter(null);
        mRvRepos.setLayoutManager(new LinearLayoutManager(this));
        mRvRepos.setAdapter(reposUsersAdapter);
        mWrapResults.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setRepos(List<GitHubRepo> repos) {
        mRepos = repos;
        setRecycler();
    }

    void setUsers(List<GitHubUser> users) {
        mUsers = users;
        setRecycler();
    }

    private void setRecycler() {
        if (mRepos == null || mUsers == null) {
            return;
        }
        mTxtSearchResult.setText(String.format(getString(R.string.search_result), mRepos.size(), mUsers.size()));
        mProgressBar.setVisibility(View.GONE);
        // compile together 2 arrays
        List<GitHubBaseObject> objects = new ArrayList<>();
        for (int i=0; i<Math.max(mRepos.size(), mUsers.size()); i++) {
            if (i < mRepos.size()) {
                objects.add(mRepos.get(i));
            }
            if (i < mUsers.size()) {
                objects.add(mUsers.get(i));
            }
        }

        boolean noRes = objects.isEmpty();
        ((ReposUsersAdapter) mRvRepos.getAdapter()).swap(objects);
        mRvRepos.scrollToPosition(0);
        mWrapPlaceholder.setVisibility(!noRes ? View.GONE : View.VISIBLE);
        mWrapResults.setVisibility(noRes ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        makeSearchRepoRequest(newText.trim());
        return false;
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnableBounce;
    private String lastQuery = "";

    private void makeSearchRepoRequest(final String query) {
        if (mRunnableBounce != null) {
            mHandler.removeCallbacks(mRunnableBounce);
        }

        if (query == null || query.isEmpty()) {
            setRepos(new ArrayList<GitHubRepo>());
            setUsers(new ArrayList<GitHubUser>());
            return;
        }

        if (query.equals(lastQuery)) {
            // текущий запрос совпадает с последним отправленным, изменения не требуются
            return;
        }

        mRunnableBounce = new Runnable() {
            @Override
            public void run() {
                lastQuery = query;
                mProgressBar.setVisibility(View.VISIBLE);
                mRepos = null;
                mUsers = null;
                RFManagerGitHub.searchRepositories(query, new Callback<SearchRepositoriesResult>() {
                    @Override
                    public void onResponse(Call<SearchRepositoriesResult> call, Response<SearchRepositoriesResult> response) {
                        Log.d(LOG_TAG, "onResponse");
                        if (!response.isSuccess()) {
                            Toast.makeText(MainActivity.this, "Lil github problem", Toast.LENGTH_LONG).show();
                            return;
                        }
                        List<GitHubRepo> repos = response.body().items;
                        if (repos == null || repos.isEmpty()) {
                            Toast.makeText(MainActivity.this, "No repos on this search query found", Toast.LENGTH_LONG).show();
                            repos = new ArrayList<GitHubRepo>();
                        }
                        setRepos(repos);
                    }

                    @Override
                    public void onFailure(Call<SearchRepositoriesResult> call, Throwable t) {

                        Toast.makeText(MainActivity.this, "Searching repos is onFailure:'(", Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, "onFailure");

                    }
                });
                RFManagerGitHub.searchUsers(query, new Callback<SearchUsersResult>() {
                    @Override
                    public void onResponse(Call<SearchUsersResult> call, Response<SearchUsersResult> response) {

                        Log.d(LOG_TAG, "onResponse");
                        if (!response.isSuccess()) {
                            Toast.makeText(MainActivity.this, "Lil github problem", Toast.LENGTH_LONG).show();
                            return;
                        }
                        List<GitHubUser> users = response.body().items;
                        if (users == null || users.isEmpty()) {
                            Toast.makeText(MainActivity.this, "No users on this search query found", Toast.LENGTH_LONG).show();
                            users = new ArrayList<GitHubUser>();
                        }
                        setUsers(users);
                    }

                    @Override
                    public void onFailure(Call<SearchUsersResult> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Searching users is onFailure:'(", Toast.LENGTH_LONG).show();
                        Log.d(LOG_TAG, "onFailure");

                    }
                });
            }
        };
        // запускаем код с запозданием в 500 мс, чтобы не отправлять запрос сразу же
        mHandler.postDelayed(mRunnableBounce, Constants.SEARCH_BOUNCE_MS);
    }
}
