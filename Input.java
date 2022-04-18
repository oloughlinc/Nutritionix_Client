package com.nutrifinder.com.nutrifinder.eclipse;

import java.util.*;

public class Input {
	
	private Scanner inputScanner = new Scanner(System.in);
	
	public String Get() {
		System.out.printf("Enter food item: ");
		return inputScanner.nextLine();
		//TODO: dump buffer and check for bad input
	}
	
}
