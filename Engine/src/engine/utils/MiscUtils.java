package engine.utils;

import java.io.PrintStream;

public class MiscUtils {
	
	public static void enginePrint(String s, PrintStream stream) {
		stream.println("ENGINE>> " + s);
	}
	
	public static void enginePrintWarning(String s) {
		enginePrint("Warning " + s, System.out);
	}
	
	public static void enginePrintSeriousWarning(String s) {
		enginePrint("Warning " + s, System.err);
	}
}
