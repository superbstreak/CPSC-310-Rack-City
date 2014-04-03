package cs310MRAK.rackcity.shared;


// yes I know it looks silly..
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class BikeRackTimeHits {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private	String	pos;
	@Persistent
	private	int	interval0to1;
	@Persistent
	private	int	interval1to2;
	@Persistent
	private	int	interval2to3;
	@Persistent
	private	int	interval3to4;
	@Persistent
	private	int	interval4to5;
	@Persistent
	private	int	interval5to6;
	@Persistent
	private	int	interval6to7;
	@Persistent
	private	int	interval7to8;
	@Persistent
	private	int	interval8to9;
	@Persistent
	private	int	interval9to10;
	@Persistent
	private	int	interval10to11;
	@Persistent
	private	int	interval11to12;
	@Persistent
	private	int	interval12to13;
	@Persistent
	private	int	interval13to14;
	@Persistent
	private	int	interval14to15;
	@Persistent
	private	int	interval15to16;
	@Persistent
	private	int	interval16to17;
	@Persistent
	private	int	interval17to18;
	@Persistent
	private	int	interval18to19;
	@Persistent
	private	int	interval19to20;
	@Persistent
	private	int	interval20to21;
	@Persistent
	private	int	interval21to22;
	@Persistent
	private	int	interval22to23;
	@Persistent
	private	int	interval23to24;

	public BikeRackTimeHits(String pos, 
			int	interval0to1,
			int	interval1to2,
			int	interval2to3,
			int	interval3to4,
			int	interval4to5,
			int	interval5to6,
			int	interval6to7,
			int	interval7to8,
			int	interval8to9,
			int	interval9to10,
			int	interval10to11,
			int	interval11to12,
			int	interval12to13,
			int	interval13to14,
			int	interval14to15,
			int	interval15to16,
			int	interval16to17,
			int	interval17to18,
			int	interval18to19,
			int	interval19to20,
			int	interval20to21,
			int	interval21to22,
			int	interval22to23,
			int	interval23to24)
	{
		this.pos = pos;
		this.interval0to1 = interval0to1;
		this.interval1to2 = interval1to2;
		this.interval2to3 = interval2to3;
		this.interval3to4 = interval3to4;
		this.interval4to5 = interval4to5;
		this.interval5to6 = interval5to6;
		this.interval6to7 = interval6to7;
		this.interval7to8 = interval7to8;
		this.interval8to9 = interval8to9;
		this.interval9to10 = interval9to10;
		this.interval10to11 = interval10to11;
		this.interval11to12 = interval11to12;
		this.interval12to13 = interval12to13;
		this.interval13to14 = interval13to14;
		this.interval14to15 = interval14to15;
		this.interval15to16 = interval15to16;
		this.interval16to17 = interval16to17;
		this.interval17to18 = interval17to18;
		this.interval18to19 = interval18to19;
		this.interval19to20 = interval19to20;
		this.interval20to21 = interval20to21;
		this.interval21to22 = interval21to22;
		this.interval22to23 = interval22to23;
		this.interval23to24 = interval23to24;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public int getInterval0to1() {
		return interval0to1;
	}

	public void setInterval0to1(int interval0to1) {
		this.interval0to1 = interval0to1;
	}

	public int getInterval1to2() {
		return interval1to2;
	}

	public void setInterval1to2(int interval1to2) {
		this.interval1to2 = interval1to2;
	}

	public int getInterval2to3() {
		return interval2to3;
	}

	public void setInterval2to3(int interval2to3) {
		this.interval2to3 = interval2to3;
	}

	public int getInterval3to4() {
		return interval3to4;
	}

	public void setInterval3to4(int interval3to4) {
		this.interval3to4 = interval3to4;
	}

	public int getInterval4to5() {
		return interval4to5;
	}

	public void setInterval4to5(int interval4to5) {
		this.interval4to5 = interval4to5;
	}

	public int getInterval5to6() {
		return interval5to6;
	}

	public void setInterval5to6(int interval5to6) {
		this.interval5to6 = interval5to6;
	}

	public int getInterval6to7() {
		return interval6to7;
	}

	public void setInterval6to7(int interval6to7) {
		this.interval6to7 = interval6to7;
	}

	public int getInterval7to8() {
		return interval7to8;
	}

	public void setInterval7to8(int interval7to8) {
		this.interval7to8 = interval7to8;
	}

	public int getInterval8to9() {
		return interval8to9;
	}

	public void setInterval8to9(int interval8to9) {
		this.interval8to9 = interval8to9;
	}

	public int getInterval9to10() {
		return interval9to10;
	}

	public void setInterval9to10(int interval9to10) {
		this.interval9to10 = interval9to10;
	}

	public int getInterval10to11() {
		return interval10to11;
	}

	public void setInterval10to11(int interval10to11) {
		this.interval10to11 = interval10to11;
	}

	public int getInterval11to12() {
		return interval11to12;
	}

	public void setInterval11to12(int interval11to12) {
		this.interval11to12 = interval11to12;
	}

	public int getInterval12to13() {
		return interval12to13;
	}

	public void setInterval12to13(int interval12to13) {
		this.interval12to13 = interval12to13;
	}

	public int getInterval13to14() {
		return interval13to14;
	}

	public void setInterval13to14(int interval13to14) {
		this.interval13to14 = interval13to14;
	}

	public int getInterval14to15() {
		return interval14to15;
	}

	public void setInterval14to15(int interval14to15) {
		this.interval14to15 = interval14to15;
	}

	public int getInterval15to16() {
		return interval15to16;
	}

	public void setInterval15to16(int interval15to16) {
		this.interval15to16 = interval15to16;
	}

	public int getInterval16to17() {
		return interval16to17;
	}

	public void setInterval16to17(int interval16to17) {
		this.interval16to17 = interval16to17;
	}

	public int getInterval17to18() {
		return interval17to18;
	}

	public void setInterval17to18(int interval17to18) {
		this.interval17to18 = interval17to18;
	}

	public int getInterval18to19() {
		return interval18to19;
	}

	public void setInterval18to19(int interval18to19) {
		this.interval18to19 = interval18to19;
	}

	public int getInterval19to20() {
		return interval19to20;
	}

	public void setInterval19to20(int interval19to20) {
		this.interval19to20 = interval19to20;
	}

	public int getInterval20to21() {
		return interval20to21;
	}

	public void setInterval20to21(int interval20to21) {
		this.interval20to21 = interval20to21;
	}

	public int getInterval21to22() {
		return interval21to22;
	}

	public void setInterval21to22(int interval21to22) {
		this.interval21to22 = interval21to22;
	}

	public int getInterval22to23() {
		return interval22to23;
	}

	public void setInterval22to23(int interval22to23) {
		this.interval22to23 = interval22to23;
	}

	public int getInterval23to24() {
		return interval23to24;
	}

	public void setInterval23to24(int interval23to24) {
		this.interval23to24 = interval23to24;
	}
	
	
	
}
