package com.example.hahaj.yeogida8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Fragment_Notify extends Fragment {

    NotifyAdapter adapter;

    public Fragment_Notify() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_notify, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView01);

        adapter = new NotifyAdapter();
        adapter.addItems(new NotifyItem("개인 정보 관련"));
        adapter.addItems(new NotifyItem("비밀번호 관련"));
        adapter.addItems(new NotifyItem("개인 정보 관련"));
        adapter.addItems(new NotifyItem("비밀번호 관련"));
        adapter.addItems(new NotifyItem("개인 정보 관련"));
        adapter.addItems(new NotifyItem("비밀번호 관련"));
        adapter.addItems(new NotifyItem("개인 정보 관련"));
        adapter.addItems(new NotifyItem("비밀번호 관련"));
        adapter.addItems(new NotifyItem("개인 정보 관련"));
        adapter.addItems(new NotifyItem("비밀번호 관련"));
        adapter.addItems(new NotifyItem("개인 정보 관련"));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NotifyItem notify = (NotifyItem) adapter.getItem(position);
                Toast.makeText(view.getContext(), "선택 : " + notify.getTitle(), Toast.LENGTH_SHORT).show();

                Intent intent;
                if(position == 0) {
                    intent = new Intent(view.getContext(), Notify_Password.class);
                    startActivity(intent);
                }

            }

        });


        return view;
    }

    class NotifyAdapter extends BaseAdapter {

        ArrayList<NotifyItem> items = new ArrayList<>();

        public void addItems(NotifyItem item) {
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
            NotifyItemView views = new NotifyItemView(getContext());

            NotifyItem item = items.get(position);
            views.setTitle(item.getTitle());

            return views;
        }
    }
}


