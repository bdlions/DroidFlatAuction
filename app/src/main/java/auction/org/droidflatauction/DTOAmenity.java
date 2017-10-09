package auction.org.droidflatauction;

import com.auction.dto.Amenity;

/**
 * Created by bdlions on 29/08/2017.
 */

public class DTOAmenity extends Amenity{
    boolean isSelected;
    String  amenity;

    public DTOAmenity(boolean isSelected, String amenity) {
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
