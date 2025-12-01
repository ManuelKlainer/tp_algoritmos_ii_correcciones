package aed;

public class NotaFinal implements Comparable<NotaFinal>, Priorizable<NotaFinal> {
    public double _nota;
    public int _id;

    public NotaFinal(double nota, int id){
        _nota = nota;
        _id = id;
    }

    @Override
    public int compararPrioridad(NotaFinal otra) {
        if (this._nota != otra._nota) {
            return Double.compare(otra._nota, this._nota);
        }
        return otra._id - this._id;
    }

    public int compareTo(NotaFinal otra){
        if (otra._id != this._id){
            return this._id - otra._id;
        }
        return Double.compare(this._nota, otra._nota);
    }

    public boolean equals(Object o){
        if (!(o instanceof NotaFinal)) return false;
        NotaFinal otra = (NotaFinal) o;
        return this._id == otra._id && this._nota == otra._nota;
    }
}
