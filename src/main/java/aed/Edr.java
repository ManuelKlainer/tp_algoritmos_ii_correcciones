package aed;
import java.util.ArrayList;

import aed.ColaDePrioridad.HandleHeap;

public class Edr {

    public HandleHeap<Estudiante>[] estudiantes;
    private ColaDePrioridad<Estudiante> estudiantesPorNota;
    private int[] examenCanonico;
    private boolean[] copiones;
    private final int LadoAula;
    private final int R; // cant respuestas 
    private final int E; // cant estudiantes

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        this.LadoAula = LadoAula;
        this.E = Cant_estudiantes;
        this.R = ExamenCanonico.length;

        this.estudiantes = new HandleHeap[Cant_estudiantes]; // -- O(E)
        this.examenCanonico = new int[this.R]; // -- O(R)
        for (int i = 0; i < this.R; i++) { // -- O(R)
            this.examenCanonico[i] = ExamenCanonico[i];
        }

        this.estudiantesPorNota = new ColaDePrioridad<>(Cant_estudiantes);

        int estudiantesPorFila = (LadoAula + 1) / 2;

        for (int i = 0; i < Cant_estudiantes; i++) { // -- O(E)
            int posicionEnFila = i % estudiantesPorFila;
            int fila = i / estudiantesPorFila;
            int col = posicionEnFila * 2;
            Estudiante est = new Estudiante(i, this.R, fila, col); // -- O(R)

            HandleHeap<Estudiante> hest = this.estudiantesPorNota.encolarEstudiante(est); // O(log E)

            this.estudiantes[i] = hest;
        }
         // -- O(E*(R+log(E))) -> se presume que R+log(E) es asintóticamente similar a R -> O(E*R)
    } // O(E*R)

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas() {
        double[] notas = new double[this.E]; // -- O(E)
        for (int i = 0; i < this.E; i++) { 
            notas[i] = this.estudiantes[i].estudiante().obtenerNota();
        }// -- O(E)
        
        return notas;
    } // O(E)

//------------------------------------------------COPIARSE------------------------------------------------------------------------


    private HandleHeap<Estudiante> obtenerVecino(int fila, int columna) {
        if (fila < 0 || fila >= this.LadoAula || columna < 0 || columna >= this.LadoAula) {
            return null;
        }

        int estudiantesPorFila = (LadoAula + 1) / 2;
        int posEnFila = columna / 2;
        if (columna % 2 != 0 || posEnFila >= estudiantesPorFila) return null;

        int idVecino = fila * estudiantesPorFila + posEnFila;

        if (idVecino >= this.E) return null;

        return this.estudiantes[idVecino];
    } // O(1)

    public void copiarse(int id_estudiante) {
        HandleHeap hestudiante = this.estudiantes[id_estudiante];
        Estudiante estudiante = hestudiante.estudiante();

        int fila = estudiante.obtenerFila();
        int col = estudiante.obtenerColumna();

        HandleHeap hVecino1 = obtenerVecino(fila, col - 2); // Izquierda
        HandleHeap hVecino2 = obtenerVecino(fila, col + 2); // Derecha
        HandleHeap hVecino3 = obtenerVecino(fila - 1, col); // Adelante

        Estudiante vecino1 = null;
        Estudiante vecino2 = null;
        Estudiante vecino3 = null;

        if (hVecino1 != null) {
            vecino1 = hVecino1.estudiante();
        }
        if (hVecino2 != null) {
            vecino2 = hVecino2.estudiante();
        }
        if (hVecino3 != null) {
            vecino3 = hVecino3.estudiante();
        }

        int[] examenEstudiante = estudiante.obtenerExamen();

        int[] respuestasCopiablesVecinos = new int[3];
        int[] primerRespuestaCopiableVecinos = new int[]{-1,-1,-1};

        int[] examenVecino1 = null;
        int[] examenVecino2 = null;
        int[] examenVecino3 = null;
        if (vecino1 != null) {
            examenVecino1 = vecino1.obtenerExamen();
        }
        if (vecino2 != null) {
            examenVecino2 = vecino2.obtenerExamen();
        }
        if (vecino3 != null) {
            examenVecino3 = vecino3.obtenerExamen();
        }

        for (int i = 0; i < this.R; i++) {
            if (examenEstudiante[i] == -1) {
                if (vecino1 != null && !vecino1.entrego() && examenVecino1[i] != -1) {
                    respuestasCopiablesVecinos[0]++;
                    if (primerRespuestaCopiableVecinos[0] == -1) {
                        primerRespuestaCopiableVecinos[0] = i;
                    }
                }
                if (vecino2 != null && !vecino2.entrego() && examenVecino2[i] != -1) {
                    respuestasCopiablesVecinos[1]++;
                    if (primerRespuestaCopiableVecinos[1] == -1) {
                        primerRespuestaCopiableVecinos[1] = i;
                    }
                }
                if (vecino3 != null && !vecino3.entrego() && examenVecino3[i] != -1) {
                    respuestasCopiablesVecinos[2]++;
                    if (primerRespuestaCopiableVecinos[2] == -1) {
                        primerRespuestaCopiableVecinos[2] = i;
                    }
                }
            }
        } // -- O(R)

        int maxRespuestasCopiables = 0;
        int indMejorVecino = -1;

        if (respuestasCopiablesVecinos[1] > maxRespuestasCopiables) {
            maxRespuestasCopiables = respuestasCopiablesVecinos[1];
            indMejorVecino = 1;
        }
        if (respuestasCopiablesVecinos[0] > maxRespuestasCopiables) {
            maxRespuestasCopiables = respuestasCopiablesVecinos[0];
            indMejorVecino = 0;
        }
        if (respuestasCopiablesVecinos[2] > maxRespuestasCopiables) {
            maxRespuestasCopiables = respuestasCopiablesVecinos[2];
            indMejorVecino = 2;
        }
        
        // --------------------------------------    
        if (indMejorVecino == -1){return;}
        // --------------------------------------


        int indicePreguntaACopiar = primerRespuestaCopiableVecinos[indMejorVecino];
        int respuestaCopiada = -1;

        if (indMejorVecino == 0) {
            respuestaCopiada = examenVecino1[indicePreguntaACopiar];
        } else if (indMejorVecino == 1) {
            respuestaCopiada = examenVecino2[indicePreguntaACopiar];
        } else if (indMejorVecino == 2) {
            respuestaCopiada = examenVecino3[indicePreguntaACopiar];
        }

        hestudiante.resolverEjercicio(indicePreguntaACopiar, respuestaCopiada, examenCanonico); // O(log E)
    } // -- O(R + log E)



//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int id_estudiante, int NroEjercicio, int res) {
        HandleHeap<Estudiante> hestudiante = this.estudiantes[id_estudiante];
        if(!hestudiante.estudiante().entrego()){
            hestudiante.resolverEjercicio(NroEjercicio, res, examenCanonico);
        }
    } // -- O(log E)



//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int k, int[] examenDW) {
        ArrayList<Estudiante> estudiantesConsultantes = new ArrayList<>();
        ArrayList<Estudiante> yaEntregaron = new ArrayList<>();
        int encontrados = 0;

        while (encontrados < k && !this.estudiantesPorNota.esVacia()) {
            Estudiante est = this.estudiantesPorNota.desencolar(); // O(log E)

            if (est.entrego()) {
                yaEntregaron.add(est);
            } else {
                estudiantesConsultantes.add(est);
                encontrados++;
            }
        } // O(k * log E)

        for (int i = 0; i < estudiantesConsultantes.size(); i++) {
            Estudiante est = estudiantesConsultantes.get(i);
            est.reemplazarExamen(examenDW, examenCanonico); // -- O(R)
        } // -- O(k*R)

        for (int i = 0; i < estudiantesConsultantes.size(); i++) {
            Estudiante est = estudiantesConsultantes.get(i);
            HandleHeap<Estudiante> nuevoHandle = this.estudiantesPorNota.encolarEstudiante(est); // -- O(log E)
            this.estudiantes[est.obtenerId()] = nuevoHandle;

        } // -- O(k log E)

        for (int i = 0; i < yaEntregaron.size(); i++) {
            Estudiante est = yaEntregaron.get(i);
            HandleHeap<Estudiante> nuevoHandle = this.estudiantesPorNota.encolarEstudiante(est); // -- O(log E)
            this.estudiantes[est.obtenerId()] = nuevoHandle;
        } // -- O(k log E)

    } // -- O(k (R + log E))
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        HandleHeap<Estudiante> hest = this.estudiantes[estudiante];
        Estudiante est = hest.estudiante();
        if (!est.entrego()) {
            est.entregar();
        }
    } // -- O(1)

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {

        ArrayList<NotaFinal> listaNotas = new ArrayList<>();
        
        if (this.copiones == null) {
            this.copiones = new boolean[this.E];
        }
        
        while (!this.estudiantesPorNota.esVacia()) {
            Estudiante est = this.estudiantesPorNota.desencolar(); // O(log E)

            if (!this.copiones[est.obtenerId()] && est.entrego()) {
                NotaFinal notaFinal = new NotaFinal(est.obtenerNota(), est.obtenerId());
                listaNotas.add(notaFinal);
            }
        } // O(E log E)

        NotaFinal[] notasFinales = new NotaFinal[listaNotas.size()];
        for (int i = 0; i < notasFinales.length; i++) {
            notasFinales[notasFinales.length - i - 1] = listaNotas.get(i);
        } // O(E)

        return notasFinales;
    } // O(E log E)

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    private int[][] contarRespuestasPorEjercicio() {
        int[][] cantidades = new int[this.R][10];
        for (int i = 0; i < this.E; i++) {
            Estudiante est = this.estudiantes[i].estudiante();
            int[] examenEst = est.obtenerExamen();
            for (int j = 0; j < this.R; j++) {
                if (examenEst[j] != -1) {
                    cantidades[j][examenEst[j]]++;
                }
            }
        }
        return cantidades;
    } // O(E*R)

    private boolean esSospechoso(Estudiante est, int[][] cantidades, double umbral) {
        int[] examenEst = est.obtenerExamen();
        boolean esSospechoso = true;
        boolean resolvioAlguno = false;

        for (int ej = 0; ej < this.R && esSospechoso; ej++) {
            int resp = examenEst[ej];
            if (resp != -1) {
                resolvioAlguno = true;
                if (cantidades[ej][resp] - 1 < umbral) {
                    esSospechoso = false;
                }
            }
        }
        return esSospechoso && resolvioAlguno;
    } // O(R)

    public int[] chequearCopias() {
        this.copiones = new boolean[this.E];
        int[][] cantidades = contarRespuestasPorEjercicio(); // O(E*R)

        double umbral = 0.25 * (this.E - 1);
        if (this.E == 1) {
            umbral = 0;
        }

        ArrayList<Integer> sospechosos = new ArrayList<>();
        for (int id = 0; id < this.E; id++) {
            Estudiante est = this.estudiantes[id].estudiante();

            if (esSospechoso(est, cantidades, umbral)) { 
                sospechosos.add(id);
                this.copiones[id] = true;
            } // O(R)
        } //O(E*R)
        int[] resultado = new int[sospechosos.size()];
        for (int i = 0; i < resultado.length; i++) {
            resultado[i] = sospechosos.get(i);
        }//O(R)
        return resultado;
    }
    //O(E*R + E*R + R ) = O(2*E*R) = O(E*R)
}