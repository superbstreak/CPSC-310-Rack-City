package cs310MRAK.rackcity.shared;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FavoriteRack{
	
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
	private Date createDate;
	
	//a good way to use constructor
		public FavoriteRack() {
			this.createDate = new Date();
		}
	 
	
	public FavoriteRack(String id, String address, String position)
	{
		this();
		this.uid = id;
		this.Address =  address;
		this.Position = position;
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
	
}