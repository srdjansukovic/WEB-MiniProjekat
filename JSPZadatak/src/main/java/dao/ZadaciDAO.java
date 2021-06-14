package dao;

import java.util.ArrayList;

import beans.TezinaZadatka;
import beans.Zadatak;

public class ZadaciDAO {
	private ArrayList<Zadatak> zadaci;
	private ArrayList<Zadatak> filtrirani;
	
	public ZadaciDAO(){
		zadaci = new ArrayList<Zadatak>();
		Zadatak z1 = new Zadatak("0001/01", "Zadatak 1", "Srdjan Srdjanovic", "0649585855", TezinaZadatka.LAK, 10, false);
        Zadatak z2 = new Zadatak("0001/02", "Zadatak 2", "Stefan Srdjanovic", "0649775855", TezinaZadatka.TEZAK, 15, false);
        Zadatak z3 = new Zadatak("0001/03", "Zadatak 3", "Srdjana Srdjanovic", "0644485855", TezinaZadatka.LAK, 10, false);
        zadaci.add(z1);
        zadaci.add(z2);
        zadaci.add(z3);
        filtrirani = new ArrayList<Zadatak>();
	}
	
	public ArrayList<Zadatak> getZadaci(){
		return zadaci;
	}
	
	public boolean addZadatak(Zadatak zadatak) {
		for(Zadatak zad : zadaci) {
			if(zad.getBrojZadatka().equals(zadatak.getBrojZadatka())) {
				return false;
			}
		}
		zadaci.add(zadatak);
		return true;
	}
	
	public boolean isZadatakUToku(Zadatak z) {
		return z.isZapocet();
	}
	
	public Zadatak getZadatakByBroj(String broj) {
		for(Zadatak z : zadaci) {
			if(z.getBrojZadatka().equals(broj))
				return z;
		}
		return null;
	}
	
	public void filterByNaziv(String naziv) {
		filtrirani.clear();
		for(Zadatak z : zadaci) {
			if(z.getNaziv().toLowerCase().contains(naziv.toLowerCase())) {
				filtrirani.add(z);
			}
		}
	}
	
	public ArrayList<Zadatak> getFiltrirani(){
		return filtrirani;
	}
}	
