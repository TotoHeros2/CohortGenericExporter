package ch.hcuge.simed.cohortgenericexporter.transformer.filters;

//import hug.businesslogic.patientfacts.PFDataSet;

import org.apache.log4j.Logger;

import ch.hcuge.simed.cohortgenericexporter.exportparameter.ConcreteCohortField;
import ch.hcuge.simed.cohortgenericexporter.transformer.ExportTransformer;
import ch.hcuge.simed.cohortgenericexporter.utilities.ExporterConstante;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

public class UnansweredFormater extends ExportTransformer {

	public static Logger log = Logger.getLogger(UnansweredFormater.class);

	public UnansweredFormater() {
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
		return new UnansweredFormater();
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
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.transformer.filters.UnansweredFormater} <format> ");
		if (value.equals(ExporterConstante.UNANSWERED_CODE)) {
			return ExporterConstante.SPACE_DOT_SPACE;
		} else
			return value.toString();
	}

//	public static String formatBigDecimalWithoutZerosEnd(Object value) {
//		log.debug("[SIMED]{hug.businesslogic.patientfacts.PFDecimalFormatter} <formatBigDecimalWithoutZerosEnd> ");
//		String tmp = value.toString();
//
//		if ((tmp.indexOf(",") >= 0) || (tmp.indexOf(".") >= 0)) {
//			int i = tmp.length() - 1;
//			boolean found = false;
//			while ((i > 0) && (!found)) {
//				char c = tmp.charAt(i);
//				found = (c != '0');
//				i--;
//			}
//			tmp = tmp.substring(0, i + 2);
//		}
//
//		return tmp;
//	}
//
//	public static void main(String[] argv) {
//		NSArray<Double> doubles = new NSArray<Double>(new Double[] { new Double(-1.0), new Double(-0.5), new Double(-0.0193), new Double(-0.0),
//				new Double(0.0), new Double(0), new Double(0.1), new Double(0.1199999), new Double(0.12), new Double(0.123), new Double(0.189),
//				new Double(0.909), new Double(Math.PI), new Double(Math.E), new Double(1), new Double(1.0), new Double(1.00), new Double(1.001),
//				new Double(1.01), new Double(1.1200003e2), new Double(1.0000000e2), new Double(1.339e12), new Double(123.123000000000) });
//		for (Double aDouble : doubles) {
//			System.out.println(aDouble.toString() + " => '" + formatBigDecimalWithoutZerosEnd(aDouble) + "'");
//		}
//	}
}
