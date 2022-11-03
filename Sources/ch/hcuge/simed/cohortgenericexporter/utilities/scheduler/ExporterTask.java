package ch.hcuge.simed.cohortgenericexporter.utilities.scheduler;

import java.util.GregorianCalendar;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import ch.hcuge.simed.cohortgenericexporter.Application;
import ch.hcuge.simed.cohortgenericexporter.exporter.Exporter;
import ch.hcuge.simed.cohortgenericexporter.utilities.InvalidJsonException;
import ch.hcuge.simed.cohortgenericexporter.utilities.JsonParser;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;

public class ExporterTask extends TimerTask {

	public static Logger log = Logger.getLogger(ExporterTask.class);

	/**
	 * Defaul constructor
	 */
	public ExporterTask() {
		// Nothing to do
	}

	public static void  export() throws InvalidJsonException {
		NSDictionary<String, Object> jsonDict = JsonParser.loadConfigurationData();
		Exporter.exportData(JsonParser.parse(jsonDict));
		GregorianCalendar gc = Application.getNextExecutionCalendar();
		Application.updateNextExportExecutionDate(new NSTimestamp(gc.getTimeInMillis()));
	}

	@Override
	public void run() {
		try {
			export();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
