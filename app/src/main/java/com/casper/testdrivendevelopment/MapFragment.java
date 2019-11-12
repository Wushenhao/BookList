package com.casper.testdrivendevelopment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.casper.testdrivendevelopment.data.ShopLoader;
import com.casper.testdrivendevelopment.data.model.Shop;

import static java.sql.Types.NULL;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private MapView mapView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) view.findViewById(R.id.map_View);

        BaiduMap baiduMap = mapView.getMap();
        LatLng centerPoint = new LatLng(22.2559,113.541112); //修改百度地图的初始位置
        MapStatus mMapStatus = new MapStatus.Builder().target(centerPoint).zoom(17).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        baiduMap.setMapStatus(mMapStatusUpdate);

        //添加标记点
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon);
        MarkerOptions markerOption = new MarkerOptions().icon(bitmap).position(centerPoint);
        Marker marker = (Marker) baiduMap.addOverlay(markerOption);//添加文字
        OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(50).fontColor(0xFFFF00FF).text("暨南大学珠海").rotate(0).position(centerPoint);
        baiduMap.addOverlay(textOption);//响应事件

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                   Toast.makeText(getContext(), "Marker被点击了！", Toast.LENGTH_SHORT).show();
                   return false;
            }
        });

        downloadAndDrawShops();

        return view;
    }

    private void downloadAndDrawShops() {
        final ShopLoader shopLoader=new ShopLoader();

        Handler handler=new Handler(){
            public void handleMessage(Message msg){
                if (mapView == null ) return;
                BaiduMap mbaiduMap = mapView.getMap();

                for (int i=0;i<shopLoader.getShops().size();i++){
                    Shop shop=shopLoader.getShops().get(i);    //获得shopLoader中的ArrayList<Shop>中位置为i的Shop型对象
                    LatLng centerPoint = new LatLng(shop.getLatitude(),shop.getLongitude()); //修改百度地图的初始位置
                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon);
                    MarkerOptions markerOption = new MarkerOptions().icon(bitmap).position(centerPoint);
                    Marker marker = (Marker) mbaiduMap.addOverlay(markerOption);//添加文字

                    OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(50).fontColor(0xFFFF00FF).text(shop.getName()).rotate(0).position(centerPoint);
                    mbaiduMap.addOverlay(textOption);//响应事件
                }
            };
        };    //创建一个handler方法的对象，在load中调用
        shopLoader.load(handler,"http://file.nidama.net/class/mobile_develop/data/bookstore.json");       //开启一个新的线程用来读取数据，异步处理消息，然后再返回主进程通知handler方法在地图上更新标记；
                                                                                                                // 1.子线程从网络下载数据 2.子线程通过sendMessage发送数据给主线程，此处为空 3.handler实现UI的更新
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if(mapView!=null)mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if(mapView!=null)mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(mapView!=null)mapView.onDestroy();
    }
}
