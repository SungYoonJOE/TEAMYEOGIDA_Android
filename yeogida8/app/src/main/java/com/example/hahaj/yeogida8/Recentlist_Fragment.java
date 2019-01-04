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

public class Recentlist_Fragment extends Fragment {

    HotelAdapter adapter;

    EditText editText;
    EditText editText2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.recentlist_fragment, container, false);
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


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) //position은 몇번째 아이템인지 인덱스값
            {
                HotelItem item = (HotelItem) adapter.getItem(position);
                Toast.makeText(getContext(),"선택 : "+item.getProductname(), Toast.LENGTH_LONG).show();
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

}
