package com.jwcomptech.shared.webapis.generators;

import com.jwcomptech.shared.webapis.services.GitHubUserService;
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
