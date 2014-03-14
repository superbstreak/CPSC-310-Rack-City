package cs310MRAK.rackcity.client;

import java.util.logging.Logger;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Rack_City implements EntryPoint {

	private MapWidget googleMap = null;
	private DockPanel dockPanel = null;
	private Marker currentMarker = null;
	private String currentDatasheetItem = null;
	private Filter filters;
	private List<BikeRack> currentRackList = null;
	private List<Crime> currentCrimeList = null;
	private LatLng currentAddress = null;
	//private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
//	private static final Logger LOG = Logger.getLogger(Rack_City.class.getName());
	private int hackyCodeInt = 0;
	//=============================
	  private LoginInfo loginInfo = null;
	  private VerticalPanel loginPanel = new VerticalPanel();
	  private Label loginLabel = new Label("Please sign in to your Google Account to access the StockWatcher application.");
	  private Anchor signInLink = new Anchor("Sign In");
	 
	  
	  

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		
//		// later we would add a checkLoggedIn(); [ where I put the [*&$] symbol ]
//		// and add the NotLoggedInException 
//			// [*&$]
//			if(hackyCodeInt == 0){
//			PersistenceManager pm = getPersistenceManager();
//				// BikeRack(String address, LatLng coordinate, double rating, int rackCount, int crimeScore)
//			pm.makePersistent(new BikeRack("134 East Abbott St, Vancouver, BC", LatLng.newInstance(49.284176,-123.106037), 3, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("216 East Abbott St, Vancouver, BC", LatLng.newInstance(49.283429,-123.106404), 4, 2, 1, 1));
//		    pm.makePersistent(new BikeRack("1600 North Alberni St, Vancouver, BC", LatLng.newInstance(49.2902,-123.131333), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("90 South Alexander, Vancouver, BC", LatLng.newInstance(49.283638,-123.102348), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("55 North Alexander, Vancouver, BC", LatLng.newInstance(49.283613,-123.103), 2, 2, 1, 1));
//		    pm.makePersistent(new BikeRack("200 North Alexander, Vancouver, BC", LatLng.newInstance(49.284083,-123.099423), 2, 2, 3, 1));
//		    pm.makePersistent(new BikeRack("305 North Alexander St, Vancouver, BC", LatLng.newInstance(49.284449,-123.097679), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("475 North Alexander St, Vancouver, BC", LatLng.newInstance(49.284113,-123.093897), 5, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("2083 West Alma, Vancouver, BC", LatLng.newInstance(49.267958,-123.185798), 4, 3, 3, 1));
//		    pm.makePersistent(new BikeRack("5704 East Balsam, Vancouver, BC", LatLng.newInstance(49.234669,-123.161332), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("1400 East Balsam St, Vancouver, BC", LatLng.newInstance(49.273465,-123.159832), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("900 East Beatty, Vancouver, BC", LatLng.newInstance(49.276482,-123.115608), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("590 East Beatty, Vancouver, BC", LatLng.newInstance(49.280006,-123.110317), 5, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("2400 West Birch St, Vancouver, BC", LatLng.newInstance(49.264387,-123.133673), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("1300 West Broughton, Vancouver, BC", LatLng.newInstance(49.283001,-123.137342), 4, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("635 West Burrard, Vancouver, BC", LatLng.newInstance(49.284999,-123.120252), 2, 4, 5, 1));
//		    pm.makePersistent(new BikeRack("1081 West Burrard, Vancouver, BC", LatLng.newInstance(49.279954,-123.128292), 3, 3, 2, 1));
//		    pm.makePersistent(new BikeRack("800 West Bute St, Vancouver, BC", LatLng.newInstance(49.285995,-123.126972), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("1100 East Bute St, Vancouver, BC", LatLng.newInstance(49.282909,-123.131625), 4, 2, 4, 1));
//		    pm.makePersistent(new BikeRack("4099 West Cambie, Vancouver, BC", LatLng.newInstance(49.184569,-123.094049), 5, 22, 4, 1));
//		    pm.makePersistent(new BikeRack("6488 East Cambie, Vancouver, BC", LatLng.newInstance(49.226148,-123.116345), 2, 3, 0, 1));
//		    pm.makePersistent(new BikeRack("8430 East Cambie, Vancouver, BC", LatLng.newInstance(49.184677,-123.130574), 3, 4, 5, 1));
//		    pm.makePersistent(new BikeRack("8500 East Cambie, Vancouver, BC", LatLng.newInstance(49.208853,-123.117194), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("511 West Carrall St, Vancouver, BC", LatLng.newInstance(49.280304,-123.104537), 3, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("211 West Columbia St, Vancouver, BC", LatLng.newInstance(49.28304,-123.102214), 2, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("2021 West Columbia St, Vancouver, BC", LatLng.newInstance(49.267197,-123.109585), 5, 2, 4, 1));
//		    pm.makePersistent(new BikeRack("1010 East Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275578,-123.069377), 3, 2, 1, 1));
//		    pm.makePersistent(new BikeRack("1016 East Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275486,-123.069385), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("1025 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275467,-123.069724), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("1034 East Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275238,-123.069362), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("1034 East Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275238,-123.069362), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("1035 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.275206,-123.069737), 2, 2, 2, 1));
//		    pm.makePersistent(new BikeRack("1103 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.274809,-123.069542), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("1111 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.274618,-123.069543), 3, 2, 2, 1));
//		    pm.makePersistent(new BikeRack("1135 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.27447,-123.069544), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("1365 West Commercial Dr, Vancouver, BC", LatLng.newInstance(49.272469,-123.069573), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("1858 North Commercial Dr, Vancouver, BC", LatLng.newInstance(49.267901,-123.069515), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("297 North Davie, Vancouver, BC", LatLng.newInstance(49.274814,-123.122565), 4, 6, 2, 1));
//		    pm.makePersistent(new BikeRack("4243 West Dunbar, Vancouver, BC", LatLng.newInstance(49.248541,-123.185332), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("4306 East Dunbar, Vancouver, BC", LatLng.newInstance(49.247927,-123.18504), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("4336 East Dunbar, Vancouver, BC", LatLng.newInstance(49.247122,-123.185203), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("4355 West Dunbar, Vancouver, BC", LatLng.newInstance(49.247468,-123.185397), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("4432 East Dunbar, Vancouver, BC", LatLng.newInstance(49.246813,-123.185058), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("4446 East Dunbar, Vancouver, BC", LatLng.newInstance(49.246727,-123.185075), 3, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("4474 East Dunbar, Vancouver, BC", LatLng.newInstance(49.246433,-123.185211), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("4497 West Dunbar, Vancouver, BC", LatLng.newInstance(49.246306,-123.18542), 5, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("2177 North Dundas St, Vancouver, BC", LatLng.newInstance(49.284794,-123.059777), 5, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("37 West Dunlevy Av, Vancouver, BC", LatLng.newInstance(49.284761,-123.095305), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("211 West Dunlevy Av, Vancouver, BC", LatLng.newInstance(49.28308,-123.095329), 2, 2, 3, 1));
//		    pm.makePersistent(new BikeRack("304 North Dunlevy Av, Vancouver, BC", LatLng.newInstance(49.281837,-123.095279), 5, 2, 2, 1));
//		    pm.makePersistent(new BikeRack("433 West Dunlevy Av, Vancouver, BC", LatLng.newInstance(49.280913,-123.095546), 2, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("1800 North E 11th Ave, Vancouver, BC", LatLng.newInstance(49.260454,-123.069483), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("2800 South E 12th Ave, Vancouver, BC", LatLng.newInstance(49.259046,-123.046942), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("607 North E 15th, Vancouver, BC", LatLng.newInstance(49.257247,-123.090752), 5, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("617 North E 15th, Vancouver, BC", LatLng.newInstance(49.257246,-123.090551), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("639 North E 15th, Vancouver, BC", LatLng.newInstance(49.257225,-123.090112), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("2450 South E 24th, Vancouver, BC", LatLng.newInstance(49.248812,-123.054464), 4, 3, 0, 1));
//		    pm.makePersistent(new BikeRack("2790 South E 29th, Vancouver, BC", LatLng.newInstance(49.244598,-123.046338), 4, 3, 1, 1));
//		    pm.makePersistent(new BikeRack("112 South E 3rd, Vancouver, BC", LatLng.newInstance(49.267941,-123.102345), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("146 South E 3rd, Vancouver, BC", LatLng.newInstance(49.268142,-123.101657), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("696 South E 45th, Vancouver, BC", LatLng.newInstance(49.229182,-123.091575), 5, 2, 2, 1));
//		    pm.makePersistent(new BikeRack("127 North E 4th, Vancouver, BC", LatLng.newInstance(49.265319,-123.103005), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("137 North E 4th, Vancouver, BC", LatLng.newInstance(49.267344,-123.101541), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("7 North E 6th, Vancouver, BC", LatLng.newInstance(49.265596,-123.104687), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("48 South E 6th, Vancouver, BC", LatLng.newInstance(49.265553,-123.103408), 2, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("1600 South E 6th Ave, Vancouver, BC", LatLng.newInstance(49.266225,-123.14104), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("156 South E 7th, Vancouver, BC", LatLng.newInstance(49.264613,-123.101558), 4, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("1600 North E 8th Ave, Vancouver, BC", LatLng.newInstance(49.263273,-123.071549), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5720 East E Boulevard, Vancouver, BC", LatLng.newInstance(49.234579,-123.15499), 3, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("1725 North E Broadway, Vancouver, BC", LatLng.newInstance(49.262397,-123.069124), 4, 4, 1, 1));
//		    pm.makePersistent(new BikeRack("8 South E Cordova, Vancouver, BC", LatLng.newInstance(49.282421,-123.104095), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("63 North E Cordova, Vancouver, BC", LatLng.newInstance(49.282389,-123.103199), 5, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("93 North E Cordova, Vancouver, BC", LatLng.newInstance(49.282376,-123.102475), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("208 South E Georgia, Vancouver, BC", LatLng.newInstance(49.278461,-123.099308), 4, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("239 North E Georgia, Vancouver, BC", LatLng.newInstance(49.278582,-123.099274), 5, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("501 North E Georgia, Vancouver, BC", LatLng.newInstance(49.278509,-123.093446), 3, 2, 2, 1));
//		    pm.makePersistent(new BikeRack("403 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.281425,-123.095266), 5, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("573 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.281215,-123.091692), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("769 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.28126,-123.087973), 3, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("843 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.281132,-123.08616), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("1489 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.281497,-123.074507), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("1618 South E Hastings St, Vancouver, BC", LatLng.newInstance(49.281248,-123.072171), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("1736 South E Hastings St, Vancouver, BC", LatLng.newInstance(49.281224,-123.069525), 3, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("1756 South E Hastings St, Vancouver, BC", LatLng.newInstance(49.281107,-123.069233), 5, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("2647 North E Hastings St, Vancouver, BC", LatLng.newInstance(49.281269,-123.050743), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("142 South E Pender St, Vancouver, BC", LatLng.newInstance(49.28034,-123.101293), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("1809 West Fir St, Vancouver, BC", LatLng.newInstance(49.269448,-123.140958), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("4097 West Fraser, Vancouver, BC", LatLng.newInstance(49.248679,-123.09015), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("4129 West Fraser, Vancouver, BC", LatLng.newInstance(49.248159,-123.090332), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("4153 West Fraser, Vancouver, BC", LatLng.newInstance(49.248024,-123.090312), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("4230 East Fraser, Vancouver, BC", LatLng.newInstance(49.247482,-123.090137), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("4259 West Fraser, Vancouver, BC", LatLng.newInstance(49.246992,-123.090195), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("4288 East Fraser, Vancouver, BC", LatLng.newInstance(49.246811,-123.090167), 3, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("4304 East Fraser, Vancouver, BC", LatLng.newInstance(49.246474,-123.090023), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("4305 West Fraser, Vancouver, BC", LatLng.newInstance(49.246547,-123.090215), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("4386 East Fraser, Vancouver, BC", LatLng.newInstance(49.245719,-123.090205), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("4590 East Fraser, Vancouver, BC", LatLng.newInstance(49.243669,-123.090255), 3, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("3497 West Fraser St, Vancouver, BC", LatLng.newInstance(49.253788,-123.090313), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("4950 East Fraser St, Vancouver, BC", LatLng.newInstance(49.239836,-123.090358), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("6007 West Fraser St, Vancouver, BC", LatLng.newInstance(49.230016,-123.090797), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("189 West Gore St, Vancouver, BC", LatLng.newInstance(49.283207,-123.097765), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("1117 West Granville St, Vancouver, BC", LatLng.newInstance(49.278239,-123.125392), 3, 4, 5, 1));
//		    pm.makePersistent(new BikeRack("1208 East Granville St, Vancouver, BC", LatLng.newInstance(49.276898,-123.126354), 5, 4, 0, 1));
//		    pm.makePersistent(new BikeRack("7985 West Granville St, Vancouver, BC", LatLng.newInstance(49.213302,-123.140388), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("8155 West Granville St, Vancouver, BC", LatLng.newInstance(49.212209,-123.140421), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("8168 East Granville St, Vancouver, BC", LatLng.newInstance(49.211761,-123.140229), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("8273 West Granville St, Vancouver, BC", LatLng.newInstance(49.211648,-123.140441), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("8324 East Granville St, Vancouver, BC", LatLng.newInstance(49.210657,-123.140236), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("8333 West Granville St, Vancouver, BC", LatLng.newInstance(49.211271,-123.140645), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("8435 West Granville St, Vancouver, BC", LatLng.newInstance(49.210116,-123.140711), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("8490 East Granville St, Vancouver, BC", LatLng.newInstance(49.20932,-123.140396), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("8630 East Granville St, Vancouver, BC", LatLng.newInstance(49.208024,-123.140429), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("8662 East Granville St, Vancouver, BC", LatLng.newInstance(49.207522,-123.140461), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("8679 West Granville St, Vancouver, BC", LatLng.newInstance(49.207418,-123.140782), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("2500 East Heather St, Vancouver, BC", LatLng.newInstance(49.263337,-123.119856), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("3195 West Heather St, Vancouver, BC", LatLng.newInstance(49.257025,-123.120124), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("409 West Heatley Av, Vancouver, BC", LatLng.newInstance(49.281,-123.089617), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("500 South Helmcken, Vancouver, BC", LatLng.newInstance(49.277258,-123.123167), 4, 2, 2, 1));
//		    pm.makePersistent(new BikeRack("600 South Helmcken, Vancouver, BC", LatLng.newInstance(49.278196,-123.124612), 5, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("600 North Helmcken, Vancouver, BC", LatLng.newInstance(49.278196,-123.124612), 4, 3, 2, 1));
//		    pm.makePersistent(new BikeRack("951 West Hornby St, Vancouver, BC", LatLng.newInstance(49.281026,-123.124262), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("1054 East Hornby St, Vancouver, BC", LatLng.newInstance(49.27997,-123.125799), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("1082 East Hornby St, Vancouver, BC", LatLng.newInstance(49.279648,-123.126044), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("1090 East Hornby St, Vancouver, BC", LatLng.newInstance(49.279611,-123.126339), 5, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("1451 West Hornby St, Vancouver, BC", LatLng.newInstance(49.275776,-123.132397), 2, 3, 1, 1));
//		    pm.makePersistent(new BikeRack("466 East Howe St, Vancouver, BC", LatLng.newInstance(49.28541,-123.115745), 4, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("1100 East Jervis St, Vancouver, BC", LatLng.newInstance(49.283505,-123.133853), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5099 North Joyce, Vancouver, BC", LatLng.newInstance(49.238214,-123.031947), 4, 3, 3, 1));
//		    pm.makePersistent(new BikeRack("145 North Keefer St, Vancouver, BC", LatLng.newInstance(49.279514,-123.100706), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("232 South Keefer St, Vancouver, BC", LatLng.newInstance(49.279367,-123.098871), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("2300 West Keith St, Vancouver, BC", LatLng.newInstance(49.206889,-123.022445), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("76 West Kingsway, Vancouver, BC", LatLng.newInstance(49.263225,-123.099926), 5, 2, 1, 1));
//		    pm.makePersistent(new BikeRack("2066 South Kingsway, Vancouver, BC", LatLng.newInstance(49.244607,-123.064352), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2127 North Kingsway, Vancouver, BC", LatLng.newInstance(49.244318,-123.063036), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("2001 South Macdonald, Vancouver, BC", LatLng.newInstance(49.268056,-123.168495), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("917 West Main, Vancouver, BC", LatLng.newInstance(49.27649,-123.100099), 3, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("222 East Main St, Vancouver, BC", LatLng.newInstance(49.282785,-123.099541), 3, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("309 West Main St, Vancouver, BC", LatLng.newInstance(49.282169,-123.099632), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("416 East Main St, Vancouver, BC", LatLng.newInstance(49.280899,-123.099479), 2, 2, 4, 1));
//		    pm.makePersistent(new BikeRack("506 East Main St, Vancouver, BC", LatLng.newInstance(49.280136,-123.099524), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("700 East Main St, Vancouver, BC", LatLng.newInstance(49.27828,-123.099758), 3, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("708 East Main St, Vancouver, BC", LatLng.newInstance(49.278212,-123.099738), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("728 East Main St, Vancouver, BC", LatLng.newInstance(49.278038,-123.099602), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("928 East Main St, Vancouver, BC", LatLng.newInstance(49.276194,-123.099694), 2, 2, 1, 1));
//		    pm.makePersistent(new BikeRack("1008 South Main St, Vancouver, BC", LatLng.newInstance(49.275274,-123.09981), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("1399 West Main St, Vancouver, BC", LatLng.newInstance(49.264346,-123.100885), 2, 4, 0, 1));
//		    pm.makePersistent(new BikeRack("2812 East Main St, Vancouver, BC", LatLng.newInstance(49.259977,-123.100849), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2818 East Main St, Vancouver, BC", LatLng.newInstance(49.259932,-123.100858), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("3835 West Main St, Vancouver, BC", LatLng.newInstance(49.250653,-123.101199), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("4750 East Main St, Vancouver, BC", LatLng.newInstance(49.242586,-123.101213), 3, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("900 West Mainland, Vancouver, BC", LatLng.newInstance(49.276584,-123.119014), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("1605 West Manitoba St, Vancouver, BC", LatLng.newInstance(49.271547,-123.106828), 5, 5, 0, 1));
//		    pm.makePersistent(new BikeRack("1945 West Manitoba St, Vancouver, BC", LatLng.newInstance(49.267543,-123.106712), 2, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("1200 South Marinaside Cr, Vancouver, BC", LatLng.newInstance(49.272885,-123.120015), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("127 West N Templeton, Vancouver, BC", LatLng.newInstance(49.286456,-123.059681), 3, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("1300 West Pacific Blvd, Vancouver, BC", LatLng.newInstance(49.272887,-123.125843), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("40 South Powell St, Vancouver, BC", LatLng.newInstance(49.283253,-123.103224), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("43 North Powell St, Vancouver, BC", LatLng.newInstance(49.283321,-123.103904), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("253 North Powell St, Vancouver, BC", LatLng.newInstance(49.283256,-123.098226), 3, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("300 South Powell St, Vancouver, BC", LatLng.newInstance(49.282748,-123.097586), 3, 2, 1, 1));
//		    pm.makePersistent(new BikeRack("362 South Powell St, Vancouver, BC", LatLng.newInstance(49.283153,-123.096292), 4, 2, 2, 1));
//		    pm.makePersistent(new BikeRack("374 South Powell St, Vancouver, BC", LatLng.newInstance(49.283029,-123.096035), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("398 South Powell St, Vancouver, BC", LatLng.newInstance(49.28314,-123.09543), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("403 North Powell St, Vancouver, BC", LatLng.newInstance(49.283195,-123.095058), 5, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("415 North Powell St, Vancouver, BC", LatLng.newInstance(49.283181,-123.094919), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("429 North Powell St, Vancouver, BC", LatLng.newInstance(49.283193,-123.094619), 5, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("451 North Powell St, Vancouver, BC", LatLng.newInstance(49.283125,-123.094077), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("453 North Powell St, Vancouver, BC", LatLng.newInstance(49.283124,-123.094027), 3, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("537 North Powell St, Vancouver, BC", LatLng.newInstance(49.283087,-123.092462), 3, 3, 0, 1));
//		    pm.makePersistent(new BikeRack("550 South Powell St, Vancouver, BC", LatLng.newInstance(49.283052,-123.09214), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("566 South Powell St, Vancouver, BC", LatLng.newInstance(49.282964,-123.091901), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("569 North Powell St, Vancouver, BC", LatLng.newInstance(49.28315,-123.091543), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2043 West Quebec St, Vancouver, BC", LatLng.newInstance(49.266431,-123.102813), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("2323 West Quebec St, Vancouver, BC", LatLng.newInstance(49.264286,-123.103046), 3, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("395 North Railway St, Vancouver, BC", LatLng.newInstance(49.284923,-123.095493), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("460 South Railway St, Vancouver, BC", LatLng.newInstance(49.284748,-123.093968), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("1400 West Richards, Vancouver, BC", LatLng.newInstance(49.273832,-123.127314), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("1200 East Richards, Vancouver, BC", LatLng.newInstance(49.275821,-123.124295), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("500 East Richards St, Vancouver, BC", LatLng.newInstance(49.283025,-123.113408), 2, 2, 2, 1));
//		    pm.makePersistent(new BikeRack("1025 North Robson St, Vancouver, BC", LatLng.newInstance(49.283628,-123.12329), 4, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("1800 North Robson St, Vancouver, BC", LatLng.newInstance(49.291533,-123.135554), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("655 West Seymour, Vancouver, BC", LatLng.newInstance(49.282365,-123.116388), 2, 4, 1, 1));
//		    pm.makePersistent(new BikeRack("1100 West Seymour, Vancouver, BC", LatLng.newInstance(49.277518,-123.123691), 3, 4, 4, 1));
//		    pm.makePersistent(new BikeRack("1000 North Smithe, Vancouver, BC", LatLng.newInstance(49.281958,-123.123995), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("1946 South Triumph St, Vancouver, BC", LatLng.newInstance(49.283905,-123.064465), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("219 North Union St, Vancouver, BC", LatLng.newInstance(49.277667,-123.098759), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("221 North Union St, Vancouver, BC", LatLng.newInstance(49.277666,-123.098711), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("231 North Union St, Vancouver, BC", LatLng.newInstance(49.277661,-123.098471), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("241 North Union St, Vancouver, BC", LatLng.newInstance(49.277656,-123.098232), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("243 North Union St, Vancouver, BC", LatLng.newInstance(49.277723,-123.098181), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("1033 North Venables St, Vancouver, BC", LatLng.newInstance(49.276913,-123.081606), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("4932 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.239512,-123.065337), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("4969 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.239457,-123.065374), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("4990 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.239103,-123.065157), 3, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5052 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.238509,-123.065359), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("5057 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.238793,-123.065572), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("5076 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.238341,-123.065359), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5124 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.23766,-123.065389), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("5157 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.237916,-123.065414), 4, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("5239 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.237128,-123.065442), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5265 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.236891,-123.065447), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5393 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.235362,-123.065485), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("5448 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.235413,-123.065447), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("5449 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.234836,-123.065504), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("5470 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.235243,-123.065451), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("5492 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.235022,-123.065462), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5501 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.234395,-123.065674), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5579 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.233852,-123.065529), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("5656 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.233276,-123.065508), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5661 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.233088,-123.065685), 4, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("5723 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.232352,-123.065569), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5732 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.232195,-123.065396), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("5780 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.231667,-123.065432), 4, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("5807 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.231609,-123.065589), 3, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("5853 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.231081,-123.065596), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("5885 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.230756,-123.065602), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("6225 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.227282,-123.06586), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("6404 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.226171,-123.065685), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("6440 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.225918,-123.065692), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("6470 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.225527,-123.065588), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("6471 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.225739,-123.065901), 2, 2, 3, 1));
//		    pm.makePersistent(new BikeRack("6495 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.225353,-123.065915), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("6586 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.224206,-123.065741), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("6647 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.223683,-123.065786), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("6689 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.223235,-123.065796), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("6904 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.22082,-123.06583), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("6942 East Victoria Dr, Vancouver, BC", LatLng.newInstance(49.220827,-123.065791), 5, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("6945 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.220977,-123.065864), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("6979 West Victoria Dr, Vancouver, BC", LatLng.newInstance(49.220577,-123.065872), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("1507 North W 12th, Vancouver, BC", LatLng.newInstance(49.26081,-123.139077), 3, 3, 0, 1));
//		    pm.makePersistent(new BikeRack("2108 South W 12th, Vancouver, BC", LatLng.newInstance(49.26103,-123.155241), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("1500 South W 13th, Vancouver, BC", LatLng.newInstance(49.259836,-123.139425), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2595 North W 16th, Vancouver, BC", LatLng.newInstance(49.257706,-123.164646), 3, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("2595 North W 16th, Vancouver, BC", LatLng.newInstance(49.257706,-123.164646), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("2603 North W 16th, Vancouver, BC", LatLng.newInstance(49.257829,-123.165031), 2, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("2611 North W 16th, Vancouver, BC", LatLng.newInstance(49.257963,-123.16511), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("2100 South W 1st, Vancouver, BC", LatLng.newInstance(49.270762,-123.152554), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("500 North W 24th, Vancouver, BC", LatLng.newInstance(49.33136,-123.085426), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("29 North W 2nd, Vancouver, BC", LatLng.newInstance(49.269298,-123.105175), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("52 South W 2nd, Vancouver, BC", LatLng.newInstance(49.269254,-123.106566), 3, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("118 South W 2nd, Vancouver, BC", LatLng.newInstance(49.269156,-123.107226), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("336 South W 2nd, Vancouver, BC", LatLng.newInstance(49.268114,-123.111514), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("1650 South W 2nd, Vancouver, BC", LatLng.newInstance(49.269717,-123.142007), 2, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("1720 South W 2nd, Vancouver, BC", LatLng.newInstance(49.269678,-123.143338), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("525 North W 2nd, Vancouver, BC", LatLng.newInstance(49.267198,-123.113882), 5, 4, 2, 1));
//		    pm.makePersistent(new BikeRack("1900 South W 2nd, Vancouver, BC", LatLng.newInstance(49.269809,-123.147793), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("60 South W 3rd, Vancouver, BC", LatLng.newInstance(49.268305,-123.105708), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("105 North W 3rd, Vancouver, BC", LatLng.newInstance(49.268342,-123.106699), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("124 South W 3rd, Vancouver, BC", LatLng.newInstance(49.268322,-123.10697), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("126 South W 3rd, Vancouver, BC", LatLng.newInstance(49.268325,-123.107105), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("190 South W 3rd, Vancouver, BC", LatLng.newInstance(49.268362,-123.109848), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("290 South W 3rd, Vancouver, BC", LatLng.newInstance(49.268363,-123.109826), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2107 North W 40th, Vancouver, BC", LatLng.newInstance(49.235576,-123.155589), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2130 South W 40th, Vancouver, BC", LatLng.newInstance(49.235569,-123.156939), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("510 South W 41st, Vancouver, BC", LatLng.newInstance(49.233519,-123.116451), 4, 4, 0, 1));
//		    pm.makePersistent(new BikeRack("2021 North W 41st, Vancouver, BC", LatLng.newInstance(49.234661,-123.152748), 4, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("2057 North W 41st, Vancouver, BC", LatLng.newInstance(49.234683,-123.153687), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("2094 South W 41st, Vancouver, BC", LatLng.newInstance(49.234522,-123.154701), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("2198 South W 41st, Vancouver, BC", LatLng.newInstance(49.234611,-123.158499), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("2208 South W 41st, Vancouver, BC", LatLng.newInstance(49.23456,-123.158774), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2225 North W 41st, Vancouver, BC", LatLng.newInstance(49.23475,-123.15826), 3, 2, 2, 1));
//		    pm.makePersistent(new BikeRack("2246 South W 41st, Vancouver, BC", LatLng.newInstance(49.234437,-123.159179), 5, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("2253 North W 41st, Vancouver, BC", LatLng.newInstance(49.234638,-123.158666), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2260 South W 41st, Vancouver, BC", LatLng.newInstance(49.234579,-123.159278), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("2267 North W 41st, Vancouver, BC", LatLng.newInstance(49.234643,-123.158861), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("2305 North W 41st, Vancouver, BC", LatLng.newInstance(49.234836,-123.159797), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("2326 South W 41st, Vancouver, BC", LatLng.newInstance(49.234539,-123.16045), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2443 North W 41st, Vancouver, BC", LatLng.newInstance(49.234697,-123.162126), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("2448 South W 41st, Vancouver, BC", LatLng.newInstance(49.234538,-123.162027), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("2496 South W 41st, Vancouver, BC", LatLng.newInstance(49.234678,-123.162586), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("38 South W 4th, Vancouver, BC", LatLng.newInstance(49.2674,-123.105724), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("163 West W 4th, Vancouver, BC", LatLng.newInstance(49.267483,-123.108684), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("165 West W 4th, Vancouver, BC", LatLng.newInstance(49.267556,-123.109324), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("1590 South W 4th, Vancouver, BC", LatLng.newInstance(49.267834,-123.140737), 3, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("1599 North W 4th, Vancouver, BC", LatLng.newInstance(49.268122,-123.140672), 3, 2, 3, 1));
//		    pm.makePersistent(new BikeRack("1628 South W 4th, Vancouver, BC", LatLng.newInstance(49.267947,-123.14121), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("1723 North W 4th, Vancouver, BC", LatLng.newInstance(49.268109,-123.143841), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("1755 North W 4th, Vancouver, BC", LatLng.newInstance(49.268125,-123.144703), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("1793 North W 4th, Vancouver, BC", LatLng.newInstance(49.268141,-123.144994), 4, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("2625 North W 4th, Vancouver, BC", LatLng.newInstance(49.268356,-123.16521), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("2645 North W 4th, Vancouver, BC", LatLng.newInstance(49.268419,-123.165735), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2662 South W 4th, Vancouver, BC", LatLng.newInstance(49.268187,-123.165235), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("2682 South W 4th, Vancouver, BC", LatLng.newInstance(49.268333,-123.165757), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("2695 North W 4th, Vancouver, BC", LatLng.newInstance(49.268512,-123.166235), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("2702 South W 4th, Vancouver, BC", LatLng.newInstance(49.268233,-123.166519), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2716 South W 4th, Vancouver, BC", LatLng.newInstance(49.268341,-123.166821), 5, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("2722 South W 4th, Vancouver, BC", LatLng.newInstance(49.268343,-123.166969), 4, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("2828 South W 4th, Vancouver, BC", LatLng.newInstance(49.268312,-123.168912), 2, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("2880 South W 4th, Vancouver, BC", LatLng.newInstance(49.268312,-123.169744), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("2923 North W 4th, Vancouver, BC", LatLng.newInstance(49.268476,-123.170532), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("2947 North W 4th, Vancouver, BC", LatLng.newInstance(49.268419,-123.170517), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("2954 South W 4th, Vancouver, BC", LatLng.newInstance(49.268406,-123.17131), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("3303 North W 4th, Vancouver, BC", LatLng.newInstance(49.268524,-123.177825), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("3327 North W 4th, Vancouver, BC", LatLng.newInstance(49.268597,-123.178325), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("3337 North W 4th, Vancouver, BC", LatLng.newInstance(49.268596,-123.178621), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("3502 South W 4th, Vancouver, BC", LatLng.newInstance(49.268579,-123.181795), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("3512 South W 4th, Vancouver, BC", LatLng.newInstance(49.268583,-123.182223), 3, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("3525 North W 4th, Vancouver, BC", LatLng.newInstance(49.268641,-123.182519), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("3545 North W 4th, Vancouver, BC", LatLng.newInstance(49.26871,-123.182697), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("3570 South W 4th, Vancouver, BC", LatLng.newInstance(49.268591,-123.183044), 3, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("3571 North W 4th, Vancouver, BC", LatLng.newInstance(49.268699,-123.183487), 2, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("3590 South W 4th, Vancouver, BC", LatLng.newInstance(49.268595,-123.183392), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("3598 South W 4th, Vancouver, BC", LatLng.newInstance(49.268469,-123.183385), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("3605 North W 4th, Vancouver, BC", LatLng.newInstance(49.268699,-123.18379), 2, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("3608 South W 4th, Vancouver, BC", LatLng.newInstance(49.268497,-123.183822), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("3650 South W 4th, Vancouver, BC", LatLng.newInstance(49.268606,-123.184841), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("3661 North W 4th, Vancouver, BC", LatLng.newInstance(49.268712,-123.184871), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("3689 North W 4th, Vancouver, BC", LatLng.newInstance(49.268744,-123.185395), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("188 North W 5th, Vancouver, BC", LatLng.newInstance(49.266551,-123.109046), 2, 2, 3, 1));
//		    pm.makePersistent(new BikeRack("112 South W 6th, Vancouver, BC", LatLng.newInstance(49.265615,-123.107233), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("4 South W 7th, Vancouver, BC", LatLng.newInstance(49.264554,-123.10507), 3, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("75 North W 7th, Vancouver, BC", LatLng.newInstance(49.264717,-123.106664), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("228 South W 7th, Vancouver, BC", LatLng.newInstance(49.264763,-123.10984), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("331 North W 7th, Vancouver, BC", LatLng.newInstance(49.264888,-123.111801), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("33 North W 8th, Vancouver, BC", LatLng.newInstance(49.263903,-123.105804), 3, 3, 2, 1));
//		    pm.makePersistent(new BikeRack("77 North W 8th, Vancouver, BC", LatLng.newInstance(49.26381,-123.106506), 3, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("138 South W 8th, Vancouver, BC", LatLng.newInstance(49.263744,-123.108291), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("156 South W 8th, Vancouver, BC", LatLng.newInstance(49.263745,-123.108657), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("224 South W 8th, Vancouver, BC", LatLng.newInstance(49.263745,-123.110049), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("260 South W 8th, Vancouver, BC", LatLng.newInstance(49.263865,-123.110438), 2, 2, 3, 1));
//		    pm.makePersistent(new BikeRack("288 South W 8th, Vancouver, BC", LatLng.newInstance(49.263868,-123.110733), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("900 North W 8th, Vancouver, BC", LatLng.newInstance(49.264205,-123.124482), 2, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("1500 South W 8th, Vancouver, BC", LatLng.newInstance(49.264484,-123.138688), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("5663 West W Boulevard, Vancouver, BC", LatLng.newInstance(49.235049,-123.155702), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("5765 West W Boulevard, Vancouver, BC", LatLng.newInstance(49.234026,-123.155615), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("5777 West W Boulevard, Vancouver, BC", LatLng.newInstance(49.233951,-123.155723), 4, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("496 South W Broadway, Vancouver, BC", LatLng.newInstance(49.262983,-123.114518), 4, 11, 3, 1));
//		    pm.makePersistent(new BikeRack("1245 North W Broadway, Vancouver, BC", LatLng.newInstance(49.26352,-123.132477), 4, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("1278 South W Broadway, Vancouver, BC", LatLng.newInstance(49.263339,-123.132747), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("1885 North W Broadway, Vancouver, BC", LatLng.newInstance(49.263896,-123.147736), 5, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("2505 North W Broadway, Vancouver, BC", LatLng.newInstance(49.264119,-123.162715), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("3124 South W Broadway, Vancouver, BC", LatLng.newInstance(49.264218,-123.174853), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("2 South W Cordova, Vancouver, BC", LatLng.newInstance(49.282487,-123.104478), 2, 5, 4, 1));
//		    pm.makePersistent(new BikeRack("108 South W Cordova, Vancouver, BC", LatLng.newInstance(49.282897,-123.10708), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("301 North W Cordova, Vancouver, BC", LatLng.newInstance(49.283652,-123.109263), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("403 North W Cordova, Vancouver, BC", LatLng.newInstance(49.284192,-123.110048), 5, 2, 4, 1));
//		    pm.makePersistent(new BikeRack("403 North W Cordova, Vancouver, BC", LatLng.newInstance(49.284192,-123.110048), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("415 North W Cordova, Vancouver, BC", LatLng.newInstance(49.284232,-123.110174), 5, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("601 North W Cordova, Vancouver, BC", LatLng.newInstance(49.285578,-123.111893), 2, 3, 5, 1));
//		    pm.makePersistent(new BikeRack("1100 South W Cordova, Vancouver, BC", LatLng.newInstance(49.289123,-123.121789), 5, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("650 South W Georgia, Vancouver, BC", LatLng.newInstance(49.28209,-123.117732), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("702 South W Georgia, Vancouver, BC", LatLng.newInstance(49.282966,-123.118879), 2, 8, 0, 1));
//		    pm.makePersistent(new BikeRack("30 South W Hastings, Vancouver, BC", LatLng.newInstance(49.281583,-123.105574), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("43 North W Hastings, Vancouver, BC", LatLng.newInstance(49.281903,-123.106063), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("84 South W Hastings, Vancouver, BC", LatLng.newInstance(49.281817,-123.107008), 2, 3, 4, 1));
//		    pm.makePersistent(new BikeRack("128 South W Hastings, Vancouver, BC", LatLng.newInstance(49.282106,-123.108271), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("207 North W Hastings, Vancouver, BC", LatLng.newInstance(49.2829,-123.109623), 3, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("219 North W Hastings, Vancouver, BC", LatLng.newInstance(49.282783,-123.109934), 3, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("300 South W Hastings, Vancouver, BC", LatLng.newInstance(49.282809,-123.110027), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("314 South W Hastings, Vancouver, BC", LatLng.newInstance(49.282992,-123.110317), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("400 South W Hastings, Vancouver, BC", LatLng.newInstance(49.28342,-123.110994), 2, 3, 0, 1));
//		    pm.makePersistent(new BikeRack("473 North W Hastings, Vancouver, BC", LatLng.newInstance(49.283955,-123.111466), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("555 North W Hastings, Vancouver, BC", LatLng.newInstance(49.284683,-123.112399), 2, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("838 South W Hastings, Vancouver, BC", LatLng.newInstance(49.286302,-123.115423), 3, 2, 0, 1));
//		    pm.makePersistent(new BikeRack("59 North W Pender, Vancouver, BC", LatLng.newInstance(49.280958,-123.106735), 5, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("309 North W Pender, Vancouver, BC", LatLng.newInstance(49.282376,-123.111202), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("319 North W Pender, Vancouver, BC", LatLng.newInstance(49.282425,-123.11137), 4, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("416 South W Pender, Vancouver, BC", LatLng.newInstance(49.282947,-123.112223), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("433 North W Pender, Vancouver, BC", LatLng.newInstance(49.283156,-123.112365), 2, 1, 0, 1));
//		    pm.makePersistent(new BikeRack("445 North W Pender, Vancouver, BC", LatLng.newInstance(49.283292,-123.112696), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("518 South W Pender, Vancouver, BC", LatLng.newInstance(49.283451,-123.112993), 5, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("605 North W Pender, Vancouver, BC", LatLng.newInstance(49.284214,-123.113985), 4, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("617 North W Pender, Vancouver, BC", LatLng.newInstance(49.284202,-123.114112), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("819 North W Pender, Vancouver, BC", LatLng.newInstance(49.285654,-123.116098), 5, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("829 North W Pender, Vancouver, BC", LatLng.newInstance(49.285704,-123.116217), 3, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("830 South W Pender, Vancouver, BC", LatLng.newInstance(49.285598,-123.116327), 4, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("3 North Water St, Vancouver, BC", LatLng.newInstance(49.283524,-123.104447), 2, 3, 5, 1));
//		    pm.makePersistent(new BikeRack("10 South Water St, Vancouver, BC", LatLng.newInstance(49.283541,-123.104695), 5, 2, 5, 1));
//		    pm.makePersistent(new BikeRack("19 North Water St, Vancouver, BC", LatLng.newInstance(49.283587,-123.104819), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("102 South Water St, Vancouver, BC", LatLng.newInstance(49.283785,-123.106609), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("142 South Water St, Vancouver, BC", LatLng.newInstance(49.283983,-123.107517), 3, 1, 2, 1));
//		    pm.makePersistent(new BikeRack("199 North Water St, Vancouver, BC", LatLng.newInstance(49.284365,-123.108632), 2, 1, 1, 1));
//		    pm.makePersistent(new BikeRack("322 South Water St, Vancouver, BC", LatLng.newInstance(49.284284,-123.109409), 4, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("332 South Water St, Vancouver, BC", LatLng.newInstance(49.284349,-123.109688), 5, 1, 3, 1));
//		    pm.makePersistent(new BikeRack("375 North Water St, Vancouver, BC", LatLng.newInstance(49.284731,-123.110571), 5, 3, 0, 1));
//		    pm.makePersistent(new BikeRack("2300 West Willow, Vancouver, BC", LatLng.newInstance(49.264262,-123.121769), 2, 1, 4, 1));
//		    pm.makePersistent(new BikeRack("5687 West Yew St, Vancouver, BC", LatLng.newInstance(49.234777,-123.157842), 2, 1, 5, 1));
//		    pm.makePersistent(new BikeRack("5692 East Yew St, Vancouver, BC", LatLng.newInstance(49.234918,-123.157611), 5, 2, 2, 1));
//		    pm.makePersistent(new BikeRack("2490 East Yukon St, Vancouver, BC", LatLng.newInstance(49.263255,-123.112883), 3, 1, 4, 1));
//
//		    
//		    
//			pm.close();
//		    hackyCodeInt = 1;
//			}

		
		
		RootPanel rootPanel = RootPanel.get();
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
		titlePanel.setSize("696px", "20px");
		
		final AbsolutePanel titleViewPanel = new AbsolutePanel();
		titlePanel.add(titleViewPanel);
		titleViewPanel.setSize("696px","20px");

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
				
				LoginServiceAsync loginService = GWT.create(LoginService.class);
			    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			      public void onFailure(Throwable error) {
			    	  Window.alert("Error loading loginService!");
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
			        	// otherwise, continue login procedure
			        	loginButton.setText("Login");
			        	
			        }
			      }
			    });
				
				
				
			}
		});
		titleViewPanel.add(loginButton, 500, 0);
		
		
		Button adminButton = new Button("adminButton");
		adminButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		});
		adminButton.setText("Admin");
		titleViewPanel.add(adminButton, 575, 0);
		
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

				if(currentMarker != null){
					((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
					currentMarker = null;
				}

				googleMap.setVisible(false);
				((AbsolutePanel) ((VerticalPanel) dockPanel.getWidget(1)).getWidget(0)).remove(0);

				//TODO INSERT CODE HERE to add the fetched list from the filter class
				final ListBox rackList = new ListBox();
				rackList.addChangeHandler(new ChangeHandler() {
					public void onChange(ChangeEvent event) {
						String temp = rackList.getValue((rackList.getSelectedIndex())); //gets selected value from listbox
						int lat = Integer.parseInt(temp.substring(1, temp.indexOf(",")));
						int lng = Integer.parseInt(temp.substring(temp.indexOf(",")+1,temp.length()));
						
						if(!currentDatasheetItem.equals(temp)){
							((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
							clickRackDisplayPanel(getRack(LatLng.newInstance(lat, lng)));
						}else if (currentDatasheetItem.equals(temp)){
						}
						else{
							clickRackDisplayPanel(getRack(LatLng.newInstance(lat, lng)));
						}
					}
				});
				rackList.setSize("700px", "500px");
				rackList.setVisibleItemCount(10);

				for (BikeRack rack : currentRackList){
					rackList.addItem("(" + rack.getCoordinate().getLatitude() + ", " + 
							rack.getCoordinate().getLongitude() + ") " + "Distance from you: " + 
							calcLatLngDistance(rack.getCoordinate()));
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
					Marker tmpRack = ((Marker) event.getOverlay());

					if(tmpRack != null && currentMarker != null && !tmpRack.equals(currentMarker)){
						((HorizontalPanel) dockPanel.getWidget(3)).remove(0);
						currentMarker = tmpRack;
						clickRackDisplayPanel(getRack(currentMarker.getLatLng()));

					}else if (tmpRack != null && currentMarker == null){
						currentMarker = tmpRack;
						clickRackDisplayPanel(getRack(currentMarker.getLatLng()));

					}else if(tmpRack != null && tmpRack.equals(currentMarker)){

					}else{
						currentMarker = null;
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
				currentAddress = point;
				googleMap.setCenter(currentAddress);
				googleMap.setZoomLevel(14);
				displayRadius(currentAddress, radius);

				googleMap.addOverlay(addMarker(point, 2));

				/*
				 * The following code should get the appropriate list based on all the preconditions.
				 */
				
				currentCrimeList = filters.getFilteredCrimeList(currentAddress, radius);
				currentRackList = filters.getFilteredRackList(currentAddress, radius, rating, crimeScore);
				
				for (BikeRack rack : currentRackList) {
					googleMap.addOverlay(addMarker(rack.getCoordinate(), 2));
				}
				
				for (Crime crime : currentCrimeList) {
					googleMap.addOverlay(addMarker(crime.getCoordinate(), 3));
				}
			}
		});
		return;
	}
	
	private void loadLogin() {
	    // Assemble login panel.
		//Window.alert("Please login to use this feature.");
	    signInLink.setHref(loginInfo.getLoginUrl());
	    loginPanel.add(loginLabel);
	    loginPanel.add(signInLink);
	    RootPanel.get("TEST").add(loginPanel);
	  }

	/**
	 * Method called when a rack on a map is clicked.
	 * Displays the 'Report Crime' button and title of rack in a panel on the right of the page
	 */
	private void clickRackDisplayPanel(BikeRack rack){
		
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
			        	Window.alert("Report Crime Pressed!");
			        } 
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
	private Marker addMarker(LatLng pos, int type)
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

	    return (double) (dist * meterConversion);
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
	
//	// because singleton. 
//	private PersistenceManager getPersistenceManager() {
//		// TODO Auto-generated method stub
//		return PMF.getPersistenceManager();
//	}
}

