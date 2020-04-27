package org.collection;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Image {
	
	private File file;
	
	private Map<String, String> imageProperties;
	private Map<String, String> fileProperties;
	
	Image (File file) {
		this.file = file;
		this.imageProperties = new HashMap<>();
		this.fileProperties = new HashMap<>();
	}
	
	public void addImageProperty(String key, String value) {
		imageProperties.put(key, value);
	}
	
	public void addFileProperty(String key, String value) {
		fileProperties.put(key, value);
	}
	
	public String getImageProperty(String key) {
		return imageProperties.get(key);
	}
	
	public String getFileProperty(String key) {
		return fileProperties.get(key);
	}
	
	public File getFile() {
		return file;
	}
	
	@Override
	public String toString() {
		StringBuilder properties = new StringBuilder();
		properties.append("Image properties\n");
		imageProperties.forEach(
				(k, v) -> properties.append(k).append(" : ").append(v).append("\n")
		);
		
		properties.append("File properties\n");
		for (Map.Entry<String, String> pair: fileProperties.entrySet()) {
			if (pair.getKey().equals("date")){
				LocalDateTime zd = LocalDateTime.ofInstant(
						Instant.ofEpochMilli(Long.parseLong(pair.getValue())),
						ZoneId.systemDefault()
				);
				String formatted = zd.format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy"));
				properties.append(pair.getKey()).append(" : ").append(formatted).append("\n");
			} else {
				properties.append(pair.getKey()).append(" : ").append(pair.getValue()).append("\n");
			}
		}
		
		return properties.toString();
	}
}
