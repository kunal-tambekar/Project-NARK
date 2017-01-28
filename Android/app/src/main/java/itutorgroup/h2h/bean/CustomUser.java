package itutorgroup.h2h.bean;

import java.util.ArrayList;

/**
 * Created by kunaltambekar on 1/28/17.
 */

public class CustomUser {
    String email;
    String name;
    String apiToken;
    String token;
    int uid;
    private  ArrayList<String> cities = new ArrayList<>();

    public CustomUser() {
        cities.add("San Francisco");
        cities.add("Beijing");
        cities.add("New Delhi");
        cities.add("Chicago");
    }

    public  ArrayList<String> getCities() {
        return cities;
    }

    public  void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
