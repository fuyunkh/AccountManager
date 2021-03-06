package org.cote.accountmanager.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cote.accountmanager.objects.OrganizationType;
import org.cote.accountmanager.objects.UserType;
import org.cote.accountmanager.objects.types.NameEnumType;
import org.cote.accountmanager.client.action.IClientAction;

public class ConsoleProcessor {
	private String prompt = null;
	private Map<String,IClientAction> actions = new HashMap<String,IClientAction>();
	private Pattern consoleLine = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
	public static final Logger logger = LogManager.getLogger(ConsoleProcessor.class);
	private CommandLineParser parser = new PosixParser();
	
	public void runConsole(){
		BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
		try{

			if(prompt == null){
				setPrompt();
			}
			String line = "";
			
			while (line.equalsIgnoreCase("quit") == false && line.equalsIgnoreCase("exit") == false) {
				System.out.print(prompt);
				line = is.readLine();
				String[] linePar = parseLine(line);
				if(linePar.length > 0 && (linePar[0].equals("exit") || linePar[0].equals("quit"))) break;
				handleCommandLine(linePar);
				///logger.info(line);
			    
			}
	
			   is.close();
			}
		catch(IOException e){
			logger.error(e.getMessage());
		} 

	}
	private void handleCommandLine(String[] linePar){
		if(linePar.length == 0){
			return;
		}
		String className = "org.cote.rocket.client.action." + linePar[0] + "Action";
		if(actions.containsKey(className) == false){
			ClassLoader classLoader = ConsoleProcessor.class.getClassLoader();	
			try {
				Class aClass = classLoader.loadClass(className);
				IClientAction action = (IClientAction)aClass.newInstance();
				actions.put(className, action);
			} catch (ClassNotFoundException e) {
				
				logger.error(e.getMessage());
			} catch (InstantiationException e) {
				
				logger.error(e.getMessage());
			} catch (IllegalAccessException e) {
				
				logger.error(e.getMessage());
			}
		}
		if(actions.containsKey(className) == false){
			logger.error("Command " + linePar[0] + " not found.");
			return;
		}
		CommandLine command = null;
		try {
			Options options = actions.get(className).getCommandLineOptions();
			if(options != null) command = parser.parse( actions.get(className).getCommandLineOptions(), Arrays.copyOfRange(linePar, 1,linePar.length));
		} catch (ParseException e) {
			
			logger.error("Error",e);
		}
		
		actions.get(className).execute(this,command,linePar);
		
	}
	private String[] parseLine(String line){
		List<String> matchList = new ArrayList<String>();
		
		Matcher regexMatcher = consoleLine.matcher(line);
		while (regexMatcher.find()) {
		    if (regexMatcher.group(1) != null) {
		        // Add double-quoted string without the quotes
		        matchList.add(regexMatcher.group(1));
		    } else if (regexMatcher.group(2) != null) {
		        // Add single-quoted string without the quotes
		        matchList.add(regexMatcher.group(2));
		    } else {
		        // Add unquoted word
		        matchList.add(regexMatcher.group());
		    }
		} 
		return matchList.toArray(new String[0]);
	}
	public void setPrompt(){
		String objName = "";
		if(ClientContext.getCurrentDirectory() != null) objName = ClientContext.getCurrentDirectory().getPath();
		prompt = "[" + ClientContext.getOrganizationPath() + "/" + ClientContext.getUser().getName() + "]:" + objName + " " + (ClientContext.getContextObjectType() != NameEnumType.UNKNOWN ? "{" + ClientContext.getContextObjectType().toString() + "}" : "") + "$ ";
	}

}
