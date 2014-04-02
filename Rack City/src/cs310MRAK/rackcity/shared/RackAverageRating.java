package cs310MRAK.rackcity.shared;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RackAverageRating {
	// latlng are not serialized, all convert to string, require convert back. by splitting comma and A[0] = lat, A[1] = lon
			@PrimaryKey
			@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
			private String pos; // RackID
			@Persistent
			private double averageRating;
			@Persistent
			private int count;
			
			public RackAverageRating(String pos, double averageRating, int count)
			{
				this.pos = pos;
				this.averageRating = averageRating;
				this.count = count;
			}

			public String getPos() {
				return pos;
			}

			public void setPos(String pos) {
				this.pos = pos;
			}

			public double getAverageRating() {
				return averageRating;
			}

			public void setAverageRating(double averageRating) {
				this.averageRating = averageRating;
			}

			public int getCount() {
				return count;
			}

			public void setCount(int count) {
				this.count = count;
			}
	
}
