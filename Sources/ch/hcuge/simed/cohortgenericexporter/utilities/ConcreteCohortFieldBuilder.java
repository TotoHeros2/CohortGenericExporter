package ch.hcuge.simed.cohortgenericexporter.utilities;

import java.security.InvalidParameterException;

//import hug.businesslogic.patientfacts.PFDataContext;
//import hug.businesslogic.patientfacts.PFDataSet;
//import hug.businesslogic.patientfacts.PFPatient;

import org.apache.log4j.Logger;

import ch.hcuge.simed.cohortgenericexporter.exportparameter.CohortField;
import ch.hcuge.simed.cohortgenericexporter.exportparameter.ConcreteCohortField;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSComparator.ComparisonException;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;


public class ConcreteCohortFieldBuilder {

	private static final Logger log = Logger.getLogger(ConcreteCohortFieldBuilder.class);

	public static NSArray<ConcreteCohortField> concreteFieldsForACCohortField(final String code, final int version, final CohortField aField) throws InvalidParameterException {
		NSMutableArray<ConcreteCohortField> results = new NSMutableArray<ConcreteCohortField>();
		String baseName = aField.externalName() != null ? aField.externalName() : aField.name();
		if (aField.instanceCount() > 1) {
			for (Integer i = 0; i < aField.instanceCount(); i++) {
				results.addObject(new ConcreteCohortField(baseName + separatorForBaseName(baseName) + i.toString(), i, aField));
			}
		} else if (aField.instanceCount() == 1) {
			results.addObject(new ConcreteCohortField(baseName, null, aField));
		} else {
			throw new InvalidParameterException("The number of instances of a field seems to be zero or non initialized");
		}
		return results.immutableClone();
	}

	private static String separatorForBaseName(String baseName) {
		return "_";
	}

	/**
	 * pour tenir compte du £
	 * 
	 * @author NIC
	 * @since 22 mai 2006
	 * @param currentField
	 * @return
	 */
	public static String getDuplicatedFieldCodeSQL(final String code, final int version, final CohortField aField) {
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.ConcreteCohortFieldBuilder} <getDuplicatedFieldCodeSQL> ");
		String sql = "SELECT DISTINCT NVL(duplicatedatacode, 0) RESULT FROM pfduplicatedatacode WHERE attributecode='" + aField.name()
				+ "' AND datacontextabbr='" + code + "' AND  datacontextversion=" + version;
		return sql;
	}

	/**
	 * 
	 * @author NIC
	 * @since 22 mai 2006
	 * @param currentField
	 * @param duplicateFieldCodes
	 * @return
	 */
	public static NSArray<String> getDuplicatedFieldsForAField(NSArray<NSDictionary<String, Object>> duplicateFieldCodes) {
		log.debug("[SIMED]{.FieldSelectionPage} <getDuplicatedFieldsForAField> ");
		NSMutableArray<String> fields = new NSMutableArray<String>();
		for (int i = 0; i < duplicateFieldCodes.count(); i++) {
			String duplicateFieldCode = (String) ((NSDictionary<String, Object>) duplicateFieldCodes.objectAtIndex(i)).objectForKey("duplicateCode");
			fields.addObject(duplicateFieldCode);
		}
		DuplicateCodeComparator comparator = new DuplicateCodeComparator();
		NSArray<String> sortedArray;
		try {
			sortedArray = fields.immutableClone().sortedArrayUsingComparator(comparator);
		} catch (ComparisonException e) {
			log.error("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.ConcreteCohortFieldBuilder} <getDuplicatedFieldsForAField> unable to sort with exception: '"
					+ e.getMessage() + "'");
			sortedArray = fields.immutableClone();
		}
		return sortedArray;
	}

	public static class DuplicateCodeComparator extends NSComparator {

		@Override
		public int compare(Object arg0, Object arg1) throws ComparisonException {
			if (((!(arg0 instanceof String)) || (!(arg0 instanceof String)))) {
				throw new ComparisonException("Ce comparateur ne traite que des String");
			}
			String string0 = (String) arg0;
			String string1 = (String) arg1;
			NSArray<String> splitedString0 = NSArray.componentsSeparatedByString(string0, "£");
			NSArray<String> splitedString1 = NSArray.componentsSeparatedByString(string1, "£");
			if (splitedString0.count() != splitedString1.count()) {
				throw new ComparisonException("Ce comparateur ne traite que des String de même type");
			}
			if (splitedString0.count() == 1) {
				try {
					if (string0 == null || string0.trim().length() == 0 || string1 == null || string1.trim().length() == 0) {
						log.error("NULL VALLUE for compare arg");
						return NSComparator.OrderedSame;
					}
					int int0 = new Integer(string0).intValue();
					int int1 = new Integer(string1).intValue();
					if (int0 < int1) {
						return NSComparator.OrderedAscending;
					} else if (int0 == int1) {
						return NSComparator.OrderedSame;
					} else {
						return NSComparator.OrderedDescending;
					}
				} catch (Exception e) {
					log.error("caught exception: '" + e.getMessage() + "' in comparator", e);
					return NSComparator.OrderedSame;
				}
			} else if (splitedString0.count() == 2) {
				int int0p1 = new Integer(splitedString0.objectAtIndex(0)).intValue();
				int int0p2 = new Integer(splitedString0.objectAtIndex(1)).intValue();
				int int1p1 = new Integer(splitedString1.objectAtIndex(0)).intValue();
				int int1p2 = new Integer(splitedString1.objectAtIndex(1)).intValue();
				if (int0p1 < int1p1) {
					return NSComparator.OrderedAscending;
				} else if (int0p1 == int1p1) {
					if (int0p2 < int1p2) {
						return NSComparator.OrderedAscending;
					} else if (int0p2 == int1p2) {
						return NSComparator.OrderedSame;
					} else {
						return NSComparator.OrderedDescending;
					}
				} else {
					return NSComparator.OrderedDescending;
				}
			} else {
				throw new ComparisonException("Ce comparateur ne traite que des String de pouvant être coupe en 1 ou 2 morceaux");
			}
		}

	}
}
