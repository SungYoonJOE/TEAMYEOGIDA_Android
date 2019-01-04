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

//인기순
public class Fragment2 extends Fragment {

    HotelAdapter adapter;

    EditText editText;
    EditText editText2;
    int personpid;
    String f2area;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent f1intent = getActivity().getIntent();
        personpid = f1intent.getIntExtra("personpid", 0);
        f2area = f1intent.getStringExtra("area");
       // Log.d("f2 인기순 ppid: ", ""+personpid);
        //Log.d("f2 area : ", f2area);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        final HotelAdapter adapter=new HotelAdapter();
        adapter.addItem(new HotelItem("박성준","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("박민수","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel2));
        adapter.addItem(new HotelItem("박성준","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("박민수","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel2));
        adapter.addItem(new HotelItem("박성준","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("박민수","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel2));
        adapter.addItem(new HotelItem("박성준","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("박민수","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel2));
        adapter.addItem(new HotelItem("박성준","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("박민수","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel2));
        adapter.addItem(new HotelItem("박성준","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("박민수","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel2));
        adapter.addItem(new HotelItem("박성준","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel));
        adapter.addItem(new HotelItem("박민수","상명호텔","경기 고양","2000","3000","20181201","20181231",R.drawable.ic_hotel2));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) //position은 몇번째 아이템인지 인덱스값
            {
                HotelItem item = (HotelItem) adapter.getItem(position);
                Toast.makeText(getContext(),"선택 : "+item.getProductname(), Toast.LENGTH_LONG).show();
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
            view.setProductname(item.getProductname());
            view.setProductpid(item.getProductpid());
            view.setProductAddress(item.getProductaddress());
            view.setProductimage(item.getProductimage());
            view.setFormerPrice(item.getFormerprice());
            view.setProductDate_s(item.getProductdate_e());
            view.setProductPrice(item.getProductprice());
            view.setProductDate_e(item.getProductdate_e());
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
