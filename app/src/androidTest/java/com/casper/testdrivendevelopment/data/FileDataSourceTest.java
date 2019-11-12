package com.casper.testdrivendevelopment.data;

import android.app.Application;
import android.app.ApplicationErrorReport;
import android.app.Instrumentation;
import android.content.pm.ApplicationInfo;
import android.support.test.InstrumentationRegistry;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;

import com.casper.testdrivendevelopment.R;
import com.casper.testdrivendevelopment.data.model.Book;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FileDataSourceTest {
    private Context context;
    private FileDataSource keeper;  //保存测试前的环境
    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        keeper=new FileDataSource(context);
        keeper.load();
    }     //

    @After
    public void tearDown() throws Exception {
        keeper.save();
    }

    @Test
    public void getBooks() throws Exception {
        FileDataSource fileDataSource=new FileDataSource(context);
        Assert.assertEquals(0,fileDataSource.getBooks().size());
    }

    @Test
    public void saveAndloader() {
        FileDataSource fileDataSource=new FileDataSource(context);
        Assert.assertEquals(0,fileDataSource.getBooks().size());
        fileDataSource.getBooks().add(new Book("测试1", R.drawable.book_1));
        fileDataSource.getBooks().add(new Book("测试2", R.drawable.book_2));
        fileDataSource.save();
        FileDataSource fileLoader=new FileDataSource(context);
        fileLoader.load();

        Assert.assertNotEquals(fileDataSource.getBooks(),fileLoader.getBooks());
        Assert.assertEquals(fileDataSource.getBooks().size(),fileLoader.getBooks().size());

        for (int i=0;i<fileDataSource.getBooks().size();i++){
            Book bookThis = fileDataSource.getBooks().get(i);
            Book bookThat = fileLoader.getBooks().get(i);
            Assert.assertEquals(bookThat.getCoverResourceId(),bookThis.getCoverResourceId());
            Assert.assertEquals(bookThat.getTitle(),bookThis.getTitle());
        }
    }

    @Test
    public void load() {
        FileDataSource fileDataSource=new FileDataSource(context);
        Assert.assertEquals(0,fileDataSource.getBooks().size());
        fileDataSource.save();
        FileDataSource fileLoader=new FileDataSource(context);
        fileLoader.load();
        Assert.assertEquals(fileDataSource.getBooks().size(),fileLoader.getBooks().size());
        Assert.assertEquals(0,fileDataSource.getBooks().size());
    }
}