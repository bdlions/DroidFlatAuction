package auction.org.droidflatauction;


/**
 *
 * @author Nazmul Hasan
 */
public class DTOBid {
    private int offset;
    private int limit;
    private EntityBid entityBid;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public EntityBid getEntityBid() {
        return entityBid;
    }

    public void setEntityBid(EntityBid entityBid) {
        this.entityBid = entityBid;
    }

    
}
