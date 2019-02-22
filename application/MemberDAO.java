package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.BlackList;
import Model.Login;
import Model.MemberInfomation;
import Model.MemberTable;
import Model.MusicRecommend;
import Model.RcNum;
import Model.RcSinger;
import Model.Sing;
import Model.SingRecommend;
import Model.User;

	
public class MemberDAO {
	public static ArrayList<RcSinger> rsList= new ArrayList<>();
	public static ArrayList<MusicRecommend> mrList= new ArrayList<>();
	public static ArrayList<MemberInfomation> dbAarryList= new ArrayList<>();
	public static ArrayList<MemberTable> memberTb= new ArrayList<>();
	public static ArrayList<Login> loginAarryList= new ArrayList<>();
	public static ArrayList<BlackList> blackAarryList= new ArrayList<>();
	public static ArrayList<String> loginList= new ArrayList<>();
	public static ArrayList<String> idList= new ArrayList<>();
	
	public static int insertMemberData(MemberInfomation member) {
		// �����ͺ��̽� ȸ�����̺� �Է��ϴ� ������
		StringBuffer insertMember=new StringBuffer();
		insertMember.append("insert into membertbl ");
		insertMember.append("(userid,password,name,age,usernick,gender,genre,blacklist,blcause) ");
		insertMember.append("values ");
		insertMember.append("(?,?,?,?,?,?,?,?,?) ");
		
		Connection con =null;
		
		PreparedStatement psmt=null;
		int count=0;
		try {
			con=DBUtility.getConnection();
			psmt=con.prepareStatement(insertMember.toString());
			
			psmt.setString(1, member.getMbId());
			psmt.setString(2, member.getMbPassword());
			psmt.setString(3, member.getMbName());
			psmt.setString(4, member.getMbAge());
			psmt.setString(5, member.getMbNickName());
			psmt.setString(6, member.getMbGender());
			psmt.setString(7, member.getMbGenre());
			psmt.setString(8, "x");
			psmt.setString(9, "�ش����");
			
			count=psmt.executeUpdate();
			if(count==0) {
				RootController.callAlent("�������� ���� : DB�������� ����");
				return count;
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
		return count;
	}
	
	public static int insertSingData(SingRecommend sr) {
		// �����ͺ��̽� �뷡��õ���̺� �Է��ϴ� ������
		StringBuffer insertSing=new StringBuffer();
		insertSing.append("insert into topsinger ");
		insertSing.append("(singer,title,genre,userid) ");
		insertSing.append("values ");
		insertSing.append("(?,?,?,?) ");
		
		Connection con =null;
		
		PreparedStatement psmt=null;
		int count=0;
		try {
			con=DBUtility.getConnection();
			psmt=con.prepareStatement(insertSing.toString());
			
			psmt.setString(1, sr.getSinger());
			psmt.setString(2, sr.getSingTitle());
			psmt.setString(3, sr.getGenre()); 
			psmt.setString(4, sr.getUserid());
		
			count=psmt.executeUpdate();
			if(count==0) {
				RootController.callAlent("�������� ���� : DB�������� ����");
				return count;
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
		return count;
	}
	
	public static int insertBlackData(BlackList bl) {
		// �����ͺ��̽� �뷡��õ���̺� �Է��ϴ� ������
		StringBuffer insertSing=new StringBuffer();
		insertSing.append("insert into member ");
		insertSing.append("(blacklist,blcause) ");
		insertSing.append("values ");
		insertSing.append("(?,?) ");
		
		Connection con =null;
		
		PreparedStatement psmt=null;
		int count=0;
		try {
			con=DBUtility.getConnection();
			psmt=con.prepareStatement(insertSing.toString());
			
			psmt.setString(1, bl.getBlackid());
			psmt.setString(2, bl.getBlackCause());
			
			count=psmt.executeUpdate();
			if(count==0) {
				RootController.callAlent("�������� ���� : DB�������� ����");
				return count;
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
		return count;
	}
	
	public static ArrayList<MemberInfomation> getMemberTotalData(){
		// �����ͺ��̽� ȸ�����̺�� ��� �������� ������
		String selectMember="select * from membertbl ";
				
				Connection con =null;
				
				PreparedStatement psmt=null;
				
				ResultSet rs=null;
				try {
					con=DBUtility.getConnection();
					psmt=con.prepareStatement(selectMember);
					rs=psmt.executeQuery();
					if(rs==null) {
						RootController.callAlent("Select ���� : Select �����Ͽ����ϴ�");
						return null;
					}
					while(rs.next()) {
						MemberInfomation member=new MemberInfomation(
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6),
							rs.getString(7));
					dbAarryList.add(member);
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
		return dbAarryList;
	}
	
	public static ArrayList<MemberTable> getMemberTableData(){
		// �����ͺ��̽� ȸ�����̺�� ������ �������� ������
		String selectMember="select userid,name,age,usernick,gender,genre,blacklist,blcause from membertbl ";
				
				Connection con =null;
				
				PreparedStatement psmt=null;
				
				ResultSet rs=null;
				try {
					con=DBUtility.getConnection();
					psmt=con.prepareStatement(selectMember);
					rs=psmt.executeQuery();
					if(rs==null) {
						RootController.callAlent("Select ���� : Select �����Ͽ����ϴ�");
						return null;
					}
					while(rs.next()) {
						MemberTable member2=new MemberTable(
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6),
							rs.getString(7),
							rs.getString(8));
						memberTb.add(member2);
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
		return memberTb;
	}
	
	public static ArrayList<Login> getLoginData(){
		//�����ͺ��̽� ������̺�� �α��� ������ �������� ������
		String selectLogin="select userid,password from membertbl ";
				
				Connection con =null;
				
				PreparedStatement psmt=null;
				
				ResultSet rs=null;
				try {
					con=DBUtility.getConnection();
					psmt=con.prepareStatement(selectLogin);
					rs=psmt.executeQuery();
					if(rs==null) {
						RootController.callAlent("Select ���� : Select �����Ͽ����ϴ�");
						return null;
					}
					while(rs.next()) {
						Login login=new Login(
								rs.getString(1),
								rs.getString(2));
						loginAarryList.add(login);
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
		return loginAarryList;
	}
	
	public static String getBlackData(String userid){
		String page=null;
		// �����ͺ��̽� ������̺�� �� ������ �������� ������
		String selectLogin="select blacklist from membertbl where userid= "+"'"+userid+"'";
	
		Connection con =null;
		
		PreparedStatement psmt=null;
		
		ResultSet rs=null;
		try {
			con=DBUtility.getConnection();
			psmt=con.prepareStatement(selectLogin);
			rs=psmt.executeQuery();
			if(rs==null) {
				RootController.callAlent("Select ���� : Select �����Ͽ����ϴ�");
				return null;
			}
				while(rs.next()) {
				page=rs.getString("blacklist");
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
		return page;
	}
		
	public static ArrayList<String> getIdData(){
		// �����ͺ��̽� ������̺�� ���̵� ������ �������� ������
		String selectId="select userid from membertbl ";
				
				Connection con =null;
				
				PreparedStatement psmt=null;
				
				ResultSet rs=null;
				try {
					con=DBUtility.getConnection();
					psmt=con.prepareStatement(selectId);
					rs=psmt.executeQuery();
					if(rs==null) {
						RootController.callAlent("Select ���� : Select �����Ͽ����ϴ�");
						return null;
					}
					while(rs.next()) {
						idList.add(rs.getString(1));
					}
				} catch (SQLException e) {
					RootController.callAlent("���� ���� : DB���� ����");
				} finally {
					//1.6 �ڿ���ü�� �ݾƾ� �Ѵ�.
					try {
						if(psmt!=null) {psmt.close();}
						if(con!=null) {con.close();}
						} catch (SQLException e) {
							RootController.callAlent("�ڿ��ݱ� ���� : psmt, con �ݱ� ����");
						}
				}
		return idList;
	}
	public static ArrayList<MusicRecommend> getTopSingerData(){
		// �����ͺ��̽� top10�뷡��õ���� ��� �������� ������
		String selectMember="select singer, title, genre, sum(rcnum) from topsinger GROUP BY singer,title order by sum(rcnum) desc ";
				
				Connection con =null;
				
				PreparedStatement psmt=null;
				
				ResultSet rs=null;
				try {
					con=DBUtility.getConnection();
					psmt=con.prepareStatement(selectMember);
					rs=psmt.executeQuery();
					if(rs==null) {
						RootController.callAlent("Select ���� : Select �����Ͽ����ϴ�");
						return null;
					}
					mrList.clear();
					while(rs.next()) {
						MusicRecommend member=new MusicRecommend(
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4));
						mrList.add(member);
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
		return mrList;
	}
	
	public static ArrayList<RcSinger> getRcSingerData(){
		// �����ͺ��̽� top10�뷡��õ���� ��� �������� ������
		String selectMember="select singer,title,genre,userid from topsinger ";
				
				Connection con =null;
				
				PreparedStatement psmt=null;
				
				ResultSet rs=null;
				try {
					con=DBUtility.getConnection();
					psmt=con.prepareStatement(selectMember);
					rs=psmt.executeQuery();
					if(rs==null) {
						RootController.callAlent("Select ���� : Select �����Ͽ����ϴ�");
						return null;
					}
					rsList.clear();
					while(rs.next()) {
						RcSinger singer=new RcSinger(
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4));
						rsList.add(singer);
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
		return rsList;
	}
	
		public static int deleteMemberData(String userid) {
			// TableView���� ������ ���ڵ带 ����Ÿ���̽����� �����ϴ� �Լ�.
					String deleteMember="delete from membertbl where userid =? ";
							
							Connection con =null;
						
							PreparedStatement psmt=null;
							int count=0;
							try {
								con=DBUtility.getConnection();
								psmt=con.prepareStatement(deleteMember);
								psmt.setString(1, userid);
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
		
		public static int deleteSingData(String singer) {
			// TableView���� ������ ���ڵ带 ����Ÿ���̽����� �����ϴ� �Լ�.
			String deleteMember="delete from topsinger where singer =? ";
			
			Connection con =null;
			
			PreparedStatement psmt=null;
			
			int count=0;
			try {
				con=DBUtility.getConnection();
				psmt=con.prepareStatement(deleteMember);
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
	
		public static int updateMemberData(MemberTable member) {
			// ���̺�信�� ������ ���ڵ带 �����ͺ��̽� ���̺� �����ϴ� �Լ�.
			StringBuffer updateMember=new StringBuffer();
			updateMember.append("update membertbl set ");
			updateMember.append("usernick=?, genre=? where userid=? ");
	
			Connection con =null;
		
			PreparedStatement psmt=null;
			int count=0;
			try {
				con=DBUtility.getConnection();
				psmt=con.prepareStatement(updateMember.toString());
				psmt.setString(1, member.getUserNick());
				psmt.setString(2, member.getGenre());
				psmt.setString(3, member.getUserid());
				
				count=psmt.executeUpdate();
				if(count==0) {
					RootController.callAlent("�������� ���� : DB�������� ����");
					return count;
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
			return count;
		}
		
		public static int updateBlackData(MemberTable black) {
			// ���̺�信�� ������Ʈ�� �߰��ϴ� �Լ�.
			StringBuffer updateMember2=new StringBuffer();
			updateMember2.append("update membertbl set ");
			updateMember2.append("userid=?, name=?, age=?, usernick=?, gender=?, genre=?, blacklist=?, blcause=? where userid=? ");
			
			Connection con =null;
			
			PreparedStatement psmt=null;
			int count=0;
			try {
				con=DBUtility.getConnection();
				psmt=con.prepareStatement(updateMember2.toString());
				
				psmt.setString(1, black.getUserid());
				psmt.setString(2, black.getName());
				psmt.setString(3, black.getAge());
				psmt.setString(4, black.getUserNick());
				psmt.setString(5, black.getGender());
				psmt.setString(6, black.getGenre());
				psmt.setString(7, black.getBlacklist());
				psmt.setString(8, black.getBlcause());
				psmt.setString(9, black.getUserid());
				
				count=psmt.executeUpdate();
				if(count==0) {
					RootController.callAlent("�������� ���� : DB�������� ����");
					return count;
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
			return count;
		}
}


