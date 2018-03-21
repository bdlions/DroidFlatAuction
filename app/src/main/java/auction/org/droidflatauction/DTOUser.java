package auction.org.droidflatauction;

import java.util.List;

/**
 *
 * @author Nazmul Hasan
 */
public class DTOUser implements java.io.Serializable {
    public int offset;
    public int limit;
    public EntityUser entityUser;
    public List<EntityRole> roles;
    public EntityAccountStatus entityAccountStatus;

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

    public EntityAccountStatus getEntityAccountStatus() {
        return entityAccountStatus;
    }

    public void setEntityAccountStatus(EntityAccountStatus entityAccountStatus) {
        this.entityAccountStatus = entityAccountStatus;
    }

    
    public EntityUser getEntityUser() {
        return entityUser;
    }

    public void setEntityUser(EntityUser entityUser) {
        this.entityUser = entityUser;
    }

    public List<EntityRole> getRoles() {
        return roles;
    }

    public void setRoles(List<EntityRole> roles) {
        this.roles = roles;
    }    
}
