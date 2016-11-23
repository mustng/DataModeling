package Prototypes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

public class fileMaker {
	

	
	public static void createFile(String info, String name){
		
		Charset utf8 = StandardCharsets.UTF_8;
		try {
		    
		    Files.write(Paths.get(name), info.getBytes(utf8));
	
		} catch (IOException e) {
		    e.printStackTrace();
		}
}
}
