package util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

/**
 * Helper that takes a byte[] of chars and tries to guess the encoding
 *
 * @author Markus Merzinger, Lingohub
 */
public class EnsureEncoding {
    public static final Charset[] encodingsToTry = new Charset[] { Charset.forName("UTF-8"), Charset.forName("ISO-8859-1"), Charset.forName("UTF-16LE"), Charset.forName("UTF-16BE"), Charset.forName("US-ASCII")};

    public EnsureEncoding() {
    }

    /**
     * will try to convert the given chars to a valid string.
     * @param chars
     *          characters in an unknown charset
     * @return the chars converted to a String decoded by the first matching {@link Charset}
     * @throws Exception
     *           if converting the chars to a String was not possible
     */
    public static String decode(byte[] chars) {
        for (Charset encodingToTry : encodingsToTry) {
            try {
                String content = decode(chars, encodingToTry);
                return content;
            } catch (CharacterCodingException e) {
                // try with next encoding
            }
        }
        return null;
    }

    private static String decode(byte[] chars, Charset encodingToTry) throws CharacterCodingException {
          CharsetDecoder decoder = encodingToTry.newDecoder().onMalformedInput(CodingErrorAction.IGNORE);

           ByteBuffer byteBuffer = ByteBuffer.wrap(chars);
           CharBuffer decoded = decoder.decode(byteBuffer);
           return decoded.toString().replaceAll(Character.toString('\0'), "");
           // return decoded.toString();
    }
}

