package auction.org.droidflatauction;

/**
 * Created by bdlions on 29/08/2017.
 */

public class AvailablabilityModel {
    boolean isSelected;
    String  availablability;

    public AvailablabilityModel(boolean isSelected, String availablability) {
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
