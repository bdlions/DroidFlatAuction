package auction.org.droidflatauction;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOMessageBody {
    private int offset;
    private int limit;
    private EntityUser entityUser;
    private EntityMessageBody entityMessageBody;
    private String createdTime;

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
    
    public EntityUser getEntityUser() {
        return entityUser;
    }

    public void setEntityUser(EntityUser entityUser) {
        this.entityUser = entityUser;
    }

    public EntityMessageBody getEntityMessageBody() {
        return entityMessageBody;
    }

    public void setEntityMessageBody(EntityMessageBody entityMessageBody) {
        this.entityMessageBody = entityMessageBody;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
    
}
