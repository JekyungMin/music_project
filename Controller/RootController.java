package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Login;
import Model.MemberInfomation;
import Model.MemberTable;
import Model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RootController implements Initializable{
	public Stage primaryStage;
	@FXML	private TextField textId;
	@FXML private PasswordField textPassword;
	@FXML private Button btnLogin;
	@FXML private Button btnClose;
	@FXML private Button btnJoin;
	@FXML private ImageView loginImg;
	ObservableList<String> cmGenre = FXCollections.observableArrayList();
	public static ObservableList<String> loginList = FXCollections.observableArrayList();
	ArrayList<Login> dbArrayList;
	ArrayList<String> db2ArrayList;
	ServerController server;
	public MemberInfomation selectMb;
	public int selectMbIndex;
	public Login selectLogin;
	public int selectLoginIndex;
	public static String selectId;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		  //���̵𿡼� ����Ű �Է� �̺�Ʈ
	      textId.setOnKeyPressed(event -> {
	            if (event.getCode().equals(KeyCode.ENTER)) {
	               handlerBtnLoginAction();
	            }
	         });
	      //��й�ȣ���� ����Ű �Է�Ű
	      textPassword.setOnKeyPressed(event -> {
	            if (event.getCode().equals(KeyCode.ENTER)) {
	               handlerBtnLoginAction();
	            }
	         });
	      	// �̹��� Ŭ���� ������ �α��� ����
	      	loginImg.setOnMouseClicked(e-> debugLoginImg(e));
	      	// �α��� ��ư Ŭ�� �̺�Ʈ
			btnLogin.setOnAction((e)-> {handlerBtnLoginAction();});
			// ������ ��ư Ŭ�� �̺�Ʈ
			btnClose.setOnAction((e)-> {handlerBtnCloseAction();});
			// ȸ������ ��ư Ŭ�� �̺�Ʈ
			btnJoin.setOnAction((e)-> {handlerBtnJoinAction();});
	}
	
	//����׿� �̹��� Ŭ���� ������ �α��� ����
	private void debugLoginImg(MouseEvent e) {
		 textId.setText("admin");
	      textPassword.setText("admin");
	}
	//ȸ������ ��ư Ŭ�� �̺�Ʈ
	private void handlerBtnJoinAction() {
		try {
			Stage joinStage=new Stage();
			FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/join.fxml"));
			Parent root=loader.load();
			Scene scene=new Scene(root);
			joinStage.setScene(scene);
			joinStage.show();
			
			TextField mbId = (TextField) root.lookup("#mbId");
			TextField mbName = (TextField) root.lookup("#mbName");
			TextField mbAge = (TextField) root.lookup("#mbAge");
			TextField mbNick = (TextField) root.lookup("#mbNick");
			PasswordField mbPass=(PasswordField) root.lookup("#mbPass");
			ObservableMap<String, Object>map=loader.getNamespace();
			ToggleGroup group=(ToggleGroup)map.get("group");
			Button mbOverlab=(Button)root.lookup("#mbOverlab");
			Button mbBtnOk=(Button)root.lookup("#mbBtnOk");
			Button mbBtnCancel=(Button)root.lookup("#mbBtnCancel");
			ComboBox<String> mbGenre=(ComboBox)root.lookup("#mbGenre");
			
			cmGenre.addAll("POP","�߶��","����","Ŭ����","��","����","����");
			mbGenre.setItems(cmGenre);
			mbOverlab.setOnAction((e) ->{
				db2ArrayList=MemberDAO.getIdData();
				for(String str2:db2ArrayList) {
					if(mbId.getText().equals(str2)) {
						callAlent("�ߺ�Ȯ�� : �̹� ������� ���̵��Դϴ�.");
						return;
					}
				}
				for(String str2:db2ArrayList) {
					if(mbId.getText().equals(str2)==false){
						callAlent("�ߺ�Ȯ�� : ��밡���� ���̵��Դϴ�.");
						return;
					}
				}
			});
			mbBtnOk.setOnAction((e)->{
				MemberInfomation member=new MemberInfomation(
						mbId.getText(),
						mbPass.getText(),
						mbName.getText(), 
						mbAge.getText(), 
						mbNick.getText(), 
						group.getSelectedToggle().getUserData().toString(), 
						mbGenre.getSelectionModel().getSelectedItem());
				ServerController server = new ServerController();
				server.mbList.add(member);
				int count=MemberDAO.insertMemberData(member);
				if(count!=0) {
					callAlent("���ԿϷ� : ȸ������ �Ǿ����ϴ�.");
				}
				joinStage.close();
				});
			mbBtnCancel.setOnAction((e)->{joinStage.close();});
		} catch (Exception e) {}
	}
	
	// �α��� ��ư Ŭ�� �̺�Ʈ
	private void handlerBtnLoginAction() {
		String id = textId.getText().trim();
		String pw = textPassword.getText().trim();
		dbArrayList=MemberDAO.getLoginData();
		if(id.equals("admin")&&pw.equals("admin")) {
			try {
				selectId=textId.getText();
				Stage serverStage=new Stage();
				FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/server_main.fxml"));
				Parent root=loader.load();
				ServerController serverController=loader.getController();
				serverController.serverStage=serverStage;
				Scene scene=new Scene(root);
				serverStage.setScene(scene);
				serverStage.setTitle("���� UI");
				primaryStage.close();
				serverStage.show();
				callAlent("�α��� ���� : ������ ����Դϴ�.");
			} catch (Exception e) {
				callAlent("�α��� ���� : �α��� �����߽��ϴ�.");
			}
		}else {
			String black=MemberDAO.getBlackData(id);
			if(black.equals("O")) {
				callAlent("�α��� ���� : ������Ʈ�Դϴ�.");
				return;
			}
		for(Login str:dbArrayList) {
			 if(id.equals(str.getUserid())&&pw.equals(str.getPassword())){
				try {
					selectId=textId.getText();
					Stage clientStage=new Stage();
					FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/client_main.fxml"));
					Parent root=loader.load();
					ClientController clientController=loader.getController();
					clientController.clientStage=clientStage;
					Scene scene=new Scene(root);
					clientStage.setScene(scene);
					clientStage.setTitle("Ŭ���̾�Ʈ UI");
					primaryStage.close();
					clientStage.show();
					callAlent("�α��� ���� : �������"+id+"�� ȯ���մϴ�.");
				} catch (Exception e) {
					callAlent("�α��� ���� : �α��� �����߽��ϴ�.");
				}
			 }
			}//end of for
		}
		User user=new User(selectId);
		int count=UserDAO.getUsersData(user);
		if(count!=0) {
			loginList.add(selectId);
		}
}
	// ������ ��ư Ŭ�� �̺�Ʈ
	private void handlerBtnCloseAction() {
		Platform.exit();
	}
	//��Ÿ :�˸�â �߰��� : �� ������ �� //���� :"�������� : ���� �Է����ּ���"
	public static void callAlent(String contentText) {  
		Alert alert=new Alert(AlertType.INFORMATION);
		alert.setTitle("�˸�");
		alert.setHeaderText(contentText.substring(0, contentText.lastIndexOf(":")));
		alert.setContentText(contentText.substring(contentText.lastIndexOf(":")+1));
		alert.showAndWait();
	}
}
