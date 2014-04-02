package cs310MRAK.rackcity.shared;


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
		private String posUid;
		@Persistent
		private String uid;
		@Persistent
		private String pos;
		@Persistent
		private String experience;

		
		 
		  
		 
		public RackExperience(String pos, String e, String uid)
		{
			this.pos = pos;
			this.experience = e;
			this.uid = uid;
			this.posUid = pos.concat(uid);
		}
  
 
 
  
 
		public String getPos() {
			return pos;
		}
 
  
 
 
 
		public void setPos(String pos) {
			this.pos = pos;
		}
 
 
		
		
		public String getUid() {
			return uid;
		}
 
  
 
 
		public void setUid(String uid) {
			this.pos = uid;
		}
    
 
 
		public String getExperience() {
			return experience;
		}
 
 
 

 
		public void setExperience(String experience) {
			this.experience = experience;
		}
 		
  		
 	
}
 