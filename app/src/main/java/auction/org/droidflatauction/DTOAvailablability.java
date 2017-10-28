package auction.org.droidflatauction;

import com.bdlions.dto.Availability;

/**
 * Created by bdlions on 29/08/2017.
 */

public class DTOAvailablability extends Availability{
    boolean isSelected;
    String  availablability;

    public DTOAvailablability(boolean isSelected, String availablability) {
        this.isSelected = isSelected;
        this.availablability = availablability;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAvailablability() {
        return availablability;
    }

    public void setAvailablability(String availablability) {
        this.availablability = availablability;
    }
}
