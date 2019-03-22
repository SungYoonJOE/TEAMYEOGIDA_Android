package com.example.hahaj.yeogida8;

public class HotelItem {

    private int productpid;
    private String pname;
    private String pimg;
    private String paddr;
    private String pdate_e;
    private String pdate_s;
    private int fprice;
    private int pprice;
    private int producthit;

    public HotelItem(int productpid, String pimg, String pname, String pdate_s, String pdate_e, String paddr, int fprice, int pprice){
        this.productpid = productpid;
        this.pimg = pimg;
        this.pname = pname;
        this.pdate_s= pdate_s;
        this.pdate_e= pdate_e;
        this.paddr= paddr;
        this.fprice= fprice;
        this.pprice= pprice;
    }

    public HotelItem(int productpid, String pimg, String pname, String pdate_s, String pdate_e, String paddr, int fprice, int pprice, int hit){
        this.productpid = productpid;
        this.pimg = pimg;
        this.pname = pname;
        this.pdate_s= pdate_s;
        this.pdate_e= pdate_e;
        this.paddr= paddr;
        this.fprice= fprice;
        this.pprice= pprice;
        this.producthit = hit;
    }

    public int getProductpid() {
        return productpid;
    }

    public void setProductpid(int productpid) {
        this.productpid = productpid;
    }

    public String getPname() {
        return pname;
    }

    public void setProductname(String pname) {
        this.pname = pname;
    }

    public String getPimg() { return pimg; }

    public void setProductimage(String pimg) {
        this.pimg = pimg;
    }

    public String getPaddr() {
        return paddr;
    }

    public void setProductAddress(String paddr) {
        this.paddr = paddr;
    }

    public String getPdate_e() {
        return pdate_e;
    }

    public void setProductdate_e(String pdate_e) {
        this.pdate_e = pdate_e;
    }

    public String getPdate_s() {
        return pdate_s;
    }

    public void setProductdate_s(String pdate_s) {
        this.pdate_s = pdate_s;
    }

    public int getFprice() {
        return fprice;
    }

    public void setFormerprice(int fprice) {
        this.fprice = fprice;
    }

    public int getPprice() {
        return pprice;
    }

    public void setProductprice(int pprice) {
        this.pprice = pprice;
    }

    public int getProducthit(){return producthit;}

    public void setProducthit(int hit){this.producthit = hit;}

}
