package paytmlabs.codechallenge.navjotsingh.currencyconverter.api;

import paytmlabs.codechallenge.navjotsingh.currencyconverter.model.CurrencyRateModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * API client interface for getting currency conversion rates from network/cache
 * <p>
 * Created by navjotsingh on 15/07/17.
 */

public interface ApiInterface {

    /**
     * Method to get conversion rates from cache only and make no network call
     *
     * @param base {@link Query}
     * @return Call
     */
    @Headers("Cache-Control: public, only-if-cached, max-stale=" + Integer.MAX_VALUE)
    @GET("latest")
    Call<CurrencyRateModel> getCurrencyConversionRatesFromCacheOnly(@Query("base") String base);


    /**
     * Method to get the currency conversion rates from network after 30 minutes
     * and before that return cached response
     *
     * @param base {@link Query}
     * @return Call
     */
    @Headers("Cache-Control: public, must-revalidate, max-age=" + 30 * 60)
    @GET("latest")
    Call<CurrencyRateModel> getCurrencyConversionRates(@Query("base") String base);
//@Headers("Cache-Control: max-age=" + 30)
    //@Headers("Cache-Control: no-cache")

//                                    .header("Cache-Control", "public, max-age=" + maxAge)
//                                    .header("Cache-Control", "no-store, no-cache, must-revalidate")
//                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)

/*
    @GET("movie/{id}")
    Call<CurrencyRateModel> getMovieDetails(@Path("base") String base, @Query("api_key") String apiKey);*/

}
