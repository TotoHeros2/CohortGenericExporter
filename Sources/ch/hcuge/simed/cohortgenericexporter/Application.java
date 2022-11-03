package ch.hcuge.simed.cohortgenericexporter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;

import ch.hcuge.simed.cohortgenericexporter.Application;
import ch.hcuge.simed.cohortgenericexporter.exporter.Exporter;
import ch.hcuge.simed.cohortgenericexporter.utilities.JsonParser;
import ch.hcuge.simed.cohortgenericexporter.utilities.scheduler.ExporterTask;
import er.extensions.appserver.ERXApplication;
import er.extensions.foundation.ERXTimestampUtilities;

public class Application extends ERXApplication {
	
	private NSTimestamp _nextExportExecutionDate = null;

	public static void main(String[] argv) {
		ERXApplication.main(argv, Application.class);
	}

	public Application() {
		ERXApplication.log.info("Welcome to " + name() + " !");
		/* ** put your initialization code in here ** */
		setAllowsConcurrentRequestHandling(true);		
	}
	
	@Override
	public void didFinishLaunching() {
		super.didFinishLaunching();
		try {
			ExporterTask manualTask = new ExporterTask();
			Thread manualExportThread = new Thread(manualTask);
			manualExportThread.start();
		} catch (Exception e) {
			
		}
	}
	
	public static Application app() {
		return (Application) ERXApplication.application();
	}
	/**
	 * @param update
	 *            the nextExportExecutionDate to nextExportDate
	 */
	public static void updateNextExportExecutionDate(NSTimestamp nextExportDate) {
		app().setNextExportExecutionDate(nextExportDate);
	}
	
	/**
	 * @return the GregorianCalandar for tomorrow at 1 AM
	 */
	public static GregorianCalendar getNextExecutionCalendar() {
		NSTimestamp tomorrow = ERXTimestampUtilities.tomorrow();
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(tomorrow);
		gc.set(Calendar.HOUR_OF_DAY, 1);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		return gc;
	}
	
	public void setNextExportExecutionDate(NSTimestamp nextExportExecutionDate) {
		this._nextExportExecutionDate = nextExportExecutionDate;
	}
}


