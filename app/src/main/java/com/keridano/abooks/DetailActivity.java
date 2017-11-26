package com.keridano.abooks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.keridano.abooks.constant.AppConstants;
import com.keridano.abooks.model.Book;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import static android.view.View.GONE;

public class DetailActivity extends AppCompatActivity {

    private Book        mBook;
    private ImageView   mCoverImage;
    private TextView    mPublisher;
    private TextView    mDate;
    private TextView    mTitle;
    private TextView    mDescription;
    private TextView    mCategoryLabel;
    private TextView    mCategories;
    private TextView    mAverageRating;
    private TextView    mPreviewLink;
    private TextView    mInfoLink;
    private TextView    mBuyLink;

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

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.mCoverImage        = findViewById(R.id.coverImage);
        this.mPublisher         = findViewById(R.id.publisher);
        this.mDate              = findViewById(R.id.date);
        this.mTitle             = findViewById(R.id.title);
        this.mDescription       = findViewById(R.id.description);
        this.mCategoryLabel     = findViewById(R.id.categoryLabel);
        this.mCategories        = findViewById(R.id.categories);
        this.mAverageRating     = findViewById(R.id.averageRating);
        this.mPreviewLink       = findViewById(R.id.previewLink);
        this.mInfoLink          = findViewById(R.id.infoLink);
        this.mBuyLink           = findViewById(R.id.buyLink);

        if(this.mBook.getVolumeInfo() != null) {

            if (this.mBook.getVolumeInfo().getImageLinks() != null && this.mBook.getVolumeInfo().getImageLinks().getThumbnail() != null) {

                Picasso.with(this)
                        .load(this.mBook.getVolumeInfo().getImageLinks().getThumbnail())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(this.mCoverImage);

            } else
                this.mCoverImage.setImageResource(R.drawable.book_placeholder);

            this.mPublisher.setText(this.mBook.getVolumeInfo().getPublisher());
            this.mDate.setText(this.mBook.getVolumeInfo().getPublishedDate());
            this.mTitle.setText(this.mBook.getVolumeInfo().getTitle());
            this.mAverageRating.setText(getString(R.string.average_rating, this.mBook.getVolumeInfo().getAverageRating()));

            if (TextUtils.isEmpty(this.mBook.getVolumeInfo().getDescription()))
                this.mDescription.setText(getString(R.string.no_description));
            else
                this.mDescription.setText(this.mBook.getVolumeInfo().getDescription());

            if (this.mBook.getVolumeInfo().getCategories() != null) {

                StringBuilder builder = new StringBuilder();
                for (String category : this.mBook.getVolumeInfo().getCategories()) {
                    builder.append(category).append(" ");
                }
                this.mCategories.setText(builder.toString());

            } else
                this.mCategoryLabel.setVisibility(GONE);

            String previewUrl = this.mBook.getVolumeInfo().getPreviewLink();
            if (previewUrl != null) {

                this.mPreviewLink.setText(setupHrefLink(previewUrl, getString(R.string.preview_link)));
                this.mPreviewLink.setMovementMethod(LinkMovementMethod.getInstance());

            } else
                this.mPreviewLink.setVisibility(GONE);

            String infoUrl = this.mBook.getVolumeInfo().getInfoLink();
            if(infoUrl != null) {

                this.mInfoLink.setText(setupHrefLink(infoUrl, getString(R.string.info_link)));
                this.mInfoLink.setMovementMethod(LinkMovementMethod.getInstance());

            } else
                this.mInfoLink.setVisibility(GONE);

        }

        if(this.mBook.getSaleInfo() != null && this.mBook.getSaleInfo().getBuyLink() != null) {

            String buyUrl = this.mBook.getSaleInfo().getBuyLink();
            this.mBuyLink.setText(setupHrefLink(buyUrl, getString(R.string.buy_link)));
            this.mBuyLink.setMovementMethod(LinkMovementMethod.getInstance());

        } else
            this.mBuyLink.setVisibility(GONE);

    }

    private Spanned setupHrefLink(String link, String visibleText){
        return Html.fromHtml("<a href=\"" + link + "\">" + visibleText + "</a>");
    }

}
