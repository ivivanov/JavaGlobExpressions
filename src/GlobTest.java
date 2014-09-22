import junit.framework.TestCase;

public class GlobTest extends TestCase {
	public void testQestionMark() {
		Glob glob = Glob.compile("abc?");
		assertFalse(glob.matches("abc")); // false => ? means exactly one
		// character
		assertTrue(glob.matches("abcd")); // true
		assertFalse(glob.matches("abcde"));
	}

	public void testAsterix() {
		Glob glob2 = Glob.compile("a*d");
		assertTrue(glob2.matches("abcd"));
		assertTrue(glob2
				.matches("abcujawiohtguahwuthawitthawuithawuthaithawtawutd"));
		assertFalse(glob2.matches("abcdej"));
		assertFalse(glob2
				.matches("abcujawiohtguahwuthawitthawuithawuthaithawtawutd1"));
	}

	public void testAsterixDotSth() {
		Glob glob3 = Glob.compile("*.html");
		assertTrue(glob3.matches("index.html"));
		assertFalse(glob3.matches("index.htm")); // false - missing 'l'
		// assertFalse(glob3.matches("directory/index.html"));
		// false -crossing directory boundaries
	}

	public void testBrackets() {
		Glob glob4 = Glob.compile("Di{nko,mitur}");
		assertTrue(glob4.matches("Dimitur"));
		assertTrue(glob4.matches("Dinko"));
		assertFalse(glob4.matches("Divna"));
	}

	public void testDoubleAsterix() {
		Glob glob5 = Glob.compile("/home/georgi/**index.html");
		assertTrue(glob5.matches("/home/georgi/testme/testme2/index.html"));
		assertTrue(glob5
				.matches("/home/georgi/testme/testme2/testME_index.html"));
		assertFalse(glob5.matches("/home/index.html"));
	}

	public void testContainsWord(){
		Glob glob = Glob.compile("*List*.java");
		assertTrue(glob.matches("vListaNaPesho.java"));
		assertTrue(glob.matches("ListaNaPesho.java"));
		assertTrue(glob.matches("vList.java"));
		assertFalse(glob.matches("asfLisAasf.java"));	
	}
}
