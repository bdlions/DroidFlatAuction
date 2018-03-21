package auction.org.droidflatauction;



/**
 *
 * //@author nazmul hasan
 */

public class EntityUserRole {

    //@Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    //@Column(name = "id")
    
    private int id;

    //@Column(name = "user_id")
    private int userId;
    
    //@Column(name = "role_id")
    private int roleId;

    public EntityUserRole() 
    {
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
