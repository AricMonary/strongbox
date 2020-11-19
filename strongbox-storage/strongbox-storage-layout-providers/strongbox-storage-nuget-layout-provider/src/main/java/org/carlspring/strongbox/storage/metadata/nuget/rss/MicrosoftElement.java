package org.carlspring.strongbox.storage.metadata.nuget.rss;

import com.google.common.base.Strings;
import org.carlspring.strongbox.artifact.coordinates.versioning.SemanticVersion;
import org.carlspring.strongbox.storage.metadata.nuget.StringListTypeAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MicrosoftElement
{
    /**
     * Creates an XML element of Microsoft DataTable format from version
     *
     * @param name
     *            the name of the XML element
     * @param nullable
     *            whether the element can be NULL
     * @param value
     *            element value
     * @return XML element
     * @throws ParserConfigurationException
     */
    public static Element createMicrosoftElement(String name,
                                           boolean nullable,
                                           SemanticVersion value)
            throws ParserConfigurationException
    {
        String stringValue = value == null ? null : value.toString();
        return createMicrosoftElement(name, nullable, MicrosoftTypes.String, stringValue);
    }

    /**
     * Creates a Microsoft DataTable XML format element from a string
     *
     * @param name
     *            the name of the XML element
     * @param nullable
     *            whether the element can be NULL
     * @param value
     *            element value
     * @return XML element
     * @throws ParserConfigurationException
     */
    public static Element createMicrosoftElement(String name,
                                           boolean nullable,
                                           String value)
            throws ParserConfigurationException
    {
        return createMicrosoftElement(name, nullable, MicrosoftTypes.String, value);
    }

    /**
     * Creates an XML element of the Microsoft DataTable format from an integer
     *
     * @param name
     *            the name of the XML element
     * @param nullable
     *            whether the element can be NULL
     * @param value
     *            element value
     * @return XML element
     * @throws ParserConfigurationException
     */
    public static Element createMicrosoftElement(String name,
                                           boolean nullable,
                                           Integer value)
            throws ParserConfigurationException
    {
        String stringValue = value == null ? null : value.toString();
        return createMicrosoftElement(name, nullable, MicrosoftTypes.Int32, stringValue);
    }

    /**
     * Creates an XML element of Microsoft DataTable format from long
     *
     * @param name
     *            the name of the XML element
     * @param nullable
     *            whether the element can be NULL
     * @param value
     *            element value
     * @return XML element
     * @throws ParserConfigurationException
     */
    public static Element createMicrosoftElement(String name,
                                           boolean nullable,
                                           Long value)
            throws ParserConfigurationException
    {
        String stringValue = value == null ? null : value.toString();
        return createMicrosoftElement(name, nullable, MicrosoftTypes.Int64, stringValue);
    }

    /**
     * Creates an XML element of the Microsoft DataTable format from a floating
     * point number
     *
     * @param name
     *            the name of the XML element
     * @param nullable
     *            whether the element can be NULL
     * @param value
     *            element value
     * @return XML element
     * @throws ParserConfigurationException
     */
    public static Element createMicrosoftElement(String name,
                                           boolean nullable,
                                           Double value)
            throws ParserConfigurationException
    {
        String stringValue = value == null ? null : value.toString();
        return createMicrosoftElement(name, nullable, MicrosoftTypes.Double, stringValue);
    }

    /**
     * Creates a Microsoft DataTable XML format element from boolean
     *
     * @param name
     *            the name of the XML element
     * @param nullable
     *            whether the element can be NULL
     * @param value
     *            element value
     * @return XML element
     * @throws ParserConfigurationException
     */
    public static Element createMicrosoftElement(String name,
                                           boolean nullable,
                                           Boolean value)
            throws ParserConfigurationException
    {
        String stringValue = value == null ? null : value.toString();
        return createMicrosoftElement(name, nullable, MicrosoftTypes.Boolean, stringValue);
    }

    /**
     * Creates a Microsoft DataTable XML format element from date / time
     *
     * @param name
     *            the name of the XML element
     * @param nullable
     *            whether the element can be NULL
     * @param value
     *            element value
     * @return XML element
     * @throws ParserConfigurationException
     */
    public static Element createMicrosoftElement(String name,
                                           boolean nullable,
                                           Date value)
            throws ParserConfigurationException
    {
        String stringValue = null;
        if (value != null)
        {
            try
            {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(value);
                XMLGregorianCalendar xmlgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
                stringValue = xmlgc.toXMLFormat();
            }
            catch (DatatypeConfigurationException ex)
            {
                throw new ParserConfigurationException("Failed to convert date to XML");
            }
        }
        return createMicrosoftElement(name, nullable, MicrosoftTypes.DateTime, stringValue);
    }

    /**
     * Creates a Microsoft DataTable XML format element from a list of strings
     *
     * @param name
     *            the name of the XML element
     * @param nullable
     *            whether the element can be NULL
     * @param value
     *            element value
     * @return XML element
     * @throws ParserConfigurationException
     */
    public static Element createMicrosoftElement(String name,
                                           boolean nullable,
                                           List<String> value)
            throws ParserConfigurationException
    {
        String stringValue = null;
        if (value != null)
        {
            try
            {
                StringListTypeAdapter adapter = new StringListTypeAdapter();
                stringValue = adapter.marshal(value);
            }
            catch (Exception e)
            {
                throw new ParserConfigurationException("Error converting string list");
            }
        }
        Element element = createMicrosoftElement(name, nullable, MicrosoftTypes.DateTime, stringValue);
        element.setAttributeNS(XMLConstants.XML_NS_URI, "space", "preserve");
        return element;
    }

    /**
     * Creates a Microsoft DataTable XML format element from a string
     *
     * @param name
     *            the name of the XML element
     * @param nullable
     *            whether the element can be NULL
     * @param type
     *            element type by Microsoft version
     * @param value
     *            element value
     * @return XML element
     * @throws ParserConfigurationException
     */
    public static Element createMicrosoftElement(String name,
                                           boolean nullable,
                                           MicrosoftTypes type,
                                           String value)
            throws ParserConfigurationException
    {
        Document document = createDocument();
        Element element = document.createElementNS("http://schemas.microsoft.com/ado/2007/08/dataservices", name);
        if (nullable && Strings.isNullOrEmpty(value))
        {
            element.setAttributeNS("http://schemas.microsoft.com/ado/2007/08/dataservices/metadata", "null", "true");
        }
        if (type != MicrosoftTypes.String)
        {
            element.setAttributeNS("http://schemas.microsoft.com/ado/2007/08/dataservices/metadata", "type",
                    type.toString());
        }
        element.setTextContent(value);
        document.appendChild(element);
        document.normalizeDocument();
        return document.getDocumentElement();
    }

    /**
     * Creates an XMl document for further use in conversion
     *
     * @return empty XML document
     * @throws ParserConfigurationException
     */
    private static Document createDocument()
            throws ParserConfigurationException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder docb = dbf.newDocumentBuilder();
        Document document = docb.newDocument();
        return document;
    }
}
