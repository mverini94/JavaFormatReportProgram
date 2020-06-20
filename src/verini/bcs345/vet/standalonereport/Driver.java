
package verini.bcs345.vet.standalonereport;

import java.io.*; //java io has a printstream and also throws exceptions
import java.util.NoSuchElementException;
import java.util.Scanner;
/***
 * This class reads procedures from a file in a certain format
 * and it writes those procedures to the stand alone report
 * in a different format
 * 
 * @author mattverini
 * @version 1.0
 * @since 9/20/2018
 */

public class Driver {
	Scanner keyboardScanner; //used to get the user input
	Scanner s; //used to access the files
	String inputFile;
	String outPutFile;
	PrintStream pso; //used to write to the file

	
	
	public static void main(String[] args) throws IOException {
		Driver myDriver = new Driver(); //creates a new driver class
		myDriver.setFileNames(); //asks the user to put in the two file names
		myDriver.openWrite(); //opens the file and writes to it
	}

	public void setFileNames() {
		/***
		 * This method asks the user for the input file name
		 * and the output file name stores it as a string
		 */
		keyboardScanner = new Scanner(System.in); 
		System.out.println("Please enter input file name");
		inputFile = keyboardScanner.nextLine();
		System.out.println("Please enter output file name");
		outPutFile = keyboardScanner.nextLine();
		keyboardScanner.close();
	}

	public void openWrite() {
		try {
			/***
			 * This method opens the input file and it grabs the input information.
			 * Then it outputs it in the correct format to the output file.
			 */
			pso = new PrintStream(outPutFile); //opens the output file so something can be written in it
			s = new Scanner(new File(inputFile)); //creates scanner that reads from the input file
			String vetName = s.nextLine(); //scanner grabs the next line then moves to the next line
			String visitMonth = s.nextLine();
			String visitDay = s.nextLine();
			String visitYear = s.nextLine();
			String petName = s.nextLine();
			String petSpecies = s.nextLine();
			String PetGender = s.nextLine();
			String NumberOfProcedures = s.nextLine();

			writeHeader(vetName, visitMonth, visitDay, visitYear, petName, petSpecies, PetGender, NumberOfProcedures);
			double totalAmount = 0;
			double totalAmountCovered = 0;
			double totalAmountDue = 0;
			while (s.hasNextLine()) { //while the scanner has the next line follow through with everything after
				String useless;
				String procedureName = s.nextLine();
				double cost = s.nextDouble();
				useless = s.nextLine();
				int qty = s.nextInt();
				useless = s.nextLine();
				String isCovered = s.nextLine();
				String pctCov1 = s.nextLine();
				double pctCov = Double.parseDouble(pctCov1);
				double amount = cost * qty;
				double amoutCovered = amount * pctCov;
				double amountDue = amount - amoutCovered;
				writeProcedures(procedureName, cost, qty, amount, isCovered, pctCov, amoutCovered, amountDue);
				
				
				totalAmount += amount; 
				totalAmountCovered += amoutCovered; 
				totalAmountDue += amountDue;
			}
			
			double temp = 12;
			int temp1 = 12;
			double temp2 = 12;
			writeToFile("----                               -----  ---     "
					+ "------  ----------   -----------   --------------   ----------");
			writeProcedures("Total", temp, temp1, totalAmount, "", temp2, totalAmountCovered, totalAmountDue);
			/***
			 * This method is used to write the total line but temp is utilized because
			 * there are only three values needed to write the total line
			 */
		} catch (FileNotFoundException e) {
			System.out.println("Could not open file");
		} catch (NoSuchElementException e) {
			System.out.println("No such element exception");
			e.printStackTrace();
		}
	}

	public void writeToFile(String s) {
		/***
		 * This method writes a string to the output file
		 * 
		 * @param s the string to print to the output file
		 */
		try {
			pso.println(s);
		} catch (Exception e) {
			System.out.println("ERROR. Could not open file!");
		}
	}

	public void writeHeader(String s1, String month, String day, String year, String pName, String pSpecies,
			String pGender, String s8) {
		/***
		 * This method prints out the header of the report
		 * 
		 * @param s1 prints the vetName to the output file
		 * @param month prints the month to the output file
		 * @param day prints the day to the output file
		 * @param year prints the year to the output file
		 * @param pName prints the petName to the output file
		 * @param pSpecies prints the pet species to the output file
		 * @param pGender prints the pet gender to the output file
		 * @param s8 prints the number of procedures to the output file
		 */
		String L1 = "Pet Veterinarian Visit Report";
		String L2 = "-----------------------------";
		String L3 = "Veterinarian: " + s1;
		String L4 = "Date        : " + month + "/" + day + "/" + year;
		String L5 = " ";
		String L6 = "Pet Name    : " + pName;
		String L7 = "Pet Species : " + pSpecies;
		String L8 = "Pet Gender  : " + pGender;
		String L9 = "Procedures";
		writeToFile(L1);
		writeToFile(L2);
		writeToFile(L3);
		writeToFile(L4);
		writeToFile(L5);
		writeToFile(L6);
		writeToFile(L7);
		writeToFile(L8);
		writeToFile(L5);
		writeToFile(L9);
		writeToFile(L5);
		writeToFile("Name                               " + "Price  Qty    Amount   Is Covered   Pct Covered   "
				+ "Amount Covered   Amount Due");
		writeToFile("----                               " + "-----  ---    ------   ----------   "
				+ "-----------   --------------   ----------");
	}

	public void writeProcedures(String procedure, double cost, int qty, double amt, String covered, double perC,
			double aCovered, double aDue) {
		/***
		 * This function checks the first string, and if it says total, then it writes the total line
		 * If it says any other procedure names, it writes the procedure in the procedure line format
		 * 
		 * @param procedure procedure name to be printed to the output file
		 * @param cost cost to be printed to the output file
		 * @param qty quantity to be printed to the output file
		 * @param amt amount to be printed to the output file
		 * @param covered is the procedure covered to be printed to the output file
		 * @param perC percentage covered to be printed to the output file
		 * @param aCovered amount covered to be printed to the output file
		 * @param aDue amount due to be printed to the output file
		 */
		try {
			if (procedure.equals("Total")) {
				pso.printf("%-25s %14s %4s %10.2f %11s %13s %16.2f %12.2f%n", procedure, covered, covered, amt, covered,
						covered, aCovered, aDue);
			} else {
				pso.printf("%-25s %14.2f %4d %10.2f %11s %13.2f %16.2f %12.2f%n", procedure, cost, qty, amt, covered,
						perC, aCovered, aDue);
			}
		} catch (Exception e) {
		}
	}
}