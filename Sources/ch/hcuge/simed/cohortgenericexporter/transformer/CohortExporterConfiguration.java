package ch.hcuge.simed.cohortgenericexporter.transformer;

import org.apache.log4j.Logger;

public final class CohortExporterConfiguration {

	public static Logger log = Logger.getLogger(CohortExporterConfiguration.class);
	// STCS --> texte libre
	public static final boolean STCS_FILTERS = false;
	public static final boolean SSCS_FILTERS = true;
	public static final boolean IBD_FILTERS = false;
	public static final boolean IHR_FILTERS = false;
	public static final boolean IFR_FILTERS = false;

	public static final boolean ACTIVATED_CLEANERS = true;

}