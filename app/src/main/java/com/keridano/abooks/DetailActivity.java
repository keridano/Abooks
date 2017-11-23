package com.keridano.abooks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.keridano.abooks.constant.AppConstants;
import com.keridano.abooks.model.Book;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private Book        mBook;
    private ImageView   mCoverImage;
    private TextView    mDate;
    private TextView    mTitle;
    private TextView    mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Gson    gson        = new Gson();
        String  bookJson    = getIntent().getStringExtra(AppConstants.BOOK_DETAIL);
        this.mBook          = gson.fromJson(bookJson, Book.class);

        setupView();

    }

    private void setupView() {

        setContentView(R.layout.activity_detail);
        Slidr.attach(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.mCoverImage    = findViewById(R.id.coverImage);
        this.mDate          = findViewById(R.id.date);
        this.mTitle         = findViewById(R.id.title);
        this.mDescription   = findViewById(R.id.description);

        Picasso.with(this)
                .load(this.mBook.getVolumeInfo().getImageLinks().getThumbnail())
                .placeholder(R.mipmap.ic_launcher)
                .into(this.mCoverImage);

        this.mDate.setText(this.mBook.getVolumeInfo().getPublishedDate());
        this.mTitle.setText(this.mBook.getVolumeInfo().getTitle());
        this.mDescription.setText(this.mBook.getVolumeInfo().getDescription());

    }


}
