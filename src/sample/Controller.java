package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.FXCollections.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.internal.module.ModuleInfo;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.sound.midi.SysexMessage;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

//for mp3s
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.*;

public class Controller {
    @FXML
    private Pane pane;

    @FXML
    public Label label1;

    @FXML
    private Slider slider_volume;

    @FXML
    public Label label2;

    @FXML
    public ListView<?> viewplaylist;

    @FXML
    public Button addsong;

    @FXML
    public Button removesong;

    @FXML
    public Button listenplaylist;

    @FXML
    private ListView<?> listofplaylists;

    @FXML
    private Button selectplaylistbutton;

    ObservableList list2;
    public static ObservableList list;
    Clip audioClip = null;
    File file;
    public static File playlist_file;
    Thread additionalthread = null;

    //for mp3s
    MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        try {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            File userDir = new File(s);
            File[] allfiles = userDir.listFiles();
            LinkedList<String> list = new LinkedList<String>();
            for (int i = 0; i < allfiles.length; i++) {
                if (allfiles[i].toString().endsWith(".txt"))
                    list.add(allfiles[i].toString().substring(0, allfiles[i].toString().indexOf(".txt")));
            }
            list2 = FXCollections.observableArrayList(list);
            listofplaylists.setItems(list2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play_song() throws Exception {
        if (additionalthread != null)
            additionalthread.stop();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        file = null;
        file = fileChooser.showOpenDialog(new Stage());
        String ext = null;
        if (file != null)
            ext = file.getName().substring(file.getName().indexOf(".") + 1);
        if (ext != null) {
            if (ext.equalsIgnoreCase("mp3")
                    || ext.equalsIgnoreCase("wav")
                    || ext.equalsIgnoreCase("aifc")) {
                try {
                    if (ext.equals("aifc") || ext.equals("wav")) {
                        File audioFile = new File(file.getPath());
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                        AudioFormat format = audioStream.getFormat();
                        DataLine.Info info = new DataLine.Info(Clip.class, format);
                        if (audioClip != null) {
                            audioClip.close();
                        }
                        if (audioClip != null) {
                            if (audioClip.isRunning())
                                audioClip.stop();
                        }
                        if (mediaPlayer != null) {
                            boolean playing = mediaPlayer.getStatus().equals(Status.PLAYING);
                            if (playing == true)
                                mediaPlayer.stop();
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
                    }
                    if (ext.equals("mp3")) {
                        String string_path = file.getPath();
                        Media music = new Media(new File(string_path).toURI().toString());
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                        }
                        if (audioClip != null) {
                            if (audioClip.isRunning())
                                audioClip.stop();
                        }
                        if (mediaPlayer != null) {
                            boolean playing = mediaPlayer.getStatus().equals(Status.PLAYING);
                            if (playing == true)
                                mediaPlayer.stop();
                        }
                        mediaPlayer = new MediaPlayer(music);
                        afisare_melodie();
                        mediaPlayer.play();
                        slider_volume.setValue(0.5);
                        mediaPlayer.setVolume(slider_volume.getValue());
                    }
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
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            label1.setText("");
        }
        if (additionalthread != null)
            additionalthread.stop();
        removesong.setDisable(false);
        if (viewplaylist.getItems().size() == 0)
            removesong.setDisable(true);
    }

    public void play_pause() {
        if (audioClip != null) {
            if (audioClip.isRunning())
                audioClip.stop();
            else
                audioClip.start();
        }
        if (mediaPlayer != null) {
            boolean playing = mediaPlayer.getStatus().equals(Status.PLAYING);
            if (playing == true)
                mediaPlayer.pause();
            else
                mediaPlayer.play();
        }
    }

    public void setvolume() {
        if (audioClip != null) {
            FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            Float value = (float) slider_volume.getValue();
            float dB = (float) (Math.log(value) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
        if (mediaPlayer != null) {
            mediaPlayer.setVolume((float) slider_volume.getValue());
        }
    }

    public void choose_playlist() throws Exception {
        try {
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            File userDir = new File(s);
            File[] allfiles = userDir.listFiles();
            LinkedList<String> list = new LinkedList<String>();
            for (int i = 0; i < allfiles.length; i++) {
                if (allfiles[i].toString().endsWith(".txt"))
                    list.add(allfiles[i].toString().substring(0, allfiles[i].toString().indexOf(".txt")));
            }
            list2 = FXCollections.observableArrayList(list);
            listofplaylists.setItems(list2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        addsong.setVisible(false);
        removesong.setVisible(false);
        listenplaylist.setVisible(false);
        listofplaylists.setVisible(true);
        selectplaylistbutton.setVisible(true);
    }

    public void addsong_toplaylist(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        File file1 = null;
        file1 = fileChooser.showOpenDialog(new Stage());
        String ext = null;
        if (file1 != null)
            ext = file1.getName().substring(file1.getName().indexOf(".") + 1);
        if (ext != null) {
            if (ext.equalsIgnoreCase("mp3")
                    || ext.equalsIgnoreCase("wav")
                    || ext.equalsIgnoreCase("aifc")) {
                FileWriter fw;
                BufferedWriter bw;
                PrintWriter out;
                try {
                    fw = new FileWriter(playlist_file, true);
                    bw = new BufferedWriter(fw);
                    out = new PrintWriter(bw);
                    out.println(file1.getAbsolutePath());
                    out.flush();
                    LinkedList<String> list_aux = new LinkedList<String>();

                    try (BufferedReader br = new BufferedReader(new FileReader(playlist_file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            list_aux.add(line.substring(line.lastIndexOf("\\") + 1));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    list = FXCollections.observableArrayList(list_aux);
                    viewplaylist.setItems(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removesong_fromplaylist(ActionEvent actionEvent) throws Exception {
        if (actionEvent.getSource() == removesong) {
            LinkedList<String> list_aux = new LinkedList<String>();
            LinkedList<String> list_aux2 = new LinkedList<String>();

            BufferedReader br = new BufferedReader(new FileReader(playlist_file));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    list_aux.add(line.substring(line.lastIndexOf("\\") + 1));
                    list_aux2.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            br.close();

            int index;
            if (list_aux.contains(viewplaylist.getSelectionModel().getSelectedItem())) {
                index = list_aux.indexOf(viewplaylist.getSelectionModel().getSelectedItem());
                list_aux.remove(viewplaylist.getSelectionModel().getSelectedItem());
                list_aux2.remove(index);
            }

            int i;
            FileWriter fw = new FileWriter(playlist_file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            LinkedList<String> list_aux3 = new LinkedList<String>();
            for (i = 0; i < list_aux2.size(); i++) {
                out.println(list_aux2.get(i));
            }
            out.flush();
            br = new BufferedReader(new FileReader(playlist_file));
            try {
                String line;
                while ((line = br.readLine()) != null) {
                    list_aux3.add(line.substring(line.lastIndexOf("\\") + 1));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            list = FXCollections.observableArrayList(list_aux3);
            viewplaylist.setItems(list);
        }
    }

    public void listen_playlist(ActionEvent actionEvent) throws Exception {
        try {
            removesong.setDisable(true);
            Thread t = new Thread() {
                public void run() {
                    LinkedList<String> list_aux = new LinkedList<String>();
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(playlist_file));
                    } catch (Exception e) { }
                    int i;
                    try {
                        String line;
                        while ((line = br.readLine()) != null) {
                            list_aux.add(line);
                        }
                        int index = viewplaylist.getSelectionModel().getSelectedIndex();
                        if (index == -1)
                            index = 0;
                        for (i = index; i < list_aux.size(); i++) {

                            list_aux = new LinkedList<String>();
                            try {
                                br = new BufferedReader(new FileReader(playlist_file));
                            } catch (Exception e) { }
                            String line1;
                            while ((line1 = br.readLine()) != null) {
                                list_aux.add(line1);
                            }

                            file = new File(list_aux.get(i));
                            Platform.runLater(new Runnable(){
                                public void run() {
                                    label1.setText(file.getName());
                                }
                            });
                            File audioFile = new File(list_aux.get(i));
                            AudioInputStream audioStream = null;
                            String ext = null;
                            if (list_aux.get(i) != null)
                                ext = file.getName().substring(file.getName().indexOf(".") + 1);
                            try {
                                if (ext.equals("aifc") || ext.equals("wav")) {
                                    audioStream = AudioSystem.getAudioInputStream(audioFile);
                                    AudioFormat format = audioStream.getFormat();
                                    DataLine.Info info = new DataLine.Info(Clip.class, format);

                                    if (audioClip != null) {
                                        audioClip.close();
                                    }
                                    if (audioClip != null) {
                                        if (audioClip.isRunning())
                                            audioClip.stop();
                                    }
                                    if (mediaPlayer != null) {
                                        boolean playing = mediaPlayer.getStatus().equals(Status.PLAYING);
                                        if (playing == true)
                                            mediaPlayer.stop();
                                    }
                                    audioClip = (Clip) AudioSystem.getLine(info);
                                    audioClip.open(audioStream);
                                    audioClip.start();

                                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                                    AudioFormat format1 = audioInputStream.getFormat();
                                    long frames = audioInputStream.getFrameLength();
                                    double durationInSeconds = (frames + 0.0) / format.getFrameRate();

                                    FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
                                    double gain = 0.5; // number between 0 and 1 (loudest)
                                    slider_volume.setValue(0.5);
                                    float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                                    gainControl.setValue(dB);

                                    this.sleep((long) durationInSeconds * 1000);
                                }
                                if (ext.equals("mp3")) {
                                    String string_path = file.getPath();
                                    Media music = new Media(new File(string_path).toURI().toString());

                                    if (mediaPlayer != null) {
                                        mediaPlayer.stop();
                                    }
                                    if (audioClip != null) {
                                        if (audioClip.isRunning())
                                            audioClip.stop();
                                    }
                                    if (mediaPlayer != null) {
                                        boolean playing = mediaPlayer.getStatus().equals(Status.PLAYING);
                                        if (playing == true)
                                            mediaPlayer.stop();
                                    }
                                    mediaPlayer = new MediaPlayer(music);
                                    afisare_melodie();
                                    mediaPlayer.play();
                                    slider_volume.setValue(0.5);
                                    mediaPlayer.setVolume(slider_volume.getValue());

                                    this.sleep(2000);
                                    double duration = music.getDuration().toSeconds();
                                    this.sleep((long)duration * 1000);
                                }
                            } catch (Exception ex) { }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            additionalthread = t;
            t.setDaemon(true);
            t.start();
        } catch (Exception exe) { }
    }

    public void selectthisplaylist(ActionEvent actionEvent) {
        if (actionEvent.getSource() == selectplaylistbutton) {
            if (listofplaylists.getSelectionModel().getSelectedItem() != null) {
                playlist_file = new File(listofplaylists.getSelectionModel().getSelectedItem() + ".txt");
                if (playlist_file != null) {
                    String string = playlist_file.getName().substring(0, playlist_file.getName().indexOf(".txt"));
                    label2.setText(string);
                    addsong.setVisible(true);
                    removesong.setVisible(true);
                    listenplaylist.setVisible(true);
                    viewplaylist.setVisible(true);
                    LinkedList<String> list_aux = new LinkedList<String>();

                    try (BufferedReader br = new BufferedReader(new FileReader(playlist_file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            list_aux.add(line.substring(line.lastIndexOf("\\") + 1));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    list = FXCollections.observableArrayList(list_aux);
                    if (list.size() == 0)
                        removesong.setDisable(true);
                    viewplaylist.setItems(list);
                }
                listofplaylists.setVisible((false));
                selectplaylistbutton.setVisible(false);
            }
        }
    }
}
