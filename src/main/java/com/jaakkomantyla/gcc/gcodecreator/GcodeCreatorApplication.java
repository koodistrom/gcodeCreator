package com.jaakkomantyla.gcc.gcodecreator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class GcodeCreatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcodeCreatorApplication.class, args);
		SVGHandler.parse();
	}

}
