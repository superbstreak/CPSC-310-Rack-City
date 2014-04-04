package cs310MRAK.rackcity.parsertests;

import static org.junit.Assert.*;

import org.junit.Test;

import cs310MRAK.rackcity.client.Parser;
import cs310MRAK.rackcity.server.URLserviceImpl;

public class ParserTests {

	@Test
	public void testParseBikeRacks() {
		Parser p = new Parser();

		String urlRack = "https://dl.dropboxusercontent.com/u/280882377/Book1.xml";

		URLserviceImpl newRackService = new URLserviceImpl();
		newRackService.adminConnection(urlRack);
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

	@Test
	public void testParseCrimes() {
		Parser c = new Parser();

		String urlCrime = "https://dl.dropboxusercontent.com/u/280882377/crime_2011.xml";

		URLserviceImpl newCrimeService = new URLserviceImpl();
		newCrimeService.adminConnection(urlCrime);
		StringBuffer sbTestCrime = newCrimeService.sb;

		System.out.println(sbTestCrime.toString());

		int initialSBSize = sbTestCrime.length();

		assertTrue(c.getXMLCrimesList().size() == 0);

		c.parseCrimes(sbTestCrime);


		int secondSBSize = sbTestCrime.length();
		assertTrue(c.getXMLCrimesList().size() > 0);

		assertFalse(initialSBSize == secondSBSize);

		int parseSizeA = c.getXMLCrimesList().size();

		System.out.println(Integer.toString(parseSizeA));

		c.parseCrimes(sbTestCrime);

		int finalSBSize = sbTestCrime.length();

		assertEquals(secondSBSize, finalSBSize);

		assertFalse(c.getXMLCrimesList().size() > parseSizeA);

		int parseSizeB = c.getXMLCrimesList().size();

		System.out.println(Integer.toString(parseSizeB));
	}

}
