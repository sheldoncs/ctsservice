package cts.service.test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.xml.rpc.Stub;

import CTSStub.CTSRemoteInterface;
import CTSStub.CTSService_Impl;




public class TestClient {

	  private Stub stub;
	  private CTSRemoteInterface IFRemote;
	  
	 public TestClient(String location){
		 
		 stub = createProxy();
		 stub._setProperty
			(javax.xml.rpc.Stub.ENDPOINT_ADDRESS_PROPERTY,
			location);
		 IFRemote = (CTSRemoteInterface)stub;
		 
	    
	 }
	 public void getList() throws RemoteException{
		 
		 ArrayList list = IFRemote.getLecturerList();
	     System.out.println(list.size());
	 }
	 
	 
	 private static Stub createProxy()
	 {
//				 Note: MyHelloService_Impl is implementation-specific.
				return (Stub) (new CTSService_Impl().getCTSRemoteInterfacePort());
	 }
	 public static void main(String[] args) throws RemoteException
	 {
		TestClient tr = new TestClient("http://behemoth01:8081/CTS/CTS");
		tr.getList();
	 }
}
