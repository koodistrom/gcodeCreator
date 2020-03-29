package com.jaakkomantyla.gcc.gcodecreator;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.util.SAXDocumentFactory;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.util.XMLResourceDescriptor;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.*;


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


    public static void parse(){
        DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            String path = "C:/Users/Jaakko/Desktop/koulujutut/dynaaminen/GcodeCreator/testFiles/artrobot.svg";
            File x = new File(path);
            System.out.println(x.getName());
            Document doc = documentBuilder.parse(new File(path));




            System.out.println(doc.toString());
            //System.out.println(doc.getDoctype().toString());
            System.out.println(doc.getNodeName());
            printSvg(doc);





            SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(doc);

            SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, false);


        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public static byte[] prettyPrintXml(MultipartFile newFile) {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(newFile.getInputStream());

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            StreamResult streamResult = new StreamResult(out);
            DOMSource domSource = new DOMSource(document);
            transformer.transform(domSource, streamResult);

            return out.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static void printSvg(Node node){
        System.out.println(node);
        if(node.hasAttributes()){
            NamedNodeMap map = node.getAttributes();
            for(int i =0; i< map.getLength(); i++){
                System.out.println(map.item(i));
            }
        }

        NodeList l =  node.getChildNodes();
        for(int i =0; i< l.getLength(); i++){
            printSvg(l.item(i));
        }
    }


}
