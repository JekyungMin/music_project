package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controller.ServerController.Client;
import Model.BlackList;
import Model.MemberTable;
import Model.MusicRecommend;
import Model.SingRecommend;
import Model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class ClientController implements Initializable{
	public Stage clientStage=null;
	@FXML private TableView<MusicRecommend> ctTableView;
	@FXML private ListView<String> ctListView;
	@FXML private ImageView ctImageView;
	@FXML private Button ctBtnNew;
	@FXML private Button ctRecommend;
	@FXML private Button ctChat;
	@FXML private Button ctExit;
	private Socket socket;
	ObservableList<MemberTable> mtList = FXCollections.observableArrayList();
	ObservableList<MusicRecommend> mrList = FXCollections.observableArrayList();
	ArrayList<MusicRecommend> singList;
	ObservableList<String> userList= FXCollections.observableArrayList();
	ArrayList<String> checkList;
	ServerController server=new ServerController();
	public MemberTable selectMb;
	public int selectMbIndex;
	public MusicRecommend selectMr;
	public int selectMrIndex;
	ObservableList<String> genreList = FXCollections.observableArrayList();
	public static String clientID=RootController.selectId;
	
	@Override
   public void initialize(URL location, ResourceBundle resources) {
		if(clientStage==null) {
			clientStart();
		}
			//Ŭ���̾�Ʈ ���� ���̺�����
			singRecommendTable();
			//������ ����Ʈ�� ����
			clientListView();
			//���ΰ�ħ ��ư �̺�Ʈ
			ctBtnNew.setOnAction((e)->{handlerNew();});
			//�뷡��õ ��ư �̺�Ʈ
			ctRecommend.setOnAction((e)-> {handlerCtSingStage();});
			//Ŭ���̾�Ʈ ä�� ��ư �̺�Ʈ
			ctChat.setOnAction((e)-> {handlerCtChatting();});
			//������ ��ư �̺�Ʈ
			ctExit.setOnAction((e)-> {
				handlerCtExit();
				stopClient();});
   }
	//������ ����Ʈ�� ����
	private void clientListView() {
		checkList=UserDAO.getCheckData();
		for(String user:checkList) {
			RootController.loginList.add(user);
		}
	  ctListView.setItems(RootController.loginList);
	}

	//���ΰ�ħ ��ư �̺�Ʈ
	private void handlerNew() {
		mrList.clear();
		ctTableView.setItems(mrList);
		singList=MemberDAO.getTopSingerData();
		for(MusicRecommend mr:singList) {
			mrList.add(mr);
		}
		ctTableView.setItems(mrList);
		
		RootController.loginList.removeAll(RootController.loginList);
		checkList.clear();
		ctListView.setItems(RootController.loginList);
		checkList=UserDAO.getCheckData();
		for(String user:checkList) {
			RootController.loginList.add(user);
		}
		ctListView.setItems(RootController.loginList);
		
	}
	//Ŭ���̾�Ʈ ���� ����
	private void clientStart() {
		try {
			socket=new Socket();
			InetSocketAddress isa=new InetSocketAddress("192.168.0.188",42135);
			
			socket.connect(isa);
				if(socket.isConnected()) {
					ServerController.callAlert("���Ӽ��� : ����Ǿ����ϴ�");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				}
	}
	
	private void stopClient() {
		if(!socket.isClosed()&&socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {}
		}
	}
	
	public void send(String message) {
		Runnable runnable=new Runnable() {
			@Override
			public void run() {
				try {
					OutputStream os=socket.getOutputStream();
					PrintWriter pw= new PrintWriter(os);
					pw.println(clientID+" :"+message);
					pw.flush();
				} catch (IOException e1) {
						ServerController.callAlert("���۽��� : ���۽��� �Ǿ����ϴ�.");
				}
			}
		};
		Thread thread=new Thread(runnable);
		thread.start();
	}
	//Ŭ���̾�Ʈ �뷡��õ �̺�Ʈ
	private void handlerCtSingStage() {
		try {
			Stage srStage=new Stage();
			FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/recommend.fxml"));
			Parent root=loader.load();
			Scene scene=new Scene(root);
			srStage.setScene(scene);
			srStage.show();
			srStage.setTitle("�뷡��õ");
			
			TextField rcSinger=(TextField)root.lookup("#rcSinger");
			TextField rcSingTitle=(TextField)root.lookup("#rcSingTitle");
			TextField rcUserid=(TextField)root.lookup("#rcUserid");
			ComboBox<String> rcGenre=(ComboBox)root.lookup("#rcGenre");
			Button rcRegister=(Button)root.lookup("#rcRegister");
			Button rcCancel=(Button)root.lookup("#rcCancel");
			
			genreList.addAll("POP","�߶��","����","Ŭ����","��","����","����");
			rcGenre.setItems(genreList);
			
			rcUserid.setText(clientID);
			
			rcRegister.setOnAction((e)-> {
				SingRecommend sr=new SingRecommend(
						rcSinger.getText(), 
						rcSingTitle.getText(), 
						rcGenre.getSelectionModel().getSelectedItem(), 
						rcUserid.getText());
				int count =MemberDAO.insertSingData(sr);
				if(count!=0) {
					RootController.callAlent("��õ�Ϸ� : �뷡��õ�� �Ͽ����ϴ�.");
				}
				srStage.close();
			});
			rcCancel.setOnAction((e)-> {srStage.close();});
		}catch(Exception e) {}
	}
	//Ŭ���̾�Ʈ ä�� ��ư �̺�Ʈ
	private void handlerCtChatting() {
		Stage chatStage=new Stage();
			try {
				FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/chatting.fxml"));
				Parent root=loader.load();
				Scene scene=new Scene(root);
				chatStage.setScene(scene);
				chatStage.setTitle("ä�ù�");
				chatStage.show();
				
				TextField ctTxtField=(TextField)root.lookup("#ctTxtField");
				TextArea ctTxtArea=(TextArea)root.lookup("#ctTxtArea");
				Button ctBtnSend=(Button)root.lookup("#ctBtnSend");
				
				ctTxtArea.setEditable(false);
				
				Runnable runnable=new Runnable() {
					@Override
					public void run() {
							//�����͸� �б� ���� ���
							while(true) {
								try {
									InputStream is=socket.getInputStream();
									InputStreamReader isr=new InputStreamReader(is);
									BufferedReader br=new BufferedReader(isr);
									String receiveMessage=br.readLine();
									Platform.runLater(()->{
										ctTxtArea.appendText((receiveMessage+"\n"));										
									});
									
									ctBtnSend.setOnAction((e) ->{
										send(ctTxtField.getText());
										System.out.println(ctTxtField.getText());
										ctTxtField.clear();
									});
									
									ctTxtField.setOnKeyPressed(event -> {
										if (event.getCode().equals(KeyCode.ENTER)) {
											send(ctTxtField.getText());
											System.out.println(ctTxtField.getText());
											ctTxtField.clear();
										}
									});
								} catch (IOException e) {
									Platform.runLater(()->{
										ctTxtArea.appendText("������ ����� ������ϴ�."+"\n");
									});
									stopClient();
									break;
								}
							}//end of while
					}
				};
				Thread thread=new Thread(runnable);
				thread.start();
					
			}catch(Exception e) {
				ServerController.callAlert("ä�ÿ��� : ������ �Ұ��մϴ�.");
			}
		}
	//Ŭ���̾�Ʈ ������ ��ư �̺�Ʈ
	private void handlerCtExit() {
		if(!socket.isClosed()&&socket!=null) {
			try {
				socket.close();
			} catch (IOException e1) {}
		}
		ServerController.callAlert("���� : ���α׷��� ����˴ϴ�.");
		clientStage.close();
		int count=UserDAO.deleteUserData(RootController.selectId);
		   if(count!=0){
			   RootController.loginList.remove(RootController.selectId);
		   }else {
			   return;
		   }
	}
	//Ŭ���̾�Ʈ ���� ���̺����� �� ����Ŭ���� �뷡�˻� ��ũ
    private void singRecommendTable() {
		TableColumn tcSinger = ctTableView.getColumns().get(0);
		tcSinger.setCellValueFactory(new PropertyValueFactory<>("singer"));
		tcSinger.setStyle("-fx-alignment : CENTER;");
		TableColumn tcSingTitle = ctTableView.getColumns().get(1);
		tcSingTitle.setCellValueFactory(new PropertyValueFactory<>("singTitle"));
		tcSingTitle.setStyle("-fx-alignment : CENTER;");
		TableColumn tcGenre = ctTableView.getColumns().get(2);
		tcGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		tcGenre.setStyle("-fx-alignment : CENTER;");
		TableColumn tcRcNum = ctTableView.getColumns().get(3);
		tcRcNum.setCellValueFactory(new PropertyValueFactory<>("rcNum"));
		tcRcNum.setStyle("-fx-alignment : CENTER;");

		singList=MemberDAO.getTopSingerData();
		for(MusicRecommend mr:singList) {
			mrList.add(mr);
		}
		ctTableView.setItems(mrList);
		
		ctTableView.setOnMouseClicked((e)-> {
				if(e.getClickCount()==2) {
					selectMr = ctTableView.getSelectionModel().getSelectedItem();
					selectMrIndex = ctTableView.getSelectionModel().getSelectedIndex();
					try {
						Stage srStage=new Stage();
						FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/link.fxml"));
						Parent root=loader.load();
						Scene scene=new Scene(root);
						srStage.setScene(scene);
						srStage.show();
						srStage.setTitle("���� �˻�â");
						
						Button btnExit=(Button)root.lookup("#btnExit");
						WebView link=(WebView)root.lookup("#link");
						WebEngine web=link.getEngine();
						web.load("https://www.youtube.com/results?search_query="+selectMr.getSinger()+" "+selectMr.getSingTitle());
						btnExit.setOnAction((e2)-> {
							srStage.close();
							web.load(null);
						});
					}catch(Exception e3) {}
				}else {
					return;					
				}
		});
}
    //�޴��� �̺�Ʈ
   public void handlerMenuRecommend(ActionEvent e) {
	   handlerCtSingStage();
   }
   public void handlerMenuChat(ActionEvent e) {
	   handlerCtChatting();
   }
   public void handlerMenuExit(ActionEvent e) {
	   if(!socket.isClosed()&&socket!=null) {
			try {
				socket.close();
			} catch (IOException e1) {}
		}
		ServerController.callAlert("���� : ���α׷��� ����˴ϴ�.");
		clientStage.close();
   }
   
}