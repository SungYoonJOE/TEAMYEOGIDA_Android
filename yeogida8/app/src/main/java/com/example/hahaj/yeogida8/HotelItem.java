package com.example.hahaj.yeogida8;

public class HotelItem {

    String productname;
    String productpid;
    String productaddress;
    String formerprice;
    String productprice;
    String productdate_e;
    String productdate_s;
    int productimage;


    public HotelItem(String productname, String productpid, String productaddress, String formerprice, String productprice, String productdate_e, String productdate_s, int productimage) {
        this.productname = productname;
        this.productpid = productpid;
        this.productaddress = productaddress;
        this.formerprice = formerprice;
        this.productprice = productprice;
        this.productdate_e = productdate_e;
        this.productdate_s = productdate_s;
        this.productimage = productimage;
    }

    public String getFormerprice() {
        return formerprice;
    }

    public void setFormerprice(String formerprice) {
        this.formerprice = formerprice;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getProductdate_e() {
        return productdate_e;
    }

    public void setProductdate_e(String productdate_e) {
        this.productdate_e = productdate_e;
    }

    public String getProductdate_s() {
        return productdate_s;
    }

    public void setProductdate_s(String productdate_s) {
        this.productdate_s = productdate_s;
    }

    public String getProductaddress() {
        return productaddress;
    }

    public void setProductaddress(String productaddress) {
        this.productaddress = productaddress;
    }


    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductpid() {
        return productpid;
    }

    public void setProductpid(String productpid) {
        this.productpid = productpid;
    }

    public int getProductimage() {
        return productimage;
    }

    public void setProductimage(int resId) {
        this.productimage = resId;
    }

    @Override
    public String toString() {
        return "Hotel_item{" +
                "productname='" + productname + '\'' +
                ", productpid='" + productpid + '\'' +
                ", productaddress='" + productaddress + '\'' +
                ", formerprice=" + formerprice +
                ", productprice=" + productprice +
                ", productdate_e='" + productdate_e + '\'' +
                ", productdate_s='" + productdate_s + '\'' +
                ", productimage=" + productimage +
                '}';
    }
}
