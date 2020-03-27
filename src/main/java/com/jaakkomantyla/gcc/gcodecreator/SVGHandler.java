package com.jaakkomantyla.gcc.gcodecreator;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.svg.SVGDocumentFactory;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class SVGHandler {

    public void test(){
        DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
        try {
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXDocumentFactory f = new SAXDocumentFactory( impl, parser);

            String uri = "http://www.w3.org/2000/svg";
            Document doc = f.createDocument(uri);
            System.out.print(doc.toString());

            SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(doc);

            SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, false);

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }



    public static boolean prettyPrintXml(File newFile) {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(newFile);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StreamResult streamResult = new StreamResult(newFile);
            DOMSource domSource = new DOMSource(document);
            transformer.transform(domSource, streamResult);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }


}
