package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.internal.module.ModuleInfo;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller{
    @FXML
    private Pane pane;

    @FXML
    private Label label1;

    @FXML
    private Slider slider_volume;

    Clip audioClip = null;
    File file;

    public void play_song() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        file = null;
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
                    if (audioClip != null) {
                        audioClip.close();
                    }
                    audioClip = (Clip) AudioSystem.getLine(info);
                    audioClip.open(audioStream);
                    afisare_melodie();
                    audioClip.start();

                    FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
                    double gain = 0.5; // number between 0 and 1 (loudest)
                    slider_volume.setValue(0.5);
                    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                    gainControl.setValue(dB);
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
        Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
        Image icon = new Image(getClass().getResourceAsStream("Icon.png"));
        Stage stage = new Stage();
        stage.getIcons().add(icon);
        stage.setTitle("DodePlayer");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void afisare_melodie() {
        if (file != null) {
            label1.setText(file.getName());
        }
    }

    public void stop_melodie() {
        if (audioClip != null) {
            audioClip.close();
            label1.setText("");
        }
    }

    public void play_pause() {
        if (audioClip != null) {
            if (audioClip.isRunning())
                audioClip.stop();
            else
                audioClip.start();
        }
    }

    public void setvolume() {
        if (audioClip != null) {
            FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            Float value = (float) slider_volume.getValue();
            float dB = (float) (Math.log(value) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }

    public void choose_playlist() {
    }
}
