package de.larssh.utils.text;

import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.UtilityClass;

/**
 * This class contains helper methods for decoding and encoding strings.
 */
@UtilityClass
public class StringConverters {
	/**
	 * Decodes {@code value} as Base64 string.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getDecoder()} with the char
	 * set {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be decoded
	 * @return the decoded value
	 */
	public static String decodeBase64(final String value) {
		return new String(Base64.getDecoder().decode(value), Strings.DEFAULT_CHARSET);
	}

	/**
	 * Decodes {@code value} using the Base64 MIME type decoding scheme.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getMimeDecoder()} with the
	 * char set {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be decoded
	 * @return the decoded value
	 */
	public static String decodeBase64Mime(final String value) {
		return new String(Base64.getMimeDecoder().decode(value), Strings.DEFAULT_CHARSET);
	}

	/**
	 * Decodes {@code value} as Base64 URL and filename-safe string.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getUrlDecoder()} with the
	 * char set {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be decoded
	 * @return the decoded value
	 */
	public static String decodeBase64Url(final String value) {
		return new String(Base64.getUrlDecoder().decode(value), Strings.DEFAULT_CHARSET);
	}

	/**
	 * Decodes {@code value} as single CSV value if {@code value} starts with
	 * {@code escaper}. Otherwise {@code value} is returned as-is.
	 *
	 * <p>
	 * <b>Examples:</b>
	 * <table>
	 * <caption>Examples</caption>
	 * <tr>
	 * <th>Parameter</th>
	 * <th>Return Value</th>
	 * </tr>
	 * <tr>
	 * <td>EMPTY</td>
	 * <td>EMPTY</td>
	 * </tr>
	 * <tr>
	 * <td>abc</td>
	 * <td>abc</td>
	 * </tr>
	 * <tr>
	 * <td>""</td>
	 * <td>EMPTY</td>
	 * </tr>
	 * <tr>
	 * <td>"abc"</td>
	 * <td>abc</td>
	 * </tr>
	 * <tr>
	 * <td>"abc""xyz"</td>
	 * <td>abc"xyz</td>
	 * </tr>
	 * <tr>
	 * <td>""""</td>
	 * <td>"</td>
	 * </tr>
	 * </table>
	 *
	 * @param value   the value to be decoded
	 * @param escaper the escaping character
	 * @return the decoded value
	 * @throws ParseException if the CSV value ends unexpectedly or some occurrence
	 *                        of {@code escaper} is not escaped correctly
	 */
	@SuppressWarnings("PMD.CyclomaticComplexity")
	public static String decodeCsv(final String value, final char escaper) throws ParseException {
		if (value.isEmpty() || value.charAt(0) != escaper) {
			return value;
		}

		final int length = value.length();
		if (length < 2) {
			throw new ParseException(
					"Value starts with the escape character. Expected a matching trailing character, but found none.");
		}
		if (value.charAt(length - 1) != escaper) {
			throw new ParseException(
					"Value starts with the escape character. Expected a matching trailing character, but found [%s].",
					value.charAt(length - 1));
		}

		int index = 1;
		final StringBuilder builder = new StringBuilder(length - 2);
		while (index < length - 1) {
			final char character = value.charAt(index);
			if (character == escaper) {
				index += 1;
				if (index >= length - 1) {
					throw new ParseException("Unexpected end after escape character in [%s].", value);
				}
				if (value.charAt(index) != escaper) {
					throw new ParseException("Unexpected character \"%s\" at index %d after escape character.",
							value.charAt(index),
							index);
				}
			}
			builder.append(character);
			index += 1;
		}
		return builder.length() == length ? value : builder.toString();
	}

	/**
	 * Decodes an {@code application/x-www-form-urlencoded} string.
	 *
	 * <p>
	 * This method is equivalent to using {@link URLDecoder#decode(String)} with the
	 * char set {@link StandardCharsets#UTF_8} as the
	 * <a href= "https://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
	 * World Wide Web Consortium Recommendation</a> states that UTF-8 should be
	 * used.
	 *
	 * @param value the value to be decoded
	 * @return the decoded value
	 * @throws ParseException on illegal or incomplete hex characters
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting checked to unchecked exception, that should never be thrown at all")
	public static String decodeUrl(final String value) throws ParseException {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
		} catch (final IllegalArgumentException e) {
			throw new ParseException(e, "Failed decoding URL.");
		} catch (final UnsupportedEncodingException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Translates {@code value} into Base64 format.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getEncoder()} with the char
	 * set {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be encoded
	 * @return the encoded value
	 */
	public static String encodeBase64(final String value) {
		return new String(Base64.getEncoder().encode(value.getBytes(Strings.DEFAULT_CHARSET)), Strings.DEFAULT_CHARSET);
	}

	/**
	 * Translates {@code value} into Base64 MIME type encoding scheme.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getMimeEncoder()} with the
	 * char set {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be encoded
	 * @return the encoded value
	 */
	public static String encodeBase64Mime(final String value) {
		return new String(Base64.getMimeEncoder().encode(value.getBytes(Strings.DEFAULT_CHARSET)),
				Strings.DEFAULT_CHARSET);
	}

	/**
	 * Translates {@code value} into Base64 URL and filename-safe format.
	 *
	 * <p>
	 * This method is equivalent to using {@link Base64#getUrlEncoder()} with the
	 * charset {@link Strings#DEFAULT_CHARSET}.
	 *
	 * @param value the value to be encoded
	 * @return the encoded value
	 */
	public static String encodeBase64Url(final String value) {
		return new String(Base64.getUrlEncoder().encode(value.getBytes(Strings.DEFAULT_CHARSET)),
				Strings.DEFAULT_CHARSET);
	}

	/**
	 * Encodes {@code value} into a single CSV value. All occurrences of
	 * {@code escaper} are escaped (doubled). If {@code value} contains
	 * inappropriate characters, it is surrounded by {@code escaper}.
	 *
	 * <p>
	 * Inappropriate characters are {@code '\r'}, {@code '\n'}, {@code escaper} and
	 * {@code separator}. The latter is used for the list of inappropriate
	 * characters only.
	 *
	 * <p>
	 * <b>Examples:</b>
	 * <table>
	 * <caption>Examples</caption>
	 * <tr>
	 * <th>Parameter</th>
	 * <th>Return Value</th>
	 * </tr>
	 * <tr>
	 * <td>EMPTY</td>
	 * <td>EMPTY</td>
	 * </tr>
	 * <tr>
	 * <td>abc</td>
	 * <td>abc</td>
	 * </tr>
	 * <tr>
	 * <td>abc"xyz</td>
	 * <td>"abc""xyz"</td>
	 * </tr>
	 * <tr>
	 * <td>"</td>
	 * <td>""""</td>
	 * </tr>
	 * </table>
	 *
	 * @param value     the value to be encoded
	 * @param escaper   the escaping character
	 * @param separator the separator character
	 * @return the encoded value
	 */
	@SuppressWarnings("PMD.CyclomaticComplexity")
	public static String encodeCsv(final String value, final char escaper, final char separator) {
		if (escaper == separator) {
			throw new IllegalArgumentException("Escape character and separator must not be equal.");
		}

		final int length = value.length();
		final StringBuilder builder = new StringBuilder(length);

		builder.append(escaper);

		int beginIndex = 0;
		boolean changed = false;
		for (int index = 0; index < length; index += 1) {
			final char character = value.charAt(index);
			if (character == escaper) {
				if (beginIndex < index) {
					builder.append(value, beginIndex, index);
				}
				beginIndex = index + 1;

				builder.append(escaper).append(escaper);
				changed = true;
			} else if (character == separator || character == '\r' || character == '\n') {
				changed = true;
			}
		}
		if (!changed) {
			return value;
		}
		if (beginIndex < length) {
			builder.append(value, beginIndex, length);
		}
		return builder.append(escaper).toString();
	}

	/**
	 * Translates a string into {@code application/x-www-form-urlencoded} format
	 * with the char set {@link StandardCharsets#UTF_8} as the
	 * <a href= "https://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
	 * World Wide Web Consortium Recommendation</a> states that UTF-8 should be
	 * used.
	 *
	 * @param value the value to be encoded
	 * @return the encoded value
	 */
	@SuppressFBWarnings(value = "EXS_EXCEPTION_SOFTENING_NO_CONSTRAINTS",
			justification = "converting checked to unchecked exception, that should never be thrown at all")
	public static String encodeUrl(final String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
		} catch (final UnsupportedEncodingException e) {
			throw new UncheckedIOException(e);
		}
	}
}
