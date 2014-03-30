package cs310MRAK.rackcity.shared;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;



@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserSearchHistoryInstance {

	// latlng are not serialized, all convert to string, require convert back. by splitting comma and A[0] = lat, A[1] = lon
		
		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private String key;
		@Persistent
		private String userID; // gmail address
		@Persistent
		private String searchAddress; // "vancouver"
		@Persistent
		private Date createDate;
		@Persistent
		private int radius; // "2"
		@Persistent
		private int crimeScore; // "5"
		@Persistent
		private int rating;
		
		
		//a good way to use constructor
			public UserSearchHistoryInstance() {
				this.createDate = new Date();
			}
		 
		 
		public UserSearchHistoryInstance(String key, String userID, String searchAddress, int radius, int crimeScore, int rate)
		{
			this();
			this.key = key;
			this.userID = userID;
			this.searchAddress = searchAddress;
			this.radius = radius;
			this.crimeScore = crimeScore;
			this.rating = rate;
		}

		public int getRating()
		{
			return this.rating;
		}
		
		public void setRating(int rate)
		{
			this.rating = rate;
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






		public int getRadius() {
			return radius;
		}






		public void setRadius(int radius) {
			this.radius = radius;
		}






		public int getCrimeScore() {
			return crimeScore;
		}






		public void setCrimeScore(int crimeScore) {
			this.crimeScore = crimeScore;
		}
  
 
 
  
  		
 	
}
 