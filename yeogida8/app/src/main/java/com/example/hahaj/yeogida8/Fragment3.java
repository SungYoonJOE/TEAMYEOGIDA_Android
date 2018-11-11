package com.example.hahaj.yeogida8;

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

//마감임박
public class Fragment3 extends Fragment {

    HotelAdapter adapter;

    EditText editText;
    EditText editText2;
    int personpid;
    String f3area;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent f3intent = getActivity().getIntent();
        personpid = f3intent.getIntExtra("personpid", 0);
        f3area = f3intent.getStringExtra("area");
        //Log.d("fragment3 마감임박 : ", ""+personpid);
        //Log.d("f3 area : ", f3area);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        final HotelAdapter adapter=new HotelAdapter();
        adapter.addItem(new HotelItem("사슴호텔","고진권",R.drawable.ic_hotel2));
        adapter.addItem(new HotelItem("자하펜션","박지수",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("자하호텔","박세훈",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("상명펜션","박성준",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("상명호텔","박민수",R.drawable.ic_hotel2));
        adapter.addItem(new HotelItem("사슴펜션","박지상",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("미백호텔","조대영",R.drawable.ic_hotel2));
        adapter.addItem(new HotelItem("미백펜션","박수빈",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("멋사호텔","정보경",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("멋사펜션","류수연",R.drawable.ic_hotel));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) //position은 몇번째 아이템인지 인덱스값
            {
                HotelItem item = (HotelItem) adapter.getItem(position);
                Toast.makeText(getContext(),"선택 : "+item.getHotel_name(), Toast.LENGTH_LONG).show();
                //서버에 personpid를 전송

                //인텐트사용하여 상품정보(상품구매)화면으로 이동
                redirectMainToBuyActivity(personpid);
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
            HotelItem item= items.get(position);
            view.setHotel_name(item.getHotel_name());
            view.setUser_name(item.getUser_name());
            view.setImage(item.getResId());
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
