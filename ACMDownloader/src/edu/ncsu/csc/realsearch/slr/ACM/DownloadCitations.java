package edu.ncsu.csc.realsearch.slr.ACM;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DownloadCitations {

	public void load()
	{
		
		WebDriver driver = new FirefoxDriver();

        // And now use this to visit Google
        driver.get("http://dl.acm.org.prox.lib.ncsu.edu");

        while(!driver.getPageSource().contains("Searching within"))
        {
        	// wait until results are loaded
        }
        
        String results = "";
        
        try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        while(hasNextLink(driver))
        {
        	WebElement body = driver.findElement(By.tagName("body"));
        	String pageText = body.getText();
        	
        	results += pageText.substring(pageText.indexOf("Result page:"), pageText.lastIndexOf("Result page:"));
        	
        	if(!hasNextLink(driver))
        	{
        		break;
        	}
        	driver.findElement(By.linkText("next")).click();  
        	
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
    	WebElement body = driver.findElement(By.tagName("body"));
    	String pageText = body.getText();
    	
    	results += pageText.substring(pageText.indexOf("Result page:"), pageText.lastIndexOf("Result page:"));

    	try {
			printResults(results);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	driver.close();
	}
	
	private boolean hasNextLink(WebDriver driver)
	{
		try
		{
			driver.findElement(By.linkText("next"));
			return true;
		} catch (NoSuchElementException e)
		{
			return false;
		}
	}
	
	private void printResults(String text) throws IOException
	{
		File file = new File("output/ACM.txt");
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(text);
		bw.close();
		fw.close();
		
		System.out.println("finished printing");
		
	}
}
