package com.example.hahaj.yeogida8;

public class HotelItem {

    int productpid;
    String pname;
    String pimg;
    String paddr;
    String pdate_e;
    String pdate_s;
    int fprice;
    int pprice;

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

    public int getProductpid() {
        return productpid;
    }

    public void setProductpid(int productpid) {
        this.productpid = productpid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPimg() {
        return pimg;
    }

    public void setPimg(String pimg) {
        this.pimg = pimg;
    }

    public String getPaddr() {
        return paddr;
    }

    public void setPaddr(String paddr) {
        this.paddr = paddr;
    }

    public String getPdate_e() {
        return pdate_e;
    }

    public void setPdate_e(String pdate_e) {
        this.pdate_e = pdate_e;
    }

    public String getPdate_s() {
        return pdate_s;
    }

    public void setPdate_s(String pdate_s) {
        this.pdate_s = pdate_s;
    }

    public int getFprice() {
        return fprice;
    }

    public void setFprice(int fprice) {
        this.fprice = fprice;
    }

    public int getPprice() {
        return pprice;
    }

    public void setPprice(int pprice) {
        this.pprice = pprice;
    }

    /*
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

    public int getProductpid() {
        return productpid;
    }

    public void setProductpid(int productpid) {
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
*/
}
