/*
 */

package unsuck.xml;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Help with JAXB
 * 
 * @author Jeff Schnitzer
 */
public class JAXBUtils
{
	/** 
	 * Unmarshal the stream to an object 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(Class<T> docClass, InputStream inputStream) throws JAXBException {
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Unmarshaller u = jc.createUnmarshaller();
		
		return (T)u.unmarshal(inputStream);
	}
}