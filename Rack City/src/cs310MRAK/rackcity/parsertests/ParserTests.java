package cs310MRAK.rackcity.parsertests;

import static org.junit.Assert.*;

import org.junit.Test;

import cs310MRAK.rackcity.client.Parser;
import cs310MRAK.rackcity.server.URLserviceImpl;

public class ParserTests {

	@Test
	public void testParseRacks() {
		Parser p = new Parser();
		
		URLserviceImpl newRackService = new URLserviceImpl();
		newRackService.adminConnection();
		StringBuffer sbTest = newRackService.sb;
		
		assertTrue(p.getXMLRackList().size() == 0);
		
		p.parseBikeRacks(sbTest);
		
		assertTrue(p.getXMLRackList().size() > 0);
		
		int parseSizeA = p.getXMLRackList().size();
		
		System.out.println(Integer.toString(parseSizeA));
		
	}

}
