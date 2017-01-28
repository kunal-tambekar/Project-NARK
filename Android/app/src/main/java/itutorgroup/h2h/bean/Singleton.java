package itutorgroup.h2h.bean;

/**
 * Created by kunaltambekar on 1/28/17.
 */

public class Singleton {
    public static CustomUser currentUser;

    public static CustomUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(CustomUser currentUser) {
        Singleton.currentUser = currentUser;
    }


}
