package cs310MRAK.rackcity.server;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Crime{
	
	// latlng are not serialized, all convert to string, require convert back. by splitting comma and A[0] = lat, A[1] = lon
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String pos;
	@Persistent
	private int year;
	@Persistent
	private Date createDate;
	
	//a good way to use constructor
		public Crime() {
			this.createDate = new Date();
		}
	 
	
	public Crime(int year, String p)
	{
		this();
		this.year = year;
		this.pos = p;
	}
	
	//======== Address
	public int getYear()
	{
		return this.year;
	}
	
	public void setYear(int y)
	{
		this.year = y;
	}
	
	//========= LAtLng
	public String getLL()
	{
		return this.pos;
	}
	
	public void setLL(String p)
	{
		this.pos = p;
	}
	
}
