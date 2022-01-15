package com.example.werewolfkiller.activity.poisearch;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.werewolfkiller.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * POI信息检索
 */
public class PoiCitySearchActivity extends AppCompatActivity
        implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener,
        BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener {

    private static final String TAG ="111111111111111111" ;
    // 地图View实例
    private MapView mMapView = null;
    LocationClient mLocationClient;

    private BaiduMap mBaiduMap = null;

    private EditText mEditTextCity = null;

    private EditText mEditTextPoi = null;

    private PoiSearch mPoiSearch = null;

    private LatLng mLatLng;

    private SuggestionSearch mSuggestionSearch = null;

    private RecyclerView mRecyclerView = null;

    private TextView mPoiTitle = null;

    private TextView mPoiAddress = null;

    private LinearLayout mLayoutDetailInfo = null;

    private PoiItemAdapter mPoiItemAdaper = null;

    private BitmapDescriptor mBitmapDescWaterDrop =
            BitmapDescriptorFactory.fromResource(R.drawable.water_drop);

    private Button mBtnSearch = null;

    private HashMap<Marker, PoiInfo> mMarkerPoiInfo = new HashMap<>();

    private Marker mPreSelectMarker = null;

    private MyTextWatcher mMyTextWatcher = new MyTextWatcher();

    // 分页
    private int mLoadIndex = 0;
    String descript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){//状态栏文字颜色随主题色变化
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        setContentView(R.layout.activity_poi_city_search_main);
        init();
        TextView textView=findViewById(R.id.finsh);
        textView.setOnClickListener(v->{
            Log.d(TAG, "onCreate: ");
            back();
            finish();
        });
    }
    public  void back(){
        Intent intent=new Intent();
        intent.putExtra("latitude",mLatLng.latitude);
        intent.putExtra("longitude",mLatLng.longitude);
        intent.putExtra("descript",mlocation);
        setResult(101,intent);
        Log.d(TAG, "onBackPressed: ");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null != mMapView) {
            mMapView.onResume();
        }
    }

    @SuppressWarnings("checkstyle:WhitespaceAround")
    @Override
    protected void onPause() {
        super.onPause();
        if (null != mMapView) {
            mMapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPoiSearch != null) {
            mPoiSearch.destroy();
        }

        if (null != mSuggestionSearch) {
            mSuggestionSearch.destroy();
        }

        if (null != mMapView) {
            mMapView.onDestroy();
        }

        if (null != mBitmapDescWaterDrop) {
            mBitmapDescWaterDrop.recycle();
        }
    }

    private void init() {
        initView();
        initMap();

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }

    private void initMap() {
        if (null == mMapView) {
            return;
        }

        mBaiduMap = mMapView.getMap();
        if (null == mBaiduMap) {
            return;
        }

        mLocationClient = new LocationClient(this);

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 高精度
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型bd09ll
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationPoiList(true);
        option.setIsNeedLocationDescribe(true);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();

        mLocationClient.registerLocationListener(myLocationListener);
        mLocationClient.start();

        // 解决圆角屏幕手机，地图logo被遮挡的问题
        mBaiduMap.setViewPadding(30, 0, 30, 20);
        mMapView.showZoomControls(false);

        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMarkerClickListener(this);
    }

    private void initView() {
        mMapView = findViewById(R.id.mapview);
        mEditTextCity = findViewById(R.id.city);
        mEditTextPoi = findViewById(R.id.poi);
        mBtnSearch = findViewById(R.id.btn_search);

        if (null == mEditTextCity || null == mEditTextPoi || null == mBtnSearch) {
            return;
        }

        mEditTextPoi.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                return false;
            }
        });

        mEditTextPoi.addTextChangedListener(mMyTextWatcher);

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPoiInCity();
            }
        });

        mRecyclerView = findViewById(R.id.poiList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if (null == mRecyclerView) {
            return;
        }
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPoiItemAdaper = new PoiItemAdapter();
        mPoiItemAdaper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SuggestionResult.SuggestionInfo suggestInfo =
                        mPoiItemAdaper.getItemSuggestInfo(position);
                locateSuggestPoi(suggestInfo);

                setPoiTextWithLocateSuggestInfo(suggestInfo);
            }
        });

        mRecyclerView.setAdapter(mPoiItemAdaper);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Utils.hideKeyBoard(PoiCitySearchActivity.this);
            }
        });

        mLayoutDetailInfo = findViewById(R.id.poiInfo);
        if (null == mLayoutDetailInfo) {
            return;
        }

        mPoiTitle = mLayoutDetailInfo.findViewById(R.id.poiTitle);
        mPoiAddress = mLayoutDetailInfo.findViewById(R.id.poiAddress);
    }

    private void searchPoiInCity() {
        String cityStr = mEditTextCity.getText().toString();
        // 获取检索关键字
        String keyWordStr = mEditTextPoi.getText().toString();
        if (TextUtils.isEmpty(cityStr) || TextUtils.isEmpty(keyWordStr)) {
            return;
        }

        if (View.VISIBLE == mRecyclerView.getVisibility()) {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
       /* mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(cityStr)
                .keyword(keyWordStr)
                .pageNum(mLoadIndex) // 分页编号
                .cityLimit(true)
                 .scope(1));*/
        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .location(mLatLng)
        .keyword(keyWordStr)
        .pageNum(mLoadIndex)
        .scope(1));

    }
    Boolean flag=false;
    String mlocation;
    public class MyLocationListener extends BDAbstractLocationListener {
        private static final String TAG ="tag" ;

        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            if (!flag){
                flag=true;
                mBaiduMap.setMyLocationData(locData);
                location.setDisToRealLocation(10);
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                mLatLng=latLng;

                MapStatus mapStatus=new MapStatus.Builder().target(latLng).zoom(20).build();
                MapStatusUpdate mapStatusUpdate= MapStatusUpdateFactory.newMapStatus(mapStatus);
                mBaiduMap.setMapStatus(mapStatusUpdate);
                Poi poi=location.getPoiList().get(0);
                mEditTextCity.setText(location.getCity());
                mEditTextPoi.setText(location.getDistrict()+poi.getName());
                mlocation=location.getDistrict()+location.getLocationDescribe();
                Log.d(TAG, "onReceiveLocation: "+location.getCity()+location.getDistrict()+location.getStreet());
                Log.d(TAG, "onReceiveLocation: "+location.getLocationDescribe());
                Toast.makeText(getApplicationContext(),poi.getName(), Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            mLoadIndex = 0;
            Toast.makeText(PoiCitySearchActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        List<PoiInfo> poiInfos = poiResult.getAllPoi();
        if (null == poiInfos) {
            return;
        }

        mRecyclerView.setVisibility(View.GONE);

        setPoiResult(poiInfos);
    }

    /**
     * @param poiDetailResult
     * @deprecated
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null
                || suggestionResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            mLoadIndex = 0;
            Toast.makeText(PoiCitySearchActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        List<SuggestionResult.SuggestionInfo> suggesInfos = suggestionResult.getAllSuggestions();
        if (null == suggesInfos) {
            return;
        }

        // 隐藏之前的
        hideInfoLayout();

        mRecyclerView.setVisibility(View.VISIBLE);

        if (null == mPoiItemAdaper) {
            mPoiItemAdaper = new PoiItemAdapter(suggesInfos);
        } else {
            mPoiItemAdaper.updateData(suggesInfos);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Utils.hideKeyBoard(this);
    }

    @Override
    public void onMapPoiClick(MapPoi mapPoi) {
        String s=mapPoi.getName();
        BDLocation location=new BDLocation();
        LatLng latLng=mapPoi.getPosition();
        mlocation=s;
        mEditTextPoi.setText(s);
        mLatLng=mapPoi.getPosition();
        searchPoiInCity();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (null == marker || null == mMarkerPoiInfo || mMarkerPoiInfo.size() <= 0) {
            return false;
        }

        Iterator itr = mMarkerPoiInfo.entrySet().iterator();
        Marker tmpMarker;
        PoiInfo poiInfo = null;
        Map.Entry<Marker, PoiInfo> markerPoiInfoEntry;
        while (itr.hasNext()) {
            markerPoiInfoEntry = (Map.Entry<Marker, PoiInfo>) itr.next();
            tmpMarker = markerPoiInfoEntry.getKey();
            if (null == tmpMarker) {
                continue;
            }

            if (tmpMarker.getId() == marker.getId()) {
                poiInfo = markerPoiInfoEntry.getValue();
                break;
            }
        }

        if (null == poiInfo) {
            return false;
        }

        InfoWindow infoWindow = getPoiInfoWindow(poiInfo);

        mBaiduMap.showInfoWindow(infoWindow);

        showPoiInfoLayout(poiInfo);

        if (null != mPreSelectMarker) {
            mPreSelectMarker.setScale(1.0f);
        }

        marker.setScale(1.5f);
        mPreSelectMarker = marker;

        return true;
    }

    /**
     * 选中某条sug检索结果时，将mEditPoi的文字设置为该sug检索结果的key
     *
     * @param suggestInfo
     */
    private void setPoiTextWithLocateSuggestInfo(SuggestionResult.SuggestionInfo suggestInfo) {
        if (null == suggestInfo) {
            return;
        }

        mEditTextPoi.removeTextChangedListener(mMyTextWatcher); // 暂时移除调TextWatcher，防止触发sug检索
        mEditTextPoi.setText(suggestInfo.getKey());
        mEditTextPoi.setSelection(suggestInfo.getKey().length()); // 将光标移到末尾
        mEditTextPoi.addTextChangedListener(mMyTextWatcher);
        mlocation=suggestInfo.address;
        mLatLng=suggestInfo.pt;
    }

    /**
     * 在地图上定位poi
     *
     * @param suggestInfo
     */
    private void locateSuggestPoi(SuggestionResult.SuggestionInfo suggestInfo) {
        if (null == suggestInfo) {
            return;
        }

        if (null == mRecyclerView || null == mMapView) {
            return;
        }

        mRecyclerView.setVisibility(View.INVISIBLE);

        LatLng latLng = suggestInfo.getPt();

        // 将地图平移到 latLng 位置
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(mapStatusUpdate);

        // 隐藏输入法
        Utils.hideKeyBoard(this);

        // 清除之前的
        clearData();

        // 显示当前的
        if (showSuggestMarker(latLng) ) {
            showPoiInfoLayout(suggestInfo);
        } else {
            setPoiTextWithLocateSuggestInfo(suggestInfo);
            searchPoiInCity();
        }
    }

    private void setPoiResult(List<PoiInfo> poiInfos) {
        if (null == poiInfos || poiInfos.size() <= 0) {
            return;
        }

        clearData();

        // 将地图平移到 latLng 位置
        LatLng latLng = poiInfos.get(0).getLocation();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(mapStatusUpdate);

        Iterator itr = poiInfos.iterator();
        List<LatLng> latLngs = new ArrayList<>();
        PoiInfo poiInfo = null;
        int i = 0;
        while (itr.hasNext()) {
            poiInfo = (PoiInfo) itr.next();
            if (null == poiInfo) {
                continue;
            }

            locatePoiInfo(poiInfo, i);
            latLngs.add(poiInfo.getLocation());
            if (0 == i) {
                showPoiInfoLayout(poiInfo);
            }

            i++;
        }

        setBounds(latLngs);
    }

    private void clearData() {
        mBaiduMap.clear();
        mMarkerPoiInfo.clear();
        mPreSelectMarker = null;
    }

    private void locatePoiInfo(PoiInfo poiInfo, int i) {
        if (null == poiInfo) {
            return;
        }

        // 隐藏输入法
        Utils.hideKeyBoard(this);

        // 显示当前的
        showPoiMarker(poiInfo, i);
    }

    private void showPoiMarker(PoiInfo poiInfo, int i) {
        if (null == poiInfo) {
            return;
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(poiInfo.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.water_drop));

        // 第一个poi放大显示
        if (0 == i) {
            InfoWindow infoWindow = getPoiInfoWindow(poiInfo);
            markerOptions.scaleX(1.5f).scaleY(1.5f).infoWindow(infoWindow);
        }

        Marker marker = (Marker) mBaiduMap.addOverlay(markerOptions);
        if (null != marker) {
            mMarkerPoiInfo.put(marker, poiInfo);

            if (0 == i) {
                mPreSelectMarker = marker;
            }
        }
    }

    private InfoWindow getPoiInfoWindow(PoiInfo poiInfo) {
        TextView textView = new TextView(this);
        textView.setText(poiInfo.getName());
        textView.setPadding(10, 5, 10, 5);
        textView.setBackground(this.getResources().getDrawable(R.drawable.bg_info));
        InfoWindow infoWindow = new InfoWindow(textView, poiInfo.getLocation(), -150);
        return infoWindow;
    }

    /**
     * 显示定位点
     *
     * @param latLng
     */
    private boolean showSuggestMarker(LatLng latLng) {
        if (null == latLng) {
            return false;
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.water_drop))
                .scaleX(1.5f)
                .scaleY(1.5f);
        mBaiduMap.addOverlay(markerOptions);

        return true;
    }

    /**
     * 显示底部suggestion详情
     *
     * @param suggestInfo
     */
    private void showPoiInfoLayout(SuggestionResult.SuggestionInfo suggestInfo) {

        if (null == mLayoutDetailInfo || null == suggestInfo) {
            return;
        }

        if (null == mPoiTitle) {
            return;
        }

        if (null == mPoiAddress) {
            return;
        }

        mLayoutDetailInfo.setVisibility(View.VISIBLE);

        mPoiTitle.setText(suggestInfo.getKey());

        String address = suggestInfo.getAddress();
        if (TextUtils.isEmpty(address)) {
            mPoiAddress.setVisibility(View.GONE);
        } else {
            mPoiAddress.setText(suggestInfo.getAddress());
            mPoiAddress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示底部poi详情
     *
     * @param poiInfo
     */
    private void showPoiInfoLayout(PoiInfo poiInfo) {

        if (null == mLayoutDetailInfo || null == poiInfo) {
            return;
        }

        if (null == mPoiTitle) {
            return;
        }

        if (null == mPoiAddress) {
            return;
        }

        mLayoutDetailInfo.setVisibility(View.VISIBLE);

        mPoiTitle.setText(poiInfo.getName());

        String address = poiInfo.getAddress();
        if (TextUtils.isEmpty(address)) {
            mPoiAddress.setVisibility(View.GONE);
        } else {
            mPoiAddress.setText(poiInfo.getAddress());
            mPoiAddress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏详情
     */
    private void hideInfoLayout() {
        if (null == mLayoutDetailInfo) {
            return;
        }

        mLayoutDetailInfo.setVisibility(View.GONE);
    }

    /**
     * 最佳视野内显示所有点标记
     */
    private void setBounds(List<LatLng> latLngs) {
        if (null == latLngs || latLngs.size() <= 0) {
            return;
        }

        int horizontalPadding = 80;
        int verticalPaddingBottom = 400;

        // 构造地理范围对象
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        // 让该地理范围包含一组地理位置坐标
        builder.include(latLngs);

        // 设置显示在指定相对于MapView的padding中的地图地理范围
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build(),
                horizontalPadding,
                verticalPaddingBottom,
                horizontalPadding,
                verticalPaddingBottom);
        // 更新地图
        mBaiduMap.setMapStatus(mapStatusUpdate);
        // 设置地图上控件与地图边界的距离，包含比例尺、缩放控件、logo、指南针的位置
        mBaiduMap.setViewPadding(0,
                0,
                0,
                verticalPaddingBottom);
    }

    class MyTextWatcher implements TextWatcher {

        /**
         * @param s
         * @param start
         * @param count
         * @param after
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        /**
         * @param s
         * @param start
         * @param before
         * @param count
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0 && View.VISIBLE == mRecyclerView.getVisibility()) {
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
        }

        /**
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {
            // 获取检索城市
            String cityStr = mEditTextCity.getText().toString();
            // 获取检索关键字
            String keyWordStr = mEditTextPoi.getText().toString();
            if (TextUtils.isEmpty(cityStr) || TextUtils.isEmpty(keyWordStr)) {
                return;
            }

            if (View.VISIBLE == mRecyclerView.getVisibility()) {
                mRecyclerView.setVisibility(View.INVISIBLE);
            }

            mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                    .city(cityStr)
                    .keyword(keyWordStr)
                    .citylimit(true));
        }
    }
}
