package org.gui.Controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import org.collection.Image;
import org.collection.ImageCreator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController implements Initializable {
	
	List<Image> images = null;
	ImageCreator imageCreator;
	Map<String, Comparator<Image>> comparators = Map.ofEntries(
			Map.entry("filename", Comparator.comparing(img -> img.getFileProperty("filename"))),
			Map.entry("range", Comparator.comparing(img -> img.getImageProperty("range"))),
			Map.entry("size", Comparator.comparing(img -> Integer.parseInt(img.getFileProperty("size")))),
			Map.entry("extension", Comparator.comparing(img -> img.getFileProperty("extension"))),
			Map.entry("date", Comparator.comparing(img -> Long.parseLong(img.getFileProperty("date"))))
	);
	
	@FXML
	private FlowPane imagesPane;
	
	@FXML
	private TextArea properties;
	
	EventHandler<MouseEvent> imageClickHandler = e -> {
		properties.clear();
		ImageView imageView = (ImageView) e.getTarget();
		int id = Integer.parseInt(imageView.getId());
		Image image = images.get(id);
		properties.appendText(image.toString());
		properties.appendText("\n");
	};
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String urls = "/home/fiskirton/Documents/Projects/repos/JavaCourse/ImageKeeper/src/main/resources/urls";
		
		imageCreator = new ImageCreator();
		
		try (Stream<String> stream = Files.lines(Paths.get(urls))){
			images = stream
					.map(imageCreator::create)
					.collect(Collectors.toList());
			
		} catch (IOException ex){
			ex.printStackTrace();
		}
		
		fillPane();
		
	}
	
	public void fillPane(){
		int id = 0;
		for (Image image : images) {
			javafx.scene.image.ImageView imageView = new ImageView(createNewImage(image.getFile()));
			imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, imageClickHandler);
			imageView.setId(String.valueOf(id));
			imagesPane.getChildren().add(imageView);
			id++;
		}
	}
	
	public String getPath(File file){
		String url = null;
		try {
			url = file.toURI().toURL().toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return url;
	}
	
	public javafx.scene.image.Image createNewImage(File file){
		return new javafx.scene.image.Image(getPath(file), 200, 100, false, false);
	}
	
	public void repaint(){
		int n = images.size();
		Image image;
		ImageView view;
		for (int i = 0; i < n; ++i){
			image = images.get(i);
			view = (ImageView) imagesPane.getChildren().get(i);
			view.setImage(createNewImage(image.getFile()));
		}
	}
	
	@FXML
	private void sortByFilename(){
		images.sort(comparators.get("filename"));
		repaint();
	}
	
	@FXML
	private void sortBySize(){
		images.sort(comparators.get("size"));
		repaint();
	}
	
	@FXML
	private void sortByExtension(){
		images.sort(comparators.get("extension"));
		repaint();
	}
	
	@FXML
	private void sortByDate(){
		images.sort(comparators.get("date"));
		repaint();
	}
	
}

