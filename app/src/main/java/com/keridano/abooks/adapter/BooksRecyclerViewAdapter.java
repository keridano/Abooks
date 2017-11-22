package com.keridano.abooks.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class BooksRecyclerViewAdapter extends RecyclerView.Adapter<BooksRecyclerViewAdapter.ViewHolder> {

    private Context                                 mContext;
    private List<Book>                              mBooks;
    private final OnListFragmentInteractionListener mListener;

    public BooksRecyclerViewAdapter(Context context, List<Book> books, OnListFragmentInteractionListener listener) {

        mContext    = context;
        mBooks      = books;
        mListener   = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mBooks.get(position);
        if(holder.mItem.getVolumeInfo() != null) {

            holder.mBookTitle.setText(holder.mItem.getVolumeInfo().getTitle());
            if(holder.mItem.getVolumeInfo().getImageLinks() != null) {

                Picasso.with(mContext)
                        .load(holder.mItem.getVolumeInfo().getImageLinks().getThumbnail())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(holder.mBookCover);

            } else
                holder.mBookCover.setImageResource(R.drawable.book_placeholder);

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListener != null) {

                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
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
