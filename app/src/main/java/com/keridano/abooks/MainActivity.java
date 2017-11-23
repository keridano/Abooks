package com.keridano.abooks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.keridano.abooks.api.GoogleBooksAPI;
import com.keridano.abooks.fragment.BooksListFragment;
import com.keridano.abooks.model.Book;
import com.keridano.abooks.model.BookQueryResult;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BooksListFragment.OnListFragmentInteractionListener {

    private final static String TAG    = MainActivity.class.getSimpleName();

    private GoogleBooksAPI      googleBooksAPI;
    private BooksListFragment   booksListFragment;
    private ProgressBar         mProgress;
    private String              lastQueryString = "harry Potter";

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
        searchBooks(lastQueryString);

        booksListFragment = BooksListFragment.newInstance(2);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, booksListFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchMenuItem   = menu.findItem(R.id.action_search);
        final SearchView searchView     = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                searchBooks(query);
                searchView.clearFocus();
                searchMenuItem.collapseActionView();
                searchView.setIconified(true);

                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

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
        this.mProgress = findViewById(R.id.progress);
    }

    private void initApi() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        googleBooksAPI = retrofit.create(GoogleBooksAPI.class);

    }

    public void searchBooks(final String queryString) {

        mProgress.setVisibility(View.VISIBLE);

        googleBooksAPI.bookSearch(queryString, getString(R.string.apiKey)).enqueue(new Callback<BookQueryResult>() {
            @Override
            public void onResponse(@NonNull Call<BookQueryResult> call, @NonNull Response<BookQueryResult> response) {

                if(response.body() != null) {

                    booksListFragment.updateBooksList(response.body().getItems(), false);
                    lastQueryString = queryString;

                }
                mProgress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NonNull Call<BookQueryResult> call, @NonNull Throwable t) {

                mProgress.setVisibility(View.GONE);
                Log.e(TAG, t.getMessage(), t);

            }

        });

    }

    public void booksSearchPagination(int page) {

        String startIndex = page > 0 ? Integer.valueOf((page*10)-1).toString() : "0";

        googleBooksAPI.bookSearch(lastQueryString, getString(R.string.apiKey), startIndex).enqueue(new Callback<BookQueryResult>() {
            @Override
            public void onResponse(@NonNull Call<BookQueryResult> call, @NonNull Response<BookQueryResult> response) {

                if(response.body() != null) {
                    booksListFragment.updateBooksList(response.body().getItems(), true);
                }

            }

            @Override
            public void onFailure(@NonNull Call<BookQueryResult> call, @NonNull Throwable t) {

                Log.e(TAG, t.getMessage(), t);
                booksListFragment.updateBooksList(null, true);

            }

        });

    }

    @Override
    public void onListFragmentInteraction(Book item) {
        // TODO: 22/11/2017
    }
    //endregion

}
