package ch.hcuge.simed.cohortgenericexporter.transformer.filters;

//import hug.businesslogic.patientfacts.PFDataSet;

import org.apache.log4j.Logger;

import ch.hcuge.simed.cohortgenericexporter.exportparameter.ConcreteCohortField;
import ch.hcuge.simed.cohortgenericexporter.transformer.ExportTransformer;
import ch.hcuge.simed.cohortgenericexporter.utilities.ExporterConstante;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

public class TrueFalseFormatter extends ExportTransformer {

	public static Logger log = Logger.getLogger(TrueFalseFormatter.class);

	public TrueFalseFormatter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.hcuge.simed.cohortexporter.transformer.ExportTransformer#newInstance()
	 */
	@Override
	public ExportTransformer newInstance() {
		return new TrueFalseFormatter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.hcuge.simed.cohortexporter.transformer.ExportTransformer#transform
	 * (java.lang.Object, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Object transform(Object initialValue, String appName, String formName, String formVersion, String fieldName, NSArray<ConcreteCohortField> templateRow, NSDictionary<String,NSArray<String>> aDataSet) {
		log.debug("[SIMED]{" + this.getClass().getName() + "} <transform> object of class: '" + initialValue.getClass().getName() + "'");
		return format(initialValue);
	}

	public static String format(Object value) {
		log.debug("[SIMED]{ch.hcuge.simed.cohortgenericexporter.transformer.filters.TrueFalseFormater} <format> ");
		if (value.equals(ExporterConstante.FALSE)) {
			return ExporterConstante.FALSE_CODE;
		} else if(value.equals(ExporterConstante.TRUE))
			return ExporterConstante.TRUE_CODE;
		else
			return value.toString();
	}
}
