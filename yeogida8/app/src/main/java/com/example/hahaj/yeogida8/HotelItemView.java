package com.example.hahaj.yeogida8;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HotelItemView extends LinearLayout {

    TextView textView;
    TextView textView2;
    ImageView imageView;

    public HotelItemView(Context context) {
        super(context);
        init(context);
    }

    public HotelItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hotel_item,this,true);

        textView= (TextView) findViewById(R.id.textView);
        textView2= (TextView) findViewById(R.id.textView2);
        imageView=(ImageView) findViewById(R.id.imageView);
    }


    public void setHotel_name(String Hotel_name) {
        textView.setText(Hotel_name);
    }

    public void setUser_name(String User_name){ textView2.setText(User_name); }

    public void setImage(int resId) {
        imageView.setImageResource(resId);
    }

}

