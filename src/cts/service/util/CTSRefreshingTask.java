/*
 * Created on 2004/7/31
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cts.service.util;

import java.util.Properties;
import java.util.TimerTask;
import java.util.Vector;

import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.binding.AccessPoint;
import org.uddi4j.datatype.binding.BindingTemplate;
import org.uddi4j.datatype.binding.TModelInstanceDetails;
import org.uddi4j.response.BindingDetail;

import reconf.w3c.uddi.replication.client.ReplicaUDDIProxy;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CTSRefreshingTask extends TimerTask {

    private UDDIProxy proxy;

    private String serviceKey;
    private String bindingKey;
    private String accessPoint;
    
	private static final String INQUIRY_URL =  "http://localhost:8081/juddi/inquiry";
	
  //Publish
    private static final String PUBLISH_URL = "http://localhost:8081/juddi/publish";
    
	public CTSRefreshingTask(Properties properties) {
		try {
			  serviceKey = properties.getProperty("CTSServiceKey");
			  bindingKey = properties.getProperty("CTSBindingKey");
			  accessPoint = properties.getProperty("AccessPoint")+"?WSDL";
			  
			  MessageLogger.out.println(serviceKey + " "+bindingKey+" "+accessPoint);
			  
			  
			  proxy = ReplicaUDDIProxy.getInstance();
			  //proxy = new UDDIProxy(new URL(INQUIRY_URL), new URL(PUBLISH_URL));
				
		} catch (Exception e) {
			e.printStackTrace(MessageLogger.out);
		}
	}
	
	
	public void run() {

	    try{
	    	
          	Vector bindings = new Vector();
           	TModelInstanceDetails details = new TModelInstanceDetails();
           	BindingTemplate binding = new BindingTemplate(bindingKey,details, new AccessPoint(accessPoint, "http"));
           	binding.setServiceKey(serviceKey);
           	bindings.add(binding);
           	
         	BindingDetail detail = proxy.save_binding(null, bindings);
         	
	    } catch (Exception e){
	    	e.printStackTrace(MessageLogger.out);
	    }
	}
}
