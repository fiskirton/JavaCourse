package org.collection;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.gui.MainApp;

import java.io.File;
import java.io.IOException;
import java.sql.Struct;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Main {
	public static void main(String[] args) throws ImageProcessingException, IOException {
		String date = "Tue Mar 20 17:41:20 MSK 2018";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
		File file = new File("/home/fiskirton/Documents/Projects/repos/JavaCourse/ImageKeeper/src/main/resources/images/test2.jpeg");
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		LocalDateTime date2 = LocalDateTime.parse(date, formatter);
		long d = Timestamp.valueOf(date2).getTime();
		String ds = String.valueOf(d);
		ZonedDateTime zd = ZonedDateTime.ofInstant(Instant.ofEpochMilli(d), ZoneId.systemDefault());
	}
}
