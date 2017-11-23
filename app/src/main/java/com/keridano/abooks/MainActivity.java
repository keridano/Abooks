package com.keridano.abooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.gson.Gson;
import com.keridano.abooks.api.GoogleBooksAPI;
import com.keridano.abooks.constant.AppConstants;
import com.keridano.abooks.fragment.BooksListFragment;
import com.keridano.abooks.model.Book;
import com.keridano.abooks.model.BookQueryResult;

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

    private GoogleBooksAPI          googleBooksAPI;
    private BooksListFragment       booksListFragment;
    private SharedPreferences       mPreferences;
    private ProgressBar             mProgress;
    private Toolbar                 mToolbar;
    private FloatingActionButton    mFab;
    private String                  lastQueryString = "harry Potter";

    //region Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setupView();
        initApi();
        searchBooks(lastQueryString);

        this.booksListFragment = BooksListFragment.newInstance(2);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, booksListFragment).commit();

        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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

    @Override
    public void onListFragmentInteraction(Book item) {

        Gson    gson                    = new Gson();
        Intent  detailActivityIntent    = new Intent(this, DetailActivity.class);
        detailActivityIntent.putExtra(AppConstants.BOOK_DETAIL, gson.toJson(item, Book.class));
        startActivity(detailActivityIntent);

    }
    //endregion

    //region Public Methods
    public void searchBooks(final String queryString) {

        mProgress.setVisibility(View.VISIBLE);

        googleBooksAPI.bookSearch(queryString, getString(R.string.apiKey)).enqueue(new Callback<BookQueryResult>() {
            @Override
            public void onResponse(@NonNull Call<BookQueryResult> call, @NonNull Response<BookQueryResult> response) {

                showTutorial();
                if(response.body() != null) {

                    booksListFragment.updateBooksList(response.body().getItems(), false);
                    lastQueryString = queryString;

                }
                mProgress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(@NonNull Call<BookQueryResult> call, @NonNull Throwable t) {

                showTutorial();
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

    public void showTutorial(){

        boolean isFirstStart = this.mPreferences.getBoolean(AppConstants.IS_FIRST_START, true);
        if(isFirstStart && this.booksListFragment.getView() != null) {

            new TapTargetSequence(this).targets(
                    TapTarget.forToolbarMenuItem(this.mToolbar, R.id.action_search, "Search your favourite books", "Tap on the search button to search for books title, auhors, publisher and much more")
                            .drawShadow(true)
                            .tintTarget(true)
                            .cancelable(false)
                            .icon(getDrawable(R.drawable.ic_search)),
                    TapTarget.forView(this.mFab, "This is the barcode scanner", "tap on it to scan an ISBN code and search for a book")
                            .drawShadow(true)
                            .tintTarget(true)
                            .cancelable(false)
                            .icon(getDrawable(R.drawable.ic_barcode_scan)),
                    TapTarget.forView(this.booksListFragment.getView(), "This is the book shelf", "Tap on a book to get the details")
                            .drawShadow(true)
                            .tintTarget(true)
                            .cancelable(false)
                            .icon(getDrawable(R.drawable.ic_book))
            ).listener(new TapTargetSequence.Listener() {
                           @Override
                           public void onSequenceFinish() {

                               SharedPreferences.Editor editor = mPreferences.edit();
                               editor.putBoolean(AppConstants.IS_FIRST_START, false);
                               editor.apply();

                           }

                           @Override
                           public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                           }

                           @Override
                           public void onSequenceCanceled(TapTarget lastTarget) {

                           }
                       }

            ).start();

        }

    }
    //endregion

    //region Private Methods
    private void setupView() {

        setContentView(R.layout.activity_main);

        this.mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.mToolbar);

        this.mFab = findViewById(R.id.fab);
        this.mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
    //endregion

}
