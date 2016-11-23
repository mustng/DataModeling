import java.util.ArrayList;

public class Parse {
	
	
	public static void main(String args[]) {
		ArrayList<String> tableInfo = new ArrayList<String>();
		
		parser(tableInfo);
		System.out.println(tableInfo);
	}
	


	private static ArrayList<String> parser(ArrayList<String> tableInfo) {
		tableInfo.add("S , C"); 
		tableInfo.add("S . SNO");
		tableInfo.add("AS");
		tableInfo.add("C . ID");
		tableInfo.add("< + tagname");
		tableInfo.add("< tagname");
		tableInfo.add("C . SNAME");
		tableInfo.add("tagname >");
		tableInfo.add("tagname >");
		tableInfo.add("QUOTA");
		tableInfo.add("CITY");
		
		return tableInfo;
	}
}
