package com.example.hahaj.yeogida8;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Chatsell_fragment extends Fragment {

    ChatroomAdapter adapter;
    NetworkUrl url = new NetworkUrl();
    int productpid = 1;
    int ppid;
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //personpid 불러오기
        SharedPreferences pref = this.getActivity().getSharedPreferences("pref_PERSONPID", Context.MODE_PRIVATE);
        //pref_PERSONPID파일의 personpid 키에 있는 데이터를 가져옴. 없으면 0을 리턴
        ppid = pref.getInt("personpid", 0);
        Log.d("ppid in 판매내역 목록>> ", "" + ppid);

        //리스트뷰 부분

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_chatsell_fragment, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView_sell);

        adapter = new Chatsell_fragment.ChatroomAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatroomItem item = (ChatroomItem) adapter.getItem(position);
                Toast.makeText(getContext(), "선택된 것:"+item.getProduct_name(),Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
    class ChatroomAdapter extends BaseAdapter {

        ArrayList<ChatroomItem> items= new ArrayList<>();

        public void addItem(ChatroomItem item) {
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChatroomItemView view = null;
            if(convertView == null) {
                view = new ChatroomItemView(getContext());
            }
            else {
                view = (ChatroomItemView) convertView;
            }

            ChatroomItem item = items.get(position);
            view.setProduct_name(item.getProduct_name());
            return view;
    }
    }

}
