package se.blunden.tvrdecoder;

import java.util.ArrayList;

public class Decoder {
	
	private String[] tvr;
	private String[] tsi;
	
	public Decoder() {
		tvr = new String[40];
		tsi = new String[16];
		
		populateArrays();
	}
	
	public ArrayList<String> decodeTVR(String input_tvr) {
		
		String decoded;
		ArrayList<String> output = new ArrayList<String>(10); //decide on a good initial size
		for(int i = 0; i < input_tvr.length(); i++) {
			decoded = parseTVRDigit(i, String.valueOf(input_tvr.charAt(i)));
			if(!decoded.equals("")) {
				output.add(decoded);
			}
		}
		if (output.size() > 0) {
			return output;
		} 
		else {
			return null;
		}
	}
	
	public ArrayList<String> decodeTSI(String input_tsi) {
		
		String decoded;
		ArrayList<String> output = new ArrayList<String>(10); //decide on a good initial size
		for(int i = 0; i < input_tsi.length(); i++) {
			decoded = parseTSIDigit(i, String.valueOf(input_tsi.charAt(i)));
			if(!decoded.equals("")) {
				output.add(decoded);
			}
		}
		if (output.size() > 0) {
			return output;
		} 
		else {
			return null;
		}
	}
	
	private String parseTVRDigit(int i, String digit) {
		//convert hex to binary
		digit = Integer.toBinaryString(Integer.parseInt(digit, 16));
		
		int rb = 0;
		String ret = "";
		for(int b = digit.length() - 1; b >= 0; b--) {
			if(digit.charAt(b) == '1') {
				ret = tvr[i * 4 + 3 - rb];
			}
			rb++;
		}
		return ret;
	}
	
	private String parseTSIDigit(int i, String digit) {
		//convert hex to binary
		digit = Integer.toBinaryString(Integer.parseInt(digit, 16));
		
		int rb = 0;
		String ret = "";
		for(int b = digit.length() - 1; b >= 0; b--) {
			if(digit.charAt(b) == '1') {
				ret = tsi[i * 4 + 3 - rb];
			}
			rb++;
		}
		return ret;
	}
	
	//data from the EMV specification
	private void populateArrays() {
		
		//Terminal Verification Results
		tvr[0] = "Offline data authentication was not performed";
		tvr[1] = "SDA failed";
		tvr[2] = "ICC data missing";
		tvr[3] = "Card appears in terminal exception file";
		tvr[4] = "DDA failed";
		tvr[5] = "CDA failed";
		tvr[6] = "Reserved for future use";
		tvr[7] = "Reserved for future use";
		tvr[8] = "ICC and terminal have different application versions";
		tvr[9] = "Expired application";
		tvr[10] = "Application not yet effective";
		tvr[11] = "Requested service not allowed for card product";
		tvr[12] = "New card";
		tvr[13] = "Reserved for future use";
		tvr[14] = "Reserved for future use";
		tvr[15] = "Reserved for future use";
		tvr[16] = "Cardholder verification was not successful";
		tvr[17] = "Unrecognised CVM";
		tvr[18] = "PIN Try Limit exceeded";
		tvr[19] = "PIN entry required and PIN pad not present or not working";
		tvr[20] = "PIN entry required, PIN pad present but PIN was not entered";
		tvr[21] = "Online PIN entered";
		tvr[22] = "Reserved for future use";
		tvr[23] = "Reserved for future use";
		tvr[24] = "Transaction exceeds floor limit";
		tvr[25] = "Lower consecutive offline limit exceeded";
		tvr[26] = "Upper consecutive offline limit exceeded";
		tvr[27] = "Transaction selected randomly for online processing";
		tvr[28] = "Merchant forced transaction online";
		tvr[29] = "Reserved for future use";
		tvr[30] = "Reserved for future use";
		tvr[31] = "Reserved for future use";
		tvr[32] = "Default TDOL used";
		tvr[33] = "Issuer authentification failed";
		tvr[34] = "Script processing failed before final GENERATE AC";
		tvr[35] = "Script processing failed after final GENERATE AC";
		tvr[36] = "Reserved for future use";
		tvr[37] = "Reserved for future use";
		tvr[38] = "Reserved for future use";
		tvr[39] = "Reserved for future use";
		
		//Transaction Status Information
		tsi[0] = "Offline data authentication was performed";
		tsi[1] = "Cardholder verification was performed";
		tsi[2] = "Card risk managment was performed";
		tsi[3] = "Issuer authentication was performed";
		tsi[4] = "Terminal risk management was performed";
		tsi[5] = "Script processing was performed";
		tsi[6] = "Reserved for future use";
		tsi[7] = "Reserved for future use";
		tsi[8] = "Reserved for future use";
		tsi[9] = "Reserved for future use";
		tsi[10] = "Reserved for future use";
		tsi[11] = "Reserved for future use";
		tsi[12] = "Reserved for future use";
		tsi[13] = "Reserved for future use";
		tsi[14] = "Reserved for future use";
		tsi[15] = "Reserved for future use";
	}
}
