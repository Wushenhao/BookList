package com.casper.testdrivendevelopment.data;

import com.casper.testdrivendevelopment.data.model.Shop;

import junit.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.*;

public class ShopLoaderTest {

    @Test
    public void getShops() throws Exception{
        ShopLoader shopLoader=new ShopLoader();
        Assert.assertEquals(0,shopLoader.getShops().size());     //验证后面的目标是否等于前面的值
    }

    @Test
    public void download() {
        ShopLoader shopLoader=new ShopLoader();
        String content=shopLoader.download("http://file.nidama.net/class/mobile_develop/data/bookstore.json");     //下载网站json内容，测试shopLoader加载的数据是否正确
        Assert.assertTrue(content.length()>300);
        Assert.assertTrue(content.contains("\"memo\": \"珠海二城广场\"") && content.contains("\"longitude\": \"113.526421\""));
    }

    @Test
    public void parseJson() {
        ShopLoader shopLoader=new ShopLoader();
        String content=shopLoader.download("http://file.nidama.net/class/mobile_develop/data/bookstore.json");
        Assert.assertEquals(0,shopLoader.getShops().size());
        shopLoader.parseJson(content);
        Assert.assertEquals(3,shopLoader.getShops().size());
        Shop shop=shopLoader.getShops().get(2);
        Assert.assertEquals("明珠商业广场",shop.getName());
        Assert.assertEquals("珠海二城广场",shop.getMemo());
        Assert.assertEquals(22.251953,shop.getLatitude(),1e-6);          //不能只用equals判断浮点数是否相等，应该再用一个差值判断误差
        Assert.assertEquals(113.526421,shop.getLongitude(),1e-6);
    }
}