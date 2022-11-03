package ch.hcuge.simed.cohortgenericexporter.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.webobjects.eoaccess.ERXModel;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import ch.hcuge.simed.cohortgenericexporter.exportparameter.ConcreteCohortField;
import ch.hcuge.simed.cohortgenericexporter.transformer.ExportTransformer;
import ch.hcuge.simed.cohortgenericexporter.utilities.ConcreteCohortFieldBuilder;
import ch.hcuge.simed.cohortgenericexporter.utilities.CompressAndEncryptExport;
import ch.hcuge.simed.cohort.sscs.eo.AbstractForm;
//import ch.hcuge.simed.cohortgenericexporter.utilities.Exporter.FileExtensionFilter;
import ch.hcuge.simed.cohortgenericexporter.datastructures.FormDumper;
import ch.hcuge.simed.cohortgenericexporter.exportparameter.CohortApplication;
import ch.hcuge.simed.cohortgenericexporter.exportparameter.CohortField;
import ch.hcuge.simed.cohortgenericexporter.exportparameter.CohortForm;
import ch.hcuge.simed.cohortgenericexporter.utilities.ExporterConstante;
import ch.hcuge.simed.cohortgenericexporter.utilities.JsonParser;
import ch.hcuge.simed.foundation.extendableenum.ExtendableEnum;

import er.extensions.appserver.ERXApplication;
import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXExpiringCache;
//import oracle.net.aso.a;

public class Exporter {
	
	private static Logger log = Logger.getLogger(Exporter.class);

	
	/*
	 * TODO: Put in properties
	 */
//	public static final String ROOT_EXPORT_FOLDER = "V:\\ExportCohort\\SSCSExport\\new\\";

	/**
	 * Utility cache to store an instance for each needed cleaners and filters
	 */
	private static ERXExpiringCache<String, ExportTransformer> TRANSFORM_CACHE = new ERXExpiringCache<String, ExportTransformer>(ERXExpiringCache.NO_TIMEOUT);
	
	/**
	 * NSMutableArray containing the list of generated file
	 */
	private static NSMutableArray<File> _fileList = null;
	
	/**
	 * ISO 8601 date formatter for saving the file in a zip file
	 */
	private static final SimpleDateFormat ISO_DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

	
	public static void run() {
		try {
			NSDictionary<String, Object> jsonDict = JsonParser.loadConfigurationData();
			NSArray<CohortApplication> parsedApps = JsonParser.parse(jsonDict);
			exportData(parsedApps);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportData(NSArray<CohortApplication> cohorts) {
		NSTimestamp fullStart = new NSTimestamp();
		NSTimestamp start = null;
		NSTimestamp end = null;
		EOEditingContext ec = null;
		
		try {
			for(CohortApplication aCohort : cohorts) {
				setFileList(new NSMutableArray<File>());
				for(CohortForm aForm : aCohort.formsData()) {
					ec = ERXEC.newEditingContext();
					start = new NSTimestamp();
					
					String name = aForm.name();
					String packageForEo = aCohort.classLocations();
										
					Class<?> automaticClass = null;
					
					automaticClass = Class.forName(packageForEo+"."+name);
					Method[] methods = automaticClass.getMethods();
					Method automaticFunction = null;
					for(Method method : methods) {
						if(method.getName().contains(ExporterConstante.FETCHALL_START) 
								&& method.getName().contains(name.subSequence(0, name.length()-1))
								&& method.getParameterCount() == 1) {
							automaticFunction = method;
							break;
						}	
					}

					
					if (automaticFunction == null)
						throw new NoSuchMethodException("No fetch all instances method was found for this class.");
					else {
//						Method automaticFunction2 = automaticClass.getMethod(ExporterConstante.FETCHALL_START+name+ExporterConstante.FETCHALL_END, EOEditingContext.class);
//						NSArray<EOGenericRecord> allObjects2 = (NSArray<EOGenericRecord>) automaticFunction2.invoke(null,ec);

//						automaticFunction.getName();
						NSArray<EOGenericRecord> allObjects = (NSArray<EOGenericRecord>) automaticFunction.invoke(null,ec);
						
//						NSArray<CohortField> aFormFields =aForm.fields();
						FormDumper formDumper = new FormDumper<>(aForm);
						
						for(EOGenericRecord eoObject : allObjects) {
							formDumper.dump(eoObject);
						}
						NSDictionary<EOGenericRecord, NSDictionary<String,NSArray<String>>> data = formDumper.returnDumpedForms();
						NSArray<ConcreteCohortField> templateRow = buildRowTemplate(aForm);
						generateExportFile(aCohort, aForm, templateRow, data);
						
						ec.dispose();
						ec = null;
						end = new NSTimestamp();
						
						log.warn("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <exportData> for form:'" + aForm.nameWithVersion() + "' found: '"
								+ data.count() + " filled forms processed in: '" + (end.getTime() - start.getTime()) + "' milliseconds");

					}		
				}
				String zipFileName = aCohort.name() + "_" + ISO_DATE_FORMATTER.format(new NSTimestamp()) + ".zip";
				if (compressExport(aCohort.exportDestination(), zipFileName.trim(), fileList())) {
					cleanFiles(fileList());
				}
				System.exit(0);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param aForm
	 * @return build the first row for a Table respecting the duplicate field
	 */
	private static NSArray<ConcreteCohortField> buildRowTemplate(final CohortForm aForm) {
//		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <buildRowTemplate> ");
		NSMutableArray<ConcreteCohortField> results = new NSMutableArray<ConcreteCohortField>();
		for (CohortField aField : aForm.fields()) {
			results.addObjectsFromArray(ConcreteCohortFieldBuilder.concreteFieldsForACCohortField(aForm.name(), aForm.version().intValue(), aField));
		}
		return results.immutableClone();
	}
	
//	private static void dump(EOGenericRecord eo, NSMutableArray<String> elInLine) {
//		// TODO mettre value of eo in array
//		// 1 seul niveau
//		for (String key: eo.attributeKeys()) // en fait lire le json pour l'entity et retrouver la liste des keys
//		{
//			Object object = eo.valueForKey(key);
//			if (object instanceof ExtendableEnum)
//			{
//				ExtendableEnum value = (ExtendableEnum) object;
//				elInLine.add(value.getCode());
//			}
//			else
//			{
//				if (object == null)
//				{
//					elInLine.add(ExporterConstante.SPACE_DOT_SPACE);
//				}
//				else
//				{
//					elInLine.add(object.toString());
//				}
//			}
////			elInLine.add(Format.format(eo.valueForKey(key)));
//		}
//	}
	
//	private static void setFileList(NSMutableArray<File> fileList) {
//		_fileList = fileList;
//	}
	/**
	 * @param aCohort
	 * @param aForm
	 * @param templateRow
	 * @param data
	 * 
	 * 
	 *            build the StringBUilder for the form and save it
	 */
	private static void generateExportFile(final CohortApplication aCohort, final CohortForm aForm, final NSArray<ConcreteCohortField> templateRow,
			final NSDictionary<EOGenericRecord, NSDictionary<String,NSArray<String>>> data) {
		StringBuilder sb = new StringBuilder();
		sb.append(buildHeaderRow(templateRow));
	
		NSArray<EOGenericRecord> objectsToWrite = data.allKeys();
		for (EOGenericRecord objectToWrite : objectsToWrite) {
			sb.append(buildDataRow(aCohort, aForm, templateRow, data.get(objectToWrite)));
		}
		saveData(aCohort.exportDestination(), aForm.externalName() + ".txt", sb);
	}

	/**
	 * @param aForm
	 * @param sb
	 * 
	 *            Save the data for form in the specified file
	 */
	private static void saveData(final String exportDestination, final String aFileName, StringBuilder sb) {
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <saveData> ");
		try {
			String fileName = aFileName;
			File f = new File(exportDestination + File.separator +  fileName);
			fileList().addObject(f);
			log.info("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <saveData> will save in: '" + f.getAbsolutePath() + "'.");
			BufferedWriter fichier = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
			fichier.write(sb.toString());
			fichier.close();
		} catch (Exception e) {
			log.error("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <saveData> generated exception. '" + e.getMessage() + "'");
			e.printStackTrace();
		}
	}
	
	/**
	 * @param templateRow
	 * @return return the StringBuilder for the first row for a form
	 */
	private static StringBuilder buildHeaderRow(NSArray<ConcreteCohortField> templateRow) {
		StringBuilder sb = new StringBuilder();
		boolean firstTime = true;
		for (ConcreteCohortField concreteCohortField : templateRow) {
			if (!firstTime) {
				sb.append(ExporterConstante.CSV_FIELD_SEPARATOR);
			}
			sb.append(concreteCohortField.headerName());
			if (firstTime) {
				firstTime = false;
			}
		}
		sb.append(ExporterConstante.CSV_LINE_SEPARATOR);
		return sb;
	}
	
	/**
	 * @param aCohort
	 * @param aForm
	 * @param templateRow
	 * @param aDataSet
	 * @return the stringbuilder containg the data for the current dataset
	 */
	private static StringBuilder buildDataRow(final CohortApplication aCohort, final CohortForm aForm, final NSArray<ConcreteCohortField> templateRow, 
			final NSDictionary<String,NSArray<String>> aDataSet) {
		StringBuilder sb = new StringBuilder();
		boolean firstTime = true;
		for (ConcreteCohortField aConcreteCohortField : templateRow) {
			if (!firstTime) {
				sb.append(ExporterConstante.CSV_FIELD_SEPARATOR);
			}
			String result = getValueInDataSet(aDataSet, aConcreteCohortField);
			try {
				sb.append(cleanedData(result, aCohort, aForm, aConcreteCohortField, templateRow, aDataSet));
			} catch (Exception e) {
				sb.append(ExporterConstante.ERROR_STRING + " while cleaning: '" + (result == null ? "NULL" : result.toString()) + "' with message: '" + e.getMessage() + "'.");
			}
			if (firstTime) {
				firstTime = false;
			}
		}
		sb.append(ExporterConstante.CSV_LINE_SEPARATOR);
		return sb;
	}
	
	private static String getValueInDataSet(final NSDictionary<String,NSArray<String>> aDataSet, ConcreteCohortField aConcreteCohortfield) {
		NSArray<String> values = aDataSet.get(aConcreteCohortfield.sourceField().name());
		String result = null; 
		if (values == null) {
			result = ExporterConstante.SPACE_DOT_SPACE;
		} else {
			String value;
			if (values.size() == 0) {
				value = ExporterConstante.SPACE_DOT_SPACE;
			} else if(aConcreteCohortfield.duplicateCode() == null) {
				value = values.get(0);
			} else if (aConcreteCohortfield.duplicateCode() >= values.size()) {
				value = ExporterConstante.SPACE_DOT_SPACE;
			}  else {
				value = values.get(aConcreteCohortfield.duplicateCode());
			}
			result = value;
		}
		return result;
	}
	
	/**
	 * @param result
	 * @param aCohort
	 * @param aForm
	 * @param aConcreteCohortField
	 * @return the cleaned data using the cleaners and filters
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	private static Object cleanedData(final Object result, final CohortApplication aCohort, final CohortForm aForm, ConcreteCohortField aConcreteCohortField, final NSArray<ConcreteCohortField> templateRow, final NSDictionary<String,NSArray<String>> aDataSet)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Boolean useFilters = aCohort.useFilters();
		CohortField sourceField = aConcreteCohortField.sourceField();
		NSMutableArray<String> transfomers = sourceField.cleaners().mutableClone();
		if (useFilters.booleanValue()) {
			transfomers.addObjectsFromArray(sourceField.filters());
		}
		NSMutableArray<String>formCleaners = aForm.cleaners().mutableClone();
		if (formCleaners != null) {
			transfomers.addObjectsFromArray(formCleaners);
		}
		NSMutableArray<String>appCleaners = aCohort.cleaners().mutableClone();
		if (appCleaners != null) {
			transfomers.addObjectsFromArray(appCleaners);
		}
		
		Object transformedData = result;
		if (result == null) {
			transformedData = ExporterConstante.SPACE_DOT_SPACE;
		} else {
			for (String aTransformerName : transfomers) {
				ExportTransformer transformer = instanceOfTransformer(aTransformerName);
				transformedData = transformer.transform(transformedData, aCohort.name(), aForm.name(), aForm.versionAsString(), aConcreteCohortField
						.sourceField().name(),templateRow, aDataSet);
			}
		}
		return transformedData;
	}
	
	/**
	 * @param aTransformerName
	 * @return an instance of the transformer
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private static ExportTransformer instanceOfTransformer(final String aTransformerName) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		ExportTransformer transformerInstance = (ExportTransformer) TRANSFORM_CACHE.objectForKey(aTransformerName);
		if (transformerInstance == null) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends ExportTransformer> transformerClazz = (Class<? extends ExportTransformer>) Class.forName(aTransformerName);
				transformerInstance = transformerClazz.newInstance();
				TRANSFORM_CACHE.setObjectForKey(transformerInstance, aTransformerName);
			} catch (ClassNotFoundException e) {
				throw new ClassNotFoundException("for transformerName: '" + aTransformerName + "'.");
			} catch (InstantiationException e) {
				throw new InstantiationException("for transformerName: '" + aTransformerName + "'.");
			} catch (IllegalAccessException e) {
				throw new IllegalAccessException("for transformerName: '" + aTransformerName + "'.");
			}
		}
		return transformerInstance;
	}
	
	private static boolean compressExport(final String exportDestination, final String zipFileName, NSMutableArray<File> fileList) {
		CompressAndEncryptExport compressor = new CompressAndEncryptExport();
		File f = compressor.compress(exportDestination, zipFileName, fileList);
		log.warn("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <compressExport> compressed file is: '" + (f == null ? "N/A" : f.getAbsolutePath())
				+ "'.");
		return f != null;
	}

	private static void cleanFiles(NSMutableArray<File> fileList) {
		for (File file : fileList) {
			log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <cleanFiles> will delete: '" + file.getAbsolutePath() + "'.");
			file.delete();
			log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <cleanFiles> did delete: '" + file.getAbsolutePath() + "'.");
		}
		log.warn("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <cleanFiles> did delete: " + fileList.count() + " file(s).");
	}

	private static void processPostConditionInFolder(CohortApplication aCohort, String string, ERXModel model) {
		NSBundle appBundle = (NSBundle) NSBundle.mainBundle();
		String partialPath = File.separator + aCohort.name() + File.separator + "postconditions" + File.separator;
		URL postconditionURL = appBundle.pathURLForResourcePath(partialPath);
		String postconditionPath = postconditionURL.getPath();
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <processPostConditionInFolder> postconditionPath: '" + postconditionPath + "'.");
		File postconditionsFolder = new File(postconditionPath);
		log.debug("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <processPostConditionInFolder> postconditionPath: '" + postconditionPath
				+ "' exists: '" + postconditionsFolder.exists() + ".");
		// String pathToFolder = bundlePath + partialPath;
		// File postconditionsFolder = new File(pathToFolder);
		FileExtensionFilter sqlFilter = new FileExtensionFilter(".sql");
		NSArray<String> sqlFiles = new NSArray<String>(postconditionsFolder.list(sqlFilter));
		log.warn("[SIMED]{ch.hcuge.simed.cohortexporter.utilities.Exporter} <processPostConditionInFolder> end processing " + sqlFiles.count() + " sqlFiles");
	}
	private static NSMutableArray<File> fileList() {
		return _fileList;
	}
	
	private static void setFileList(NSMutableArray<File> fileList) {
		_fileList = fileList;
	}
	
	private static class FileExtensionFilter implements FilenameFilter {

		private String _extension = null;

		/**
		 * @param extention
		 *            must include the dot
		 */
		public FileExtensionFilter(final String extention) {
			this._extension = extention;
		}

		private String extension() {
			return this._extension;
		}

		@Override
		public boolean accept(File dir, String name) {
			boolean shouldAccept = false;
			if (name.lastIndexOf('.') > 0) {

				int lastIndex = name.lastIndexOf('.');
				// get extension
				String nameExtension = name.substring(lastIndex);
				// match path name extension
				if (nameExtension.equals(extension())) {
					shouldAccept = true;
				}
			}
			return shouldAccept;
		}
	}
}
