package com.keridano.abooks.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keridano.abooks.MainActivity;
import com.keridano.abooks.R;
import com.keridano.abooks.adapter.BooksRecyclerViewAdapter;
import com.keridano.abooks.adapter.EndlessRecyclerViewScrollListener;
import com.keridano.abooks.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Books.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BooksListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private List<Book>                          mBooks          = new ArrayList<>();
    private int                                 mColumnCount    = 1;
    private OnListFragmentInteractionListener   mListener;
    private EndlessRecyclerViewScrollListener   mEndlessListener;
    private BooksRecyclerViewAdapter            mAdapter;

    public BooksListFragment() {}

    public static BooksListFragment newInstance(int columnCount) {

        BooksListFragment   fragment    = new BooksListFragment();
        Bundle              args        = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {

            Context             context         = view.getContext();
            RecyclerView        recyclerView    = (RecyclerView) view;
            LinearLayoutManager llManager       = null;
            GridLayoutManager   glManager       = null;

            if (mColumnCount <= 1) {

                llManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(llManager);

            } else {

                glManager = new GridLayoutManager(context, mColumnCount);
                recyclerView.setLayoutManager(glManager);

            }

            mEndlessListener = new EndlessRecyclerViewScrollListener(llManager != null ? llManager : glManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    ((MainActivity)getActivity()).booksSearchPagination(totalItemsCount);
                }

            };

            mAdapter = new BooksRecyclerViewAdapter(getActivity(), mBooks, mListener);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addOnScrollListener(mEndlessListener);

        }

        return view;

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof OnListFragmentInteractionListener)
            mListener = (OnListFragmentInteractionListener) context;
        else
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");

    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;

    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Book item);
    }

    public void updateBooksList(List<Book> books, boolean isAppend) {

        if(!isAppend) {

            mBooks.clear();
            mEndlessListener.resetState();

        }

        mBooks.addAll(books);
        mAdapter.notifyDataSetChanged();

    }

}
