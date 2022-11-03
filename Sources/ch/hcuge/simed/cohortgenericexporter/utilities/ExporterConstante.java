package ch.hcuge.simed.cohortgenericexporter.utilities;

import org.apache.log4j.Logger;

public class ExporterConstante {

	public static Logger log = Logger.getLogger(ExporterConstante.class);

	/*
	 * String constante
	 */
	public static final String MINUS_ONE = "-1";
	public static final String ZERO = "0";
	public static final String ONE = "1";
	public static final String TWO = "2";
	public static final String THREE = "3";
	public static final String FOUR = "4";
	public static final String FIVE = "5";
	public static final String SIX = "6";
	public static final String SEVEN = "7";
	public static final String EIGHT = "8";
	public static final String NINE = "9";
	public static final String TEN = "10";
	public static final String ELEVEN = "11";
	public static final String TWELVE = "12";
	public static final String THIRTEEN = "13";
	public static final String FOURTEEN = "14";
	public static final String FIFTEEN = "15";
	public static final String SIXTEEN = "16";
	public static final String SEVENTEEN = "17";
	public static final String EIGHTEEN = "18";
	public static final String NINETEEN = "19";
	public static final String TWENTY = "20";
	public static final String DOT = ".";

	public static final String EMPTY_STRING = "";
	public static final String NO = "NO";
	public static final String NON = "non";
	public static final String NO_CODE = ZERO;
	public static final String YES = "Yes";
	public static final String OUI = "oui";
	public static final String YES_CODE = ONE;
	public static final String TRUE_CODE = ONE;
	public static final String TRUE = "true";
	public static final String FALSE_CODE = ZERO;
	public static final String FALSE = "false";
	public static final String NOT_DEFINED = "Not defined";
	public static final String NOT_DEFINED_CODE = NINE;
	public static final String NOT_CLASSIFIED = "Not classified";
	public static final String NONE = "None";
	public static final String NOT_CLASSIFIED_CODE = NINE;
	public static final String DOUBLE_HYPHEN = "--";
	public static final String DOUBLE_HYPHEN_CODE = DOT;
	public static final String OTHER = "Other";
	public static final String OTHER_CODE = "88";
	public static final String NOT_SPECIFIED = "Not specified";
	public static final String UNKNOWN = "Unknown";
	public static final String UNKNOWN_CODE = "77";
	public static final String ND = "ND";
	public static final String ND_CODE = "99";
	public static final String UNANSWERED = DOUBLE_HYPHEN;
	public static final String UNANSWERED_CODE = MINUS_ONE;
	
	public static final String CSV_FIELD_SEPARATOR = "~";
	public static final String CSV_LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String ERROR_STRING = " ### ERROR ### ";
	
	/*
	 * Class utilities constants
	 */
	public static final String FETCHALL_START = "fetchAll";
	public static final String FETCHALL_END = "s";
	
	/*
	 * Integer constante
	 */
	public static final Integer INTEGER_ONE = new Integer(1);

	/*
	 * App
	 */
	public static final String STCS_CODE = "STCS";
	public static final String SSCS_CODE = "SSCS";
	public static final String IBD_CODE = "IBD";
	public static final String IFR_CODE = "IFR";
	public static final String IHR_CODE = "IHR";

	public static final String SPACE_DOT_SPACE = " . ";

	/*
	 * STCS_FORM
	 */
	public static String PATIENT_ADMIN = "STCS_0001";
	// manager
	public static String MANAGER = "STCS_0002";
	// SwissTx
	public static String SWISSTX = "STCS_0004";
	// Organ
	public static String OrganForm = "STCS_0005";
	// Event
	public static String GlobalEvent = "STCS_0003";
	public static String OrganEvent = "STCS_0006";
	// Common
	public static String DOTxCommon = "STCS_0007";
	public static String FUPCommon = "STCS_0008";
	// Kidney
	public static String DOTxKidney = "STCS_0009";
	public static String FUPKidney = "STCS_0010";
	// Heart
	public static String DOTxHeart = "STCS_0011";
	public static String FUPHeart = "STCS_0012";
	// Liver
	public static String DOTxLiver = "STCS_0013";
	public static String FUPLiver = "STCS_0014";
	// Lung
	public static String DOTxLung = "STCS_0015";
	public static String FUPLung = "STCS_0016";
	// Pancreas
	public static String DOTxPancreas = "STCS_0017";
	public static String FUPPancreas = "STCS_0018";
	// Islets
	public static String DOTxIslets = "STCS_0019";
	public static String FUPIslets = "STCS_0020";
	public static String Reperfusion_islets = "STCS_0028";
	// Small bowels
	public static String DOTxSmallBowel = "STCS_0023";
	public static String FUPSmallBowel = "STCS_0024";
	// Bone marrow autologous
	public static String DOTxBoneMarrowAutologous = "STCS_0029";
	public static String FUPBoneMarrowAutologous = "STCS_0029";
	// Bone marrow allogeneic
	public static String DOTxBoneMarrowAllogeneic = "STCS_0030";
	public static String FUPBoneMarrowAllogeneic = "STCS_0030";
	// No organ form
	public static String noGraftDataContext = "STCS_0031";
	// Infectious diseases
	public static String InfectiousDiseases = "STCS_0021";
	public static String InfectionForm = "STCS_0022";
	// Samples
	public static String SampleManager = "STCS_0027";
	public static String SampleForm = "STCS_0026";
	// Stop
	public static String STOPForm = "STCS_0025";
	// Drug
	public static String MaintenanceImmunosuppressiveForm = "STCS_0032";
	public static String InductionForm = "STCS_0033";
	public static String InfDisProphyForm = "STCS_0034";
	public static String DrugOtherForm = "STCS_0035";
	// FEV1best
	public static String FEV1bestForm = "STCS_0037";

}