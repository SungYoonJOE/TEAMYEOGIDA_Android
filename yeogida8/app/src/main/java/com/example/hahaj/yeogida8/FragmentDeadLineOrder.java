package com.example.hahaj.yeogida8;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentDeadLineOrder extends Fragment {

    HotelAdapter adapter;

    EditText editText;
    EditText editText2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_deadline_order, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        final HotelAdapter adapter=new HotelAdapter();
        adapter.addItem(new HotelItem(1, "http://cfs11.tistory.com/image/33/tistory/2009/02/26/22/41/49a69bf854e7c", "대영호텔", "2019-01-01", "2019-01-02", "서울시 광진구 자양동", 500,100));
        adapter.addItem(new HotelItem(2, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRJe8n7J51TfYyKlP2SK-72TisT2P2t8tPGsVxPtqqhvuCBCQeZw", "진권호텔", "2019-01-02", "2019-01-03", "서울시 광진구 자양동", 600,200));
        adapter.addItem(new HotelItem(3, "http://cfs11.tistory.com/image/33/tistory/2009/02/26/22/41/49a69bf854e7c", "성준호텔", "2019-01-03", "2019-01-04", "서울시 광진구 자양동", 700,300));
        adapter.addItem(new HotelItem(4, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRJe8n7J51TfYyKlP2SK-72TisT2P2t8tPGsVxPtqqhvuCBCQeZw", "지해호텔", "2019-01-04", "2019-01-05", "서울시 광진구 자양동", 800,400));
        adapter.addItem(new HotelItem(5, "http://cfs11.tistory.com/image/33/tistory/2009/02/26/22/41/49a69bf854e7c", "유림호텔", "2019-01-05", "2019-01-06", "서울시 광진구 자양동", 900,500));
        adapter.addItem(new HotelItem(6, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRJe8n7J51TfYyKlP2SK-72TisT2P2t8tPGsVxPtqqhvuCBCQeZw", "성윤호텔", "2019-01-06", "2019-01-07", "서울시 광진구 자양동", 1000,600));
        adapter.addItem(new HotelItem(7, "http://cfs11.tistory.com/image/33/tistory/2009/02/26/22/41/49a69bf854e7c", "상명호텔", "2019-01-07", "2019-01-08", "서울시 광진구 자양동", 1100,700));

        //adapter.addItem(new HotelItem("박성준","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel));
        //adapter.addItem(new HotelItem("박민수","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel2));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) //position은 몇번째 아이템인지 인덱스값
            {
                HotelItem item = (HotelItem) adapter.getItem(position);
                Toast.makeText(getContext(),"선택 : "+item.getPname(), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    class HotelAdapter extends BaseAdapter {

        ArrayList<HotelItem> items = new ArrayList<HotelItem>();

        @Override
        public int getCount() //몇개의 아이템이 있니?
        {
            return items.size();
        }

        public void addItem(HotelItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) //몇 번째 아이템인지
        {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) //id있음 넘겨줘
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            HotelItemView view=null;
            if(convertView==null) {
                view= new HotelItemView(getContext());
            }
            else{
                view= (HotelItemView) convertView;
            }
            HotelItem item=items.get(position);
            view.setProductname(item.getPname());
            view.setProductpid(item.getProductpid());
            view.setProductAddress(item.getPaddr());
            view.setProductimage(item.getPimg());
            view.setFormerPrice(item.getFprice());
            view.setProductDate_s(item.getPdate_s());
            view.setProductPrice(item.getPprice());
            view.setProductDate_e(item.getPdate_e());
            return view;
        }
    }
}
