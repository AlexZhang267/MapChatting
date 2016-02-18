package com.fudan.android.mapchatting.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;




import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.fudan.android.mapchatting.R;
import com.fudan.android.mapchatting.config.Config;
import com.fudan.android.mapchatting.entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * AMapV2地图中介绍定位三种模式的使用，包括定位，追随，旋转
 */
public class MainActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener,OnCheckedChangeListener,NavigationView.OnNavigationItemSelectedListener {
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private RadioGroup mGPSModeGroup;

    private AMap.OnMarkerClickListener onMarkerClickListener;

    public static final String TAG = "MainActivity";

    //this should be wrote in sever
    Map hashMap= new HashMap<Marker, User>();

//    private TextView mLocationErrText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
        onMarkerClickListener = new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                User user = (User)hashMap.get(marker);

                //this is just for test, i will remove it in a while
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));



                if(user != null){
                    Log.d(TAG, "User name is" + user.getName());

                    Intent intent =  new Intent(getApplicationContext(), DiscussionActivity.class);
                    intent.putExtra(Config.KEY_DISCUSSIONID,1);
                    intent.putExtra(Config.KEY_DISCUSSION,"Discussion from map");
                    intent.putExtra(Config.KEY_PHONE_NUM,"11222233");
                    intent.putExtra(Config.KEY_USER, user);
                    startActivity(intent);

                }
                return false;
            }
        };
        aMap.setOnMarkerClickListener(onMarkerClickListener);
        MarkerOptions markerOptions = getTestMarkerOption();
        addMarker(markerOptions);

        mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
        mGPSModeGroup.setOnCheckedChangeListener(this);
//        mLocationErrText = (TextView)findViewById(R.id.location_errInfo_text);
//        mLocationErrText.setVisibility(View.GONE);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.gps_locate_button:
                // 设置定位的类型为定位模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
                break;
            case R.id.gps_follow_button:
                // 设置定位的类型为 跟随模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
                break;
            case R.id.gps_rotate_button:
                // 设置定位的类型为根据地图面向方向旋转
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                break;
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
//                mLocationErrText.setVisibility(View.GONE);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
//                mLocationErrText.setVisibility(View.VISIBLE);
//                mLocationErrText.setText(errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

//    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Log.d(TAG,"onNavigationItemSelected");

        if (id == R.id.nav_map_chat) {
            Intent i = new Intent(this, DisListActivity.class);
            //test by alex
            String token = "Alex";
            String phone_num = "15121106182";
            i.putExtra(Config.KEY_TOKEN, token);
            i.putExtra(Config.KEY_PHONE_NUM, phone_num);
            startActivity(i);

            // itself
        } else if (id == R.id.nav_my_chat) {

        } else if (id == R.id.nav_near_me) {
            String token = Config.getCachedToken(this);
            String phone_num = Config.getCachedPhoneNum(this);

            if (token != null && phone_num != null) {
                Intent i = new Intent(this, DisListActivity.class);
                i.putExtra(Config.KEY_TOKEN, token);
                i.putExtra(Config.KEY_PHONE_NUM, phone_num);
                startActivity(i);
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }

        } else if (id == R.id.nav_recommend) {

        } else if (id == R.id.nav_private_chat) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addMarker(MarkerOptions markerOptions){
        Marker marker = aMap.addMarker(markerOptions);
//        marker.setTitle("西安市！！");
        marker.showInfoWindow();

        User user = new User("TEST!");

        //send marker and user to sever, them map them
        hashMap.put(marker,user);
    }

    public MarkerOptions getTestMarkerOption(){
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latLng = new LatLng(30.341568, 117.940174);
        markerOptions.title("快来按我！");
        markerOptions.position(latLng);
        markerOptions.draggable(true);
//        markerOptions.setFlat(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
        return markerOptions;
    }
}
