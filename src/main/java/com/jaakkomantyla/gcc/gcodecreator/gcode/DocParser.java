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

public class DocParser {


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
