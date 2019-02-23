package com.example.hahaj.yeogida8;

//채팅 화면

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class ChattingActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatDataList;
    //닉네임 대신 personpid 이용하면됨!
    private String nick = "nick1";
    private EditText Edittext_chat;
    private Button button_send;

    //private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        //전송버튼
        button_send = findViewById(R.id.sendbutton);
        //채팅 메세지
        Edittext_chat = findViewById(R.id.chat);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = Edittext_chat.getText().toString();

                if(msg != null) {
                    ChatData chat = new ChatData();
                    chat.setMsg(msg);
                    //파이어베이스로 보내는 코드
                    //myRef.push().setValue(chat);
                }
            }
        });

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatDataList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatDataList, ChattingActivity.this, nick) ;

        mRecyclerView.setAdapter(mAdapter);

        // Write a message to the database
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //myRef = database.getReference();

        /*
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatData chat = dataSnapshot.getValue(ChatData.class);
                ((ChatAdapter) mAdapter).addChat(chat);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });*/
    }

}
