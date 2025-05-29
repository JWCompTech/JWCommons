package com.jwcomptech.commons.webapis.generators;

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

import com.jwcomptech.commons.webapis.services.GitHubUserService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("unused")
public final class GitHubServiceGenerator {

    private static final String BASE_URL = "https://api.github.com/";

    private static final Retrofit.Builder builder
            = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static final HttpLoggingInterceptor logging
            = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);

    public static <S> @NotNull S createService(final Class<S> serviceClass) {
        Retrofit retrofit = builder.build();
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }

    public static <S> @NotNull S createService(final Class<S> serviceClass, final String token) {
        Retrofit retrofit = builder.build();
        if (null != token) {
            httpClient.interceptors().clear();
            httpClient.addInterceptor(chain -> {
                Request.Builder authBuilder = chain.request().newBuilder()
                        .header("Authorization", token);
                Request request = authBuilder.build();
                return chain.proceed(request);
            });
            builder.client(httpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }

    public static @NotNull GitHubUserService createUserService() {
        return createService(GitHubUserService.class);
    }

    public static @NotNull GitHubUserService createUserService(final String token) {
        return createService(GitHubUserService.class, token);
    }

    /** Prevents instantiation of this utility class. */
    private GitHubServiceGenerator() { }
}
