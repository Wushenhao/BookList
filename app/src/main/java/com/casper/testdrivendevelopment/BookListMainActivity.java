package com.casper.testdrivendevelopment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class BookListMainActivity extends AppCompatActivity {

    private ArrayList<Book> theBooks;
    private ListView listViewSuper;
    private BooksArrayAdapter theAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        InitData();

        listViewSuper = (ListView)this.findViewById(R.id.list_view_books);
        theAdapter = new BooksArrayAdapter(this, R.layout.book_list,theBooks);
        listViewSuper.setAdapter(theAdapter);
        this.registerForContextMenu(listViewSuper); //为控件注册场景菜单
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v==listViewSuper) {
            int itemPosition=((AdapterView.AdapterContextMenuInfo)menuInfo).position;
            menu.setHeaderTitle(theBooks.get(itemPosition).getTitle());  //前两行在点击时判断位置
            menu.add(0, 1, 0, "新建");
            menu.add(0, 2, 0, "删除");
            menu.add(0, 3, 0, "关于...");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 1: {
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

                theBooks.add(menuInfo.position+1, new Book("New Book",R.drawable.new_book));
                theAdapter.notifyDataSetChanged(); //通知adapter底层数据已改变，修改数据
                Toast.makeText(this, "你选择了新建", Toast.LENGTH_SHORT).show();

                break;
            }
            case 2: {
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final int itemPosition=menuInfo.position;

                new android.app.AlertDialog.Builder(this)
                        .setTitle("询问")
                        .setMessage("你确定要删除这本书吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                theBooks.remove(itemPosition);
                                theAdapter.notifyDataSetChanged(); //通知adapter底层数据已改变，修改数据
                                Toast.makeText(BookListMainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })   //点击确定来删除
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //取消则不做任何操作
                            }
                        })
                        .create().show();
                break;
            }
            case 3:
                Toast.makeText(this, "版权所有by Wushenhao", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
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
            name.setText(book_item.getTitle()); //设定当前的itemview中所有控件的数据为资源数据；通过book中的getter函数，获取当前的资源数据。
            return book;
        }
    }
}
