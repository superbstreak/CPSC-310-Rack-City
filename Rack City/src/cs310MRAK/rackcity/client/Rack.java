package cs310MRAK.rackcity.client;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Rack{
	
	// latlng are not serialized, all convert to string, require convert back. by splitting comma and A[0] = lat, A[1] = lon
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String pos;
	@Persistent
	private String addr;
	@Persistent
	private int numR;
	@Persistent
	private int stel;
	@Persistent
	private double css;
	@Persistent
	private double rat;
	@Persistent
	private Date createDate;
	
	//a good way to use constructor
		public Rack() {
			this.createDate = new Date();
		}
	 
	
	public Rack(String adr, String p, int nR, int stl, double c, double r)
	{
		this();
		this.addr = adr;
		this.pos = p;
		this.numR = nR;
		this.stel = stl;
		this.css = c;
		this.rat = r;
	}
	
	//======== Address
	public String getAddr()
	{
		return this.addr;
	}
	
	public void setAddr(String s)
	{
		this.addr = s;
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
	
	//========= Rack #
	public int getRnum()
	{
		return this.numR;
	}
	
	public void setRnum(int r)
	{
		this.numR = r;
	}
	
	//========= STOLEN ===========
	public int getStolen()
	{
		return this.stel;
	}
	
	public void setStolen(int s)
	{
		this.stel = s;
	}
	
	//======== CSS ===============
	public double getCS()
	{
		return this.css;
	}
	
	public void setCS(double c)
	{
		this.css = c;
	}
	
	//======== rating ============
	public double getRating()
	{
		return this.rat;
	}
	
	public void setRating(double r)
	{
		this.rat = r;
	}
	
}
