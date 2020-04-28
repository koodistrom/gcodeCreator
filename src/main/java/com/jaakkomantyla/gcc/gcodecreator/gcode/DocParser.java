package com.jaakkomantyla.gcc.gcodecreator.gcode;

import com.jaakkomantyla.gcc.gcodecreator.utils.ToGCodeHandler;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.PathHandler;
import org.apache.batik.parser.PathParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.function.Consumer;

/**
 * The DocParser class holds methods for parsing SVG files as org.w3c.dom.Document objects to my custom Gcode objects.
 */
public class DocParser {


    /**
     * Doc to gcode.
     *
     * @param doc      the svg as a Document object
     * @param filename the filename to be given to the gcode file
     * @param options the user given options for creating the gcode
     * @return the gcode
     */
    public static Gcode docToGcode(Document doc, String filename,  Options options){
        Gcode gcode = new Gcode(options);
        gcode.addCommand(new Command("Generated from: "+filename));
        iterateSvg(doc, node -> {
            if(node.getNodeName().equals("path")) {
                pathToGcode(node, gcode);
            }
        });

        return gcode;
    }

    /**
     * Doc to gcode.
     *
     * @param doc      the svg as a Document object
     * @param filename the filename to be given to the gcode file
     * @return the gcode
     */
    public static Gcode docToGcode(Document doc, String filename){
        Gcode gcode = new Gcode();
        gcode.addCommand(new Command("Generated from: "+filename));
        iterateSvg(doc, node -> {
            if(node.getNodeName().equals("path")) {
                pathToGcode(node, gcode);
            }
        });

        return gcode;
    }

    /**
     * Path to gcode is used by docToGcode() method to parse svg path to gcode commands.
     *
     * @param node  the svg path node from Document
     * @param gcode the gcode object to which the commands are added
     * @throws ParseException the parse exception
     */
    public static void pathToGcode(Node node, Gcode gcode) throws ParseException {

        NamedNodeMap map = node.getAttributes();
        if(map.getNamedItem("id")!=null){
            gcode.addCommand(new Command(map.getNamedItem("id").getNodeValue()));
        }
        System.out.println(map.getNamedItem("d").getNodeValue());
        String str = map.getNamedItem("d").getNodeValue();

        PathParser pp = new PathParser();
        PathHandler ph = new ToGCodeHandler(gcode);
        pp.setPathHandler(ph);
        pp.parse(str);

    }

    /**
     * Iterates Document. Used by docToGcode() method to iterate svg.
     *
     * @param document the document to iterate
     * @param doThings the Consumer that holds operations to do to the node
     */
    public static void iterateSvg(Document document, Consumer<Node> doThings){
        NodeList nodeList = document.getElementsByTagName("*");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                doThings.accept(node);
            }
        }
    }

}
