package com.example.hahaj.yeogida8;

//채팅화면 리사이클러뷰 어댑터

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> mDataset;
    private String myNickname;
    int personpid=11;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView Textview_msg;
        public View rootview;
        public MyViewHolder(View v) {
            super(v);

            Textview_msg = v.findViewById(R.id.text_msg);
            rootview = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    //nickname을 personpid로 바꾼다
    public ChatAdapter(List<ChatData> myDataset, Context context, int personpid) {
        mDataset = myDataset;
        this.personpid = personpid;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ChatData chat=mDataset.get(position);
        holder.Textview_msg.setText(chat.getMsg());

        //person pid를 비교해서 띄움
        //여기 personpid비교로 바꿈
        //GRAVITY로 정렬
        if(chat.getPersonpid()==this.personpid) {
            //holder.Textview_msg.setBackgroundResource(R.drawable.chatbubble_right);
            //holder.Textview_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.Textview_msg.setGravity(Gravity.RIGHT);
        }
        else{
            //holder.Textview_msg.setBackgroundResource(R.drawable.chatbubble_left);
            holder.Textview_msg.setGravity(View.TEXT_ALIGNMENT_TEXT_START);

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return mDataset == null ? 0: mDataset.size();
    }
    public ChatData getChat(int position) {
        return mDataset !=null ? mDataset.get(position) : null;
    }
    public void addChat(ChatData chat) {
        mDataset.add(chat);
        //새로 추가되는 Chatdata만 추가

        notifyItemInserted(mDataset.size()-1);

    }
}
