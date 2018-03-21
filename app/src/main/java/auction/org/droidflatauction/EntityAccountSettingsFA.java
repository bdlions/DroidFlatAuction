package auction.org.droidflatauction;
/**
 *
 * @author nazmul hasan
 */
public class EntityAccountSettingsFA {

    private int id;
    private int userId;
    private double chargePerClick;
    private double dailyBudget;
    private boolean campainActive;
    private long createdOn;
    private long modifiedOn;

    public EntityAccountSettingsFA() 
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

    public double getChargePerClick() {
        return chargePerClick;
    }

    public void setChargePerClick(double chargePerClick) {
        this.chargePerClick = chargePerClick;
    }

    public double getDailyBudget() {
        return dailyBudget;
    }

    public void setDailyBudget(double dailyBudget) {
        this.dailyBudget = dailyBudget;
    }

    public boolean isCampainActive() {
        return campainActive;
    }

    public void setCampainActive(boolean campainActive) {
        this.campainActive = campainActive;
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
