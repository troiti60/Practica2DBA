/**
 * Representa coordenadas en 2D
 *
 * @author Alexander Straub
 */
public class Coord {
	
	/**
	* Coordenadas
	*/
	private int x, y;
	
	/**
	* Getter devolviendo la coordenada X
	*
	* @return Coordenada X
	* @author Alexander Straub
	*/
	public int getX() {
		return this.x;
	}
	
	/**
	* Setter parar guardar un nuevo valor de X
	*
	* @param newX Nuevo valor de X
	* @author Alexander Straub
	*/
	public void setX(int newX) {
		this.x = newX;
	}
	
	/**
	* Getter devolviendo la coordenada Y
	*
	* @return Coordenada Y
	* @author Alexander Straub
	*/
	public int getY() {
		return this.y;
	}
	
	/**
	* Setter parar guardar un nuevo valor de Y
	*
	* @param newY Nuevo valor de Y
	* @author Alexander Straub
	*/
	public void setY(int newY) {
		this.y = newY;
	}
	
	/**
	* Constructor
	*
	* @param x Coordenada X
	* @param y Coordenada Y
	* @author Alexander Straub
	*/
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	* Devolver la coordenada en el norte
	*
	* @return Coordenada en el norte
	* @author Alexander Straub
	*/
	public Coord N() {
		return new Coord(x, y-1);
	}
	
	/**
	* Devolver la coordenada en el este
	*
	* @return Coordenada en el este
	* @author Alexander Straub
	*/
	public Coord E() {
		return new Coord(x+1, y);
	}
	
	/**
	* Devolver la coordenada en el sur
	*
	* @return Coordenada en el sur
	* @author Alexander Straub
	*/
	public Coord S() {
		return new Coord(x, y+1);
	}
	
	/**
	* Devolver la coordenada en el oeste
	*
	* @return Coordenada en el oeste
	* @author Alexander Straub
	*/
	public Coord O() {
		return new Coord(x-1, y);
	}
	
	/**
	* Devolver la coordenada en el noreste
	*
	* @return Coordenada en el noreste
	* @author Alexander Straub
	*/
	public Coord NE() {
		return new Coord(x+1, y-1);
	}
	
	/**
	* Devolver la coordenada en el sureste
	*
	* @return Coordenada en el sureste
	* @author Alexander Straub
	*/
	public Coord SE() {
		return new Coord(x+1, y+1);
	}
	
	/**
	* Devolver la coordenada en el suroeste
	*
	* @return Coordenada en el suroeste
	* @author Alexander Straub
	*/
        public Coord SO() {
		return new Coord(x-1, y+1);
	}
	
	/**
	* Devolver la coordenada en el noroeste
	*
	* @return Coordenada en el noroeste
	* @author Alexander Straub
	*/
	public Coord NO() {
		return new Coord(x-1, y-1);
	}
	
	/**
	* Comparar este objeto con otro, devolviendo true si
	* las coordenadas coinciden
	*
	* @param otro Objeto con que comparar
	* @return True si las coordenadas coinciden
	* @author Alexander Straub
	*/
	@Override
	public boolean equals(Object otro) {
		if (!(otro instanceof Coord)) {
			return false;
		}
                if (otro==this) return true;

		Coord segundo = (Coord) otro;

		return (this.x == segundo.getX()
			&& this.y == segundo.getY());
	}
    
	/**
	* Crear un hash code
	*
	* @return Hash code
	* @author
	*/
    @Override
    public int hashCode() {
        return (this.x + (this.y * (int)((double)Integer.MAX_VALUE / 2.0)));
    }
	
}