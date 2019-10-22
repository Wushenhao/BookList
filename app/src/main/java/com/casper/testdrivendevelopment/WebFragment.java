package com.casper.testdrivendevelopment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebFragment extends Fragment {
    BookListMainActivity.BooksArrayAdapter booksArrayAdapter;

    public WebFragment(BookListMainActivity.BooksArrayAdapter booksArrayAdapter) {
        this.booksArrayAdapter = booksArrayAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_book_list,container,false);
        ListView listViewSuper = (ListView)view.findViewById(R.id.list_view_books);
        listViewSuper.setAdapter(booksArrayAdapter);

        this.registerForContextMenu(listViewSuper); //为控件注册场景菜单
        // Inflate the layout for this fragment
        return view;
    }
}
