package auction.org.droidflatauction;



/**
 *
 * @author nazmul hasan
 */

public class EntityProductCategory
{
    private int id;
    private String title;
    private int orderNo;

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

    @Override
    public String toString() {
        return title;
    }
}
