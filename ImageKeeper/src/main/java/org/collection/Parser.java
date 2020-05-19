package org.collection;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Parser {
	
	private final BufferedImage image;
	private Metadata metadata;
	private FileSystemDirectory sysDir;
	private FileTypeDirectory typeDir;
	private ExifSubIFDDirectory exifDir;
	
	public Parser(File file) throws IOException, ImageProcessingException {
		this.image = ImageIO.read(file);
		this.metadata = ImageMetadataReader.readMetadata(file);
		this.sysDir = metadata.getFirstDirectoryOfType(FileSystemDirectory.class);
		this.typeDir = metadata.getFirstDirectoryOfType(FileTypeDirectory.class);
		this.exifDir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
	}
	
	public String getCreationDate() {
		Date date;
		if (exifDir != null) {
			date = exifDir.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
		} else {
			date = sysDir.getDate(FileSystemDirectory.TAG_FILE_MODIFIED_DATE);
		}
		return String.valueOf(date.getTime());
	}
	
	public String[] getResolution() {
		return new String[]{String.valueOf(image.getWidth()), String.valueOf(image.getHeight())};
	}
	
	public String getColorRange() {
		Set<Integer> colors = new HashSet<>();
		int height = image.getHeight();
		int width = image.getWidth();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = image.getRGB(x, y);
				colors.add(pixel);
			}
		}
		
		return String.valueOf(colors.size());
	}
	
	public String getFileSize() {
		String size = sysDir.getDescription(FileSystemDirectory.TAG_FILE_SIZE);
		
		return size.substring(0, size.indexOf(" "));
	}
	
	public String getFileExtension() {
		return typeDir.getDescription(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME);
	}
	
	public String getFileName() {
		return sysDir.getDescription(FileSystemDirectory.TAG_FILE_NAME);
	}
	
}
