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
		newRackService.adminConnection("https://dl.dropboxusercontent.com/u/280882377/Book1.xml");
		StringBuffer sbTest = newRackService.sb;
		
		int initialSBSize = sbTest.length();
		
		assertTrue(p.getXMLRackList().size() == 0);
		
		p.parseBikeRacks(sbTest);
		
		int secondSBSize = sbTest.length();
		
		assertTrue(p.getXMLRackList().size() > 0);
		
		assertFalse(initialSBSize == secondSBSize);
		
		int parseSizeA = p.getXMLRackList().size();
		
		System.out.println(Integer.toString(parseSizeA));
		
		p.parseBikeRacks(sbTest);
		
		int finalSBSize = sbTest.length();
		
		assertEquals(secondSBSize, finalSBSize);
		
		assertFalse(p.getXMLRackList().size() > parseSizeA);
		
		int parseSizeB = p.getXMLRackList().size();
		
		System.out.println(Integer.toString(parseSizeB));
	}

}
