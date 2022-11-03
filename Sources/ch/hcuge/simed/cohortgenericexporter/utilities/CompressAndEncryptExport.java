package ch.hcuge.simed.cohortgenericexporter.utilities;

import ch.hcuge.simed.cohortgenericexporter.exporter.Exporter;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.webobjects.foundation.NSArray;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class CompressAndEncryptExport {

	public static final Logger log = Logger.getLogger(CompressAndEncryptExport.class);

	private ZipFile _zipFile = null;
	private boolean _hasException = false;
	private String password = "research";

	public File compress(String exportDestination, String zipFileName, File directoryToCompress) {
		File theZipFile = null;
		try {
			// This is name and path of zip file to be created
			setZipFile(new ZipFile(getPathForZipFile(exportDestination, zipFileName)));
			// Get the parameters
			ZipParameters parameters = getZipParameters();
			// explicitly set include rootfolder even if it's true by default
			parameters.setIncludeRootFolder(true);
			// Now add the folder to the zip file
			zipFile().addFolder(directoryToCompress, parameters);
		} catch (ZipException e) {
			_hasException = true;
			log.error("[SIMED]{" + this.getClass().getName() + "} <compress> ZipException: '" + e.getMessage() + "'.");
		} catch (InvalidParameterException e) {
			_hasException = true;
			log.error("[SIMED]{" + this.getClass().getName() + "} <compress> InvalidParameterException: '" + e.getMessage() + "'.");
		} finally {
			if (!_hasException) {
				theZipFile = zipFile().getFile();
			}
		}
		return theZipFile;
	}

	public File compress(String exportDestination, String zipFileName, NSArray<File> fileList) {
		File theZipFile = null;
		try {
			// This is name and path of zip file to be created
			setZipFile(new ZipFile(getPathForZipFile(exportDestination, zipFileName)));
			// Add files to be archived into zip file
			ArrayList<File> filesToAdd = new ArrayList<File>(fileList.arrayList());
			// Get the parameters
			ZipParameters parameters = getZipParameters();
			// Now add the files to the zip file
			zipFile().addFiles(filesToAdd, parameters);
		} catch (ZipException e) {
			e.printStackTrace();
			_hasException = true;
			log.error("[SIMED]{" + this.getClass().getName() + "} <compress> ZipException: '" + e.getMessage() + "'.");
		} catch (InvalidParameterException e) {
			e.printStackTrace();
			_hasException = true;
			log.error("[SIMED]{" + this.getClass().getName() + "} <compress> InvalidParameterException: '" + e.getMessage() + "'.");
		} finally {
			if (!_hasException) {
				theZipFile = zipFile().getFile();
			}
		}
		return theZipFile;
	}

	private ZipParameters getZipParameters() {
		// Initiate Zip Parameters which define various properties
		ZipParameters parameters = new ZipParameters();
		// Set compression method to deflate evn if it's the default method
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		// Set compression level to DEFLATE_LEVEL_ULTRA
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
		// Set the encryption flag to true
		parameters.setEncryptFiles(true);
		// Set the encryption method to AES Zip Encryption
		parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
		// AES_STRENGTH_256 - For both encryption and decryption
		parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
		// Set password
		parameters.setPassword(password.toCharArray());
		return parameters;
	}

	public ZipFile zipFile() {
		return this._zipFile;
	}

	private void setZipFile(ZipFile zipFile) {
		this._zipFile = zipFile;
	}

	private File getPathForZipFile(String exportDestination, String zipFileName) {
		File f = new File(exportDestination + File.separator + zipFileName);
		if (f.exists()) {
			throw new InvalidParameterException("The file: '" + f.getAbsolutePath() + "' already exists we can't overwrite it!");
		}
		return f;
	}

}
