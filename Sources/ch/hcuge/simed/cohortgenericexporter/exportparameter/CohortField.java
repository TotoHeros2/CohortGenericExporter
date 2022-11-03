/**
 * 
 */
package ch.hcuge.simed.cohortgenericexporter.exportparameter;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSSet;

/**
 * @author dban
 * 
 *         contains the data representing a specific field to export
 * 
 *         json look like this
 * 
 *         <pre>
 * {
 * 	"name": "patientCode",
 * 	"externalName": "patID",
 * 	"cleaners":["cleaner1Name", "cleaner2Name"], NSArray of String
 * 	"filters":["filer1Name", "filter"Name"], NSArray of String
 * 	"staticValue": "Not Found"
 * }
 * </pre>
 */
public class CohortField extends NSMutableDictionary<String, Object> {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Mandatory Key
	 */
	public static final String NAME_KEY = "name";

	/*
	 * Optional keys
	 */
	public static final String EXTERNAL_NAME_KEY = "externalName";
	public static final String INSTANCE_COUNT_KEY = "instanceCount";
	public static final String CLEANERS_KEY = "cleaners";
	public static final String FILTERS_KEY = "filters";
	public static final String STATIC_VALUE_KEY = "staticValue";
	public static final String DOCUMENTATION_KEY = "documentation";
	public static final String CLASS_KEY = "classAttribute";
	public static final String DUPLICATE_KEY = "allowDuplicateValue";
	// classAttribute, allowDuplicateValue

	/*
	 * Validation Set
	 */
	public static final NSSet<String> MANDATORY_KEY = new NSSet<String>(new String[] { NAME_KEY });
	public static final NSSet<String> OPTIONAL_KEY = new NSSet<String>(new String[] { EXTERNAL_NAME_KEY, CLEANERS_KEY, FILTERS_KEY, STATIC_VALUE_KEY,
			CLASS_KEY, DUPLICATE_KEY, DOCUMENTATION_KEY,INSTANCE_COUNT_KEY });
	public static final NSSet<String> ALL_KNOWN_KEY = MANDATORY_KEY.setByUnioningSet(OPTIONAL_KEY);
	public static final Boolean ALLOW_UNKNOWN_KEY = Boolean.FALSE;
	
	/*
	 * CommonKey
	 */
	public static final String COHORT_ID="cohortID";
	public static final String PATIENT_BIRTHDAY="patientBirthday";
	public static final String PATIENT_SEX="patientSex";
	public static final String CREATION_DATE="creationDate";
	public static final String CREATION_INITIAL="creationInitial";
	public static final String MODIFICATION_DATE="modificationDate";
	public static final String MODIFICATION_INITIAL="modificationInitial";
	public static final String FORM_STATUS="formStatus";
	public static final String FORM_NAME="formName";
	public static final String STCS_OWN_ID="ownID";
	public static final String STCS_ORGAN_ID="organID";
	public static final String CLASS_CODE_DATA ="PFCodeAttribute"; 
	public static final String CLASS_DATE_DATA ="PFDateAttribute"; 
	public static final String CLASS_DECIMAL_DATA ="PFDecimalAttribute"; 
	public static final String CLASS_IMAGE_DATA ="PFImageAttribute"; 
	public static final String CLASS_INTEGER_DATA ="PFIntegerAttribute"; 
	public static final String CLASS_LONG_TEXT_DATA ="PFLongTextAttribute"; 
	public static final String CLASS_SHORT_TEXT_DATA ="PFShortTextAttribute"; 

	/*
	 * Constructor
	 */
	public CohortField(String name) {
		this.takeValueForKey(name, NAME_KEY);
	}

	/*
	 * Getter
	 */
	public String name() {
		return (String) this.valueForKey(NAME_KEY);
	}

	/*
	 * Accessor
	 */
	public String externalName() {
		return (String) this.valueForKey(EXTERNAL_NAME_KEY);
	}

	public void setExternalName(String newExterName) {
		this.takeValueForKey(newExterName, EXTERNAL_NAME_KEY);
	}
	
	public Integer instanceCount() {
		return (Integer) this.valueForKey(INSTANCE_COUNT_KEY);
	}

	public void setInstanceCount(Integer newInstanceCount) {
		this.takeValueForKey(newInstanceCount, INSTANCE_COUNT_KEY);
	}

	@SuppressWarnings("unchecked")
	public NSArray<String> cleaners() {
		return (NSArray<String>) this.valueForKey(CLEANERS_KEY);
	}

	public void setCleaners(NSArray<String> newCleaners) {
		this.takeValueForKey(newCleaners, CLEANERS_KEY);
	}

	@SuppressWarnings("unchecked")
	public NSArray<String> filters() {
		return (NSArray<String>) this.valueForKey(FILTERS_KEY);
	}

	public void setFilters(NSArray<String> newFilters) {
		this.takeValueForKey(newFilters, FILTERS_KEY);
	}

	public String staticValue() {
		return (String) this.valueForKey(STATIC_VALUE_KEY);
	}

	public void setStaticValue(String newStaticValue) {
		this.takeValueForKey(newStaticValue, STATIC_VALUE_KEY);
	}

	public String classValue() {
		return (String) this.valueForKey(CLASS_KEY);
	}

	public void setClassValue(String newClassValue) {
		this.takeValueForKey(newClassValue, CLASS_KEY);
	}

	public Boolean allowDuplicate() {
		return (Boolean) this.valueForKey(DUPLICATE_KEY);
	}

	public void setAllowDuplicate(Boolean newAllowDuplicate) {
		this.takeValueForKey(newAllowDuplicate, DUPLICATE_KEY);
	}
}
