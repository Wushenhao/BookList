package com.casper.testdrivendevelopment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    public static final int CONTEXT_MENU_NEW = 1;
    public static final int CONTEXT_MENU_UPDATE = 2;
    public static final int CONTEXT_MENU_DELETE = 3;
    public static final int CONTEXT_MENU_ABOUT = 4;
    public static final int REQUEST_CODE_NEW_BOOK= 901;
    public static final int REQUEST_CODE_UPDATE_BOOK = 902;
    private ArrayList<Book> theBooks;
    private FileDataSource fileDataSource;
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
            menu.add(0, CONTEXT_MENU_NEW, 0, "新建");
            menu.add(0, CONTEXT_MENU_UPDATE, 0, "修改");
            menu.add(0, CONTEXT_MENU_DELETE, 0, "删除");
            menu.add(0, CONTEXT_MENU_ABOUT, 0, "关于...");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode) {
            case REQUEST_CODE_NEW_BOOK:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("edit_position",0);
                    String name = data.getStringExtra("book_title");

                    theBooks.add(position+1, new Book(name,R.drawable.new_book));
                    theAdapter.notifyDataSetChanged(); //通知adapter底层数据已改变，修改数据
                    Toast.makeText(this, "新建成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_UPDATE_BOOK:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("edit_position", 0);
                    String title = data.getStringExtra("book_title");

                    Book bookAtPosition=theBooks.get(position);
                    bookAtPosition.setTitle(title);
                    theAdapter.notifyDataSetChanged(); //通知adapter底层数据已改变，修改数据
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }   //从EditActivty返回后的操作

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_NEW: {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Intent intent = new Intent(BookListMainActivity.this, BookEditActivity.class);

                intent.putExtra("edit_position", menuInfo.position);
                intent.putExtra("book_title", "输入书籍名称");
                startActivityForResult(intent, REQUEST_CODE_NEW_BOOK);
                break;
            }
            case CONTEXT_MENU_UPDATE: {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                Book book = theBooks.get(menuInfo.position);

                Intent intent = new Intent(BookListMainActivity.this, BookEditActivity.class);
                intent.putExtra("edit_position", menuInfo.position);
                intent.putExtra("book_title", book.getTitle());
                startActivityForResult(intent, REQUEST_CODE_UPDATE_BOOK);
                break;
            }
            case CONTEXT_MENU_DELETE: {
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                final int itemPosition = menuInfo.position;

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
            case CONTEXT_MENU_ABOUT:
                Toast.makeText(this, "版权所有by Wushenhao", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void InitData(){
        fileDataSource=new FileDataSource(this);
        theBooks=fileDataSource.load();
        if (theBooks.size()==0) {
            theBooks.add(new Book("目前没有书",R.drawable.book_no_name));
        }
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
            TextView name = (TextView)book.findViewById(R.id.text_view_book_name);

            Book book_item = this.getItem(position);
            img.setImageResource(book_item.getCoverResourceId());
            name.setText(book_item.getTitle()); //设定当前的itemview中所有控件的数据为资源数据；通过book中的getter函数，获取当前的资源数据。
            return book;
        }
    }
}
