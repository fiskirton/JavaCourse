package org.collection;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;


public class Image {
	
	private final File file;
	
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
	
	public BufferedImage scaleImage(File originImageFile, int newWidth, int newHeight) throws IOException {
		BufferedImage originImage = ImageIO.read(originImageFile);
		BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, originImage.getType());
		Graphics2D graphics = scaledImage.createGraphics();
		graphics.drawImage(originImage, 0, 0, newWidth, newHeight, null);
		graphics.dispose();
		return scaledImage;
	}
	
	public List<RGBColor> getPixelsDifference(BufferedImage image) {
		int height = image.getHeight();
		int width = image.getWidth();
		List<RGBColor> pixelsDiff = new ArrayList<>();
		RGBColor currentPixel = RGBColor.getRGBColor(0, 0, 0);
		RGBColor nextPixel;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixel = image.getRGB(x, y);
				nextPixel = RGBColor.getScaledColor(pixel);
				if (x != 0 || y != 0) {
					pixelsDiff.add(
							RGBColor.getRGBColor(
									Math.abs(nextPixel.getRed() - currentPixel.getRed()),
									Math.abs(nextPixel.getGreen() - currentPixel.getGreen()),
									Math.abs(nextPixel.getBlue() - currentPixel.getBlue())
							)
					);
				}
				currentPixel = nextPixel;
			}
		}
		return pixelsDiff;
	}
	
	public double getDifferenceBetweenImages(List<RGBColor> pixelDiff1, List<RGBColor> pixelDiff2) {
		double difference;
		int length = pixelDiff1.size();
		difference = IntStream.range(0, length)
				.parallel()
				.mapToDouble(i -> pixelDiff1.get(i).getDifference(pixelDiff2.get(i)))
				.sum();
		return difference;
	}
	
	public String getMostSimilarImgPath(List<Image> images) throws IOException {
		double minDiff = Double.POSITIVE_INFINITY;
		double newDiff;
		String mostSimilarImgPath = null;
		int commonWidth = 1024;
		int commonHeight = 768;
		BufferedImage scaledMainImg = scaleImage(this.file, commonWidth, commonHeight);
		BufferedImage scaledCompareImg;
		List<RGBColor> mainImgPixelsDifference = getPixelsDifference(scaledMainImg);
		for (Image image : images) {
			if (!image.equals(this)) {
				scaledCompareImg = scaleImage(image.file, commonWidth, commonHeight);
				List<RGBColor> compareImgPixelsDifference = getPixelsDifference(scaledCompareImg);
				newDiff = getDifferenceBetweenImages(mainImgPixelsDifference, compareImgPixelsDifference);
				if (newDiff < minDiff) {
					minDiff = newDiff;
					mostSimilarImgPath = image.file.getPath();
				}
			}
		}
		return mostSimilarImgPath;
	}
	
	@Override
	public String toString() {
		StringBuilder properties = new StringBuilder();
		properties.append("Image properties:\n");
		imageProperties.forEach(
				(k, v) -> properties.append(k).append(" : ").append(v).append("\n")
		);
		
		properties.append("File properties:\n");
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

class RGBColor {
	private int red;
	private int green;
	private int blue;
	
	private RGBColor(int pixel) {
		this.red = (pixel & 0x00ff0000) >> 16;
		this.green = (pixel & 0x0000ff00) >> 8;
		this.blue = pixel & 0x000000ff;
	}
	
	private RGBColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	public static RGBColor getRGBColor(int pixel) {
		return new RGBColor(pixel);
	}
	
	public static RGBColor getRGBColor(int red, int green, int blue) {
		return new RGBColor(red, green, blue);
	}
	
	public int getRed() {
		return red;
	}
	
	public int getGreen() {
		return green;
	}
	
	public int getBlue() {
		return blue;
	}
	
	public static RGBColor getScaledColor(int pixel) {
		RGBColor rgbColor = getRGBColor(pixel);
		rgbColor.red = (int) Math.round(rgbColor.red / 28.3);
		rgbColor.green = (int) Math.round(rgbColor.green / 28.3);
		rgbColor.blue = (int) Math.round(rgbColor.blue / 28.3);
		return rgbColor;
	}
	
	public double getDifference(RGBColor otherColor) {
		double diff = 0;
		diff += Math.abs(this.red - otherColor.getRed());
		diff += Math.abs(this.green - otherColor.getGreen());
		diff += Math.abs(this.blue - otherColor.getBlue());
		return diff;
	}
}
