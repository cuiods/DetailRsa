package com.cuiods.cryptology.rsa.controller;

import com.cuiods.cryptology.rsa.rsa.RSASignature;
import com.cuiods.cryptology.rsa.util.FileUtil;
import com.cuiods.cryptology.rsa.util.StringConvert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RSAController implements Initializable {

    private RSASignature signature = new RSASignature();

    @FXML private ChoiceBox<String> method;
    @FXML private TextArea genN;
    @FXML private TextArea genE;
    @FXML private TextArea genP;
    @FXML private TextArea genQ;
    @FXML private TextArea genD;

    @FXML private ChoiceBox<String> enMethod;
    @FXML private TextArea enN;
    @FXML private TextArea enE;
    @FXML private TextArea enMessage;
    @FXML private TextArea enEncrypted;

    @FXML private ChoiceBox<String> deMethod;
    @FXML private TextArea deP;
    @FXML private TextArea deQ;
    @FXML private TextArea deD;
    @FXML private TextArea deEncrypted;
    @FXML private TextArea deMessage;

    private FileChooser chooser = new FileChooser();
    private File inputFile = null;
    private File outPutFile = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        method.setItems(FXCollections.observableArrayList("RSA-256", "RSA-512", "RSA-768", "RSA-1024", "RSA-2048"));
        method.setValue("RSA-768");
        enMethod.setItems(FXCollections.observableArrayList("RSA-256", "RSA-512", "RSA-768", "RSA-1024", "RSA-2048"));
        enMethod.setValue("RSA-768");
        deMethod.setItems(FXCollections.observableArrayList("RSA-256", "RSA-512", "RSA-768", "RSA-1024", "RSA-2048"));
        deMethod.setValue("RSA-768");
    }

    @FXML
    protected void generateKeys() {
        String[] keys = signature.generateKeys(getRSABit(method.getValue()));
        String N = StringConvert.convert(keys[0],10,16);
        String E = StringConvert.convert(keys[1],10,16);
        String D = StringConvert.convert(keys[2],10,16);
        String P = StringConvert.convert(keys[3],10,16);
        String Q = StringConvert.convert(keys[4],10,16);
        genN.setText(N);
        genE.setText(E);
        genD.setText(D);
        genP.setText(P);
        genQ.setText(Q);
        enE.setText(E);
        enN.setText(N);
        deD.setText(D);
        deP.setText(P);
        deQ.setText(Q);
        enMethod.setValue(method.getValue());
        deMethod.setValue(method.getValue());
    }

    @FXML
    protected void resetGen() {
        signature = new RSASignature();
        genN.clear();
        genE.clear();
        genD.clear();
        genP.clear();
        genQ.clear();
        enE.clear();
        enN.clear();
        deD.clear();
        deP.clear();
        deQ.clear();
        enMessage.clear();
        enEncrypted.clear();
        deMessage.clear();
        deEncrypted.clear();
    }

    @FXML
    protected void encrypt() {
        int bit = getRSABit(enMethod.getValue());
        String N = enN.getText();
        String E = enE.getText();
        String message = enMessage.getText();
        if (bit <= 0 || N.isEmpty() || E.isEmpty() || message.isEmpty()) {
            showAlert("参数不足", "请生成公钥并填写需加密文字后继续操作", Alert.AlertType.WARNING);
            return;
        }
        String enM = signature.encryption(message, StringConvert.convert(E,16,10),
                StringConvert.convert(N, 16, 10), bit/2);
        enEncrypted.setText(enM);
        deEncrypted.setText(enM);
    }

    @FXML
    protected void decrypt() {
        int bit = getRSABit(deMethod.getValue());
        String D = deD.getText();
        String P = deP.getText();
        String Q = deQ.getText();
        String enM = deEncrypted.getText();
        if (bit <= 0 || D.isEmpty() || P.isEmpty() || Q.isEmpty() || enM.isEmpty()) {
            showAlert("参数不足", "请生成私钥并填写已加密文字后继续操作", Alert.AlertType.WARNING);
            return;
        }
        String message = signature.decryption(enM, StringConvert.convert(D, 16, 10),
                StringConvert.convert(P,16,10), StringConvert.convert(Q,16,10), bit/2);
        deMessage.setText(message);
    }

    @FXML
    protected void genSaveN() {
        save("N", genN);
    }

    @FXML
    protected void genSaveE() {
        save("E", genE);
    }

    @FXML
    protected void genSaveD() {
        save("D", genD);
    }

    @FXML
    protected void genSaveP() {
        save("P", genP);
    }

    @FXML
    protected void genSaveQ() {
        save("Q", genQ);
    }

    @FXML
    protected void enSaveEn() {
        save("encryted", enEncrypted);
    }

    @FXML
    protected void deSaveMessage() {
        save("message", deMessage);
    }

    @FXML
    protected void enLoadN() {
        load(enN);
    }

    @FXML
    protected void enLoadE() {
        load(enE);
    }

    @FXML
    protected void deLoadP() {
        load(deP);
    }

    @FXML
    protected void deLoadQ() {
        load(deQ);
    }

    @FXML
    protected void deLoadD() {
        load(deD);
    }

    @FXML
    protected void deLoadEn() {
        load(deEncrypted);
    }

    @FXML
    protected void saveAllPublic() {
        String N = genN.getText();
        String E = genE.getText();
        if (N.isEmpty() || E.isEmpty()) {
            showAlert("空文件", "请生成公钥后继续操作", Alert.AlertType.WARNING);
            return;
        }
        FileUtil.save(N, "N.key");
        FileUtil.save(E, "E.key");
        showAlert("成功", "文件已保存至当前目录", Alert.AlertType.INFORMATION);
    }

    @FXML
    protected void saveAllPrivate() {
        String P = genP.getText();
        String Q = genQ.getText();
        String D = genD.getText();
        if (P.isEmpty() || Q.isEmpty() || D.isEmpty()) {
            showAlert("空文件", "请生成秘钥后继续操作", Alert.AlertType.WARNING);
            return;
        }
        FileUtil.save(P, "P.key");
        FileUtil.save(Q, "Q.key");
        FileUtil.save(D, "D.key");
        showAlert("成功", "文件已保存至当前目录", Alert.AlertType.INFORMATION);
    }

    @FXML
    protected void enResetPublic() {
        enN.clear();
        enE.clear();
    }

    @FXML
    protected void enResetMessage() {
        enMessage.clear();
    }

    @FXML
    protected void deResetPrivate() {
        deP.clear();
        deQ.clear();
        deD.clear();
    }

    private void save(String name, TextArea area) {
        selectOut(name+".key");
        if (area.getText().isEmpty()) {
            showAlert("空文件", "请生成"+name+"后继续操作", Alert.AlertType.WARNING);
            return;
        }
        boolean result = FileUtil.save(area.getText(), outPutFile.getPath());
        if (result)
            showAlert("成功", "文件已保存至"+outPutFile.getPath(), Alert.AlertType.INFORMATION);
        else
            showAlert("失败", "文件保存失败", Alert.AlertType.ERROR);
    }

    private void load(TextArea area) {
        selectIn();
        String message = FileUtil.load(inputFile.getPath());
        if (message == null || message.isEmpty()) {
            showAlert("空文件", "文件内容为空", Alert.AlertType.WARNING);
        } else {
            area.setText(message);
        }
    }

    private void selectOut(String name) {
        Stage stage = (Stage) method.getScene().getWindow();
        chooser.setTitle("导出秘钥");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.setInitialFileName(name);
        outPutFile = chooser.showSaveDialog(stage);
    }

    private void selectIn() {
        Stage stage = (Stage) method.getScene().getWindow();
        chooser.setTitle("导入秘钥");
        inputFile = chooser.showOpenDialog(stage);
    }

    private void showAlert(String header, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("提示");
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private int getRSABit(String method) {
        return Integer.parseInt(method.split("-")[1]);
    }
}
