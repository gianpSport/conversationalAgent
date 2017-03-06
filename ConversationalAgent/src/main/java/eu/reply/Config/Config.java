package eu.reply.Config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private static final String propFileName = "ConversationalAgent.properties";

	private static Config config = getInstance();
	
	private static Properties properties;

	private static boolean DEBUG_DM;
	private static boolean DEEP_DEBUG_DM;
	private static boolean DEBUG_DB;
	private static boolean DEBUG_NLG;
	private static boolean DEBUG_OPENER;
	private static boolean DEBUG_SEMANTIC_POOL;
	private static boolean DEBUG_TOKEN_ALLIGN;
	private static boolean DEEP_DEBUG_TOKEN_ALLIGN;
	private static boolean DEBUG_TRANSLATE_TOKEN;
	private static boolean DEEP_DEBUG_TRANSLATE_TOKEN;
	
	private static String PATH_ONT;
	
	private static String PATH_SEMANTIC_NET;
	private static String PATH_EXAMPLE;
	
	
	private Config() {

		Properties prop = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		try {
			if (inputStream != null) {

				prop.load(inputStream);
				properties=prop;

			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static Config getInstance() {
		if (config == null) {
			config=new Config();
			fillProperties();
		}
		return config;
	}
	
	private static void fillProperties(){
		PATH_ONT=properties.getProperty("path.ontology");
		PATH_EXAMPLE=properties.getProperty("path.example");
		PATH_SEMANTIC_NET=properties.getProperty("path.semantic.net");
		DEBUG_TRANSLATE_TOKEN=properties.getProperty("debug.translate.token").equals("true");
		DEEP_DEBUG_TRANSLATE_TOKEN=properties.getProperty("debug.deep.translate.token").equals("true");
		DEBUG_TOKEN_ALLIGN=properties.getProperty("debug.token.allign").equals("true");
		DEEP_DEBUG_TOKEN_ALLIGN=properties.getProperty("debug.deep.token.allign").equals("true");
		DEBUG_SEMANTIC_POOL=properties.getProperty("debug.semantic.pool").equals("true");
		DEBUG_OPENER=properties.getProperty("debug.opener").equals("true");
		DEBUG_NLG=properties.getProperty("debug.nlg").equals("true");
		DEBUG_DB=properties.getProperty("debug.db").equals("true");
		DEBUG_DM=properties.getProperty("debug.dm").equals("true");
		DEEP_DEBUG_DM=properties.getProperty("debug.deep.dm").equals("true");
	}
	
	public static boolean getDialogManagerDebug(){
		return DEBUG_DM;
	}
	
	public static boolean getDialogManagerDeepDebug(){
		return DEEP_DEBUG_DM;
	}
	
	public static boolean getDBDebug(){
		return DEBUG_DB;
	}
	
	public static boolean getNLGDebug(){
		return DEBUG_NLG;
	}
	
	public static boolean getopeNERDebug(){
		return DEBUG_OPENER;
	}
	
	public static boolean getSemanticPoolDebug(){
		return DEBUG_SEMANTIC_POOL;
	}
	public static boolean getTokenAllignDebug(){
		return DEBUG_TOKEN_ALLIGN;
	}
	public static boolean getTokenAllignDeepDebug(){
		return DEEP_DEBUG_TOKEN_ALLIGN;
	}
	public static boolean getTranslateTokenDebug(){
		return DEBUG_TRANSLATE_TOKEN;
	}
	public static boolean getTranslateTokenDeepDebug(){
		return DEEP_DEBUG_TRANSLATE_TOKEN;
	}
	
	public static String getPathSemanticNet(){
		return PATH_SEMANTIC_NET;
	}
	
	public static String getPathExample(){
		return PATH_EXAMPLE;
	}
	
	public static String getPathOntology(){
		return PATH_ONT;
	}

}
