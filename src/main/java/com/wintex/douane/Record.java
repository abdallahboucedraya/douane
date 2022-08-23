package com.wintex.douane;

import java.io.IOException;

public class Record {

	public static void main(String[] args) {
		try {
			Runtime.getRuntime().exec("explorer.exe /select," + System.getProperty("user.home"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String idTracking;
	private String numberTacking;
	private String fullName;
	private String fullNamePhonetic;
	private String addresse;
	private String adressePhonetic;

	public String getIdTracking() {
		return idTracking;
	}

	public void setIdTracking(String idTracking) {
		this.idTracking = idTracking;
	}

	public String getNumberTacking() {
		return numberTacking;
	}

	public void setNumberTacking(String numberTacking) {
		this.numberTacking = numberTacking;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullNamePhonetic() {
		return fullNamePhonetic;
	}

	public void setFullNamePhonetic(String fullNamePhonetic) {
		this.fullNamePhonetic = fullNamePhonetic;
	}

	public String getAddresse() {
		return addresse;
	}

	public void setAddresse(String addresse) {
		this.addresse = addresse;
	}

	public String getAdressePhonetic() {
		return adressePhonetic;
	}

	public void setAdressePhonetic(String adressePhonetic) {
		this.adressePhonetic = adressePhonetic;
	}

	public Record() {

	}

	public Record(String idTracking, String numberTacking, String fullName, String fullNamePhonetic, String addresse,
			String adressePhonetic) {
		super();
		this.idTracking = idTracking;
		this.numberTacking = numberTacking;
		this.fullName = fullName;
		this.fullNamePhonetic = fullNamePhonetic;
		this.addresse = addresse;
		this.adressePhonetic = adressePhonetic;
	}

}
