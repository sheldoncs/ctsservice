package cts.service.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import cts.service.util.CourseProperties;
import cts.service.util.LecturerProperties;


public interface CTSRemoteInterface extends Remote {
	
	public ArrayList getLecturerList() throws RemoteException;
	public LecturerProperties getLecturerProperty(String pidm) throws RemoteException;
    public ArrayList getLecturerCourses(String pidm) throws RemoteException;
    public CourseProperties getSingleCourse(String crn, String title, String day, String startTime) throws RemoteException;
    public CourseProperties getCourseProperty(String crn) throws RemoteException; 
    

}
