package cs310MRAK.rackcity.client;

import java.util.ArrayList;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler.MarkerClickEvent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.Maps;


public class RackCityMap {

	
	Maps m;
	LatLng address = null;
	
	//========================== OVERLAY ==============================
	
	// add markers onto the map. Add marker overlay for each latlng within a list, center at address
	public void addMarker(ArrayList<LatLng> pos)
	{
		MapWidget centerPoint = moveCamera(address);
		
		for (int i = 0; i < pos.size(); i++)
		{
			Marker mark = new Marker(pos.get(i));
			/* set listener if the marker is pressed (single)
			mark.addMarkerClickHandler(new MarkerClickHandler() {
				@Override
				public void onClick(MarkerClickEvent event) {
					
				}
			});
			*/
			centerPoint.addOverlay(mark);
		}
	}
	
	
	public void drawRadius(int radius, LatLng center, MapWidget m)
	{
		
	}
	
	//========================= MAP OPs ================================
	
	// pan and zoom camera onto the the address searched by user/or to ubc as default
	public MapWidget moveCamera(LatLng pos)
	{
		final MapWidget centerPoint;
		if (pos != null)
		{
			centerPoint = new MapWidget(pos, 14);
		}
		else
		{
			LatLng ubc = LatLng.newInstance(49.272733,-123.255036);  //set default
			centerPoint = new MapWidget(ubc, 14);
		}
		return centerPoint;
	}
	
	// ====================== POSITION FUNC =============================
	
	//conver string to lat long
	public LatLng geoForwarding(String addr)
	{
		if (!addr.isEmpty())		// may have to change this to check if the address is valid or not
		{
			
		}
		return null;
	}
}
