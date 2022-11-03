package ch.hcuge.simed.cohortgenericexporter.utilities;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSSet;

import ch.hcuge.simed.cohortgenericexporter.exportparameter.CohortApplication;
import ch.hcuge.simed.cohortgenericexporter.exportparameter.CohortField;
import ch.hcuge.simed.cohortgenericexporter.exportparameter.CohortForm;
import er.extensions.foundation.ERXPropertyListSerialization;

public class JsonParser {

	private static Logger log = Logger.getLogger(JsonParser.class);

	public static final String DEFAULT_CONFIGURATION_FILE = "Configuration.json";
	public static final String APPLICATIONS_KEY = "Applications";

	@SuppressWarnings("unchecked")
	public static NSArray<CohortApplication> parse(NSDictionary<String, Object> jsonData) throws InvalidJsonException {
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <parse> starting");
		NSMutableArray<CohortApplication> applications = new NSMutableArray<CohortApplication>();
		NSArray<NSDictionary<String, Object>> applicationsData = (NSArray<NSDictionary<String, Object>>) jsonData.valueForKey(APPLICATIONS_KEY);
		for (NSDictionary<String, Object> anApplicationData : applicationsData) {
			applications.addObject(parseApplication(anApplicationData));
		}
		log.warn("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <parse> parsed json data SUCCEED");
		return applications.immutableClone();
	}

	@SuppressWarnings("unchecked")
	private static CohortApplication parseApplication(NSDictionary<String, Object> anApplicationData) throws InvalidJsonException {
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <parseApplication> starting with: '" + anApplicationData.toString() + "'");

		// Validate and Init
		validateKeys(anApplicationData.allKeys(), CohortApplication.MANDATORY_KEY, CohortApplication.ALL_KNOWN_KEY, CohortApplication.ALLOW_UNKNOWN_KEY,
				CohortApplication.class.getName());

		// Get mandatory key
		String name = (String) anApplicationData.valueForKey(CohortApplication.NAME_KEY);
		String dbUrl = (String) anApplicationData.valueForKey(CohortApplication.DB_URL_KEY);
		String dbLogin = (String) anApplicationData.valueForKey(CohortApplication.DB_LOGIN_KEY);
		String dbPwd = (String) anApplicationData.valueForKey(CohortApplication.DB_PWD_KEY);
		String classLocations = (String) anApplicationData.valueForKey(CohortApplication.CLASS_LOC_KEY);
		String exportDestination = (String) anApplicationData.valueForKey(CohortApplication.EXPORT_DESTINATION);
		NSArray<String> formsReferences = (NSArray<String>) anApplicationData.valueForKey(CohortApplication.FORMS_KEY);
		NSMutableArray<CohortForm> formsData = new NSMutableArray<CohortForm>();
		for (String aFormRef : formsReferences) {
			formsData.addObject(parseForm(File.separator + name + File.separator + aFormRef + ".json"));
		}
		NSArray<String> cleaners = (NSArray<String>) anApplicationData.valueForKey(CohortApplication.CLEANERS_KEY);
		
		// get optional key
		Boolean filterData = (Boolean) anApplicationData.valueForKey(CohortApplication.USE_FILTERS_KEY);
		NSArray<String> preOperation = (NSArray<String>) anApplicationData.valueForKey(CohortApplication.PRE_OPERATION_KEY);
		NSArray<String> postOperation = (NSArray<String>) anApplicationData.valueForKey(CohortApplication.POST_OPERATION_KEY);
		// Create CohortApplication
		CohortApplication app = new CohortApplication(name, dbUrl, dbLogin, dbPwd, classLocations, exportDestination, formsData.immutableClone());
		if (filterData != null) {
			app.setUseFilters(filterData);
		}
		if (preOperation != null) {
			app.setPreOperation(preOperation);
		}
		if (postOperation != null) {
			app.setPostOperation(postOperation);
		}
		if (cleaners != null) {
			app.setCleaners(cleaners);
		}
		log.warn("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <parseApplication> succedd parsing an application named: '" + name + "'");
		return app;
	}

	private static CohortForm parseForm(String aFormFileRef) throws InvalidJsonException {
		log.info("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <parseForm> starting with: '" + aFormFileRef + "'");

		// Load data
		NSDictionary<String, Object> aFormData = loadFormData(aFormFileRef);
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <parseForm> jsonData: '" + aFormData.toString() + "'");

		// Validate and Init
		validateKeys(aFormData.allKeys(), CohortForm.MANDATORY_KEY, CohortForm.ALL_KNOWN_KEY, CohortForm.ALLOW_UNKNOWN_KEY, CohortForm.class.getName());
		NSMutableArray<CohortField> fields = new NSMutableArray<CohortField>();

		// get mandatory key
		String name = (String) aFormData.valueForKey(CohortForm.NAME_KEY);
		BigInteger version = (BigInteger) aFormData.valueForKey(CohortForm.VERSION_KEY);
		@SuppressWarnings("unchecked")
		NSArray<NSDictionary<String, Object>> fieldsData = (NSArray<NSDictionary<String, Object>>) aFormData.valueForKey(CohortForm.FIELDS_KEY);
		for (NSDictionary<String, Object> aFieldData : fieldsData) {
			fields.addObject(parseField(aFieldData));
		}

		// Get optional key
		String externalName = (String) aFormData.valueForKey(CohortForm.EXTERNAL_NAME_KEY);
		String source = (String) aFormData.valueForKey(CohortForm.SOURCE_KEY);
		String datasetlink = (String) aFormData.valueForKey(CohortForm.DATASETLINK_KEY);
		NSMutableArray<String> cleaners = (NSMutableArray<String>) aFormData.valueForKey(CohortForm.CLEANERS_KEY);

		// Create CohortForm
		CohortForm form = new CohortForm(name, version, fields);
		if (externalName != null) {
			form.setExternalName(externalName);
		}
		//TODO idem for SOURCE et DATASETLINK
		if (source != null)
		{
			form.setSource(source);
		}
		if (datasetlink != null)
		{
			form.setDatabaselink(datasetlink);
		}
		if (cleaners != null) {
			form.setCleaners(cleaners);
		} else {
			form.setCleaners(new NSMutableArray<String>());
		}
		
		log.warn("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <parseForm> succed parsing form with name: '" + name + "-v-" + version.toString()
				+ "'");
		return form;
	}

	private static CohortField parseField(NSDictionary<String, Object> aFieldData) throws InvalidJsonException {
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <parseField> starting with: '" + aFieldData + "'");

		// Validate and Init
		validateKeys(aFieldData.allKeys(), CohortField.MANDATORY_KEY, CohortField.ALL_KNOWN_KEY, CohortField.ALLOW_UNKNOWN_KEY, CohortField.class.getName());

		// get mandatory key
		String name = (String) aFieldData.valueForKey(CohortField.NAME_KEY);

		// Get optional key
		String externalName = (String) aFieldData.valueForKey(CohortField.EXTERNAL_NAME_KEY);
		@SuppressWarnings("unchecked")
		NSArray<String> cleaners = (NSArray<String>) aFieldData.valueForKey(CohortField.CLEANERS_KEY);
		@SuppressWarnings("unchecked")
		NSArray<String> filters = (NSArray<String>) aFieldData.valueForKey(CohortField.FILTERS_KEY);
		String staticValue = (String) aFieldData.valueForKey(CohortField.STATIC_VALUE_KEY);
		String classValue = (String) aFieldData.valueForKey(CohortField.CLASS_KEY);
		Boolean allowDuplicate = (Boolean) aFieldData.valueForKey(CohortField.DUPLICATE_KEY);

		// Create CohortForm
		CohortField field = new CohortField(name);
		if (externalName != null) {
			field.setExternalName(externalName);
		}
		field.setInstanceCount(1);
		if (cleaners != null) {
			field.setCleaners(cleaners);
		} else {
			field.setCleaners(new NSArray<String>());
		}
		if (filters != null) {
			field.setFilters(filters);
		} else {
			field.setFilters(new NSArray<String>());
		}
		if (staticValue != null) {
			field.setStaticValue(staticValue);
		}
		if (classValue != null) {
			field.setClassValue(classValue);
		}
		if (allowDuplicate != null) {
			field.setAllowDuplicate(allowDuplicate);
		}

		log.info("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <parseField> parsing field with name: '" + name + "'");
		return field;
	}

	public static void validateKeys(NSArray<String> keys, NSSet<String> mandatoryKeys, NSSet<String> allKnownKeys, Boolean allowUnknownKey, String className)
			throws InvalidJsonException {
		NSSet<String> passedKeys = new NSSet<String>(keys);

		// Check if all the mandatory keys are present
		NSSet<String> knownMandatoryKeys = passedKeys.setByIntersectingSet(mandatoryKeys);
		if (knownMandatoryKeys.count() != mandatoryKeys.count()) {
			NSSet<String> missingMandatoryKeys = mandatoryKeys.setBySubtractingSet(knownMandatoryKeys);
			throw new InvalidJsonException("[SIMED]{" + className + "}<validateKeys> The following mandatory keys where missing in the json: '"
					+ missingMandatoryKeys.allObjects().componentsJoinedByString(", ") + "'");
		}

		// Check if there are unknown keys and they are forbidden
		NSSet<String> extraneousKeys = passedKeys.setBySubtractingSet(allKnownKeys);
		if (extraneousKeys.count() > 0 && !allowUnknownKey) {
			throw new InvalidJsonException("[SIMED]{" + className + "}<validateKeys> There was the following unkown keys in the json: '"
					+ extraneousKeys.allObjects().componentsJoinedByString(", ") + "'");
		}
	}

	public static NSDictionary<String, Object> loadConfigurationData() throws InvalidJsonException {
		log.info("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <loadConfiguration> start");
		NSBundle appBundle = (NSBundle) NSBundle.mainBundle();
		byte[] bytes = appBundle.bytesForResourcePath(DEFAULT_CONFIGURATION_FILE);
		if (bytes.length == 0) {
			throw new InvalidJsonException("The file '" + DEFAULT_CONFIGURATION_FILE + "' seems to be missing");
		}
		String configurationString = new String(bytes, Charset.forName("UTF-8"));
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <loadConfigurationData> loaded string: \n" + configurationString);
		NSDictionary<String, Object> result = ERXPropertyListSerialization.<String, Object> dictionaryForJSONString(configurationString);
		log.warn("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <loadConfiguration> succed loading file: '"+DEFAULT_CONFIGURATION_FILE+"'.");
		return result;
	}

	private static NSDictionary<String, Object> loadFormData(String formToLoad) throws InvalidJsonException {
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <loadFormData> for file: '" + formToLoad + "'");
		NSBundle appBundle = (NSBundle) NSBundle.mainBundle();
		byte[] bytes = appBundle.bytesForResourcePath(formToLoad);
		if (bytes.length == 0) {
			log.error("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <loadFormData> The file '" + formToLoad + "' seems to be missing");
			throw new InvalidJsonException("The file '" + formToLoad + "' seems to be missing");
		}
		String configurationString = new String(bytes, Charset.forName("UTF-8"));
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <loadFormData> loaded string: \n" + configurationString);
		NSDictionary<String, Object> result = ERXPropertyListSerialization.<String, Object> dictionaryForJSONString(configurationString);
		log.warn("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.JsonParser} <loadFormData> succedd loading file: '" + formToLoad+"'.");
		return result;
	}
}
