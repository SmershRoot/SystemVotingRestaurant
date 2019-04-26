package ru.smv.system.restaurant.constants;

public class AccessPath {

    public static final String API = "/api";
    public static final String API_USERS = API + "/users";
    public static final String API_USERS_SUD = API_USERS + "/{userId}";
    public static final String API_USERS_SUD_PASSWORD = API_USERS_SUD + "/password";

    public static final String API_RESTAURANTS = API + "/restaurants";
    public static final String API_RESTAURANTS_SUD = API_RESTAURANTS + "/{restaurantId}";

    public static final String API_RESTAURANTS_SUD_VOTING = API_RESTAURANTS_SUD + "/voting";

    public static final String API_RESTAURANTS_VOTING = API_RESTAURANTS + "/voting" + "/{reportDate}";
}
