package com.example.hahaj.yeogida8;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NotifyQnAMainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int NOTIFY_FRAGMENT = 1;
    private final int QnA_FRAGMENT = 2;


    private Button bt_tab1, bt_tab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_tab1 = (Button)findViewById(R.id.notifyButton);
        bt_tab2 = (Button)findViewById(R.id.QnAButton);

        bt_tab1.setOnClickListener(this);
        bt_tab2.setOnClickListener(this);

        callFragment(NOTIFY_FRAGMENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.notifyButton :
                // '버튼1' 클릭 시 '프래그먼트1' 호출
                callFragment(NOTIFY_FRAGMENT);
                break;

            case R.id.QnAButton :
                // '버튼2' 클릭 시 '프래그먼트2' 호출
                callFragment(QnA_FRAGMENT);
                break;
        }
    }

    private void callFragment(int fragment_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment_no){
            case 1:
                // '프래그먼트1' 호출
                Fragment_Notify fragment1 = new Fragment_Notify();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                Fragment_QnA fragment2 = new Fragment_QnA();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;
        }
    }
}


