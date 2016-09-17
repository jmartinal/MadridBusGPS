package com.jmartinal.madridbusgps.Utils;

/**
 * Created by Jorge on 18/09/2015.
 */
public class Constants {

    public static final int PERMISSION_REQUEST_LOCATION = 100;

    public static final String BUS_LINES_FILE_DIR = "MadridBusGPS";
    public static final String BUS_LINES_FILE_NAME = "BusLinesInfo";

    public static final String MAPQUEST_KEY = "key=Orm06a3qba7whZAI3MOzYMuBJ5PMrvDy";

    public static final String GEOCODING_BASE_URL = "http://www.mapquestapi.com/geocoding/v1/address?";
    public static final String GEOCODING_STREET = "&street=";
    public static final String GEOCODING_MAX_RESULTS = "&maxResults=10";
    public static final String GEOCODING_COMMUNITY_FILTER = "&state=Community+of+Madrid";
    public static final String GEOCODING_COUNTRY_FILTER = "&country=ES";

    public static final String REVERSE_GEOCODING_BASE_URL = "http://www.mapquestapi.com/geocoding/v1/reverse?";
    public static final String REVERSE_GEOCODING_LAT = "&lat=";
    public static final String REVERSE_GEOCODING_LNG = "&lng=";

    public static final String ROUTING_BASE_URL = "http://open.mapquestapi.com/directions/v2/route?";
    public static final String ROUTING_FROM = "&from=";
    public static final String ROUTING_TO = "&to=";
    public static final String ROUTING_ROUTE_TYPE = "&routeType=pedestrian";
    public static final String ROUTING_SHAPE_FORMAT = "&shapeFormat=raw";
    public static final String ROUTING_GENERALIZE = "&generalize=1";
    public static final String ROUTING_LANGUAGE = "&locale=es_ES";
    public static final String ROUTING_UNIT = "&unit=k";

    public static final String EMT_API_BUS_SERVICE_URL = "https://openbus.emtmadrid.es:9443/emt-proxy-server/last/bus/";
    public static final String EMT_API_GEO_SERVICE_URL = "https://openbus.emtmadrid.es:9443/emt-proxy-server/last/geo/";
    public static final String EMT_API_ID_CLIENT_KEY = "idClient";
    public static final String EMT_API_ID_CLIENT_VALUE = "WEB.SERV.jorgemartinalvarado@gmail.com";
    public static final String EMT_API_PASSKEY_KEY = "passKey";
    public static final String EMT_API_PASSKEY_VALUE = "DACF9690-5422-4697-A673-9BF476D02C05";

}
