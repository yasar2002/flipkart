package pojo;

public class order {
    private int orderid,pquantity;
    private String uid,pid,Date;
    private float price;
    public order(int orderid,String uid, String pid,int pquantity, float price,String date) {
        this.orderid = orderid;
        this.pquantity = pquantity;
        this.uid = uid;
        this.pid = pid;
        Date = date;
        this.price = price;
    }
    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getPquantity() {
        return pquantity;
    }

    public void setPquantity(int pquantity) {
        this.pquantity = pquantity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}