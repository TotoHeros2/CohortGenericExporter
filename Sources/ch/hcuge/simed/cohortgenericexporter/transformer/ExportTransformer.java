package ch.hcuge.simed.cohortgenericexporter.transformer;

import ch.hcuge.simed.cohortgenericexporter.exportparameter.ConcreteCohortField;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

public abstract class ExportTransformer {
	/**
	 * @return a new instance of the concrete class
	 */
	public abstract ExportTransformer newInstance();

	/**
	 * @param initialValue
	 * @param appName
	 * @param formName
	 * @param formVersion
	 * @param fieldName
	 * @return the cleaned value
	 */
	public abstract Object transform(Object initialValue, String appName, String formName, String formVersion, String fieldName, NSArray<ConcreteCohortField> templateRow,  NSDictionary<String,NSArray<String>> aDataSet);

}
