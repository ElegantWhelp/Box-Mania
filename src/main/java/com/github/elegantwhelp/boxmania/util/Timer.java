package com.github.elegantwhelp.boxmania.util;

public class Timer {
	private double time;
	
	public Timer() {
		time = 0;
	}
	
	public void start() {
		time = getTimeInSeconds();
	}
	
	public double getElapsedTime() {
		return getTimeInSeconds() - time;
	}
	
	public static double getTimeInSeconds() {
		return (double)System.nanoTime() / (double)1000000000L;
	}
}
