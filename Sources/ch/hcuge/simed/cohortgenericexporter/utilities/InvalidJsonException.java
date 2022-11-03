/**
 * 
 */
package ch.hcuge.simed.cohortgenericexporter.utilities;

/**
 * @author dban
 * 
 *         Exception thrown when there is someting wrong while parsing the
 *         Export configuration json
 * 
 */
public class InvalidJsonException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public InvalidJsonException() {
	}

	/**
	 * @param message
	 */
	public InvalidJsonException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidJsonException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidJsonException(String message, Throwable cause) {
		super(message, cause);
	}

}
