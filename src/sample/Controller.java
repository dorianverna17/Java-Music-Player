package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.internal.module.ModuleInfo;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class Controller {
    private ModuleInfo ImageIO;

    public void play_song() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = null;
        file = fileChooser.showOpenDialog(new Stage());
        String ext = null;
        if (file != null)
            ext = file.getName().substring(file.getName().indexOf(".") + 1);
        if (ext != null) {
            if (ext.equalsIgnoreCase("m4a")
                    || ext.equalsIgnoreCase("flac")
                    || ext.equalsIgnoreCase("mp3")
                    || ext.equalsIgnoreCase("mp4")
                    || ext.equalsIgnoreCase("wav")
                    || ext.equalsIgnoreCase("wma")
                    || ext.equalsIgnoreCase("aac")
                    || ext.equalsIgnoreCase("aifc")) {
                try {
                    File audioFile = new File(file.getPath());
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                    AudioFormat format = audioStream.getFormat();
                    DataLine.Info info = new DataLine.Info(Clip.class, format);
                    Clip audioClip = (Clip) AudioSystem.getLine(info);
                    audioClip.open(audioStream);
                    audioClip.start();
                } catch (Exception e) {
                    String workingDir = System.getProperty("user.dir");
                    System.out.println("Current working directory : " + workingDir);
                    e.printStackTrace();
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The file choosen must be an audio file");
                errorAlert.showAndWait();
            }
        }
    }

    public void create_playlist() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = null;
        file = fileChooser.showOpenDialog(new Stage());
        String ext = null;
        if (file != null)
            ext = file.getName().substring(file.getName().indexOf(".") + 1);
        if (ext != null) {
            if (ext.equalsIgnoreCase("m4a")
                    || ext.equalsIgnoreCase("flac")
                    || ext.equalsIgnoreCase("mp3")
                    || ext.equalsIgnoreCase("mp4")
                    || ext.equalsIgnoreCase("wav")
                    || ext.equalsIgnoreCase("wma")
                    || ext.equalsIgnoreCase("aac")
                    || ext.equalsIgnoreCase("aifc")) {
                try {
                    File audioFile = new File(file.getPath());
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                    AudioFormat format = audioStream.getFormat();
                    DataLine.Info info = new DataLine.Info(Clip.class, format);
                    Clip audioClip = (Clip) AudioSystem.getLine(info);
                    audioClip.open(audioStream);
                    audioClip.start();
                } catch (Exception e) {
                    String workingDir = System.getProperty("user.dir");
                    System.out.println("Current working directory : " + workingDir);
                    e.printStackTrace();
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The file choosen must be an audio file");
                errorAlert.showAndWait();
            }
        }
    }
}
