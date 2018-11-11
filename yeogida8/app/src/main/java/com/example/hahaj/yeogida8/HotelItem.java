package com.example.hahaj.yeogida8;

public class HotelItem {

    String hotel_name;
    String user_name;
    int resId;

    public HotelItem(String hotel_name, String user_name, int resId) {
        this.hotel_name = hotel_name;
        this.user_name = user_name;
        this.resId = resId;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public String toString() {
        return "HotelItem{" +
                "hotel_name='" + hotel_name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", resId=" + resId +
                '}';
    }
}
