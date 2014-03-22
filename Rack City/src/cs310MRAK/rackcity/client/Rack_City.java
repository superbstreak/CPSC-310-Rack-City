package cs310MRAK.rackcity.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.google.api.gwt.client.GoogleApiRequestTransport;
import com.google.api.gwt.client.OAuth2Login;
import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.api.gwt.services.plus.shared.Plus;
import com.google.api.gwt.services.plus.shared.Plus.ActivitiesContext.ListRequest.Collection;
import com.google.api.gwt.services.plus.shared.Plus.PeopleContext.GetRequest;
import com.google.api.gwt.services.plus.shared.Plus.PlusAuthScope;
import com.google.api.gwt.services.plus.shared.model.Activity;
import com.google.api.gwt.services.plus.shared.model.ActivityFeed;
import com.google.api.gwt.services.plus.shared.model.Person;
import com.google.gwt.core.client.Callback;
//======================== ^ G+ ============================
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
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
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;

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
	  private static final Plus plus = GWT.create(Plus.class);
	  private static final String CLIENT_ID = "146858113551-ktl431gm3sbkrvid1khqrlvh1afclct4.apps.googleusercontent.com";
	  private static final String API_KEY = "AIzaSyCeb8Iws1UqI8caz2aHJee_JtTTiNdqqAY";
	  private static final String APPLICATION_NAME = "cs310rackcity";
	  private int loginFlipFlop = 0;
	  private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	  private static final String PLUS_ME_SCOPE = "https://www.googleapis.com/auth/plus.me";
	  private static final Auth AUTH = Auth.get();
	 // Server stuff
	  private rackServiceAsync rService = GWT.create(rackService.class);
	  private crimeServiceAsync cService = GWT.create(crimeService.class);
	  private int initialsync = 0;
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		
		// ================ Please double check this =============
//		FTPserviceAsync ftpService = GWT.create(FTPservice.class);
//		AsyncCallback callback = new AsyncCallback<Void>()
//				{
//					public void onFailure(Throwable error)
//					{
//						Window.alert("Failed FTP.");
//						handleError(error);
//					}
//					public void onSuccess(Void ignore)
//					{
//						Window.alert("Success FTP");
//					}};
//		ftpService.adminConnection(callback);
		// ========================================================
		if (initialsync == 0)
		{
			addtolist();
			initialsync = 1;
		}
			
		
		GUIsetup();
		
	}
	
	 private void handleError(Throwable error) {
		    Window.alert(error.getMessage());
		    if (error instanceof NotLoggedInException) {
		      Window.Location.replace(loginInfo.getLogoutUrl());
		    }
		  }
	 
	private void getMe(final Plus p)
	{	
		messenger("inGetMe");		
		p.people().get("me").to(new Receiver <Person>(){
			@Override
			public void onSuccess(Person per) {
				// TODO Auto-generated method stub
				String out = "Welcom "+ per.getDisplayName()+"! Your Bday: "+ per.getBirthday()
						+", Gender: "+per.getGender();
				messenger(out);
				getMyActivities(p);
			}
		}).fire();
				
	}
	
	private void messenger(String s)
	{
		Window.alert(s);
	}
	
	private void getMyActivities(Plus p)
	{
		Window.alert("DEBUG GMA-FETCHING");
		p.activities().list("me", Collection.PUBLIC).to(new Receiver<ActivityFeed>()
		{
			@Override
			public void onSuccess(ActivityFeed f) {
				// TODO Auto-generated method stub
				if (f.getItems() == null || f.getItems().isEmpty())
				{
					messenger("DEBUG GMA-NO-ACTV");
				}
				else
				{
					messenger("DEBUG GMA-HAVE-ACTV");
				}
			}
		});
	}
	
	private void startLoginProcess(final Plus p, final Button loginButton)
	{
		if (loginFlipFlop == 0)
		{
			OAuth2Login.get().authorize(CLIENT_ID, PlusAuthScope.PLUS_ME, new Callback<Void, Exception>()
					{
						public void onSuccess(Void v)
						{
							messenger("G+ SUCCESS-SLP-SUCC");
							loginButton.setText("Sign Out");
							loginFlipFlop = 1;
							getMe(p);
						}
						@Override
						public void onFailure(Exception reason) {
							messenger("G+ ERROR-SLP-Fail!");
					    	handleError(reason);
						}
					});
			
			
			final AuthRequest req = new AuthRequest(GOOGLE_AUTH_URL, CLIENT_ID).withScopes(PLUS_ME_SCOPE);
			AUTH.login(req, new Callback<String, Throwable>() {
		          @Override
		          public void onSuccess(String token) {
		            Window.alert("Got an OAuth token:\n" + token + "\n"+ "Token expires in " + AUTH.expiresIn(req) + " ms\n");
		            
		          }

		          @Override
		          public void onFailure(Throwable caught) {
		            Window.alert("Error:\n" + caught.getMessage());
		          }
		        });
		}
		else
		{
			
			Auth.get().clearAllTokens();
			 Window.alert("Successfully cleared all tokens and signed out");
			loginButton.setText("Sign in");
			loginFlipFlop = 0;
		}
		
//		LoginServiceAsync loginService = GWT.create(LoginService.class);
//	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
//	      public void onFailure(Throwable error) {
//	    	  Window.alert("Error loading loginService!");
//	    	  handleError(error);
//	      }
//
//	      public void onSuccess(LoginInfo result) {
//	        loginInfo = result;
//	        if(loginInfo.isLoggedIn()) 
//	        {
//	        	// if logged in, set text to sign out
//	        	
//	        	 // Set up sign out hyperlink.
//	        	rootPanel.clear();
//	        	signOutLink.setHref(loginInfo.getLogoutUrl());
//	        	loginPanel.add(logoutLabel);
//		     	loginPanel.add(signOutLink);
//		     	RootPanel.get().add(loginPanel);
//	        } 
//	        else 
//	        {
//	        	// otherwise, continue login procedure
//	        	rootPanel.clear();
//	        	signInLink.setHref(loginInfo.getLoginUrl());
//	     	    loginPanel.add(loginLabel);
//	     	    loginPanel.add(signInLink);
//	     	    RootPanel.get().add(loginPanel);
//	        }
//	 
//	      }
//	      
//	    });
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
//		LoginServiceAsync loginService = GWT.create(LoginService.class);
//	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
//	      public void onFailure(Throwable error) {
//	    	  Window.alert("Error loading loginService!");
//	    	  handleError(error);
//	      }
//
//	      public void onSuccess(LoginInfo result) {
//	        loginInfo = result;
//	        if(loginInfo.isLoggedIn()) 
//	        {
//	        	// if logged in, set text to sign out
//	        	loginButton.setText("Sign Out");
//	        } 
//	        else 
//	        {
//	        	loginButton.setText("Sign In");
//	        }
//	      }
//	    });
	    
		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				LoginServiceAsync loginService = GWT.create(LoginService.class);
//			    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
//			      public void onFailure(Throwable error) {
//			    	  Window.alert("Error loading loginService!");
//			    	  handleError(error);
//			      }
//
//			      public void onSuccess(LoginInfo result) {
//			        loginInfo = result;
//			        if(loginInfo.isLoggedIn()) 
//			        {
//			        	// if logged in, set text to sign out
//			        	loginButton.setText("Sign Out");			        	
//			        } 
//			        else 
//			        {
//			        	loginButton.setText("Sign In");
//			        }
//			      }
//			    });
				plus.initialize(new SimpleEventBus(), new GoogleApiRequestTransport(APPLICATION_NAME, API_KEY));
				startLoginProcess(plus, loginButton);

			}
		});
		loginButton.setText("Login");
		titleViewPanel.add(loginButton, 500, 5);
		
		
		Button adminButton = new Button("adminButton");
		adminButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				w++;
				addtolist();
				if (w == 3)
				{
					//addRacker("TESTer", LatLng.newInstance(49.123,-123.0023), 1, 1, 1, 1, 1);
				}
				if (w == 4)
				{
					parseRack ();
					w = 0;
				}
//				LatLng testOUTPUT = LatLng.newInstance(49.284176,-123.106037);
//				String tout = testOUTPUT.toString();
//				
//				String[] results = tout.split( "," );
//				String Lat = results[0];
//				String Lon = results[1];
//				System.out.println("D1 Lat: "+Lat+" Lon: "+Lon);
//				Lat = Lat.substring(1);
//				Lon = Lon.substring(0, Lon.length() - 1);
//				System.out.println("D2 Lat: "+Lat+" Lon: "+Lon);
//				double LatVal = Double.parseDouble(Lat);
//				double LonVal = Double.parseDouble(Lon);
//				System.out.println("D3 Lat: "+LatVal+" Lon: "+LonVal);
//				LatLng pos = LatLng.newInstance(LatVal, LonVal);
				
			    Window.alert("DEBUG Done");
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
	
	// ===================== SERVER ASYNC CALLS  ==========================
	/**
	 * Call rackOps (Admin only): 
	 * type == 0: REMOVE OPERATION. require LatLng
	 * type == 1: ADD OPERATION. require all parameters
	 */
	private void rackOps(String a, LatLng p, int rn, int s, int cs, int r, int type)
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
				int intcs = Integer.parseInt(cs);
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
		listofracks = new ArrayList<BikeRack>();
		listofcrimes = new ArrayList<Crime>();
		parseRack();
		parseCrime();
	}
	// ===================== SERVER ASYNC CALLS ENDS ==========================
	public static ArrayList<Crime> getCrimeData()
	{
		return listofcrimes;
	}
	public static ArrayList<BikeRack> getRackData()
	{
		return listofracks;
	}

}

