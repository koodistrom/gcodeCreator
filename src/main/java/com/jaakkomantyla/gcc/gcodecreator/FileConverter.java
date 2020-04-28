package com.jaakkomantyla.gcc.gcodecreator;

import com.jaakkomantyla.gcc.gcodecreator.gcode.DocParser;
import com.jaakkomantyla.gcc.gcodecreator.gcode.Gcode;
import com.jaakkomantyla.gcc.gcodecreator.gcode.Options;
import org.apache.batik.dom.GenericDOMImplementation;
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

/**
 * The File converter class has methods that take MultipartFile objects received from front end and returns
 * the wanted outputs as byte arrays that can be send back to the front end.
 */
public class FileConverter {


    /**
     * Pretty print xml byte [ ] takes xml file as MultipartFile object and reformats it. Each node
     * is printed to their own line and indented properly.
     *
     * @param file the file to prettify
     * @return the reformated file as byte array
     */
    public static byte[] prettyPrintXml(MultipartFile file) {
        Document document = mPFileToDoc(file);
        try {
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

    /**
     * Converts svg presented as MultipartFile object to gcode.
     *
     * @param file the svg file to convert
     * @param options the user given options
     * @return the gcode as byte array
     */
    public static byte[] mPFileToGcode(MultipartFile file, Options options){
        String fileName = file.getOriginalFilename();
        Document doc = mPFileToDoc(file);
        Gcode g = DocParser.docToGcode(doc, fileName, options);
        return g.commandsAsByteArray();
    }

    /**
     * Used in testing prints a node and all its child nodes.
     *
     * @param node the node
     */
    public static void printNode(Node node){
        System.out.println(node);
        if(node.hasAttributes()){
            NamedNodeMap map = node.getAttributes();
            for(int i =0; i< map.getLength(); i++){
                System.out.println(map.item(i));
            }
        }

        NodeList l =  node.getChildNodes();
        for(int i =0; i< l.getLength(); i++){
            printNode(l.item(i));
        }
    }


    /**
     * Converts xml file presented as MultipartFile object to Document.
     *
     * @param file the file to convert
     * @return the document
     */
    public static Document mPFileToDoc(MultipartFile file) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file.getInputStream());
            return document;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Converts xml file from given path to Document.
     *
     * @param path the path
     * @return the document
     */
    public static Document fileToDoc(String path){
        DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            File x = new File(path);
            System.out.println(x.getName());
            Document doc = documentBuilder.parse(new File(path));
            return doc;
        }catch (Exception ex){
                ex.printStackTrace();
                return null;
        }
    }


    /**
     * Gets the name from MultipartFile and changes the ending to user given one
     *
     * @param file   the file
     * @param ending the new ending
     * @return the name with new ending
     */
    public static String changeFileEnding(MultipartFile file, String ending){
        String str = file.getOriginalFilename().split("\\.")[0];
        return str+ending;
    }

}
