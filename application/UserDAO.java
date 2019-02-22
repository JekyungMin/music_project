package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.User;

public class UserDAO {
	
	public static ArrayList<String> userAarryList= new ArrayList<>();

	public static int getUsersData(User userid){
		// �����ͺ��̽� ������̺�� �α��� ������ �������� ������
		StringBuffer insertMember=new StringBuffer();
		insertMember.append("insert into usertbl ");
		insertMember.append("(userid) ");
		insertMember.append("values ");
		insertMember.append("(?) ");
		
		Connection con =null;
		
		PreparedStatement psmt=null;
		int count=0;
		try {
			con=DBUtility.getConnection();
			psmt=con.prepareStatement(insertMember.toString());
			psmt.setString(1, userid.getUserid());
			count=psmt.executeUpdate();
			if(count==0) {
				RootController.callAlent("�������� ���� : DB�������� ����");
				return count;
			}
		} catch (SQLException e) {
			RootController.callAlent("�α��� ���� : �α��� ���Դϴ�.");
		} finally {
			try {
				if(psmt!=null) {psmt.close();}
				if(con!=null) {con.close();}
				} catch (SQLException e) {
					RootController.callAlent("�ڿ��ݱ� ���� : psmt, con �ݱ� ����");
				}
		}
		return count;
	}
	
	public static ArrayList<String> getCheckData(){
		// �����ͺ��̽� ������̺�� �α��� ������ �������� ������
		String selectUser="select * from usertbl ";
				
				Connection con =null;
				
				PreparedStatement psmt=null;
				
				ResultSet rs=null;
				try {
					con=DBUtility.getConnection();
					psmt=con.prepareStatement(selectUser);
					rs=psmt.executeQuery();
					if(rs==null) {
						RootController.callAlent("Select ���� : Select �����Ͽ����ϴ�");
						return null;
					}
					while(rs.next()) {
						User user=new User(
								rs.getString(1));
						userAarryList.add(user.getUserid());
					}
				} catch (SQLException e) {
					RootController.callAlent("���� ���� : DB���� ����");
				} finally {
					try {
						if(psmt!=null) {psmt.close();}
						if(con!=null) {con.close();}
						} catch (SQLException e) {
							RootController.callAlent("�ڿ��ݱ� ���� : psmt, con �ݱ� ����");
						}
				}
		return userAarryList;
	}
	
			public static int deleteUserData(String user) {
				// TableView���� ������ ���ڵ带 ����Ÿ���̽����� �����ϴ� �Լ�.
				String deleteMember="delete from usertbl where userid =? ";
				
				Connection con =null;
				
				PreparedStatement psmt=null;
				
				int count=0;
				try {
					con=DBUtility.getConnection();
					psmt=con.prepareStatement(deleteMember);
					psmt.setString(1, user);
					count=psmt.executeUpdate();
					if(count==0) {
						RootController.callAlent("Delete ���� : Delete �����Ͽ����ϴ�");
						return count;
					}
				} catch (SQLException e) {
					RootController.callAlent("Delete ���� : DBDelete ����");
				} finally {
					try {
						if(psmt!=null) {psmt.close();}
						if(con!=null) {con.close();}
					} catch (SQLException e) {
						RootController.callAlent("�ڿ��ݱ� ���� : psmt, con �ݱ� ����");
					}
				}
				return count;
			}
}
