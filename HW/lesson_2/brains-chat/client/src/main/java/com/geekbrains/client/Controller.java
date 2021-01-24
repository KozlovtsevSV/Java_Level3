package com.geekbrains.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    TextArea textArea;

    @FXML
    TextField msgField, loginField, loginRenameField, passRenameField, newLoginField;

    @FXML
    HBox msgPanel, authPanel, renamePanel;

    @FXML
    PasswordField passField;

    @FXML
    ListView<String> clientsList;

    @FXML
    Button renameButton;

    private boolean authenticated;
    private boolean renameUser;

    private String nickname;

    public void setAuthenticated(boolean authenticated, boolean renameUser) {
        this.authenticated = authenticated;
        this.renameUser = renameUser;

        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        msgPanel.setVisible(authenticated);
        msgPanel.setManaged(authenticated);
        clientsList.setVisible(authenticated);
        clientsList.setManaged(authenticated);
        renameButton.setVisible(authenticated);
        renameButton.setManaged(authenticated);

        renamePanel.setVisible(renameUser);
        renamePanel.setManaged(renameUser);

        if (!authenticated) {
            nickname = "";
            setRename(false);
        }
        else {
            setRename(this.renameUser);
        }
    }

    public void setRename(boolean renameUser) {
         this.renameUser = renameUser;
        renamePanel.setVisible(renameUser);
        renamePanel.setManaged(renameUser);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthenticated(false, false);
        clientsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String nickname = clientsList.getSelectionModel().getSelectedItem();
                msgField.setText("/w " + nickname + " ");
                msgField.requestFocus();
                msgField.selectEnd();
            }
        });
        linkCallbacks();
    }

    public void sendAuth() {
        Network.sendAuth(loginField.getText(), passField.getText());
        loginField.clear();
        passField.clear();
    }

    public void sendRename() {
        Network.sendRenameUser(loginRenameField.getText(), passRenameField.getText(),newLoginField.getText());
        passRenameField.clear();
        newLoginField.clear();
    }

    public void clickRenameButton() {
        this.renameUser = ! this.renameUser;
        loginRenameField.setText(nickname);
        setRename(this.renameUser);
        renameButton.setVisible(!this.renameUser);
    }


    public void sendMsg() {
        if (Network.sendMsg(msgField.getText())) {
            msgField.clear();
            msgField.requestFocus();
        }
    }

    public void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
            alert.showAndWait();
        });
    }

    public void linkCallbacks() {
        Network.setCallOnException(args -> showAlert(args[0].toString()));

        Network.setCallOnCloseConnection(args -> setAuthenticated(false, false));

        Network.setCallOnAuthenticated(args -> {
            setAuthenticated(true, false);
            nickname = args[0].toString();
        });

        Network.setCallOnMsgReceived(args -> {
            String msg = args[0].toString();
            if (msg.startsWith("/")) {
                if (msg.startsWith("/clients ")) {
                    String[] tokens = msg.split("\\s");
                    Platform.runLater(() -> {
                        clientsList.getItems().clear();
                        for (int i = 1; i < tokens.length; i++) {
                            clientsList.getItems().add(tokens[i]);
                        }
                    });
                }
                else if(msg.startsWith("/renameUser ")) {
                    String[] tokens = msg.split("\\s");
                    nickname = tokens[1];
                    this.renameUser = !this.renameUser;
                    setRename(this.renameUser);
                    renameButton.setVisible(!this.renameUser);
                }

            } else {
                textArea.appendText(msg + "\n");
            }
        });
    }
}