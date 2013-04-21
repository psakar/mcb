package com.chare.mcb.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;

import com.chare.mcb.entity.Booking;
import com.chare.mcb.entity.PostingFile;
import com.chare.mcb.repository.SettingRepository;

public class PostingFileWriter {

	public static final String PARAM_DIR_IBS = "DIR_IBS";

	private final BookingFormatter formatter;
	private final SettingRepository settingRepository;

	public PostingFileWriter(BookingFormatter formatter, SettingRepository settingRepository) {
		this.formatter = formatter;
		this.settingRepository = settingRepository;
	}

	public void write(PostingFile postingFile) {
		//FIXME create under temporary name and then rename !!!
		// check there are any settlements
		File outputDirectory = assertExportDir();
		File file = new File(outputDirectory, postingFile.filename);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileOutputStream(file));
			for (Booking settlement : postingFile.getBookings())
				writer.write(formatter.format(settlement));
		} catch (Exception e) {
			throw new IllegalStateException("Error writing posting file " + e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	private File assertExportDir() {
		String exportDirName = settingRepository.getValue(SettingRepository.EXPORT_DIR, null);
		if (exportDirName == null)
			throw new IllegalArgumentException("Export directory not set");//FIXME localize
		File exportDir = new File(exportDirName);
		if (!(exportDir.exists() && exportDir.isDirectory()))
			throw new IllegalArgumentException("Export directory not found");//FIXME localize
		return exportDir;
	}
}
