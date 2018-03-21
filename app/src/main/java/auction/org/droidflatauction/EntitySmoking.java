package auction.org.droidflatauction;



/**
 *
 * //@author nazmul hasan
 */

public class EntitySmoking {

    //@Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    //@Column(name = "id")
    
    private int id;

    //@Column(name = "title", length = 200)
    private String title;

    public EntitySmoking() 
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

    @Override
    public String toString()
    {
        return title;
    }
}
