package cs310MRAK.rackcity.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserObject {

	// latlng are not serialized, all convert to string, require convert back. by splitting comma and A[0] = lat, A[1] = lon
		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private String userID; // gmail address
		@Persistent
		private String searchAddress; // "vancouver"
		@Persistent
		private String searchAddressLatLong; // "49.231, -149.859"
		@Persistent
		private String radius; // "2"
		@Persistent
		private String crimeScore; // "5"
		  
		 
		public UserObject(String userID, String searchAddress, String searchAddressLatLong, String radius, String crimeScore)
		{
			this.userID = userID;
			this.searchAddress = searchAddress;
			this.searchAddressLatLong = searchAddressLatLong;
			this.radius = radius;
			this.crimeScore = crimeScore;
		}






		public String getUserID() {
			return userID;
		}






		public void setUserID(String userID) {
			this.userID = userID;
		}






		public String getSearchAddress() {
			return searchAddress;
		}






		public void setSearchAddress(String searchAddress) {
			this.searchAddress = searchAddress;
		}






		public String getSearchAddressLatLong() {
			return searchAddressLatLong;
		}






		public void setSearchAddressLatLong(String searchAddressLatLong) {
			this.searchAddressLatLong = searchAddressLatLong;
		}






		public String getRadius() {
			return radius;
		}






		public void setRadius(String radius) {
			this.radius = radius;
		}






		public String getCrimeScore() {
			return crimeScore;
		}






		public void setCrimeScore(String crimeScore) {
			this.crimeScore = crimeScore;
		}
  
 
 
  
  		
 	
}
 