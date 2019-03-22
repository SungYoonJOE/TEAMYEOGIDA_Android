package com.example.hahaj.yeogida8;

//채팅목록 아이템뷰

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatroomItemView extends LinearLayout {

    TextView chatroomname;

    public ChatroomItemView(Context context) {
        super(context);
        init(context);
    }

    public ChatroomItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chatroom_item, this, true);

        chatroomname = (TextView) findViewById(R.id.chatroomname);

    }

    public void setProduct_name(String product_name) {
        chatroomname.setText(product_name);
    }

}
