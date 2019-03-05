package com.example.hahaj.yeogida8;

//채팅 목록에 뿌려줄 아이템

public class ChatroomItem {
    String product_name;
    int roompid;

    public int getRoompid() {
        return roompid;
    }

    public void setRoompid(int roompid) {
        this.roompid = roompid;
    }

    public ChatroomItem(String product_name) {
        this.product_name = product_name;
    }

    public ChatroomItem(String product_name, int roompid) {
        this.product_name = product_name;
        this.roompid = roompid;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
