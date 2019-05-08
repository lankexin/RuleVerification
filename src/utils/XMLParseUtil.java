package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

public class XMLParseUtil {
	
	public XMLParseUtil() {
		
	}
	
	public static List<HashMap<String, String>> parseXML(String inputPath) {
		List<HashMap<String, String>> xmlContent = new ArrayList<>();
		
		SAXReader reader = new SAXReader();
		
		try {
			Document document = reader.read(inputPath);
			Element root = document.getRootElement();
			Iterator it = root.elementIterator();
			
			while (it.hasNext()) {
				System.out.print("*****************************\n");
				Element component = (Element)it.next();
				
				List<Attribute> componentAttrs = component.attributes();
				
				for (Attribute attr : componentAttrs) {
//					System.out.print("属性名: " + attr.getName() + "   属性值: "
//							+ attr.getValue() + "\n");
					
				}
				
				Iterator itt = component.elementIterator();
				
				while (itt.hasNext()) {
					HashMap<String, String> tempElement = new HashMap<>();
					Element componentChild = (Element) itt.next();

//					System.out.print("节点名: " + componentChild.getName() +
//							"   节点值: " + componentChild.getStringValue() + "\n");
					tempElement.put("class", componentChild.getName());
					
					List<Attribute> componentChildAttrs = componentChild.attributes();
					
					for (Attribute attr : componentChildAttrs) {
//						System.out.print("属性名: " + attr.getName() + "   属性值: "
//								+ attr.getValue() + "\n");
						
						tempElement.put(attr.getName(), attr.getValue());
					}
					
					xmlContent.add(tempElement);
				}
				
				
			}
			
//			System.out.print("*******************************\n");
			
			//System.out.print(xmlContent);
			
			return xmlContent;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
