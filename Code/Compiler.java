//COP 4710
//Final Project

import java.util.ArrayList;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.lang.String;
//import Language.*;//16
import java.util.ArrayList;
//import System.*;//16

public class Compiler
{
	private static ArrayList<String> attributes = new ArrayList<String>();
	private static ArrayList<String> values = new ArrayList<String>();
	private static ArrayList<String> attributeTypes = new ArrayList<String>();
	private static ArrayList<String> firstOperand = new ArrayList<String>();
	private static ArrayList<String> secondOperand = new ArrayList<String>();
	private static ArrayList<String> actions = new ArrayList<String>();
    //private static DatabaseManager databaseManager = new DatabaseManager("Databases/");//16
    //private static Database database = null;//16
    //private static DatabaseDataDefinitionManager databaseDataDefinitionManager = null;//16
    //private static DatabaseDataManipulationManager databaseDataManipulationManager = null;//16
    //private static DatabaseDataWManipulationManager databaseWDataManipulationManager = null;//16

	private static final boolean DEBUG = true;
	private static final boolean REPORT_ERROR = true;

	private static ArrayList<String> originalList = new ArrayList<String>();
	private static ArrayList<String> tableNames = new ArrayList<String>();
	private static ArrayList<String> finalList = new ArrayList<String>();
	private static ArrayList<String> originalAttributeNames = new ArrayList<String>();
	private static ArrayList<String> newAttributeNames = new ArrayList<String>();
	private static ArrayList<String> tagNames = new ArrayList<String>();
	private static ArrayList<String> taggedAttributes = new ArrayList<String>();
	private static String lineEntered  = "";
	private static String tablesEntered  = "";
	private static boolean stop = false;
	private static boolean dotSpacer = true;
	private static boolean correctSql = true;
	private static ArrayList<Token> tokenList = new ArrayList<Token>();                              //The token list for parsing and semantics
	private static String lineInput = "";                                                            //Stores the queries for scanning, parsing, and semantics
	private static ArrayList<String> operator = new ArrayList<String>();                             //Used for the compare method
	private static ArrayList<String> keyWord = new ArrayList<String>();                              //Create reference array to compare tokens
	private static ArrayList<String> tokens = new ArrayList<String>();                               //Create reference array to compare tokens
	private static ArrayList<String> systemLevel = new ArrayList<String>();                          //Create reference array list for first of system level commands
	private static boolean tableFlag = false;                                                        //flag for creating tables and checks for parsing and/or semantics
	private static boolean numFlag = false;                                                          //flag for scanner logic
	private static boolean charFlag = false;                                                         //flag for scanner logic
	private static  String ID = "";                                                                  //variable for scanner
	private static String num = "";                                                                  //variable for scanner
	private static int index = 0;                                                                    //variable to keep track of the current token in the tokenList for Parsing
	private static Token currToken;                                                                  //variable to keep track of the current token for Parsing
	private static boolean errorFlag = false;                                                        //Flag to check for errors inside of the parse
	private static boolean createTabFlag = false;                                                    //Flag to check for a create table command inside the parse
	private static String tableNameG  = "";
	private static String attributeNameG = "";
	private static String valueNameG = "";
	private static int tempDistinct = 0;

	private static long total = 0;




   static ArrayList<String> compiler(ArrayList<String> finalList)


    {

        //databaseManager.init();//16



        int i = 0;

        /*Add the Keywords, Symbols or tokens in the langauge to the appropriate Array Lists*/

        keyWord.add("CREATE");
        keyWord.add("DATE");
        keyWord.add("SELECT");
        keyWord.add("DROP");
        keyWord.add("SAVE");
        keyWord.add("TABLE");
        keyWord.add("COMMIT");
        keyWord.add("LOAD");
        keyWord.add("INSERT");
        keyWord.add("INTO");
        keyWord.add("DELETE");
        keyWord.add("FROM");
        keyWord.add("UPDATE");
        keyWord.add("WUPDATE");
        keyWord.add("WSELECT");
        keyWord.add("VALUES");
        keyWord.add("WHERE");
        keyWord.add("SET");
        keyWord.add("TABLE");
        keyWord.add("DATABASE");
        keyWord.add("NOT");
        keyWord.add("NULL");
        keyWord.add("RENAME");
        keyWord.add("CHARACTER");
        keyWord.add("NUMBER");
        keyWord.add("INTEGER");
        keyWord.add("AS");

        operator.add("<>");
        operator.add(">");
        operator.add("<");
        operator.add("=");
        operator.add("<=");
        operator.add(">=");

        systemLevel.add("SAVE");
        systemLevel.add("COMMIT");

        tokens.add("/");
        tokens.add("*");
        tokens.add("+");
        tokens.add("-");
        tokens.add("*");
        tokens.add("<");
        tokens.add("=");
        tokens.add(">");
        tokens.add("<");
        tokens.add(",");
        tokens.add(";");
        tokens.add("(");
        tokens.add(")");
        tokens.add(":");
        tokens.add("'");
        tokens.add(".");

        
        /*End of adding the Keywords, Symbols or Tokens in the langauge*/

        boolean value = true;                                                               //Flag to execute a user interactive menu

        while (value == true && stop == false)
        {
            value = menu();
//            reset();//16
            debug("");
        }
        
        return tableNames;
    }
   
	public static void resetTableName() {
		tableNames.clear();
		stop = false;
	}
    public static boolean menu()
    {
        Scanner input = new Scanner(System.in);
        String check = "";
        System.out.print("SQL > ");
        while (input.hasNextLine())
        {
            lineInput += input.nextLine() + "\n";
            check = lineInput;
            if (lineInput.indexOf(";") != -1 || lineInput.indexOf("exit") != -1 || lineInput.indexOf("EXIT") != -1)
                break;
        }

        if (lineInput.indexOf("exit") != -1 || lineInput.indexOf("EXIT") != -1)
        {
	    debug("");
            debug("Exiting...\n");
            System.exit(0);


            return false;
        }

        else
        {
            scan();
            parse();

          
            return true;
        }
    }

    public static void reset()
    {
        ID = "";
        num = "";
        errorFlag = false;
        createTabFlag = false;
        lineInput = "";
        tokenList.clear();
        currToken.tok = "";
        currToken.type = "";
        index = 0;
        tempDistinct = 0;
        originalList.clear();
        correctSql = true;
        tableNames.clear();
        //newAttributeNames.clear();
        tagNames.clear();
        //taggedAttributes.clear();
//        finalList.clear();
        lineEntered = "";
        tablesEntered = "";
        dotSpacer = true;
    }

    public static void scan()
    {
        int i = 0;
        while (i < lineInput.length())                                                                       //While loop for reading character by character to determine Identifiers, Keywords, symbols, and strip delimiters
        {
            char a = lineInput.charAt(i);
            String token = "";
            token += a;




            if (Character.isUpperCase(a) || Character.isLowerCase(a) || a == '_' || (Character.isDigit(a) && Character.isLetter(lineInput.charAt(i-1))) || (Character.isDigit(a) && lineInput.charAt(i-1) == '_'))                                       //Check if the character is a lowercase or uppercase letter
            {


                if(ID.length() >= 1){
                    if(Character.isLetter(ID.charAt(0))){
                        //firstIsLetter = false;
                        ID += a;
                        charFlag = true;
                    }
                    else{
                        charFlag = false;
                    }
                }else{
                    ID += a;
                    charFlag = true;
                }


            }

            else if (Character.isDigit(a))                                                                   //Check for a decimal inside a number or is a number
            {
                num += a;
                numFlag = true;
            }


            /*
            else if (lineInput.charAt(i) == '.'){
                String temp = "";
                Token theToken = new Token("SYMBOL", temp);
                tokenList.add(theToken);
            }
            */

            else if (tokens.contains(token))
            {
                String temp = "";
                if ((lineInput.charAt(i + 1) == '=' && (a == '<' || a == '>' || a == '=')) || (a == '<' && lineInput.charAt(i + 1) == '>'))             //Check for two character comparisons
                {
                    temp += a;
                    a = lineInput.charAt(++i);                                                                  //store the token as a string
                    temp += a;
                }



                else
                    temp += a;                                                                                  //Cast to string and store in the array list

                Token theToken = new Token("SYMBOL", temp);
                tokenList.add(theToken);
                //originalList.add(temp);//20
            }

            else if (a == '\n' || a == ' ' || a == '\t' || (i == lineInput.length() - 1))
            {
                String tem = ID;
                if (!Objects.equals(ID, ""))                                                                     //We have a reached a delimiter so check if ID has a value
                {
                    if (keyWord.contains(tem.toUpperCase()))
                    {
                        Token theToken = new Token("KW", ID.toUpperCase());
                        tokenList.add(theToken);                                                                 //If it is not a keyword it is an identifier
                        ID = "";
                    }

                    else
                    {
                        Token theToken = new Token("ID", ID);
                        tokenList.add(theToken);
                    }

                }

                else if (!Objects.equals(num, ""))
                {
                    Token theToken = new Token("NUM", num);
                    tokenList.add(theToken);
                    numFlag = false;
                    num = "";
                }

            }

            else
            {
                debug("Invalid character!");
                break;
            }



            if (charFlag == true && !Character.isLetterOrDigit(lineInput.charAt(i + 1)) && !Objects.equals(lineInput.charAt(i+1), '_'))
            {
                String temp = ID;
                if (keyWord.contains(temp.toUpperCase()))
                {
                    Token theToken = new Token("KW", ID.toUpperCase());
                    tokenList.add(theToken);                                        //If it is not a keyword it is an identifier
                    charFlag = false;
                    ID = "";
                }

                else if (!Objects.equals(ID, ""))
                {
                    Token theToken = new Token("ID", ID);
                    tokenList.add(theToken);
                    ID = "";
                    charFlag = false;
                }

            }
            else if (numFlag == true && !Character.isDigit(lineInput.charAt(i + 1)))
            {
                Token theToken = new Token("NUM", num);
                tokenList.add(theToken);
                num = "";
                numFlag = false;
            }
            i++;                                                                                                        //Increment i to the next position of the string


        }


    }

    public static void failed(String expected, String current){
        debug("");
        debug(" [ERROR] EXPECTED THE TOKEN OR TOKEN TYPE: " + expected);
        debug("USER INPUT: " + current);
        errorFlag = true;
    }

    public static void sqlStatement(String singleData){
        //originalList.add(tokenList.get(index).tok);
        originalList.add(singleData);
    }

    public static void sqlLineEntered(String wholeData){
        if(dotSpacer == false){
            lineEntered += wholeData;
        }else {
            lineEntered += " "+wholeData;
        }
    }
    
    public static void sqlTableNames(String tableData){        
    	tablesEntered += tableData;    
    }

    public static void CheckToken(String expected, String current)                                                      //Function to check whether the current token is correct
    {
        if (Objects.equals(expected, current))
        {
            //debug("ACCEPTED: " + current);
            if(correctSql == true) {
                sqlLineEntered(tokenList.get(index).tok);
            }

            index++;
            currToken = tokenList.get(index);
            System.out.println(current);


        }

        else if (errorFlag == false && (!Objects.equals(currToken.tok, ";") || createTabFlag == true))
        {
	         debug("");
            debug(" [ERROR] EXPECTED THE TOKEN OR TOKEN TYPE: " + expected);
            debug("USER INPUT: " + current);
            errorFlag = true;
        }
    }

    public static void parse()
    {
        currToken = tokenList.get(index);
        if (Objects.equals(currToken.tok, ";"))
        {
            debug("COMMAND MUST COME BEFORE A SEMICOLON!!!");
        }
        else
            command();

        if (Objects.equals(currToken.tok, ";") && errorFlag == false && !Objects.equals(tokenList.get(0).tok, ";") && (index == tokenList.size()-1))                                                               //If we finish all the parsing checks then accept on semi colon
        {
            sqlLineEntered(";");
 	        debug("");
            debug("ACCEPT");
            index = 0;
            //System.out.print(originalList);

            tableNames.addAll(originalList);
            tableNames.add(0, lineEntered);
            tableNames.add(1, tablesEntered);
            System.out.println(tableNames);
            //for(String thing : originalList)
                //System.out.print(thing);
            //semantics();
        }

        else                                                                                                            //Else reject
        {
	        debug("");
            debug(" [ERROR] COMMAND IS NOT PARSABLE");
        }
    }

    public static void command()
    {
        if (systemLevel.contains(currToken.tok) || Objects.equals(tokenList.get(index+1).tok, "DATABASE")) {
            systemCommand();
        }

        else if (Objects.equals(currToken.tok, "CREATE") || Objects.equals(currToken.tok, "DROP"))
        {
            ddlCommand();
        }

        else
        {
            dmlCommand();
        }
    }

    public static void systemCommand()
    {
        if (Objects.equals(currToken.tok, "CREATE"))
        {
            CheckToken("KW", currToken.type);
            createDatabase();
        }

        else if (Objects.equals(currToken.tok, "DROP"))
        {
            CheckToken("KW", currToken.type);
            dropDatabase();
        }

        else if (Objects.equals(currToken.tok, "SAVE")) {
            save();
        }

        else if (Objects.equals(currToken.tok, "COMMIT"))
        {
            commit();
        }

        else if (Objects.equals(currToken.tok, "LOAD"))
        {
            loadDatabase();
        }
    }

    public static void ddlCommand()
    {
        if (Objects.equals(currToken.tok, "CREATE") && Objects.equals(tokenList.get(index+1).tok, "TABLE"))
        {

            CheckToken("KW", currToken.type);
            createTable();
        }

        else if (Objects.equals(currToken.tok, "DROP") && Objects.equals(tokenList.get(index+1).tok, "TABLE"))
        {
            CheckToken("KW", currToken.type);
            dropTable();
        }
    }

    public static void dmlCommand()
    {
        if (Objects.equals(currToken.tok, "INSERT"))
        {
            CheckToken("INSERT", currToken.tok);
            insertCommand();
        }

        else if (Objects.equals(currToken.tok, "DELETE"))
        {
            CheckToken("DELETE", currToken.tok);
            deleteCommand();
        }

        else if (Objects.equals(currToken.tok, "UPDATE"))
        {
            CheckToken("UPDATE", currToken.tok);
            updateCommand();
        }

        else if (Objects.equals(currToken.tok, "WUPDATE"))
        {
            CheckToken("WUPDATE", currToken.tok);
            wUpdateCommand();
        }

        else if (Objects.equals(currToken.tok, "SELECT"))
        {
            //correctSql = false;
            CheckToken("SELECT", currToken.tok);

            //int next = index + 1;
            selectCommand();
            /*
            if(Objects.equals(currToken.tok, "*")) {
                selectCommand();
            }else if(Objects.equals(currToken.type, "ID")){
                selectCommandNotAll();
            }
            */
        }

        else if (Objects.equals(currToken.tok, "WSELECT"))
        {
            CheckToken("WSELECT", currToken.tok);
            wSelectCommand();
        }
    }

    public static void createDatabase()
    {
        CheckToken("DATABASE", currToken.tok);                                               //WE must see the KW DATABASE
        CheckToken("ID", currToken.type);                                                    //We must see an ID for this command
    }

    public static void dropDatabase()
    {
        CheckToken("DATABASE", currToken.tok);                                               //WE must see the KW DATABASE
        CheckToken("ID", currToken.type);                                                    //We must see an ID for this command
    }

    public static void save()
    {
        CheckToken("SAVE", currToken.tok);
    }

    public static void commit()
    {
        CheckToken("COMMIT", currToken.tok);
    }

    public static void loadDatabase()
    {
        CheckToken("LOAD", currToken.tok);
        CheckToken("DATABASE", currToken.tok);
        CheckToken("ID", currToken.type);
    }

    public static void createTable()
    {
        createTabFlag = true;
        CheckToken("TABLE", currToken.tok);                                                      //WE must see the KW TABLE
        CheckToken("ID", currToken.type);                                                     //We must see a token type of table for this command
        CheckToken("(", currToken.tok);
        fieldDefList();
        CheckToken(")", currToken.tok);
    }

    public static void dropTable()
    {
        CheckToken("TABLE", currToken.tok);                                                      //WE must see the KW TABLE
        CheckToken("ID", currToken.type);                                                     //We must see a token type of table for this command
    }

    public static void insertCommand()
    {
        //We already saw the word insert to get here and accepted it
        CheckToken("INTO", currToken.tok);
        CheckToken("ID", currToken.type);
        insertCommand2();
    }

    public static void insertCommand2()
    {
        //Continue from the original insert command
        if (Objects.equals(tokenList.get(index).tok, "("))
        {
            CheckToken("(", currToken.tok);                                              //We accept the left paren
            literal();                                                                   //Call fieldList for more checks
            CheckToken(")", currToken.tok);                                              //We are out of the function call and we must see a right paren
            CheckToken("VALUES", currToken.tok);                                         //Must see Keyword values
            CheckToken("(", currToken.tok);                                              //Must see a left paren
            fieldList();                                                                 //Call Literal to check parse
            CheckToken(")", currToken.tok);                                              //Must see a right paren
        }

        else
        {
            CheckToken("VALUES", currToken.tok);                                         //We must see keyword values
            CheckToken("(", currToken.tok);                                              //We must see a right paren
            fieldList();                                                               //Call Literal List for checks
            CheckToken(")", currToken.tok);                                              //Must see a right paren
        }
    }

    public static void deleteCommand()
    {
        //We already have seen delete and accepted
        CheckToken("FROM", currToken.tok);
        CheckToken("ID", currToken.type);
        deleteCommand2();
    }

    public static void deleteCommand2()
    {
        //Continue with the current delete command
       if (Objects.equals(currToken.tok, "WHERE"))
       {
           CheckToken("WHERE", currToken.tok);
           condition();
       }
    }

    public static void updateCommand()
    {
        //We have already seen update
        CheckToken("ID", currToken.type);
        CheckToken("SET", currToken.tok);
        updateCommand2();
    }

    public static void updateCommand2()
    {
        //Continue on the update command
        CheckToken("ID", currToken.type);
        CheckToken("=", currToken.tok);
        expression();                                                                   //expression
        if (Objects.equals(currToken.tok, ","))
        {
            if((tokenList.get(index+1).tok != "FROM")|| (tokenList.get(index+1).tok != ">")){
                lineEntered += " "+",";
                //correctSql = false;
            }
            correctSql = false;

            CheckToken(",", currToken.tok);
            correctSql = true;
            updateCommand2();
        }
        else
        {
            updateCommand3();
        }
    }

    public static void updateCommand3()
    {
        //if we see a WHERE condition accept and call condition
        if (Objects.equals(currToken.tok, "WHERE"))
        {
            CheckToken("WHERE", currToken.tok);
            condition();
        }
    }

    public static void wUpdateCommand()
    {
        //We have already seen wUpdate
        CheckToken("ID", currToken.type);
        CheckToken("SET", currToken.tok);
        wUpdateCommand2();
    }

    public static void wUpdateCommand2()
    {
        //we have seen wUpdate command, now check for the following tokens
        if (Objects.equals(currToken.tok, "DATE"))
        {
            CheckToken("DATE", currToken.tok);
            date_time();
        }
        CheckToken("ID", currToken.type);
        CheckToken("=", currToken.tok);
        expression();
        if (Objects.equals(currToken.tok, ","))
        {
            //CheckToken(",", currToken.tok);
            wUpdateCommand2();
        }
        else
        {
            wUpdateCommand3();
        }
    }

    public static void wUpdateCommand3()
    {
        //if we see a WHERE condition accept and call condition
        if (Objects.equals(currToken.tok, "WHERE"))
        {
            CheckToken("WHERE", currToken.tok);
            condition();
        }
    }

    public static void selectCommand()
    {
        //We have seen select and now we accept *

        //CheckToken("*", currToken.tok);
        //CheckToken("FROM", currToken.tok);
        //CheckToken("ID", currToken.type);


        if (Objects.equals(currToken.tok, "*"))
        {
            sqlStatement(tokenList.get(index).tok);
            CheckToken("*", currToken.tok);
            if(Objects.equals(currToken.tok, "FROM")) {
                CheckToken("FROM", currToken.tok);
                fromKeyWord();
            }
            //CheckToken("FROM", currToken.tok);
            //CheckToken("ID", currToken.type);
            wSelectCommand3();
        }
        else if(Objects.equals(currToken.tok, "DISTINCT") && tempDistinct == 0){
            CheckToken("DISTINCT", currToken.tok);
            if(Objects.equals(currToken.type, "ID")){
                idFound();
                //CheckToken("ID", currToken.type);
                fieldList();
            }
            else {
                System.out.print("Error #12a: there must be an attribute(s) after the word DISTINCT");
                failed("The right token", currToken.tok);
            }

            tempDistinct++;
        }

        else if(Objects.equals(currToken.type, "ID"))
        {

            idFound();
            //CheckToken("ID", currToken.type);
            fieldList();
            //CheckToken("FROM", currToken.tok);
            //CheckToken("ID", currToken.type);
            //wSelectCommand3();
        }

    }
    public static void idFound(){
        if(Objects.equals(currToken.type, "ID"))
        {

            if(Objects.equals(tokenList.get(index+1).tok, ".")) {
                sqlStatement(tokenList.get(index).tok+" "+tokenList.get(index+1).tok+" "+tokenList.get(index+2).tok);
            }
            else if(!Objects.equals(tokenList.get(index+1).tok, ".") && !Objects.equals(tokenList.get(index-1).tok, ".")) {
                sqlStatement(tokenList.get(index).tok);
            }
            CheckToken("ID", currToken.type);
            //fieldList();
            //CheckToken("FROM", currToken.tok);
            //CheckToken("ID", currToken.type);
            //wSelectCommand3();
        }
    }

    public static void fromKeyWord(){
        //if(Objects.equals(currToken.tok, "FROM"))
        //{
            //CheckToken("FROM", currToken.tok);
            if(Objects.equals(currToken.type, "ID")){
//                tableNames.add(tokenList.get(index).tok);
                sqlTableNames(tokenList.get(index).tok);
                CheckToken("ID", currToken.type);
                if(Objects.equals(currToken.tok, "WHERE")){
                    wSelectCommand3();
                }
                else if(Objects.equals(currToken.tok, ","))
                {
                	sqlTableNames(",");

                    if((tokenList.get(index+1).tok != "FROM")|| (tokenList.get(index+1).tok != ">")){
                        lineEntered += " "+",";
                        //correctSql = false;
                    }
                    correctSql = false;

                    CheckToken(",", currToken.tok);
                    correctSql = true;
                    fromKeyWord();
                }
                else if(Objects.equals(currToken.tok, ";"))
                {

                }
                else
                    System.out.print("missing a WHERE clause, more tables or a semicolon");
            }
            else
                failed("ID", currToken.type);
        //}
    }

    public static void selectCommandNotAll()
    {
        //We have seen select and now we accept ID

        CheckToken("ID", currToken.type);
        CheckToken("FROM", currToken.tok);
        CheckToken("ID", currToken.type);

    }

    public static void wSelectCommand()
    {
        //We have already accepted wSelect, call command2 for the rest of the tokens
        wSelectCommand2();
    }


    public static void wSelectCommand2()
    {
        //We have seen wSelect and now we must see a * or (
        if (Objects.equals(currToken.tok, "*"))
        {
            CheckToken("*", currToken.tok);
            CheckToken("FROM", currToken.tok);
            CheckToken("ID", currToken.type);
            wSelectCommand3();
        }

        else
        {
            CheckToken("(", currToken.tok);
            fieldList();
            CheckToken(")", currToken.tok);
            CheckToken("FROM", currToken.tok);
            CheckToken("ID", currToken.type);
            wSelectCommand3();
        }
    }


    public static void wSelectCommand3()
    {
        //If we see keyword where then accept and call condition
        if (Objects.equals(currToken.tok, "WHERE"))
        {
            CheckToken("WHERE", currToken.tok);
            if(Objects.equals(currToken.type, "ID")) {
                CheckToken("ID", currToken.type);
                //idFound();
                expression();
            }
            else
                System.out.print("Error #9a: There must be an attribute after the WHERE key word!");

        } else if (Objects.equals(currToken.tok, ";")){

        }
        else{
            failed("WHERE", currToken.tok);
        }
        //If not fall out the bottom
    }

    public static void fieldDefList()
    {
        //Check for ID then the type of ID

        CheckToken("ID", currToken.type);

        fieldType();
        if (Objects.equals(currToken.tok, ","))
        {
            //correctSql = true;
            //CheckToken(",", currToken.tok);
            fieldDefList();
        }
    }

    public static void fieldType()
    {
        //Checks for the type of attribute created
        if (Objects.equals(currToken.tok, "NUMBER"))
        {
            CheckToken("NUMBER", currToken.tok);
            if (Objects.equals(currToken.tok, "("))
            {
                CheckToken("(", currToken.tok);
                CheckToken("NUM", currToken.type);
                if (Objects.equals(currToken.tok, ","))
                {
                    //CheckToken(",", currToken.tok);
                    CheckToken("NUM", currToken.type);
                }
                CheckToken(")", currToken.tok);

            }
        }

        else if (Objects.equals(currToken.tok, "INTEGER"))
        {
            CheckToken("INTEGER", currToken.tok);
            if (Objects.equals(currToken.tok, "("))
            {
                CheckToken("(", currToken.tok);
                CheckToken("NUM", currToken.type);
                CheckToken(")", currToken.tok);
            }
        }

        else if (Objects.equals(currToken.tok, "CHARACTER"))
        {
            CheckToken("CHARACTER", currToken.tok);
            CheckToken("(", currToken.tok);
            CheckToken("NUM", currToken.type);
            CheckToken(")", currToken.tok);

        }

        else if (Objects.equals(currToken.tok, "DATE"))
        {
            CheckToken("DATE", currToken.tok);
            //date_time();
        }

        if (Objects.equals(currToken.tok, "NOT"))
        {
            CheckToken("NOT", currToken.tok);
            CheckToken("NULL", currToken.tok);
        }
    }

    public static void dotAttribute(){
        //String tempString = "";
        if (Objects.equals(currToken.tok, "."))
        {
            //tempString = tokenList.get(index-1).tok+" "+tokenList.get(index).tok+" "+tokenList.get(index+1).tok;
            //sqlStatement(tokenList.get(index-1).tok+" "+tokenList.get(index).tok+" "+tokenList.get(index+1).tok);
            dotSpacer = false;
            CheckToken(".", currToken.tok);
            if(Objects.equals(currToken.type, "ID")){
                CheckToken("ID", currToken.type);
                dotSpacer = true;
                if(Objects.equals(currToken.tok, "."))
                    failed(".", currToken.tok);
            }
            else {
                failed("ID", currToken.type);
            }
        }
    }

    public static void fieldList()
    {
        //CheckToken(",", currToken.tok);
        if (Objects.equals(currToken.tok, "AS"))
        {
            sqlStatement(tokenList.get(index).tok);
            asStatment();
        }
        else if (Objects.equals(currToken.tok, ","))
        {


        	//correctSql = true;
            int tempNum = index + 1;
            if((Objects.equals((tokenList.get(tempNum).tok), "<"))){
                //lineEntered += " "+",";
                correctSql = false;
                System.out.println("yaraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab!");
            }

            CheckToken(",", currToken.tok);
            correctSql = true;
            if(Objects.equals(currToken.type, "ID"))
            {
                idFound();
                //CheckToken("ID", currToken.type);
                fieldList();
            }
            else if(Objects.equals(currToken.tok, "<")){
                definitionThree();
            }
            else {
                System.out.print("Error #14a: missing an attribute or an opening angle bracket!");
                failed("The right token", currToken.tok);
            }
        }
        else if(Objects.equals(currToken.tok, "FROM")){
            CheckToken("FROM", currToken.tok);
            fromKeyWord();
        }
        else if(Objects.equals(currToken.tok, ".")){

            dotAttribute();
            fieldList();
        }
        else{
            System.out.print("Error #6a: missing a FROM, a comma, a dot or an AS");
            failed("The right token", currToken.tok);
        }
        /*
        else if (Objects.equals(currToken.tok, "'"))
        {
            CheckToken("'", currToken.tok);                                                 //Accept ' and do more checks
            if (Objects.equals(currToken.type, "NUM"))
            {
                CheckToken("NUM", currToken.type);
                if (Objects.equals(currToken.tok, "/"))                                                    //A Character insert for date
                {
                    CheckToken("/", currToken.tok);
                    CheckToken("NUM", currToken.type);
                    CheckToken("/", currToken.tok);
                    CheckToken("NUM", currToken.type);
                }
                else if (Objects.equals(currToken.tok, "-"))                                               //A Character insert for a phone number
                {
                    CheckToken("-", currToken.tok);
                    CheckToken("NUM", currToken.type);
                    CheckToken("-", currToken.tok);
                    CheckToken("NUM", currToken.type);
                }
                else if (Objects.equals(currToken.type, "ID"))
                {
                    CheckToken("ID", currToken.type);
                    while (Objects.equals(currToken.type,"ID") || Objects.equals(currToken.type, "NUM"))
                    {
                        if (Objects.equals(currToken.type, "ID"))
                            CheckToken("ID", currToken.type);
                        else
                            CheckToken("NUM", currToken.type);
                    }
                }
            }

            else if (Objects.equals(currToken.type, "ID"))
            {
                CheckToken("ID", currToken.type);
                while (Objects.equals(currToken.type, "ID") || Objects.equals(currToken.type, "NUM"))
                {
                    if (Objects.equals(currToken.type, "ID"))
                        CheckToken("ID", currToken.type);
                    else
                        CheckToken("NUM", currToken.type);
                }
            }
            CheckToken("'", currToken.tok);
        }
        */

    }

    public static void definitionThree_fieldList()
    {
        if (Objects.equals(currToken.tok, "AS"))
        {
            sqlStatement(tokenList.get(index).tok);
            definitionThree_asStatment();
        }
        else if (Objects.equals(currToken.tok, ","))
        {


//            if((Objects.equals((tokenList.get(index+1).tok), "FROM"))){
//                correctSql = false;
//            }else{
//                correctSql = true;
//            }
            int tempNum = index + 1;
            if((Objects.equals((tokenList.get(tempNum).tok), "FROM"))|| (Objects.equals((tokenList.get(tempNum).tok), ">"))){
                //lineEntered += " "+",";
                correctSql = false;
                System.out.println("yaraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab!");
            }

            CheckToken(",", currToken.tok);
            correctSql = true;

            if(Objects.equals(currToken.type, "ID"))
            {
                /*
                if((tokenList.get(index-1).tok != ".")||(tokenList.get(index+1).tok != ".")){
                    if((tokenList.get(index+1).tok == ",") && (tokenList.get(index+2).tok == ">")){
                        sqlStatement(tokenList.get(index).tok+" "+tokenList.get(index+2).tok);
                    }else {
                        sqlStatement(tokenList.get(index).tok);
                    }
                }
                */
                idFound();
                //CheckToken("ID", currToken.type);
                definitionThree_fieldList();
            }
            else if(Objects.equals(currToken.tok, "<")){
                definitionThree();
            }
            else if(Objects.equals(currToken.tok, ">"))
                definitionThree_closing();
        }
        else if(Objects.equals(currToken.tok, "."))
        {
            dotAttribute();
            definitionThree_fieldList();
        }
        else{
            System.out.print("Error # 6a: missing a FROM, a comma, a dot or an AS");
        }
    }

    public static void closingTagNames(){
        //int ii = 0;
        String tempi = "";
        for (int ii = 0;tagNames.size()> ii; ii++){
            tempi = tagNames.get(tagNames.size()-1);
            sqlStatement(tempi+" "+">");

            tagNames.remove(tagNames.size()-1);
            //ii++;
        }
    }

    public static void definitionThree_closing(){
        if(Objects.equals(currToken.tok, ">")){
            closingTagNames();
            correctSql = false;
            CheckToken(">", currToken.tok);
            if(Objects.equals(currToken.tok, ",")){
//                if((Objects.equals((tokenList.get(index+1).tok), "FROM"))){
//                    correctSql = false;
//                }else{
//                    correctSql = true;
//                }
                int tempNum = index + 1;
                if((Objects.equals((tokenList.get(tempNum).tok), "FROM"))|| (Objects.equals((tokenList.get(tempNum).tok), ">"))){
                    //lineEntered += " "+",";
                    correctSql = false;
                    System.out.println("yaraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab!");
                }

                CheckToken(",", currToken.tok);
                correctSql = true;
                if(Objects.equals(currToken.tok, "FROM")){
                    correctSql = true;
                    CheckToken("FROM", currToken.tok);
                    fromKeyWord();
                }
                else if(Objects.equals(currToken.tok, ">")){
                    definitionThree_closing();
                }
                else {
                    System.out.print("Error #13a: missing a FROM keyword or a closed angle bracket!");
                    failed("The right token", currToken.tok);
                }
            }
            else
                failed(",", currToken.tok);
        }
        else
            System.out.print("Error #12a: missing either an attribute, an open angle bracket, or an closed angle bracket! ");
    }

    public static void definitionThree(){
        if(Objects.equals(currToken.tok, "<")){
            correctSql = false;

            CheckToken("<", currToken.tok);
            if(Objects.equals(currToken.type, "ID")){
                correctSql = true;
                sqlStatement(tokenList.get(index-1).tok+" "+tokenList.get(index).tok);//TagName
                tagNames.add(tokenList.get(index).tok);
                definitionThree_tag();
            }
            else if(Objects.equals(currToken.tok, "+")){
                sqlStatement(tokenList.get(index-1).tok+" "+tokenList.get(index).tok+""+tokenList.get(index+1).tok);//CompName
                CheckToken("+", currToken.tok);
                tagNames.add(tokenList.get(index).tok);
                definitionThree_tag();
            }
            else
            {
                System.out.print("Error #15a: missing the Compression Name or the Compression sign!");
                failed("The right token", currToken.tok);
            }
        }
        else {
            failed("<", currToken.tok);
        }
    }

    public static void definitionThree_tag(){
        if(Objects.equals(currToken.type, "ID")){
            //TaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaagNames!
        	correctSql = false;
            CheckToken("ID", currToken.type);
            if(Objects.equals(currToken.tok, ",")){
                if((tokenList.get(index+1).tok != "FROM")|| (tokenList.get(index+1).tok != ">")){
                    lineEntered += " "+",";
                    //correctSql = false;
                }
                correctSql = false;
                CheckToken(",", currToken.tok);
                correctSql = true;
                if(Objects.equals(currToken.type, "ID")){
                    idFound();
                    //CheckToken("ID", currToken.type);
                    definitionThree_fieldList();
                }
                else
                {
                    failed("ID", currToken.type);
                }
            }
            else
            {
                failed(",", currToken.tok);
            }
        }
        else
        {
            failed("ID", currToken.type);
        }
    }

    public static void asStatment(){
        if (Objects.equals(currToken.tok, "AS"))
        {
            CheckToken("AS", currToken.tok);
            if(Objects.equals(currToken.type, "ID")){
                idFound();
                //CheckToken("ID", currToken.type);
                if(Objects.equals(currToken.tok, ",") || Objects.equals(currToken.tok, "FROM"))
                    fieldList();
                else
                    failed(",", currToken.tok);
            }
            else
                failed("ID", currToken.type);
        }
    }

    public static void definitionThree_asStatment(){
        if (Objects.equals(currToken.tok, "AS"))
        {
            CheckToken("AS", currToken.tok);
            if(Objects.equals(currToken.type, "ID")){
                idFound();
                //CheckToken("ID", currToken.type);
                if(Objects.equals(currToken.tok, ",")|| Objects.equals(currToken.tok, "FROM"))
                    definitionThree_fieldList();
                else
                    failed(",", currToken.tok);
            }
            else
                failed("ID", currToken.type);
        }
    }



    public static void literal()
    {
        //Must see an ID or a NUM
        if (Objects.equals(currToken.type, "ID"))
        {
            //Must be ID or NUM
            CheckToken("ID", currToken.type);
        }
        if (Objects.equals(currToken.tok, ","))
        {
            //CheckToken(",", currToken.tok);
            literal();
        }
    }

    public static void literal2()
    {
        //Must see an ID or a NUM
        if (Objects.equals(currToken.type, "ID"))
        {
            //Must be ID
            CheckToken("ID", currToken.type);
        }

        else
        {
            //Must see a Number
            CheckToken("NUM", currToken.type);
        }
    }

    public static void date_time()
    {
        //Call date then time
        date();
        time();
    }

    public static void date()
    {
        //We must see these tokens in date
        CheckToken("(", currToken.tok);
        CheckToken("NUM", currToken.type);
        CheckToken("/", currToken.tok);
        CheckToken("NUM", currToken.type);
        CheckToken("/", currToken.tok);
        CheckToken("NUM", currToken.type);
        CheckToken(")", currToken.tok);
    }

    public static void time()
    {
        //We must see these tokens in time
        CheckToken(":", currToken.tok);
        CheckToken("NUM", currToken.type);
        CheckToken(":", currToken.tok);
        CheckToken("NUM", currToken.type);
        CheckToken(":", currToken.tok);
        CheckToken("NUM", currToken.type);

    }

    public static void condition()
    {
        //Checks for attribute and then expression
        if (Objects.equals(currToken.type, "ID"))
        {
            CheckToken("ID", currToken.tok);
            //compare();
            expression();
        }
        else if(Objects.equals(currToken.tok, ".")){
            dotAttribute();
            expression();
        }
        else{
            failed("ID", currToken.tok);
        }
        //CheckToken("ID", currToken.type);
        //compare();
        //expression();
    }

    public static void compare()
    {
        if (operator.contains(currToken.tok))
            CheckToken("SYMBOL", currToken.type);
        else
            failed("SYMBOL", currToken.type);
    }

    public static void expression()//????????????????????????????????????                                                                                     //Expression supports ID = ID or ID = NUM
    {                                                                                                                   //Or ID = ID + ID (concatenate strings)
        /*if (Objects.equals(currToken.type, "NUM") && Objects.equals(tokenList.get(index+1).tok, "+"))                   //or ID = NUM + NUM (add values to existing values)
        {
            CheckToken("NUM", currToken.type);
            while(Objects.equals(currToken.tok, "+"))
            {
                CheckToken("+", currToken.tok);
                CheckToken("NUM", currToken.type);
            }
        }

        else */
        //if (Objects.equals(currToken.type, "ID") )
        //{
            //CheckToken("ID", currToken.type);

            if(Objects.equals(currToken.tok, "=")) {
                CheckToken("=", currToken.tok);
                afterOperator();
            }
            else if(Objects.equals(currToken.tok, "<>")) {
                CheckToken("<>", currToken.tok);
                afterOperator();
            }
            else if(Objects.equals(currToken.tok, "<")) {
                CheckToken("<", currToken.tok);
                afterOperator();
            }
            else if(Objects.equals(currToken.tok, ">")) {
                CheckToken(">", currToken.tok);
                afterOperator();
            }
            else if(Objects.equals(currToken.tok, ">=")) {
                CheckToken(">=", currToken.tok);
                afterOperator();
            }
            else if(Objects.equals(currToken.tok, "<=")) {
                CheckToken("<=", currToken.tok);
                afterOperator();
            }
            else if(Objects.equals(currToken.tok, ".")){
                dotAttribute();
                expression();
            }
            /*else if(Objects.equals(currToken.tok, "BETWEEN")) {
                CheckToken("BETWEEN", currToken.tok);
                inQuotes();
            }*/
            else {
                System.out.print("Error #7a: missing an operator like = or < or > or <= or >= or a continuation of the attribute!");
                failed("The right token", currToken.tok);
            }




        /*
            while(Objects.equals(currToken.tok, "+"))
            {
                CheckToken("+", currToken.tok);
                CheckToken("ID", currToken.type);
            }
        */
        //}

    /*
        else if (Objects.equals(currToken.type, "ID"))
        {
            CheckToken("ID", currToken.type);
        }

        else if (Objects.equals(currToken.type, "NUM"))
        {
            CheckToken("NUM", currToken.type);
        }
        */
    }

    public static void afterOperator() {
        if (Objects.equals(currToken.tok, "'")){
            inQuotes();
            andContinuation();
        }
        else if(Objects.equals(currToken.type, "NUM")) {
            CheckToken("NUM", currToken.type);
            if (Objects.equals(currToken.tok, "AND")){
                andContinuation();
            }
            else if(Objects.equals(currToken.tok, ";")){

            }
            else{
                System.out.print("Error #8a: missing the AND keyword or the semicolon!");
                failed("The right token", currToken.tok);
            }
        }
        else if(Objects.equals(currToken.type, "ID")){
            CheckToken("ID", currToken.type);
            dotAttribute();
        }
        else {
            System.out.print("Error #9a: next token should be a String, a number or another attribute!");
            failed("The right token", currToken.tok);
        }
    }

    public static void inQuotes(){
        if(Objects.equals(currToken.tok, "'")){
            CheckToken("'", currToken.tok);
            if(Objects.equals(currToken.type, "ID")){
                //idFound();
                CheckToken("ID", currToken.type);
                if(Objects.equals(currToken.tok, "'"))
                    CheckToken("'", currToken.tok);
                else if(Objects.equals(currToken.tok, ".")){
                    dotAttribute();
                    CheckToken("'", currToken.tok);
                }
                else {
                    System.out.print("Error #11a: missing either the closed quote or attribute continuation");
                    failed("The right token", currToken.tok);
                }
            }
            else{
                failed("ID", currToken.type);
            }

        }else
            failed("", currToken.tok);


    }

    public static void andContinuation(){
        if (Objects.equals(currToken.tok, "AND")){
            CheckToken("AND", currToken.tok);
            if (Objects.equals(currToken.type, "ID")) {
                //idFound();
                CheckToken("ID", currToken.type);
                expression();
            } else {
                System.out.print("Error #10a: missing the attribute!");
                failed("The right token", currToken.tok);
            }
        }
        else if(Objects.equals(currToken.tok, ";")){

        }
        else{
            System.out.print("Error #8a: missing the AND keyword or the semicolon!");
        }
    }


	/********************************************************************************/
	/********************************************************************************/
	/********************************************************************************/
	/********************************************************************************/
	/********************************************************************************/
	/********************************************************************************/
	/********************************************************************************/
	/********************************************************************************/
	/********************************************************************************/
	/********************************************************************************/
    /*This is a place holder to seperate the parser code and semantic code***********/
    
    
    public static void loopUntilNoMoreAttributes(int count, String table_name) {
        count++;
        debug("^^^ 1loopUntilNoMoreAttributes: "+tokenList.get(count).tok);

        if (Objects.equals("ID", tokenList.get(count).type)) {
        	report_error(count, "CODE: 0 [ERROR] Expected ID.");
        	return;
        }
		
		debug("^^^ 2loopUntilNoMoreAttributes*");
        /*16
        if (database.table(table_name) == null || database.table(table_name).get_attribute(tokenList.get(count).tok) == null) {
        	report_error(count, "CODE: 10 [ERROR] This attribute doesn't exist.");
        	return;
        }
		*/

        attributes.add(tokenList.get(count).tok);
		
        count ++;
    	debug(tokenList.get(count).tok);
		if (!Objects.equals(tokenList.get(count).tok, "INTEGER") && !Objects.equals(tokenList.get(count).tok, "NUMBER") && !Objects.equals(tokenList.get(count).tok, "CHARACTER") && !Objects.equals(tokenList.get(count).tok, "DATE")) {
			report_error(count, "CODE: 10-2 [ERROR] Expected INTEGER/NUMBER/CHARACTER/DATE.");
        	return;
		}
		
        attributeTypes.add(tokenList.get(count).tok);
        
        if (Objects.equals(tokenList.get(count).tok, "INTEGER")) {
			count++;
            debug("^^^ 3loopUntilNoMoreAttributes: "+tokenList.get(count).tok);
            dataTypeColumnSpec(count, "integer", tableNameG);
        }
        else if (Objects.equals(tokenList.get(count).tok, "NUMBER")) {
			count++;
            debug("^^^ 4loopUntilNoMoreAttributes: "+tokenList.get(count).tok);
            dataTypeColumnSpec(count, "number", tableNameG);
        }
        else if (Objects.equals(tokenList.get(count).tok, "CHARACTER")) {
			count++;
            debug("^^^ 5loopUntilNoMoreAttributes: "+tokenList.get(count).tok);
            dataTypeColumnSpec(count, "char", tableNameG);
        }
        else if (Objects.equals(tokenList.get(count).tok, "DATE")) {
			count++;
            debug("^^^ 6loopUntilNoMoreAttributes: "+tokenList.get(count).tok);
            dataTypeColumnSpec(count, "date", tableNameG);
        } 
        else if (Objects.equals(tokenList.get(count).tok, ")")) {
            count++;
            debug("^^^ 8loopUntilNoMoreAttributes: "+tokenList.get(count).tok);
            if (!Objects.equals(tokenList.get(count).tok, ";")) {
            	 report_error(count, "CODE: 9 [ERROR] expecting a semicolon.");
            	 return;
            }
            
            debug("ACCEPT!");
            debug("The field attributes are: " +attributes);
            debug("The field attribute types are: " +attributeTypes);
            //databaseDataDefinitionManager.create_table(table_name, attributes, attributeTypes);//16
            reset_global_storage();
        }
    }
	
    public static void integerDataTypeCheck(int count) {
        count++;
        debug("^^^ 1integerDataTypechek: "+tokenList.get(count).tok);
        if (!Objects.equals("NUM", tokenList.get(count).type)) {
        	report_error(count, "CODE: 8 [ERROR] This should be a number.");
        	return;
        }
        
        int columnSpecInteger = Integer.parseInt(tokenList.get(count).tok);
		count++;
        debug("^^^ 2integerDataTypechek: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count).tok, ")")) {
        	report_error(count, "CODE: 7 [ERROR] There should be a closed parenthesis.");
         	return;
		}
        
        count++;
        debug("^^^ 3integerDataTypechek: "+tokenList.get(count).tok);
        if (Objects.equals(tokenList.get(count).tok, ",")) {
			loopUntilNoMoreAttributes(count, tableNameG);
        }
        else {
        	if (!Objects.equals(tokenList.get(count).tok, ")")) {
        		report_error(count, "CODE: 6 [ERROR] There should be a comma or a closed parenthesis.");
        		return;
        	}
        	
			count++;
            debug("^^^ 4integerDataTypechek: "+tokenList.get(count).tok);
            if (!Objects.equals(tokenList.get(count).tok, ";")) {
                report_error(count, "CODE: 5 [ERROR] There should be a semicolon.");
                return;
            }
             
            debug("ACCEPT!");
            debug("The field attributes are: " +attributes);
            debug("The field attribute types are: " +attributeTypes);
   		}
    }
	
    public static void numberDataTypeCheck(int count) {
        count++;
        debug("^^^ 1numberDataTypechek: "+tokenList.get(count).tok);
        if (!Objects.equals("NUM", tokenList.get(count).type)) {
        	report_error(count, "CODE: 18 [ERROR] This should be a number.");
        	return;
        }
        
        int columnSpecNumber = Integer.parseInt(tokenList.get(count).tok);
		count++;
        debug("^^^ 2numberDataTypechek: "+tokenList.get(count).tok);
        
        boolean isClosedParenthesis = Objects.equals(tokenList.get(count).tok, ")");
        boolean isComma = Objects.equals(tokenList.get(count).tok, ",");
        
        if (!isClosedParenthesis && !isComma) {
        	report_error(count, "CODE: 17 [ERROR] There should be a comma or a closed parenthesis.");
        	return;
        }
        
        if (isClosedParenthesis) {
        	count++;
            debug("^^^ 3numberDataTypechek: "+tokenList.get(count).tok);
            
            boolean isClosedParenthesis2 = Objects.equals(tokenList.get(count).tok, ")");
            boolean isComma2 = Objects.equals(tokenList.get(count).tok, ",");
            
            if (!isClosedParenthesis2 && !isComma2) {
            	report_error(count, "CODE: 9 [ERROR] There should be a comma or a closed parenthesis.");
                return;
            }
            
            if (isComma2) {
                count++;
                if (!Objects.equals("NUM", tokenList.get(count).type)) {
                    report_error(count, "CODE: 6 [ERROR] There should be another number after the comma.");
                    return;
                }
                
                count++;
                if (!Objects.equals(tokenList.get(count).tok, ")")) {
                    report_error(count, "CODE: 5 [ERROR] There should be a closed paren.");
                    return;
                }
                
                count++;
                if (!Objects.equals(tokenList.get(count).tok, ",")) {
                    report_error(count, "CODE: 4 [ERROR] There should be another comma.");
                    return;
                }
				
                loopUntilNoMoreAttributes(count, tableNameG);
            }
            else if (isClosedParenthesis2) {
                count++;
                debug("^^^ 7numberDataTypechek: "+tokenList.get(count).tok);
                if (!Objects.equals(tokenList.get(count).tok, ";")) {
                    report_error(count, "CODE: 8 [ERROR] There should be a semicolon.");
                    return;
                }
                
                debug("ACCEPT!");
                debug("The field attributes are: " +attributes);
                debug("The field attribute types are: " +attributeTypes);
            }
        }
        else if (isComma) {
            count++;
            debug("^^^ 10numberDataTypechek: "+tokenList.get(count).tok);
            if (!Objects.equals("NUM", tokenList.get(count).type)) {
                report_error(count, "CODE: 16 [ERROR] This should be a number.");
                return;
            }
			
            count++;
            debug("^^^ 11numberDataTypechek: "+tokenList.get(count).tok);
            if (!Objects.equals(tokenList.get(count).tok, ")")) {
                report_error(count, "CODE: 16-2 [ERROR] There should be an end param.");
                return;
            }
			
            count++;
            debug("^^^ 12numberDataTypechek: "+tokenList.get(count).tok);
            
            boolean isClosedParenthesis2 = Objects.equals(tokenList.get(count).tok, ")");
            boolean isComma2 = Objects.equals(tokenList.get(count).tok, ",");
            
            if (!isClosedParenthesis2 && !isComma2) {
                report_error(count, "CODE: 15 [ERROR] There should be a comma or a closed parenthesis.");
                return;
            }
            
            if (isComma2) {
				loopUntilNoMoreAttributes(count, tableNameG);
            }
            else if (isClosedParenthesis2) {
				count++;
                debug("^^^ 13numberDataTypechek: "+tokenList.get(count).tok);
                if (!Objects.equals(tokenList.get(count).tok, ";")) {
                    report_error(count, "CODE: 14 [ERROR] There should be a semicolon.");
                    return;
                }
                debug("ACCEPT!");
                debug("The field attributes are: " +attributes);
                debug("The field attribute types are: " +attributeTypes);
            }
		}
    }
	
    public static void charDataTypeCheck(int count) {
        count = skipUntil(count, "(");
        debug("^^^ 1charDataTypechek: "+tokenList.get(count).tok);
		
        if (!Objects.equals("NUM", tokenList.get(count).type)) {
        	report_error(count, "CODE: 8 [ERROR] This should be a number.");
        	return;
        }
        
        int columnSpecCharacter = Integer.parseInt(tokenList.get(count).tok);
		count++;
        debug("^^^ 2charDataTypechek: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count).tok, ")")) {
        	report_error(count, "CODE: 7 [ERROR] There should be a closed parenthesis.");
        	return;
        }
		
        count++;
        debug("^^^ 3charDataTypechek: "+tokenList.get(count).tok);
        
        boolean isClosedParenthesis = Objects.equals(tokenList.get(count).tok, ")");
        boolean isComma = Objects.equals(tokenList.get(count).tok, ",");
        
        if (!isClosedParenthesis && !isComma) {
        	report_error(count, "CODE: 6 [ERROR] There should be a comma or a closed parenthesis.");
        	return;
        }
        
        if (isComma) {
			loopUntilNoMoreAttributes(count, tableNameG);
        }
        else if (isClosedParenthesis) {
			count++;
            debug("^^^ 4charDataTypechek: "+tokenList.get(count).tok);
            if (!Objects.equals(tokenList.get(count).tok, ";")) {
                report_error(count, "CODE: 5 [ERROR] There should be a semicolon.");
                return;
            }
             
            debug("ACCEPT!");
            debug("The field attributes are: " +attributes);
            debug("The field attribute types are: " +attributeTypes);

        }
    }
    
    public static void dateDataTypeCheck(int count) {
        int wierdTemp = 4;

        count++;
        debug("^^^ 1dateDataTypechek: "+tokenList.get(count).tok);
        if (!Objects.equals("NUM", tokenList.get(count).type)) {
        	report_error(count, "CODE: 19 [ERROR] This should be a number.");
        	return;
        }
        
        if (!((wierdTemp >= 1) &&  (wierdTemp <= 12))) {
        	report_error(count, "CODE: 18 [ERROR] There is 12 month in a year.");
        	return;
        }
        
        count++;
        debug("^^^ 2dateDataTypechek: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count).tok, "/")) {
        	report_error(count, "CODE: 17 [ERROR] Where is the first slash between the date.");
        	return;
        }
        
        count++;
        debug("^^^ 3dateDataTypechek: "+tokenList.get(count).tok);
        if (!Objects.equals("NUM", tokenList.get(count).type)) {
			report_error(count, "CODE: 16 [ERROR] This should be a number.");
			return;
		}
        
        if (!(wierdTemp >= 1 &&  wierdTemp <= 31)) {
        	report_error(count, "CODE: 15 [ERROR] There are usually 31 or less days in a month.");
        	return;
        }
		
        count ++;
        debug("^^^ 4dateDataTypechek: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count).tok, "/")) {
			report_error(count, "CODE: 14 [ERROR] Where is the second slash between the date.");
			return;
		}
        
        count++;
        debug("^^^ 5dateDataTypechek: "+tokenList.get(count).tok);
        if (!Objects.equals("NUM", tokenList.get(count).type)) {
        	report_error(count, "CODE: 13 [ERROR] This should be a number.");
        	return;
        }
        
       	if (!((wierdTemp >= 0 &&  wierdTemp <= 2015) || (wierdTemp >= 0 &&  wierdTemp <= 99))) {
			report_error(count, "CODE: 12 [ERROR] The year is out of range.");
			return;
		}
    	
    	count++;
        debug("^^^ 6dateDataTypechek: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count).tok, ")")) {
			report_error(count, "CODE: 11 [ERROR] There should be a closed parenthesis.");
			return;
		}
       	
       	count++;
       	debug("^^^ 7dateDataTypechek: "+tokenList.get(count).tok);
        
        boolean isClosedParenthesis = Objects.equals(tokenList.get(count).tok, ")");
        boolean isComma = Objects.equals(tokenList.get(count).tok, ",");
        
        if (!isClosedParenthesis && !isComma) {
        	report_error(count, "CODE: 10 [ERROR] There should be a comma or a closed parenthesis.");
        	return;
        }
        
        if (isComma) {
			loopUntilNoMoreAttributes(count, tableNameG);
        }
        else if (isClosedParenthesis) {
			count++;
           	debug("^^^ 8dateDataTypechek: "+tokenList.get(count).tok);
            if (!Objects.equals(tokenList.get(count).tok, ";")) {
                report_error(count, "CODE: 9 [ERROR] There should be a semicolon.");
                return;
            }
            
            debug("ACCEPT!");
            debug("The field attributes are: " +attributes);
           	debug("The field attribute types are: " +attributeTypes);
        }
    }
	
    public static long dateAndTime(int count) {
        int year, month, day, hour, minute, second;
        count++;
        debug("^^^ 1dateAndTime: "+tokenList.get(count).tok);
        if (!Objects.equals("NUM", tokenList.get(count).type)) {
        	report_error(count, "CODE: 25 [ERROR] This should be a number.");
        	return -1;
        }
        
        if (!(Integer.parseInt(tokenList.get(count).tok) >= 1 &&  Integer.parseInt(tokenList.get(count).tok) <= 12)) {
        	report_error(count, "CODE: 24 [ERROR] There is 12 month in a year.");
        	return -1;
        }
        
        month = Integer.parseInt(tokenList.get(count).tok);
		
		count++;
        debug("^^^ 2dateAndTime: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count).tok, "/")) {
        	report_error(count, "CODE: 23 [ERROR] Where is the first slash between the date.");
        	return -1;
        }
		
        count++;
        debug("^^^ 3dateAndTime: "+tokenList.get(count).tok);
        if (!Objects.equals("NUM", tokenList.get(count).type)) {
        	report_error(count, "CODE: 22 [ERROR] This should be a number.");
        	return -1;
        }
        
        if (!(Integer.parseInt(tokenList.get(count).tok) >= 1 &&  Integer.parseInt(tokenList.get(count).tok) <= 31)) {
        	report_error(count, "CODE: 21 [ERROR] There is usually 31 or less days in a month.");
        	return -1;
        }
        
        day = Integer.parseInt(tokenList.get(count).tok);
		
        count++;
        debug("^^^ 4dateAndTime: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count).tok, "/")) {
        	report_error(count, "CODE: 20 [ERROR] Where is the second slash between the date.");
        	return -1;
        }
		
		count++;
    	debug("^^^ 5dateAndTime: "+tokenList.get(count).tok);
        if (Objects.equals("NUM", tokenList.get(count).type)) {
        	report_error(count, "CODE: 19 [ERROR] This should be a number.");
        	return -1;
        }
        
        if (!((Integer.parseInt(tokenList.get(count).tok) >= 0 &&  Integer.parseInt(tokenList.get(count).tok) <= 2015) || (Integer.parseInt(tokenList.get(count).tok) >= 0 &&  Integer.parseInt(tokenList.get(count).tok) <= 99))) {
			report_error(count, "CODE: 18 [ERROR] The year is out of range.");
			return -1;
		}
		
        if (Integer.parseInt(tokenList.get(count).tok) > 99)
        	year = Integer.parseInt(tokenList.get(count).tok);
        else
       		year = 2000 + Integer.parseInt(tokenList.get(count).tok);
		
        count++;
        debug("^^^ 6dateAndTime: "+tokenList.get(count).tok);
      	if (!Objects.equals(tokenList.get(count).tok, ")")) {
			report_error(count, "CODE: 17 [ERROR] There should be a closed parenthesis.");
			return -1;
		}
		
		count++;
        debug("^^^ 7dateAndTime: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count).tok, ":")) {
        	report_error(count, "CODE: 16 [ERROR] There should be a colon.");
        	return -1;
        }
		
		count++;
        debug("^^^ 8dateAndTime: "+tokenList.get(count).tok);
        if (!(Integer.parseInt(tokenList.get(count).tok) >= 1 &&  Integer.parseInt(tokenList.get(count).tok) <= 12)) {
			 report_error(count, "CODE: 15 [ERROR] There are two 12 hours in a day.");
			 return -1;
		}
        
        hour = Integer.parseInt(tokenList.get(count).tok);
		
		count += 2;
        if (!(Integer.parseInt(tokenList.get(count).tok) >= 60 &&  Integer.parseInt(tokenList.get(count).tok) <= 60)) {
			report_error(count, "CODE: 14 [ERROR] There are only 60 minutes in an hour.");
			return -1;
		}
        
        minute = Integer.parseInt(tokenList.get(count).tok);
		
		count +=2;
        if (!(Integer.parseInt(tokenList.get(count).tok) >= 60 &&  Integer.parseInt(tokenList.get(count).tok) <= 60)) {
			report_error(count, "CODE: 13 [ERROR] There are only 60 seconds in a minute.");
			return -1;
		}
        
        second = Integer.parseInt(tokenList.get(count).tok);
        total = (31536000 * year) + (2628000 * month) + (86400 * day) + (3600 * hour) + (60 * minute) + second;
        
        count++;
        debug("^^^ 9dateAndTime: "+tokenList.get(count).tok);
        if (Objects.equals(tokenList.get(count).tok, "WHERE")) {
        	
            count++;
            debug("^^^ 10commandList: "+tokenList.get(count).tok);
            /*16
            if (database.table(tableNameG).get_attribute(tokenList.get(count).tok) == null) {
            	report_error(count, "CODE: 12 [ERROR] This attribute name doesn't exist.");
                return -1;
            }
			*/
			attributeNameG = tokenList.get(count).tok;
			
            count++;
            if (!Objects.equals(tokenList.get(count).tok, "<") && !Objects.equals(tokenList.get(count).tok, ">") && !Objects.equals(tokenList.get(count).tok, "<>") && !Objects.equals(tokenList.get(count).tok, "<=") && !Objects.equals(tokenList.get(count).tok, ">=") || !Objects.equals(tokenList.get(count).tok, "=")) {
            	report_error(count, "CODE: 11 [ERROR] missing an operator.");
                return -1;
            }
            
            actions.add(tokenList.get(count).tok);

            count ++;
            valueNameG = tokenList.get(count).tok;

        	count ++;
             
            if (!Objects.equals(tokenList.get(count).tok, ";")) {
            	report_error(count, "CODE: 11 [ERROR] missing a semicolon.");
                return -1;
        	}
             
            debug("ACCEPT");
            debug("The first operand is: " +firstOperand);
            debug("The operator is: " +actions);
            debug("The second operand is: " +secondOperand);
            debug("Attributes: "+attributes);
            debug("TIME: "+total);
            //16
            //databaseWDataManipulationManager.table_wupdate(tableNameG, attributes, firstOperand, actions, secondOperand,attributeNameG, valueNameG, total);//16
            reset_global_storage();
   		}
        else if (Objects.equals("ID", tokenList.get(count).type)) {
            /*16
        	if (database.table(tableNameG).get_attribute(tokenList.get(count).tok) == null) {
        		report_error(count, "CODE: 11-2 [ERROR] This attribute name doesn't exist.");
        		return -1;
        	}
        	*/
			count++;
            fieldName_expression(count);
    	}
        return total;
    }
    
    public static void conditionHandling(int count, String temp) {
    	if (!Objects.equals(temp, tokenList.get(count).type)) {
    		report_error(count, "CODE: 5 [ERROR] The attribute type is not correct.");
    		return;
    	}
    	
    	count++;
        debug("^^^ 1conditionHandling: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count).tok, "=") && !Objects.equals(tokenList.get(count).tok, "<=") && !Objects.equals(tokenList.get(count).tok, ">=") && !Objects.equals(tokenList.get(count).tok, "<>") && !Objects.equals(tokenList.get(count).tok, "<") && !Objects.equals(tokenList.get(count).tok, ">")) {
        	report_error(count, "CODE: 4 [ERROR] The conditional symbol is missing.");
        	return;
        }
			
		count++;
        debug("^^^ 2conditionHandling: "+tokenList.get(count).tok);
        if (!Objects.equals(temp, tokenList.get(count).type)) {
			report_error(count, "CODE: 3 [ERROR] These types don't match.");
            return;
        }
    }

    public static void fieldName_expression(int count) {
        debug("^^^ 1fieldName_expression: "+tokenList.get(count).tok);
        /*16
        if (database.table(tableNameG).get_attribute(tokenList.get(count).tok) == null) {
        	report_error(count, "CODE: 11 [ERROR] the attribute does not exist.");
        	return;
        }
		*/
		attributes.add(tokenList.get(count).tok);
		
        count++;
        debug("^^^ 2fieldName_expression: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count).tok, "=")) {
			report_error(count, "CODE: 10 [ERROR] the equal sign is missing.");
			return;
		}
		
    	count++;
        debug("^^^ 3fieldName_expression: "+tokenList.get(count).tok);
        if (!Objects.equals(tokenList.get(count+2).type, tokenList.get(count).type)) {
        	report_error(count, "CODE: 9 [ERROR] the data types don't match.");
        	return;
        }
        
        firstOperand.add(tokenList.get(count).tok);
        actions.add(tokenList.get(count+1).tok);
        secondOperand.add(tokenList.get(count+2).tok);
		
        count +=3;
        debug("^^^ 4fieldName_expression: "+tokenList.get(count).tok);
        if (Objects.equals(tokenList.get(count).tok, ",")) {
			count++;
            fieldName_expression(count);
        }
        
        boolean isSemicolon = Objects.equals(tokenList.get(count).tok, ";");
        boolean isWhere = Objects.equals(tokenList.get(count).tok, "WHERE");
        
        if (!isSemicolon && !isWhere) {
        	report_error(count, "CODE: 8 [ERROR] looking for semicolon or the key word WHERE.");
        	return;
        }
        
        if (isSemicolon) {
			debug("ACCEPT!");
            debug("The first operands are: " +firstOperand);
            debug("The operators are: " +actions);
            debug("The second operands are: " +secondOperand);
            debug("Attributes: "+attributes);
            debug("TIME: "+total);
            //16
            //databaseWDataManipulationManager.table_wupdate(tableNameG, attributes, firstOperand, actions, secondOperand, total);//16
			reset_global_storage();
        }
        else if (isWhere) {
            count++;
            debug("^^^ 5fieldName_expression: "+tokenList.get(count).tok);
            /*16
            if (database.table(tableNameG).get_attribute(tokenList.get(count).tok) == null) {
            	report_error(count, "CODE: 7 [ERROR] This attribute name doesn't exist.");
            	return;
            }
			*/
			count++;
             if (!Objects.equals(tokenList.get(count).tok, "<") && !Objects.equals(tokenList.get(count).tok, ">") && !Objects.equals(tokenList.get(count).tok, "<>") && !Objects.equals(tokenList.get(count).tok, "<=") && !Objects.equals(tokenList.get(count).tok, ">=") && !Objects.equals(tokenList.get(count).tok, "=")) {
               	report_error(count, "CODE: 6 [ERROR] missing an operator.");
                return;
            }
            
            actions.add(tokenList.get(count).tok);
			count ++;
            if (!Objects.equals(tokenList.get(count).tok, ";")) {
            	report_error(count, "CODE: 6-2 [ERROR] Missing semicolon.");
                return;
            }
            
            debug("ACCEPT");
            debug("The first operands are: " +firstOperand);
            debug("The operators are: " +actions);
            debug("The second operands are: " +secondOperand);
            //16
            //databaseDataManipulationManager.table_update(tableNameG, attributes, firstOperand, actions, secondOperand, attributeNameG,valueNameG);//16
            reset_global_storage();
        }
        index = count;
    }
	
    public static void fields(int count) {
        debug("^^^ 1fields: "+tokenList.get(count).tok);

        /*16
        if (database.table(tableNameG).get_attribute(tokenList.get(count).tok) == null) {
        	report_error(count, "CODE: 3 [ERROR] the attribute does not exist.");
        	index = count;
        	return;
        }
		*/
        count++;
        debug("^^^ 2fields: "+tokenList.get(count).tok);
        if (Objects.equals(tokenList.get(count).tok, ","))
			fields(count);
		
        index = count;
    }
	
    public static int fieldsInsert(int count) {
        debug("^^^ 1fieldInsert: "+tokenList.get(count).tok);
        int countFields = 0;
        /*16
        if (database.table(tableNameG).get_attribute(tokenList.get(count).tok) == null) {
        	 report_error(count, "CODE: 3 [ERROR] the attribute does not exist.");
        	 index = count;
        	 return countFields;
        }
        */
        attributes.add(tokenList.get(count).tok);

        countFields++;
        count++;
        debug("^^^ 2fieldInsert: "+tokenList.get(count).tok);
        
        if (Objects.equals(tokenList.get(count).tok, ",")) {
        	count++;
			int temp = fieldsInsert(count);
		}
			
        index = count;
        return countFields;
    }

    public static int valuesInsert(int count) {
    	debug("^^^ 1valuesInsert: "+tokenList.get(count).tok);
        int countValues = 0;
        
        if (Objects.equals(tokenList.get(count).tok, "'"))
			count++;
       
        if (!Objects.equals("ID", tokenList.get(count).type) && !Objects.equals("NUM", tokenList.get(count).type)) {
        	report_error(count, "CODE: 3 [ERROR] this should be of type ID or NUM.");
        	index = count;
        	return countValues;
        }
        
        values.add(tokenList.get(count).tok);
        
        countValues++;
        count++;
        debug("^^^ 2valuesInsert: "+tokenList.get(count).tok);
        
        if (Objects.equals(tokenList.get(count).tok, "'"))
			count++;
        
        if (Objects.equals(tokenList.get(count).tok, ",")) {
            count++;
            int temp = valuesInsert(count);
        }
        
        index = count;
        return countValues;
    }
    
    public static void dataTypeColumnSpec(int count, String whichType, String table_name) {
        count += 3;
        debug("^^^ 1dataTypeColumnSpec: "+tokenList.get(count).tok);
        if (Objects.equals(tokenList.get(count).tok, "NOT")) {
			count++;
            debug("^^^ 2dataTypeColumnSpec: "+tokenList.get(count).tok);

            if (!Objects.equals(tokenList.get(count).tok, "NULL")) {
            	report_error(count, "CODE: 4 [ERROR] The key word should be NULL after Not.");
            	return;
            }

           	count++;
            debug("^^^ 3dataTypeColumnSpec: "+tokenList.get(count).tok);
			if (!Objects.equals(tokenList.get(count).tok, ")")) {
				report_error(count, "CODE: 4-2 [ERROR] Missing end parenthesis.");
				return;
			}
            
            count++;
            if (!Objects.equals(tokenList.get(count).tok, ";")) {
            	report_error(count, "CODE: 4-3 [ERROR] Missing semicolon.");
				return;
            }
            
            debug("ACCEPT!");
            debug("The field attributes are: " +attributes);
            debug("The field attribute types are: " +attributeTypes);
            //16
            //databaseDataDefinitionManager.create_table(table_name, attributes, attributeTypes);//16
            reset_global_storage();
        }
        
        else if (Objects.equals(tokenList.get(count).tok, ",")) {
            loopUntilNoMoreAttributes(count, table_name);
        }
        
        else if (Objects.equals(tokenList.get(count).tok, ")")) {
            count++;
            debug("^^^ 1dataTypeColumnSpec: "+tokenList.get(count).tok);
            if (!Objects.equals(tokenList.get(count).tok, ";")) {
            	report_error(count, "CODE: 2 [ERROR] there should be a semicolon.");
            	return;
            }
            
            debug("CODE: ACCEPT!");
            debug("The field attributes are: " +attributes);
            debug("The field attribute types are: " +attributeTypes);
            //16
            //databaseDataDefinitionManager.create_table(table_name, attributes, attributeTypes);//16
            reset_global_storage();
        }

        else if (Objects.equals(tokenList.get(count).tok, "(")) {
            if (whichType.equals("integer"))
                integerDataTypeCheck(count);
            else if (whichType.equals("number"))
                numberDataTypeCheck(count);
            else if (whichType.equals("char"))
                charDataTypeCheck(count);
            else if (whichType.equals("date"))
                dateDataTypeCheck(count);
        }
        
        else report_error(count, "CODE: 3 [ERROR] Next token should be a NOT, a comma, or a closed parenthesis.");
    }
	
    public static int skipUntil(int count, String currentTok) {
        while ((!Objects.equals(currentTok, tokenList.get(count).tok)) && (count <= tokenList.size()))
            count++;

        count++;
        return count;
    }
	
    public static void expression(int count) {
        count++;
        firstOperand.add(tokenList.get(count).tok);

        count++;
        if (Objects.equals(tokenList.get(count).tok, "+") || Objects.equals(tokenList.get(count).tok, "-") || Objects.equals(tokenList.get(count).tok, "/") || Objects.equals(tokenList.get(count).tok, "*"))
            actions.add(tokenList.get(count).tok);
        else
            report_error(count, "CODE: 1 [ERROR] There should be an operator.");

        count++;
        secondOperand.add(tokenList.get(count).tok);
    }
	
    public static void semantics() {
    	debug(tokenList.get(index).tok);
		//String kWord = "";
        //String create = "CREATE";

        String s = tokenList.get(index).tok;
        /*
        if (s.equals("CREATE")) {
            index++;

            boolean createTable = Objects.equals(tokenList.get(index).tok, "TABLE");
            boolean createDatabase = Objects.equals(tokenList.get(index).tok, "TABLE");

            if (!createTable && !createDatabase) {
                report_error("CODE: 8 [ERROR] This should be the key word TABLE or DATABASE after CREATE.");
                //break;//16
            }

            if (createTable) {
                index++;
                debug(tokenList.get(index).tok);
                */
                    /*16
                    if (database.table(tokenList.get(index).tok) != null) {
                    	report_error("CODE: 4 [ERROR] this table name already exists.");
                    	break;
                    }
                    */
        /*
                tableNameG = tokenList.get(index).tok;

                index++;
                if (!Objects.equals(tokenList.get(index).tok, "(")) {
                    report_error("CODE: 3 [ERROR] missing a closed paren.");
                    //break;//16
                }

                index++;
                debug(tokenList.get(index).tok);
                if (!Objects.equals("ID", tokenList.get(index).type))
                    //break;//16

                attributes.add(tokenList.get(index).tok);

                index++;

                if (!Objects.equals(tokenList.get(index).tok, "INTEGER") && !Objects.equals(tokenList.get(index).tok, "NUMBER") && !Objects.equals(tokenList.get(index).tok, "CHARACTER") && !Objects.equals(tokenList.get(index).tok, "DATE")) {
                    report_error("CODE: 2 [ERROR] This should be a keyword.");
                    //break;//16;
                }

                attributeTypes.add(tokenList.get(index).tok);
                debug(tokenList.get(index).tok);

                if (Objects.equals(tokenList.get(index).tok, "INTEGER")) {
                    index++;
                    dataTypeColumnSpec(index, "integer", tableNameG);
                } else if (Objects.equals(tokenList.get(index).tok, "NUMBER")) {
                    index++;
                    dataTypeColumnSpec(index, "number", tableNameG);
                } else if (Objects.equals(tokenList.get(index).tok, "CHARACTER")) {
                    index++;
                    dataTypeColumnSpec(index, "char", tableNameG);
                } else if (Objects.equals(tokenList.get(index).tok, "DATE")) {
                    index++;
                    dataTypeColumnSpec(index, "date", tableNameG);
                }
            } else if (createDatabase) {
                index++;
    */
                    /*16
                    if (databaseManager.database_exists(tokenList.get(index).tok)) {
                       report_error("CODE: 5 [ERROR] The Database name already exists.");
                       break;
                    }
                    */
        /*
                index++;
                debug("^^^ 6commandList: " + tokenList.get(index).tok);
                if (!Objects.equals(tokenList.get(index).tok, ";")) {
                    report_error("CODE: 7 [ERROR] This should be a semicolon and end of statment.");
                    //break;//16;
                }
        */
                	/*16
                	database = databaseManager.get_database(tokenList.get(index-1).tok);
                    databaseDataDefinitionManager = new DatabaseDataDefinitionManager(database);
                    databaseDataManipulationManager = new DatabaseDataManipulationManager(database);
                    databaseWDataManipulationManager = new DatabaseDataWManipulationManager(database);
                    */
        /*
            }

        } else*/ /* if (s.equals("DROP")) {
            index++;
            debug("^^^ 9commandList: " + tokenList.get(index).tok);

            boolean dropTable = Objects.equals(tokenList.get(index).tok, "TABLE");
            boolean dropDatabase = Objects.equals(tokenList.get(index).tok, "TABLE");


            if (!dropTable && !dropDatabase) {
                report_error("CODE: 16 [ERROR] This should be the key word TABLE or DATABASE after DROP.");
                //break;//16;
            }

            if (dropTable) {
                index++;
                debug("^^^ 10commandList: " + tokenList.get(index).tok);
                */
                    /*16
                    if (database.table(tokenList.get(index).tok) == null) {
                    	report_error("CODE: 12 [ERROR] this table name does not exists.");
                    	break;
                    }
                    */
        /*
                tableNameG = tokenList.get(index).tok;

                index++;
                debug("^^^ 11commandList: " + tokenList.get(index).tok);
                if (!Objects.equals(tokenList.get(index).tok, ";")) {
                    report_error("CODE:  [ERROR] looking for semicolon.");
                    //break;//16;
                }

                debug("ACCEPT");

                //databaseDataDefinitionManager.drop_table(tableNameG);//16
            } else if (dropDatabase) {
                index++;
            */
                    /*16
                    if (!databaseManager.database_exists(tokenList.get(index).tok)) {
                       report_error("CODE: 13 [ERROR] The Database name already exists.");
                       break;
                    }
                    */
        /*
                index++;
                debug("^^^ 14commandList: " + tokenList.get(index).tok);
                if (!Objects.equals(tokenList.get(index).tok, ";")) {
                    report_error("CODE: 15 [ERROR] This should be a semicolon and end of statment.");
                    //break;//16;
                }

                debug("ACCEPT");

                //databaseManager.drop_database(database.name());//16
                //database = null;//16
            }

        } else*/ if (s.equals("SELECT")) {
            boolean selectDatabase = Objects.equals(tokenList.get(index).tok, "TABLE");

            index++;
            debug("^^^ 17commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, "*")) {
                //report_error("CODE: 23 [ERROR] Where is the * or the ID ");//16
                //break;//16;
            }

            index++;
            debug("^^^ 18commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, "FROM")) {
                report_error("CODE: 22 [ERROR] There should be a FROM key word. ");
                //break;//16;
            }

            index++;
            debug("^^^ 19commandList: " + tokenList.get(index).tok);
            try {
                    /*16
                	if (database.table(tokenList.get(index).tok) == null) {
                		report_error("CODE: 21 [ERROR] this table name does not exists.");
                		break;
                	}
                    else*/
                if (selectDatabase) {//////////////////////////////////////////////////////////////////////////////////////////////
                    index++;
                        /*16
                        if (databaseManager.database_exists(tokenList.get(index).tok)) {
                            report_error("CODE: 21B [ERROR] The Database name already exists.");
                            break;
                        }
                        */
                    index++;
                    debug("^^^ 6commandList: " + tokenList.get(index).tok);
                    if (!Objects.equals(tokenList.get(index).tok, ";")) {
                        report_error("CODE: 21C [ERROR] This should be a semicolon and end of statment.");
                        //break;//16;
                    }

                    //database = databaseManager.get_database(tokenList.get(index-1).tok);//16
                    //databaseDataDefinitionManager = new DatabaseDataDefinitionManager(database);//16
                    //databaseDataManipulationManager = new DatabaseDataManipulationManager(database);//16
                    //databaseWDataManipulationManager = new DatabaseDataWManipulationManager(database);//16
                }
            } catch (Exception e) {
                report_error("CODE: 21-0 [ERROR] Must load a database first.");
                //break;//16;
            }

            tableNameG = tokenList.get(index).tok;

            index++;
            debug("^^^ 20commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, ";")) {
                report_error("CODE:  [ERROR] looking for semicolon.");
                //break;//16;
            }

            debug("ACCEPT");
            //System.out.println(databaseDataManipulationManager.table_select(tableNameG));//16

        } /*else if (s.equals("WSELECT")) {
            index++;
            debug("^^^ 24commandList: " + tokenList.get(index).tok);

            boolean isAsterick = Objects.equals(tokenList.get(index).tok, "*");
            //boolean isValidAttribute = database.table(tableNameG).get_attribute(tokenList.get(index).tok) != null;//16
            boolean isValidAttribute = true;//16

            if (!isAsterick && !isValidAttribute) {
                report_error("CODE: 40 [ERROR] either the attribute name doesn't exists or you didn't select star.");
                //break;//16;
            }

            if (isAsterick) {
                index++;
                debug("^^^ 25commandList: " + tokenList.get(index).tok);
                if (!Objects.equals(tokenList.get(index).tok, "FROM")) {
                    report_error("CODE: 40-2 [ERROR] Expected FROM.");
                    //break;//16;
                }

                index++;
                debug("^^^ 26commandList: " + tokenList.get(index).tok);
                    //16
                    //if (database.table(tokenList.get(index).tok) == null) {
                    	//report_error("CODE: 29 [ERROR] this table name does not exists.");
                    	//break;
                    //}

                tableNameG = tokenList.get(index).tok;

                index++;
                debug("^^^ 27commandList: " + tokenList.get(index).tok);
                if (!Objects.equals(tokenList.get(index).tok, ";")) {
                    report_error("CODE: 28 [ERROR] looking for semicolon.");
                    //break;//16;
                }

                debug("ACCEPT");
                //TO DO
            } else if (isValidAttribute) {
                index++;
                if (Objects.equals(tokenList.get(index).tok, ",")) {
                    index++;
                    fields(index);
                }

                if (!Objects.equals(tokenList.get(index).tok, ")")) {
                    report_error("CODE: 39 [ERROR] missing a comma or closed parenthesis.");
                    //break;//16;
                }

                index++;
                debug("^^^ 30commandList: " + tokenList.get(index).tok);
                if (!Objects.equals(tokenList.get(index).tok, "FROM")) {
                    report_error("CODE: 38 [ERROR] missing the key word FROM.");
                    //break;//16;
                }

                index++;
                debug("^^^ 31commandList: " + tokenList.get(index).tok);
                    //16
                    //if (database.table(tokenList.get(index).tok) == null) {
                    	 //report_error("CODE: 37 [ERROR] this table name does not exists.");
                    	 //break;
                    //}

                index++;
                debug("^^^ 33commandList: " + tokenList.get(index).tok);

                boolean isSemicolon = Objects.equals(tokenList.get(index).tok, ";");
                boolean isWhere = Objects.equals(tokenList.get(index).tok, "WHERE");

                if (!isSemicolon && !isWhere) {
                    report_error("CODE: 36 [ERROR] looking for semicolon or the key word WHERE.");
                    //break;//16;
                }

                if (isSemicolon) {
                    debug("ACCEPT!");
                    debug("The field attributes are: " + attributes);
                    debug("The field attribute types are: " + attributeTypes);
                    //TODO
                } else if (Objects.equals(tokenList.get(index).tok, "WHERE")) {
                    index++;
                    debug("^^^ 33commandList: " + tokenList.get(index).tok);
                        //16
                        //if (database.table(tableNameG).get_attribute(tokenList.get(index).tok) == null) {
                        	//report_error("CODE: 35 [ERROR] This attribute name doesn't exist.");
                        	//break;
                        //}


                    attributeNameG = tokenList.get(index).tok;

                    index++;
                    if (!Objects.equals(tokenList.get(index).tok, "<") && !Objects.equals(tokenList.get(index).tok, ">") && !Objects.equals(tokenList.get(index).tok, "<>") && !Objects.equals(tokenList.get(index).tok, "<=") && !Objects.equals(tokenList.get(index).tok, ">=") && !Objects.equals(tokenList.get(index).tok, "=")) {
                        report_error("CODE: 34 [ERROR] missing an operator.");
                        //break;//16;
                    }

                    index++;
                    valueNameG = tokenList.get(index).tok;

                    index++;
                    if (!Objects.equals(tokenList.get(index).tok, ";")) {
                        report_error("CODE: 34 [ERROR] missing an operator.");
                        //break;//16;
                    }

                    debug("ACCEPT");
                    //TODO
                }
            }

        } else if (s.equals("INSERT")) {
            int dummy = 0;
            int numberOfFields;

            index++;
            debug("^^^ 41commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, "INTO")) {
                report_error("CODE: 66 [ERROR] missing the key word INTO.");
                //break;//16;
            }

            index++;
            debug("^^^ 42commandList: " + tokenList.get(index).tok);
                //16
                //if (database.table(tokenList.get(index).tok) == null) {
                	//report_error("CODE: 65 [ERROR] this table name does not exists.");
                	//break;
                //}


            tableNameG = tokenList.get(index).tok;

            index++;
            debug("^^^ 43commandList: " + tokenList.get(index).tok);
            boolean isLeftParam = Objects.equals(tokenList.get(index).tok, "(");
            boolean isValues = Objects.equals(tokenList.get(index).tok, "VALUES");
            if (!isLeftParam && !isValues) {
                report_error("CODE: 64 [ERROR] there should be a open parenthesis or the key word VALUES.");
                //break;//16;
            }

            if (isLeftParam) {
                dummy = 1;

                index++;
                if (Objects.equals(tokenList.get(index).tok, "'"))
                    index++;

                debug("^^^ 44commandList: " + tokenList.get(index).tok);
                    //16
                    //if (database.table(tableNameG).get_attribute(tokenList.get(index).tok) == null) {
                        //report_error("CODE: 56 [ERROR] this attribute name does not exists.");
                        //break;
                    //}

                attributes.add(tokenList.get(index).tok);
                index++;
                if (Objects.equals(tokenList.get(index).tok, "'"))
                    index++;

                debug("^^^ 45commandList: " + tokenList.get(index).tok);
                if (Objects.equals(tokenList.get(index).tok, ",")) {
                    index++;
                    numberOfFields = fieldsInsert(index);
                }

                if (!Objects.equals(tokenList.get(index).tok, ")")) {
                    report_error("CODE: 55 [ERROR] missing the closed parenthesis.");
                    //break;//16;
                }

                index++;
                debug("^^^ 46commandList: " + tokenList.get(index).tok);
                if (!Objects.equals(tokenList.get(index).tok, "VALUES")) {
                    report_error("CODE: 54 [ERROR] missing the key word VALUES.");
                    //break;//16;
                }

                index++;
                debug("^^^ 47commandList: " + tokenList.get(index).tok);
                if (!Objects.equals(tokenList.get(index).tok, "(")) {
                    report_error("CODE: 53 [ERROR] missing an open parenthesis.");
                    //break;//16;
                }

                index++;
                if (Objects.equals(tokenList.get(index).tok, "'"))
                    index++;

                debug("^^^ 48commandList: " + tokenList.get(index).tok);
                if (!Objects.equals("ID", tokenList.get(index).type) && !Objects.equals("NUM", tokenList.get(index).type)) {
                    report_error("CODE: 52 [ERROR] this should be of type ID or NUM.");
                    //break;//16;
                }

                values.add(tokenList.get(index).tok);

                index++;
                if (Objects.equals(tokenList.get(index).tok, "'"))
                    index++;

                debug("^^^ 49commandList: " + tokenList.get(index).tok);
                if (Objects.equals(tokenList.get(index).tok, ",")) {
                    index++;
                    numberOfFields = valuesInsert(index);
                }

                if (!Objects.equals(tokenList.get(index).tok, ")")) {
                    report_error("CODE: 51 [ERROR] this should be an end parenthesis.");
                    //break;//16;
                }

                index++;
                if (!Objects.equals(tokenList.get(index).tok, ";")) {
                    report_error("CODE: 50 [ERROR] missing a semicolon.");
                    //break;//16;
                }

                debug("ACCEPT!");
                debug("The field attributes are: " + attributes);
                debug("The field values are: " + values);
                //databaseDataManipulationManager.table_insert(tableNameG, attributes, values);//16
                reset_global_storage();
            } else if (isValues) {
                index++;
                debug("^^^ 57commandList@: " + tokenList.get(index).tok);
                if (!Objects.equals(tokenList.get(index).tok, "(")) {
                    report_error("CODE: 63 [ERROR] missing an open parenthesis.");
                    //break;//16;
                }

                index++;
                if (Objects.equals(tokenList.get(index).tok, "'"))
                    index++;

                debug("^^^ 58commandList@@: " + tokenList.get(index).tok);
                if (!Objects.equals("ID", tokenList.get(index).type) && !Objects.equals("NUM", tokenList.get(index).type)) {
                    report_error("CODE: 62 [ERROR] this should be of type ID or NUM.");
                    //break;//16;
                }

                values.add(tokenList.get(index).tok);

                index++;
                if (Objects.equals(tokenList.get(index).tok, "'"))
                    index++;

                debug("^^^ 59commandList@@@: " + tokenList.get(index).tok);
                if (Objects.equals(tokenList.get(index).tok, ",")) {
                    index++;
                    numberOfFields = valuesInsert(index);
                }

                if (!Objects.equals(tokenList.get(index).tok, ")")) {
                    report_error("CODE: 61 [ERROR] this should be an end param.");
                    //break;//16;
                }

                index++;
                if (!Objects.equals(tokenList.get(index).tok, ";")) {
                    report_error("CODE: 60 [ERROR] missing a semicolon.");
                    //break;//16;
                }

                debug("ACCEPT!");
                debug("The field values are: " + values);
                //databaseDataManipulationManager.table_insert(tableNameG, values);//16
                reset_global_storage();
            }

        } else if (s.equals("DELETE")) {
            index++;
            debug("^^^ 67commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, "FROM")) {
                report_error("CODE: 75 [ERROR] Where is the key word FROM ");
                //break;//16;
            }

            index++;
            debug("^^^ 68commandList: " + tokenList.get(index).tok);
                //16
                //if (database.table(tokenList.get(index).tok) == null) {
                	//report_error("CODE: 74 [ERROR] this table name does not exists.");
                	//break;
                //}

            tableNameG = tokenList.get(index).tok;

            index++;
            debug("^^^ 69commandList: " + tokenList.get(index).tok);

            if (!Objects.equals(tokenList.get(index).tok, ";") && !Objects.equals(tokenList.get(index).tok, "WHERE")) {
                report_error("CODE: 73 [ERROR] looking for semicolon or the key word WHERE.");
                //break;//16;
            }

            if (Objects.equals(tokenList.get(index).tok, "WHERE")) {
                index++;
                debug("^^^ 70commandList: " + tokenList.get(index).tok);
                    //16
                    //if (database.table(tableNameG).get_attribute(tokenList.get(index).tok) == null) {
                    	//report_error("CODE: 72 [ERROR] This attribute name doesn't exist.");
                    	//break;
                    //}

                attributeNameG = tokenList.get(index).tok;

                index++;
                if (Objects.equals(tokenList.get(index).tok, "<") || Objects.equals(tokenList.get(index).tok, ">") || Objects.equals(tokenList.get(index).tok, "<>") || Objects.equals(tokenList.get(index).tok, "<=") || Objects.equals(tokenList.get(index).tok, ">=") || Objects.equals(tokenList.get(index).tok, "=")) {
                    index++;
                    valueNameG = tokenList.get(index).tok;

                    index++;

                    if (!Objects.equals(tokenList.get(index).tok, ";")) {
                        debug("71 [ERROR] missing an operator.");
                        //break;//16;
                    }

                    debug("ACCEPT");
                    //databaseDataManipulationManager.table_delete(tableNameG,attributeNameG,valueNameG );//16
                }
            }

        } else if (s.equals("UPDATE")) {
            index++;
            debug("^^^ 76commandList: " + tokenList.get(index).tok);
                //16
                //if (database.table(tokenList.get(index).tok) == null) {
                	//report_error("CODE: 90 [ERROR] this table does not exists.");
                	//break;
                //}

            tableNameG = tokenList.get(index).tok;

            index++;
            debug("^^^ 77commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, "SET")) {
                report_error("CODE: 89 [ERROR] the key word SET is missing.");
                //break;//16;
            }

            index++;
            debug("^^^ 78commandList: " + tokenList.get(index).tok);
                //16
                //if (database.table(tableNameG).get_attribute(tokenList.get(index).tok) == null) {
                    //report_error("CODE: 88 [ERROR] the attribute does not exist.");
                    //break;
                //}

            attributes.add(tokenList.get(index).tok);

            index++;
            debug("^^^ 79commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, "=")) {
                report_error("CODE: 87 [ERROR] the equal sign is missing.");
                //break;//16;
            }

            index++;
            debug("^^^ 80commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index + 2).type, tokenList.get(index).type)) {
                report_error("CODE: 86 [ERROR] the data types don't match.");
                //break;//16;
            }

            firstOperand.add(tokenList.get(index).tok);
            actions.add(tokenList.get(index + 1).tok);
            secondOperand.add(tokenList.get(index + 2).tok);

            index += 3;
            debug("^^^ 81commandList: " + tokenList.get(index).tok);

            if (Objects.equals(tokenList.get(index).tok, ",")) {
                index++;
                fieldName_expression(index);
            }

            boolean isSemicolon = Objects.equals(tokenList.get(index).tok, ";");
            boolean isWhere = Objects.equals(tokenList.get(index).tok, "WHERE");

            if (!isSemicolon && !isWhere) {
                report_error("CODE: 85 [ERROR] looking for semicolon or the key word WHERE.");
                //break;//16;
            }

            if (isSemicolon) {
                debug("ACCEPT!");
                debug("The first operand is: " + firstOperand);
                debug("The operator is: " + actions);
                debug("The second operand is: " + secondOperand);
                debug("Attributes: " + attributes);
                //databaseDataManipulationManager.table_update(tableNameG, attributes, firstOperand, actions, secondOperand);//16
                reset_global_storage();
            } else if (isWhere) {
                index++;
                debug("^^^ 82commandList: " + tokenList.get(index).tok);
                    //16
                    //if (database.table(tableNameG).get_attribute(tokenList.get(index).tok) == null) {
                    	//report_error("CODE: 84 [ERROR] This attribute name doesn't exist.");
                    	//break;
                    //}

                attributeNameG = tokenList.get(index).tok;

                index++;
                if (!Objects.equals(tokenList.get(index).tok, "<") && !Objects.equals(tokenList.get(index).tok, ">") && !Objects.equals(tokenList.get(index).tok, "<>") && !Objects.equals(tokenList.get(index).tok, "<=") && !Objects.equals(tokenList.get(index).tok, ">=") && !Objects.equals(tokenList.get(index).tok, "=")) {
                    report_error("83 [ERROR] missing an operator.");
                    //break;//16;
                }

                index++;
                valueNameG = tokenList.get(index).tok;

                index++;

                if (!Objects.equals(tokenList.get(index).tok, ";")) {
                    report_error("83-2 [ERROR] missing a semicolon.");
                    //break;//16;
                }

                debug("ACCEPT");
                debug("The first operand is: " + firstOperand);
                debug("The operator is: " + actions);
                debug("The second operand is: " + secondOperand);
                //16
                //databaseDataManipulationManager.table_update(tableNameG, attributes, firstOperand, actions, secondOperand, attributeNameG,valueNameG);//16
                reset_global_storage();
            }

        } else if (s.equals("WUPDATE")) {
            boolean isWhere;
            boolean isSemicolon;
            index++;
            debug("^^^ 91commandList: " + tokenList.get(index).tok);
                //16
                //if (database.table(tokenList.get(index).tok) == null) {
                	//report_error("CODE: 105 [ERROR] this table does not exists.");
                	//break;
                //}


            tableNameG = tokenList.get(index).tok;

            index++;
            debug("^^^ 92commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, "SET")) {
                report_error("CODE: 104 [ERROR] the key word SET is missing.");
                //break;//16;
            }

            index++;
            debug("^^^ 93commandList: " + tokenList.get(index).tok);
            if (Objects.equals(tokenList.get(index).tok, "DATE")) {
                long totalTime = dateAndTime(index);
                //break;//16;
            }

                //16
                //if (database.table(tableNameG).get_attribute(tokenList.get(index).tok) == null) {
                	//report_error("CODE: 103 [ERROR] there should be a valid attribute.");
                //}

            attributes.add(tokenList.get(index).tok);

            index++;
            debug("^^^ 94commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, "=")) {
                report_error("CODE: 102 [ERROR] the equal sign is missing.");
                //break;//16;
            }

            index++;
            debug("^^^ 95commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index + 2).type, tokenList.get(index).type)) {
                report_error("CODE: 101 [ERROR] the data types don't match.");
                //break;//16;
            }

            firstOperand.add(tokenList.get(index).tok);
            actions.add(tokenList.get(index + 1).tok);
            secondOperand.add(tokenList.get(index + 2).tok);

            index += 3;
            debug("^^^ 96commandList: " + tokenList.get(index).tok);
            if (Objects.equals(tokenList.get(index).tok, ",")) {
                index++;
                fieldName_expression(index);
            }

            isSemicolon = Objects.equals(tokenList.get(index).tok, ";");
            isWhere = Objects.equals(tokenList.get(index).tok, "WHERE");

            if (!isSemicolon && !isWhere) {
                report_error("CODE: 100 [ERROR] looking for semicolon or the key word WHERE.");
                //break;//16;
            }

            if (isSemicolon) {
                debug("ACCEPT!");
                debug("The first operand is: " + firstOperand);
                debug("The operator is: " + actions);
                debug("The second operand is: " + secondOperand);
                debug("Attributes: " + attributes);
                //16
                //databaseDataManipulationManager.table_update(tableNameG, attributes, firstOperand, actions,secondOperand);//16
                reset_global_storage();
            } else if (isWhere) {
                index++;
                debug("^^^ 97commandList: " + tokenList.get(index).tok);
                    //16
                    //if (database.table(tableNameG).get_attribute(tokenList.get(index).tok) == null) {
                    	//report_error("CODE: 99 [ERROR] This attribute name already exists.");
                    	//break;
                    //}

                attributeNameG = tokenList.get(index).tok;

                index++;
                if (!Objects.equals(tokenList.get(index).tok, "<") && !Objects.equals(tokenList.get(index).tok, ">") && !Objects.equals(tokenList.get(index).tok, "<>") && !Objects.equals(tokenList.get(index).tok, "<=") && !Objects.equals(tokenList.get(index).tok, ">=") && !Objects.equals(tokenList.get(index).tok, "=")) {
                    report_error("CODE: 98 [ERROR] missing an operator.");
                    //break;//16;
                }

                index++;
                valueNameG = tokenList.get(index).tok;

                index++;
                if (!Objects.equals(tokenList.get(index).tok, ";")) {
                    report_error("CODE: 98-2 [ERROR] missing semicolon.");
                    //break;//16;
                }

                debug("ACCEPT");
                debug("The first operand is: " + firstOperand);
                debug("The operator is: " + actions);
                debug("The second operand is: " + secondOperand);
                //16
                //databaseDataManipulationManager.table_update(tableNameG, attributes, firstOperand, actions,secondOperand,attributeNameG,valueNameG);//16
                reset_global_storage();
            }

        } else if (s.equals("SAVE") || s.equals("COMMIT")) {
            index++;
            debug("^^^ 106commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, ";")) {
                report_error("CODE: 107 [ERROR] Where is the semicolon ");
                //break;//16;
            }

            debug("ACCEPT");
            //databaseManager.save_database(database);//16

        } else if (s.equals("LOAD")) {
            index++;
            debug("^^^ 108commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, "DATABASE")) {
                report_error("CODE: 112 [ERROR] This should be the key word DATABASE after LOAD.");
            }

            index += 2;
            debug("^^^ 110commandList: " + tokenList.get(index).tok);
            if (!Objects.equals(tokenList.get(index).tok, ";")) {
                report_error("CODE: 111 [ERROR] This should be a semicolon and end of statment.");
                //break;//16;
            }

            debug("ACCEPT");
            //database = databaseManager.get_database(tokenList.get(index-1).tok);//16
            //databaseDataDefinitionManager = new DatabaseDataDefinitionManager(database);//16
            //databaseDataManipulationManager = new DatabaseDataManipulationManager(database);//16
            //databaseWDataManipulationManager = new DatabaseDataWManipulationManager(database);//16

        }*/ else {
            report_error("CODE: 113 [ERROR] wrong first key word.");

        }
    }
    
    public static void reset_global_storage() {
    	attributes = new ArrayList<String>();
    	values = new ArrayList<String>();
   		attributeTypes = new ArrayList<String>();
    	firstOperand = new ArrayList<String>();
    	secondOperand = new ArrayList<String>();
    	actions = new ArrayList<String>();
    }
    
    public static void debug(String s) {
    	if (DEBUG)
    		System.out.println(s);

        stop = true;
    }
    
    public static void report_error(String e) {
    	if (REPORT_ERROR)
    		System.out.println(e + " [ at token " + tokenList.get(index).tok + " ] ");
    }
    
    public static void report_error(int count, String e) {
    	if (REPORT_ERROR)
    		System.out.println(e + " [ at token " + tokenList.get(count).tok + " ] ");
    }

}

//Token objects for Parser and Semantics
class Token
{
    String type = "";
    String tok = "";
    public Token(String type, String tok)
    {
        this.type = type;
        this.tok = tok;
    }
}
