package cs310MRAK.rackcity.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cs310MRAK.rackcity.client.RackFavouritesService;

public class RackFavouritesServiceImpl extends RemoteServiceServlet implements RackFavouritesService{

	@Override
	public void addFavourite(String rackID) {
		// TODO JDO !!!
		
	}

	@Override
	public ArrayList<String> getFavourites() {
		// TODO JDO !!!
		return null;
	}

	@Override
	public void removeFavourite(String rackID) {
		// TODO JDO !!!
		
	}

}
