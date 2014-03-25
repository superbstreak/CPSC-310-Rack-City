package cs310MRAK.rackcity.client;



import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;

public class Dropbox {

  private final static String APP_KEY = "mimy6yqx86oltns";
  private final static String APP_SECRET = "12cg9lo36ts4vp4";
  private final static String USER_NAME = "sorting@outlook.com";
  private final static String PASSWORD = "mrakconsultinggruwp";
  private final static AccessType ACCESS_TYPE = AccessType.DROPBOX;
  
  private DropboxAPI<WebAuthSession> mDBApi;

  public void init() throws Exception {
    AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
    WebAuthSession session = new WebAuthSession(appKeys, ACCESS_TYPE);
    mDBApi = new DropboxAPI<WebAuthSession>(session);
    WebAuthInfo authInfo = session.getAuthInfo();
    System.out.println("url="+authInfo.url);
    DropboxUtil.authorizeForm(authInfo.url, USER_NAME, PASSWORD);
    mDBApi.getSession().retrieveWebAccessToken(authInfo.requestTokenPair);
    System.out.println(mDBApi.accountInfo().displayName);
  }
}