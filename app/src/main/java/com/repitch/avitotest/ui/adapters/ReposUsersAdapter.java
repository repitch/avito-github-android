package com.repitch.avitotest.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.repitch.avitotest.App;
import com.repitch.avitotest.R;
import com.repitch.avitotest.network.models.GitHubBaseObject;
import com.repitch.avitotest.network.models.GitHubRepo;
import com.repitch.avitotest.network.models.GitHubUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by repitch on 22.05.16.
 */
public class ReposUsersAdapter extends RecyclerView.Adapter<ReposUsersAdapter.BaseViewHolder> {

    private static final int TYPE_REPO = 0;
    private static final int TYPE_USER = 1;

    private List<GitHubBaseObject> reposAndUsers = new ArrayList<>();

    public ReposUsersAdapter(List<GitHubBaseObject> reposAndUsers) {
        this.reposAndUsers = reposAndUsers;
    }

    public void swap(List repos) {
        if (this.reposAndUsers != null) {
            this.reposAndUsers.clear();
            if (repos != null) {
                this.reposAndUsers.addAll(repos);
            }
        } else {
            this.reposAndUsers = repos;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        GitHubBaseObject obj = reposAndUsers.get(position);
        if (obj instanceof GitHubRepo) {
            return TYPE_REPO;
        }
        if (obj instanceof GitHubUser) {
            return TYPE_USER;
        }
        return position % 2;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case TYPE_REPO:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false);
                return new RepoViewHolder(v);
            case TYPE_USER:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
                return new UserViewHolder(v);
        }
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo, parent, false);
        return new RepoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof RepoViewHolder) {
            // repos
            final GitHubRepo repo = (GitHubRepo) reposAndUsers.get(position); // position not right
            ((RepoViewHolder) holder).txtRepoName.setText(repo.name);
            ((RepoViewHolder) holder).txtRepoDescription.setText(repo.description);
            ((RepoViewHolder) holder).txtRepoForksCount.setText(String.format("%d", repo.forksCount));
        } else if (holder instanceof UserViewHolder) {
            // user
            final GitHubUser user = (GitHubUser) reposAndUsers.get(position); // position not right
            ((UserViewHolder) holder).txtUserLogin.setText(user.login);
            Picasso.with(App.getInstance())
                    .load(user.avatarUrl)
                    .into(((UserViewHolder) holder).imgUserAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return reposAndUsers == null ? 0 : reposAndUsers.size();
    }

    abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    class RepoViewHolder extends BaseViewHolder {

        public TextView txtRepoName, txtRepoDescription, txtRepoForksCount;

        public RepoViewHolder(View itemView) {
            super(itemView);
            txtRepoName = (TextView) itemView.findViewById(R.id.txt_repo_name);
            txtRepoDescription = (TextView) itemView.findViewById(R.id.txt_repo_description);
            txtRepoForksCount = (TextView) itemView.findViewById(R.id.txt_repo_forks_count);
        }
    }

    class UserViewHolder extends BaseViewHolder {

        public TextView txtUserLogin;
        public ImageView imgUserAvatar;

        public UserViewHolder(View itemView) {
            super(itemView);
            txtUserLogin = (TextView) itemView.findViewById(R.id.txt_user_login);
            imgUserAvatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
        }
    }
}
