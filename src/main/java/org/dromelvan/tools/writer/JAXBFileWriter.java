package org.dromelvan.tools.writer;

import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.dromelvan.tools.parser.old.ParserObject;

import com.google.common.io.Resources;

public abstract class JAXBFileWriter<T extends ParserObject> extends AbstractFileWriter<T> {

    private Class<?> xmlRootClass;

    public Class<?> getXmlRootClass() {
        return xmlRootClass;
    }
    public void setXmlRootClass(Class<?> xmlRootClass) {
        this.xmlRootClass = xmlRootClass;
    }

    protected abstract JAXBElement buildDocument(T parserObject);
    
    @Override
    public void write(T parserObject) {
        URL url = Resources.getResource(getXmlRootClass(), getXmlRootClass().getSimpleName() + ".xsd");
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            Schema schema = schemaFactory.newSchema(url);
            JAXBContext jaxbContext = JAXBContext.newInstance(getXmlRootClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setSchema(schema);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(buildDocument(parserObject), getFile());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
