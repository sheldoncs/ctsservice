package cts.service.remote;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import cts.service.db.CTSDb;
import cts.service.util.CourseProperties;
import cts.service.util.LecturerProperties;
import cts.service.util.MessageLogger;

public class CTSRemoteService implements CTSRemoteInterface {

	public ArrayList getLecturerList() throws RemoteException {
		// TODO Auto-generated method stub
		
		CTSDb db = new CTSDb(0);
		db.openConn();
		
		
		try {
		
			ArrayList list = db.collectLecturers(); 
		    return list;
		    
		} catch (SQLException ex){
			ex.printStackTrace();
		}
		return null;
	}

	public LecturerProperties getLecturerproperty(String pidm) throws RemoteException {
		// TODO Auto-generated method stub
		CTSDb db = new CTSDb(0);
		db.openConn();
	
		try {
		LecturerProperties lecturer = db.collectLecturerDetail(pidm);
		return lecturer;
		} catch (SQLException ex){
			ex.printStackTrace();
		}
		return null;
	}

	public ArrayList getLecturerCourses(String pidm) throws RemoteException {
		// TODO Auto-generated method stub
		CTSDb db = new CTSDb(0);
		db.openConn();
		try {
			ArrayList list = db.collectLecturerCourses(pidm);
			
			
			return list;
		} catch (Exception ex){
			ex.printStackTrace();
		}
		  return null;
	}

	
	public CourseProperties getCourseProperty(String crn)
			throws RemoteException {
		// TODO Auto-generated method stub
		
		CTSDb db = new CTSDb(0);
		db.openConn();
		try {
			CourseProperties course = db.collectCourse(crn);
			return course;
		} catch (Exception ex){
			ex.printStackTrace();
		}
		  return null;
	}

	public LecturerProperties getLecturerProperty(String pidm)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public CourseProperties getSingleCourse(String crn, String title, String day,
			String startTime) throws RemoteException {
		// TODO Auto-generated method stub
		
		CTSDb db = new CTSDb(0);
		db.openConn();
		
		try {
			CourseProperties cp = db.collectSingleCourse(crn, title, day, startTime);
			return cp;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	

	

}
