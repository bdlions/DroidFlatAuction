package auction.org.droidflatauction;

/**
 * Created by bdlions on 29/08/2017.
 */

public class AmenityModel {
    boolean isSelected;
    String  amenity;

    public AmenityModel(boolean isSelected, String amenity) {
        this.isSelected = isSelected;
        this.amenity = amenity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }
}
