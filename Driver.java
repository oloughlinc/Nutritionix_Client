package com.nutrifinder.com.nutrifinder.eclipse;

public class Driver {
	
	public static void main(String[] args) {
		
		App app = App.getInstance();
		
		while (! app.quit) {
			app.run();
		}
	}

}
