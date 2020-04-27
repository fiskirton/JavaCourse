package org.collection;

import com.drew.imaging.ImageProcessingException;

import java.io.File;
import java.io.IOException;

public class ImageCreator {
	
	public Image create(String url) {
		File file = new File(url);
		
		Parser parser = null;
		try {
			parser = new Parser(file);
		} catch (ImageProcessingException | IOException e) {
			e.printStackTrace();
		}
		
		if (parser == null){
			return null;
		}
		
		Image image = new Image(file);
		
		image.addImageProperty("range", parser.getColorRange());
		String[] resolution = parser.getResolution();
		image.addImageProperty("width", resolution[0]);
		image.addImageProperty("height", resolution[1]);
		
		image.addFileProperty("filename", parser.getFileName());
		image.addFileProperty("size", parser.getFileSize());
		image.addFileProperty("extension", parser.getFileExtension());
		image.addFileProperty("date", parser.getCreationDate());
		
		return image;
	}
}
