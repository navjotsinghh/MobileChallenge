package paytmlabs.codechallenge.navjotsingh.currencyconverter.api;

import android.content.Context;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * API Client for fetching latest currency conversion rates
 * <p>
 * Created by navjotsingh on 15/07/17.
 */

public class ApiClient {

    private final String BASE_URL = "https://api.fixer.io/";

    private Retrofit retrofit = null;

    private Cache cache;

    private OkHttpClient okHttpClient;

    private int cacheSize = 10 * 1024 * 1024; // 10 MB

    private static ApiClient apiClientInstance = null;

    private ApiClient(Context context) {
        cache = new Cache(context.getCacheDir(), cacheSize);
        okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    /**
     * Method to get the singleton instance of the class
     *
     * @param context {@link Context}
     * @return ApiClient instance
     */
    public static ApiClient getInstance(Context context) {
        if (apiClientInstance == null) {
            apiClientInstance = new ApiClient(context);
        }
        return apiClientInstance;
    }

    /**
     * Method to return {@link Retrofit} client
     *
     * @return Retrofit client
     */
    public Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
