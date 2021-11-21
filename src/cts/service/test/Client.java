package cts.service.test;


import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.encoding.Deserializer;
import javax.xml.rpc.encoding.Serializer;
import javax.xml.rpc.encoding.TypeMapping;
import javax.xml.rpc.encoding.TypeMappingRegistry;




public class Client {

	private static String ENCODING_STYLE_PROPERTY = "javax.xml.rpc.encodingstyle.namespace.uri";
	private static String NS_XSD = "http://www.w3.org/2001/XMLSchema";
	private static String URI_ENCODING = "http://schemas.xmlsoap.org/soap/encoding/";

	static String serviceName = "CTSService";
	static String qnamePort = "CTSRemoteInterfacePort";
	
	public void testService(){
		
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String URL = "http://behemoth01:8081/CTS/CTS";

		

		String nameSpaceUri = "http://behemoth01:8081/CTS/CTS?WSDL";

		try {
			
			
			ServiceFactory factory = ServiceFactory.newInstance();
			Service service = factory.createService(new QName(serviceName));

			// ===========================================================================================================
			// Code for adding ArrayList to typemapping retistry
			TypeMappingRegistry typemappingregistry = service
					.getTypeMappingRegistry();

			TypeMapping typemapping = typemappingregistry
					.getTypeMapping(URI_ENCODING);

			QName arrayListTypeQName = new QName(
					"http://java.sun.com/jax-rpc-ri/internal", "arrayList");
			
			QName lecturerPropertiesTypeQName = new QName(
					"http://java.sun.com/jax-rpc-ri/internal", "LecturerProperties");
			
			
			
			TypeMappingRegistry registry = com.sun.xml.rpc.client.BasicService
					.createStandardTypeMappingRegistry();
			com.sun.xml.rpc.encoding.InternalTypeMappingRegistryImpl internaltypemappingregistry = new com.sun.xml.rpc.encoding.InternalTypeMappingRegistryImpl(
			
					registry);
			
			/*ArrayList*/
			
			com.sun.xml.rpc.encoding.CombinedSerializer mapSerializer = (com.sun.xml.rpc.encoding.CombinedSerializer) internaltypemappingregistry
					.getSerializer(URI_ENCODING, java.util.ArrayList.class,
							arrayListTypeQName);
			
			
			/*ArrayList*/
			Serializer obj = new com.sun.xml.rpc.encoding.ReferenceableSerializerImpl(
					false, mapSerializer,
					com.sun.xml.rpc.soap.SOAPVersion.SOAP_11);
			
			
			
			
			/*ArrayList*/
			typemapping
			.register(
					java.util.ArrayList.class,
					arrayListTypeQName,
					new com.sun.xml.rpc.encoding.SingletonSerializerFactory(
							obj),
					new com.sun.xml.rpc.encoding.SingletonDeserializerFactory(
							(Deserializer) obj));
			
			
			// ===========================================================================================================

			QName port = new QName(qnamePort);
			//QName QNAME_TYPE_STRING = new QName(NS_XSD, "string");
			QName QNAME_TYPE_ValueType = arrayListTypeQName;
			
			Call call = service.createCall(port);
			call.setTargetEndpointAddress(URL);
			call.setProperty(Call.SOAPACTION_USE_PROPERTY, new Boolean(true));
			call.setProperty(Call.SOAPACTION_URI_PROPERTY, "");
			call.setProperty(ENCODING_STYLE_PROPERTY, URI_ENCODING);
			call.setReturnType(QNAME_TYPE_ValueType);
			call.setOperationName(new QName(nameSpaceUri,
					"getLecturerList"));
			
			//Object resp = call.invoke(arrayListTypeQName, null);
		    //ArrayList list = (ArrayList)call.invoke(null);
		    call.invoke(null);
		    
		 
		    //System.out.println(resp.getClass());
			//call.addParameter("String_1", QNAME_TYPE_STRING, ParameterMode.IN);

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
