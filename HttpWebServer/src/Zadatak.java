
public class Zadatak {
	private String brojZadatka;
	private String naziv;
	private String zaduzeni;
	private String brojTelefona;
	private TezinaZadatka tezinaZadatka;
	private int bodovi;
	private boolean isZapocet;
	public String getBrojZadatka() {
		return brojZadatka;
	}
	public void setBrojZadatka(String brojZadatka) {
		this.brojZadatka = brojZadatka;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public String getZaduzeni() {
		return zaduzeni;
	}
	public void setZaduzeni(String zaduzeni) {
		this.zaduzeni = zaduzeni;
	}
	public String getBrojTelefona() {
		return brojTelefona;
	}
	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}
	public TezinaZadatka getTezinaZadatka() {
		return tezinaZadatka;
	}
	public void setTezinaZadatka(TezinaZadatka tezinaZadatka) {
		this.tezinaZadatka = tezinaZadatka;
	}
	public int getBodovi() {
		return bodovi;
	}
	public void setBodovi(int bodovi) {
		this.bodovi = bodovi;
	}
	public boolean isZapocet() {
		return isZapocet;
	}
	public void setZapocet(boolean isZavrsen) {
		this.isZapocet = isZavrsen;
	}
	public Zadatak(String brojZadatka, String naziv, String zaduzeni, String brojTelefona, TezinaZadatka tezinaZadatka,
			int bodovi, boolean isZavrsen) {
		super();
		this.brojZadatka = brojZadatka;
		this.naziv = naziv;
		this.zaduzeni = zaduzeni;
		this.brojTelefona = brojTelefona;
		this.tezinaZadatka = tezinaZadatka;
		this.bodovi = bodovi;
		this.isZapocet = isZavrsen;
	}
	
	public Zadatak() {
		
	}
}
