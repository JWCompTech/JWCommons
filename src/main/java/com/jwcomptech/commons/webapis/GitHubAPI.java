package com.jwcomptech.commons.webapis;

/*-
 * #%L
 * JWCT Commons
 * %%
 * Copyright (C) 2025 JWCompTech
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.jwcomptech.commons.SingletonManager;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.Map;

@Data
@SuppressWarnings("unused")
public final class GitHubAPI {
    //private final GitHubUserService gitHubUserService;
    private GitHub gitHub;

    public static GitHubAPI getInstance() {
        return SingletonManager.getInstance(GitHubAPI.class, GitHubAPI::new);
    }

    private GitHubAPI() {
        //this.gitHubUserService = GitHubServiceGenerator.createUserService();
        try {
            gitHub = GitHub.connectAnonymously();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //My username is "jlwisedev"
    public GHUser getUser(final String username) throws IOException {
        return gitHub.getUser(username);
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

    //    @GET("/users")
//    public List<GitHubUser> getUsers(final int per_page, final int page) throws IOException {
//        return gitHubUserService.getUsers(per_page, page).execute().body();
//    }
//
//    @GET("/users/{username}")
//    public GitHubUser getUser(final String username) throws IOException {
//        return gitHubUserService.getUser(username).execute().body();
//    }
//
//    public GitHubUserService getGitHubUserService() {
//        return gitHubUserService;
//    }
}
