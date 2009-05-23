package cc.scada;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import java.io.*;
import java.util.*;


/**
 * Parser XML - LinkedHashMap or LinkedHashMap - XML 
 * @version 1.0, 29/05/09
 * @author CaKeChuff team
 */
public class XMLManager{
	
	private static XMLManager _uniqueInstance;
 
	private XMLManager(){}
  
	public static XMLManager getInstance(){
		if (_uniqueInstance==null){
			_uniqueInstance = new XMLManager();
		}
		return _uniqueInstance;
	}

	/**
	 * Generate an XML file with the name given by parameter
	 * Fill it with the values contained in the hash map
	 * The root will be the type given in the first argument
	 * @param type Type for the root element of the XML file
	 * @param filename Name of the XML file to be generated
	 * @param h Linked hash map with the data
	 * @return f XML file generated
	 * @exception IOException Error getting the XML properties file (.dtd)
	 */
	public File generate (String type, String filename, LinkedHashMap h){
		Properties props = new Properties();
    
		try{
			props.load(new FileInputStream("dtd.properties"));
		}
		catch(IOException e){
			System.out.println("Error while getting XML properties file");
			e.printStackTrace();
		}
		Element root=new Element(type);
    
		Iterator i = h.keySet().iterator();
		while(i.hasNext()){
			Object attribute = i.next();
			Element elem = new Element(attribute.toString());
			elem.setText(h.get(attribute).toString());
			root.addContent(elem);
		}
		DocType doctype = new DocType(type, props.getProperty(type+".dtd"));
		Document doc=new Document(root, doctype);
		File f = null;
		try{
			XMLOutputter out=new XMLOutputter(Format.getPrettyFormat());
			f = new File(filename);
			FileOutputStream file=new FileOutputStream(f);
			out.output(doc,file);
			file.flush();
			file.close();
     
		}catch(Exception e){e.printStackTrace();}
		return f;
	}
  
  /**
   * Read the content of an XML file
   * Translate it to a LinkedHashMap
   * @param file Name of the XML file to be read
   * @return h Loaded LinkedHashMap 
   */
  public LinkedHashMap read(File file){
      try{
          
          LinkedHashMap h = new LinkedHashMap();
          SAXBuilder builder = new SAXBuilder(true);
          Document doc = builder.build(file);
          Element root = doc.getRootElement();
          List children = root.getChildren();
          Iterator<Element> i = children.iterator();
          while(i.hasNext()){
              Element e = i.next();
              h.put(e.getName(), e.getText());
          }
          return h;
      } 
      catch(Exception e){
          e.printStackTrace();
          return null;
      }
  }
}