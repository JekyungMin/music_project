package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.function.LongToIntFunction;

import Model.BlackList;
import Model.MemberInfomation;
import Model.MemberTable;
import Model.MusicRecommend;
import Model.RcSinger;
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
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class ServerController implements Initializable {
	public Stage serverStage;
	public Stage clientStage;
   @FXML private TableView<MusicRecommend>svTableView;
   @FXML private ListView<String> svListView;
   @FXML private Button svBtnNew;
   @FXML private Button svMusicList;
   @FXML private Button svChat;
   @FXML private Button svExit;
   @FXML public TableView<MemberTable> memberInfo;
   @FXML private TextField txtSearch;
   @FXML private Button btnSearch;
   @FXML private Button blackRegister;
   @FXML private Button blackClear;
   @FXML private Button infoEdit;
   @FXML private Button infoDelete;
   private TableView<RcSinger> mlTableView;
   ObservableList<RcSinger> recList = FXCollections.observableArrayList();
   public static ObservableList<MusicRecommend> mrList = FXCollections.observableArrayList();
   public ObservableList<MemberInfomation> mbList= FXCollections.observableArrayList();
   public static ObservableList<MemberTable> mbTable= FXCollections.observableArrayList();
   ObservableList<String> searchList = FXCollections.observableArrayList();
   public MemberInfomation selectMi;
   public int selectMiIndex;
	public MusicRecommend selectSr;
	public int selectSrIndex;
	public static MemberTable selectMb;
	public static int selectMbIndex;
	public BlackList selectBl;
	public int selectBlIndex;
	public RcSinger selectRc;
	public int selectRcIndex;
	public Client selectLogin;
	public int selectLoginIndex;
	private ServerSocket serverSocket;
	private Thread mainthread;
	Socket socket;
	TextArea ctTxtArea=null;
	public static List<Client> list=new Vector<Client>();
	public static ArrayList<MemberTable> dbArrayList;
	ArrayList<MusicRecommend> singList;
	ArrayList<RcSinger> rsList;
	ArrayList<String> userList;
	ObservableList<String> cmGenre = FXCollections.observableArrayList();
	ArrayList<String> db2ArrayList;
	ArrayList<MemberInfomation> memberList;
	
	
   @Override
   public void initialize(URL location, ResourceBundle resources) {
	   if(serverStage==null) {
		   startServer();
	   }
	 //��1. �뷡��õ����
	  singRecommendTab();
	  //������ ����Ʈ�� ����
	  loginListView();
	  // ���ΰ�ħ �̺�Ʈ
	  svBtnNew.setOnAction((e)->{
		  loginListView();
		  handlerNew();});
	  //��õ��� �̺�Ʈ
	  svMusicList.setOnAction((e)->{ handlerMusicList();}); 
	  //ä��â �̺�Ʈ
	  svChat.setOnAction((e)->{handlerChatting();});
	  //������ �̺�Ʈ
	  svExit.setOnAction((e)-> {
		  stopServer();
		  serverStage.close();
		  });
	 
	//��2. ȸ���������̺�
	  memberInformationTab();
	  //�˻���ư �̺�Ʈ
	  btnSearch.setOnAction((e)->{handlerMemberSearch();});
	  //������ �̺�Ʈ
	  blackRegister.setOnAction((e)-> {handlerBlackList();});
	  //�������� �̺�Ʈ
	  blackClear.setOnAction((e)-> {handlerBlackclear();});
	  //ȸ������ ���� �̺�Ʈ
	  infoEdit.setOnAction((e)-> {handlerMemberEdit();});
	  //ȸ�� ���� �̺�Ʈ
	  infoDelete.setOnAction((e)-> {handlerMemberDel();});

   }
// ������ ����Ʈ�� ����
  private void loginListView() {
	  userList=UserDAO.getCheckData();
		for(String user:userList) {
			RootController.loginList.add(user);
		}
	  svListView.setItems(RootController.loginList);
}

// ���ΰ�ħ �̺�Ʈ
private void handlerNew() {
	//��õ�뷡 ���̺�
	mrList.removeAll(mrList);
	singList=MemberDAO.getTopSingerData();
	for(MusicRecommend mr:singList) {
		mrList.add(mr);
	}
	svTableView.setItems(mrList);
	//������ Ȯ��
	RootController.loginList.removeAll(RootController.loginList);
	userList.clear();
	svListView.setItems(RootController.loginList);
	userList=UserDAO.getCheckData();
	for(String user:userList) {
		RootController.loginList.add(user);
	}
	svListView.setItems(RootController.loginList);
	//ȸ�� ����
	mbTable.removeAll(mbTable);
	dbArrayList.clear();
	dbArrayList=MemberDAO.getMemberTableData();
	for(MemberTable member:dbArrayList) {
		mbTable.add(member);
	}
	memberInfo.setItems(mbTable);
}

//��1. �뷡��õ����
   private void singRecommendTab() {
		TableColumn tcSinger = svTableView.getColumns().get(0);
		tcSinger.setCellValueFactory(new PropertyValueFactory<>("singer"));
		tcSinger.setStyle("-fx-alignment : CENTER;");
		TableColumn tcSingTitle = svTableView.getColumns().get(1);
		tcSingTitle.setCellValueFactory(new PropertyValueFactory<>("singTitle"));
		tcSingTitle.setStyle("-fx-alignment : CENTER;");
		TableColumn tcGenre = svTableView.getColumns().get(2);
		tcGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		tcGenre.setStyle("-fx-alignment : CENTER;");
		TableColumn tcRcNum = svTableView.getColumns().get(3);
		tcRcNum.setCellValueFactory(new PropertyValueFactory<>("rcNum"));
		tcRcNum.setStyle("-fx-alignment : CENTER;");

		singList=MemberDAO.getTopSingerData();
		for(MusicRecommend mr:singList) {
			mrList.add(mr);
		}
		svTableView.setItems(mrList);
		svTableView.setOnMouseClicked((e)-> {
			if(e.getClickCount()==2) {
				selectSr = svTableView.getSelectionModel().getSelectedItem();
				selectSrIndex = svTableView.getSelectionModel().getSelectedIndex();
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
					web.load("https://www.youtube.com/results?search_query="+selectSr.getSinger()+" "+selectSr.getSingTitle());
					
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
//��õ��� �̺�Ʈ
   private void handlerMusicList() {
	try {
		Stage mlStage=new Stage();
		FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/music_list.fxml"));
		Parent root=loader.load();
		Scene scene=new Scene(root);
		mlStage.setScene(scene);
		mlStage.setTitle("��õ���");
		mlStage.show();
		
		mlTableView=(TableView)root.lookup("#listTableView");
		TextField mlTxtSearch=(TextField)root.lookup("#listTxtSearch");
		Button mlBtnSearch=(Button)root.lookup("#listBtnSearch");
		Button mlBtnClear=(Button)root.lookup("#listBtnClear");
		Button mlBtnDelete=(Button)root.lookup("#listBtnDelete");
		Button mlBtnExit=(Button)root.lookup("#listBtnExit");
		ComboBox<String> mlCmbList=(ComboBox)root.lookup("#cmbList");
		
		TableColumn tcSinger = mlTableView.getColumns().get(0);
		tcSinger.setCellValueFactory(new PropertyValueFactory<>("singer"));
		tcSinger.setStyle("-fx-alignment : CENTER;");
		TableColumn tcSingTitle = mlTableView.getColumns().get(1);
		tcSingTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		tcSingTitle.setStyle("-fx-alignment : CENTER;");
		TableColumn tcGenre = mlTableView.getColumns().get(2);
		tcGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
		tcGenre.setStyle("-fx-alignment : CENTER;");
		TableColumn tcRecommend = mlTableView.getColumns().get(3);
		tcRecommend.setCellValueFactory(new PropertyValueFactory<>("userid"));
		tcRecommend.setStyle("-fx-alignment : CENTER;");
		
			mlTableView.setItems(recList);
			recList.clear();
			rsList=MemberDAO.getRcSingerData();
			for(RcSinger rs:rsList) {
				recList.add(rs);
			}
			searchList.clear();
			searchList.addAll("����","�뷡����","�帣","��õ��");
			mlCmbList.setItems(searchList);
		
		mlBtnSearch.setOnAction((e)->{
			for (RcSinger mr : rsList) {
				if (mlTxtSearch.getText().trim().equals(mr.getSinger())){
					mlTableView.getSelectionModel().select(mr);
				}else if(mlTxtSearch.getText().trim().equals(mr.getTitle())) {
					mlTableView.getSelectionModel().select(mr);
				}else if(mlTxtSearch.getText().trim().equals(mr.getGenre())) {
					mlTableView.getSelectionModel().select(mr);
				}else if(mlTxtSearch.getText().trim().equals(mr.getUserid())) {
					mlTableView.getSelectionModel().select(mr);
				}
			}
		});
		mlBtnClear.setOnAction((e)->{ 
			mlCmbList.getSelectionModel().clearSelection();
			mlTxtSearch.clear();
			mlTableView.getSelectionModel().clearSelection();
		});
		mlBtnDelete.setOnAction((e)->{
			selectRc = mlTableView.getSelectionModel().getSelectedItem();
			selectRcIndex=mlTableView.getSelectionModel().getSelectedIndex();
			int count=MemberDAO.deleteSingData(selectRc.getSinger());
			if(count!=0) {
				recList.remove(selectRcIndex);
				rsList.remove(selectRc);
			}
		});
		mlBtnExit.setOnAction((e)->{mlStage.close();});
	} catch (Exception e) {}
}

   //ä�ù� �̺�Ʈ
   public void handlerChatting() {
	try {
		Stage chatStage=new Stage();
		FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/chatting.fxml"));
		Parent root=loader.load();
		Scene scene=new Scene(root);
		chatStage.setScene(scene);
		chatStage.setTitle("ä�ù�");
		chatStage.show();
		
		TextField ctTxtField=(TextField)root.lookup("#ctTxtField");
		ctTxtArea=(TextArea)root.lookup("#ctTxtArea");
		Button ctBtnSend=(Button)root.lookup("#ctBtnSend");
		ctTxtArea.setEditable(false);
		
		ctBtnSend.setOnAction((e) ->{
			Client client =new Client(socket);
			client.send(ctTxtField.getText());
			ctTxtArea.appendText(("������ :"+ctTxtField.getText()+"\n"));
			ctTxtField.clear();
		});
		
		ctTxtField.setOnKeyPressed(event -> {
			 if (event.getCode().equals(KeyCode.ENTER)) {
				 Client client =new Client(socket);
				 client.send(ctTxtField.getText());
				 ctTxtArea.appendText(("������ :"+ctTxtField.getText()+"\n"));
				 ctTxtField.clear();
	            }
		});
	}catch(Exception e) {}
}

//��2. ȸ���������̺�
   private void memberInformationTab() {
	
	TableColumn tcMbId = memberInfo.getColumns().get(0);
	tcMbId.setCellValueFactory(new PropertyValueFactory<>("userid"));
	tcMbId.setStyle("-fx-alignment : CENTER;");
	TableColumn tcMbName = memberInfo.getColumns().get(1);
	tcMbName.setCellValueFactory(new PropertyValueFactory<>("name"));
	tcMbName.setStyle("-fx-alignment : CENTER;");
	TableColumn tcMbAge = memberInfo.getColumns().get(2);
	tcMbAge.setCellValueFactory(new PropertyValueFactory<>("age"));
	tcMbAge.setStyle("-fx-alignment : CENTER;");
	TableColumn tcMbNick = memberInfo.getColumns().get(3);
	tcMbNick.setCellValueFactory(new PropertyValueFactory<>("userNick"));
	tcMbNick.setStyle("-fx-alignment : CENTER;");
	TableColumn tcMbGender = memberInfo.getColumns().get(4);
	tcMbGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
	tcMbGender.setStyle("-fx-alignment : CENTER;");
	TableColumn tcMbGenre = memberInfo.getColumns().get(5);
	tcMbGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
	tcMbGenre.setStyle("-fx-alignment : CENTER;");
	TableColumn tcMbBlack = memberInfo.getColumns().get(6);
	tcMbBlack.setCellValueFactory(new PropertyValueFactory<>("blacklist"));
	tcMbBlack.setStyle("-fx-alignment : CENTER;");
	TableColumn tcMbBc = memberInfo.getColumns().get(7);
	tcMbBc.setCellValueFactory(new PropertyValueFactory<>("blcause"));
	tcMbBc.setStyle("-fx-alignment : CENTER;");
	
	dbArrayList=MemberDAO.getMemberTableData();
	for(MemberTable member:dbArrayList) {
		mbTable.add(member);
	}
	memberInfo.setItems(mbTable);
}
//ȸ�� �˻� �̺�Ʈ
   private void handlerMemberSearch() {
	for (MemberTable mb : dbArrayList) {
		if (txtSearch.getText().trim().equals(mb.getName())) {
			memberInfo.getSelectionModel().select(mb);
		}
	}
}
//���� ��� �̺�Ʈ
   private void handlerBlackList() {
	try {
		Stage blackStage=new Stage();
		FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/blacklist.fxml"));
		Parent root=loader.load();
		Scene scene=new Scene(root);
		blackStage.setScene(scene);
		blackStage.setTitle("���� ���");
		blackStage.show();
		
		TextField blackId=(TextField)root.lookup("#blackId");
		TextArea blackCause=(TextArea)root.lookup("#blackCause");
		Button blackBtnOk=(Button)root.lookup("#blackBtnOk");
		Button blackBtnExit=(Button)root.lookup("#blackBtnExit");
		
		selectMb=memberInfo.getSelectionModel().getSelectedItem();
		selectMbIndex=memberInfo.getSelectionModel().getSelectedIndex();
		
		blackId.setText(selectMb.getUserid());
		blackId.setDisable(true);
		blackBtnOk.setOnAction((e)->{
			MemberTable blackList = new MemberTable(
					selectMb.getUserid(), 
					selectMb.getName(), 
					selectMb.getAge(), 
					selectMb.getUserNick(), 
					selectMb.getGender(), 
					selectMb.getGenre(), 
					"O",
					blackCause.getText());
			int count =MemberDAO.updateBlackData(blackList);
			if(count!=0) {
				mbTable.remove(selectMbIndex);
				mbTable.add(selectMbIndex,blackList);
			}
			callAlert("����Ϸ� : "+selectMb.getUserid()+"���� ����Ǿ����ϴ�.");
			blackStage.close();
		});
		blackBtnExit.setOnAction((e)->{blackStage.close();});
	}catch(Exception e) {}
}
 //���� ���� �̺�Ʈ
   private void handlerBlackclear() {
	   selectMb=memberInfo.getSelectionModel().getSelectedItem();
	   selectMbIndex=memberInfo.getSelectionModel().getSelectedIndex();
	   
	   MemberTable blackClear = new MemberTable(
				selectMb.getUserid(), 
				selectMb.getName(), 
				selectMb.getAge(), 
				selectMb.getUserNick(), 
				selectMb.getGender(), 
				selectMb.getGenre(), 
				"x",
				"�ش����");
		int count =MemberDAO.updateBlackData(blackClear);
		if(count!=0) {
			mbTable.remove(selectMbIndex);
			mbTable.add(selectMbIndex,blackClear);
		}
		callAlert("�������� �Ϸ� : "+selectMb.getUserid()+"���� �������� �Ǿ����ϴ�.");
   }

   //ȸ�� ���� �̺�Ʈ
   private void handlerMemberEdit() {
	   try {
			Stage editMbStage=new Stage();
			FXMLLoader loader=new FXMLLoader(getClass().getResource("../View/join.fxml"));
			Parent root=loader.load();
			Scene scene=new Scene(root);
			editMbStage.setScene(scene);
			editMbStage.setTitle("ȸ������");
			editMbStage.show();
		
			selectMb=memberInfo.getSelectionModel().getSelectedItem();
			selectMbIndex=memberInfo.getSelectionModel().getSelectedIndex();
			
			TextField mbId = (TextField) root.lookup("#mbId");
			TextField mbName = (TextField) root.lookup("#mbName");
			TextField mbAge = (TextField) root.lookup("#mbAge");
			TextField mbNick = (TextField) root.lookup("#mbNick");
			PasswordField mbPass=(PasswordField) root.lookup("#mbPass");
			ObservableMap<String, Object>map=loader.getNamespace();
			ToggleGroup group=(ToggleGroup)map.get("group");
			RadioButton tgMan=(RadioButton)root.lookup("#tgMan");
			RadioButton tgWoman=(RadioButton)root.lookup("#tgWoman");
			Button mbOverlab=(Button)root.lookup("#mbOverlab");
			Button mbBtnOk=(Button)root.lookup("#mbBtnOk");
			Button mbBtnCancel=(Button)root.lookup("#mbBtnCancel");
			ComboBox<String> mbGenre=(ComboBox)root.lookup("#mbGenre");
			
			cmGenre.addAll("POP","�߶��","����","Ŭ����","��","����","����");
			mbGenre.setItems(cmGenre);
			
			mbId.setText(selectMb.getUserid());
			mbId.setDisable(true);
			mbPass.setDisable(true);
			mbName.setText(selectMb.getName());
			mbName.setDisable(true);
			mbAge.setText(selectMb.getAge());
			mbAge.setDisable(true);
			mbNick.setText(selectMb.getUserNick());
			mbGenre.getSelectionModel().getSelectedItem();
			if(selectMb.getGender().equals("����")) {
				tgMan.setSelected(true);
			}else {
				tgWoman.setSelected(true);
			}
			tgMan.setDisable(true);
			tgWoman.setDisable(true);
			mbBtnOk.setOnAction((e)->{
				MemberTable member=new MemberTable(
						mbId.getText(),
						mbName.getText(), 
						mbAge.getText(), 
						mbNick.getText(), 
						group.getSelectedToggle().getUserData().toString(),
						mbGenre.getSelectionModel().getSelectedItem(), 
						mbTable.get(selectMbIndex).getBlacklist(),
						mbTable.get(selectMbIndex).getBlcause());
				
				int count =MemberDAO.updateMemberData(member);
				if(count!=0) {
					mbTable.remove(selectMbIndex);
					mbTable.add(selectMbIndex,member);
					int arrayIndex=dbArrayList.indexOf(selectMb);
					dbArrayList.set(arrayIndex, member);
					callAlert("�����Ϸ� : " + selectMb.getUserid() + "���� �����Ǿ����ϴ�.");
					editMbStage.close();
				}else {
					return;
				}
			});
			mbBtnCancel.setOnAction((e)->{editMbStage.close();});
	   }catch(Exception e){}
   }
 //ȸ�� ���� �̺�Ʈ
   private void handlerMemberDel() {
	   selectMb=memberInfo.getSelectionModel().getSelectedItem();
	   selectMbIndex=memberInfo.getSelectionModel().getSelectedIndex();
	   int count=MemberDAO.deleteMemberData(selectMb.getUserid());
	   if(count!=0){
		   mbTable.remove(selectMbIndex);
		   dbArrayList.remove(selectMb);
		   callAlert("�����Ϸ� : "+selectMb.getUserid()+"���� ���� �Ǿ����ϴ�. ");
	   }else {
		   return;
	   }
   }
//Ŭ���̾�Ʈ
  public class Client{
		Socket socket;
		
		public Client(Socket socket){
			this.socket = socket;
			recieve();
		}
		private void recieve() {
			Runnable runnable=new Runnable() {
				
				@Override
				public void run() {
					try {
					while(true) {
							InputStream is=socket.getInputStream();
							InputStreamReader isr=new InputStreamReader(is);
							BufferedReader br=new BufferedReader(isr);
							String receiveMessage=br.readLine();
							if(receiveMessage==null) {throw new IOException();}
							Platform.runLater(()-> {
								ctTxtArea.appendText(receiveMessage+"\n");
								for(Client client:list) {
									client.send(receiveMessage+"\n");
								}
						});
					}//end of while
					} catch (IOException e) {
						list.remove(Client.this);
					}
					
				}
			};
			Thread thread=new Thread(runnable);
			thread.start();
		}

		public void send(String message) {
			Runnable runnable=new Runnable() {
				@Override
				public void run() {
					try {
						OutputStream os=socket.getOutputStream();
						PrintWriter pw=new PrintWriter(os);
					
							pw.println(message);
						
						pw.flush();
					} catch (IOException e) {
						Platform.runLater(()-> {
							callAlert("��� ���� : �߸��� �����Դϴ�");
					});
						list.remove(Client.this);
						try {
							socket.close();
						} catch (IOException e1) {}
					}
				}
			};
			Thread thread=new Thread(runnable);
			thread.start();
		}
	}
 //���� ���� ����
   public void startServer() {
	   //���� �����
		 try {
				serverSocket=new ServerSocket();
				serverSocket.bind(new InetSocketAddress("192.168.0.188", 42135));
				
			} catch (IOException e) {
				if(!serverSocket.isClosed()) {
					stopServer();
				}
				return;
			}
			Runnable runnable=new Runnable() {
				
				@Override
				public void run() {
					//�Ϲݽ����忡�� javaFX application Thread���� �ؾߵ� ����(UI�����Ϸ���)
					//Platform.runLater()�� �ҷ��� �۾��� ��û�ؾߵȴ�.
					Platform.runLater(()-> {
							callAlert("�������� : ������ ����Ǿ����ϴ�.");
					});
					while(true) {
						try {
							socket=serverSocket.accept();		//������
							Client client =new Client(socket);
							list.add(client);
						} catch (IOException e) {
							if(!serverSocket.isClosed()) {
								stopServer();
							}
							break;
						}
					}//end of while
				}
			};//end of Runnable
			mainthread=new Thread(runnable);
			mainthread.start();
   }
   public void stopServer() {
		 try {
			 if(!socket.isClosed()&&socket!=null) {
					try {
						socket.close();
					} catch (IOException e) {}
			 }
				} catch (Exception e) {
					if(mainthread.isAlive()) {
						mainthread.stop();
					}
				}
				if(!serverSocket.isClosed()&& serverSocket!=null) {
					try {
						callAlert("�������� : ������ �����մϴ�.");
						serverSocket.close();
						int count=UserDAO.deleteUserData(RootController.selectId);
						   if(count!=0){
							   RootController.loginList.remove(RootController.selectId);
						   }else {
							   return;
						   }
					} catch (IOException e1) {}
				}
	}
   
 //��Ÿ. ���â�Լ� : �߰��� : �����ٰ�, ���� : "���� : �����߻�"
   public static void callAlert(String contentText) {
      Alert alert= new Alert(AlertType.INFORMATION);
      alert.setTitle("�˸�â");
      alert.setHeaderText(contentText.substring(0, contentText.lastIndexOf(":")));
      alert.setContentText(contentText.substring(contentText.lastIndexOf(":")+1));
      alert.showAndWait();
   }
}