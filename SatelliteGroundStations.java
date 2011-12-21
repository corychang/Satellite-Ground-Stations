package com.spacexproject;

import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.http.client.*;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import gov.nasa.gsfc.spdf.ssc.client.*;


public class SatelliteGroundStations {
	
	public static void main(String args[]) throws Exception 
    {
		/*
		 * The only argument main takes is the url:
		 * http://sscWeb.gsfc.nasa.gov/WS/ssc/2/SatelliteSituationCenterService?wsdl
		 */
        SatelliteSituationCenterService service =
            new SatelliteSituationCenterService(
                new URL(args[0]),
                new QName("http://ssc.spdf.gsfc.nasa.gov/",
                          "SatelliteSituationCenterService"));

        SatelliteSituationCenterInterface ssc =
            service.getSatelliteSituationCenterPort();
        
         //The SatelliteSituationCenterInterface object uses getAllGroundStations()
        List<GroundStationDescription> stations = ssc.getAllGroundStations();
        
        printAllGroundStations(stations);
        
        mapAllGroundStations(stations);
    }
    
    /* 
     * This method loops through the list of all of the stations and prints out
     * their information in the order of ID, latitude, longitude, and name.
     */
    public static void printAllGroundStations(List<GroundStationDescription> stations)
    {
        for(GroundStationDescription station:stations) 
        {
            System.out.println(
                "  " + station.getId() +
                "  " + station.getLatitude() + 
                "  " + station.getLongitude() +
                "  " + station.getName());
        }
    }
    
    /*
     * Loop through the list of all stations, get their latitude and longitude,
     * and map using the Google Maps API
     */
    public static void mapAllGroundStations(List<GroundStationDescription> stations)
    {
    	// Create a map with the ability to zoom in and out
    	MapWidget map = new MapWidget();
    	map.setSize("100%", "100%");
    	map.addControl(new LargeMapControl());
    	// Loop through all of the stations and add a marker at their latitude/longitude
    	for(GroundStationDescription station:stations)
    	{
    		LatLng temp = LatLng.newInstance(station.getLatitude(), station.getLongitude());
    		/*
    		 * The url for the request to perform reverse geocoding is formed given the same
    		 * initial string, the latitude and longitude of the station separated by commas,
    		 * and the same end string.
    		 */
    		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" +
    				station.getLatitude() + "," +
    				station.getLongitude() + 
    				"&sensor=true_or_false";
    		/*
    		 * Once the string from the url request is retrieved, the only important information
    		 * contained within is the country data. This corresponds to the file containing the
    		 * picture to place onto the map.
    		 */
    		String country = getStationCountry(station, url);
    		//this should be changed to display a flag
    		map.addOverlay(new Marker(temp));
    	}
    	DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
    	dock.addNorth(map, 500);
    	RootLayoutPanel.get().add(dock);
    }
    
    /*
     * This method should, through the url, take the JSON string and retrieve the country
     * information. The two letter acronym should match the file name of the icon to be
     * used.
     */
    public static String getStationCountry(GroundStationDescription station, String url)
    {
    	RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    	
    	return "";
    }

}
