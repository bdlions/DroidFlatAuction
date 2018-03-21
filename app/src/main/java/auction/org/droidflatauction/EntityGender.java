package auction.org.droidflatauction;

/**
 *
 * @author nazmul hasan
 */

public class EntityGender {

    private int id;

    private String title;
    
    private int orderNo;
    
    public EntityGender() 
    {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    } 

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
}
