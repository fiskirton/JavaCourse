package org.gui.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;

import javafx.stage.Stage;
import org.collection.Image;
import org.collection.ImageCreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainController implements Initializable {
	
	List<Image> images = null;
	Set<String> urls;
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
	
	private ImageView targetImage;
	private FileChooser fileChooser;
	private Stage stage;
	
	private static final String IMAGE_URLS = "src/main/resources/urls.txt";
	
	EventHandler<MouseEvent> imageClickHandler = e -> {
		properties.clear();
		targetImage = (ImageView) e.getTarget();
		int id = Integer.parseInt(targetImage.getId());
		Image image = images.get(id);
		properties.appendText(image.toString());
	};
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fileChooser= new FileChooser();
		urls = new HashSet<>();
		try (Stream<String> stream = Files.lines(Paths.get(IMAGE_URLS))){
			stream.forEach(urls::add);
			images = urls.stream()
					.map(ImageCreator::create)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
		} catch (IOException ex){
			ex.printStackTrace();
		}
		fillPane();
	}
	
	public void fillPane(){
		int id = 0;
		for (Image image : images) {
			imagesPane.getChildren().add(makeImgView(image, id));
			id++;
		}
	}
	
	public ImageView makeImgView(Image image, int id) {
		javafx.scene.image.ImageView imageView = new ImageView(createNewImage(image.getFile()));
		imageView.addEventFilter(MouseEvent.MOUSE_CLICKED, imageClickHandler);
		imageView.setId(String.valueOf(id));
		return imageView;
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
	
	@FXML
	private void findMostSimilar() {
		if (targetImage != null) {
			int targetId = Integer.parseInt(targetImage.getId());
			try {
				String mostSimilarImgPath = images.get(targetId).getMostSimilarImgPath(images);
				properties.appendText("most similar image path: ");
				properties.appendText(mostSimilarImgPath);
				properties.appendText("\n");
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	@FXML
	private void openFileChooser() throws IOException {
		List<File> files = fileChooser.showOpenMultipleDialog(stage);
		Set<String> formats = new HashSet<>(Arrays.asList("jpg", "jpeg", "png"));
		files = files.stream()
				.filter(path -> formats.contains(path.getAbsolutePath().substring(path.getAbsolutePath().lastIndexOf('.') + 1)))
				.collect(Collectors.toList());
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(IMAGE_URLS), StandardOpenOption.APPEND)) {
			for (File file : files) {
				try {
					if (urls.contains(file.getAbsolutePath())) {
						continue;
					}
					Image newImage = ImageCreator.create(file.getAbsolutePath());
					images.add(newImage);
					urls.add(file.getAbsolutePath());
					assert newImage != null;
					imagesPane.getChildren().add(makeImgView(newImage, images.size() - 1));
					repaint();
					writer.write("\n" + file.getAbsolutePath());
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}
	
	@FXML
	private void deletePickedImage() {
		if (targetImage == null) {
			return;
		}
		int id = Integer.parseInt(targetImage.getId());
		String deleteUrl = images.get(id).getFile().getAbsolutePath();
		images.remove(id);
		urls.remove(deleteUrl);
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(IMAGE_URLS))) {
			for (String url: urls) {
				writer.write(url + "\n");
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		imagesPane.getChildren().remove(id);
		properties.clear();
		reindex();
		repaint();
	}
	
	public void reindex() {
		int id = 0;
		for (Node view: imagesPane.getChildren()) {
			view.setId(String.valueOf(id));
			id++;
		}
	}

	public void closeApp() {
		stage.close();
	}
}

