package auction.org.droidflatauction;

import com.auction.dto.Role;

/**
 * Created by bdlions on 09/09/2017.
 */

public class RoleDTO extends Role
{
    boolean isSelected;
    String  role;

    public RoleDTO(boolean isSelected, String role) {
        this.isSelected = isSelected;
        this.role = role;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
