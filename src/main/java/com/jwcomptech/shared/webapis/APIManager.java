package com.jwcomptech.shared.webapis;

import com.jwcomptech.shared.utils.SingletonUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class APIManager {
    public final GitHubAPI GITHUB;

    public static APIManager getInstance() {
        return SingletonUtils.getInstance(APIManager.class, APIManager::new);
    }

    public APIManager() {
        this.GITHUB = GitHubAPI.getInstance();
    }

    public GitHubAPI gitHub() {
        return GITHUB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        APIManager that = (APIManager) o;

        return new EqualsBuilder().append(GITHUB, that.GITHUB).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(GITHUB).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("gitHubApi", GITHUB)
                .toString();
    }
}
