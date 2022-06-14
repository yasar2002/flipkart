package pojo;

public class Products {
    private String pid,uid,type,image,name,desc;
    private int quantity;
    private float price;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Products(String pid, String uid, String type, String image, String name, String desc, float price, int quantity) {
        this.pid = pid;
        this.uid = uid;
        this.type = type;
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.quantity = quantity;
        this.price = price;
    }
}
