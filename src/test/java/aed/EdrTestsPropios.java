package aed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;


class EdrTestsPropios {
    Edr edr;
    int d_aula;
    int cant_alumnos;
    int[] solucion;

    @BeforeEach
    void setUp(){
        d_aula = 5;
        cant_alumnos = 4;
        solucion = new int[]{0,1,2,3,4,5,6,7,8,9};

        edr = new Edr(d_aula, cant_alumnos, solucion);
    }

    @Test
    void estructura_correcta() {

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);
        // Cantidad de estudiantes
        assertEquals(cant_alumnos, edr.estudiantes.length);
        // cada nota pertenece a un Estudiante
        assertEquals(cant_alumnos, edr.notas().length);
    }


    //------------------------------------- copiarse -----------------------------------------------//

    // No hay vecinos 

    @Test
    void  no_hay_vecinos() {
        d_aula = 2; 
        cant_alumnos = 1; 

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        double[] notas_antes = edr.notas();

        edr.copiarse(0);

        double[] notas_despues = edr.notas();

        assertTrue(Arrays.equals(notas_antes, notas_despues));
    }    


    @Test
    void  tres_vecinos_lentos() {
        d_aula = 5;
        cant_alumnos = 5;

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        edr.resolver(4, 0, 0);
        edr.resolver(4, 1, 1);
        edr.resolver(4, 2, 2);
        edr.resolver(4, 3, 3);

        double[] notasAntes = edr.notas();

        edr.copiarse(4);

        double[] notasDepues = edr.notas();

        assertTrue(Arrays.equals(notasAntes,notasDepues));
    }

    @Test
    void  dos_vecinos_lentos() {
        d_aula = 5;
        cant_alumnos = 3;

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        edr.resolver(2, 0, 0);
        edr.resolver(2, 1, 1);
        edr.resolver(2, 2, 2);
        edr.resolver(2, 3, 3);

        double[] notasAntes = edr.notas();

        edr.copiarse(2);

        double[] notasDepues = edr.notas();

        assertTrue(Arrays.equals(notasAntes,notasDepues));

    }


    // No se puede copiar a alguien que ya entregó--------------------------------------------------------------

    @Test
    void  el_mas_copiable_entrego() {
        d_aula = 5;
        cant_alumnos = 3;

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        // el estudiante 1 resuelve su examen tranquilo
        edr.resolver(1, 0, 0);
        edr.resolver(1, 1, 1);
        edr.resolver(1, 2, 2);
        edr.resolver(1, 3, 3);

        // y entrega 
        edr.entregar(1);

        double[] notas_antes = edr.notas();

        // el estudiante 0 esta que se quiere matar pero al único que le puede copiar ya se fue (está en la esquina)
        edr.copiarse(0);

        // como no se puede copiar las notas deberian quedar igual
        double[] notas_despues = edr.notas();

        assertTrue(Arrays.equals(notas_antes,notas_despues));


    }

//---------------------

    @Test
    void  el_mas_copiable_entrego_pero_hay_alguien_mas() {
        d_aula = 5;
        cant_alumnos = 3;

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        // el estudiante 2 resuelve su examen tranquilo
        edr.resolver(2, 1, 1);
        edr.resolver(2, 2, 2);
        edr.resolver(2, 3, 3);

        // y entrega 
        edr.entregar(2);

        // y también el estudiante 0 la tiene re clara y va lento pero seguro resolviendo
        edr.resolver(0, 0, 0);
        edr.resolver(0, 1, 1);
  
        double[] notas_antes = edr.notas();
        
        // el estudiante 1 pobrecito está en otra, la mejor opción para copiar es el estudiante 2 pero ya se fue, le copia al 0
        edr.copiarse(1);
        

        double[] notas_despues = edr.notas();

        // las notas deberían ser distintas porque estudiante 1 copio
        assertFalse(Arrays.equals(notas_antes, notas_despues));

        // y la primera respuesta de estudiante 1 debe ser la misma que estudiante 0, así, le copió a estudiante 0 y no a estudiante 2
        int[] examen_0 = edr.estudiantes[0].estudiante().obtenerExamen();
        int[] examen_1 = edr.estudiantes[1].estudiante().obtenerExamen();

        assertTrue(examen_0[0] == examen_1[0]);
    }


    // empate de vecinos 

    @Test
    void  vecinos_con_mismas_cant_respuestas() {
        d_aula = 5;
        cant_alumnos = 3;

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        // otra vez los 3 de antes, solo que esta vez 0 y 2 tienen la misma cantidad de respuestas y misma copiabilidad o-o, debería desempatar por IDmayor

        edr.resolver(2, 1, 1);
        edr.resolver(2, 2, 2);
        edr.resolver(2, 3, 3);

        edr.resolver(0, 0, 0);
        edr.resolver(0, 1, 1);
        edr.resolver(0,2,2 );

        double[] notas_antes = edr.notas();

        // otra vez 1 quiere copiar, hay que hablar con estudiante 1.. (debería de copiarle a 2)
        edr.copiarse(1);
        
        double[] notas_despues = edr.notas();

        assertFalse(Arrays.equals(notas_antes, notas_despues));

        // pregunta de 1 igual a la de 2 

        int[] examen_2 = edr.estudiantes[2].estudiante().obtenerExamen();
        int[] examen_1 = edr.estudiantes[1].estudiante().obtenerExamen();
        
        assertTrue(examen_1[1] == examen_2[1]);

    }    

    // Alumno ya resolvió todo 
    @Test
    void  alumno_ya_resolivo_todo() {
        d_aula = 5;
        cant_alumnos = 5;
        solucion = new int[]{0,1,2,3};

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        // en verdad el estudiante 1 tiene potencial, pero le faltaba motivación (se motivó)
        edr.resolver(1, 0, 3);
        edr.resolver(1, 1, 2);
        edr.resolver(1, 2, 1);
        edr.resolver(1, 3, 0);

        //mientras los otros siguen haciendo el examen el ya terminó
        edr.resolver(0,0,0);
        edr.resolver(0,1,1);

        edr.resolver(2, 1, 1);

        //guardar momento
        double[] notasAntes = edr.notas();

        // pero aunque haya terminado, intenta copiar porque bueno, la costumbre
        edr.copiarse(1);

        // pero no debería haber logrado nada porque anotó con lapicero las respuestas
        double[] notasDepues = edr.notas();

        assertTrue(Arrays.equals(notasAntes,notasDepues));
    }

    // --------------- resolver ----------------
    @Test
    void  resolver_algo_resuelto() {
        d_aula = 5;
        cant_alumnos = 5;
        solucion = new int[]{0,1,2,3};

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        edr.resolver(1,0,0);
        edr.resolver(1,1,1);

        int[] examen_antes = edr.estudiantes[1].estudiante().obtenerExamen();

        edr.resolver(1, 0, 1);

        int[] examen_despues = edr.estudiantes[1].estudiante().obtenerExamen();
     
        assertTrue(Arrays.equals(examen_antes,examen_despues));
    }

    @Test
    void ya_entregado() {
        d_aula = 5;
        cant_alumnos = 5;
        solucion = new int[]{0,1,2,3};

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        edr.resolver(1,0,0);
        edr.resolver(1,1,1);

        edr.entregar(1);

        double[] notasAntes = edr.notas();

        edr.resolver(1, 2, 2);

        double[] notas_despues = edr.notas();

        assertTrue(Arrays.equals(notasAntes,notas_despues));

    }

    @Test
    void respuestas_incorrecta() {
        d_aula = 5;
        cant_alumnos = 5;
        solucion = new int[]{0,1,2,3};

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        edr.resolver(1, 0, 0);
        edr.resolver(1, 1, 1);
        edr.resolver(1, 2, 2);

        double nota_actual = edr.estudiantes[1].estudiante().obtenerNota();

        edr.resolver(1,3,0);

        // como la respuesta nueva está mal, se mantiene la nota antigua
        double nota_nueva = edr.estudiantes[1].estudiante().obtenerNota();

        assertTrue(nota_actual == nota_nueva);
    
    }
    
    @Test
    void respuestas_correcta() {
        d_aula = 5;
        cant_alumnos = 5;
        solucion = new int[]{0,1,2,3};

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        edr.resolver(1, 0, 0);
        edr.resolver(1, 1, 1);
        edr.resolver(1, 2, 2);

        double nota_actual = edr.estudiantes[1].estudiante().obtenerNota();

        edr.resolver(1,3,3);

        // como la respuesta está bien, suma puntos
        double nota_nueva = edr.estudiantes[1].estudiante().obtenerNota();

        assertTrue(nota_actual < nota_nueva);
    
    }

// ------------------------------------------ DarkWeb--------------------------------------------------
    @Test
    void darkweb_con_mas_k_que_alumnos() {
        d_aula = 5;
        cant_alumnos = 2;
        int[] solucion = new int[]{1,2,3,4,5};
        int[] solucionDW = new int[]{2,2,2,4,5};
        
        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        // no habían hecho nada antes de consultadrDarkWeb
        edr.consultarDarkWeb(10, solucionDW);

        // ahora todos deberían tener el mismo examen
        assertTrue(Arrays.equals(edr.estudiantes[0].estudiante().obtenerExamen(), solucionDW));
        assertTrue(Arrays.equals(edr.estudiantes[1].estudiante().obtenerExamen(), solucionDW));
        
    }

    @Test
    void darkweb_desepate_por_id() {
        d_aula = 5;
        cant_alumnos = 3;
        int[] solucion = new int[]{0,1,2,3,4,5};
        int[] solucionDW = new int[]{1,2,2,2,4,5};
        
        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        // hay 3 alumnos, el alumno 0 está resolviendo
        edr.resolver(0, 0, 0);
        edr.resolver(0, 1, 1);
        edr.resolver(0, 2, 2);


        // el 1 y el 2 están medio mal, pero uno se arriesga a consultarDarkWeb
        // debería cambiar solo el alumno 1

        int[] examen_0 = edr.estudiantes[0].estudiante().obtenerExamen();
        int[] examen_2 = edr.estudiantes[2].estudiante().obtenerExamen();

        edr.consultarDarkWeb(1, solucionDW);


        // ahora todos deberían tener el mismo examen menos el 1 
        assertTrue(Arrays.equals(edr.estudiantes[1].estudiante().obtenerExamen(), solucionDW));

        assertTrue(Arrays.equals(edr.estudiantes[0].estudiante().obtenerExamen(), examen_0));
        assertTrue(Arrays.equals(edr.estudiantes[2].estudiante().obtenerExamen(), examen_2));
    }


    @Test
    void darkweb_todos_respondieron_al_menos_1() {
        d_aula = 5;
        cant_alumnos = 3;
        int[] solucion = new int[]{0,1,2,3,4,5};
        int[] solucionDW = new int[]{1,2,2,2,4,5};
        
        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        // alumno 0 
        edr.resolver(0, 0, 0);
        edr.resolver(0, 1, 1);
        edr.resolver(0, 2, 2);
        
        // alumno 1
        edr.resolver(1, 0, 0);
        edr.resolver(1, 2, 1);
        edr.resolver(1, 3, 2);

        // alumno 2
        edr.resolver(2, 0, 0);
        edr.resolver(2, 3, 1);
        edr.resolver(2, 2, 2);

        // misma lógica que la anterior 
        int[] examen_0 = edr.estudiantes[0].estudiante().obtenerExamen();
        int[] examen_2 = edr.estudiantes[2].estudiante().obtenerExamen();

        edr.consultarDarkWeb(1, solucionDW);


        // ahora todos deberían tener el mismo examen menos el 1 
        assertTrue(Arrays.equals(edr.estudiantes[1].estudiante().obtenerExamen(), solucionDW));

        assertTrue(Arrays.equals(edr.estudiantes[0].estudiante().obtenerExamen(), examen_0));
        assertTrue(Arrays.equals(edr.estudiantes[2].estudiante().obtenerExamen(), examen_2));

    }
    
    @Test
    void darkweb_algunos_k_peores() {
        d_aula = 5;
        cant_alumnos = 3;
        int[] solucion = new int[]{0,1,2,3,4,5};
        int[] solucionDW = new int[]{1,2,2,2,4,5};
        
        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        // alumno 0 
        edr.resolver(0, 0, 0);
        edr.resolver(0, 1, 1);
        edr.resolver(0, 2, 2);
        
        // alumno 1
        edr.resolver(1, 0, 0);
        edr.resolver(1, 2, 1);
        edr.resolver(1, 3, 2);

        // alumno 2
        edr.resolver(2, 0, 0);
        edr.resolver(2, 3, 1);
        edr.resolver(2, 2, 2);

        // ahora se van a copiar el alumno 1 y 2
        int[] examen_0 = edr.estudiantes[0].estudiante().obtenerExamen();

        edr.consultarDarkWeb(2, solucionDW);

        // ahora todos deberían tener el mismo examen menos el 1 y 2
        assertTrue(Arrays.equals(edr.estudiantes[1].estudiante().obtenerExamen(), solucionDW));
        assertTrue(Arrays.equals(edr.estudiantes[2].estudiante().obtenerExamen(), solucionDW));

        assertTrue(Arrays.equals(edr.estudiantes[0].estudiante().obtenerExamen(), examen_0));

    }

    @Test
    void darkweb_alguno_entrego() {
        d_aula = 5;
        cant_alumnos = 4;
        int[] solucion = new int[]{0,1,2,3,4,5};
        int[] solucionDW = new int[]{1,2,2,2,4,5};
        
        Edr edr = new Edr(d_aula, cant_alumnos, solucion);
 
        // alumno 0 - 3 bien
        edr.resolver(0, 0, 0);
        edr.resolver(0, 1, 1);
        edr.resolver(0, 2, 2);
        
        // alumno 1 - 1 bien
        edr.resolver(1, 0, 0);
        edr.resolver(1, 2, 1);
        edr.resolver(1, 3, 2);

        // alumno 2 - 2 bien - entrega
        edr.resolver(2, 0, 0);
        edr.resolver(2, 3, 1);
        edr.resolver(2, 2, 2);

        // alumno 3 - 3 bien
        edr.resolver(3, 0, 0);
        edr.resolver(3, 3, 3);
        edr.resolver(3, 2, 2);

        // guardo los que no deberían cambiar 
        int[] examen_3 = edr.estudiantes[3].estudiante().obtenerExamen();
        int[] examen_2 = edr.estudiantes[2].estudiante().obtenerExamen();

        edr.entregar(2);

        edr.consultarDarkWeb(2, solucionDW);

        // ahora todos deberían tener el mismo examen menos el 1 y 2
        assertTrue(Arrays.equals(edr.estudiantes[1].estudiante().obtenerExamen(), solucionDW));
        assertTrue(Arrays.equals(edr.estudiantes[0].estudiante().obtenerExamen(), solucionDW));

        assertTrue(Arrays.equals(edr.estudiantes[2].estudiante().obtenerExamen(), examen_2));
        assertTrue(Arrays.equals(edr.estudiantes[3].estudiante().obtenerExamen(), examen_3));

    }



    @Test
    void peor_nota_entrego() {
        d_aula = 5;
        cant_alumnos = 6; // 3 van a tener muy buena nota pero no entregar, 3 van a tener peor nota pero entregar
        int[] solucion = new int[]{0,1,2,3};
        int[] solucionDW = new int[]{1,2,2,2};
        
        Edr edr = new Edr(d_aula, cant_alumnos, solucion);
 
        // alumno 0 ---- buena nota (no entrega)
        edr.resolver(0, 0, 0);
        edr.resolver(0, 1, 1);
        edr.resolver(0, 2, 2);
        edr.resolver(0, 3, 3);
        
        // alumno 1 ---- mas o menos (entrega)
        edr.resolver(1, 0, 0);
        edr.resolver(1, 1, 0);
        edr.resolver(1, 2, 0);

        edr.entregar(1);

        // alumno 2 - 2 buena nota (no entrega)
        edr.resolver(2, 0, 0);
        edr.resolver(2, 1, 1);
        edr.resolver(2, 2, 2);
        edr.resolver(2, 3, 0);

        // alumno 3 - bien (no entrega)
        edr.resolver(3, 0, 0);
        edr.resolver(3, 1, 3);
        edr.resolver(3, 2, 2);
        edr.resolver(3, 3, 3);

        // alumno 4 - mal (entrega)
        edr.resolver(4,3,0);

        edr.entregar(4);

        // alumno 5 - mal (entrega) 
        edr.resolver(5,2,3);

        edr.entregar(5);

        // no deberían cambiar los examenes del 1, 4 y 5 
        int[] examen_1 = edr.estudiantes[1].estudiante().obtenerExamen();
        int[] examen_4 = edr.estudiantes[4].estudiante().obtenerExamen();
        int[] examen_5 = edr.estudiantes[5].estudiante().obtenerExamen();

        
        edr.consultarDarkWeb(3, solucionDW);

        // deberian haber cambiando los examenes de los alumnos 0, 2 y 3
        assertTrue(Arrays.equals(edr.estudiantes[0].estudiante().obtenerExamen(), solucionDW));
        assertTrue(Arrays.equals(edr.estudiantes[2].estudiante().obtenerExamen(), solucionDW));
        assertTrue(Arrays.equals(edr.estudiantes[3].estudiante().obtenerExamen(), solucionDW));

        assertTrue(Arrays.equals(edr.estudiantes[1].estudiante().obtenerExamen(), examen_1));
        assertTrue(Arrays.equals(edr.estudiantes[4].estudiante().obtenerExamen(), examen_4));
        assertTrue(Arrays.equals(edr.estudiantes[5].estudiante().obtenerExamen(), examen_5));
    }

    @Test
    void darkweb_triple_empate() {
        d_aula = 5;
        cant_alumnos = 3; 
        int[] solucion = new int[]{0,1,2,3};
        int[] solucionDW = new int[]{1,2,2,2};
        
        Edr edr = new Edr(d_aula, cant_alumnos, solucion);
        
        edr.resolver(0, 1, 1);

        edr.consultarDarkWeb(2, solucionDW);

        assertTrue(Arrays.equals(edr.estudiantes[1].estudiante().obtenerExamen(), solucionDW));
        assertTrue(Arrays.equals(edr.estudiantes[2].estudiante().obtenerExamen(), solucionDW));

    }

    // -------------------------------------------corregir() + chequearCopia() ------------------------------------------

    @Test
    void corregir_sin_copiones_y_con_desempate() { 
        d_aula = 5;
        cant_alumnos = 4;
        int[] solucion = new int[]{0,1,2,3};
        

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        double[] notasEsperadas = {50.0,50.0,25.0,25.0};
        int[] estudiantesEsperados = {3,0,2,1};

        // alumno_0 debería tener 50 puntos 
        edr.resolver(0,3,3);
        edr.resolver(0,2,2);
        edr.entregar(0);

        // alumno_1 debería tener 25 puntos
        edr.resolver(1,1,2);
        edr.resolver(1,2,2);
        edr.entregar(1);

        // alumno_2 debería tener 25 puntos
        edr.resolver(2,1,0);
        edr.resolver(2,2,2);
        edr.entregar(2);

        // alumno_3 debería tener 50 puntos
        edr.resolver(3,1,1);
        edr.resolver(3,2,2);
        edr.entregar(3);

        // por especificación 
        int[] copiones = edr.chequearCopias();

        NotaFinal [] notas_alumnos = edr.corregir();

        double [] notasCorregir = new double[4];
        int [] alumnos = new int[4];

        int i = 0;

        while(i < notas_alumnos.length){
            notasCorregir[i] = notas_alumnos[i]._nota;
            alumnos[i] = notas_alumnos[i]._id;
            i++;
        }

        assertTrue(Arrays.equals(notasEsperadas, notasCorregir));
        assertTrue(Arrays.equals(estudiantesEsperados, alumnos));
        assertEquals(copiones.length, 0);
    }

    // ------------------- Handle (test que nos pediste 😝)
    @Test
    void handle_tiene_aliasing() {
        d_aula = 5;
        cant_alumnos = 4;
        solucion = new int[]{0,1,2,3};

        Edr edr = new Edr(d_aula, cant_alumnos, solucion);

        double notaAntes = edr.estudiantes[1].estudiante().obtenerNota();
        assertEquals(0.0, notaAntes);

        edr.resolver(1, 0, 0);

        double notaDespues = edr.estudiantes[1].estudiante().obtenerNota();

        assertTrue(notaDespues > notaAntes);

        Estudiante estudianteEnHeap = edr.estudiantes[1].estudiante();
        assertSame(edr.estudiantes[1].estudiante(), estudianteEnHeap);
        
        assertEquals(notaDespues, edr.estudiantes[1].estudiante().obtenerNota());
        
        assertTrue(edr.estudiantes[1].posicion() >= 0);
        assertTrue(edr.estudiantes[1].posicion() < cant_alumnos);
    }
}