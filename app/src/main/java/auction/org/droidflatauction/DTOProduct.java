package auction.org.droidflatauction;


/**
 *
 * @author Nazmul Hasan
 */
public class DTOProduct {
    private int offset;
    private int limit;
    private String images;
    //total number of seconds auction will be ended
    private long auctionEndTimeLeft;
    private EntityProduct entityProduct;

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

    public EntityProduct getEntityProduct() {
        return entityProduct;
    }

    public void setEntityProduct(EntityProduct entityProduct) {
        this.entityProduct = entityProduct;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public long getAuctionEndTimeLeft() {
        return auctionEndTimeLeft;
    }

    public void setAuctionEndTimeLeft(long auctionEndTimeLeft) {
        this.auctionEndTimeLeft = auctionEndTimeLeft;
    }
    
}
