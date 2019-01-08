package com.example.hahaj.yeogida8;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

//최신순
public class Fragment1 extends Fragment {

    HotelAdapter adapter;

    EditText editText;
    EditText editText2;
    int personpid=0;
    String f1area;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent f1intent = getActivity().getIntent();
        personpid = f1intent.getIntExtra("personpid", 0);
        f1area = f1intent.getStringExtra("area");
        //Log.d("f1 최신순 ppid : ", ""+personpid);
        //Log.d("f1 area : ", f1area);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
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
                //서버에 personpid를 전송

                //인텐트사용하여 상품정보(상품구매)화면으로 이동
                redirectMainToBuyActivity(personpid);
            }
        });

        return rootView;
    }


    //어댑터를 이용하여 data관리, 어댑터의 리턴값이 아이템으로 보임

    class HotelAdapter extends BaseAdapter {

        ArrayList<HotelItem> items=new ArrayList<HotelItem>();

        public void addItem(HotelItem item) {
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
            HotelItemView view=null;
            if(convertView==null) {
                view=new HotelItemView(getContext());
            }
            else{
                view=(HotelItemView) convertView;
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




    //메인에서 상품 구매하는 화면으로 이동하는 메소드
    public void redirectMainToBuyActivity(int personpid){
        Intent intent = new Intent(getContext(), BuyItemActivity.class);
        intent.putExtra("personpid", 0);
        startActivityForResult(intent, BasicInfo.REQUEST_CODE_MAINTOBUY);
    }

}
