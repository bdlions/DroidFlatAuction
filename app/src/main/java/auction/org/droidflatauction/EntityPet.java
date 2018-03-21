package auction.org.droidflatauction;

/**
 *
 * @author nazmul hasan
 */
public class EntityPet {

    private int id;

    private String title;

    public EntityPet() 
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
