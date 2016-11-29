package commaStrip;

public class strip {
	
	public static void main(String args[]){
		
		String query = "SELECT s.sname as Salesperson_Name, <+Customer, QUOTA ,> , FROM s;";
		query = clearComma(query);
		System.out.println(query);
		
	}

	public static String clearComma(String query){
		
		int pos = query.indexOf("FROM");
		for(int i = pos; i > 0; i--){
			if(query.charAt(i) == ',' && (query.charAt(i-1) == '>' || query.charAt(i-2) == '>')){
				
				int comma = i;
				
				String cleanQuery = query.substring(0, comma);
				cleanQuery += query.substring(comma+1);
				
				return cleanQuery;
				
			}
		}
		return query;
		
		
	}
}
