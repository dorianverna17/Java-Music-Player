package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.util.LinkedList;

public class Sample2 {

    String playlistname = null;

    @FXML
    private TextField textfield;

    @FXML
    private Button createbutton;

    public void nameplaylist(ActionEvent actionEvent) throws Exception {
        if(actionEvent.getSource() == createbutton) {
            this.playlistname = textfield.getText();
            PrintWriter writer = null;
            try {
                if (playlistname != "") {
                    playlistname += ".txt";
                    writer = new PrintWriter(playlistname, "UTF-8");
                }
            } catch (Exception e){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The name is not valid");
                errorAlert.showAndWait();
            }
        }
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
