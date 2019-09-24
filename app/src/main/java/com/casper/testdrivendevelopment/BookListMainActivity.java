package com.casper.testdrivendevelopment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class BookListMainActivity extends AppCompatActivity {

    private ArrayList<Book> theBooks;
    private ListView listViewSuper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        InitData();

        listViewSuper = (ListView)this.findViewById(R.id.list_view_books);
        ArrayAdapter<Book> theAdapter = new BooksArrayAdapter(this, R.layout.book_list,theBooks);
        listViewSuper.setAdapter(theAdapter);
    }

    private void InitData(){
        theBooks = new ArrayList<Book>();
        theBooks.add(new Book("软件项目管理案例教程（第4版）",R.drawable.book_2));
        theBooks.add(new Book("创新工程实践",R.drawable.book_no_name));
        theBooks.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
    }

    protected class BooksArrayAdapter extends ArrayAdapter<Book>
    {
        private int resourceid;

        public BooksArrayAdapter(@NonNull Context context, int resource, @NonNull List<Book> objects) {
            super(context, resource, objects);
            this.resourceid = resource;
        }

        @NonNull
        @Override

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater= LayoutInflater.from(this.getContext());
            View book = mInflater.inflate(this.resourceid,null);

            ImageView img = (ImageView)book.findViewById(R.id.image_view_book_cover);
            TextView name = (TextView)book.findViewById(R.id.text_view_book_title);

            Book book_item = this.getItem(position);
            img.setImageResource(book_item.getCoverResourceId());
            name.setText(book_item.getTitle());

            return book;
        }
    }
}
