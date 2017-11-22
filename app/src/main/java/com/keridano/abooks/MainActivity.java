package com.keridano.abooks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.keridano.abooks.api.GoogleBooksAPI;
import com.keridano.abooks.fragment.BooksListFragment;
import com.keridano.abooks.model.BookQueryResult;

import java.lang.reflect.Type;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final static String TAG    = MainActivity.class.getSimpleName();

    private TextView            sampleText;
    private GoogleBooksAPI      googleBooksAPI;
    private BooksListFragment   booksListFragment;

    //region Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupView();
        initApi();
        searchBooks("harry potter");

        booksListFragment = BooksListFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, booksListFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    //endregion

    //region Private Methods
    private void setupView(){
        this.sampleText = findViewById(R.id.sampleText);
    }

    private void initApi() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleBooksAPI = retrofit.create(GoogleBooksAPI.class);

    }

    private void searchBooks(String queryString) {

        googleBooksAPI.bookSearch(queryString, getString(R.string.apiKey)).enqueue(new Callback<BookQueryResult>() {
            @Override
            public void onResponse(@NonNull Call<BookQueryResult> call, @NonNull Response<BookQueryResult> response) {

                Gson gson = new Gson();
                sampleText.setText(gson.toJson(response.body(), BookQueryResult.class));

            }

            @Override
            public void onFailure(@NonNull Call<BookQueryResult> call, @NonNull Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }

        });

    }
    //endregion

}
