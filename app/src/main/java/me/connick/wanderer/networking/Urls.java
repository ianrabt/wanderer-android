package me.connick.wanderer.networking;

/**
 * Created by connick on 2/8/15.
 */
public class Urls {

    private Urls() {}

    private static String URL_BASE = "http://192.168.91.135:8000/api/";

    public static String getLocationsURL() {
        return URL_BASE + "locations/?format=json";
    }

    public static String getLocationURL(int id) {
        return URL_BASE + "locations/" + id + "/?format=json";
    }
}
