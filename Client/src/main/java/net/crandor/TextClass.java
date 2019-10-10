package net.crandor;

final class TextClass {

	private static final char[] validChars = {'_', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9'};
    private static final StringBuffer stringbuffer = new StringBuffer();

    public static long longForName(String s) {
        long l = 0L;
        for (int i = 0; i < s.length() && i < 12; i++) {
            char c = s.charAt(i);
            l *= 37L;
            if (c >= 'A' && c <= 'Z')
                l += (1 + c) - 65;
            else if (c >= 'a' && c <= 'z')
                l += (1 + c) - 97;
            else if (c >= '0' && c <= '9')
                l += (27 + c) - 48;
        }

        for (; l % 37L == 0L && l != 0L; l /= 37L)
            ;
        return l;
    }

	public static String nameForLong(long l) {
		if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
			return "invalid_name";
		if (l % 37L == 0L)
			return "invalid_name";
		int i = 0;
		char ac[] = new char[12];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = validChars[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

    public static long method585(String s) {
        s = s.toUpperCase();
        long l = 0L;
        for (int i = 0; i < s.length(); i++) {
            l = (l * 61L + s.charAt(i)) - 32L;
            l = l + (l >> 56) & 0xffffffffffffffL;
        }
        return l;
    }

    public static String decodeDNS(int i) {
        return (i >> 24 & 0xff) + "." + (i >> 16 & 0xff) + "."
                + (i >> 8 & 0xff) + "." + (i & 0xff);
    }

    public static String fixName(String s) {
        if (s.length() > 0) {
            char ac[] = s.toCharArray();
            for (int j = 0; j < ac.length; j++)
                if (ac[j] == '_') {
                    ac[j] = ' ';
                    if (j + 1 < ac.length && ac[j + 1] >= 'a'
                            && ac[j + 1] <= 'z')
                        ac[j + 1] = (char) ((ac[j + 1] + 65) - 97);
                }

            if (ac[0] >= 'a' && ac[0] <= 'z')
                ac[0] = (char) ((ac[0] + 65) - 97);
            return new String(ac);
        } else {
            return s;
        }
    }

	public static String toSentence(String string) {
		char[] chars = string.toLowerCase().toCharArray();

		boolean capitalize = true;
		for (int n = 0; n < chars.length; n++) {
			char c = chars[n];

			if (capitalize && c >= 'a' && c <= 'z') {
				chars[n] -= 32;
				capitalize = false;
			}

			if (c == '.' || c == '!') {
				capitalize = true;
			}
		}
		return new String(chars);
	}

	public static String convertMarkup(String text) {
		text = text.replaceAll("@red@", "<col=ff0000>");
		text = text.replaceAll("@gre@", "<col=00ff00>");
		text = text.replaceAll("@blu@", "<col=255>");
		text = text.replaceAll("@yel@", "<col=ffff00>");
		text = text.replaceAll("@cya@", "<col=65535>");
		text = text.replaceAll("@mag@", "<col=ff00ff>");
		text = text.replaceAll("@whi@", "<col=ffffff>");
		text = text.replaceAll("@lre@", "<col=ff9040>");
		text = text.replaceAll("@dre@", "<col=800000>");
		text = text.replaceAll("@bla@", "<col=0>");
		text = text.replaceAll("@or1@", "<col=ffb000>");
		text = text.replaceAll("@or2@", "<col=ff7000>");
		text = text.replaceAll("@or3@", "<col=ff3000>");
		text = text.replaceAll("@gr1@", "<col=c0ff00>");
		text = text.replaceAll("@gr2@", "<col=80ff00>");
		text = text.replaceAll("@gr3@", "<col=40ff00>");
		return text;
	}

    public static String passwordAsterisks(String s) {
        stringbuffer.setLength(0);
        for (int j = 0; j < s.length(); j++)
            stringbuffer.append("*");
        return stringbuffer.toString();
    }

}
