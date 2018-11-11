package com.example.hahaj.yeogida8;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotifyItemView extends LinearLayout {

    TextView textView;

    public NotifyItemView(Context context) {
        super(context);
        init(context);
    }

    public NotifyItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.notify_item, this, true);

        textView = (TextView)findViewById(R.id.textView01);
    }

    public void setTitle(String title) {

        textView.setText(title);
    }
}
