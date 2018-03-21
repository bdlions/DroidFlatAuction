package auction.org.droidflatauction;

/**
 *
 * @author nazmul hasan
 */

public class EntityMessageBody {

    private int id;

    private int messageHeaderId;
    
    private String message;
    
    private int userId;
    
    private long createdOn;
    
    private long modifiedOn;
    
    public EntityMessageBody() 
    {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMessageHeaderId() {
        return messageHeaderId;
    }

    public void setMessageHeaderId(int messageHeaderId) {
        this.messageHeaderId = messageHeaderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public long getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(long modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
    
}
