package pojo;

public class Revenue {
    private String pid,uid;
    private float revenue;
    private int sold;

    public Revenue(String pid, String uid, float revenue, int sold) {
        this.pid = pid;
        this.uid = uid;
        this.revenue = revenue;
        this.sold = sold;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public float getRevenue() {
        return revenue;
    }

    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}
