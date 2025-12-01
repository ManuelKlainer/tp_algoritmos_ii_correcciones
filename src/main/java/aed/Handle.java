package aed;

interface Handle {    
    /**
     * Devuelve el estudiante asociado
     * 
     */
    public Estudiante estudiante();
    
    /**
     * Devuelve la posición en la cola de prioridad del estudiante
     * 
     */
    public int posicion();
}
