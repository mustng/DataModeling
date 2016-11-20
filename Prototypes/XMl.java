import java.util.ArrayList;

public class XMl {
	
	public static void main(String args[]) {
		String[] fakeData = {"1","dog", "item1", "2", "cat", "item2", "3", "bird", "item1", "4", "mice", "item2"}; 
		ArrayList<String> tableInfo = new ArrayList<String>();
		ArrayList<String> xmlLines = new ArrayList<String>();
		
		tableInfo.add("Animals"); 
		tableInfo.add("ID"); 
		tableInfo.add("AS");
		tableInfo.add("NUM");
//		tableInfo.add("<tagname");
		tableInfo.add("Species"); 
//		tableInfo.add("tagname>");
		tableInfo.add("Item");
		
		String printOut = "<?xml ve rsion = \"1.0\"?>\n<!DOCTYPE " + tableInfo.get(0) + " INFORMATION \"" + tableInfo.get(0) + ".dtd\">\n"; 
		
		xmlLines = xmlLines(xmlLines , tableInfo);
		
		System.out.println(xmlLines);
		
		int lengthReset = 2;
		printOut += xmlLines.get(0) + "\n";
		
		for (int i = 0; i < fakeData.length; i++){     // This is where the while loop is where sql query data  while( res.next()
			
			if (lengthReset == xmlLines.size()){
				printOut += xmlLines.get(1) + "\n";
				
				printOut += xmlLines.get(0) + "\n";
				lengthReset = 2;
				printOut += xmlLines.get(lengthReset) + fakeData[i] + xmlLines.get(lengthReset + 1) + "\n";
			}
//			else if (xmlLines.get(i).contains("@")){
//				System.out.println("hi");
//				printOut += xmlLines.get(lengthReset) + "\n";
//				i--;
//			}
			else{
				printOut += xmlLines.get(lengthReset) + fakeData[i] + xmlLines.get(lengthReset + 1) + "\n";
			}
			
			lengthReset +=2;
			
		}
		
		printOut += xmlLines.get(1) + "\n";
		
		System.out.println(printOut);
	}
	
	public static ArrayList<String> xmlLines (ArrayList<String> xmlLines, ArrayList<String> tableInfo){  //This creates the lines going into the code 
																										// example <ID   table="Animals" name="ID"> and </ID>
		int tagNameDepthCount = 0;
		
		for (int i = 0; i < tableInfo.size() ; i++){	
			
			if(i == 0){
				xmlLines.add("<" + tableInfo.get(0) + ">");
				xmlLines.add("</" + tableInfo.get(0) + ">");
			}
			else if(i != tableInfo.size() - 1 && tableInfo.get(i + 1).equals("AS")){
				xmlLines.add(tabMaker(tagNameDepthCount) + "<" + tableInfo.get(i + 2) + "   table=\"" + tableInfo.get(0) + "\" name=\"" + tableInfo.get(i) + "\">");
				xmlLines.add(tabMaker(tagNameDepthCount) + "</" + tableInfo.get(i + 2) + ">");
				i+=2;
			}
			else if(tableInfo.get(i).contains("<")){
				xmlLines.add(tabMaker(tagNameDepthCount) + tableInfo.get(i) + ">");
				tagNameDepthCount++;
			}
			else if(tableInfo.get(i).contains(">")){
				tagNameDepthCount--;
				xmlLines.add(tabMaker(tagNameDepthCount) + "</" + tableInfo.get(i));
			}
			else{
				xmlLines.add(tabMaker(tagNameDepthCount) + "<" + tableInfo.get(i) + "   table=\"" + tableInfo.get(0) + "\" name=\"" + tableInfo.get(i) + "\">");
				xmlLines.add(tabMaker(tagNameDepthCount) + "</" + tableInfo.get(i) + ">");
			}
			
		}
		return xmlLines;
	}
	
	public static String tabMaker(int tagNameDepthCount){
		String space = "";
		for(int i = 0; i < tagNameDepthCount; i++){
			space += "\t";
		}
		
		return space;
	}
}
