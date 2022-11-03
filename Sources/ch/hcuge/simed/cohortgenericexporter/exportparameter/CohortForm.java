/**
 * 
 */
package ch.hcuge.simed.cohortgenericexporter.exportparameter;

import java.math.BigInteger;

import ch.hcuge.simed.cohortgenericexporter.utilities.ExporterConstante;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSSet;

/**
 * @author dban
 * 
 *         contains the data representing a form to export
 * 
 *         json look like this
 * 
 *         <pre>
 * {
 * 	"name": "STCS_001",
 * 	"version": 1,
 * 	"useVersionInName": false,
 * 	"externalName": "MonbeauNom",
 * 	"fields":[] see CohortField
 * }
 * </pre>
 * 
 */
public class CohortForm extends NSMutableDictionary<String, Object> {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Mandatory Key
	 */
	public static final String NAME_KEY = "name";
	public static final String FIELDS_KEY = "fields";
	public static final String VERSION_KEY = "version";

	/*
	 * Optional keys
	 */
	public static final String EXTERNAL_NAME_KEY = "externalName";
	public static final String DOCUMENTATION_KEY = "documentation";
	public static final String SOURCE_KEY = "source";
	public static final String DATASETLINK_KEY = "datasetlink";
	public static final String CLEANERS_KEY = "cleaners";
	
	/*
	 * Validation Set
	 */
	public static final NSSet<String> MANDATORY_KEY = new NSSet<String>(new String[] { NAME_KEY, FIELDS_KEY, VERSION_KEY });
	public static final NSSet<String> OPTIONAL_KEY = new NSSet<String>(new String[] { EXTERNAL_NAME_KEY, DOCUMENTATION_KEY ,SOURCE_KEY,DATASETLINK_KEY, CLEANERS_KEY});
	public static final NSSet<String> ALL_KNOWN_KEY = MANDATORY_KEY.setByUnioningSet(OPTIONAL_KEY);
	public static final Boolean ALLOW_UNKNOWN_KEY = Boolean.FALSE;

	/*
	 * Constructor
	 */
	public CohortForm(String name, BigInteger version, NSArray<CohortField> fields) {
		this.takeValueForKey(name, NAME_KEY);
		this.takeValueForKey(version, VERSION_KEY);
		this.takeValueForKey(fields, FIELDS_KEY);
	}

	/*
	 * Getter
	 */
	public String name() {
		return (String) this.valueForKey(NAME_KEY);
	}

	@SuppressWarnings("unchecked")
	public NSArray<CohortField> fields() {
		return (NSArray<CohortField>) this.valueForKey(FIELDS_KEY);
	}

	public BigInteger version() {
		return (BigInteger) this.valueForKey(VERSION_KEY);
	}

	/*
	 * Accessor
	 */

	public String externalName() {
		return (String) this.valueForKey(EXTERNAL_NAME_KEY);
	}

	public void setExternalName(String newExternalName) {
		this.takeValueForKey(newExternalName, EXTERNAL_NAME_KEY);
	}

	public String nameWithVersion() {
		return this.name() + " v(" + this.version().toString() + ")";
	}
	public String source()
	{
		return (String)this.valueForKey(SOURCE_KEY);
	}
	public void setSource(String newSource)
	{
		this.takeValueForKey(newSource, SOURCE_KEY);
	}
	
	public String databaselink()
	{
		return (String)this.valueForKey(DATASETLINK_KEY);
	}
	public void setDatabaselink(String newDatabaselink)
	{
		this.takeValueForKey(newDatabaselink, DATASETLINK_KEY);
	}
	
	public NSArray<String> cleaners()
	{
		return (NSArray<String>) this.valueForKey(CLEANERS_KEY);
	}
	public void setCleaners(NSMutableArray<String> newCleaners)
	{
		this.takeValueForKey(newCleaners, CLEANERS_KEY);
	}
	
	/*
	 * Utility method
	 */
	public String versionAsString() {
		if (version() == null) {
			return ExporterConstante.ONE;
		}
		return version().toString();

	}
}
