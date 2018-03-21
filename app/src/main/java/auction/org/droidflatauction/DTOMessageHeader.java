package auction.org.droidflatauction;


/**
 *
 * @author Nazmul Hasan
 */
public class DTOMessageHeader {
    private int offset;
    private int limit;
    private EntityUser sender;
    private EntityUser receiver;
    private EntityMessageHeader entityMessageHeader;
    private EntityMessageBody entityMessageBody;

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
    
    public EntityUser getSender() {
        return sender;
    }

    public void setSender(EntityUser sender) {
        this.sender = sender;
    }

    public EntityUser getReceiver() {
        return receiver;
    }

    public void setReceiver(EntityUser receiver) {
        this.receiver = receiver;
    }

    public EntityMessageHeader getEntityMessageHeader() {
        return entityMessageHeader;
    }

    public void setEntityMessageHeader(EntityMessageHeader entityMessageHeader) {
        this.entityMessageHeader = entityMessageHeader;
    }

    public EntityMessageBody getEntityMessageBody() {
        return entityMessageBody;
    }

    public void setEntityMessageBody(EntityMessageBody entityMessageBody) {
        this.entityMessageBody = entityMessageBody;
    }
    
}
