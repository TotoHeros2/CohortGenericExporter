package ch.hcuge.simed.cohortgenericexporter.transformer.filters.sscs;

//import hug.businesslogic.patientfacts.PFDataSet;

import org.apache.log4j.Logger;

import ch.hcuge.simed.cohortgenericexporter.exportparameter.ConcreteCohortField;
import ch.hcuge.simed.cohortgenericexporter.transformer.ExportTransformer;
import ch.hcuge.simed.cohortgenericexporter.utilities.ExporterConstante;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

public class RemainingSamplesFormater extends ExportTransformer {

	public static Logger log = Logger.getLogger(RemainingSamplesFormater.class);

	public RemainingSamplesFormater() {
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
		return new RemainingSamplesFormater();
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
		return format(aDataSet);
	}

	public static String format(NSDictionary<String,NSArray<String>> aDataSet) {
		log.debug("[SIMED]{ch.hcuge.simed.cohortgenericexporter.transformer.filters.RemainingSamplesFormater} <format> ");
		NSArray<String> storedAliquots = aDataSet.get("numberOfAliquot");
		Integer totalAliquots = 0;
		for (String aliquot : storedAliquots) {
			totalAliquots += Integer.parseInt(aliquot);
		}
		NSArray<String> usedAliquots = aDataSet.get("aliquotUsages.quantity");
		for (String usage : usedAliquots) {
			totalAliquots -= Integer.parseInt(usage);
		}
		return totalAliquots.toString();
	}

}
