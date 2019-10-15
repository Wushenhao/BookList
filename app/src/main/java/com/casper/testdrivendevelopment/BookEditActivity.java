package com.casper.testdrivendevelopment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookEditActivity extends AppCompatActivity {

    private EditText editTextBookTitle;
    private Button buttonOk,buttonCancel;
    private int editPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_edit);

        editPosition=getIntent().getIntExtra("edit_position",0);

        editTextBookTitle=(EditText)findViewById(R.id.edit_text_book_title);
        buttonCancel=(Button)findViewById(R.id.button_cancel);
        buttonOk=(Button)findViewById(R.id.button_ok);

        String bookTitle= getIntent().getStringExtra("book_title");
        if(bookTitle!=null) {
            editTextBookTitle.setText(bookTitle);
        }


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("edit_position", editPosition);
                intent.putExtra("book_title", editTextBookTitle.getText().toString().trim());
                setResult(RESULT_OK, intent);
                BookEditActivity.this.finish();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookEditActivity.this.finish();
            }
        });
    }
}
