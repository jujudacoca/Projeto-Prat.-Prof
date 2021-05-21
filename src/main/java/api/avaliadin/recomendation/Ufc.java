package api.avaliadin.recomendation;

public class Ufc {

	private String uf;
	
	private int contador;
	

	public Ufc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Ufc(String uf, int contador) {
		super();
		this.uf = uf;
		this.contador = contador;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public int getContador() {
		return contador;
	}

	public void setContador(int contador) {
		this.contador = contador;
	}
}
