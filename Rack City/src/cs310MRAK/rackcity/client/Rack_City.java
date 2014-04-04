package cs310MRAK.rackcity.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.core.client.Callback;
//======================== ^ G+ ============================
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.JsArray;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;

import cs310MRAK.rackcity.shared.UserInfo;
import cs310MRAK.rackcity.shared.UserSearchHistoryInstance;
import cs310MRAK.rackcity.shared.rackStarRatings;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Rack_City implements EntryPoint {
	private RootPanel rootPanel;
	private MapWidget googleMap = null;
	private DockPanel dockPanel = null;
	private Marker currentMarker = null;
	private String currentDatasheetItem = null;
	private Filter filters = null;
	private List<BikeRack> currentRackList = null;
	private List<BikeRack> savedRackList = null;
	private List<Crime> currentCrimeList = null;
	private List<Crime> savedCrimeList = null;
	private LatLng currentAddress = null;
	private static ArrayList<BikeRack> listofracks = null;
	private static ArrayList<Crime> listofcrimes = null;
	boolean filter = false;
	boolean isCrimeShown = true;
	boolean isRackShown = true;
	//private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	//	private static final Logger LOG = Logger.getLogger(Rack_City.class.getName());
	// google+ login stuff ------ S2 ---------
	private static final String CLIENT_ID = "146858113551-ktl431gm3sbkrvid1khqrlvh1afclct4.apps.googleusercontent.com";
	@SuppressWarnings("unused")
	private static final String API_KEY = "AIzaSyCeb8Iws1UqI8caz2aHJee_JtTTiNdqqAY";
	@SuppressWarnings("unused")
	private static final String APPLICATION_NAME = "cs310rackcity";
	private int loginFlipFlop = 0;
	private int loginAttempt = 0;
	private static final String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	private static final String PLUS_ME_SCOPE = "https://www.googleapis.com/auth/plus.me";
	private static final String FRIEND_SCOPE = "https://www.googleapis.com/auth/plus.login";
	private static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/plus.profile.emails.read";
	private static final Auth AUTH = Auth.get();
	public int refreshCount = 0;
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
	private ArrayList< ArrayList<String[]>> favRacksCommon = new ArrayList< ArrayList<String[]>>();
	private String currentRackPos = "";
	private String currentRackTimeHits = "";
	private String favBike = "";
	private String bikeName = "";
	private String bikeColor = "";
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
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
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
			ftpService.adminConnection("https://dl.dropboxusercontent.com/u/280882377/Book1.xml",callback);

			ftpService.adminConnection("https://dl.dropboxusercontent.com/u/280882377/crime_2011.xml",callback);


			if (initialsync == 0)
			{	
				addtolist();
				initialsync = 1;
			}
	}

	/**
	 * Generates the user interface when the program is started
	 */
	@SuppressWarnings("deprecation")
	private void GUIsetup(){
		DOM.removeChild(RootPanel.getBodyElement(), DOM.getElementById("loading"));
		rootPanel = RootPanel.get();
		rootPanel.setSize("1350px", "1000px");
		dockPanel = new DockPanel();
		rootPanel.add(dockPanel, 10, 50);
		dockPanel.setSize("1250px", "500px");

		createAppTitle();
		createUserInputPanel();
		createRackView();
		createTitlePanel();
		createRackClickPanel();
	}

	private void createAppTitle(){
		AbsolutePanel appTitlePanel = new AbsolutePanel();
		appTitlePanel.setSize("1300px", "50px");

		Label appTitleLbl = new Label("Rack City");
		appTitleLbl.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		appTitleLbl.setSize("191px", "18px");
		appTitleLbl.setStyleName("gwt-Title-Label-Style");
		appTitlePanel.add(appTitleLbl, 500, 0);
		
		rootPanel.add(appTitlePanel,0,0);

	}
	
	private void onSearchViewClick(){
		
		
		if(currentDatasheetItem != null){
			((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
			currentDatasheetItem = null;
		}
		
		if(currentMarker != null){
			((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
			currentMarker = null;
		}
		
		((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).remove(0);
		createSearchDataGrid();
		
		hideHideLocationButtons();
	}
	
	private void createSearchDataGrid(){
		DataGrid<UserSearchHistoryInstance> searchDataGrid = new DataGrid<UserSearchHistoryInstance>();
		searchDataGrid.setPageSize(100);

		//need click handler
		searchDataGrid.setSize("700px", "500px");
		
		TextColumn<UserSearchHistoryInstance> searchCol = new TextColumn<UserSearchHistoryInstance>() {
			@Override
			public String getValue(UserSearchHistoryInstance hist) {
				return hist.getSearchAddress();
			}
		};
		searchDataGrid.addColumn(searchCol, "Search Address");
		searchDataGrid.setColumnWidth(searchCol, 25, Unit.PCT);
		
		TextColumn<UserSearchHistoryInstance> radiusCol = new TextColumn<UserSearchHistoryInstance>() {
			@Override
			public String getValue(UserSearchHistoryInstance hist) {
				
				if(hist.getRadius() == 0){
					return Double.toString(0.5);
				}
				
				return Integer.toString(hist.getRadius());
			}
		};
		searchDataGrid.addColumn(radiusCol, "Search Radius");
		searchDataGrid.setColumnWidth(radiusCol, 25, Unit.PCT);
		
		
		TextColumn<UserSearchHistoryInstance> crimeCol = new TextColumn<UserSearchHistoryInstance>() {
			@Override
			public String getValue(UserSearchHistoryInstance hist) {
				return Integer.toString(hist.getCrimeScore());
			}
		};
		searchDataGrid.addColumn(crimeCol, "Search Crime");
		searchDataGrid.setColumnWidth(crimeCol, 25, Unit.PCT);
		
		TextColumn<UserSearchHistoryInstance> ratingCol = new TextColumn<UserSearchHistoryInstance>() {
			@Override
			public String getValue(UserSearchHistoryInstance hist) {
				return Integer.toString(hist.getRating());
			}
		};
		searchDataGrid.addColumn(ratingCol, "Search Rating");
		searchDataGrid.setColumnWidth(ratingCol, 25, Unit.PCT);


		ListDataProvider<UserSearchHistoryInstance> dataProvider = new ListDataProvider<UserSearchHistoryInstance>();
		dataProvider.addDataDisplay(searchDataGrid);

		List<UserSearchHistoryInstance> tmpSearchlist = dataProvider.getList();
		for (UserSearchHistoryInstance hist : userHistory) {
			tmpSearchlist.add(hist);
		}

		final NoSelectionModel<UserSearchHistoryInstance> selectionModel = new NoSelectionModel<UserSearchHistoryInstance>();
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	                @Override
	                public void onSelectionChange(SelectionChangeEvent event) {
	                	
	                	AbsolutePanel userPanel = ((AbsolutePanel) ((HorizontalPanel) dockPanel.getWidget(0)).getWidget(0));
	                	
	                	if(selectionModel.getLastSelectedObject().getRadius() == 0){
	                		((ListBox) userPanel.getWidget(3)).setItemSelected(1, true);
	                	}else
	                		((ListBox) userPanel.getWidget(3)).setItemSelected(selectionModel.getLastSelectedObject().getRadius() + 1, true);
	                	
	                	((SuggestBox) userPanel.getWidget(0)).setText(selectionModel.getLastSelectedObject().getSearchAddress());
	                	((ListBox) userPanel.getWidget(6)).setItemSelected(selectionModel.getLastSelectedObject().getCrimeScore() + 1, true);
	                	((ListBox) userPanel.getWidget(8)).setItemSelected(selectionModel.getLastSelectedObject().getRating() + 1, true);
	                	
	                	searchButtonClick(((SuggestBox) userPanel.getWidget(0)), ((ListBox) userPanel.getWidget(3)),
	                			((ListBox) userPanel.getWidget(6)), ((ListBox) userPanel.getWidget(8)), userPanel);
	                	
	                	//selectionModel.getLastSelectedObject().getCrimeScore()
	                }
	            });
	    searchDataGrid.setSelectionModel(selectionModel);
		
		((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).add(searchDataGrid);
	}
	
	private void onProfileViewClick(){
		
		if(userId.equals("")){
			Window.alert("You are not logged in. Please login.");
			return;
		}
		
		hideHideLocationButtons();
		
		if(currentDatasheetItem != null){
			((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
			currentDatasheetItem = null;
		}
		
		if(currentMarker != null){
			((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
			currentMarker = null;
		}
		
		((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).remove(0);
		
		AbsolutePanel userProfilePanel = new AbsolutePanel();
		userProfilePanel.setSize("700px", "500px");
		
		Label lblProfile = new Label("Profile Page");
		userProfilePanel.add(lblProfile, 10, 10);
		
		
		Image profileImage = new Image();
		profileImage.setSize("30px", "30px");
		profileImage.setUrl(userImageURL);
		userProfilePanel.add(profileImage, 30, 40);

		Label lblProfileImg = new Label("Image URL:");
		userProfilePanel.add(lblProfileImg, 200, 20);
		
		final TextBox profileImageTextBox = new TextBox();
		profileImageTextBox.setText(userImageURL);
		userProfilePanel.add(profileImageTextBox, 200, 45);
		
		
		Label lblFavBike = new Label("Your Favorite Bike:");
		userProfilePanel.add(lblFavBike, 200, 90);
		
		final TextBox favoriteBikeTextBox = new TextBox();
		favoriteBikeTextBox.setText(favBike);
		userProfilePanel.add(favoriteBikeTextBox, 200, 115);
		
		
		Label lblBikeName = new Label("Bike Name:");
		userProfilePanel.add(lblBikeName, 200, 155);
		
		final TextBox bikeNameTextBox = new TextBox();
		bikeNameTextBox.setText(bikeName);
		userProfilePanel.add(bikeNameTextBox, 200, 180);
		
		
		Label lblBikeColour = new Label("Bike Colour:");
		userProfilePanel.add(lblBikeColour, 200, 225);
		
		final TextBox bikeColourTextBox = new TextBox();
		bikeColourTextBox.setText(bikeColor);
		userProfilePanel.add(bikeColourTextBox, 200, 250);
		
		Button submitChanges = new Button("submitChanges");
		submitChanges.addClickHandler( new ClickHandler(){ 
            public void onClick( ClickEvent event ) { 
            	favBike = favoriteBikeTextBox.getText();
            	bikeName = bikeNameTextBox.getText();
            	bikeColor = bikeColourTextBox.getText();
            	userImageURL = profileImageTextBox.getText();
            	
            	AddUserInfo(userId, userName, userEmail, userGender, userIsPlus, profileImageTextBox.getText(), favoriteBikeTextBox.getText(), 
            			bikeNameTextBox.getText(), bikeColourTextBox.getText());
            	
            	Window.alert("Changes Made!");
            	onProfileViewClick();
            }
        });
		submitChanges.setText("Submit Changes");
		userProfilePanel.add(submitChanges, 230, 320);
		
		
		((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).add(userProfilePanel);
	}
	
	private void onMapViewClick(){
		
		showHideLocationButtons();
		
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
	
	
	private void onDatasheetViewClick(){
		if(currentAddress == null){
			Window.alert("No Address searched! Please search an address first before entering DataSheet view.");
			return;
		}

		if(currentMarker != null){
			((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
			currentMarker = null;
		}

		if(currentRackList != null && !currentRackList.isEmpty()){

			googleMap.setVisible(false);
			((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).remove(0);

			createDataGrid();
		}else{
			Window.alert("No Bike Racks to display in Datasheet View! Please search again.");
		}
		
		hideHideLocationButtons();
	}
	
	private void onFavoritesViewClick(){
		
		if(userId.equals("")){
			Window.alert("You are not logged in. Please login.");
			return;
		}
		
		if(currentMarker != null){
			((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
			currentMarker = null;
		}

		if(!favRacks.isEmpty()){

			((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).remove(0);

			createFavoritesGrid();
		}else{
			Window.alert("You have no favorites!");
			return;
		}
		
		hideHideLocationButtons();
	}
	
	private void hideHideLocationButtons(){
		
		AbsolutePanel userPanel = ((AbsolutePanel) ((HorizontalPanel) dockPanel.getWidget(0)).getWidget(0));
		
		for(int i = 0; i < userPanel.getWidgetCount(); i++){
			Widget tmpW = userPanel.getWidget(i);
			
			if(tmpW.toString().contains("Crime Locations") || tmpW.toString().contains("Rack Locations")){
				tmpW.setVisible(false);
			}
		}
	}
	
	private void showHideLocationButtons(){
		
		AbsolutePanel userPanel = ((AbsolutePanel) ((HorizontalPanel) dockPanel.getWidget(0)).getWidget(0));
		
		for(int i = 0; i < userPanel.getWidgetCount(); i++){
			Widget tmpW = userPanel.getWidget(i);
			
			if(tmpW.toString().contains("Crime Locations") || tmpW.toString().contains("Rack Locations")){
				tmpW.setVisible(true);
			}
		}
	}
	
	private void createFavoritesGrid(){
		
		AbsolutePanel gridPanel = new AbsolutePanel();
		gridPanel.setSize("700px","500px");
		
		DataGrid<BikeRack> favoritesDataGrid = new DataGrid<BikeRack>();
		favoritesDataGrid.setPageSize(100);

		//need click handler
		favoritesDataGrid.setSize("700px", "400px");
		
		TextColumn<BikeRack> ratingCol = new TextColumn<BikeRack>() {
			@Override
			public String getValue(BikeRack rack) {
				return rack.getRating() + "/5";
			}
		};
		favoritesDataGrid.addColumn(ratingCol, "Rating");
		favoritesDataGrid.setColumnWidth(ratingCol, 25, Unit.PCT);
		
		
		TextColumn<BikeRack> friendRatingCol = new TextColumn<BikeRack>() {
			@Override
			public String getValue(BikeRack rack) {
				int rackCount = 0;
				for (BikeRack rk : favRacks) {
					if(rk.equals(rack)){
						break;
					}
					rackCount++;
				}
				
				return Double.toString(round(getFRIENDAverageRating (rackCount),2)) + "/5";
			}
		};
		favoritesDataGrid.addColumn(friendRatingCol, "Friends Rating");
		favoritesDataGrid.setColumnWidth(friendRatingCol, 25, Unit.PCT);
		
		
		TextColumn<BikeRack> addrCol = new TextColumn<BikeRack>() {
			@Override
			public String getValue(BikeRack rack) {
				return rack.getAddress();
			}
		};
		favoritesDataGrid.addColumn(addrCol, "Rack Address");
		favoritesDataGrid.setColumnWidth(addrCol, 25, Unit.PCT);
		
		
		TextColumn<BikeRack> friendsCol = new TextColumn<BikeRack>() {
			@Override
			public String getValue(BikeRack rack) {
				
				int rackCount = 0;
				for (BikeRack rk : favRacks) {
					if(rk.equals(rack)){
						break;
					}
					rackCount++;
				}
				
				ArrayList<String[]> temp;
				String listOfFriends = "";
				
				if(!(temp = favRacksCommon.get(rackCount)).isEmpty()){
					for(String[] friends : temp){
						listOfFriends = listOfFriends + friends[0] + " ";
					}
				}
				
				return listOfFriends;
			}
		};
		favoritesDataGrid.addColumn(friendsCol, "Friends Favorites");
		favoritesDataGrid.setColumnWidth(friendsCol, 25, Unit.PCT);


		ListDataProvider<BikeRack> dataProvider = new ListDataProvider<BikeRack>();
		dataProvider.addDataDisplay(favoritesDataGrid);

		List<BikeRack> tmpFavlist = dataProvider.getList();
		for (BikeRack rack : favRacks) {
			tmpFavlist.add(rack);
		}
		
		
		final NoSelectionModel<BikeRack> selectionModel = new NoSelectionModel<BikeRack>();
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	                @Override
	                public void onSelectionChange(SelectionChangeEvent event) {
	                	clickRackDisplayPanel(selectionModel.getLastSelectedObject());
	                }
	            });
	    favoritesDataGrid.setSelectionModel(selectionModel);
	    
	    gridPanel.add(favoritesDataGrid, 0, 0);
	    
	    Button showFavOnMap = new Button("showFavOnMap");
	    showFavOnMap.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				for (BikeRack rack : favRacks){
					addMarker(rack.getCoordinate(), 4);
				}
			}
	    });
	    showFavOnMap.setText("Show on Map");
	    gridPanel.add(showFavOnMap, 500, 350);
		
		((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).add(gridPanel,0,0);
	}

	/**
	 * Creates the panel on the left of the screen that holds the user input objects
	 */
	private void createUserInputPanel(){
		HorizontalPanel leftUserInputPanel = new HorizontalPanel();
		leftUserInputPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		dockPanel.add(leftUserInputPanel, DockPanel.WEST);
		dockPanel.setCellVerticalAlignment(leftUserInputPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		leftUserInputPanel.setSize("200px", "541px");

		final AbsolutePanel userInputPanel = new AbsolutePanel();
		leftUserInputPanel.add(userInputPanel);
		userInputPanel.setSize("200px", "500px");

		createAddressBox(userInputPanel);
		createUserLabelsCombos(userInputPanel);
		createSearchButton(userInputPanel);
	}

	/**
	 * Creates the address text box for user input
	 * @param userInputPanel
	 */
	private void createAddressBox(final AbsolutePanel userInputPanel){
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		
		updateSuggestBox(oracle);

		final SuggestBox suggestBox = new SuggestBox(oracle);
		suggestBox.getValueBox().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(suggestBox.getText().equals("Enter Address Here.")){
					suggestBox.setText("");
				}
				updateSuggestBox((MultiWordSuggestOracle) suggestBox.getSuggestOracle());

			}
		});

		suggestBox.setText("Enter Address Here.");
		userInputPanel.add(suggestBox, 20, 24);
		suggestBox.setSize("145px", "18px");
		suggestBox.setTabIndex(3);
	}

	private void updateSuggestBox(MultiWordSuggestOracle oracle){
		oracle.clear();
		
		if(!userHistory.isEmpty()){
			for(UserSearchHistoryInstance hist : userHistory){
				
				oracle.add(hist.getSearchAddress());
			}
		}
	}

	/**
	 * Generates the datagrid in the rack view panel when DataSheetView button is pressed
	 */
	private void createDataGrid(){
		DataGrid<BikeRack> rackDataGrid = new DataGrid<BikeRack>();
		rackDataGrid.setPageSize(100);

		//need click handler
		rackDataGrid.setSize("700px", "500px");
		
		TextColumn<BikeRack> addressCol = new TextColumn<BikeRack>() {
			@Override
			public String getValue(BikeRack rack) {
				return rack.getAddress();
			}
		};
		rackDataGrid.addColumn(addressCol, "Address");
		rackDataGrid.setColumnWidth(addressCol, 25, Unit.PCT);


		TextColumn<BikeRack> coordinatesCol = new TextColumn<BikeRack>() {
			@Override
			public String getValue(BikeRack rack) {
				return rack.getCoordinate().toString();
			}
		};
		coordinatesCol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		rackDataGrid.addColumn(coordinatesCol, "Coordinates");
		rackDataGrid.setColumnWidth(coordinatesCol, 20, Unit.PCT);


		TextColumn<BikeRack> distanceCol = new TextColumn<BikeRack>() {
			@Override
			public String getValue(BikeRack rack) {
				return Double.toString(round(calcLatLngDistance(rack.getCoordinate()), 2));
			}
		};
		distanceCol.setSortable(true);
		distanceCol.setDefaultSortAscending(false);
		distanceCol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rackDataGrid.addColumn(distanceCol, "Distance From You");
		rackDataGrid.setColumnWidth(distanceCol, 25, Unit.PCT);


		TextColumn<BikeRack> ratingCol = new TextColumn<BikeRack>() {
			@Override
			public String getValue(BikeRack rack) {
				return Double.toString(rack.getRating());
			}
		};
		ratingCol.setSortable(true);
		ratingCol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rackDataGrid.addColumn(ratingCol, "Rating");
		rackDataGrid.setColumnWidth(ratingCol, 12, Unit.PCT);

		TextColumn<BikeRack> crimeScoreCol = new TextColumn<BikeRack>() {
			@Override
			public String getValue(BikeRack rack) {
				return Double.toString(round(rack.getCrimeScore(), 1));
			}
		};
		crimeScoreCol.setSortable(true);
		crimeScoreCol.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		rackDataGrid.addColumn(crimeScoreCol, "Crime Score");
		rackDataGrid.setColumnWidth(crimeScoreCol, 18, Unit.PCT);

		ListDataProvider<BikeRack> dataProvider = new ListDataProvider<BikeRack>();
		dataProvider.addDataDisplay(rackDataGrid);

		List<BikeRack> tmpRacklist = dataProvider.getList();
		for (BikeRack contact : currentRackList) {
			tmpRacklist.add(contact);
		}

		ListHandler<BikeRack> sortHandler = new ListHandler<BikeRack>(tmpRacklist);

		sortHandler.setComparator(distanceCol, new Comparator<BikeRack>() {
			@Override
			public int compare(BikeRack rack1, BikeRack rack2) {
				//implement comparator for distance

				double compare = calcLatLngDistance(rack1.getCoordinate()) - calcLatLngDistance(rack2.getCoordinate());

				if(compare < 0){
					return -1;
				}else if(compare > 0){
					return 1;
				}else{
					return 0;
				}
			}
		});

		sortHandler.setComparator(ratingCol, new Comparator<BikeRack>() {
			@Override
			public int compare(BikeRack o1, BikeRack o2) {
				double rating1 = o1.getRating();
				double rating2 = o2.getRating();
				
				if(rating1 > rating2){
					return 1;
				}else if(rating1 < rating2){
					return -1;
				}
				
				return 0;
			}
		});

		sortHandler.setComparator(crimeScoreCol, new Comparator<BikeRack>() {
			@Override
			public int compare(BikeRack o1, BikeRack o2) {
				double crime1 = o1.getCrimeScore();
				double crime2 = o2.getCrimeScore();
				
				if(crime1 > crime2){
					return 1;
				}else if(crime1 < crime2){
					return -1;
				}
				
				return 0;
			}
		});

		final NoSelectionModel<BikeRack> selectionModel = new NoSelectionModel<BikeRack>();
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	                @Override
	                public void onSelectionChange(SelectionChangeEvent event) {
	                	clickRackDisplayPanel(selectionModel.getLastSelectedObject());
	                }
	            });
	    rackDataGrid.setSelectionModel(selectionModel);
		
		rackDataGrid.addColumnSortHandler(sortHandler);	
		((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).add(rackDataGrid);
	}

	/**
	 * Generates the combo lists and labels for the user input panel
	 * @param userInputPanel
	 */
	private void createUserLabelsCombos(AbsolutePanel userInputPanel){
		Label dsntAddressLbl = new Label("Search Destination Address:");
		dsntAddressLbl.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		userInputPanel.add(dsntAddressLbl, 10, 0);
		dsntAddressLbl.setSize("191px", "18px");

		Label lblFilterResults = new Label("Filter Results:");
		userInputPanel.add(lblFilterResults, 10, 73);
		lblFilterResults.setSize("147px", "18px");

		final ListBox radiusCombo = new ListBox(); //6
		radiusCombo.addItem("");
		radiusCombo.addItem("0.5");
		radiusCombo.addItem("1");
		radiusCombo.addItem("2");
		userInputPanel.add(radiusCombo, 24, 120);
		radiusCombo.setSize("151px", "22px");
		radiusCombo.setTabIndex(4);

		Label lblRadius = new Label("Radius (km):");
		userInputPanel.add(lblRadius, 24, 97);

		Label lblCrimeScore = new Label("Crime Score <=:");
		userInputPanel.add(lblCrimeScore, 24, 149);
		lblCrimeScore.setSize("151px", "18px");

		final ListBox crimeCombo = new ListBox(); //9
		crimeCombo.addItem("");
		crimeCombo.addItem("0");
		crimeCombo.addItem("1");
		crimeCombo.addItem("2");
		crimeCombo.addItem("3");
		crimeCombo.addItem("4");
		crimeCombo.addItem("5");
		userInputPanel.add(crimeCombo, 24, 173);
		crimeCombo.setSize("151px", "22px");
		crimeCombo.setTabIndex(5);

		Label lblRating = new Label("Rating >=:");
		userInputPanel.add(lblRating, 24, 205);
		lblRating.setSize("151px", "18px");

		final ListBox ratingCombo = new ListBox(); //11
		ratingCombo.addItem("");
		ratingCombo.addItem("0");
		ratingCombo.addItem("1");
		ratingCombo.addItem("2");
		ratingCombo.addItem("3");
		ratingCombo.addItem("4");
		ratingCombo.addItem("5");
		userInputPanel.add(ratingCombo, 24, 229);
		ratingCombo.setSize("151px", "22px");
		ratingCombo.setTabIndex(5);
	}

	/**
	 * Generates the search button for the user input panel and associated click handlers
	 * @param userInputPanel
	 */
	private void createSearchButton(final AbsolutePanel userInputPanel){
		final SuggestBox txtbxAddress = (SuggestBox) userInputPanel.getWidget(0);
		final ListBox radiusCombo = (ListBox) userInputPanel.getWidget(3);
		final ListBox crimeCombo = (ListBox) userInputPanel.getWidget(6);
		final ListBox ratingCombo = (ListBox) userInputPanel.getWidget(8);

		Button searchButton = new Button("searchButton");
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(!txtbxAddress.getText().equals("") && !txtbxAddress.getText().equals("Enter Address Here.")){
					if(!radiusCombo.getValue(radiusCombo.getSelectedIndex()).equals("")){
						if(!crimeCombo.getValue(crimeCombo.getSelectedIndex()).equals("")){
							if(!ratingCombo.getValue(ratingCombo.getSelectedIndex()).equals("")){

								searchButtonClick(txtbxAddress, radiusCombo, crimeCombo, ratingCombo, userInputPanel);

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
		userInputPanel.add(searchButton, 118, 276);
	}
	
	private void searchButtonClick(SuggestBox txtbxAddress, ListBox radiusCombo, ListBox crimeCombo, ListBox ratingCombo, final AbsolutePanel userInputPanel){
		

		googleMap.clearOverlays();
		currentRackList = null;
		savedRackList = null;
		currentCrimeList = null;
		savedRackList = null;
		currentAddress = null;

		if(currentMarker != null){
			currentMarker = null;
			((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
		}
		
		onMapViewClick();

		addMapOverlay(txtbxAddress.getText(), 
				Double.parseDouble(radiusCombo.getValue(radiusCombo.getSelectedIndex())),
				Integer.parseInt(crimeCombo.getValue(crimeCombo.getSelectedIndex())), 
				Integer.parseInt(ratingCombo.getValue(ratingCombo.getSelectedIndex())));

		showCrimeButton(userInputPanel);
		showRackButton(userInputPanel);
	}

	/**
	 * Saves search history for specific search (called when search button is pressed)
	 * @param address
	 * @param radiusCombo
	 * @param d
	 * @param e
	 */
	private void saveSearchHistory(String address, int radiusCombo, int cs, int r){
		if(!(userEmail.isEmpty() && userId.isEmpty())){
			// String userID, String searchAddress, String radius, String crimeScore
			String userID = userId;
			String searchAddress = address;
			int radius = radiusCombo;
			if (radius == 1) radius = 0;
			else if (radius == 2) radius = 1;
			else  if (radius == 3) radius = 2;
			int crimeScore = cs - 1;
			int rateVal = r - 1;
			AddUserSearchHistory(userID, searchAddress, radius, crimeScore, rateVal);
		}
	}

	/**
	 * Adds a user search entry when they are logged in and the search button is pressed
	 * @param userID
	 * @param searchAddress
	 * @param radius
	 * @param crimeScore
	 * @param rate
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
				// no messages
			}
				};
				String key = null;
				Random r = null;
				@SuppressWarnings("static-access")
				int secGenA = r.nextInt(9999);
				@SuppressWarnings("static-access")
				int secGenB = r.nextInt(9999);
				@SuppressWarnings("static-access")
				int secGenC = r.nextInt(9999);
				key = String.valueOf(secGenA);
				String inputkey = userID + searchAddress + String.valueOf(radius) + String.valueOf(crimeScore) + String.valueOf(rate);
				inputkey = inputkey.trim().toLowerCase();
				if (inputkey.length() > 20)
					inputkey = inputkey.substring(0, 20);
				key = key + inputkey;
				key = key.substring(0, key.length()/2) + String.valueOf(secGenB) + key.substring(key.length()/2, key.length());
				key = key + String.valueOf(secGenC);
				uService.addUserSearchHistoryInstance(key, userID, searchAddress, radius, crimeScore, rate, callback);
	}

	/**
	 * Displays show crime button when search is pressed
	 * @param userInputPanel
	 */
	private void showCrimeButton(final AbsolutePanel userInputPanel){
		Button showCrimeButton = new Button("showCrimeButton");
		showCrimeButton.setText("Hide Crime Locations");
		showCrimeButton.setSize("180px", "30px");
		showCrimeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isCrimeShown) {
					isCrimeShown = false;
					
					googleMap.clearOverlays();
					
					final SuggestBox txtbxAddress = (SuggestBox) userInputPanel.getWidget(0);
					final ListBox radiusCombo = (ListBox) userInputPanel.getWidget(3);
					final Button showCrimeButton = (Button) userInputPanel.getWidget(10);
					
					addMapOverlay(txtbxAddress.getText(), 
							Double.parseDouble(radiusCombo.getValue(radiusCombo.getSelectedIndex())), -1, -1);
					
					showCrimeButton.setText("Show Crime Locations");
				}
				else if (!isCrimeShown) {
					googleMap.clearOverlays();
					
					final SuggestBox txtbxAddress = (SuggestBox) userInputPanel.getWidget(0);
					final ListBox radiusCombo = (ListBox) userInputPanel.getWidget(3);
					final ListBox crimeCombo = (ListBox) userInputPanel.getWidget(6);
					final ListBox ratingCombo = (ListBox) userInputPanel.getWidget(8);
					final Button showCrimeButton = (Button) userInputPanel.getWidget(10);
					
					addMapOverlay(txtbxAddress.getText(), 
							Double.parseDouble(radiusCombo.getValue(radiusCombo.getSelectedIndex())),
							Integer.parseInt(crimeCombo.getValue(crimeCombo.getSelectedIndex())), 
							Integer.parseInt(ratingCombo.getValue(ratingCombo.getSelectedIndex())));
					
					showCrimeButton.setText("Hide Crime Locations");
					
					isCrimeShown = true;
				}
			}
		});
		userInputPanel.add(showCrimeButton, 10, 328);
	}
	/**
	 * Displays show rack button when search is pressed
	 * @param userInputPanel
	 */
	private void showRackButton(final AbsolutePanel userInputPanel){
		Button showRackButton = new Button("showRackButton");
		showRackButton.setText("Hide Rack Locations");
		showRackButton.setSize("180px", "30px");
		showRackButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isRackShown) {
					isRackShown = false;
					
					googleMap.clearOverlays();
					
					final SuggestBox txtbxAddress = (SuggestBox) userInputPanel.getWidget(0);
					final ListBox radiusCombo = (ListBox) userInputPanel.getWidget(3);
					final Button showRackButton = (Button) userInputPanel.getWidget(11);
					
					addMapOverlay(txtbxAddress.getText(), 
							Double.parseDouble(radiusCombo.getValue(radiusCombo.getSelectedIndex())), -1, -1);
					
					showRackButton.setText("Show Rack Locations");
				}
				else if (!isRackShown) {
					googleMap.clearOverlays();
					
					final SuggestBox txtbxAddress = (SuggestBox) userInputPanel.getWidget(0);
					final ListBox radiusCombo = (ListBox) userInputPanel.getWidget(3);
					final ListBox crimeCombo = (ListBox) userInputPanel.getWidget(6);
					final ListBox ratingCombo = (ListBox) userInputPanel.getWidget(8);
					final Button showRackButton = (Button) userInputPanel.getWidget(11);
					
					addMapOverlay(txtbxAddress.getText(), 
							Double.parseDouble(radiusCombo.getValue(radiusCombo.getSelectedIndex())),
							Integer.parseInt(crimeCombo.getValue(crimeCombo.getSelectedIndex())), 
							Integer.parseInt(ratingCombo.getValue(ratingCombo.getSelectedIndex())));
					
					showRackButton.setText("Hide Rack Locations");
					
					isRackShown = true;
				}
			}
		});
		userInputPanel.add(showRackButton, 10, 368);
	}

	/**
	 * Creates the center panel for holding the google map and datasheet views
	 */
	private void createRackView(){
		VerticalPanel centerRackViewPanel = new VerticalPanel();
		dockPanel.add(centerRackViewPanel, DockPanel.CENTER);
		dockPanel.setCellVerticalAlignment(centerRackViewPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		centerRackViewPanel.setSize("700px", "500px");

		final AbsolutePanel rackViewPanel = new AbsolutePanel();
		centerRackViewPanel.add(rackViewPanel);
		rackViewPanel.setSize("700px","500px");

		loadGoogleMap(rackViewPanel);
	}

	/**
	 * Creates the title panel at the top of the page
	 */
	private void createTitlePanel(){
		VerticalPanel titlePanel = new VerticalPanel();
		dockPanel.add(titlePanel, DockPanel.NORTH);
		titlePanel.setSize("1000px", "40px");

		final AbsolutePanel titleViewPanel = new AbsolutePanel();
		titlePanel.add(titleViewPanel);
		titleViewPanel.setSize("1000px","40px");
		
		createTitleMenu(titleViewPanel);
		createAdminButton(titleViewPanel);
		createLoginButton(null);
	}
	
	private void createTitleMenu(AbsolutePanel titleViewPanel){
		MenuBar menuBar = new MenuBar();
		menuBar.addStyleName("gwt-MenuBar-Rackcity");
		menuBar.setSize("600px", "33px");
		
		MenuItem mapView = new MenuItem("Map View", new ScheduledCommand() {
		    @Override
		    public void execute() {
		    	onMapViewClick();
		    }
		});
		mapView.setSize("70px", "20px");
		mapView.addStyleName("gwt-MenuItem");
		menuBar.addItem(mapView);
		
		MenuItem dataSheetView = new MenuItem("DataSheet View", new ScheduledCommand() {
		    @Override
		    public void execute() {
		    	onDatasheetViewClick();
		    }
		});
		dataSheetView.setSize("112px", "20px");
		menuBar.addItem(dataSheetView);
		
		MenuItem favView = new MenuItem("Favorites View", new ScheduledCommand() {
		    @Override
		    public void execute() {
		    	onFavoritesViewClick();
		    }
		});
		favView.setSize("105px", "20px");
		menuBar.addItem(favView);
		
		MenuItem profileView = new MenuItem("View Profile", new ScheduledCommand() {
		    @Override
		    public void execute() {
		    	onProfileViewClick();
		    }
		});
		profileView.setSize("105px", "20px");
		menuBar.addItem(profileView);
		
		
		MenuItem searchView = new MenuItem("View Searches", new ScheduledCommand() {
		    @Override
		    public void execute() {
		    	onSearchViewClick();
		    }
		});
		searchView.setSize("105px", "20px");
		menuBar.addItem(searchView);
		
		titleViewPanel.add(menuBar,0,0);
	}

	/**
	 * Creates the login button in the title panel and destroys the logged in button
	 */
	private void createLoginButton(Button loggedInButton){
		final AbsolutePanel titleViewPanel = (AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(2)).getWidget(0);

		if(loggedInButton != null)
			titleViewPanel.remove(loggedInButton);

		final Button loginButton = new Button("loginButton");

		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				startLoginProcess();
				
				createLoggedInButton(loginButton);

			}
		});
		loginButton.setText("Login");
		titleViewPanel.add(loginButton, 648, 5);
	}

	/**
	 * Creates the logout button in the title panel and destroys the login button
	 * @param loginButton
	 */
	private void createLoggedInButton(Button loginButton){
		final AbsolutePanel titleViewPanel = (AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(2)).getWidget(0);
		titleViewPanel.remove(loginButton);

		final Button loggedInButton = new Button("loggedInButton");

		loggedInButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				startLoginProcess();
				createLoginButton(loggedInButton);
			}
		});
		loggedInButton.setText("Logout");
		titleViewPanel.add(loggedInButton, 640, 5);
	}

	/**
	 * Creates a text field with the username when logged in
	 */
	private void createUserLabel(){
		final AbsolutePanel titleViewPanel = (AbsolutePanel) rootPanel.getWidget(1);

		Label userLabel = new Label(userName + ", you are currently logged in.");
		userLabel.setSize("350px", "10px");

		titleViewPanel.add(userLabel, 10, 15);
	}

	/**
	 * Destroys the text field with the username in it (called when user logs out)
	 */
	private void removeUserLabel(){
		final AbsolutePanel titleViewPanel = (AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(2)).getWidget(0);

		for(int i = 0; i < titleViewPanel.getWidgetCount(); i++){
			if(titleViewPanel.getWidget(i).toString().contains("gwt-Label")){
				titleViewPanel.remove(i);
				break;
			}
		}
	}

	/**
	 * Creates Admin button in the title panel
	 * @param titleViewPanel
	 */
	private void createAdminButton(AbsolutePanel titleViewPanel){
		Button adminButton = new Button("adminButton");
		adminButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				refreshCount++;
				if (refreshCount == 3)
				{
					messenger("You are 1 click away from requesting for a refectch!");
				}
				if (refreshCount == 4)
				{
					if (userEmail.equals("robwu15@gmail.com") || userEmail.equals("obedientworker@gmail.com") || userEmail.equals("kevin.david.greer@gmail.com") || userEmail.equals("abhisek.pradhan91@gmail.com"))
					{
						messenger("Refetch Request Approved");

						// here we hardcode
						messenger("Refetch Currently disabled due to busy traffic");
						printerdebug();		


						// =================================== ***** ======================
						// ------------------ RACKEXPERIENCE ---------------
						// we'll use this when we get the textbox to add rack experiences // 
//						if (rService == null) 
//						{
//							rService = GWT.create(rackService.class);
//						}
//						AsyncCallback<Void> callback = new AsyncCallback<Void>()
//								{
//							public void onFailure(Throwable error)
//							{
//								Window.alert("Server Error! (RackExperience)");
//								handleError(error);
//							}
//							@Override
//							public void onSuccess(Void result) {
//									Window.alert("Server Success! You added a rack experience.");
//							}
//								};
//								rService.addBikeExperienceComment(LatLng.newInstance(49.284192, -123.110048).toString(),"This is the best bike rack ever!", "105344220509568726102", callback);
//								rService.addBikeExperienceComment(LatLng.newInstance(49.284176, -123.106037).toString(),"I do not like this bike rack!", "116016420567291713038", callback);
//								rService.addBikeExperienceComment(LatLng.newInstance(49.284192, -123.110048).toString(),"So I just had some delicious blueberry yogurt this morning.", "105344220509568726102", callback);
//								rService.addBikeExperienceComment(LatLng.newInstance(49.284192, -123.110048).toString(),"Sup dude.", "102502510324439815963", callback);	
						// ------------------------------------------------------------------------
						// =================================== ***** ======================
						
						getUserSearchHistory(userId);
						//addBikeRackTimeHit("49.284176, -123.106037");
						//addtolist();

					}
					else
						// dude, amazing. 
						messenger("Refetch Request Denied. You are not a registered admin");
					refreshCount = 0;
				}
			}
		});
		adminButton.setText("Admin");
		titleViewPanel.add(adminButton, 705, 5);
	}

	/**
	 * Creates the holder for the display when a rack is clicked
	 */
	private void createRackClickPanel(){
		HorizontalPanel rightRackClickPanel = new HorizontalPanel();
		dockPanel.add(rightRackClickPanel, DockPanel.EAST);
		dockPanel.setCellHorizontalAlignment(rightRackClickPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		dockPanel.setCellVerticalAlignment(rightRackClickPanel, HasVerticalAlignment.ALIGN_TOP);
		rightRackClickPanel.setSize("340px", "500px");
	}

	/**
	 * Creates a googleMap object and adds it to the centerPanel
	 */
	private void loadGoogleMap(final AbsolutePanel rackViewPanel){

		Maps.loadMapsApi("", "2", false, new Runnable() { public void run() {
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
							}
						}
					}else{
						if(currentMarker != null){
							currentMarker = null;
							((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
						}
					}
				}
			});

			googleMap.setSize("100%", "100%");
			googleMap.addControl(new LargeMapControl());
			final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
			dock.addNorth(googleMap, 500);
			rackViewPanel.add(dock);
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

		//Geocodes the address that the user inputs and creates a latlong object
		Geocoder latLongAddress = new Geocoder();
		latLongAddress.getLatLng(address, new LatLngCallback() {
			@Override
			public void onFailure() {
				Window.alert("Sorry, we were unable to find that address");
			}

			@Override
			public void onSuccess(LatLng point) {
				
				int radiusCombo = 0;
				if (radius == 0.5) radiusCombo = 0;
				if (radius == 1) radiusCombo = 1;
				if (radius == 2) radiusCombo = 2;
				userHistory.add(0, new UserSearchHistoryInstance("0", userId, address, radiusCombo, (int)crimeScore, (int) rating));
				
				currentAddress = point;
				googleMap.setCenter(currentAddress);
				googleMap.setZoomLevel(14);
				displayRadius(currentAddress, radius);
				
				
				saveSearchHistory(address, radiusCombo, (int)(crimeScore + 1), (int)(rating + 1));
				
				addMarker(currentAddress, 1);

				/*
				 * The following code should get the appropriate list based on all the preconditions.
				 */
				if(filters == null){
					filters = new Filter();
				}
				
				savedCrimeList = filters.getFilteredCrimeList(currentAddress, radius);
				savedRackList = filters.getFilteredRackList(currentAddress, radius, rating, crimeScore);
				
				if(currentRackList == null || currentRackList.isEmpty()){
					currentRackList = filters.getFilteredRackList(currentAddress, radius, rating, crimeScore);
				}
				
				if(currentCrimeList == null || currentCrimeList.isEmpty()){
					currentCrimeList = filters.getFilteredCrimeList(currentAddress, radius);
				}
				
				if(!currentRackList.isEmpty() && isRackShown){
					for (BikeRack rack : currentRackList) {
						//System.out.println("Rack Coordinate: " + rack.getCoordinate());
						addMarker(rack.getCoordinate(), 2);
					}
				}

				if(!currentCrimeList.isEmpty() && isCrimeShown){
					for (Crime crime : currentCrimeList) {
						//System.out.println("Crime Coordinate: " + crime.getCoordinate());
						addMarker(crime.getCoordinate(), 3);
					}
				}
			}
		});
	}

	/**
	 * Method called when a rack on a map is clicked.
	 * Displays the 'Report Crime' button and title of rack in a panel on the right of the page
	 */
	private void clickRackDisplayPanel(final BikeRack rack){


		if(rack == null){
			return;
		}
		
		/**
		 * going to have to put the server calls for additional bike rack objects information
		 * here, because we are only going to make server calls now for the objects that are 
		 * clicked on.
		 * 
		 */
		if (rService == null) 
		{
			rService = GWT.create(rackService.class);
		}
		AsyncCallback<String> callback = new AsyncCallback<String>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (getBikeRackTimeHits)");
				handleError(error);
			}
			@Override
			public void onSuccess(String result) {
				Window.alert("Server Success! (getBikeRackTimeHits): " + result);
				currentRackTimeHits = result;

			}
				};

				LatLng pos2 = rack.getCoordinate();
				
				rService.getRackTimeHits(pos2.toString(), callback);

		final AbsolutePanel rackClickPanel = new AbsolutePanel();
		((HorizontalPanel) dockPanel.getWidget(3)).clear();
		((HorizontalPanel) dockPanel.getWidget(3)).add(rackClickPanel);
		rackClickPanel.setSize("340px", "500px");

		final Button reportCrimeButton = new Button("reportCrimeButton");
		reportCrimeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//=====================================================================
				// Check login status using login service.
				if(!userEmail.isEmpty())  
				{
					LatLng incident = currentMarker.getLatLng();
					int numOfStolen = 0;
					//messenger("INCIDENT: "+incident.toString());
					// Adds to numberStolenBike of the rack in question
					
					int i = 0;
					BikeRack currentRack = null;
					while (i < currentRackList.size())
					{
						currentRack = currentRackList.get(i);
						//messenger("INCIDENT ? CURRENT "+currentRack.getCoordinate().toString());
						if (currentRack.getCoordinate().toString().equals(incident.toString())) 
						{
							//messenger("INCIDENT == CURRENT "+currentRack.getCoordinate().toString());
							currentRack.addStolenBike();
							//messenger ("new crimescore: "+currentRack.getCrimeScore());
							numOfStolen = currentRack.getNumberStolenBikes();
							clickRackDisplayPanel(currentRack);
							break;
						}
						i++;
					}
					// =============== UPDATE STOLEN BIKE ON DATASTORE ==================
					String newp = incident.toString();
					if (rService == null) 
					{
						rService = GWT.create(rackService.class);
					}
					reportCrimeButton.setEnabled(false);
					AsyncCallback<Void> callback = new AsyncCallback<Void>()
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
						if (currentRack != null)
							rackOps(currentRack.getAddress(), currentRack.getCoordinate(), currentRack.getRackCount(), currentRack.getNumberStolenBikes(),	currentRack.getCrimeScore(), currentRack.getRating(), 3);
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
		rackClickPanel.add(reportCrimeButton, 240, 465);
		
		
		final Button checkInButton = new Button("checkInButton");
		checkInButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//TODO Insert code that handles check in functionality
				
				if (rService == null) 
				{
					rService = GWT.create(rackService.class);
				}
				AsyncCallback<Void> callback = new AsyncCallback<Void>()
						{
					public void onFailure(Throwable error)
					{
						Window.alert("Server Error! (getBikeRackTimeHits)");
						handleError(error);
					}
					@Override
					public void onSuccess(Void result) {
							Window.alert("Server Success! It was updated.");
					}
						};
				
						// *^*^*^
						LatLng pos2 = rack.getCoordinate();

						String problem = currentRackTimeHits;
						
						String problemWithoutLeftBrack = problem.replace("[", "");
						String problemWithoutRightAndLeftBranch = problemWithoutLeftBrack.replace("]", "");
						String problemAlsoWithoutSpace = problemWithoutRightAndLeftBranch.replace(" ", "");
						
						String[] problemArray = problemAlsoWithoutSpace.split(",");
						
						// there are 24 elements
						// to access the last one, it's [23]
						// can also think of 0to1 as 24to1. That is, 11:59pm -> | 12am.. 12:02 am.. 12:58 am -> 1:00 am
						// And 0%24 = 24%24
						// so either way, if I is 24 or 0 at 12am, it will work
						
						Date date = new Date();
						int hourInt = date.getHours();
						
						String w = problemArray[hourInt % 24];
						int output = Integer.parseInt(w);
						
						int timeHitsInput = output + 1;
					
						String whichOne = "";
						if(hourInt == 0) whichOne = "0to1";
						if(hourInt == 1) whichOne = "1to2";
						if(hourInt == 2) whichOne = "2to3";
						if(hourInt == 3) whichOne = "3to4";
						if(hourInt == 4) whichOne = "4to5";
						if(hourInt == 5) whichOne = "5to6";
						if(hourInt == 6) whichOne = "6to7";
						if(hourInt == 7) whichOne = "7to8";
						if(hourInt == 8) whichOne = "8to9";
						if(hourInt == 9) whichOne = "9to10";
						if(hourInt == 10) whichOne = "10to11";
						if(hourInt == 11) whichOne = "11to12";
						if(hourInt == 12) whichOne = "12to13";
						if(hourInt == 13) whichOne = "13to14";
						if(hourInt == 14) whichOne = "14to15";
						if(hourInt == 15) whichOne = "15to16";
						if(hourInt == 16) whichOne = "16to17";
						if(hourInt == 17) whichOne = "17to18";
						if(hourInt == 18) whichOne = "18to19";
						if(hourInt == 19) whichOne = "19to20";
						if(hourInt == 20) whichOne = "20to21";
						if(hourInt == 21) whichOne = "21to22";
						if(hourInt == 22) whichOne = "22to23";
						if(hourInt == 23) whichOne = "23to24";
						
				//rService.getRackTimeHits("49.284176, -123.106037", callback);
				rService.updateBikeRackTimeHit(pos2.toString(), timeHitsInput, whichOne, callback);
			}
		});
		checkInButton.setText("Check-In");
		rackClickPanel.add(checkInButton, 265, 430);
		
		final Button addFavButton = new Button("addFavButton");
		addFavButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!userId.equals(""))
				{
					Boolean checkalready = false;
					for (int i = 0; i < favRacks.size(); i++)
					{
						if (favRacks.get(i).getAddress().equals(rack.getAddress()))
						{
							checkalready = true;
						}
					}
					if (checkalready == true)
					{
						messenger("You've already favorited this rack!");
					}
					else if (checkalready == false)
					{
						Add2Fav(userId, rack.getAddress(), rack.getCoordinate());
						favRacks.add(rack);
						favRacksCommon.add(new ArrayList<String[]>());
						messenger("Added Your Favorite!");
					}					
				}
				else
				{
					messenger("Please login to use this feature!");
				}
					
			}
		});
		addFavButton.setText("Add to Favorites");
		rackClickPanel.add(addFavButton, 220, 395);
		
		
		final Button removeFavButton = new Button("removeFavButton");
		removeFavButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//TODO implement this functionality here
				removeFav(rack);
				
				int rackCount = 0;
				for(BikeRack rck : favRacks){
					if(rck.equals(rack)){
						return;
					}
					rackCount++;
				}
				
				favRacks.remove(rack);
				favRacksCommon.remove(rackCount);
				
			}
		});
		removeFavButton.setText("Remove from Favorites");
		rackClickPanel.add(removeFavButton, 180, 360);
		
		
		Label rackAddress = new Label("Address: " + rack.getAddress());
		rackAddress.setStyleName("gwt-Rack-Label-Style");
		rackAddress.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		rackClickPanel.add(rackAddress, 0, 0);
		rackAddress.setSize("340px", "54px");

		//Add code to get # of stolen bikes for a particular rack
		Label rackStolenBikesLabel = new Label("# of Stolen Bikes in this Location: " + rack.getNumberStolenBikes());
		rackClickPanel.add(rackStolenBikesLabel, 0, 30);
		rackStolenBikesLabel.setSize("130px", "54px");

		//Add code to get rating for a particular rack
		Label rackRatingLabel = new Label("Bike Rack Rating (out of 5): " + rack.getRating());
		rackClickPanel.add(rackRatingLabel, 170, 30);
		rackRatingLabel.setSize("120px", "54px");

		Label crimeRatingLabel = new Label("Bike Rack Crime Score (out of 5): " + rack.getCrimeScore());
		rackClickPanel.add(crimeRatingLabel, 0, 80);
		crimeRatingLabel.setSize("145px", "54px");

		Label rackCountRatingLabel = new Label("Bike Rack Count: " + rack.getRackCount());
		rackClickPanel.add(rackCountRatingLabel, 170, 80);
		rackCountRatingLabel.setSize("130px", "54px");

		Label distanceLabel = new Label("Distance from you (km): " + round(calcLatLngDistance(rack.getCoordinate()), 2));
		rackClickPanel.add(distanceLabel, 0, 130);
		distanceLabel.setSize("130px", "54px");
		
		Label timeHits = new Label("Rack time hits: " + this.currentRackTimeHits);
		rackClickPanel.add(timeHits, 170, 130);
		timeHits.setSize("150px", "54px");
		// &!&!&!&
		
		
		Label userRatingLbl = new Label("Set Rack Rating:");
		rackClickPanel.add(userRatingLbl, 0, 180);
		
		final ListBox userRatingCombo = new ListBox(); //9
		userRatingCombo.addItem("");
		userRatingCombo.addItem("0");
		userRatingCombo.addItem("1");
		userRatingCombo.addItem("2");
		userRatingCombo.addItem("3");
		userRatingCombo.addItem("4");
		userRatingCombo.addItem("5");
		
		userRatingCombo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
				if(userRatingCombo.getSelectedIndex() > 0){
					//TODO implement set rating functionality
					
					
					userRatingCombo.setItemSelected(0, true);//call this when you are done
				}
			}
		});
		
		rackClickPanel.add(userRatingCombo, 0, 200);
		userRatingCombo.setSize("100px", "22px");
		
		
		Label shareExperienceLbl = new Label("Share your experience:");
		rackClickPanel.add(shareExperienceLbl, 170, 180);
		
		Button submitShareExperience = new Button("submitShareExperience");
		submitShareExperience.addClickHandler(new ClickHandler() {
	         public void onClick(ClickEvent event) {
	         	
	         	
	        	 
	             final PopupPanel popup = new PopupPanel(true);
	             popup.setPopupPositionAndShow(new PopupPanel.PositionCallback(){
	                public void setPosition(int offsetWidth, int offsetHeight) {
	                   int left = (Window.getClientWidth() - offsetWidth) / 3;
	                   int top = (Window.getClientHeight() - offsetHeight) / 3;
	                   popup.setPopupPosition(left, top);
	                }
	             });
	             
	             AbsolutePanel userExpPanel = new AbsolutePanel();
	             userExpPanel.setSize("400px", "300px");
	             
	             Label shareExperienceLbl = new Label("Share your experience:");
	             userExpPanel.add(shareExperienceLbl, 10, 10);
	             
	             final TextArea userExperienceTextBox = new TextArea();
	             userExperienceTextBox.setSize("300px", "200px");
	             userExpPanel.add(userExperienceTextBox, 30, 30);
	             
	             Button submitShareExperience = new Button("submitShareExperience");
	             submitShareExperience.addClickHandler(new ClickHandler() {
	     	         public void onClick(ClickEvent event) {
	     	        	//TODO Implement new user experience
	     	        // =================================== ***** ======================
						// ------------------ RACKEXPERIENCE ---------------
	     	        	LatLng positionOfExperience = rack.getCoordinate();
	     	        	String positionOfExperienceString = positionOfExperience.toString();
	     	        	//gets text from the current popup
	     	        
						if (rService == null) 
						{
							rService = GWT.create(rackService.class);
						}
						AsyncCallback<Void> callback = new AsyncCallback<Void>()
								{
							public void onFailure(Throwable error)
							{
								Window.alert("Server Error! (RackExperience)");
								handleError(error);
							}
							@Override
							public void onSuccess(Void result) {
									Window.alert("Server Success! You added a rack experience.");
							}
								};
								rService.addBikeExperienceComment(positionOfExperienceString,userExperienceTextBox.getText(), userId, callback);
							
						// ------------------------------------------------------------------------
						// =================================== ***** ======================
	     	        	
						Window.alert("Experience Submitted!");
	     	        	popup.removeFromParent();
	     	        }
	             });
	             submitShareExperience.setText("Submit Experience");
	             userExpPanel.add(submitShareExperience, 250, 260);
	             
	             popup.setWidget(userExpPanel);
	             
	          }
	       });
		submitShareExperience.setText("Submit Experience");
		submitShareExperience.setSize("140px", "30px");
		rackClickPanel.add(submitShareExperience, 170, 200);
		
		
		
		Label userExperienceLbl = new Label("View User Experiences:");
		rackClickPanel.add(userExperienceLbl, 0, 240);
		
		Button viewUserExperienceButton = new Button("viewUserExperienceButton");
		viewUserExperienceButton.addClickHandler(new ClickHandler() {
	         public void onClick(ClickEvent event) {
	         	
	         	
	        	 
	             final PopupPanel popup = new PopupPanel(true);
	             popup.setPopupPositionAndShow(new PopupPanel.PositionCallback(){
	                public void setPosition(int offsetWidth, int offsetHeight) {
	                   int left = (Window.getClientWidth() - offsetWidth) / 3;
	                   int top = (Window.getClientHeight() - offsetHeight) / 3;
	                   popup.setPopupPosition(left, top);
	                }
	             });
	             
	             AbsolutePanel userExpPanel = new AbsolutePanel();
	             userExpPanel.setSize("400px", "300px");
	             
	             Label shareExperienceLbl = new Label("All User Experiences:");
	             userExpPanel.add(shareExperienceLbl, 10, 10);
	             
	             final TextArea userExperienceTextBox = new TextArea();
	             userExperienceTextBox.setText("TODO IMPLEMENT TEXT FROM SERVER");
	             userExperienceTextBox.setReadOnly(true);
	             userExperienceTextBox.setSize("300px", "200px");
	             userExpPanel.add(userExperienceTextBox, 30, 30);
	             
	             Button submitShareExperience = new Button("submitShareExperience");
	             submitShareExperience.addClickHandler(new ClickHandler() {
	     	         public void onClick(ClickEvent event) {
	     	        	popup.removeFromParent();
	     	        }
	             });
	             submitShareExperience.setText("Close");
	             userExpPanel.add(submitShareExperience, 250, 260);
	             
	             
	             popup.setWidget(userExpPanel);
	             
	          }
	       });
		viewUserExperienceButton.setText("View Experiences");
		viewUserExperienceButton.setSize("130px", "30px");
		rackClickPanel.add(viewUserExperienceButton, 0, 260);
		
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
	/**
	 * Add markers onto the map. Add marker overlay for each latlng and type submitted.
	 * google icon file from here: https://sites.google.com/site/gmapicons/
	 * @param pos - location for marker
	 * @param type - 1 = Current Location; 2 = racks; 3 = crimes
	 */
	private void addMarker(LatLng pos, int type)
	{
		if (type == 1)		// search address: ME (blue)
		{
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			Icon icn;

			if (!userImageURL.isEmpty() || userImageURL == null){
				icn = Icon.newInstance(userImageURL);
				icn.setIconAnchor(Point.newInstance(15, 15));
			}
			else{
				icn = Icon.newInstance(Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_blue.png"));
				icn.setIconAnchor(Point.newInstance(6, 20));
			}
			markerOptions.setIcon(icn);

			Marker mark = new Marker(pos, markerOptions);
			googleMap.addOverlay(mark);
		}
		else if (type == 2)		// bike racks: GREEN, !!!!! SHOULD HAVE DIFFERENT COLOR BASED ON RACK#
		{
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			Icon icn = Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_green.png");
			icn.setIconAnchor(Point.newInstance(6, 20));
			markerOptions.setIcon(icn);
			Marker mark = new Marker(pos, markerOptions);
			googleMap.addOverlay(mark);
		}
		else if (type == 3)		// crime place: RED
		{
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			Icon icn = Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_red.png");
			icn.setIconAnchor(Point.newInstance(6, 20));
			markerOptions.setIcon(icn);
			Marker mark = new Marker(pos, markerOptions);
			googleMap.addOverlay(mark);
		}
		else if (type == 4){
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			Icon icn = Icon.newInstance("http://labs.google.com/ridefinder/images/mm_20_blue.png");
			icn.setIconAnchor(Point.newInstance(6, 20));
			markerOptions.setIcon(icn);
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

	private void sortUserHistory(ArrayList<UserSearchHistoryInstance> uhist)
	{
		UserSearchHistoryInstance[] sortedHist = new UserSearchHistoryInstance[uhist.size()];
		ArrayList<String> unsortedindexlist = new ArrayList<String>();
		ArrayList<String> indexlist = new ArrayList<String>();
		if (uhist.size() != 0)
		{
			for (int i = 0; i < uhist.size(); i++)
			{
				String[] index = uhist.get(i).getDate().split(" ");
				
				// =================== convert month to digit ================
				String month = index[1];
				int mon = 1;
				if (month.equals("JAN")) mon = 1;
				if (month.equals("FEB")) mon = 2;
				if (month.equals("MAR")) mon = 3;
				if (month.equals("APR")) mon = 4;
				
				if (month.equals("MAY")) mon = 5;
				if (month.equals("JUN")) mon = 6;
				if (month.equals("JUL")) mon = 7;
				if (month.equals("AUG")) mon = 8;
				
				if (month.equals("SEP")) mon = 9;
				if (month.equals("OCT")) mon = 10;
				if (month.equals("NOV")) mon = 11;
				if (month.equals("DEC")) mon = 12;
				// =================== convert date to digit ================
				int  date = Integer.parseInt(index[2]);
				// =================== convert time to digit ================
				String  time = index[3];
				String[] t = time.split(":");
				int hour = Integer.parseInt(t[0]);
				int minute = Integer.parseInt(t[1]);
				int second = Integer.parseInt(t[2]);
				int timeindex = (hour*100*100)+(minute*100)+second;
				// =================== convert year to digit ================
				int  year = Integer.parseInt(index[5]);
				// ============= FINAL INDEX VALUE FOR SORT =================
				int finindex = (year*100*100*100*100*100)+(mon*100*100*100*100)+(date*100*100*100)+timeindex;
				String inx = String.valueOf(finindex);
				unsortedindexlist.add(inx);
				indexlist.add(inx);
			}
			Collections.sort(indexlist);
			for (int s = 0; s < uhist.size(); s++)
			{
				int x = 0;
				while (x < uhist.size())
				{
					if (unsortedindexlist.get(s).equals(indexlist.get(x)))
					{
						sortedHist[x] = uhist.get(s);
						break;
					}
					x++;
				}
			}
			userHistory = new ArrayList<UserSearchHistoryInstance>();
			for (int where = indexlist.size() - 1; where >= 0; where--)
			{
				userHistory.add(sortedHist[where]);				
			}
		}
	}
	

	// ===================== SERVER ASYNC CALLS  ==========================

	/**
	 *  Called when user add a rack to favorite
	 */ //!!!!!
	private void Add2Fav(String uid, String address, LatLng pos)
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

				if (uid.equals(userId))	 assignFavresult(result);
				else compareFriendFav(name, uid, result);
			}
				});
	}
	
	private void removeFav(BikeRack rack)
	{
		if (!userId.equals(""))
		{
			String newp = rack.getCoordinate().toString();
			if (fService == null) {
				fService = GWT.create(rackService.class);
			}
			AsyncCallback<Void> callback = new AsyncCallback<Void>()
					{
				public void onFailure(Throwable error)
				{
					Window.alert("Server Error! (RMV-FAV)");
					handleError(error);
				}
				public void onSuccess(Void ignore)
				{
					Window.alert("Successfully removed from your favorite list!");
				}
					};
				fService.removeFavorite(userId, newp, rack.getAddress(), callback);
		}
		
	}

	private void assignFavresult(ArrayList<String[]> fav)
	{
		if (!(fav.isEmpty() || fav == null))
		{
			for(int i = 0; i < fav.size(); i++)
			{
				//Window.alert("P"+i);
				String[] temp = fav.get(i);
				String LL = temp[0].toString();   // string		lat and long of position	

				for (int a = 0; a < listofracks.size(); a++)
				{
					if (listofracks.get(a).getCoordinate().toString().equals(LL))
					{

						favRacks.add(listofracks.get(a));
						ArrayList<String[]> tmp = new  ArrayList<String[]>(); 	// 
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
	private void checkUserInfo(final String id, final String name, final String email, final String gender, final Boolean isPlus, final String propic, final String favBike, final String bikeName, final String bikeColor)
	{
		if (uService == null) 
		{
			uService = GWT.create(userService.class);
		}
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (HAS-USER)");
				handleError(error);
			}
			@Override
			public void onSuccess(Boolean result) {
				if (result == false)
				{
					AddUserInfo(id, name, email, gender, isPlus, propic, favBike, bikeName, bikeColor);
					onProfileViewClick();
				}
				else if (result == true)
				{
					parseUserInfo(id);
				}
			}
				};
				uService.hasUser(id, callback);
	}

	/**
	 *call when new user is logged in to G+ or want to change information
	 */
	private void AddUserInfo(String id, String name, String email, String gender, Boolean isPlus, String propic, String favBike, String bikeName, String bikeColor)
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
				// no messages
			}
				};
				uService.addUser(id, name, email, gender, isPlus, propic, favBike, bikeName, bikeColor, callback);
	}
	
	/**
	 *  for the profile page
	 */
	private void parseUserInfo(String id)
	{
		if (uService == null) 
		{
			uService = GWT.create(userService.class);
		}
		
		uService.getUser(id, new AsyncCallback<ArrayList<UserInfo>>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (PAR-USER)");
				handleError(error);
			}

			@Override
			public void onSuccess(ArrayList<UserInfo> result) {
				assignUserInfo(result);		
			}});
	}
	
	private void assignUserInfo(ArrayList<UserInfo> result)
	{
		UserInfo me = result.get(0);
		favBike = me.getFavBike();
		bikeName = me.getbikeName();
		bikeColor = me.getbikeColor();
	}
	
	/**
	 * Will only use for admin button to massively load a bikeracktimehit object for every bike rack initially. Just to get them in the datastore.
	 */
	private void addBikeRackTimeHit(String pos){

		AsyncCallback<Void> callback = new AsyncCallback<Void>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (ADD-TIME)");
				handleError(error);
			}
			public void onSuccess(Void ignore)
			{
				Window.alert("Success (ADD-TIME)");
			}
				};

				rService.addBikeRackTimeHit(pos, callback);
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
					messenger("Server ERROR: GET-HISTORY");
				}

				@Override
				public void onSuccess(
						ArrayList<UserSearchHistoryInstance> result) {
						if (result != null)
						{
							sortUserHistory(result);
						}
				}
					});
		}
	}
	
	/**
	 * Call rackOps (Admin only): 
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
			AsyncCallback<Void> callback = new AsyncCallback<Void>()
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
			AsyncCallback<Void> callback = new AsyncCallback<Void>()
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
			AsyncCallback<Void> callback = new AsyncCallback<Void>()
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
	@SuppressWarnings("unused")
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
				GUIsetup();
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
				Window.alert("Success. (PAR-RACK)");
				assignrackOutput(result);
			}
				});
	}

	/**
	 * Called automagically by parseRack
	 */
	@SuppressWarnings("unused")
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

	private void submituserRating (final String uid, final String addr, final String pos, int rating)
	{
		if (uService == null)
		{
			uService = GWT.create(rackService.class);
		}


		AsyncCallback<Void> ratingCallback = new AsyncCallback<Void>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (ADD-RATE)");
				handleError(error);
			}
			public void onSuccess(Void ignore)
			{
				Window.alert("Success. (ADD-RATE)");
				getALLRatingATpos (uid, addr, pos);
			}
				};
				uService.addStarRating(uid, addr, pos, rating, ratingCallback);
	}

	// type 2: get all user rating on this address
	private void getALLRatingATpos(String uid, final String addr, final String pos)
	{
		if (uService == null)
		{
			uService = GWT.create(userService.class);
		}

		uService.getStarRating(uid, addr, pos, 2, new AsyncCallback<ArrayList<rackStarRatings>>()
				{

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Server Error! (PAR-STAR-2)");
				handleError(caught);
			}

			@Override
			public void onSuccess(ArrayList<rackStarRatings> result) {
				if (result != null)
				{
					calculateNewAvg(addr, pos, result);
				}
			}
				});
	}


	private void getFriendRatings(String fid, String addr, String pos, final String[] person)
	{
		// parse friend rating ASYNC CALL		
		if (uService == null)
		{
			uService = GWT.create(userService.class);
		}

		// type 3: get this user's rating for THIS RACK ONLY
		uService.getStarRating(fid, addr, pos, 3, new AsyncCallback<ArrayList<rackStarRatings>>()
				{
			public void onFailure(Throwable error)
			{
				Window.alert("Server Error! (PAR-STAR-3)");
				handleError(error);
			}

			@Override
			public void onSuccess(ArrayList<rackStarRatings> result) 
			{
				// assign ratings
				int rating = result.get(0).getRating();
				if (rating != 0)
				{
					// add it to the fav common list
					person[2] = String.valueOf(rating);
				}
				else
				{
					// do nothing
				}
			}
				});
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

	private void assignFriendRating(int arrayRACK, String fid)
	{
		// get the position, where to add the friend data
		if (favRacksCommon != null && !favRacksCommon.isEmpty() && !favRacks.isEmpty() && favRacks != null)
		{
			for (int b = 0; b < favRacksCommon.get(arrayRACK).size(); b++)
			{
				String[] person = favRacksCommon.get(arrayRACK).get(b);
				if (person[1].equals(fid))
				{
					BikeRack brtemp =favRacks.get(arrayRACK);
					getFriendRatings(fid, brtemp.getAddress(), brtemp.getCoordinate().toString(), person);
				}
			}
		}
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

	private void compareFriendFav(String name, String fid, ArrayList<String[]> result)
	{
		if (!(result.isEmpty() || result == null))
		{
			for (int i = 0; i < result.size(); i++)
			{
				String[] fav = result.get(i);
				String fLL = fav[0];
				for (int a = 0; a < favRacks.size(); a++)
				{
					//messenger(fLL+" vs Comparing racks?  "+favRacks.get(a).getCoordinate().toString());
					if (fLL.equals(favRacks.get(a).getCoordinate().toString()))
					{
						//						friend's rating
						String[] ftemp = {name, fid, "0"};
						favRacksCommon.get(a).add(ftemp);
						assignFriendRating (a, fid);
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
					@SuppressWarnings("unused")
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
									getUserSearchHistory(userId);
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
									parseFav(userName, userId);
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

	private void startLoginProcess()
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
						saveToken(token);
						// ============== on Success ==============
						String url = "https://www.googleapis.com/plus/v1/people/me?access_token=" + token;
						RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
						try 
						{
							@SuppressWarnings("unused")
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
											userImageURL = userImageURL.substring(0, userImageURL.length() - 2) + "30";
											//System.out.println(userImageURL);
											userGender = js.get("gender").isString().stringValue();
											userIsPlus = js.get("isPlusUser").isBoolean().booleanValue();

											createUserLabel();
											checkUserInfo(userId, userName, userEmail, userGender, userIsPlus, userImageURL, favBike, bikeName, bikeColor);
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
			@SuppressWarnings("unused")
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
			favRacksCommon = new ArrayList< ArrayList<String[]>>();
			userHistory = new ArrayList<UserSearchHistoryInstance>();
			favBike = "";
			bikeName = "";
			bikeColor = "";
			
			messenger("Successfully cleared all tokens and signed out");
			removeUserLabel();
			loginFlipFlop = 0;
		}
	}
	

	private void printerdebug()
	{
		String output = "Friends who also liked the same rack: \n";
		for (int i = 0; i < favRacksCommon.size(); i++)
		{
			if (favRacksCommon.size() == 0)
			{
				messenger("No common rack");
			}
			else
			{
				for(int j=0; j < favRacksCommon.get(i).size() ; j++)
				{				
					String[] names =  favRacksCommon.get(i).get(j);
					output = output + "\n POS: "+ favRacks.get(i).getCoordinate().toString() + "\n Name: " + names[0]+" id: "+names[1]+" Rating: "+names[2];
				} 
			}

		}
		messenger(output);
	}

	// ====================== LOGIN PROCEDURE CALLS END ======================	
	public double getFRIENDAverageRating (int arrayRACKpos)
	{
		double output = 0;
		int friendcount = 0;
		int RatingsubTotal = 0;
		for (int i = 0; i < favRacksCommon.get(arrayRACKpos).size(); i++)
		{
			String[] person = favRacksCommon.get(arrayRACKpos).get(i);
			RatingsubTotal += Integer.parseInt(person[2]);
			friendcount += 1;
		}
		
		if(friendcount > 0){
			output = RatingsubTotal/friendcount;
		}

		return output;
	}

	public void calculateNewAvg(String address, String position, ArrayList<rackStarRatings> todorating)
	{
		int occurance = 0;
		int subtotal = 0;
		double output = 0;
		for (int r = 0;  r< todorating.size(); r++)
		{
			occurance ++;
			subtotal += todorating.get(r).getRating();
		}
		output = subtotal/occurance;

		// update local!
		int i = 0;
		while (i < listofracks.size())
		{
			if (listofracks.get(i).getAddress().equals(address) && listofracks.get(i).getCoordinate().toString().equals(position))
			{
				if (output != listofracks.get(i).getRating())
				{
					BikeRack br = listofracks.get(i);
					listofracks.get(i).setRating(output);
					// server update calls
					rackOps(br.getAddress(), br.getCoordinate(), br.getRackCount(), br.getNumberStolenBikes(), br.getCrimeScore(), output, 2);
				}

				break;
			}
			i++;
		}		
	}

	public static ArrayList<Crime> getCrimeData()
	{
		return listofcrimes;
	}
	public static ArrayList<BikeRack> getRackData()
	{
		return listofracks;
	}

}

