package auction.org.droidflatauction;



/**
 *
 * //@author nazmul hasan
 */

public class EntityTime {

    //@Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    //@Column(name = "id")    
    private int id;

    //@Column(name = "title", length = 200)
    private String title;
    
    //@Column(name = "sec", columnDefinition = "int(11) unsigned DEFAULT 0")
    private long sec;

    public EntityTime() 
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

    public long getSec() {
        return sec;
    }

    public void setSec(long sec) {
        this.sec = sec;
    }

    @Override
    public String toString()
    {
        return title;
    }

    
}
