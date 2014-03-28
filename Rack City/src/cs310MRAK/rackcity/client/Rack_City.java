package cs310MRAK.rackcity.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;
//======================== ^ G+ ============================
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
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
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;

import cs310MRAK.rackcity.shared.UserSearchHistoryInstance;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Rack_City implements EntryPoint {
	private ListHandler<BikeRack> sortHandler = new ListHandler<BikeRack>(Collections.<BikeRack>emptyList());
	private RootPanel rootPanel;
	private MapWidget googleMap = null;
	private DockPanel dockPanel = null;
	private Marker currentMarker = null;
	private String currentDatasheetItem = null;
	private Filter filters = null;
	private List<BikeRack> currentRackList = null;
	private List<Crime> currentCrimeList = null;
	private LatLng currentAddress = null;
	private static ArrayList<BikeRack> listofracks = null;
	private static ArrayList<Crime> listofcrimes = null;
	boolean filter = false;
	boolean isCrimeShown = false;
	//private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	//	private static final Logger LOG = Logger.getLogger(Rack_City.class.getName());
	// google+ login stuff ------ S2 ---------
	private static final String CLIENT_ID = "146858113551-ktl431gm3sbkrvid1khqrlvh1afclct4.apps.googleusercontent.com";
	private static final String API_KEY = "AIzaSyCeb8Iws1UqI8caz2aHJee_JtTTiNdqqAY";
	private static final String APPLICATION_NAME = "cs310rackcity";
	private int loginFlipFlop = 0;
	private int loginAttempt = 0;
	private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	private static final String PLUS_ME_SCOPE = "https://www.googleapis.com/auth/plus.me";
	private static final String FRIEND_SCOPE = "https://www.googleapis.com/auth/plus.login";
	private static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/plus.profile.emails.read";
	private static final Auth AUTH = Auth.get();
	public int w = 0;
	// User information
	private String userEmail = "";
	private String userName = "";
	private String userToken = "";
	private String userId = "";
	private String userImageURL = "";
	private String userGender = "";
	private Boolean userIsPlus = false;
	private ArrayList<String[]> userFriends = new ArrayList<String[]>();
	private ArrayList<BikeRack> favRacks = new ArrayList<BikeRack>();
	private ArrayList< ArrayList<String>> favRacksCommon = new ArrayList< ArrayList<String>>();
	private ArrayList<UserSearchHistoryInstance> userHistory = new ArrayList<UserSearchHistoryInstance>();

	// Server stuff
	private rackServiceAsync rService = GWT.create(rackService.class);
	private crimeServiceAsync cService = GWT.create(crimeService.class);
	private userServiceAsync uService = GWT.create(userService.class);
	private RackFavouritesServiceAsync fService = GWT.create(RackFavouritesService.class);
	private int initialsync = 0;
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// ========== Issue #72 ==========
		URLserviceAsync ftpService = GWT.create(URLservice.class);
		AsyncCallback callback = new AsyncCallback<Void>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Failed URL.");
				handleError(error);
			}
			public void onSuccess(Void ignore)
			{
				Window.alert("Success URL");
			}};
			ftpService.adminConnection(callback);


			if (initialsync == 0)
			{	
				addtolist();
				initialsync = 1;
			}

			GUIsetup();

	}

	private void GUIsetup()
	{
		rootPanel = RootPanel.get();
		rootPanel.setSize("1000px", "700px");
		dockPanel = new DockPanel();
		rootPanel.add(dockPanel, 10, 0);
		dockPanel.setSize("1000px", "700px");

		//* Filling out the Root and Dock Panels with Horizontal and Absolute Panels

		HorizontalPanel leftUserInputPanel = new HorizontalPanel();
		leftUserInputPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		dockPanel.add(leftUserInputPanel, DockPanel.WEST);
		dockPanel.setCellVerticalAlignment(leftUserInputPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		leftUserInputPanel.setSize("200px", "600px");
		leftUserInputPanel.setBorderWidth(1);

		final AbsolutePanel userInputPanel = new AbsolutePanel();
		leftUserInputPanel.add(userInputPanel);
		userInputPanel.setSize("200px", "500px");

		VerticalPanel centerRackViewPanel = new VerticalPanel();
		dockPanel.add(centerRackViewPanel, DockPanel.CENTER);
		dockPanel.setCellVerticalAlignment(centerRackViewPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		centerRackViewPanel.setSize("700px", "500px");

		final AbsolutePanel rackViewPanel = new AbsolutePanel();
		centerRackViewPanel.add(rackViewPanel);
		rackViewPanel.setSize("700px","500px");

		VerticalPanel titlePanel = new VerticalPanel();
		dockPanel.add(titlePanel, DockPanel.NORTH);
		titlePanel.setSize("696px", "40px");

		final AbsolutePanel titleViewPanel = new AbsolutePanel();
		titlePanel.add(titleViewPanel);
		titleViewPanel.setSize("696px","40px");

		HorizontalPanel rightRackClickPanel = new HorizontalPanel();
		dockPanel.add(rightRackClickPanel, DockPanel.EAST);
		dockPanel.setCellHorizontalAlignment(rightRackClickPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		dockPanel.setCellVerticalAlignment(rightRackClickPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		rightRackClickPanel.setSize("250px", "500px");



		//* Adding UI elements to each panel

		final Button loginButton = new Button("loginButton");
		loginButton.setText("Login");

		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				startLoginProcess(loginButton);
				getUserSearchHistory(userId);

			}
		});
		loginButton.setText("Login");
		titleViewPanel.add(loginButton, 500, 5);


		Button adminButton = new Button("adminButton");
		adminButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				w++;
				if (w == 3)
				{
					messenger("You are 1 click away from requesting for a refectch!");
				}
				if (w == 4)
				{
					if (userEmail.equals("robwu15@gmail.com") || userEmail.equals("obedientworker@gmail.com") || userEmail.equals("kevin.david.greer@gmail.com") || userEmail.equals("abhisek.pradhan91@gmail.com"))
					{
						messenger("Refetch Request Approved");
						addtolist();
					}
					else
						// dude, amazing. 
						messenger("Refetch Request Denied. You are not a registered admin");
					w = 0;
				}
			}
		});
		adminButton.setText("Admin");
		titleViewPanel.add(adminButton, 575, 5);

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

				if(currentAddress == null){
					Window.alert("No Address searched! Please search an address first before entering DataSheet view.");
					return;
				}

				if(currentMarker != null){
					((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
					((HorizontalPanel) dockPanel.getWidget(3)).setBorderWidth(0);
					currentMarker = null;
				}

				if(currentRackList != null || !currentRackList.isEmpty()){
					googleMap.setVisible(false);
					((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).remove(0);

					//TODO INSERT CODE HERE to add the fetched list from the filter class
					DataGrid<BikeRack> rackDataGrid = new DataGrid<BikeRack>();
					rackDataGrid.addColumnSortHandler(sortHandler);
					rackViewPanel.add(rackDataGrid, 0, 0);
					rackDataGrid.setSize("700px", "500px");

					TextColumn<BikeRack> addressCol = new TextColumn<BikeRack>() {
						@Override
						public String getValue(BikeRack rack) {
							return rack.getAddress();
						}
					};
					addressCol.setSortable(true);
					rackDataGrid.addColumn(addressCol, "Address");
					sortHandler.setComparator(addressCol, new Comparator<BikeRack>() {
						public int compare(BikeRack o1, BikeRack o2) {
							//implement comparator for address
							return 0;
						}
					});


					TextColumn<BikeRack> coordinatesCol = new TextColumn<BikeRack>() {
						@Override
						public String getValue(BikeRack rack) {
							return rack.getCoordinate().toString();
						}
					};
					rackDataGrid.addColumn(coordinatesCol, "Coordinates");


					TextColumn<BikeRack> distanceCol = new TextColumn<BikeRack>() {
						@Override
						public String getValue(BikeRack rack) {
							return Double.toString(round(calcLatLngDistance(rack.getCoordinate()), 2));
						}
					};
					distanceCol.setSortable(true);
					sortHandler.setComparator(distanceCol, new Comparator<BikeRack>() {
						public int compare(BikeRack r2, BikeRack r1) {
							//implement comparator for distance
							return 0;
						}
					});
					rackDataGrid.addColumn(distanceCol, "Distance from you");


					TextColumn<BikeRack> ratingCol = new TextColumn<BikeRack>() {
						@Override
						public String getValue(BikeRack rack) {
							return Double.toString(rack.getRating());
						}
					};
					ratingCol.setSortable(true);
					sortHandler.setComparator(ratingCol, new Comparator<BikeRack>() {
						public int compare(BikeRack o1, BikeRack o2) {
							//implement comparator for rating
							return 0;
						}
					});
					ratingCol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					rackDataGrid.addColumn(ratingCol, "Rating");

					TextColumn<BikeRack> crimeScoreCol = new TextColumn<BikeRack>() {
						@Override
						public String getValue(BikeRack rack) {
							return Double.toString(round(rack.getCrimeScore(), 1));
						}
					};
					crimeScoreCol.setSortable(true);
					sortHandler.setComparator(crimeScoreCol, new Comparator<BikeRack>() {
						public int compare(BikeRack o1, BikeRack o2) {
							//implement comparator for crime score
							return 0;
						}
					});
					rackDataGrid.addColumn(crimeScoreCol, "Crime Score");

					rackDataGrid.setRowData(currentRackList);
					((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).add(rackDataGrid);

				}


				/*
				final ListBox rackList = new ListBox();
				rackList.addChangeHandler(new ChangeHandler() {
					public void onChange(ChangeEvent event) {
						String temp = rackList.getValue((rackList.getSelectedIndex())); //gets selected value from listbox
						String tmpLat = temp.substring(1, temp.indexOf(","));
						String tmpLng = temp.substring(temp.indexOf(",")+2,temp.length());

						double lat,lng;

						lat = Double.parseDouble(tmpLat);
						lng = Double.parseDouble(tmpLng);

						if(currentDatasheetItem != null && !currentDatasheetItem.equals(temp)){
							currentDatasheetItem = temp;
							((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
							clickRackDisplayPanel(getRack(LatLng.newInstance(lat, lng)));
						}else if (currentDatasheetItem.equals(temp)){
							//do nothing
						}
						else{
							currentDatasheetItem = temp;
							clickRackDisplayPanel(getRack(LatLng.newInstance(lat, lng)));
						}
					}
				});

				rackList.setSize("700px", "500px");
				rackList.setVisibleItemCount(10);

				if(currentRackList != null || !currentRackList.isEmpty()){
					for (BikeRack rack : currentRackList){
						rackList.addItem("(" + rack.getCoordinate().getLatitude() + ", " + 
								rack.getCoordinate().getLongitude() + ") " + "Distance from you (km): " + 
								round(calcLatLngDistance(rack.getCoordinate()), 2));
					}
				}
				 */

			}
		});
		datasheetViewButton.setText("Datasheet View");
		userInputPanel.add(datasheetViewButton, 106, 0);
		datasheetViewButton.setSize("84px", "44px");
		datasheetViewButton.setTabIndex(2);

		Button mapViewButton = new Button("mapViewButton");
		mapViewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if(currentDatasheetItem != null){
					((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
					currentDatasheetItem = null;
				}

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

		Label lblCrimeScore = new Label("Crime Score <=:");
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

		Label lblRating = new Label("Rating >=:");
		userInputPanel.add(lblRating, 24, 277);
		lblRating.setSize("151px", "18px");

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

								// task 45
								if(!(userEmail.isEmpty() && userId.isEmpty())){
									// String userID, String searchAddress, String radius, String crimeScore
									String userID = userId;
									String searchAddress = txtbxAddress.getText();
									int radius = radiusCombo.getSelectedIndex();
									int crimeScore = crimeCombo.getSelectedIndex();
									int rateVal = ratingCombo.getSelectedIndex();
									AddUserSearchHistory(userID, searchAddress, radius, crimeScore, rateVal);
								}
								if(userEmail.isEmpty() && userId.isEmpty()){
									messenger("Please login to save your precious search history! :P");

								}
								// now call the function and will need to make  db parse function to call on it 

								googleMap.clearOverlays();
								currentRackList = null;
								currentCrimeList = null;
								currentAddress = null;

								if(currentMarker != null){
									currentMarker = null;
									((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
									((HorizontalPanel) dockPanel.getWidget(3)).setBorderWidth(0);
								}

								addMapOverlay(txtbxAddress.getText(), 
										Double.parseDouble(radiusCombo.getValue(radiusCombo.getSelectedIndex())),
										Integer.parseInt(crimeCombo.getValue(crimeCombo.getSelectedIndex())), 
										Integer.parseInt(ratingCombo.getValue(ratingCombo.getSelectedIndex())));

								Button showCrimeButton = new Button("showCrimeButton");
								showCrimeButton.setSize("180px", "30px");
								if (isCrimeShown) {
									showCrimeButton.setText("Hide Crime Locations");
								}
								else {
									showCrimeButton.setText("Show Crime Locations");
								}
								showCrimeButton.addClickHandler(new ClickHandler() {
									public void onClick(ClickEvent event) {
										for (Crime crime : currentCrimeList) {
											//System.out.println("Crime Coordinate: " + crime.getCoordinate());
											hideMarker(crime.getCoordinate(), 3);
										}

										showOrHideCrimes();
									}

									private void showOrHideCrimes() {
										if (!isCrimeShown) {
											isCrimeShown = true;
											for (Crime crime : currentCrimeList) {
												//System.out.println("Crime Coordinate: " + crime.getCoordinate());
												hideMarker(crime.getCoordinate(), 3);
											}
										}
										else {
											isCrimeShown = false;
											for (Crime crime : currentCrimeList) {
												//System.out.println("Crime Coordinate: " + crime.getCoordinate());
												addMarker(crime.getCoordinate(), 3);
											}
										}
									}
								});
								userInputPanel.add(showCrimeButton, 10, 400);

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
					Overlay ovr = event.getOverlay();

					if(ovr != null && ovr.getClass().toString().contains("Marker")){

						Marker tmpRack = ((Marker) event.getOverlay());

						if(getRack(tmpRack.getLatLng()) != null && tmpRack != null && !latlngCompare(tmpRack.getLatLng(), currentAddress)){
							if(currentMarker != null && !tmpRack.equals(currentMarker)){
								((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
								currentMarker = tmpRack;
								clickRackDisplayPanel(getRack(currentMarker.getLatLng()));

							}else if (currentMarker == null){
								currentMarker = tmpRack;
								clickRackDisplayPanel(getRack(currentMarker.getLatLng()));

							}else if(tmpRack.equals(currentMarker)){
								//do nothing
							}
						}else{
							if(currentMarker != null){
								currentMarker = null;
								((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
								((HorizontalPanel) dockPanel.getWidget(3)).setBorderWidth(0);
							}
						}
					}else{
						if(currentMarker != null){
							currentMarker = null;
							((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
							((HorizontalPanel) dockPanel.getWidget(3)).setBorderWidth(0);
						}
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
	private void addMapOverlay(final String address, final double radius, final double crimeScore, final double rating){

		//Geocodes the address that the user inputs and creates a latlong opbject
		Geocoder latLongAddress = new Geocoder();
		latLongAddress.getLatLng(address, new LatLngCallback() {
			@Override
			public void onFailure() {
				Window.alert("Sorry, we were unable to find that address");
			}

			@Override
			public void onSuccess(LatLng point) {

				currentAddress = point;
				googleMap.setCenter(currentAddress);
				googleMap.setZoomLevel(14);
				displayRadius(currentAddress, radius);

				addMarker(point, 1);

				/*
				 * The following code should get the appropriate list based on all the preconditions.
				 */
				if(filters == null){
					filters = new Filter();
				}
				currentCrimeList = filters.getFilteredCrimeList(currentAddress, radius);
				currentRackList = filters.getFilteredRackList(currentAddress, radius, rating, crimeScore);

				if(!currentRackList.isEmpty()){
					for (BikeRack rack : currentRackList) {
						//System.out.println("Rack Coordinate: " + rack.getCoordinate());
						addMarker(rack.getCoordinate(), 2);
					}
				}

				if(!currentCrimeList.isEmpty()){
					if (isCrimeShown = true) {
						for (Crime crime : currentCrimeList) {
							//System.out.println("Crime Coordinate: " + crime.getCoordinate());
							addMarker(crime.getCoordinate(), 3);
						}

						isCrimeShown = false;
					}
				}
			}
		});
		return;
	}

	/**
	 * Method called when a rack on a map is clicked.
	 * Displays the 'Report Crime' button and title of rack in a panel on the right of the page
	 */
	private void clickRackDisplayPanel(BikeRack rack){

		if(rack == null){
			return;
		}

		final AbsolutePanel rackClickPanel = new AbsolutePanel();
		((HorizontalPanel) dockPanel.getWidget(3)).clear();
		((HorizontalPanel) dockPanel.getWidget(3)).add(rackClickPanel);
		rackClickPanel.setSize("250px", "500px");

		final Button reportCrimeButton = new Button("reportCrimeButton");
		reportCrimeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//TODO INSERT CODE HERE to handle when user wants to report a crime
				// TODO: stolenBikesServiceAsync !!!
				// pesudo
				// Get value from server with request (LatLng)
				// add one (value += 1)
				// update server update (LatLng, value)

				//=====================================================================
				// Check login status using login service.
				if(!userEmail.isEmpty()) //TODO loginInfo.isLoggedIn() 
				{
					LatLng incident = currentMarker.getLatLng();
					int numOfStolen = 0;
					// Adds to numberStolenBike of the rack in question
					for (int i = 0; i < currentRackList.size(); i++) 
					{
						BikeRack currentRack = currentRackList.get(i);
						if (currentRack.getCoordinate() == incident) 
						{
							currentRack.addStolenBike();
							numOfStolen = currentRack.getNumberStolenBikes();
							clickRackDisplayPanel(currentRack);
						}
					}
					// =============== UPDATE STOLEN BIKE ON DATASTORE ==================
					String newp = incident.toString();
					if (rService == null) 
					{
						rService = GWT.create(rackService.class);
					}
					reportCrimeButton.setEnabled(false);
					AsyncCallback callback = new AsyncCallback<Void>()
							{
						public void onFailure(Throwable error)
						{
							Window.alert("Server Error!");
							handleError(error);
						}
						public void onSuccess(Void ignore)
						{
							Window.alert("Crime successfully reported!");
						}};

						//rService.getStolen(newp, callback);
						rService.updateStolen(newp, numOfStolen, callback);
						reportCrimeButton.setEnabled(true);
						//Window.alert(" Reported!");
				} 
				//=========================================================================
				else 
				{
					Window.alert("Please login to use this feature.");
					//loadLogin();
				}


			}
		});
		reportCrimeButton.setText("Report Crime");
		rackClickPanel.add(reportCrimeButton, 80, 425);

		//Reverse Geocodes the rack location into an address for the user to see
		Geocoder latLongAddress = new Geocoder();
		latLongAddress.getLocations(rack.getCoordinate(), new LocationCallback() {
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
		Label rackStolenBikesLabel = new Label("# of Stolen Bikes in this Location: " + rack.getNumberStolenBikes());
		rackStolenBikesLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rackClickPanel.add(rackStolenBikesLabel, 0, 108);
		rackStolenBikesLabel.setSize("250px", "54px");

		//Add code to get rating for a particular rack
		Label rackRatingLabel = new Label("Bike Rack Rating (out of 5): " + rack.getRating());
		rackRatingLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rackClickPanel.add(rackRatingLabel, 0, 162);
		rackRatingLabel.setSize("250px", "54px");

		Label crimeRatingLabel = new Label("Bike Rack Crime Score (out of 5): " + rack.getCrimeScore());
		crimeRatingLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rackClickPanel.add(crimeRatingLabel, 0, 216);
		crimeRatingLabel.setSize("250px", "54px");

		Label rackCountRatingLabel = new Label("Bike Rack Count: " + rack.getRackCount());
		rackCountRatingLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rackClickPanel.add(rackCountRatingLabel, 0, 270);
		rackCountRatingLabel.setSize("250px", "54px");

		Label distanceLabel = new Label("Distance from you (km): " + round(calcLatLngDistance(rack.getCoordinate()), 2));
		distanceLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rackClickPanel.add(distanceLabel, 0, 324);
		distanceLabel.setSize("250px", "54px");

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
	private void addMarker(LatLng pos, int type)
	{
		if (type == 1)		// search address: ME (blue)
		{
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			markerOptions.setIcon(Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_blue.png"));
			if (!userImageURL.isEmpty() || userImageURL == null)  
				markerOptions.setIcon(Icon.newInstance(userImageURL));
			Marker mark = new Marker(pos, markerOptions);
			googleMap.addOverlay(mark);
		}
		else if (type == 2)		// bike racks: GREEN, !!!!! SHOULD HAVE DIFFERENT COLOR BASED ON RACK#
		{
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			markerOptions.setIcon(Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_green.png"));
			Marker mark = new Marker(pos, markerOptions);
			googleMap.addOverlay(mark);
		}
		else if (type == 3)		// crime place: RED
		{
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			markerOptions.setIcon(Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_red.png"));
			Marker mark = new Marker(pos, markerOptions);
			googleMap.addOverlay(mark);
		}
		/* set listener if the marker is pressed (single)
				mark.addMarkerClickHandler(new MarkerClickHandler() {
					@Override
					public void onClick(MarkerClickEvent event) {

					}
				});
		 */
	}

	// google icon file from here: https://sites.google.com/site/gmapicons/
	// add markers onto the map. Add marker overlay for each latlng within a list, center at address
	private void hideMarker(LatLng pos, int type)
	{
		if (type == 1)		// search address: ME (blue)
		{
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			markerOptions.setIcon(Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_blue.png"));
			if (!userImageURL.isEmpty() || userImageURL == null)  
				markerOptions.setIcon(Icon.newInstance(userImageURL));
			Marker mark = new Marker(pos, markerOptions);
			googleMap.removeOverlay(mark);
		}
		else if (type == 2)		// bike racks: GREEN, !!!!! SHOULD HAVE DIFFERENT COLOR BASED ON RACK#
		{
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			markerOptions.setIcon(Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_green.png"));
			Marker mark = new Marker(pos, markerOptions);
			googleMap.removeOverlay(mark);
		}
		else if (type == 3)		// crime place: RED
		{
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			markerOptions.setIcon(Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_red.png"));
			Marker mark = new Marker(pos, markerOptions);
			googleMap.removeOverlay(mark);
		}
		/* set listener if the marker is pressed (single)
					mark.addMarkerClickHandler(new MarkerClickHandler() {
						@Override
						public void onClick(MarkerClickEvent event) {

						}
					});
		 */
	}

	/**
	 * Code adapted from http://stackoverflow.com/questions/837872/calculate-distance-in-meters-when-you-know-longitude-and-latitude-in-java
	 * Takes in a LatLng point and calculates the distance away from the current searched location
	 * @param rackPoint
	 * @return
	 */
	private double calcLatLngDistance(LatLng rackPoint){

		double lat1, lng1, lat2, lng2;
		lat1 = currentAddress.getLatitude();
		lng1 = currentAddress.getLongitude();
		lat2 = rackPoint.getLatitude();
		lng2 = rackPoint.getLongitude();


		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (double) (dist * meterConversion)/1000;
	}

	/**
	 * Gets the BikeRack from the list of current BikeRacks that matches the lat and long
	 * @param lat
	 * @param lng
	 * @return
	 */
	private BikeRack getRack(LatLng latlng){
		for (BikeRack rack : currentRackList){
			if(rack.getCoordinate().getLatitude() == latlng.getLatitude() && rack.getCoordinate().getLongitude() == latlng.getLongitude()){
				return rack;
			}
		}
		
		return null;
	}

	/**
	 * Returns true if A=B and false if otherwise
	 * @param A
	 * @param B
	 * @return
	 */
	private boolean latlngCompare(LatLng A, LatLng B){
		double latA = A.getLatitude();
		double lngA = A.getLongitude();
		double latB = B.getLatitude();
		double lngB = B.getLongitude();

		if (latA == latB && lngA == lngB){
			return true;
		}

		return false;

	}

	/**
	 * Code adapted from http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places?lq=1
	 * Rounds double values to user-specified decimal places
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	// ===================== SERVER ASYNC CALLS  ==========================

	/**
	 *  Called when user add a rack to favorite
	 */
	private void Add2Fav (String uid, String address, LatLng pos)
	{
		String newP = pos.toString();
		if (fService == null) 
		{
			fService = GWT.create(RackFavouritesService.class);
		}
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (ADD-FAV)");
				handleError(error);
			}
			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				// no messages
			}
				};
				fService.addToFavorite(uid, address, newP, callback);
	}

	private void parseFav (final String name, final String uid)
	{
		if (fService == null) {
			fService = GWT.create(RackFavouritesService.class);
		}

		fService.getFavorite(uid, new AsyncCallback<ArrayList<String[]>>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (PAR-CRIME)");
				handleError(error);
			}
			@Override
			public void onSuccess(ArrayList<String[]> result) {
				// TODO Auto-generated method stub

				if (uid.equals(userId))	 assignFavresult(result);
				else compareFriendFav(name, result);
			}
				});
	}

	private void assignFavresult(ArrayList<String[]> fav)
	{
		if (!(fav.isEmpty() || fav == null))
		{
			for(int i = 0; i < fav.size(); i++)
			{
				//Window.alert("P"+i);
				String[] temp = fav.get(i);
				String LL = temp[1].toString();   // string			
				for (int a = 0; a < listofracks.size(); a++)
				{
					if (listofracks.get(a).coordinate.equals(LL))
					{
						favRacks.add(listofracks.get(a));
						ArrayList<String> tmp = new  ArrayList<String>();
						favRacksCommon.add(tmp);
					}
				}

			}
			getCommonFavorites();
		}
	}

	/**
	 * Called when user log in to determine new or returning
	 */
	// search tag: &^%%
	private void checkUserInfo(final String id, final String name, final String email, final String gender, final Boolean isPlus, final String propic)
	{
		if (uService == null) 
		{
			uService = GWT.create(userService.class);
		}
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (ADD-USER)");
				handleError(error);
			}
			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub
				if (result == false)
				{
					AddUserInfo(id, name, email, gender, isPlus, propic);
				}
			}
				};
				uService.hasUser(id, callback);
	}

	/**
	 *call when new user is logged in to G+
	 */
	private void AddUserInfo(String id, String name, String email, String gender, Boolean isPlus, String propic)
	{
		if (uService == null) 
		{
			uService = GWT.create(userService.class);
		}
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (ADD-USER)");
				handleError(error);
			}
			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				// no messages
			}
				};
				uService.addUser(id, name, email, gender, isPlus, propic, callback);
	}

	/**
	 *call when new user is logged in to G+
	 */
	private void AddUserSearchHistory(String userID, String searchAddress, int radius, int crimeScore, int rate)
	{
		if (uService == null) 
		{
			uService = GWT.create(userService.class);
		}
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (ADD-USER-HISTORY-INSTANCE)");
				handleError(error);
			}
			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				// no messages
			}
				};
				uService.addUserSearchHistoryInstance(userID, searchAddress, radius, crimeScore, rate, callback);
	}
	
	private void getUserSearchHistory(String uid)
	{
		if (!uid.isEmpty())
		{
			//======== PARSE FROM DB =============
			if (uService == null) {
				uService = GWT.create(userService.class);
			}

			uService.getHistory(uid, new AsyncCallback<ArrayList<UserSearchHistoryInstance>>()
			{

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					messenger("Server ERROR: GET-HISTORY");
				}

				@Override
				public void onSuccess(
						ArrayList<UserSearchHistoryInstance> result) {
					// TODO Auto-generated method stub
					assignUserHistory(result);
				}
				
			});
				
		}
	}
	
	private void assignUserHistory(ArrayList<UserSearchHistoryInstance> result)
	{
		userHistory = new ArrayList<UserSearchHistoryInstance>();
		userHistory = result;
	}

	/**
	 * Call rackOps (Admin only): 
	 * type == 0: REMOVE OPERATION. require LatLng
	 * type == 1: ADD OPERATION. require all parameters
	 */
	private void rackOps(String a, LatLng p, int rn, int s, double cs, double r, int type)
	{
		if (type == 0)		// delete rack
		{
			String newp = p.toString();
			if (rService == null) {
				rService = GWT.create(rackService.class);
			}
			AsyncCallback callback = new AsyncCallback<Void>()
					{
				public void onFailure(Throwable error)
				{
					Window.alert("Server Error! (RMV-RACK)");
					handleError(error);
				}
				public void onSuccess(Void ignore)
				{
					Window.alert("Success (RMV-RACK)");
				}
					};
					rService.removeRack(newp, callback);
		}

		// !@#
		if (type == 1)
		{
			String newp = p.toString();
			if (rService == null) {
				rService = GWT.create(rackService.class);
			}
			AsyncCallback callback = new AsyncCallback<Void>()
					{
				public void onFailure(Throwable error)
				{
					Window.alert("Server Error! (ADD-RACK)");
					handleError(error);
				}
				public void onSuccess(Void ignore)
				{
					Window.alert("Success. (ADD-RACK)");
				}
					};
					rService.addRack(a, newp, rn, s, cs, r, callback);
		}	

		if (type == 2)		//update
		{
			String newp = p.toString();
			if (rService == null) {
				rService = GWT.create(rackService.class);
			}
			AsyncCallback callback = new AsyncCallback<Void>()
					{
				public void onFailure(Throwable error)
				{
					Window.alert("Server Error! (UPD-RACK)");
					handleError(error);
				}
				public void onSuccess(Void ignore)
				{
					Window.alert("Success. (UPD-RACK)");
				}
					};
					rService.updateStolen(newp, 0, callback);
					rService.updateCS(newp, 0, callback);
					rService.updateRate(newp, 5, callback);
		}	
	}

	/**
	 * Call crimeOps (Admin only): 
	 * type == 0: REMOVE OPERATION. require LatLng
	 * type == 1: ADD OPERATION. require all parameters
	 */
	private void crimeOps (int i, LatLng p, int type)
	{
		//cService = GWT.create(crimeService.class);
		if (type == 0)		// delete crime
		{
			String newp = p.toString();
			if (cService == null) {
				cService = GWT.create(crimeService.class);
			}
			AsyncCallback<Void> crimeCallback = new AsyncCallback<Void>()
					{
				public void onFailure(Throwable error)
				{
					Window.alert("Server Error! (RMV-CRIME)");
					handleError(error);
				}
				public void onSuccess(Void ignore)
				{
					Window.alert("Success. (RMV-CRIME");
				}
					};
					cService.removeCrime(newp, crimeCallback);
		}

		else if (type == 1)				// add crime
		{
			String newp = p.toString();
			if (cService == null) {
				cService = GWT.create(crimeService.class);
			}
			AsyncCallback<Void> crimeCallback = new AsyncCallback<Void>()
					{
				public void onFailure(Throwable error)
				{
					Window.alert("Server Error! (UPD-CRIME)");
					handleError(error);
				}
				public void onSuccess(Void ignore)
				{
					Window.alert("Success. (UPD-CRIME)");
				}
					};
					cService.addCrime(i, newp, crimeCallback);
		}
	}

	/**
	 * Only call this if want to refetch CRIME from db
	 */
	public void parseCrime()
	{
		//======== PARSE FROM DB =============
		if (cService == null) {
			cService = GWT.create(crimeService.class);
		}

		cService.getCrimes(new AsyncCallback<ArrayList<String[]>>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (PAR-CRIME)");
				handleError(error);
			}
			@Override
			public void onSuccess(ArrayList<String[]> result) {
				// TODO Auto-generated method stub
				Window.alert("Success. (PAR-CRIME)");
				assignCrimeOutput(result);
			}
				});
	}

	/**
	 * Called automagically by parseCrime
	 */
	public void assignCrimeOutput(ArrayList<String[]> result)
	{
		//Window.alert("Parsing...");

		if (!(result.isEmpty() || result == null))
		{
			for(int i = 0; i <= result.size(); i++)
			{
				//Window.alert("P"+i);
				String[] temp = result.get(i);
				String addr = temp[0].toString();
				String LL = temp[1].toString();   // string

				listofcrimes.add(new Crime (addr, LL));
			}
		}

	}

	/**
	 * Only call this if want to refetch RACK from db
	 */
	public void parseRack ()
	{
		//======== PARSE FROM DB =============
		if (rService == null) {
			rService = GWT.create(rackService.class);
		}

		rService.getRacks(new AsyncCallback<ArrayList<String[]>>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (PAR-RACK)");
				handleError(error);
			}
			@Override
			public void onSuccess(ArrayList<String[]> result) {
				// TODO Auto-generated method stub
				Window.alert("Success. (PAR-RACK)");
				assignrackOutput(result);
			}
				});
	}

	/**
	 * Called automagically by parseRack
	 */
	private void assignrackOutput (ArrayList<String[]> result)
	{

		//Window.alert("Parsing...");
		if (!(result.isEmpty() || result == null))
		{
			for(int i = 0; i <= result.size(); i++)
			{
				//Window.alert("P"+i);
				String[] temp = result.get(i);
				String addr = temp[0].toString();
				String LL = temp[1].toString();   // string
				String rate = temp[2].toString(); // double
				String rackN = temp[3].toString(); // int
				String cs = temp[4].toString().substring(0,1); 	// int
				String stolenN = temp[5].toString(); // int


				//System.out.println("butwhy" + addr+", "+LL+", "+rate+", "+rackN+", "+cs+", "+stolenN);
				double rateDouble = Double.parseDouble(rate);
				int intRackN = Integer.parseInt(rackN);
				double intcs = Double.parseDouble(cs);
				int intstolenN = Integer.parseInt(stolenN);
				//System.out.println("butwhy" + addr+", "+LL+", "+rateDouble+", "+intRackN+", "+intcs+", "+intstolenN);
				listofracks.add(new BikeRack (addr, LL, Double.parseDouble(rate), Integer.parseInt(rackN), Integer.parseInt(cs), Integer.parseInt(stolenN)));
			}
		}

	}

	/**
	 * Only call this if want to refetch ALL from db (SLOW!)
	 */
	public void addtolist()
	{
		listofcrimes = new ArrayList<Crime>();
		listofracks = new ArrayList<BikeRack>();
		parseRack();
		parseCrime();
	}
	// ===================== SERVER ASYNC CALLS ENDS ==========================

	// FAV procedure
	// login
	// parse friends
	// parse fav
	// assign fav (gets auto called)
	// getcommonfav (auto calls)
	// two lists (favRacks, favRacksCommon)
	// favRacks (My favorite racks), facRacksCommon (friends who also fav them)

	// ===================== LOGIN PROCEDURE CALLS ============================

	private void handleError(Throwable error) 
	{
		Window.alert(error.getMessage());   
	}

	private void messenger(String s)
	{
		Window.alert(s);
	}

	private void deBugMessenger()
	{
		String output = "Your Friends: \n";
		for (int i = 0; i < userFriends.size(); i++)
		{
			String[] tmp = userFriends.get(i);
			output = output + "\nName: " + tmp[0] +" id: "+tmp[1];
		}
		messenger(output);
	}

	private void getCommonFavorites()
	{
		if (!(userFriends.isEmpty() || userFriends == null))
		{
			for (int i = 0; i < userFriends.size(); i++)
			{
				String[] tmp = userFriends.get(i);
				parseFav(tmp[0], tmp[1]);
			}
		}
	}

	private void compareFriendFav(String name, ArrayList<String[]> result)
	{
		if (!(result.isEmpty() || result == null))
		{
			for (int i = 0; i < result.size(); i++)
			{
				String[] fav = result.get(i);
				String fLL = fav[1];
				for (int a = 0; a < favRacks.size(); a++)
				{
					if (fLL.equals(favRacks.get(a).getCoordinate()))
					{
						favRacksCommon.get(a).add(name);
					}
				}
			}
		}
	}

	private void getUserFriends()
	{
		if (!userToken.isEmpty() && !userEmail.isEmpty())
		{
			if (userIsPlus == true)
			{
				// confirm user logged in, user is a google plus user, token is valid (hopfully not expired)
				String url = "https://www.googleapis.com/plus/v1/people/me/people/visible?access_token=" + userToken;
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
				try 
				{
					Request request = builder.sendRequest(null, new RequestCallback() {
						@SuppressWarnings("deprecation")
						@Override
						public void onResponseReceived(Request request, Response response) 
						{
							// request successful
							if (response.getStatusCode() == 200)
							{
								// success request, start info parsing							
								if (response.getText() != null)
								{
									JSONValue jsv = JSONParser.parse(response.getText());			
									JSONObject js = jsv.isObject();

									int totalItems = (int) js.get("totalItems").isNumber().doubleValue();
									JSONArray jsFriends = js.get("items").isArray();

									for (int i = 0; i < totalItems; i++)
									{
										String friendId = jsFriends.get(i).isObject().get("id").isString().stringValue();
										String friendName = jsFriends.get(i).isObject().get("displayName").isString().stringValue();
										String friendType = jsFriends.get(i).isObject().get("objectType").isString().stringValue();
										if (friendType.equals("person"))
										{
											String[] person = {friendName, friendId};
											userFriends.add(person);
										}
									}
									deBugMessenger();
								}
							}
							else if (response.getStatusCode() == 400)
							{
								// bad request, bad information
								messenger("Failed to GetFriends-TRY-REQ-RES=BAD");	
							}
							else
							{
								messenger("Failed to GetFriends-TRY-REQ-RES=FORBIDDEN");
							}
						}
						@Override
						public void onError(Request request, Throwable exception)
						{
							messenger("Error on GetFriends-TRY-ONERROR");										
						}});
				} 
				catch (RequestException e) 
				{
					// fail request
					messenger("Error on GetFriends-CATCH"+e);	
				}
			}
			else
			{
				messenger("Please sign up for Google+ to take advantage of our favorite page social features");
			}
		}
	}


	private void loginAttemptSwitcher()
	{
		if (loginAttempt == 0) loginAttempt = 1;
		else if (loginAttempt == 1) loginAttempt = 0;
	}

	private void saveToken(String t)
	{
		userToken = t;
	}

	private void startLoginProcess(final Button loginButton)
	{
		loginAttempt = 0;
		if (loginFlipFlop == 0)
		{
			final AuthRequest req = new AuthRequest(GOOGLE_AUTH_URL, CLIENT_ID).withScopes(PLUS_ME_SCOPE, FRIEND_SCOPE, EMAIL_SCOPE);
			AUTH.login(req, new Callback<String, Throwable>() {
				@Override
				public void onSuccess(String token) {
					//Window.alert("Got an OAuth token:\n" + token + "\n"+ "Token expires in " + AUTH.expiresIn(req) + " ms\n");
					if (!token.isEmpty())
					{
						//TODO token recived, start api access
						saveToken(token);
						// ============== on Success ==============
						String url = "https://www.googleapis.com/plus/v1/people/me?access_token=" + token;
						RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
						try 
						{
							Request request = builder.sendRequest(null, new RequestCallback() {
								@SuppressWarnings("deprecation")
								@Override
								public void onResponseReceived(Request request, Response response) 
								{
									// search tag: %#%#
									// request successful
									if (response.getStatusCode() == 200)
									{
										// success request, start info parsing
										if (response.getText() != null)
										{
											JSONValue jsv = JSONParser.parse(response.getText());			
											JSONObject js = jsv.isObject();

											JSONArray jsemail = js.get("emails").isArray();
											userEmail = jsemail.get(0).isObject().get("value").isString().stringValue();
											userName = js.get("displayName").isString().stringValue();
											userId = js.get("id").isString().stringValue();
											userImageURL = js.get("image").isObject().get("url").isString().stringValue();
											userImageURL = userImageURL.substring(0, userImageURL.length() - 2) + "20";
											//System.out.println(userImageURL);
											userGender = js.get("gender").isString().stringValue();
											userIsPlus = js.get("isPlusUser").isBoolean().booleanValue();

											checkUserInfo(userId, userName, userEmail, userGender, userIsPlus, userImageURL);
											loginButton.setText(userName);
											getUserFriends();
										}
									}
									else if (response.getStatusCode() == 400)
									{
										// bad request, bad information
										messenger("Failed to LOGIN-TRY-REQ-RES=BAD");	
									}
									else
									{
										messenger("Failed to LOGIN-TRY-REQ-RES=FORBIDDEN");
									}
								}
								@Override
								public void onError(Request request, Throwable exception)
								{
									messenger("Error on LOGIN-TRY-ONERROR");										
								}});
						} 
						catch (RequestException e) 
						{
							// fail request
							messenger("Error on LOGIN-CATCH"+e);	
						}

						// ============== on Success ==============
					}
					loginButton.setText("Sign Out");
					loginFlipFlop = 1;
				}

				@Override
				public void onFailure(Throwable caught) 
				{
					loginAttemptSwitcher();
					messenger("G+ ERROR-SLP-Fail TYPE 0!");
				}
			});

		}
		else
		{		
			String url = "https://www.google.com/accounts/Logout?continue";	
			final AuthRequest req = new AuthRequest(url, CLIENT_ID);
			AUTH.clearAllTokens();
			com.google.gwt.user.client.Window.open(url, "_blank", "");

			userEmail = "";
			userName = "";
			userToken = "";
			userId = "";
			userImageURL = "";
			userFriends = new ArrayList<String[]>();
			favRacks = new ArrayList<BikeRack>();
			favRacksCommon = new ArrayList< ArrayList<String>>();
			userHistory = new ArrayList<UserSearchHistoryInstance>();
			messenger("Successfully cleared all tokens and signed out");
			loginButton.setText("Sign in");
			loginFlipFlop = 0;
		}
	}

	// ====================== LOGIN PROCEDURE CALLS END ======================

	public static ArrayList<Crime> getCrimeData()
	{
		return listofcrimes;
	}
	public static ArrayList<BikeRack> getRackData()
	{
		return listofracks;
	}

}

