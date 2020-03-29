package com.jaakkomantyla.gcc.gcodecreator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

@CrossOrigin("*")
@RestController
public class GCCController {


    private static final Logger logger = LoggerFactory.getLogger(GCCController.class);

    @PostMapping(value = "/uploadSVG", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadFile(@RequestParam MultipartFile file) {
        logger.info(String.format("File name '%s' uploaded successfully.", file.getOriginalFilename()));

        byte[] svgAsByteArr = SVGHandler.prettyPrintXml(file);
        ByteArrayInputStream bais = new ByteArrayInputStream(svgAsByteArr);
        int length = svgAsByteArr.length;


        InputStreamResource resource = new InputStreamResource(bais);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getOriginalFilename())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(length)
                .body(resource);
    }

    /*
    @RequestMapping(value = "/svgPrettyPrint", method = RequestMethod.POST)
    public File svgPrettyPrint(@RequestBody PlayerInfo playerInfo){
        String id = playerInfo.getId();
        Optional<Score> score = highScoreRepository.findById(playerInfo.getId());

        Integer points = score.get().getPoints();
        String name =  score.get().getName();
        if(points == null){
            points = 0;
        }
        Optional<PushCounter> pushCounter= pushCountRepository.findById(1);
        if(pushCounter.isPresent() && points>0){
            points --;
            int newCount= ((pushCounter.get().getCounter()+1)%500);
            int pointsWon = ClickCountUtils.checkWin(newCount);
            boolean win = !(pointsWon==0);
            int distanceToNextPrice = ClickCountUtils.distanceToNextPrice(newCount);
            pushCountRepository.setCountForPushCounter(newCount,1);
            points +=pointsWon;
            score.get().setPoints(points);
            score.get().setName(name);
            highScoreRepository.save(score.get());

            return new PlayerInfo(points, distanceToNextPrice, win, name, id);
        }else {
            return new PlayerInfo(points, -1, false, name, id);
        }

    }

     */
}
