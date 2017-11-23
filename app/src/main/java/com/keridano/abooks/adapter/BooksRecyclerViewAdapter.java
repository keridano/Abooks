package com.keridano.abooks.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.keridano.abooks.R;
import com.keridano.abooks.fragment.BooksListFragment.OnListFragmentInteractionListener;
import com.keridano.abooks.model.Book;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Book} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_BOOK    = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context                                 mContext;
    private List<Book>                              mBooks;
    private final OnListFragmentInteractionListener mListener;


    public BooksRecyclerViewAdapter(Context context, List<Book> books, OnListFragmentInteractionListener listener) {

        mContext    = context;
        mBooks      = books;
        mListener   = listener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_BOOK) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book, parent, false);
            return new ViewHolder(view);

        } else if (viewType == VIEW_TYPE_LOADING) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_loading, parent, false);
            return new LoadingViewHolder(view);

        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.mItem = mBooks.get(position);
            if (viewHolder.mItem.getVolumeInfo() != null) {

                viewHolder.mBookTitle.setText(viewHolder.mItem.getVolumeInfo().getTitle());
                if (viewHolder.mItem.getVolumeInfo().getImageLinks() != null) {

                    Picasso.with(mContext)
                            .load(viewHolder.mItem.getVolumeInfo().getImageLinks().getThumbnail())
                            .placeholder(R.mipmap.ic_launcher)
                            .into(viewHolder.mBookCover);

                } else
                    viewHolder.mBookCover.setImageResource(R.drawable.book_placeholder);

            }

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mListener != null) {

                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(viewHolder.mItem);

                    }

                }
            });

        } else if (holder instanceof LoadingViewHolder) {

            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.bookProgress.setIndeterminate(true);

        }

    }

    @Override
    public int getItemCount() {
        return mBooks == null ? 0 : mBooks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position >= mBooks.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_BOOK;
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder{

        final ProgressBar bookProgress;

        LoadingViewHolder(View itemView) {

            super(itemView);
            bookProgress = itemView.findViewById(R.id.bookProgress);

        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final   View        mView;
        final   ImageView   mBookCover;
        final   TextView    mBookTitle;
                Book        mItem;

        ViewHolder(View view) {

            super(view);
            mView       = view;
            mBookCover  = view.findViewById(R.id.bookCover);
            mBookTitle  = view.findViewById(R.id.bookTitle);
        }

    }

}
