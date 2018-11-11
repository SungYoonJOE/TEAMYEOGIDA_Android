package com.example.hahaj.yeogida8;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

public class BuyItemActivity extends AppCompatActivity {
    private ImageView imageView1;
    ImageButton btn_onof;
    int i=0;
    boolean isLike;

    //지도API
    //LinearLayout mapview;
    TMapView tmap;
    private final String MAPAPPKEY = "";
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_item);

        double lat = 37.6026422;
        double lng = 126.953058;
        likeItem();
        initTmap(lng, lat);
        Log.d("문제확인6", "여기 아니다!!!!!!!!");
        addMarker(lng, lat);
    }
    //찜 기능
    public void likeItem(){
        imageView1 = (ImageView)findViewById(R.id.image1);
        imageView1.setImageResource(R.drawable.item1);

        btn_onof = (ImageButton)findViewById(R.id.btn_likeonoff);
        btn_onof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1-i;
                if(i==1){
                    btn_onof.setImageResource(R.drawable.likeon);
                    Toast.makeText(getApplicationContext(), "찜 상품에 등록되었습니다.", Toast.LENGTH_LONG).show();
                    isLike = true;
                    //서버로 정보 전달
                }
                else if(i==0){
                    btn_onof.setImageResource(R.drawable.likeoff);
                    Toast.makeText(getApplicationContext(), "찜 상품이 취소되었습니다.", Toast.LENGTH_LONG).show();
                    isLike = false;
                    //서버로 정보 전달
                }
            }
        });
    }

    //TMap 생성
    private void initTmap(double lng, double lat){
        tmap = new TMapView(this);
        tmap.setSKTMapApiKey(MAPAPPKEY);
        Log.d("문제확인1", "여기 아니다!!!!!!!!");
        ConstraintLayout mapview = (ConstraintLayout) findViewById(R.id.mapview);
        Log.d("문제확인2", "여기 아니다!!!!!!!!");
        this.lat = lat;
        this.lng = lng;
        //double lat = 37.5725749;
        //double lng = 126.9789405;
        tmap.setCenterPoint(lng, lat);
        tmap.setZoomLevel(15);

        mapview.addView(tmap);
        Log.d("문제확인3", "여기 아니다!!!!!!!!");
    }

    private void addMarker(double lng, double lat){
        //this.lat = lat;
        //this.lng = lng;
        TMapPoint tPoint = new TMapPoint(lat, lng);
        TMapMarkerItem marker = new TMapMarkerItem();
        marker.setTMapPoint(tPoint);
        Log.d("문제확인4", "여기 아니다!!!!!!!!");
        tmap.addMarkerItem("marker", marker);
        Log.d("문제확인5", "여기 아니다!!!!!!!!");
    }


}
