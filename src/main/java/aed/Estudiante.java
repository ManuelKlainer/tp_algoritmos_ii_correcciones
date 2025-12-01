package aed;
public class Estudiante implements Comparable<Estudiante>, Priorizable<Estudiante> {
    private double nota;
    private int id;
    private int[] examen;
    private boolean entregado;
    private int cantidadResueltos;
    private int posicionHeap;
    private int fila, columna;

    public Estudiante(int id, int cantidadEjercicios, int fila, int columna) { // -- O(R)
        this.id = id;
        this.nota = 0.0;
        this.examen = new int[cantidadEjercicios];
        for (int i = 0; i < cantidadEjercicios; i++) {
            this.examen[i] = -1; // -1 indica que el ejercicio no ha sido resuelto
        } // O(R)
        this.entregado = false;
        this.cantidadResueltos = 0;
        this.posicionHeap = -1;
        this.fila = fila;
        this.columna = columna;
    } // O(R)

    public double obtenerNota(){ return nota; } // O(1)
    public int obtenerId(){ return id; } // O(1)
    public int obtenerFila() { return fila; } // O(1)
    public int obtenerColumna() { return columna; } // O(1)
    public int[] obtenerExamen() { return examen; } // O(1)
    public boolean entrego() { return entregado; } // O(1)
    public int obtenerCantidadResueltos() { return cantidadResueltos; } // O(1)
    public int obtenerPosicionHeap() { return posicionHeap; } // O(1)

    public void setearPosicionHeap(int posicion) { this.posicionHeap = posicion; } // O(1)
    public void entregar() { this.entregado = true; } // O(1)
    
    public void reemplazarExamen(int[] nuevoExamen, int[] examenCanonico) {
        this.cantidadResueltos = 0;
        this.nota = 0.0;
        for (int i = 0; i < examen.length; i++) {
            if (nuevoExamen[i] != -1) {
                this.examen[i] = nuevoExamen[i];
                this.cantidadResueltos++;
                if (nuevoExamen[i] == examenCanonico[i]) {
                    this.nota += 100.0 / examenCanonico.length;
                }
            } else {
                this.examen[i] = -1;
            }
        } // -- O(R)
    } // -- O(R)

    public boolean intentarResolver(int indiceEjercicio, int respuesta, int[] examenCanonico) {
        if (this.examen[indiceEjercicio] == -1) {
            this.examen[indiceEjercicio] = respuesta;
            this.cantidadResueltos++;
            if (respuesta == examenCanonico[indiceEjercicio]) {
                this.nota += 100.0 / examenCanonico.length;
            }
            return true;
        }
        return false;
    } // O(1)

    @Override
    public int compararPrioridad(Estudiante otro) {

        int compNota = Double.compare(this.nota, otro.nota);
        if (compNota != 0) return compNota;

        return Integer.compare(this.id, otro.id);
    } // O(1)

    @Override
    public int compareTo(Estudiante otro) {
        return compararPrioridad(otro);
    } // O(1)
}
