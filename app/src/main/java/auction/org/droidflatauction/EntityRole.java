package auction.org.droidflatauction;



/**
 *
 * //@author nazmul hasan
 */

public class EntityRole {

    //@Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    //@Column(name = "id")
    
    private int id;

    //@Column(name = "title", length = 200)
    private String title;
    
    //@Column(name = "description", length = 200)
    private String description;

    public EntityRole() 
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
