package Controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtility {
	public static Connection getConnection() {
		Connection con=null;
		try {
			//1. MYSQL DataBase Class �ε��Ѵ�.
			Class.forName("com.mysql.jdbc.Driver");
		//2. �ּҿ� ���̵�, ��й�ȣ�� ���ؼ� ���ӿ�û�Ѵ�.
		con=DriverManager.getConnection("jdbc:mysql://localhost/memberdb", "root", "123456");
		
		} catch (Exception e) {
			RootController.callAlent("������� : DB���� ����");
			return null;
		}
		return con;
	}
}
