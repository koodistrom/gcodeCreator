package com.jaakkomantyla.gcc.gcodecreator;

import com.jaakkomantyla.gcc.gcodecreator.gcode.Gcode;
import com.jaakkomantyla.gcc.gcodecreator.utils.BezierToArcs;
import com.jaakkomantyla.gcc.gcodecreator.utils.BiArc;
import com.jaakkomantyla.gcc.gcodecreator.utils.CubicBezier;
import com.jaakkomantyla.gcc.gcodecreator.utils.ToGCodeHandler;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.parser.*;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class GcodeCreatorApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void bezierConversionTests(){
		Assertions.assertEquals(new Vector2D(3,4).getNorm(), 5);
		//biArc();
		//bezier();
	}

	@Test
	void testSvgParsing(){
		testParseSVG();
	}

	void biArc(){

		BiArc ba = new BiArc(new Vector2D(0,0), new Vector2D(0,1), new Vector2D(2,0), new Vector2D(0,1), new Vector2D(1,1));
		System.out.println("biarc: " +ba);

		BiArc ba2 = new BiArc(new Vector2D(-1,0), new Vector2D(0,1), new Vector2D(1,0), new Vector2D(0,1), new Vector2D(0,1));
		System.out.println("biarc: " +ba2);
	}

	void bezier(){
		CubicBezier b = new CubicBezier(new Vector2D(0,0), new Vector2D(3,3), new Vector2D(6,3), new Vector2D(9,0));
		List<BiArc> biArcs = BezierToArcs.ApproxCubicBezier(b,1f,0.1f);

		System.out.println("bezier: \n" + b );
		System.out.println("arc list: \n"+ biArcs);
	}

	public static void testParseSVG(){
		DOMImplementation impl = GenericDOMImplementation.getDOMImplementation();
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setValidating(false);
			documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			String path = "C:/Users/Jaakko/Desktop/koulujutut/dynaaminen/GcodeCreator/testFiles/path1.svg";
			File x = new File(path);
			System.out.println(x.getName());
			Document doc = documentBuilder.parse(new File(path));




			System.out.println(doc.toString());
			//System.out.println(doc.getDoctype().toString());
			System.out.println(doc.getNodeName());
			//SVGHandler.printSvg(doc);
			SVGHandler.iterateSvg(doc, node -> {
				if(node.getNodeName().equals("path")) {
					if(node.hasAttributes()){
						NamedNodeMap map = node.getAttributes();
						System.out.println(map.getNamedItem("d").getNodeValue());
						pathDataToGcode(map.getNamedItem("d").getNodeValue());
						/*for(int i =0; i< map.getLength(); i++){

							System.out.println(map.item(i));
						}

						 */
					}


				}
			});




			SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(doc);

			SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, false);


		} catch (Exception ex) {
			System.out.println(ex);
		}

	}

	public static void pathDataToGcode(String s) throws ParseException {
		Gcode gcode = new Gcode();
		PathParser pp = new PathParser();
		PathHandler ph = new ToGCodeHandler(gcode);
		pp.setPathHandler(ph);
		pp.parse(s);
		gcode.printCommands();
	}

}
