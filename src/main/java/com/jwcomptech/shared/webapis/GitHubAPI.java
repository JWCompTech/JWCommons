package com.jwcomptech.shared.webapis;

import com.jwcomptech.shared.utils.SingletonUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.Map;

@SuppressWarnings("unused")
public final class GitHubAPI {
    //private final GitHubUserService gitHubUserService;
    private GitHub gitHub;

    public static GitHubAPI getInstance() {
        return SingletonUtils.getInstance(GitHubAPI.class, GitHubAPI::new);
    }

    private GitHubAPI() {
        //this.gitHubUserService = GitHubServiceGenerator.createUserService();
        try {
            gitHub = GitHub.connectAnonymously();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GHUser getUser(final String username) throws IOException {
        return gitHub.getUser("jlwisedev");
    }

    public GHRepository getRepository(final String name) throws IOException {
        return gitHub.getRepository(name);
    }

    public Map<String, GHRepository> getMyRepositories() throws IOException {
        return gitHub.getMyself().getRepositories();
    }

    public GHPersonSet<GHOrganization> getMyOrganizations() throws IOException {
        return gitHub.getMyself().getOrganizations();
    }

    public GHOrganization getOrganization(final String name) throws IOException {
        return gitHub.getOrganization(name);
    }

    public Map<String, GHRepository> getOrganizationRepositories(final String org) throws IOException {
        return gitHub.getOrganization(org).getRepositories();
    }

    public GHRepository getOrganizationRepository(final String org, final String name) throws IOException {
        return gitHub.getOrganization(org).getRepository(name);
    }

    public GitHubAPI login() throws IOException {
        gitHub = GitHub.connect();
        return this;
    }

    public GitHubAPI login(final String login, final String oauthAccessToken) throws IOException {
        gitHub = GitHub.connect(login, oauthAccessToken);
        return this;
    }

    public GitHubAPI logout() throws IOException {
        gitHub = GitHub.connectAnonymously();
        return this;
    }

    public GitHub getGitHub() {
        return gitHub;
    }

    //    @GET("/users")
//    public List<GitHubUser> getUsers(final int per_page, final int page) throws IOException {
//        return gitHubUserService.getUsers(per_page, page).execute().body();
//    }
//
//    @GET("/users/{username}")
//    public GitHubUser getUser(String username) throws IOException {
//        return gitHubUserService.getUser(username).execute().body();
//    }
//
//    public GitHubUserService getGitHubUserService() {
//        return gitHubUserService;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        GitHubAPI gitHubApi = (GitHubAPI) o;

        return new EqualsBuilder()
                //.append(gitHubUserService, gitHubApi.gitHubUserService)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                //.append(gitHubUserService)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                //.append("gitHubUserService", gitHubUserService)
                .toString();
    }
}
