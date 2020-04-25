package org.elevators.util;

public class Config {
	private static int floorsNum = 10;
	private static int elevatorsNum = 3;
	
	public static void setFloorsNum(int floorsNum){
		Config.floorsNum = floorsNum;
	}
	
	public static void setElevatorsNum(int elevatorsNum) {
		Config.elevatorsNum = elevatorsNum;
	}
	
	public static int getFloorsNum() {
		return floorsNum;
	}
	
	public static int getElevatorsNum() {
		return elevatorsNum;
	}
	
	public static void parse(String[] cmdArgs){
		if (cmdArgs.length == 2) {
			checkArg(cmdArgs[0], cmdArgs[1]);
		}
		if (cmdArgs.length == 4){
			checkArg(cmdArgs[0], cmdArgs[1]);
			checkArg(cmdArgs[2], cmdArgs[3]);
		}
	}
	
	public static void checkArg(String arg, String val){
		if (arg.equals("bn")) {
			setFloorsNum(Integer.parseInt(val));
		}
		if (arg.equals("en")) {
			setElevatorsNum(Integer.parseInt(val));
		}
	}
}
