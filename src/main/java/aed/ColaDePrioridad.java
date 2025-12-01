package aed;

public class ColaDePrioridad<T extends Priorizable<T>> {
    private T[] heap;
    private int tamaño;
    private int capacidad;

    // Clase interna para el Handle
    public static class HandleHeap<T extends Priorizable<T>> implements Handle {

        private T elementoReal;
        private ColaDePrioridad<T> colaPropietaria;

        public HandleHeap(T elemento, ColaDePrioridad<T> colaPropietaria) {
            this.elementoReal = elemento;
            this.colaPropietaria = colaPropietaria;
        } // O(1)

        public Estudiante estudiante() {
            if (this.elementoReal instanceof Estudiante) {
                return (Estudiante) this.elementoReal;
            }
            return null;
        } // O(1)

        public int posicion() {
            if (this.elementoReal instanceof Estudiante) {
                return ((Estudiante) this.elementoReal).obtenerPosicionHeap();
            }
            return -1;
        } // O(1)

        public void resolverEjercicio(int indiceEjercicio, int respuesta, int[] examenCanonico) {
            
            if (this.elementoReal instanceof Estudiante) {
                Estudiante estudiante = (Estudiante) this.elementoReal;

                double notaAnterior = estudiante.obtenerNota();
                boolean cambioEstado = estudiante.intentarResolver(indiceEjercicio, respuesta, examenCanonico); // O(1)
                
                if (cambioEstado) {
                    double notaNueva = estudiante.obtenerNota();
                    if (notaNueva != notaAnterior) {
                        this.colaPropietaria.actualizarPrioridad(this); // O(log E)
                    }
                }
            }
        } // O(log E)
    }

    

    public ColaDePrioridad(int cant_elementos) {
        this.capacidad = cant_elementos;
        this.heap = (T[]) new Priorizable[cant_elementos];
        this.tamaño = 0;
    } // O(1)

    public ColaDePrioridad(T[] elementos) {
        this.tamaño = elementos.length;
        this.capacidad = elementos.length;
        this.heap = (T[]) new Priorizable[this.capacidad];

        for (int i = 0; i < this.tamaño; i++) {
            this.heap[i] = elementos[i];
            actualizarPosicionInterna(i, i);
        } // O(n)

        if (this.tamaño > 0) {
            int indiceUltimoNoHoja = obtenerIndicePadre(this.tamaño - 1);
            for (int i = indiceUltimoNoHoja; i >= 0 ; i--) {
                bajar(i);
            }
        } // O(n) por Floyd
    } // O(n)

    private void actualizarPosicionInterna(int indice, int nuevaPos) {
        if (indice < 0 || indice >= capacidad) return;
        T elemento = heap[indice];
        if (elemento instanceof Estudiante) {
            ((Estudiante) elemento).setearPosicionHeap(nuevaPos);
        }
    } // O(1)

    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

        actualizarPosicionInterna(i, i);
        actualizarPosicionInterna(j, j);
    } // O(1)

    private void subir(int indice) {
        T elemento = (T) heap[indice];
        boolean sigueSubiendo = true;

        while (indice > 0 && sigueSubiendo) {
            int indicePadre = obtenerIndicePadre(indice);
            T padre = (T) heap[indicePadre];

            if (elemento.compararPrioridad(padre) < 0) {
                swap(indice, indicePadre);
                indice = indicePadre; // Actualización de índice
            } else {
                sigueSubiendo = false; // Detiene el loop
            }
        }
    } // O (log n)

    private void bajar(int indice) {
        boolean seguirBajando = true;

        while (tieneHijoIzquierdo(indice) && seguirBajando) {
            int mejorIndiceHijo = obtenerIndiceHijoIzquierdo(indice);
            T mejorHijo = (T) heap[mejorIndiceHijo];

            if (tieneHijoDerecho(indice)) {
                T hijoDerecho = (T) heap[obtenerIndiceHijoDerecho(indice)];
                
                if (hijoDerecho.compararPrioridad(mejorHijo) < 0) {
                    mejorIndiceHijo = obtenerIndiceHijoDerecho(indice);
                    mejorHijo = hijoDerecho;
                }
            }

            T actual = (T) heap[indice];

            if (actual.compararPrioridad(mejorHijo) > 0) {
                swap(indice, mejorIndiceHijo);
                indice = mejorIndiceHijo; // Actualización de índice
            } else {
                seguirBajando = false; // Detiene el loop
            }
        }
    } // O(log n)

    public void actualizarPrioridad(HandleHeap<T> handle) {
        int indiceEnHeap = handle.posicion();
        if (indiceEnHeap < 0 || indiceEnHeap >= tamaño) return;

        if (heap[indiceEnHeap] != handle.elementoReal) {

            return;
        }

        int indicePadre = obtenerIndicePadre(indiceEnHeap);

        if (indiceEnHeap > 0 && heap[indiceEnHeap].compararPrioridad(heap[indicePadre]) < 0) {
            subir(indiceEnHeap);
        } else {
            bajar(indiceEnHeap);
        }
    } // O(log n)

    public HandleHeap<T> encolarEstudiante(T elemento) {
        if (tamaño >= heap.length) throw new IllegalStateException("Cola llena");

        heap[tamaño] = elemento;
        actualizarPosicionInterna(tamaño, tamaño);

        HandleHeap<T> handle = new HandleHeap<T>(elemento, this);
        
        subir(tamaño);
        tamaño++;
        return handle;
    } // O(log n)

    public void encolar(T elemento) {
        if (tamaño >= heap.length) throw new IllegalStateException("Cola llena");

        heap[tamaño] = elemento;
        actualizarPosicionInterna(tamaño, tamaño);
        subir(tamaño);
        tamaño++;
    } // O(log n)

    public T desencolar() {
        if (esVacia()) return null;

        T mejorElemento = (T) heap[0];
        T peorElemento = (T) heap[tamaño - 1];

        heap[0] = peorElemento;
        heap[tamaño - 1] = null;
        tamaño --;

        if (mejorElemento instanceof Estudiante) {
            ((Estudiante) mejorElemento).setearPosicionHeap(-1);
        }

        if (!esVacia()) {
            actualizarPosicionInterna(0, 0);
            bajar(0);
        }

        return mejorElemento;
    } // O(log n)

    public int tamaño() {return tamaño;} // O(1)
    public boolean esVacia() { return tamaño == 0; } // O(1)
    private int obtenerIndicePadre(int i) { return (i - 1) / 2; } // O(1)
    private int obtenerIndiceHijoIzquierdo(int i) { return 2 * i + 1; } // O(1)
    private int obtenerIndiceHijoDerecho(int i) { return 2 * i + 2; } // O(1)
    private boolean tieneHijoIzquierdo(int i) { return obtenerIndiceHijoIzquierdo(i) < tamaño; } // O(1)
    private boolean tieneHijoDerecho(int i) { return obtenerIndiceHijoDerecho(i) < tamaño; } // O(1)
}