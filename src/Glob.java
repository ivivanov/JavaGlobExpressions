import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Glob {

	private static final char BACKSLASH = '\\';
	private static String pattern;
	private static Glob instance;

	public static Glob compile(String input) {
		pattern = convertGlobToRegex(input);

		if (instance == null) {
			instance = new Glob();
		}
		return instance;
	}

	public boolean matches(String input) {
		Boolean result = Pattern.matches(pattern, input);
		return result;
	}

	public static String convertGlobToRegex(String glob) {
		StringBuilder regex = new StringBuilder();
		int setOpen = 0;
		int curlyOpen = 0;
		int len = glob.length();
		Boolean hasWildcard = false;

		for (int i = 0; i < len; i++) {
			char c = glob.charAt(i);

			switch (c) {
			case BACKSLASH:
				regex.append(c).append(glob.charAt(i));
				continue;
			case '.':
			case '$':
			case '(':
			case ')':
			case '|':
			case '+':
				// escape regex special chars that are not glob special chars
				regex.append(BACKSLASH);
				break;
			case '*':
				regex.append('.');
				hasWildcard = true;
				break;
			case '?':
				regex.append('.');
				hasWildcard = true;
				continue;
			case '{': // start of a group
				regex.append("(?:"); // non-capturing
				curlyOpen++;
				hasWildcard = true;
				continue;
			case ',':
				regex.append(curlyOpen > 0 ? '|' : c);
				continue;
			case '}':
				if (curlyOpen > 0) {
					// end of a group
					curlyOpen--;
					regex.append(")");
					continue;
				}
				break;
			case '[':
				setOpen++;
				hasWildcard = true;
				break;
			case '^': // ^ inside [...] can be unescaped
				if (setOpen == 0) {
					regex.append(BACKSLASH);
				}
				break;
			case '!': // [! needs to be translated to [^
				regex.append(setOpen > 0 && '[' == glob.charAt(i - 1) ? '^'
						: '!');
				continue;
			case ']':
				// Many set errors like [][] could not be easily detected here,
				// as []], []-] and [-] are all valid POSIX glob and java regex.
				// We'll just let the regex compiler do the real work.
				setOpen = 0;
				break;
			default:
			}
			regex.append(c);
		}

		return regex.toString();
	}

}
