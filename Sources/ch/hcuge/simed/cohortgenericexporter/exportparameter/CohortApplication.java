/**
 * 
 */
package ch.hcuge.simed.cohortgenericexporter.exportparameter;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSSet;

/**
 * @author dban
 * 
 *         contains the data representing a cohort application
 * 
 *         json look like this
 * 
 *         <pre>
 * {
 * 	"name": "STCS",
 * 	"dburl": "",
 * 	"dblogin": "",
 * 	"dbpwd": "",
 * 	"forms": [] see CohortForm
 * }
 * </pre>
 * 
 */
public class CohortApplication extends NSMutableDictionary<String, Object> {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Mandatory Key
	 */
	public static final String NAME_KEY = "name";
	public static final String FORMS_KEY = "forms";
	public static final String DB_URL_KEY = "dburl";
	public static final String DB_LOGIN_KEY = "dblogin";
	public static final String DB_PWD_KEY = "dbpwd";
	public static final String CLASS_LOC_KEY = "classLocations";
	public static final String EXPORT_DESTINATION = "exportDestination";

	/*
	 * Optional keys
	 */
	public static final String DOCUMENTATION_KEY = "documentation";
	public static final String PRE_OPERATION_KEY = "preOperation";
	public static final String POST_OPERATION_KEY = "postOperation";
	public static final String USE_FILTERS_KEY = "useFilters";
	public static final String FORMS_DATA_KEY = "formsData";
	public static final String CLEANERS_KEY = "cleaners";
	/*
	 * Validation Set
	 */
	public static final NSSet<String> MANDATORY_KEY = new NSSet<String>(new String[] { NAME_KEY, FORMS_KEY, DB_URL_KEY, DB_LOGIN_KEY, DB_PWD_KEY,CLASS_LOC_KEY,EXPORT_DESTINATION });
	public static final NSSet<String> OPTIONAL_KEY = new NSSet<String>(
			new String[] { PRE_OPERATION_KEY, POST_OPERATION_KEY, USE_FILTERS_KEY, DOCUMENTATION_KEY,CLEANERS_KEY });
	public static final NSSet<String> ALL_KNOWN_KEY = MANDATORY_KEY.setByUnioningSet(OPTIONAL_KEY);
	public static final Boolean ALLOW_UNKNOWN_KEY = Boolean.FALSE;

	/*
	 * Constructor
	 */
	public CohortApplication(String name, 
			String dbUrl, 
			String dbLogin, 
			String dbPwd, 
			String classLocations, 
			String exportDestination, 
			NSArray<CohortForm> formsData) {
		this.takeValueForKey(name, NAME_KEY);
		this.takeValueForKey(dbUrl, DB_URL_KEY);
		this.takeValueForKey(dbLogin, DB_LOGIN_KEY);
		this.takeValueForKey(dbPwd, DB_PWD_KEY);
		this.takeValueForKey(classLocations, CLASS_LOC_KEY);
		this.takeValueForKey(exportDestination, EXPORT_DESTINATION);
		this.takeValueForKey(formsData, FORMS_DATA_KEY);
		this.takeValueForKey(new NSMutableArray<String>(),CLEANERS_KEY);
	}

	/*
	 * Getter
	 */
	public String name() {
		return (String) this.valueForKey(NAME_KEY);
	}

	public String exportDestination() {
		return (String) this.valueForKey(EXPORT_DESTINATION);
	}
	
	public String dbUrl() {
		return (String) this.valueForKey(DB_URL_KEY);
	}

	public String dbLogin() {
		return (String) this.valueForKey(DB_LOGIN_KEY);
	}

	public String dbPwd() {
		return (String) this.valueForKey(DB_PWD_KEY);
	}
	
	public String classLocations() {
		return (String) this.valueForKey(CLASS_LOC_KEY);
	}

	@SuppressWarnings("unchecked")
	public NSArray<String> forms() {
		return (NSArray<String>) this.valueForKey(FORMS_KEY);
	}

	public Boolean useFilters() {
		Object result = this.valueForKey(USE_FILTERS_KEY);
		if (result == null) {
			return Boolean.FALSE;
		}
		return (Boolean) result;
	}

	/*
	 * Setter and getter
	 */
	@SuppressWarnings("unchecked")
	public NSArray<String> preOperation() {
		return (NSArray<String>) this.valueForKey(PRE_OPERATION_KEY);
	}

	@SuppressWarnings("unchecked")
	public NSArray<String> postOperation() {
		return (NSArray<String>) this.valueForKey(POST_OPERATION_KEY);
	}

	@SuppressWarnings("unchecked")
	public NSArray<CohortForm> formsData() {
		return (NSArray<CohortForm>) this.valueForKey(FORMS_DATA_KEY);
	}
	
	@SuppressWarnings("unchecked")
	public NSArray<String> cleaners() {
		return (NSArray<String>) this.valueForKey(CLEANERS_KEY);
	}

	public void setUseFilters(Boolean shouldFilterData) {
		this.takeValueForKey(shouldFilterData, USE_FILTERS_KEY);
	}

	public void setPreOperation(NSArray<String> newPreOperation) {
		this.takeValueForKey(newPreOperation, PRE_OPERATION_KEY);
	}

	public void setPostOperation(NSArray<String> newPostOperation) {
		this.takeValueForKey(newPostOperation, POST_OPERATION_KEY);
	}
	
	public void setCleaners(NSArray<String> cleaners) {
		this.takeValueForKey(cleaners, CLEANERS_KEY);
	}
	
	public void setExportDestination(String exportDestination) {
		this.takeValueForKey(exportDestination, EXPORT_DESTINATION);
	}

}
