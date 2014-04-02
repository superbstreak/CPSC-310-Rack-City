package cs310MRAK.rackcity.shared;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class rackStarRatings {

	// latlng are not serialized, all convert to string, require convert back. by splitting comma and A[0] = lat, A[1] = lon
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String uidaddress;
	@Persistent
	private String uid;
	@Persistent
	private String Address;
	@Persistent
	private String Position;
	@Persistent
	private int rating;
	@Persistent
	private Date createDate;
	
	//a good way to use constructor
		public rackStarRatings() {
			this.createDate = new Date();
		}
	 
	
	public rackStarRatings(String id, String address, String position, int rate)
	{
		this();
		this.uid = id;
		this.Address =  address;
		this.Position = position;
		this.rating = rate;
		this.uidaddress = address.concat(id);
	}
	
	public String getUid()
	{
		return this.uid;
	}
	public String getAddress()
	{
		return this.Address;
	}
	public String getPosition()
	{
		return this.Position;
	}
	
	public String getUidAddress()
	{
		return this.uidaddress;
	}
	
	public int getRating()
	{
		return this.rating;
	}
	
	public void setRating(int r)
	{
		this.rating = r;
	}

}
