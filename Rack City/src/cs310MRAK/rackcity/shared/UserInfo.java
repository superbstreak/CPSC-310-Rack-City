package cs310MRAK.rackcity.shared;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7716652380384771953L;
	
	
	// latlng are not serialized, all convert to string, require convert back. by splitting comma and A[0] = lat, A[1] = lon
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String Id;
	@Persistent
	private String Name;
	@Persistent
	private String Email;
	@Persistent
	private String Gender;
	@Persistent 
	private Boolean isPlus;
	@Persistent
	private String proPicURL;
	@Persistent
	private String favBike;
	@Persistent
	private String bikeName;
	@Persistent
	private String bikeColor;
	
	//a good way to use constructor
	public UserInfo() {
		
		}
	 
	public UserInfo(String id, String name, String email, String gender, Boolean isplus, String url, String favbike, String bikename, String bikecolor)
	{
		this();
		this.Id = id;
		this.Name = name;
		this.Email = email;
		this.Gender = gender;
		this.isPlus = isplus;
		this.proPicURL = url;
		this.favBike = favbike;
		this.bikeName = bikename;
		this.bikeColor = bikecolor;
	}
	
	public String getFavBike()
	{
		return this.favBike;
	}
	public void setFavBike(String f)
	{
		this.favBike = f;
	}
	
	public String getbikeColor()
	{
		return this.bikeColor;
	}
	public void setbikeColor(String bc)
	{
		this.bikeColor = bc;
	}
	
	public String getbikeName()
	{
		return this.bikeName;
	}
	public void setBikeName(String b)
	{
		this.bikeName = b;
	}
	
	public String getId()
	{
		return this.Id;
	}
	public void setId(String id)
	{
		this.Id = id;
	}
	
	public String getName()
	{
		return this.Name;
	}
	public void setName(String name)
	{
		this.Name = name;
	}
	
	public String getEmail()
	{
		return this.Email;
	}
	public void setEmail(String email)
	{
		this.Email = email;
	}
	
	public String getGender()
	{
		return this.Gender;
	}
	public void setGender(String gender)
	{
		this.Id = gender;
	}
	
	public Boolean getisPlus()
	{
		return this.isPlus;
	}
	public void setisPlus(Boolean status)
	{
		this.isPlus = status;
	}
	
	public String getProfilePicURL()
	{
		return this.proPicURL;
	}
	public void setProfilePicURL(String url)
	{
		this.proPicURL = url;
	}
}