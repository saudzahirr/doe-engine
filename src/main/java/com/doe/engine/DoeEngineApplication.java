package com.doe.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.commons.math3.linear.RealMatrix;
import com.doe.algorithms.BoxBehnkenDOE;

@SpringBootApplication
public class DoeEngineApplication {

	public static void main(String[] args) {
        // Generate a Box-Behnken design with 4 factors
        RealMatrix design = BoxBehnkenDOE.boxBehnkenDesign(4);
        System.out.println("Generated design matrix: " + design);

		SpringApplication.run(DoeEngineApplication.class, args);
    }

}
