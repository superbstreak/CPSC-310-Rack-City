package cs310MRAK.rackcity.client;

import java.io.IOException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class DropboxUtil {

    public static void authorizeForm(String url, String testing_user, String testing_password) throws IOException {

        assert url != null : "You must give a url.";
        assert testing_user != null : "You gave a null testing_user.";
        assert testing_password != null : "You gave a null testing_password.";

        System.out.println("AUTHORIZING: " + url);

        
    String proxyHost = "proxy.Host";
    String schemes[] = {"https", "http" };
    WebClient webClient = null;
    try{
      for (String scheme : schemes) {
        String proxHostTmp = System.getProperty(scheme + ".proxyHost"); 
        int proxyPortTmp = Integer.parseInt( System.getProperty(scheme + ".proxyPort"));  
        if (null!=proxHostTmp){
          webClient = new WebClient(BrowserVersion.FIREFOX_3_6, proxyHost, proxyPortTmp);
        }
      }
    }catch(Exception e){}
    if (webClient == null)
      webClient = new WebClient();
       // webClient.setJavaScriptEnabled(false);
        
        HtmlPage page = (HtmlPage)webClient.getPage(url);
        HtmlForm form = (HtmlForm)page.getForms().get(2);
        HtmlSubmitInput button = (HtmlSubmitInput)form.getInputByValue("Log in");


        HtmlTextInput emailField = (HtmlTextInput)form.getInputByName("login_email");
        emailField.setValueAttribute(testing_user);

        HtmlPasswordInput password = (HtmlPasswordInput)form.getInputByName("login_password");
        password.setValueAttribute(testing_password);

        // Now submit the form by clicking the button and get back the second page.
        HtmlPage page2 = (HtmlPage)button.click();

        try {
            form = (HtmlForm)page2.getForms().get(2);
            button = (HtmlSubmitInput)form.getInputByValue("Allow");
            button.click();
        } catch(ElementNotFoundException e) {
            System.out.println("No allow button, must be already approved.");
        } catch(IndexOutOfBoundsException e) {
            System.out.println("No second form, must be already approved.");
        }
    }

}
