package auction.org.droidflatauction;

/**
 * Created by bdlions on 15/06/2017.
 */

public class Place {
    private static String place;
    public Place(String place){
        this.setPlace(place);
    }

    public static String getPlace() {
        return place;
    }

    public static void setPlace(String place) {
        Place.place = place;
    }


}
