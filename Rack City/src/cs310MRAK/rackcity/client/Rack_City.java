package cs310MRAK.rackcity.client;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Rack_City implements EntryPoint {

	private static MapWidget googleMap = null;
	private static DockPanel dockPanel = null;
	private static Marker currentRack = null;
	private Filter filters;
	private ArrayList<BikeRack> currentSearchList = null;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		RootPanel rootPanel = RootPanel.get();
		rootPanel.setSize("1160px", "637px");
		dockPanel = new DockPanel();
		rootPanel.add(dockPanel, 10, 0);
		dockPanel.setSize("1150px", "627px");

		/*
		 * Filling out the Root and Dock Panels with Horizontal and Absolute Panels
		 */
		HorizontalPanel leftUserInputPanel = new HorizontalPanel();
		dockPanel.add(leftUserInputPanel, DockPanel.WEST);
		dockPanel.setCellVerticalAlignment(leftUserInputPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		leftUserInputPanel.setSize("200px", "500px");
		leftUserInputPanel.setBorderWidth(1);

		AbsolutePanel userInputPanel = new AbsolutePanel();
		leftUserInputPanel.add(userInputPanel);
		userInputPanel.setSize("200px", "500px");

		VerticalPanel centerRackViewPanel = new VerticalPanel();
		dockPanel.add(centerRackViewPanel, DockPanel.CENTER);
		dockPanel.setCellVerticalAlignment(centerRackViewPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		centerRackViewPanel.setSize("700px", "500px");

		final AbsolutePanel rackViewPanel = new AbsolutePanel();
		centerRackViewPanel.add(rackViewPanel);
		rackViewPanel.setSize("700px","500px");

		VerticalPanel verticalPanel_1 = new VerticalPanel();
		dockPanel.add(verticalPanel_1, DockPanel.NORTH);
		verticalPanel_1.setSize("696px", "0px");

		HorizontalPanel rightRackClickPanel = new HorizontalPanel();
		dockPanel.add(rightRackClickPanel, DockPanel.EAST);
		dockPanel.setCellHorizontalAlignment(rightRackClickPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		dockPanel.setCellVerticalAlignment(rightRackClickPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		rightRackClickPanel.setSize("250px", "500px");


		/*
		 * Adding UI elements to each panel
		 */
		final TextBox txtbxAddress = new TextBox();
		txtbxAddress.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(txtbxAddress.getText().equals("Enter Address Here.")){
					txtbxAddress.setText("");
				}
			}
		});
		txtbxAddress.setText("Enter Address Here.");
		userInputPanel.add(txtbxAddress, 20, 96);
		txtbxAddress.setSize("145px", "18px");
		txtbxAddress.setTabIndex(3);

		Button datasheetViewButton = new Button("datasheetViewButton");
		datasheetViewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if(currentRack != null){
					((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
					currentRack = null;
				}

				googleMap.setVisible(false);
				((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).remove(0);

				//TODO INSERT CODE HERE to add the fetched list from the filter class
				final ListBox rackList = new ListBox();
				rackList.addChangeHandler(new ChangeHandler() {
					public void onChange(ChangeEvent event) {
						rackList.getValue((rackList.getSelectedIndex())); //gets selected value from listbox
					}
				});
				rackList.setSize("700px", "500px");
				rackList.setVisibleItemCount(10);

				//Test Data
				rackList.addItem(LatLng.newInstance(49.249698, -123.139099).toString());
				rackList.addItem(LatLng.newInstance(49.249700, -123.139100).toString());
				rackList.addItem(LatLng.newInstance(49.249702, -123.139102).toString());
				rackList.addItem(LatLng.newInstance(49.249704, -123.139104).toString());
				rackList.addItem(LatLng.newInstance(49.249706, -123.139106).toString());

				((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).add(rackList);
			}
		});
		datasheetViewButton.setText("Datasheet View");
		userInputPanel.add(datasheetViewButton, 100, 0);
		datasheetViewButton.setSize("84px", "44px");
		datasheetViewButton.setTabIndex(2);

		Button mapViewButton = new Button("mapViewButton");
		mapViewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				//TODO INSERT CODE HERE to handle when user wants to see the map view of the bike racks
				((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).remove(0);

				final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
				dock.addNorth(googleMap, 500);
				((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).add(dock);
				googleMap.setVisible(true);
			}
		});
		mapViewButton.setText("Map View");
		userInputPanel.add(mapViewButton, 10, 0);
		mapViewButton.setSize("84px", "44px");
		mapViewButton.setTabIndex(1);

		Label dsntAddressLbl = new Label("Search Destination Address:");
		dsntAddressLbl.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		userInputPanel.add(dsntAddressLbl, 10, 72);
		dsntAddressLbl.setSize("191px", "18px");

		Label lblFilterResults = new Label("Filter Results:");
		userInputPanel.add(lblFilterResults, 10, 145);
		lblFilterResults.setSize("147px", "18px");

		final ListBox radiusCombo = new ListBox();
		radiusCombo.addItem("");
		radiusCombo.addItem("0.5");
		radiusCombo.addItem("1");
		radiusCombo.addItem("2");
		userInputPanel.add(radiusCombo, 24, 193);
		radiusCombo.setSize("151px", "22px");
		radiusCombo.setTabIndex(4);

		Label lblRadius = new Label("Radius (km):");
		userInputPanel.add(lblRadius, 24, 169);

		Label lblCrimeScore = new Label("Crime Score:");
		userInputPanel.add(lblCrimeScore, 24, 221);
		lblCrimeScore.setSize("151px", "18px");

		final ListBox crimeCombo = new ListBox();
		crimeCombo.addItem("");
		crimeCombo.addItem("0");
		crimeCombo.addItem("1");
		crimeCombo.addItem("2");
		crimeCombo.addItem("3");
		crimeCombo.addItem("4");
		crimeCombo.addItem("5");
		userInputPanel.add(crimeCombo, 24, 245);
		crimeCombo.setSize("151px", "22px");
		crimeCombo.setTabIndex(5);

		Label lblRating = new Label("Rating:");
		userInputPanel.add(lblRating, 24, 277);
		lblRating.setSize("44px", "18px");

		final ListBox ratingCombo = new ListBox();
		ratingCombo.addItem("");
		ratingCombo.addItem("0");
		ratingCombo.addItem("1");
		ratingCombo.addItem("2");
		ratingCombo.addItem("3");
		ratingCombo.addItem("4");
		ratingCombo.addItem("5");
		userInputPanel.add(ratingCombo, 24, 301);
		ratingCombo.setSize("151px", "22px");
		ratingCombo.setTabIndex(5);

		Button searchButton = new Button("searchButton");
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!txtbxAddress.getText().equals("")){
					if(!radiusCombo.getValue(radiusCombo.getSelectedIndex()).equals("")){
						if(!crimeCombo.getValue(crimeCombo.getSelectedIndex()).equals("")){
							if(!ratingCombo.getValue(ratingCombo.getSelectedIndex()).equals("")){

								googleMap.clearOverlays();
								addMapOverlay(txtbxAddress.getText(), 
										Double.parseDouble(radiusCombo.getValue(radiusCombo.getSelectedIndex())),
										Integer.parseInt(crimeCombo.getValue(crimeCombo.getSelectedIndex())), 
										Integer.parseInt(ratingCombo.getValue(ratingCombo.getSelectedIndex())));

							}else
								Window.alert("No Rating selected!");
						}else
							Window.alert("No Crime Score selected!");
					}else
						Window.alert("No Radius selected!");
				}else
					Window.alert("No Address entered!");
			}
		});
		searchButton.setText("Search");
		userInputPanel.add(searchButton, 118, 348);

		loadGoogleMap();
	}

	private void loadGoogleMap(){
		Maps.loadMapsApi("", "2", false, new Runnable() { public void run() {
			// This code is used to create lat/long data points
			LatLng def = LatLng.newInstance(49.249697, -123.139098); //set default

			googleMap = new MapWidget(def, 12);
			googleMap.setCenter(def);
			googleMap.setVisible(false);
			googleMap.setVisible(true);
			googleMap.addMapClickHandler(new MapClickHandler() {
				public void onClick(MapClickEvent event) {
					Marker tmpRack = ((Marker) event.getOverlay());

					if(tmpRack != null && currentRack != null && !tmpRack.equals(currentRack)){
						((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
						currentRack = tmpRack;
						clickRackDisplayPanel(currentRack.getLatLng());

					}else if (tmpRack != null && currentRack == null){
						currentRack = tmpRack;
						clickRackDisplayPanel(currentRack.getLatLng());

					}else if(tmpRack != null && tmpRack.equals(currentRack)){

					}else{
						currentRack = null;
						((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
						((HorizontalPanel) dockPanel.getWidget(3)).setBorderWidth(0);
					}
				}
			});

			googleMap.setSize("100%", "100%");

			// Add some controls for the zoom level
			googleMap.addControl(new LargeMapControl());

			// Add the map to the HTML host page
			final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
			dock.addNorth(googleMap, 500);
			((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).add(dock);

		}});
	}

	/**
	 * Generates markers for all bike racks based on specified user inputs
	 * @param address - Street address of user's current location
	 * @param radius - The farthest distance from address to display bike racks
	 * @param crimeScore - Only display racks that have > input crime score
	 * @param rating - Only display racks that have > input rating
	 */
	private void addMapOverlay(String address, final double radius, final int crimeScore, final double rating){

		//Geocodes the address that the user inputs and creates a latlong opbject
		Geocoder latLongAddress = new Geocoder();
		latLongAddress.getLatLng(address, new LatLngCallback() {
			@Override
			public void onFailure() {
				Window.alert("Sorry, we were unable to find that address");
			}

			@Override
			public void onSuccess(LatLng point) {
				googleMap.setCenter(point);
				googleMap.setZoomLevel(14);
				displayRadius(point, radius);

				googleMap.addOverlay(addMarker(point, 2));
				
				/*
				 * The following code should get the appropriate list based on all the preconditions.
				 */
				currentSearchList = filters.completeFilteredList(radius, crimeScore, rating);

				/*
				 * The following code should plot the position of each rack within the given 
				 * radius. This needs to be tested once the parser is functional.
				 */
				for (BikeRack rack : currentSearchList) {
					googleMap.addOverlay(addMarker(rack.getCoordinate(), 2));
				}
			}
		});
		return;
	}

	/**
	 * Method called when a rack on a map is clicked.
	 * Displays the 'Report Crime' button and title of rack in a panel on the right of the page
	 */
	private void clickRackDisplayPanel(LatLng rack){

		final AbsolutePanel rackClickPanel = new AbsolutePanel();
		((HorizontalPanel) dockPanel.getWidget(3)).add(rackClickPanel);
		rackClickPanel.setSize("250px", "500px");

		Button reportCrimeButton = new Button("reportCrimeButton");
		reportCrimeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//TODO INSERT CODE HERE to handle when user wants to report a crime
				// TODO: stolenBikesServiceAsync !!!
				// pesudo
				// Get value from server with request (LatLng)
				// add one (value += 1)
				// update server update (LatLng, value)

				Window.alert("Report Crime Pressed!");
			}
		});
		reportCrimeButton.setText("Report Crime");
		rackClickPanel.add(reportCrimeButton, 80, 425);

		//Reverse Geocodes the rack location into an address for the user to see
		Geocoder latLongAddress = new Geocoder();
		latLongAddress.getLocations(rack, new LocationCallback() {
			@Override
			public void onFailure(int statusCode) {

			}

			@Override
			public void onSuccess(JsArray<Placemark> locations) {
				Label rackAddress = new Label(locations.get(0).getAddress());
				rackAddress.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				rackClickPanel.add(rackAddress, 0, 0);
				rackAddress.setSize("250px", "54px");
			}
		});

		//Add code to get # of stolen bikes for a particular rack
		Label rackStolenBikesLabel = new Label("# of Stolen Bikes in this Location: ");
		rackStolenBikesLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rackClickPanel.add(rackStolenBikesLabel, 0, 108);
		rackStolenBikesLabel.setSize("250px", "54px");

		//Add code to get rating for a particular rack
		Label rackRatingLabel = new Label("Bike Rack Rating (out of 5): ");
		rackRatingLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rackClickPanel.add(rackRatingLabel, 0, 162);
		rackRatingLabel.setSize("250px", "54px");

		((HorizontalPanel) dockPanel.getWidget(3)).setBorderWidth(1);

		dockPanel.setVisible(false);
		dockPanel.setVisible(true);
	}

	/**
	 * Code taken from https://groups.google.com/forum/#!topic/google-web-toolkit/ljSNdmK8dAw
	 * Takes in a center point and radius and displays a radius circle on the map
	 * @param center
	 * @param radius
	 */
	private void displayRadius(LatLng center, double radius) {

		LatLng[] circlePoints = new LatLng[361];
		Polygon circle;

		double PI = 3.1415;

		double d = radius / 6378.8; // radians

		double lat1 = (PI / 180) * center.getLatitude(); // radians
		double lng1 = (PI / 180) * center.getLongitude(); // radians

		for (int a = 0; a < 361; a++) {
			double tc = (PI / 180) * a;
			double y = Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1)
					* Math.sin(d) * Math.cos(tc));
			double dlng = Math.atan2(Math.sin(tc) * Math.sin(d)
					* Math.cos(lat1), Math.cos(d) - Math.sin(lat1)
					* Math.sin(y));
			double x = ((lng1 - dlng + PI) % (2 * PI)) - PI; // MOD function
			LatLng point = LatLng.newInstance(y * (180 / PI), x * (180 / PI));
			circlePoints[a] = point;
		}

		circle = new Polygon(circlePoints, "#000000", 1, 1.0, "#aaaaaa", .4);
		googleMap.addOverlay(circle);
	}

	// google icon file from here: https://sites.google.com/site/gmapicons/
	// add markers onto the map. Add marker overlay for each latlng within a list, center at address
	public Marker addMarker(LatLng pos, int type)
	{
		Marker mark = new Marker(pos);
		if (type == 1)		// search address: ME
		{
			mark.setImage("http://labs.google.com/ridefinder/images/mm_20_blue.png");
		}
		else if (type == 2)		// bike racks: GREEN, !!!!! SHOULD HAVE DIFFERENT COLOR BASED ON RACK#
		{
			mark.setImage("http://labs.google.com/ridefinder/images/mm_20_green.png");
		}
		else if (type == 3)		// crime place: RED
		{
			mark.setImage("http://labs.google.com/ridefinder/images/mm_20_red.png");
		}
		/* set listener if the marker is pressed (single)
				mark.addMarkerClickHandler(new MarkerClickHandler() {
					@Override
					public void onClick(MarkerClickEvent event) {

					}
				});
		 */
		return mark;
	}
}
