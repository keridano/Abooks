package com.keridano.abooks.api;

import com.keridano.abooks.model.BookQueryResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author enrico.sallusti
 */
public interface GoogleBooksAPI {

    @GET("/books/v1/volumes")
    Call<BookQueryResult> bookSearch(@Query("q") String query, @Query("key") String apiKey);

}
