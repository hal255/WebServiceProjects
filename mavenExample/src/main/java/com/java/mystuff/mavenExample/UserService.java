package com.java.mystuff.mavenExample;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;


@Path("") 
public class UserService {  
	UserDao userDao = new UserDao();  
	@GET 
	@Path("/users")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) 
	public Response getUsers(@Context UriInfo uriInfo){
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(userDao.getAllUsers()){};
		String format = uriInfo.getQueryParameters().getFirst("format");
		if (format == "json"){
			for (int i = 0; i < 10000; i++){
				
			}
			return Response.ok(entity, MediaType.APPLICATION_JSON)
					.build();
		}
		return Response.ok(entity, format==null? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON)
				.build();
	}

    
    @GET
    @Path("/streaming")
    public Response streamXmlExample(@Context UriInfo uriInfo){
    	String format = uriInfo.getQueryParameters().getFirst("format");
        StreamingOutput streamJson = new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
            	List<User> userList = userDao.getAllUsers();
            	JsonFactory jf = new JsonFactory();
            	JsonGenerator jg = jf.createGenerator(out);
            	jg.setPrettyPrinter(new DefaultPrettyPrinter());
            	jg.writeStartArray();
            	for (int i = 0; i < 10000; i++){
                	for (User user: userList){
                		jg.writeStartObject();
                		jg.writeNumberField("id", user.getId());
                		if (user.getName() != null)
                			jg.writeStringField("name", user.getName());
                		if (user.getProfession() != null)
                			jg.writeStringField("profession", user.getProfession());
                		if (user.getCash() != null)
                			jg.writeNumberField("cash", user.getCash());
                		jg.writeEndObject();
                        jg.flush();
                	}
            	}
            	jg.writeEndArray();
            	jg.close();
            }
        };
    	
    	
        StreamingOutput streamXml = new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
            	
                XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
                XMLStreamWriter writer = null;
                try {
                    writer = outputFactory.createXMLStreamWriter(out, "utf-8");
                    writer.writeStartDocument("utf-8", "1.0");
                    writer.writeStartElement("users");
	            	for (int i = 0; i < 10000; i++){
	                    writer.writeStartElement("user");
	                    writeElem(writer, i, "name" + i, "profession" + i, i/2.0);
		                writer.writeEndElement();
	            	}
	                writer.writeEndElement();
                    writer.writeEndDocument();
                } catch (XMLStreamException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
                    if (writer != null)
						try {
							writer.close();
						} catch (XMLStreamException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                }

            }
        };
 
		return Response.ok(format==null? streamXml : streamJson).build();
    }
    
    private void writeElem(XMLStreamWriter writer, int id, String name, String profession, double cash) throws XMLStreamException {
    	writer.writeStartElement("id");
        writer.writeCharacters(id + "");
        writer.writeEndElement();
 
    	writer.writeStartElement("name");
        writer.writeCharacters(name);
        writer.writeEndElement();
 
    	writer.writeStartElement("profession");
        writer.writeCharacters(profession);
        writer.writeEndElement();

        writer.writeStartElement("cash");
        writer.writeCharacters(cash + "");
        writer.writeEndElement();
   }
}