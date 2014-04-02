package cs310MRAK.rackcity.server;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RackExperience {

	// latlng are not serialized, all convert to string, require convert back. by splitting comma and A[0] = lat, A[1] = lon
		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private String pos;
		@Persistent
		private String experience;

		
		 
		  
		 
		public RackExperience(String pos, String e)
		{
			this.pos = pos;
			this.experience = e;
		}
  
 
 
  
 
		public String getPos() {
			return pos;
		}
 
  
 
 
 
		public void setPos(String pos) {
			this.pos = pos;
		}
 
 
    
 
 
		public String getExperience() {
			return experience;
		}
 
 
 

 
		public void setExperience(String experience) {
			this.experience = experience;
		}
 		
  		
 	
}
 