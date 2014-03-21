package cs310MRAK.rackcity.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
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

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Rack_City implements EntryPoint {
	RootPanel rootPanel;
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
	private boolean filter = false;
	//private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
//	private static final Logger LOG = Logger.getLogger(Rack_City.class.getName());
	//=============================
	  private LoginInfo loginInfo = null;
	  private VerticalPanel loginPanel = new VerticalPanel();
	  private Label loginLabel = new Label("Please sign in to your Google Account to access specific features.");
	  private Label logoutLabel = new Label("Please click on signout to confirm your action");
	  private Anchor signInLink = new Anchor("Sign In");
	  private Anchor signOutLink = new Anchor("Sign Out");
	  public int w = 0;
	// google+ login stuff ------ S2 ---------
	  //private static final Plus plus = GWT.create(Plus.class);
	  //private static final String CLIENT_ID = "692753340433.apps.googleusercontent.com";
	  //private static final String API_KEY = "AIzaSyA5bNyuRQFaTQle_YC5BUH7tQzRmAPiqsM";
	  //private static final String APPLICATION_NAME = "cs310rackcity/1.0";
	  
	 // Server stuff
	  private rackServiceAsync rService = GWT.create(rackService.class);
	  private crimeServiceAsync cService = GWT.create(crimeService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		GUIsetup();
		
		
	}
	
	 private void handleError(Throwable error) {
		    Window.alert(error.getMessage());
		    if (error instanceof NotLoggedInException) {
		      Window.Location.replace(loginInfo.getLogoutUrl());
		    }
		  }
	
	private void startLoginProcess()
	{
		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  Window.alert("Error loading loginService!");
	    	  handleError(error);
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) 
	        {
	        	// if logged in, set text to sign out
	        	
	        	 // Set up sign out hyperlink.
	        	rootPanel.clear();
	        	signOutLink.setHref(loginInfo.getLogoutUrl());
	        	loginPanel.add(logoutLabel);
		     	loginPanel.add(signOutLink);
		     	RootPanel.get().add(loginPanel);
	        } 
	        else 
	        {
	        	// otherwise, continue login procedure
	        	rootPanel.clear();
	        	signInLink.setHref(loginInfo.getLoginUrl());
	     	    loginPanel.add(loginLabel);
	     	    loginPanel.add(signInLink);
	     	    RootPanel.get().add(loginPanel);
	        }
	 
	      }
	      
	    });
	}
	
	private void GUIsetup()
	{
		rootPanel = RootPanel.get();
		rootPanel.setSize("1160px", "637px");
		dockPanel = new DockPanel();
		rootPanel.add(dockPanel, 10, 0);
		dockPanel.setSize("1150px", "627px");
		
		 //* Filling out the Root and Dock Panels with Horizontal and Absolute Panels
		 
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
		
		//set text correctly
		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	    	  Window.alert("Error loading loginService!");
	    	  handleError(error);
	      }

	      public void onSuccess(LoginInfo result) {
	        loginInfo = result;
	        if(loginInfo.isLoggedIn()) 
	        {
	        	// if logged in, set text to sign out
	        	loginButton.setText("Sign Out");
	        } 
	        else 
	        {
	        	loginButton.setText("Sign In");
	        }
	      }
	    });
	    
		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//addtolist();
				LoginServiceAsync loginService = GWT.create(LoginService.class);
			    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			      public void onFailure(Throwable error) {
			    	  Window.alert("Error loading loginService!");
			    	  handleError(error);
			      }

			      public void onSuccess(LoginInfo result) {
			        loginInfo = result;
			        if(loginInfo.isLoggedIn()) 
			        {
			        	// if logged in, set text to sign out
			        	loginButton.setText("Sign Out");			        	
			        } 
			        else 
			        {
			        	loginButton.setText("Sign In");
			        }
			      }
			    });
				
				
				startLoginProcess();
				
				
				
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
					addtolist();
					// 
					addRacker("134 East Abbott St, Vancouver, BC", LatLng.newInstance(49.284176,-123.106037), 3, 1, 1, 1, 1);
				//	rService.addRack(addr, p, rnum, stolen, cs, rate, callback);
					w = 0;
				    Window.alert("Data added to the datastore because you clicked the admin button three times. Awesome. ");

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

				googleMap.setVisible(false);
				((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).remove(0);

				//TODO INSERT CODE HERE to add the fetched list from the filter class
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

		//MAP API KEY: AIzaSyCOopo9nSwdUPDKLay6JVPCzzD4ruAwmG0 (Might not work)
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
	private void addMapOverlay(final String address, final double radius, final int crimeScore, final double rating){

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
					for (Crime crime : currentCrimeList) {
						//System.out.println("Crime Coordinate: " + crime.getCoordinate());
						addMarker(crime.getCoordinate(), 3);
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
			    LoginServiceAsync loginService = GWT.create(LoginService.class);
			    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			      public void onFailure(Throwable error) {
			    	  Window.alert("Error loading loginService!");
			      }

			      public void onSuccess(LoginInfo result) {
			        loginInfo = result;
			        if(loginInfo.isLoggedIn()) 
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
			        	System.out.println("newp value is: "+ newp);
			        //	String beforeNewp = "name=";
			       // 	newp = beforeNewp + newp;
			        	System.out.println("now newp value is: " + newp);
						if (rService == null) 
						{
							rService = GWT.create(rackService.class);
						}
						reportCrimeButton.setEnabled(false);
						AsyncCallback callback = new AsyncCallback<String>()
						{
							public void onFailure(Throwable error)
							{
								Window.alert("Server Error  !");
								handleError(error);
							}
							public void onSuccess(Void ignore)
							{
								Window.alert("Crime successfully reported!");
							}
							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
								
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

		double PI = 3.14159265;

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
			if(rack.getCoordinate().equals(latlng)){
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
	
	private void removeRacker(String a, LatLng p, int rn, int s, int cs, int r, int type){
		addRacker(a, p, rn, s, cs, r, 0);
	}
						// 						3		1		1		1		2
	private void addRacker(String a, LatLng p, int rn, int s, int cs, int r, int type)
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
		
		if (type == 2)
		{
			String newp = p.toString();
			if (rService == null) {
				rService = GWT.create(rackService.class);
			    }
			AsyncCallback callback = new AsyncCallback<Void>()
					{
						public void onFailure(Throwable error)
						{
							Window.alert("Server Error! (UPD-CRIME)");
							handleError(error);
						}
						public void onSuccess(Void ignore)
						{
							Window.alert("Success. (UPD-RACK)");
						}
					};
			rService.addRack(a, newp, rn, s, cs, r, callback);
			BikeRack tmp = new BikeRack(a, p, r, rn, cs, s);
			listofracks.add(tmp);
		}
		else if (type == 3)
		{
			BikeRack tmp = new BikeRack(a, p, r, rn, cs, s);
			listofracks.add(tmp);
		}
		
	}
	
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
		
		else if (type == 3)			// save local
		{
			//String ytoSTR = i.toString();
			//Crime tmp = new Crime(, p);
			//listofcrimes.add(tmp);
		}
	}
	
	// `
	public void addtolist()
	{
		listofracks = new ArrayList<BikeRack>();
		listofcrimes = new ArrayList<Crime>();
		
		// ============= call Parser ============
		final ArrayList<Rack> output = new ArrayList<Rack>();
		if (rService == null)
		{
			rService = GWT.create(rackService.class);
		}
		AsyncCallback callback = new AsyncCallback<ArrayList<Rack>>()
				{
					public void onFailure(Throwable error)
					{
						Window.alert("Server Error! (RMV-RACK)");
						handleError(error);
					}
					public void onSuccess(ArrayList<Rack> result) {
						// TODO Auto-generated method stub
						Window.alert("Success (RMV-RACK)");
						output.addAll(result);
					}
				};
		rService.getRacks(callback);
		
		for(int i = 0; i <= output.size(); i++)
		{
			//String address, LatLng coordinate, double rating, int rackCount, int crimeScore, int numberStolenBikes
			Rack rctemp = output.get(i);
			String addr = rctemp.getAddr();
			Window.alert("P" + i);
			// ==== Convert from string latlng to latlng ====
			String LL = rctemp.getLL();
			List<String> LLlist = Arrays.asList(LL.split(","));
			String Lat = LLlist.get(0);
			String Lon = LLlist.get(1);
			Lat = Lat.substring(1);
			Lon = Lon.substring(0, Lon.length() -1 );
			double Latvalue = Double.parseDouble(Lat);
			double Lonvalue = Double.parseDouble(Lon);
			LatLng pos = LatLng.newInstance(Latvalue, Lonvalue);
			
			double rate = rctemp.getRating();
			int rackcount = rctemp.getRnum();
			double crimeScore = rctemp.getCS();
			int numStolen = rctemp.getStolen();
			BikeRack temp = new BikeRack(addr, pos, rate, rackcount, (int)crimeScore, numStolen);
			listofracks.add(temp);
		}
		//=========================================
		
//		addRacker("134 East Abbott St, Vancouver, BC", LatLng.newInstance(49.284176,-123.106037), 3, 1, 1, 1, 2);
//		addRacker("216 East Abbott St, Vancouver, BC", LatLng.newInstance(49.283429,-123.106404), 4, 2, 1, 1, 2);
//	    addRacker("1600 North Alberni St, Vancouver, BC", LatLng.newInstance(49.2902,-123.131333), 3, 1, 3, 1, 2);
//	    addRacker("90 South Alexander, Vancouver, BC", LatLng.newInstance(49.283638,-123.102348), 5, 1, 3, 1, 2);
//	    addRacker("55 North Alexander, Vancouver, BC", LatLng.newInstance(49.283613,-123.103), 2, 2, 1, 1, 2);
//	    addRacker("200 North Alexander, Vancouver, BC", LatLng.newInstance(49.284083,-123.099423), 2, 2, 3, 1, 2);
//	    addRacker("305 North Alexander St, Vancouver, BC", LatLng.newInstance(49.284449,-123.097679), 2, 1, 0, 1, 2);
//	    addRacker("475 North Alexander St, Vancouver, BC", LatLng.newInstance(49.284113,-123.093897), 5, 2, 0, 1, 2);
//	    addRacker("2083 West Alma, Vancouver, BC", LatLng.newInstance(49.267958,-123.185798), 4, 3, 3, 1, 2);
//	    addRacker("5704 East Balsam, Vancouver, BC", LatLng.newInstance(49.234669,-123.161332), 2, 1, 3, 1, 2);
//	    addRacker("1400 East Balsam St, Vancouver, BC", LatLng.newInstance(49.273465,-123.159832), 5, 1, 1, 1, 2);
//	    addRacker("900 East Beatty, Vancouver, BC", LatLng.newInstance(49.276482,-123.115608), 3, 1, 3, 1, 2);
//	    addRacker("590 East Beatty, Vancouver, BC", LatLng.newInstance(49.280006,-123.110317), 5, 2, 0, 1, 2);
//	    addRacker("2400 West Birch St, Vancouver, BC", LatLng.newInstance(49.264387,-123.133673), 4, 1, 4, 1, 2);
//	    addRacker("1300 West Broughton, Vancouver, BC", LatLng.newInstance(49.283001,-123.137342), 4, 2, 5, 1, 2);
//	    addRacker("635 West Burrard, Vancouver, BC", LatLng.newInstance(49.284999,-123.120252), 2, 4, 5, 1, 2);
//	    addRacker("1081 West Burrard, Vancouver, BC", LatLng.newInstance(49.279954,-123.128292), 3, 3, 2, 1, 2);
//	    addRacker("800 West Bute St, Vancouver, BC", LatLng.newInstance(49.285995,-123.126972), 5, 1, 3, 1, 2);
//	    addRacker("1100 East Bute St, Vancouver, BC", LatLng.newInstance(49.282909,-123.131625), 4, 2, 4, 1, 2);
//	    addRacker("4099 West Cambie, Vancouver, BC", LatLng.newInstance(49.184569,-123.094049), 5, 22, 4, 1, 2);
//	    addRacker("6488 East Cambie, Vancouver, BC", LatLng.newInstance(49.226148,-123.116345), 2, 3, 0, 1, 2);
//	    addRacker("8430 East Cambie, Vancouver, BC", LatLng.newInstance(49.184677,-123.130574), 3, 4, 5, 1, 2);
//	    addRacker("8500 East Cambie, Vancouver, BC", LatLng.newInstance(49.208853,-123.117194), 5, 1, 1, 1, 2);
//	    addRacker("511 West Carrall St, Vancouver, BC", LatLng.newInstance(49.280304,-123.104537), 3, 2, 5, 1, 2);
//	    addRacker("211 West Columbia St, Vancouver, BC", LatLng.newInstance(49.28304,-123.102214), 2, 1, 2, 1, 2);
//	    addRacker("2021 West Columbia St, Vancouver, BC", LatLng.newInstance(49.267197,-123.109585), 5, 2, 4, 1, 2);
//	    addRacker("1010 East Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275578,-123.069377), 3, 2, 1, 1, 2);
//	    addRacker("1016 East Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275486,-123.069385), 2, 1, 4, 1, 2);
//	    addRacker("1025 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275467,-123.069724), 4, 1, 5, 1, 2);
//	    addRacker("1034 East Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275238,-123.069362), 5, 1, 5, 1, 2);
//	    addRacker("1034 East Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275238,-123.069362), 2, 1, 0, 1, 2);
//	    addRacker("1035 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275206,-123.069737), 2, 2, 2, 1, 2);
//	    addRacker("1103 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.274809,-123.069542), 2, 1, 1, 1, 2);
//	    addRacker("1111 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.274618,-123.069543), 3, 2, 2, 1, 2);
//	    addRacker("1135 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.27447,-123.069544), 5, 1, 1, 1, 2);
//	    addRacker("1365 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.272469,-123.069573), 2, 1, 4, 1, 2);
//	    addRacker("1858 North Commercial Dr, Vancouver, BC", LatLng.newInstance(49.267901,-123.069515), 4, 1, 4, 1, 2);
//	    addRacker("297 North Davie, Vancouver, BC", LatLng.newInstance(49.274814,-123.122565), 4, 6, 2, 1, 2);
//	    addRacker("4243 West Dunbar, Vancouver, BC", LatLng.newInstance(49.248541,-123.185332), 2, 1, 5, 1, 2);
//	    addRacker("4306 East Dunbar, Vancouver, BC", LatLng.newInstance(49.247927,-123.18504), 5, 1, 4, 1, 2);
//	    addRacker("4336 East Dunbar, Vancouver, BC", LatLng.newInstance(49.247122,-123.185203), 4, 1, 0, 1, 2);
//	    addRacker("4355 West Dunbar, Vancouver, BC", LatLng.newInstance(49.247468,-123.185397), 2, 1, 5, 1, 2);
//	    addRacker("4432 East Dunbar, Vancouver, BC", LatLng.newInstance(49.246813,-123.185058), 5, 1, 4, 1, 2);
//	    addRacker("4446 East Dunbar, Vancouver, BC", LatLng.newInstance(49.246727,-123.185075), 3, 1, 4, 1, 2);
//	    addRacker("4474 East Dunbar, Vancouver, BC", LatLng.newInstance(49.246433,-123.185211), 2, 1, 3, 1, 2);
//	    addRacker("4497 West Dunbar, Vancouver, BC", LatLng.newInstance(49.246306,-123.18542), 5, 1, 0, 1, 2);
//	    addRacker("2177 North Dundas St, Vancouver, BC", LatLng.newInstance(49.284794,-123.059777), 5, 1, 0, 1, 2);
//	    addRacker("37 West Dunlevy Av, Vancouver, BC", LatLng.newInstance(49.284761,-123.095305), 5, 1, 1, 1, 2);
//	    addRacker("211 West Dunlevy Av, Vancouver, BC", LatLng.newInstance(49.28308,-123.095329), 2, 2, 3, 1, 2);
//	    addRacker("304 North Dunlevy Av, Vancouver, BC", LatLng.newInstance(49.281837,-123.095279), 5, 2, 2, 1, 2);
//	    addRacker("433 West Dunlevy Av, Vancouver, BC", LatLng.newInstance(49.280913,-123.095546), 2, 2, 5, 1, 2);
//	    addRacker("1800 North E 11th Ave, Vancouver, BC", LatLng.newInstance(49.260454,-123.069483), 4, 1, 3, 1, 2);
//	    addRacker("2800 South E 12th Ave, Vancouver, BC", LatLng.newInstance(49.259046,-123.046942), 5, 1, 1, 1, 2);
//	    addRacker("607 North E 15th, Vancouver, BC", LatLng.newInstance(49.257247,-123.090752), 5, 2, 0, 1, 2);
//	    addRacker("617 North E 15th, Vancouver, BC", LatLng.newInstance(49.257246,-123.090551), 2, 1, 1, 1, 2);
//	    addRacker("639 North E 15th, Vancouver, BC", LatLng.newInstance(49.257225,-123.090112), 3, 1, 2, 1, 2);
//	    addRacker("2450 South E 24th, Vancouver, BC", LatLng.newInstance(49.248812,-123.054464), 4, 3, 0, 1, 2);
//	    addRacker("2790 South E 29th, Vancouver, BC", LatLng.newInstance(49.244598,-123.046338), 4, 3, 1, 1, 2);
//	    addRacker("112 South E 3rd, Vancouver, BC", LatLng.newInstance(49.267941,-123.102345), 5, 1, 3, 1, 2);
//	    addRacker("146 South E 3rd, Vancouver, BC", LatLng.newInstance(49.268142,-123.101657), 5, 1, 4, 1, 2);
//	    addRacker("696 South E 45th, Vancouver, BC", LatLng.newInstance(49.229182,-123.091575), 5, 2, 2, 1, 2);
//	    addRacker("127 North E 4th, Vancouver, BC", LatLng.newInstance(49.265319,-123.103005), 4, 1, 3, 1, 2);
//	    addRacker("137 North E 4th, Vancouver, BC", LatLng.newInstance(49.267344,-123.101541), 4, 1, 0, 1, 2);
//	    addRacker("7 North E 6th, Vancouver, BC", LatLng.newInstance(49.265596,-123.104687), 4, 1, 3, 1, 2);
//	    addRacker("48 South E 6th, Vancouver, BC", LatLng.newInstance(49.265553,-123.103408), 2, 2, 0, 1, 2);
//	    addRacker("1600 South E 6th Ave, Vancouver, BC", LatLng.newInstance(49.266225,-123.14104), 3, 1, 2, 1, 2);
//	    addRacker("156 South E 7th, Vancouver, BC", LatLng.newInstance(49.264613,-123.101558), 4, 2, 5, 1, 2);
//	    addRacker("1600 North E 8th Ave, Vancouver, BC", LatLng.newInstance(49.263273,-123.071549), 4, 1, 1, 1, 2);
//	    addRacker("5720 East E Boulevard, Vancouver, BC", LatLng.newInstance(49.234579,-123.15499), 3, 1, 4, 1, 2);
//	    addRacker("1725 North E Broadway, Vancouver, BC", LatLng.newInstance(49.262397,-123.069124), 4, 4, 1, 1, 2);
//	    addRacker("8 South E Cordova, Vancouver, BC", LatLng.newInstance(49.282421,-123.104095), 2, 1, 1, 1, 2);
//	    addRacker("63 North E Cordova, Vancouver, BC", LatLng.newInstance(49.282389,-123.103199), 5, 1, 0, 1, 2);
//	    addRacker("93 North E Cordova, Vancouver, BC", LatLng.newInstance(49.282376,-123.102475), 4, 1, 3, 1, 2);
//	    addRacker("208 South E Georgia, Vancouver, BC", LatLng.newInstance(49.278461,-123.099308), 4, 1, 2, 1, 2);
//	    addRacker("239 North E Georgia, Vancouver, BC", LatLng.newInstance(49.278582,-123.099274), 5, 1, 0, 1, 2);
//	    addRacker("501 North E Georgia, Vancouver, BC", LatLng.newInstance(49.278509,-123.093446), 3, 2, 2, 1, 2);
//	    addRacker("403 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.281425,-123.095266), 5, 1, 2, 1, 2);
//	    addRacker("573 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.281215,-123.091692), 5, 1, 3, 1, 2);
//	    addRacker("769 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.28126,-123.087973), 3, 2, 5, 1, 2);
//	    addRacker("843 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.281132,-123.08616), 4, 1, 1, 1, 2);
//	    addRacker("1489 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.281497,-123.074507), 4, 1, 3, 1, 2);
//	    addRacker("1618 South E Hastings St, Vancouver, BC", LatLng.newInstance(49.281248,-123.072171), 3, 1, 5, 1, 2);
//	    addRacker("1736 South E Hastings St, Vancouver, BC", LatLng.newInstance(49.281224,-123.069525), 3, 1, 4, 1, 2);
//	    addRacker("1756 South E Hastings St, Vancouver, BC", LatLng.newInstance(49.281107,-123.069233), 5, 1, 0, 1, 2);
//	    addRacker("2647 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.281269,-123.050743), 5, 1, 1, 1, 2);
//	    addRacker("142 South E Pender St, Vancouver, BC", LatLng.newInstance(49.28034,-123.101293), 5, 1, 1, 1, 2);
//	    addRacker("1809 West Fir St, Vancouver, BC", LatLng.newInstance(49.269448,-123.140958), 3, 1, 2, 1, 2);
//	    addRacker("4097 West Fraser, Vancouver, BC", LatLng.newInstance(49.248679,-123.09015), 3, 1, 5, 1, 2);
//	    addRacker("4129 West Fraser, Vancouver, BC", LatLng.newInstance(49.248159,-123.090332), 4, 1, 1, 1, 2);
//	    addRacker("4153 West Fraser, Vancouver, BC", LatLng.newInstance(49.248024,-123.090312), 2, 1, 1, 1, 2);
//	    addRacker("4230 East Fraser, Vancouver, BC", LatLng.newInstance(49.247482,-123.090137), 2, 1, 0, 1, 2);
//	    addRacker("4259 West Fraser, Vancouver, BC", LatLng.newInstance(49.246992,-123.090195), 5, 1, 5, 1, 2);
//	    addRacker("4288 East Fraser, Vancouver, BC", LatLng.newInstance(49.246811,-123.090167), 3, 1, 4, 1, 2);
//	    addRacker("4304 East Fraser, Vancouver, BC", LatLng.newInstance(49.246474,-123.090023), 4, 1, 4, 1, 2);
//	    addRacker("4305 West Fraser, Vancouver, BC", LatLng.newInstance(49.246547,-123.090215), 5, 1, 4, 1, 2);
//	    addRacker("4386 East Fraser, Vancouver, BC", LatLng.newInstance(49.245719,-123.090205), 2, 1, 1, 1, 2);
//	    addRacker("4590 East Fraser, Vancouver, BC", LatLng.newInstance(49.243669,-123.090255), 3, 2, 5, 1, 2);
//	    addRacker("3497 West Fraser St, Vancouver, BC", LatLng.newInstance(49.253788,-123.090313), 4, 1, 1,1, 2);
//	    addRacker("4950 East Fraser St, Vancouver, BC", LatLng.newInstance(49.239836,-123.090358), 2, 1, 3, 1, 2);
//	    addRacker("6007 West Fraser St, Vancouver, BC", LatLng.newInstance(49.230016,-123.090797), 4, 1, 1, 1, 2);
//	    addRacker("189 West Gore St, Vancouver, BC", LatLng.newInstance(49.283207,-123.097765), 4, 1, 5, 1, 2);
//	    addRacker("1117 West Granville St, Vancouver, BC", LatLng.newInstance(49.278239,-123.125392), 3, 4, 5, 1, 2);
//	    addRacker("1208 East Granville St, Vancouver, BC", LatLng.newInstance(49.276898,-123.126354), 5, 4, 0, 1, 2);
//	    addRacker("7985 West Granville St, Vancouver, BC", LatLng.newInstance(49.213302,-123.140388), 2, 1, 0, 1, 2);
//	    addRacker("8155 West Granville St, Vancouver, BC", LatLng.newInstance(49.212209,-123.140421), 4, 1, 3, 1, 2);
//	    addRacker("8168 East Granville St, Vancouver, BC", LatLng.newInstance(49.211761,-123.140229), 3, 1, 2, 1, 2);
//	    addRacker("8273 West Granville St, Vancouver, BC", LatLng.newInstance(49.211648,-123.140441), 5, 1, 5, 1, 2);
//	    addRacker("8324 East Granville St, Vancouver, BC", LatLng.newInstance(49.210657,-123.140236), 2, 1, 3, 1, 2);
//	    addRacker("8333 West Granville St, Vancouver, BC", LatLng.newInstance(49.211271,-123.140645), 4, 1, 5, 1, 2);
//	    addRacker("8435 West Granville St, Vancouver, BC", LatLng.newInstance(49.210116,-123.140711), 3, 1, 5, 1, 2);
//	    addRacker("8490 East Granville St, Vancouver, BC", LatLng.newInstance(49.20932,-123.140396), 3, 1, 5, 1, 2);
//	    addRacker("8630 East Granville St, Vancouver, BC", LatLng.newInstance(49.208024,-123.140429), 5, 1, 1, 1, 2);
//	    addRacker("8662 East Granville St, Vancouver, BC", LatLng.newInstance(49.207522,-123.140461), 2, 1, 5, 1, 2);
//	    addRacker("8679 West Granville St, Vancouver, BC", LatLng.newInstance(49.207418,-123.140782), 2, 1, 3, 1, 2);
//	    addRacker("2500 East Heather St, Vancouver, BC", LatLng.newInstance(49.263337,-123.119856), 3, 1, 2, 1, 2);
//	    addRacker("3195 West Heather St, Vancouver, BC", LatLng.newInstance(49.257025,-123.120124), 5, 1, 4, 1, 2);
//	    addRacker("409 West Heatley Av, Vancouver, BC", LatLng.newInstance(49.281,-123.089617), 2, 1, 0, 1, 2);
//	    addRacker("500 South Helmcken, Vancouver, BC", LatLng.newInstance(49.277258,-123.123167), 4, 2, 2, 1, 2);
//	    addRacker("600 South Helmcken, Vancouver, BC", LatLng.newInstance(49.278196,-123.124612), 5, 2, 0, 1, 2);
//	    addRacker("600 North Helmcken, Vancouver, BC", LatLng.newInstance(49.278196,-123.124612), 4, 3, 2, 1, 2);
//	    addRacker("951 West Hornby St, Vancouver, BC", LatLng.newInstance(49.281026,-123.124262), 2, 1, 0, 1, 2);
//	    addRacker("1054 East Hornby St, Vancouver, BC", LatLng.newInstance(49.27997,-123.125799), 4, 1, 0, 1, 2);
//	    addRacker("1082 East Hornby St, Vancouver, BC", LatLng.newInstance(49.279648,-123.126044), 2, 1, 3, 1, 2);
//	    addRacker("1090 East Hornby St, Vancouver, BC", LatLng.newInstance(49.279611,-123.126339), 5, 1, 0, 1, 2);
//	    addRacker("1451 West Hornby St, Vancouver, BC", LatLng.newInstance(49.275776,-123.132397), 2, 3, 1, 1, 2);
//	    addRacker("466 East Howe St, Vancouver, BC", LatLng.newInstance(49.28541,-123.115745), 4, 1, 2, 1, 2);
//	    addRacker("1100 East Jervis St, Vancouver, BC", LatLng.newInstance(49.283505,-123.133853), 2, 1, 1, 1, 2);
//	    addRacker("5099 North Joyce, Vancouver, BC", LatLng.newInstance(49.238214,-123.031947), 4, 3, 3, 1, 2);
//	    addRacker("145 North Keefer St, Vancouver, BC", LatLng.newInstance(49.279514,-123.100706), 2, 1, 5, 1, 2);
//	    addRacker("232 South Keefer St, Vancouver, BC", LatLng.newInstance(49.279367,-123.098871), 5, 1, 4, 1, 2);
//	    addRacker("2300 West Keith St, Vancouver, BC", LatLng.newInstance(49.206889,-123.022445), 2, 1, 0, 1, 2);
//	    addRacker("76 West Kingsway, Vancouver, BC", LatLng.newInstance(49.263225,-123.099926), 5, 2, 1, 1, 2);
//	    addRacker("2066 South Kingsway, Vancouver, BC", LatLng.newInstance(49.244607,-123.064352), 5, 1, 5, 1, 2);
//	    addRacker("2127 North Kingsway, Vancouver, BC", LatLng.newInstance(49.244318,-123.063036), 5, 1, 4, 1, 2);
//	    addRacker("2001 South Macdonald, Vancouver, BC", LatLng.newInstance(49.268056,-123.168495), 3, 1, 5, 1, 2);
//	    addRacker("917 West Main, Vancouver, BC", LatLng.newInstance(49.27649,-123.100099), 3, 2, 0, 1, 2);
//	    addRacker("222 East Main St, Vancouver, BC", LatLng.newInstance(49.282785,-123.099541), 3, 1, 1, 1, 2);
//	    addRacker("309 West Main St, Vancouver, BC", LatLng.newInstance(49.282169,-123.099632), 5, 1, 1, 1, 2);
//	    addRacker("416 East Main St, Vancouver, BC", LatLng.newInstance(49.280899,-123.099479), 2, 2, 4, 1, 2);
//	    addRacker("506 East Main St, Vancouver, BC", LatLng.newInstance(49.280136,-123.099524), 4, 1, 3, 1, 2);
//	    addRacker("700 East Main St, Vancouver, BC", LatLng.newInstance(49.27828,-123.099758), 3, 2, 5, 1, 2);
//	    addRacker("708 East Main St, Vancouver, BC", LatLng.newInstance(49.278212,-123.099738), 2, 1, 1, 1, 2);
//	    addRacker("728 East Main St, Vancouver, BC", LatLng.newInstance(49.278038,-123.099602), 4, 1, 5, 1, 2);
//	    addRacker("928 East Main St, Vancouver, BC", LatLng.newInstance(49.276194,-123.099694), 2, 2, 1, 1, 2);
//	    addRacker("1008 South Main St, Vancouver, BC", LatLng.newInstance(49.275274,-123.09981), 5, 1, 3, 1, 2);
//	    addRacker("1399 West Main St, Vancouver, BC", LatLng.newInstance(49.264346,-123.100885), 2, 4, 0, 1, 2);
//	    addRacker("2812 East Main St, Vancouver, BC", LatLng.newInstance(49.259977,-123.100849), 2, 1, 5, 1, 2);
//	    addRacker("2818 East Main St, Vancouver, BC", LatLng.newInstance(49.259932,-123.100858), 2, 1, 3, 1, 2);
//	    addRacker("3835 West Main St, Vancouver, BC", LatLng.newInstance(49.250653,-123.101199), 5, 1, 3, 1, 2);
//	    addRacker("4750 East Main St, Vancouver, BC", LatLng.newInstance(49.242586,-123.101213), 3, 1, 1, 1, 2);
//	    addRacker("900 West Mainland, Vancouver, BC", LatLng.newInstance(49.276584,-123.119014), 3, 1, 0, 1, 2);
//	    addRacker("1605 West Manitoba St, Vancouver, BC", LatLng.newInstance(49.271547,-123.106828), 5, 5, 0, 1, 2);
//	    addRacker("1945 West Manitoba St, Vancouver, BC", LatLng.newInstance(49.267543,-123.106712), 2, 1, 2, 1, 2);
//	    addRacker("1200 South Marinaside Cr, Vancouver, BC", LatLng.newInstance(49.272885,-123.120015), 5, 1, 1, 1, 2);
//	    addRacker("127 West N Templeton, Vancouver, BC", LatLng.newInstance(49.286456,-123.059681), 3, 1, 4, 1, 2);
//	    addRacker("1300 West Pacific Blvd, Vancouver, BC", LatLng.newInstance(49.272887,-123.125843), 3, 1, 3, 1, 2);
//	    addRacker("40 South Powell St, Vancouver, BC", LatLng.newInstance(49.283253,-123.103224), 2, 1, 0, 1, 2);
//	    addRacker("43 North Powell St, Vancouver, BC", LatLng.newInstance(49.283321,-123.103904), 4, 1, 5, 1, 2);
//	    addRacker("253 North Powell St, Vancouver, BC", LatLng.newInstance(49.283256,-123.098226), 3, 1, 4, 1, 2);
//	    addRacker("300 South Powell St, Vancouver, BC", LatLng.newInstance(49.282748,-123.097586), 3, 2, 1, 1, 2);
//	    addRacker("362 South Powell St, Vancouver, BC", LatLng.newInstance(49.283153,-123.096292), 4, 2, 2, 1, 2);
//	    addRacker("374 South Powell St, Vancouver, BC", LatLng.newInstance(49.283029,-123.096035), 4, 1, 5, 1, 2);
//	    addRacker("398 South Powell St, Vancouver, BC", LatLng.newInstance(49.28314,-123.09543), 2, 1, 5, 1, 2);
//	    addRacker("403 North Powell St, Vancouver, BC", LatLng.newInstance(49.283195,-123.095058), 5, 1, 0, 1, 2);
//	    addRacker("415 North Powell St, Vancouver, BC", LatLng.newInstance(49.283181,-123.094919), 4, 1, 1, 1, 2);
//	    addRacker("429 North Powell St, Vancouver, BC", LatLng.newInstance(49.283193,-123.094619), 5, 1, 0, 1, 2);
//	    addRacker("451 North Powell St, Vancouver, BC", LatLng.newInstance(49.283125,-123.094077), 4, 1, 4, 1, 2);
//	    addRacker("453 North Powell St, Vancouver, BC", LatLng.newInstance(49.283124,-123.094027), 3, 1, 1, 1, 2);
//	    addRacker("537 North Powell St, Vancouver, BC", LatLng.newInstance(49.283087,-123.092462), 3, 3, 0, 1, 2);
//	    addRacker("550 South Powell St, Vancouver, BC", LatLng.newInstance(49.283052,-123.09214), 3, 1, 3, 1, 2);
//	    addRacker("566 South Powell St, Vancouver, BC", LatLng.newInstance(49.282964,-123.091901), 4, 1, 4, 1, 2);
//	    addRacker("569 North Powell St, Vancouver, BC", LatLng.newInstance(49.28315,-123.091543), 5, 1, 5, 1, 2);
//	    addRacker("2043 West Quebec St, Vancouver, BC", LatLng.newInstance(49.266431,-123.102813), 5, 1, 3, 1, 2);
//	    addRacker("2323 West Quebec St, Vancouver, BC", LatLng.newInstance(49.264286,-123.103046), 3, 2, 0, 1, 2);
//	    addRacker("395 North Railway St, Vancouver, BC", LatLng.newInstance(49.284923,-123.095493), 2, 1, 5, 1, 2);
//	    addRacker("460 South Railway St, Vancouver, BC", LatLng.newInstance(49.284748,-123.093968), 4, 1, 5, 1, 2);
//	    addRacker("1400 West Richards, Vancouver, BC", LatLng.newInstance(49.273832,-123.127314), 3, 1, 2, 1, 2);
//	    addRacker("1200 East Richards, Vancouver, BC", LatLng.newInstance(49.275821,-123.124295), 3, 1, 2, 1, 2);
//	    addRacker("500 East Richards St, Vancouver, BC", LatLng.newInstance(49.283025,-123.113408), 2, 2, 2, 1, 2);
//	    addRacker("1025 North Robson St, Vancouver, BC", LatLng.newInstance(49.283628,-123.12329), 4, 2, 0, 1, 2);
//	    addRacker("1800 North Robson St, Vancouver, BC", LatLng.newInstance(49.291533,-123.135554), 4, 1, 0, 1, 2);
//	    addRacker("655 West Seymour, Vancouver, BC", LatLng.newInstance(49.282365,-123.116388), 2, 4, 1, 1, 2);
//	    addRacker("1100 West Seymour, Vancouver, BC", LatLng.newInstance(49.277518,-123.123691), 3, 4, 4, 1, 2);
//	    addRacker("1000 North Smithe, Vancouver, BC", LatLng.newInstance(49.281958,-123.123995), 2, 1, 1, 1, 2);
//	    addRacker("1946 South Triumph St, Vancouver, BC", LatLng.newInstance(49.283905,-123.064465), 5, 1, 4, 1, 2);
//	    addRacker("219 North Union St, Vancouver, BC", LatLng.newInstance(49.277667,-123.098759), 4, 1, 0, 1, 2);
//	    addRacker("221 North Union St, Vancouver, BC", LatLng.newInstance(49.277666,-123.098711), 5, 1, 3, 1, 2);
//	    addRacker("231 North Union St, Vancouver, BC", LatLng.newInstance(49.277661,-123.098471), 2, 1, 3, 1, 2);
//	    addRacker("241 North Union St, Vancouver, BC", LatLng.newInstance(49.277656,-123.098232), 3, 1, 5, 1, 2);
//	    addRacker("243 North Union St, Vancouver, BC", LatLng.newInstance(49.277723,-123.098181), 4, 1, 0, 1, 2);
//	    addRacker("1033 North Venables St, Vancouver, BC", LatLng.newInstance(49.276913,-123.081606), 2, 1, 1, 1, 2);
//	    addRacker("4932 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.239512,-123.065337), 3, 1, 5, 1, 2);
//	    addRacker("4969 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.239457,-123.065374), 2, 1, 4, 1, 2);
//	    addRacker("4990 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.239103,-123.065157), 3, 1, 1, 1, 2);
//	    addRacker("5052 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.238509,-123.065359), 3, 1, 0, 1, 2);
//	    addRacker("5057 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.238793,-123.065572), 2, 1, 3, 1, 2);
//	    addRacker("5076 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.238341,-123.065359), 4, 1, 1, 1, 2);
//	    addRacker("5124 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.23766,-123.065389), 3, 1, 3, 1, 2);
//	    addRacker("5157 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.237916,-123.065414), 4, 1, 2, 1, 2);
//	    addRacker("5239 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.237128,-123.065442), 5, 1, 1, 1, 2);
//	    addRacker("5265 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.236891,-123.065447), 2, 1, 1, 1, 2);
//	    addRacker("5393 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.235362,-123.065485), 4, 1, 3, 1, 2);
//	    addRacker("5448 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.235413,-123.065447), 5, 1, 5, 1, 2);
//	    addRacker("5449 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.234836,-123.065504), 5, 1, 4, 1, 2);
//	    addRacker("5470 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.235243,-123.065451), 5, 1, 5, 1, 2);
//	    addRacker("5492 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.235022,-123.065462), 2, 1, 1, 1, 2);
//	    addRacker("5501 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.234395,-123.065674), 4, 1, 1, 1, 2);
//	    addRacker("5579 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.233852,-123.065529), 3, 1, 5, 1, 2);
//	    addRacker("5656 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.233276,-123.065508), 4, 1, 1, 1, 2);
//	    addRacker("5661 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.233088,-123.065685), 4, 1, 2, 1, 2);
//	    addRacker("5723 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.232352,-123.065569), 5, 1, 1, 1, 2);
//	    addRacker("5732 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.232195,-123.065396), 5, 1, 3, 1, 2);
//	    addRacker("5780 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.231667,-123.065432), 4, 1, 2, 1, 2);
//	    addRacker("5807 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.231609,-123.065589), 3, 1, 1, 1, 2);
//	    addRacker("5853 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.231081,-123.065596), 3, 1, 0, 1, 2);
//	    addRacker("5885 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.230756,-123.065602), 2, 1, 3, 1, 2);
//	    addRacker("6225 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.227282,-123.06586), 3, 1, 5, 1, 2);
//	    addRacker("6404 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.226171,-123.065685), 4, 1, 5, 1, 2);
//	    addRacker("6440 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.225918,-123.065692), 4, 1, 5, 1, 2);
//	    addRacker("6470 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.225527,-123.065588), 5, 1, 3, 1, 2);
//	    addRacker("6471 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.225739,-123.065901), 2, 2, 3, 1, 2);
//	    addRacker("6495 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.225353,-123.065915), 4, 1, 4, 1, 2);
//	    addRacker("6586 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.224206,-123.065741), 2, 1, 4, 1, 2);
//	    addRacker("6647 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.223683,-123.065786), 3, 1, 0, 1, 2);
//	    addRacker("6689 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.223235,-123.065796), 3, 1, 3, 1, 2);
//	    addRacker("6904 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.22082,-123.06583), 2, 1, 4, 1, 2);
//	    addRacker("6942 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.220827,-123.065791), 5, 1, 0, 1, 2);
//	    addRacker("6945 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.220977,-123.065864), 2, 1, 4, 1, 2);
//	    addRacker("6979 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.220577,-123.065872), 5, 1, 5, 1, 2);
//	    addRacker("1507 North W 12th, Vancouver, BC", LatLng.newInstance(49.26081,-123.139077), 3, 3, 0, 1, 2);
//	    addRacker("2108 South W 12th, Vancouver, BC", LatLng.newInstance(49.26103,-123.155241), 2, 1, 5, 1, 2);
//	    addRacker("1500 South W 13th, Vancouver, BC", LatLng.newInstance(49.259836,-123.139425), 3, 1, 5, 1, 2);
//	    addRacker("2595 North W 16th, Vancouver, BC", LatLng.newInstance(49.257706,-123.164646), 3, 1, 4, 1, 2);
//	    addRacker("2595 North W 16th, Vancouver, BC", LatLng.newInstance(49.257706,-123.164646), 3, 1, 3, 1, 2);
//	    addRacker("2603 North W 16th, Vancouver, BC", LatLng.newInstance(49.257829,-123.165031), 2, 2, 0, 1, 2);
//	    addRacker("2611 North W 16th, Vancouver, BC", LatLng.newInstance(49.257963,-123.16511), 4, 1, 3, 1, 2);
//	    addRacker("2100 South W 1st, Vancouver, BC", LatLng.newInstance(49.270762,-123.152554), 3, 1, 0, 1, 2);
//	    addRacker("500 North W 24th, Vancouver, BC", LatLng.newInstance(49.33136,-123.085426), 3, 1, 5, 1, 2);
//	    addRacker("29 North W 2nd, Vancouver, BC", LatLng.newInstance(49.269298,-123.105175), 4, 1, 3, 1, 2);
//	    addRacker("52 South W 2nd, Vancouver, BC", LatLng.newInstance(49.269254,-123.106566), 3, 2, 5, 1, 2);
//	    addRacker("118 South W 2nd, Vancouver, BC", LatLng.newInstance(49.269156,-123.107226), 4, 1, 4, 1, 2);
//	    addRacker("336 South W 2nd, Vancouver, BC", LatLng.newInstance(49.268114,-123.111514), 4, 1, 4, 1, 2);
//	    addRacker("1650 South W 2nd, Vancouver, BC", LatLng.newInstance(49.269717,-123.142007), 2, 1, 2, 1, 2);
//	    addRacker("1720 South W 2nd, Vancouver, BC", LatLng.newInstance(49.269678,-123.143338), 2, 1, 5, 1, 2);
//	    addRacker("525 North W 2nd, Vancouver, BC", LatLng.newInstance(49.267198,-123.113882), 5, 4, 2, 1, 2);
//	    addRacker("1900 South W 2nd, Vancouver, BC", LatLng.newInstance(49.269809,-123.147793), 2, 1, 5, 1, 2);
//	    addRacker("60 South W 3rd, Vancouver, BC", LatLng.newInstance(49.268305,-123.105708), 3, 1, 2, 1, 2);
//	    addRacker("105 North W 3rd, Vancouver, BC", LatLng.newInstance(49.268342,-123.106699), 4, 1, 4, 1, 2);
//	    addRacker("124 South W 3rd, Vancouver, BC", LatLng.newInstance(49.268322,-123.10697), 2, 1, 4, 1, 2);
//	    addRacker("126 South W 3rd, Vancouver, BC", LatLng.newInstance(49.268325,-123.107105), 3, 1, 2, 1, 2);
//	    addRacker("190 South W 3rd, Vancouver, BC", LatLng.newInstance(49.268362,-123.109848), 5, 1, 1, 1, 2);
//	    addRacker("290 South W 3rd, Vancouver, BC", LatLng.newInstance(49.268363,-123.109826), 3, 1, 5, 1, 2);
//	    addRacker("2107 North W 40th, Vancouver, BC", LatLng.newInstance(49.235576,-123.155589), 5, 1, 5, 1, 2);
//	    addRacker("2130 South W 40th, Vancouver, BC", LatLng.newInstance(49.235569,-123.156939), 4, 1, 1, 1, 2);
//	    addRacker("510 South W 41st, Vancouver, BC", LatLng.newInstance(49.233519,-123.116451), 4, 4, 0, 1, 2);
//	    addRacker("2021 North W 41st, Vancouver, BC", LatLng.newInstance(49.234661,-123.152748), 4, 1, 2, 1, 2);
//	    addRacker("2057 North W 41st, Vancouver, BC", LatLng.newInstance(49.234683,-123.153687), 4, 1, 3, 1, 2);
//	    addRacker("2094 South W 41st, Vancouver, BC", LatLng.newInstance(49.234522,-123.154701), 2, 1, 1, 1, 2);
//	    addRacker("2198 South W 41st, Vancouver, BC", LatLng.newInstance(49.234611,-123.158499), 3, 1, 2, 1, 2);
//	    addRacker("2208 South W 41st, Vancouver, BC", LatLng.newInstance(49.23456,-123.158774), 3, 1, 5, 1, 2);
//	    addRacker("2225 North W 41st, Vancouver, BC", LatLng.newInstance(49.23475,-123.15826), 3, 2, 2, 1, 2);
//	    addRacker("2246 South W 41st, Vancouver, BC", LatLng.newInstance(49.234437,-123.159179), 5, 1, 0, 1, 2);
//	    addRacker("2253 North W 41st, Vancouver, BC", LatLng.newInstance(49.234638,-123.158666), 5, 1, 5, 1, 2);
//	    addRacker("2260 South W 41st, Vancouver, BC", LatLng.newInstance(49.234579,-123.159278), 2, 1, 0, 1, 2);
//	    addRacker("2267 North W 41st, Vancouver, BC", LatLng.newInstance(49.234643,-123.158861), 2, 1, 1, 1, 2);
//	    addRacker("2305 North W 41st, Vancouver, BC", LatLng.newInstance(49.234836,-123.159797), 3, 1, 0, 1, 2);
//	    addRacker("2326 South W 41st, Vancouver, BC", LatLng.newInstance(49.234539,-123.16045), 3, 1, 5, 1, 2);
//	    addRacker("2443 North W 41st, Vancouver, BC", LatLng.newInstance(49.234697,-123.162126), 5, 1, 1, 1, 2);
//	    addRacker("2448 South W 41st, Vancouver, BC", LatLng.newInstance(49.234538,-123.162027), 3, 1, 2, 1, 2);
//	    addRacker("2496 South W 41st, Vancouver, BC", LatLng.newInstance(49.234678,-123.162586), 3, 1, 0, 1, 2);
//	    addRacker("38 South W 4th, Vancouver, BC", LatLng.newInstance(49.2674,-123.105724), 4, 1, 3, 1, 2);
//	    addRacker("163 West W 4th, Vancouver, BC", LatLng.newInstance(49.267483,-123.108684), 2, 1, 3, 1, 2);
//	    addRacker("165 West W 4th, Vancouver, BC", LatLng.newInstance(49.267556,-123.109324), 2, 1, 0, 1, 2);
//	    addRacker("1590 South W 4th, Vancouver, BC", LatLng.newInstance(49.267834,-123.140737), 3, 2, 0, 1, 2);
//	    addRacker("1599 North W 4th, Vancouver, BC", LatLng.newInstance(49.268122,-123.140672), 3, 2, 3, 1, 2);
//	    addRacker("1628 South W 4th, Vancouver, BC", LatLng.newInstance(49.267947,-123.14121), 4, 1, 5, 1, 2);
//	    addRacker("1723 North W 4th, Vancouver, BC", LatLng.newInstance(49.268109,-123.143841), 4, 1, 4, 1, 2);
//	    addRacker("1755 North W 4th, Vancouver, BC", LatLng.newInstance(49.268125,-123.144703), 4, 1, 0, 1, 2);
//	    addRacker("1793 North W 4th, Vancouver, BC", LatLng.newInstance(49.268141,-123.144994), 4, 2, 5, 1, 2);
//	    addRacker("2625 North W 4th, Vancouver, BC", LatLng.newInstance(49.268356,-123.16521), 4, 1, 4, 1, 2);
//	    addRacker("2645 North W 4th, Vancouver, BC", LatLng.newInstance(49.268419,-123.165735), 2, 1, 5, 1, 2);
//	    addRacker("2662 South W 4th, Vancouver, BC", LatLng.newInstance(49.268187,-123.165235), 2, 1, 4, 1, 2);
//	    addRacker("2682 South W 4th, Vancouver, BC", LatLng.newInstance(49.268333,-123.165757), 2, 1, 4, 1, 2);
//	    addRacker("2695 North W 4th, Vancouver, BC", LatLng.newInstance(49.268512,-123.166235), 2, 1, 0, 1, 2);
//	    addRacker("2702 South W 4th, Vancouver, BC", LatLng.newInstance(49.268233,-123.166519), 4, 1, 5, 1, 2);
//	    addRacker("2716 South W 4th, Vancouver, BC", LatLng.newInstance(49.268341,-123.166821), 5, 1, 2, 1, 2);
//	    addRacker("2722 South W 4th, Vancouver, BC", LatLng.newInstance(49.268343,-123.166969), 4, 1, 2, 1, 2);
//	    addRacker("2828 South W 4th, Vancouver, BC", LatLng.newInstance(49.268312,-123.168912), 2, 1, 2, 1, 2);
//	    addRacker("2880 South W 4th, Vancouver, BC", LatLng.newInstance(49.268312,-123.169744), 3, 1, 3, 1, 2);
//	    addRacker("2923 North W 4th, Vancouver, BC", LatLng.newInstance(49.268476,-123.170532), 5, 1, 4, 1, 2);
//	    addRacker("2947 North W 4th, Vancouver, BC", LatLng.newInstance(49.268419,-123.170517), 3, 1, 3, 1, 2);
//	    addRacker("2954 South W 4th, Vancouver, BC", LatLng.newInstance(49.268406,-123.17131), 2, 1, 3, 1, 2);
//	    addRacker("3303 North W 4th, Vancouver, BC", LatLng.newInstance(49.268524,-123.177825), 3, 1, 3, 1, 2);
//	    addRacker("3327 North W 4th, Vancouver, BC", LatLng.newInstance(49.268597,-123.178325), 4, 1, 0, 1, 2);
//	    addRacker("3337 North W 4th, Vancouver, BC", LatLng.newInstance(49.268596,-123.178621), 3, 1, 5, 1, 2);
//	    addRacker("3502 South W 4th, Vancouver, BC", LatLng.newInstance(49.268579,-123.181795), 4, 1, 4, 1, 2);
//	    addRacker("3512 South W 4th, Vancouver, BC", LatLng.newInstance(49.268583,-123.182223), 3, 1, 4, 1, 2);
//	    addRacker("3525 North W 4th, Vancouver, BC", LatLng.newInstance(49.268641,-123.182519), 2, 1, 1, 1, 2);
//	    addRacker("3545 North W 4th, Vancouver, BC", LatLng.newInstance(49.26871,-123.182697), 2, 1, 0, 1, 2);
//	    addRacker("3570 South W 4th, Vancouver, BC", LatLng.newInstance(49.268591,-123.183044), 3, 1, 1, 1, 2);
//	    addRacker("3571 North W 4th, Vancouver, BC", LatLng.newInstance(49.268699,-123.183487), 2, 2, 0, 1, 2);
//	    addRacker("3590 South W 4th, Vancouver, BC", LatLng.newInstance(49.268595,-123.183392), 3, 1, 0, 1, 2);
//	    addRacker("3598 South W 4th, Vancouver, BC", LatLng.newInstance(49.268469,-123.183385), 4, 1, 0, 1, 2);
//	    addRacker("3605 North W 4th, Vancouver, BC", LatLng.newInstance(49.268699,-123.18379), 2, 1, 2, 1, 2);
//	    addRacker("3608 South W 4th, Vancouver, BC", LatLng.newInstance(49.268497,-123.183822), 3, 1, 5, 1, 2);
//	    addRacker("3650 South W 4th, Vancouver, BC", LatLng.newInstance(49.268606,-123.184841), 2, 1, 4, 1, 2);
//	    addRacker("3661 North W 4th, Vancouver, BC", LatLng.newInstance(49.268712,-123.184871), 5, 1, 5, 1, 2);
//	    addRacker("3689 North W 4th, Vancouver, BC", LatLng.newInstance(49.268744,-123.185395), 2, 1, 1, 1, 2);
//	    addRacker("188 North W 5th, Vancouver, BC", LatLng.newInstance(49.266551,-123.109046), 2, 2, 3, 1, 2);
//	    addRacker("112 South W 6th, Vancouver, BC", LatLng.newInstance(49.265615,-123.107233), 3, 1, 0, 1, 2);
//	    addRacker("4 South W 7th, Vancouver, BC", LatLng.newInstance(49.264554,-123.10507), 3, 1, 4, 1, 2);
//	    addRacker("75 North W 7th, Vancouver, BC", LatLng.newInstance(49.264717,-123.106664), 2, 1, 1, 1, 2);
//	    addRacker("228 South W 7th, Vancouver, BC", LatLng.newInstance(49.264763,-123.10984), 3, 1, 2, 1, 2);
//	    addRacker("331 North W 7th, Vancouver, BC", LatLng.newInstance(49.264888,-123.111801), 3, 1, 0, 1, 2);
//	    addRacker("33 North W 8th, Vancouver, BC", LatLng.newInstance(49.263903,-123.105804), 3, 3, 2, 1, 2);
//	    addRacker("77 North W 8th, Vancouver, BC", LatLng.newInstance(49.26381,-123.106506), 3, 1, 5, 1, 2);
//	    addRacker("138 South W 8th, Vancouver, BC", LatLng.newInstance(49.263744,-123.108291), 3, 1, 0, 1, 2);
//	    addRacker("156 South W 8th, Vancouver, BC", LatLng.newInstance(49.263745,-123.108657), 2, 1, 3, 1, 2);
//	    addRacker("224 South W 8th, Vancouver, BC", LatLng.newInstance(49.263745,-123.110049), 3, 1, 2, 1, 2);
//	    addRacker("260 South W 8th, Vancouver, BC", LatLng.newInstance(49.263865,-123.110438), 2, 2, 3, 1, 2);
//	    addRacker("288 South W 8th, Vancouver, BC", LatLng.newInstance(49.263868,-123.110733), 4, 1, 0, 1, 2);
//	    addRacker("900 North W 8th, Vancouver, BC", LatLng.newInstance(49.264205,-123.124482), 2, 1, 3, 1, 2);
//	    addRacker("1500 South W 8th, Vancouver, BC", LatLng.newInstance(49.264484,-123.138688), 4, 1, 3, 1, 2);
//	    addRacker("5663 West W Boulevard, Vancouver, BC", LatLng.newInstance(49.235049,-123.155702), 4, 1, 0, 1, 2);
//	    addRacker("5765 West W Boulevard, Vancouver, BC", LatLng.newInstance(49.234026,-123.155615), 4, 1, 4, 1, 2);
//	    addRacker("5777 West W Boulevard, Vancouver, BC", LatLng.newInstance(49.233951,-123.155723), 4, 1, 3, 1, 2);
//	    addRacker("496 South W Broadway, Vancouver, BC", LatLng.newInstance(49.262983,-123.114518), 4, 11, 3, 1, 2);
//	    addRacker("1245 North W Broadway, Vancouver, BC", LatLng.newInstance(49.26352,-123.132477), 4, 1, 5, 1, 2);
//	    addRacker("1278 South W Broadway, Vancouver, BC", LatLng.newInstance(49.263339,-123.132747), 5, 1, 1, 1, 2);
//	    addRacker("1885 North W Broadway, Vancouver, BC", LatLng.newInstance(49.263896,-123.147736), 5, 2, 5, 1, 2);
//	    addRacker("2505 North W Broadway, Vancouver, BC", LatLng.newInstance(49.264119,-123.162715), 3, 1, 2, 1, 2);
//	    addRacker("3124 South W Broadway, Vancouver, BC", LatLng.newInstance(49.264218,-123.174853), 5, 1, 5, 1, 2);
//	    addRacker("2 South W Cordova, Vancouver, BC", LatLng.newInstance(49.282487,-123.104478), 2, 5, 4, 1, 2);
//	    addRacker("108 South W Cordova, Vancouver, BC", LatLng.newInstance(49.282897,-123.10708), 5, 1, 3, 1, 2);
//	    addRacker("301 North W Cordova, Vancouver, BC", LatLng.newInstance(49.283652,-123.109263), 3, 1, 0, 1, 2);
//	    addRacker("403 North W Cordova, Vancouver, BC", LatLng.newInstance(49.284192,-123.110048), 5, 2, 4, 1, 2);
//	    addRacker("403 North W Cordova, Vancouver, BC", LatLng.newInstance(49.284192,-123.110048), 3, 1, 3, 1, 2);
//	    addRacker("415 North W Cordova, Vancouver, BC", LatLng.newInstance(49.284232,-123.110174), 5, 2, 0, 1, 2);
//	    addRacker("601 North W Cordova, Vancouver, BC", LatLng.newInstance(49.285578,-123.111893), 2, 3, 5, 1, 2);
//	    addRacker("1100 South W Cordova, Vancouver, BC", LatLng.newInstance(49.289123,-123.121789), 5, 1, 4, 1, 2);
//	    addRacker("650 South W Georgia, Vancouver, BC", LatLng.newInstance(49.28209,-123.117732), 5, 1, 3, 1, 2);
//	    addRacker("702 South W Georgia, Vancouver, BC", LatLng.newInstance(49.282966,-123.118879), 2, 8, 0, 1, 2);
//	    addRacker("30 South W Hastings, Vancouver, BC", LatLng.newInstance(49.281583,-123.105574), 2, 1, 1, 1, 2);
//	    addRacker("43 North W Hastings, Vancouver, BC", LatLng.newInstance(49.281903,-123.106063), 2, 1, 4, 1, 2);
//	    addRacker("84 South W Hastings, Vancouver, BC", LatLng.newInstance(49.281817,-123.107008), 2, 3, 4, 1, 2);
//	    addRacker("128 South W Hastings, Vancouver, BC", LatLng.newInstance(49.282106,-123.108271), 5, 1, 1, 1, 2);
//	    addRacker("207 North W Hastings, Vancouver, BC", LatLng.newInstance(49.2829,-123.109623), 3, 1, 1, 1, 2);
//	    addRacker("219 North W Hastings, Vancouver, BC", LatLng.newInstance(49.282783,-123.109934), 3, 1, 0, 1, 2);
//	    addRacker("300 South W Hastings, Vancouver, BC", LatLng.newInstance(49.282809,-123.110027), 4, 1, 4, 1, 2);
//	    addRacker("314 South W Hastings, Vancouver, BC", LatLng.newInstance(49.282992,-123.110317), 3, 1, 2, 1, 2);
//	    addRacker("400 South W Hastings, Vancouver, BC", LatLng.newInstance(49.28342,-123.110994), 2, 3, 0, 1, 2);
//	    addRacker("473 North W Hastings, Vancouver, BC", LatLng.newInstance(49.283955,-123.111466), 3, 1, 3, 1, 2);
//	    addRacker("555 North W Hastings, Vancouver, BC", LatLng.newInstance(49.284683,-123.112399), 2, 2, 0, 1, 2);
//	    addRacker("838 South W Hastings, Vancouver, BC", LatLng.newInstance(49.286302,-123.115423), 3, 2, 0, 1, 2);
//	    addRacker("59 North W Pender, Vancouver, BC", LatLng.newInstance(49.280958,-123.106735), 5, 1, 2, 1, 2);
//	    addRacker("309 North W Pender, Vancouver, BC", LatLng.newInstance(49.282376,-123.111202), 5, 1, 3, 1, 2);
//	    addRacker("319 North W Pender, Vancouver, BC", LatLng.newInstance(49.282425,-123.11137), 4, 1, 0, 1, 2);
//	    addRacker("416 South W Pender, Vancouver, BC", LatLng.newInstance(49.282947,-123.112223), 2, 1, 4, 1, 2);
//	    addRacker("433 North W Pender, Vancouver, BC", LatLng.newInstance(49.283156,-123.112365), 2, 1, 0, 1, 2);
//	    addRacker("445 North W Pender, Vancouver, BC", LatLng.newInstance(49.283292,-123.112696), 2, 1, 4, 1, 2);
//	    addRacker("518 South W Pender, Vancouver, BC", LatLng.newInstance(49.283451,-123.112993), 5, 1, 1, 1, 2);
//	    addRacker("605 North W Pender, Vancouver, BC", LatLng.newInstance(49.284214,-123.113985), 4, 1, 1, 1, 2);
//	    addRacker("617 North W Pender, Vancouver, BC", LatLng.newInstance(49.284202,-123.114112), 3, 1, 2, 1, 2);
//	    addRacker("819 North W Pender, Vancouver, BC", LatLng.newInstance(49.285654,-123.116098), 5, 1, 5, 1, 2);
//	    addRacker("829 North W Pender, Vancouver, BC", LatLng.newInstance(49.285704,-123.116217), 3, 1, 3, 1, 2);
//	    addRacker("830 South W Pender, Vancouver, BC", LatLng.newInstance(49.285598,-123.116327), 4, 1, 2, 1, 2);
//	    addRacker("3 North Water St, Vancouver, BC", LatLng.newInstance(49.283524,-123.104447), 2, 3, 5, 1, 2);
//	    addRacker("10 South Water St, Vancouver, BC", LatLng.newInstance(49.283541,-123.104695), 5, 2, 5, 1, 2);
//	    addRacker("19 North Water St, Vancouver, BC", LatLng.newInstance(49.283587,-123.104819), 5, 1, 3, 1, 2);
//	    addRacker("102 South Water St, Vancouver, BC", LatLng.newInstance(49.283785,-123.106609), 3, 1, 2, 1, 2);
//	    addRacker("142 South Water St, Vancouver, BC", LatLng.newInstance(49.283983,-123.107517), 3, 1, 2, 1, 2);
//	    addRacker("199 North Water St, Vancouver, BC", LatLng.newInstance(49.284365,-123.108632), 2, 1, 1, 1, 2);
//	    addRacker("322 South Water St, Vancouver, BC", LatLng.newInstance(49.284284,-123.109409), 4, 1, 4, 1, 2);
//	    addRacker("332 South Water St, Vancouver, BC", LatLng.newInstance(49.284349,-123.109688), 5, 1, 3, 1, 2);
//	    addRacker("375 North Water St, Vancouver, BC", LatLng.newInstance(49.284731,-123.110571), 5, 3, 0, 1, 2);
//	    addRacker("2300 West Willow, Vancouver, BC", LatLng.newInstance(49.264262,-123.121769), 2, 1, 4, 1, 2);
//	    addRacker("5687 West Yew St, Vancouver, BC", LatLng.newInstance(49.234777,-123.157842), 2, 1, 5, 1, 2);
//	    addRacker("5692 East Yew St, Vancouver, BC", LatLng.newInstance(49.234918,-123.157611), 5, 2, 2, 1, 2);
//	    addRacker("2490 East Yukon St, Vancouver, BC", LatLng.newInstance(49.263255,-123.112883), 3, 1, 4, 1, 2);
//		crimeOps(2011, LatLng.newInstance(49.03026,-122.79582), 1);
//	    crimeOps(2011, LatLng.newInstance(49.16268,-123.14441), 1);
//	    crimeOps(2011, LatLng.newInstance(49.18967,-123.14418), 1);
//	    crimeOps(2011, LatLng.newInstance(49.203193,-122.910916), 1);
//	    crimeOps(2011, LatLng.newInstance(49.204003,-122.906742), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20402,-123.14281), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20467,-123.14146), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20663,-123.05049), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20671,-123.05734), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20689,-123.12479), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20709,-123.04781), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20839,-122.89945), 1);
//	    crimeOps(2011, LatLng.newInstance(49.209095,-122.90258), 1);
//	    crimeOps(2011, LatLng.newInstance(49.209146,-122.902647), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20918,-122.902692), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20923,-122.89867), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20929,-122.89863), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2093,-122.90285), 1);
//	    crimeOps(2011, LatLng.newInstance(49.209384,-122.90295), 1);
//	    crimeOps(2011, LatLng.newInstance(49.209397,-122.902965), 1);
//	    crimeOps(2011, LatLng.newInstance(49.209486,-122.90307), 1);
//	    crimeOps(2011, LatLng.newInstance(49.20952,-122.90311), 1);
//	    crimeOps(2011, LatLng.newInstance(49.209783,-122.898324), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21021,-123.02687), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21051,-123.02737), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21065,-123.06608), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21111,-123.0641), 1);
//	    crimeOps(2011, LatLng.newInstance(49.211386,-122.897286), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21149,-123.06378), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2117,-123.06448), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21191,-123.10475), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21209,-123.10538), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21216,-123.10565), 1);
//	    crimeOps(2011, LatLng.newInstance(49.212176,-122.896493), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21233,-123.10651), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21234,-123.05931), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21301,-123.1287), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21359,-123.06467), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2139,-123.1532), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21402,-123.02992), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21445,-123.05534), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21457,-123.06848), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21506,-123.13889), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21526,-123.0291), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21545,-123.07404), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21561,-123.03325), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21579,-123.05931), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21579,-123.06845), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21613,-123.05251), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21629,-123.0246), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21657,-123.06331), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2166,-123.06464), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21664,-123.06924), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21675,-123.0295), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21688,-123.17323), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21693,-123.05508), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21707,-123.07024), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21712,-123.062), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21735,-123.07117), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21738,-123.07124), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21764,-123.05573), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21811,-123.02902), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21823,-123.0489), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21842,-123.03848), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21857,-123.02499), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21893,-123.05063), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21929,-123.18367), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21935,-123.0481), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21936,-123.03172), 1);
//	    crimeOps(2011, LatLng.newInstance(49.21947,-123.06454), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22278,-123.02661), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22278,-123.0489), 1);
//	    crimeOps(2011, LatLng.newInstance(49.223714,-122.888253), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22373,-122.888271), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22435,-123.11346), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22503,-123.15681), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22643,-123.03514), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22672,-123.04909), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22688,-123.05605), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22714,-123.19841), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22716,-123.19417), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22731,-123.02783), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22755,-123.04764), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22765,-123.1933), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22849,-123.03651), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22849,-123.05009), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22853,-123.05466), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22926,-123.02783), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22926,-123.02924), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22926,-123.03058), 1);
//	    crimeOps(2011, LatLng.newInstance(49.22945,-123.05789), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23006,-123.12155), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23083,-123.03064), 1);
//	    crimeOps(2011, LatLng.newInstance(49.231332,-122.861487), 1);
//	    crimeOps(2011, LatLng.newInstance(49.231518,-122.861491), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2316,-123.03607), 1);
//	    crimeOps(2011, LatLng.newInstance(49.231704,-122.861495), 1);
//	    crimeOps(2011, LatLng.newInstance(49.231797,-122.861497), 1);
//	    crimeOps(2011, LatLng.newInstance(49.231979,-122.8615), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23207,-123.06052), 1);
//	    crimeOps(2011, LatLng.newInstance(49.232142,-122.8615), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23242,-123.13643), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23247,-123.0361), 1);
//	    crimeOps(2011, LatLng.newInstance(49.232515,-122.861513), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23254,-123.12666), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23254,-123.14333), 1);
//	    crimeOps(2011, LatLng.newInstance(49.232562,-122.861515), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23259,-123.14635), 1);
//	    crimeOps(2011, LatLng.newInstance(49.232605,-122.861516), 1);
//	    crimeOps(2011, LatLng.newInstance(49.232647,-122.861518), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23274,-123.19684), 1);
//	    crimeOps(2011, LatLng.newInstance(49.232815,-122.861523), 1);
//	    crimeOps(2011, LatLng.newInstance(49.232873,-122.861525), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23289,-123.06212), 1);
//	    crimeOps(2011, LatLng.newInstance(49.232899,-122.861526), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23297,-123.04178), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2332,-123.13179), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23373,-123.10553), 1);
//	    crimeOps(2011, LatLng.newInstance(49.233731,-123.10558), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23392,-123.03101), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23398,-123.03043), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23405,-123.04741), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23409,-123.02481), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23468,-123.03699), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2347,-123.02815), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23478,-123.18956), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23491,-122.87305), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23497,-122.84451), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23502,-123.02695), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23508,-123.0304), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23509,-123.04019), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23512,-122.8454), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23528,-122.84442), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23547,-122.84464), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23547,-123.14136), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23569,-123.11186), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23581,-123.03429), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23582,-123.05026), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23589,-123.02894), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23598,-123.04443), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23611,-123.04422), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23625,-123.03668), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2366,-123.09039), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23662,-123.03065), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23685,-123.02777), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23691,-123.03812), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23734,-123.0518), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23735,-123.05309), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23735,-123.05768), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23798,-123.15546), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23827,-123.08124), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23831,-123.08568), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23836,-123.08914), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2386,-123.04391), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2388,-123.03955), 1);
//	    crimeOps(2011, LatLng.newInstance(49.239,-123.03514), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23913,-123.11665), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2392,-123.03197), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23923,-123.0406), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23927,-123.15482), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23959,-123.05099), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23975,-123.02638), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23984,-123.03716), 1);
//	    crimeOps(2011, LatLng.newInstance(49.23987,-123.03114), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24001,-123.04253), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2404,-123.04347), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24042,-123.03246), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24045,-123.0251), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2406,-123.02837), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24086,-123.11676), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24088,-123.08786), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24097,-123.14224), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24112,-123.03885), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24153,-123.14751), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24161,-123.06935), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24164,-123.04652), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24167,-123.04912), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24214,-123.11826), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24223,-123.16006), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24251,-123.03896), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24251,-123.0489), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24255,-123.07755), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24255,-123.0788), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24265,-123.09413), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2429,-123.05273), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24292,-123.03709), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24295,-123.0314), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24305,-123.10918), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24307,-123.07068), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24309,-123.04172), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24355,-123.12906), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24362,-123.04301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2437,-123.05353), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24376,-123.03062), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24382,-123.02806), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24399,-123.06049), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24419,-123.02618), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24422,-123.04031), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24446,-123.09985), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24449,-123.12988), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24466,-123.07811), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24516,-123.06515), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2455,-123.08122), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24615,-123.17561), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24639,-123.12765), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24652,-122.83275), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24669,-123.1617), 1);
//	    crimeOps(2011, LatLng.newInstance(49.246711,-123.110181), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24722,-123.10112), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24795,-123.0712), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24817,-123.06577), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2482,-123.05798), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24846,-123.07386), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2489,-123.10487), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2489,-123.104923), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2489,-123.104924), 1);
//	    crimeOps(2011, LatLng.newInstance(49.248901,-123.104978), 1);
//	    crimeOps(2011, LatLng.newInstance(49.248902,-123.105029), 1);
//	    crimeOps(2011, LatLng.newInstance(49.248902,-123.105033), 1);
//	    crimeOps(2011, LatLng.newInstance(49.248903,-123.105083), 1);
//	    crimeOps(2011, LatLng.newInstance(49.248904,-123.105136), 1);
//	    crimeOps(2011, LatLng.newInstance(49.248904,-123.105189), 1);
//	    crimeOps(2011, LatLng.newInstance(49.248905,-123.105196), 1);
//	    crimeOps(2011, LatLng.newInstance(49.248905,-123.105242), 1);
//	    crimeOps(2011, LatLng.newInstance(49.248906,-123.105296), 1);
//	    crimeOps(2011, LatLng.newInstance(49.248909,-123.105468), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24891,-123.105576), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24919,-123.1582), 1);
//	    crimeOps(2011, LatLng.newInstance(49.24982,-123.15318), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25011,-123.10988), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25034,-123.06253), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25038,-123.06667), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25046,-123.0602), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25046,-123.07458), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25075,-123.12962), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25076,-123.15997), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25085,-123.13904), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25097,-123.15139), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25098,-123.14401), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25104,-123.06171), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25166,-123.07014), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25175,-123.12746), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25192,-123.13202), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25193,-123.13899), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25209,-123.13675), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25229,-123.06764), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25235,-123.03104), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25249,-123.06793), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25252,-123.06929), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25267,-123.12742), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25275,-123.06419), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25317,-123.03375), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25352,-123.13503), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25357,-123.02454), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25403,-123.13072), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25435,-123.07088), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25438,-123.07269), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25455,-123.02753), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25455,-123.13889), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25484,-123.03055), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25523,-123.02654), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25555,-123.17584), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2556,-123.02937), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25562,-123.14381), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25621,-123.13597), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25626,-123.03258), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2563,-123.07968), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25637,-123.06983), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25641,-123.1786), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25665,-123.07588), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25711,-123.13023), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25719,-123.13827), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2574,-123.04955), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25819,-123.03036), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25821,-123.05296), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25908,-123.0443), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25946,-123.06161), 1);
//	    crimeOps(2011, LatLng.newInstance(49.25972,-123.03205), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26021,-123.09822), 1);
//	    crimeOps(2011, LatLng.newInstance(49.260441,-123.114029), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26103,-123.04016), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26131,-123.06351), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2619,-123.03242), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26218,-122.57653), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2623,-123.06697), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26232,-123.02348), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26302,-123.10492), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26302,-123.104959), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263021,-123.104998), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263021,-123.105), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263022,-123.105037), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263022,-123.10504), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263023,-123.105076), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263023,-123.10508), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263024,-123.105115), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263024,-123.10512), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263024,-123.105155), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263025,-123.10516), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263025,-123.105194), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263025,-123.1052), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263026,-123.105233), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263026,-123.10524), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263027,-123.105272), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263027,-123.10528), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263028,-123.105311), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263028,-123.10532), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263029,-123.10536), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263029,-123.10539), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26303,-123.1054), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26303,-123.105429), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26303,-123.10544), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263031,-123.10548), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263032,-123.105507), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263032,-123.10552), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263033,-123.105546), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263033,-123.10556), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263033,-123.105586), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263034,-123.1056), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263034,-123.105625), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26306,-123.10684), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26321,-123.1149), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26324,-123.17195), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26373,-123.1467), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263818,-123.100346), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263844,-123.100367), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263846,-123.100369), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263875,-123.100392), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263896,-123.100408), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263905,-123.100416), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263935,-123.10044), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263948,-123.10045), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263964,-123.100463), 1);
//	    crimeOps(2011, LatLng.newInstance(49.263994,-123.100487), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264,-123.100492), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264023,-123.10051), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264026,-123.100512), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264053,-123.100534), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264078,-123.100554), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264082,-123.100558), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264104,-123.100575), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26413,-123.100596), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264141,-123.100605), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264156,-123.100616), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264171,-123.100629), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2642,-123.100652), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264208,-123.100658), 1);
//	    crimeOps(2011, LatLng.newInstance(49.264234,-123.100679), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26426,-123.1007), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26428,-123.0813), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26433,-123.0851), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26436,-123.08705), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26453,-123.1953), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26454,-123.10087), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26466,-123.19646), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26477,-123.10945), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26505,-123.11982), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26509,-123.12171), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26513,-123.12409), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2653,-123.089), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26532,-123.09091), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26537,-123.09483), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26608,-123.07672), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26613,-123.13601), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26644,-123.0685), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2665,-123.12036), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26663,-123.11277), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26665,-123.12673), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26669,-123.09633), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26688,-123.09581), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26734,-123.11945), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26749,-123.10945), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26752,-123.1108), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26781,-123.09821), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26826,-123.11149), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26839,-123.10943), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26869,-123.11829), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26894,-123.03095), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26952,-123.13218), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26966,-123.13912), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26973,-123.13346), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26975,-123.14319), 1);
//	    crimeOps(2011, LatLng.newInstance(49.269896,-123.132426), 1);
//	    crimeOps(2011, LatLng.newInstance(49.269897,-123.132427), 1);
//	    crimeOps(2011, LatLng.newInstance(49.26993,-123.09819), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27001,-123.11522), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27022,-123.17366), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27045,-123.18944), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27046,-123.04694), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27052,-123.10033), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27054,-123.1346), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27057,-123.07539), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27074,-123.13594), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27097,-123.1046), 1);
//	    crimeOps(2011, LatLng.newInstance(49.271,-123.10656), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27105,-123.14112), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27107,-123.09824), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27113,-123.17365), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27119,-123.17752), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27122,-123.05798), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27122,-123.17953), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27123,-123.16821), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27126,-123.18152), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27135,-123.18753), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27141,-123.07155), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27141,-123.13597), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27154,-123.10459), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27155,-123.14641), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27159,-123.13598), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27171,-123.15498), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27176,-123.1573), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2718,-123.15964), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2719,-123.16429), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27191,-123.16615), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27196,-123.12145), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27213,-123.18346), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27214,-123.14064), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27216,-123.07446), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27218,-123.12792), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27224,-123.18554), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2724,-123.14595), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27247,-123.14892), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27251,-123.1514), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27254,-123.08673), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27254,-123.20573), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27264,-123.0928), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27266,-123.16199), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27278,-123.1409), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27297,-123.10149), 1);
//	    crimeOps(2011, LatLng.newInstance(49.273,-123.16425), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27305,-123.1259), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2731,-123.11995), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27318,-123.11596), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27321,-123.07855), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27328,-123.04269), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27361,-123.20876), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27371,-123.0629), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27386,-123.05984), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27396,-123.20259), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27412,-123.11333), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27413,-123.11495), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27417,-123.13098), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2742,-123.1249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27425,-123.08144), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27425,-123.11508), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27442,-123.14625), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27452,-123.11256), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2746,-123.12009), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27476,-123.11285), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27492,-123.10194), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27501,-123.07925), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27507,-123.14412), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2754,-123.09898), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27541,-123.15181), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27556,-123.12053), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27573,-123.09305), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27585,-123.08263), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27591,-123.13602), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27592,-123.08), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27607,-123.10072), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2762,-123.14619), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27621,-123.14766), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27625,-123.07144), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27625,-123.15005), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27631,-123.12167), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27649,-123.08493), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27651,-123.11554), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27666,-123.06372), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27676,-123.10097), 1);
//	    crimeOps(2011, LatLng.newInstance(49.276943,-123.103488), 1);
//	    crimeOps(2011, LatLng.newInstance(49.276945,-123.10349), 1);
//	    crimeOps(2011, LatLng.newInstance(49.276946,-123.103492), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277133,-123.10271), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277136,-123.102715), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27714,-123.11826), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27731,-123.03145), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27751,-123.11871), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27753,-123.05196), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27757,-123.11394), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277576,-122.85249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27758,-123.12359), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277593,-122.85249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277614,-122.85249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277615,-123.103551), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277617,-123.103584), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277618,-122.85249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277618,-123.103617), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27762,-123.10365), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27764,-123.1313), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277643,-122.85249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277656,-122.85249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27767,-123.07718), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277679,-122.85249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277681,-122.85249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277692,-122.85249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.277694,-122.85249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27772,-123.11005), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27774,-123.11744), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27793,-123.10211), 1);
//	    crimeOps(2011, LatLng.newInstance(49.278008,-122.86177), 1);
//	    crimeOps(2011, LatLng.newInstance(49.278032,-122.86177), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27834,-123.08488), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27834,-123.13029), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27835,-123.06367), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27837,-123.08722), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27843,-123.12817), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2785,-123.09698), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27858,-123.11031), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2787,-123.11223), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27914,-123.06367), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27921,-123.08484), 1);
//	    crimeOps(2011, LatLng.newInstance(49.279245,-123.104271), 1);
//	    crimeOps(2011, LatLng.newInstance(49.279246,-123.104274), 1);
//	    crimeOps(2011, LatLng.newInstance(49.279247,-123.104319), 1);
//	    crimeOps(2011, LatLng.newInstance(49.279248,-123.104363), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27925,-123.10441), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27931,-123.09149), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27934,-123.09341), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27945,-123.07713), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27959,-123.04138), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2797,-123.06385), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27984,-123.11058), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27987,-123.126), 1);
//	    crimeOps(2011, LatLng.newInstance(49.27989,-123.03349), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28006,-123.10437), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28011,-123.10587), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28011,-123.12759), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28014,-123.1101), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28015,-123.08716), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28017,-123.0361), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2802,-123.08951), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28023,-123.09338), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28026,-123.09531), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28027,-123.09724), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28033,-123.06171), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28043,-123.1115), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28046,-123.12706), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2805,-123.13842), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28052,-123.10437), 1);
//	    crimeOps(2011, LatLng.newInstance(49.280538,-123.10448), 1);
//	    crimeOps(2011, LatLng.newInstance(49.280556,-123.10459), 1);
//	    crimeOps(2011, LatLng.newInstance(49.280574,-123.1047), 1);
//	    crimeOps(2011, LatLng.newInstance(49.280592,-123.10481), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28061,-123.10492), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28063,-123.105023), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28065,-123.105126), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28067,-123.10523), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28074,-123.105637), 1);
//	    crimeOps(2011, LatLng.newInstance(49.280757,-123.105737), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28076,-123.105751), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28077,-123.105809), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28079,-123.03875), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28095,-123.1224), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28107,-123.08475), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28108,-123.13014), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28109,-123.10757), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28114,-123.07916), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28118,-123.12596), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28119,-123.05656), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28129,-123.0726), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2813,-123.07408), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28133,-123.07806), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28134,-123.07771), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28139,-123.07915), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28141,-123.10927), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28142,-123.13704), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28148,-123.10436), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281491,-123.10442), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281502,-123.10448), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281513,-123.10454), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281524,-123.1046), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281535,-123.10466), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281546,-123.10472), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281568,-123.10484), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28158,-123.1049), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281591,-123.10496), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281602,-123.10502), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281613,-123.10508), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281624,-123.10514), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281635,-123.1052), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281657,-123.10532), 1);
//	    crimeOps(2011, LatLng.newInstance(49.281668,-123.10538), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28186,-123.12492), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28203,-123.08238), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28209,-123.136), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2821,-123.0578), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28213,-123.11093), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28214,-123.09137), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28226,-123.07407), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2823,-123.07706), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28232,-123.07912), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28235,-123.08127), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28246,-123.1043), 1);
//	     crimeOps(2011, LatLng.newInstance(49.282469,-123.10435), 1);
//	    crimeOps(2011, LatLng.newInstance(49.282469,-123.104351), 1);
//	    crimeOps(2011, LatLng.newInstance(49.282479,-123.104401), 1);
//	    crimeOps(2011, LatLng.newInstance(49.282489,-123.104455), 1);
//	    crimeOps(2011, LatLng.newInstance(49.282498,-123.104502), 1);
//	    crimeOps(2011, LatLng.newInstance(49.282499,-123.104506), 1);
//	    crimeOps(2011, LatLng.newInstance(49.282508,-123.104558), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28252,-123.10958), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28276,-123.1119), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28293,-123.10678), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28306,-123.10476), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28307,-123.09132), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28313,-123.12469), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28316,-123.07031), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28325,-123.102932), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28325,-123.102944), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28325,-123.102945), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28325,-123.102956), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28325,-123.102957), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283251,-123.102981), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283251,-123.102982), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283251,-123.103005), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283251,-123.103007), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283251,-123.103017), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283251,-123.10302), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283251,-123.10303), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28335,-123.10406), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283371,-123.104021), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283393,-123.103982), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28341,-123.11092), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283414,-123.103945), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283493,-123.08705), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283499,-123.08705), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2835,-123.1043), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28351,-123.06815), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28355,-123.07249), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283572,-123.072491), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283573,-123.104686), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283589,-123.104772), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283615,-123.072492), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283617,-123.072492), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283628,-123.072493), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283637,-123.072493), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283683,-123.072495), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28389,-123.10636), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28391,-123.1302), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28392,-123.0703), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283928,-123.0703), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28395,-123.12172), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28395,-123.12172), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283955,-123.0703), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283963,-123.070301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283964,-123.070301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283972,-123.070301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283973,-123.070301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283981,-123.070301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283982,-123.070301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28399,-123.070301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283991,-123.070301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.283998,-123.070301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284,-123.070301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284007,-123.070302), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284009,-123.070302), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284016,-123.070302), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284018,-123.070302), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284025,-123.070302), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284036,-123.070302), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284042,-123.070302), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284045,-123.070302), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28405,-123.11188), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284054,-123.070303), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28406,-123.09521), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284106,-123.065482), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284143,-123.06548), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284193,-123.065477), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284197,-123.065477), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284228,-123.065475), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284246,-123.065474), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284281,-123.065472), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284288,-123.065472), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28432,-123.09782), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284323,-123.068079), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28433,-123.068079), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28433,-123.1088), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284337,-123.068078), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284342,-123.065469), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284352,-123.065468), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28436,-123.065468), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28437,-123.065467), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284378,-123.065467), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284396,-123.065466), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284406,-123.065465), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284415,-123.065465), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284423,-123.065464), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284428,-123.099301), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284433,-123.065463), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284477,-123.065461), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284494,-123.06546), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28451,-123.10075), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284521,-123.099234), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284541,-123.065457), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284548,-123.065457), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284565,-123.065456), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284578,-123.065455), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284632,-123.065452), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284636,-123.065452), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284668,-123.10408), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284686,-123.065449), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2847,-123.099139), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284706,-123.063526), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284707,-123.065448), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284723,-123.065447), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284725,-123.065447), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284729,-123.063525), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284733,-123.088356), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284736,-123.088353), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284739,-123.08835), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28474,-123.02357), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28474,-123.02554), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284759,-123.02554), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284759,-123.0282), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28476,-123.04404), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284761,-123.065445), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28477,-123.0545), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284778,-123.065444), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284779,-123.0282), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284786,-123.093191), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284789,-123.051899), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28479,-123.04665), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284794,-123.097911), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284796,-123.065443), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284799,-123.04404), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284803,-123.09319), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284806,-123.023339), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284808,-123.051899), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284808,-123.097913), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284809,-123.097914), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28481,-123.05652), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284818,-123.04404), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28482,-123.09319), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284824,-123.063522), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284824,-123.097916), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284828,-123.056519), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284828,-123.05769), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284829,-123.05769), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284834,-123.1007), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28484,-123.05968), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284847,-123.056519), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28485,-123.09917), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284865,-123.051897), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28487,-123.06155), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284871,-123.023567), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284883,-123.04665), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284885,-123.0545), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284885,-123.056518), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284887,-123.061549), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284895,-123.059678), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284896,-123.04404), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284898,-123.0282), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2849,-123.06352), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284907,-123.0545), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284908,-123.02554), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284911,-123.030764), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284916,-123.100651), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284917,-123.099219), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284921,-123.056517), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284924,-123.04926), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284961,-123.056516), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284961,-123.05769), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284961,-123.100601), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284965,-123.0545), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284968,-123.030766), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284973,-123.030766), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284974,-123.04404), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284975,-123.061548), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28498,-123.11238), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284984,-123.099268), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284992,-123.0282), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284992,-123.061548), 1);
//	    crimeOps(2011, LatLng.newInstance(49.284994,-123.04404), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285,-123.051895), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285006,-123.100552), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285007,-123.061547), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285018,-123.056515), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285032,-123.056515), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285033,-123.099318), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285034,-123.04665), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285037,-123.056515), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285051,-123.056514), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285051,-123.100503), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285056,-123.056514), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285058,-123.04926), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285062,-123.061547), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285071,-123.04926), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285075,-123.056514), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285075,-123.061546), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285088,-123.056513), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28509,-123.04926), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285094,-123.023336), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285094,-123.05769), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285096,-123.100453), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2851,-123.05769), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285113,-123.05769), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285118,-123.099416), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285118,-123.100404), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28512,-123.05769), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285142,-123.0545), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28515,-123.11998), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285153,-123.051891), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285157,-123.100305), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285165,-123.04926), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285177,-123.100256), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285181,-123.056511), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285184,-123.04926), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285189,-123.056511), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285197,-123.100206), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2852,-123.056511), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285203,-123.099614), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285209,-123.023334), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285213,-123.099663), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285217,-123.099713), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285217,-123.100157), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285218,-123.056511), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285221,-123.04926), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285221,-123.100108), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285222,-123.099762), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285222,-123.100058), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285224,-123.100009), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285226,-123.09996), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285227,-123.099811), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285227,-123.09991), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285229,-123.099861), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28523,-123.04926), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285246,-123.023558), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285266,-123.023334), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285269,-123.04926), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285288,-123.04926), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285292,-123.056509), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28534,-123.056508), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28539,-123.09518), 1);
//	    crimeOps(2011, LatLng.newInstance(49.285497,-123.056504), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2856,-123.13069), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28571,-123.06146), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28571,-123.11325), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28586,-123.1354), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28593,-123.1168), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28684,-123.11417), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28691,-123.11347), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28701,-123.13132), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28722,-123.13099), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28727,-123.13327), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28734,-123.13551), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28737,-123.11284), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28754,-123.06047), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28764,-123.13816), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28791,-123.11581), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28795,-123.0335), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28844,-123.05968), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28878,-123.11856), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2888,-123.12272), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28924,-123.12989), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28931,-123.05894), 1);
//	    crimeOps(2011, LatLng.newInstance(49.28994,-123.12412), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29007,-123.13755), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29029,-123.12607), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29042,-123.12845), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29077,-123.14679), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2911,-123.13916), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29117,-123.13281), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29139,-123.12935), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29188,-123.1312), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2932,-123.13594), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29337,-123.13263), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29395,-122.76317), 1);
//	    crimeOps(2011, LatLng.newInstance(49.2944,-123.13683), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29709,-123.13559), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29735,-123.13594), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29785,-123.13091), 1);
//	    crimeOps(2011, LatLng.newInstance(49.29964,-123.13578), 1);
//	    crimeOps(2011, LatLng.newInstance(49.30094,-123.14331), 1);
//	    crimeOps(2011, LatLng.newInstance(49.32797,-123.16811), 1);
	    //String test = LatLng.newInstance(49.26688,-123.09581).toString();
	    //Window.alert("DONE ");
	    }
	public static ArrayList<Crime> getCrimeData()
	{
		return listofcrimes;
	}
	public static ArrayList<BikeRack> getRackData()
	{
		return listofracks;
	}
//	// because singleton. 
//	private PersistenceManager getPersistenceManager() {
//		// TODO Auto-generated method stub
//		return PMF.getPersistenceManager();
//	}
}

