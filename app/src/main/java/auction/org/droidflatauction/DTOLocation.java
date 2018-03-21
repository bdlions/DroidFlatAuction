package auction.org.droidflatauction;


/**
 *
 * @author Nazmul Hasan
 */
public class DTOLocation {
    private int offset;
    private int limit;
    private EntityLocation entityLocation;

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

    public EntityLocation getEntityLocation() {
        return entityLocation;
    }

    public void setEntityLocation(EntityLocation entityLocation) {
        this.entityLocation = entityLocation;
    }
    
}
