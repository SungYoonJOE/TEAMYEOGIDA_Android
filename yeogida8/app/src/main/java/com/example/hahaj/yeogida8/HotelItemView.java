package com.example.hahaj.yeogida8;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HotelItemView extends LinearLayout {

    TextView productName;
    TextView productPid;
    TextView productAddress;
    TextView formerPrice;
    TextView productPrice;
    TextView productDate_e;
    TextView productDate_s;
    ImageView imageView;


    public HotelItemView(Context context) {
        super(context);
        init(context);
    }
    public HotelItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init (Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.hotel_item,this,true);

        productName = (TextView) findViewById(R.id.productname);
        productPid = (TextView) findViewById(R.id.productPid);
        productAddress = (TextView) findViewById(R.id.productaddress);
        productDate_e = (TextView) findViewById(R.id.productdate_e);
        productDate_s = (TextView) findViewById(R.id.productdate_s);
        formerPrice = (TextView) findViewById(R.id.formerprice);
        productPrice = (TextView) findViewById(R.id.productprice);
        imageView = (ImageView) findViewById(R.id.imageView);
    }


    public void setFormerPrice(String formerPrice) { productPid.setText(formerPrice); }
    public void setProductAddress(String productaddress) { productAddress.setText(productaddress);}
    public void setProductname(String productname){ productName.setText(productname); }
    public void setProductpid(String productpid){
        productPid.setText(productpid);
    }
    public void setProductDate_s(String productdate_s) { productDate_s.setText(productdate_s);}
    public void setProductDate_e(String productdate_e) { productDate_e.setText(productdate_e);}
    public void setProductPrice(String productprice) { productPrice.setText(productprice); }
    public void setProductimage(int productimage){
        imageView.setImageResource(productimage);
    }
}

