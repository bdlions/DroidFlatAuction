package auction.org.droidflatauction;


/**
 *
 * @author Nazmul Hasan
 */
public class DTOAutoBid {
    private int offset;
    private int limit;
    private EntityAutoBid entityAutoBid;

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

    public EntityAutoBid getEntityAutoBid() {
        return entityAutoBid;
    }

    public void setEntityAutoBid(EntityAutoBid entityAutoBid) {
        this.entityAutoBid = entityAutoBid;
    }
}
