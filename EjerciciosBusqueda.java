import java.util.Arrays;
import java.util.Scanner;

public class EjerciciosBusqueda {

    // 1. BUSQUEDA LINEAL
    // Encuentra el primer ticket critico.
    // Complejidad: O(n)

    static class Ticket {
        int id;
        boolean critico;

        Ticket(int id, boolean critico) {
            this.id = id;
            this.critico = critico;
        }

        @Override
        public String toString() {
            return "Ticket{id=" + id + ", critico=" + critico + "}";
        }
    }

    public static Ticket primerTicketCritico(Ticket[] tickets) {
        for (Ticket ticket : tickets) {
            if (ticket.critico) {
                return ticket;
            }
        }
        return null;
    }

    // 2. BUSQUEDA BINARIA - LOWER BOUND
    // Encuentra la primera posicion de insercion.
    // Complejidad: O(log n)

    public static int lowerBound(int[] precios, int objetivo) {
        int izquierda = 0;
        int derecha = precios.length;

        while (izquierda < derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;

            if (precios[medio] < objetivo) {
                izquierda = medio + 1;
            } else {
                derecha = medio;
            }
        }

        return izquierda;
    }

    // 3. BUSQUEDA POR SALTOS
    // Devuelve el indice y las comparaciones realizadas.
    // Complejidad: O(sqrt(n))

    static class Resultado {
        int indice;
        int comparaciones;

        Resultado(int indice, int comparaciones) {
            this.indice = indice;
            this.comparaciones = comparaciones;
        }

        @Override
        public String toString() {
            return "Indice: " + indice
                    + ", comparaciones: " + comparaciones;
        }
    }

    public static Resultado busquedaSaltos(
            int[] datos, int objetivo) {

        int n = datos.length;
        int comparaciones = 0;

        if (n == 0) {
            return new Resultado(-1, 0);
        }

        int salto = Math.max(1, (int) Math.sqrt(n));
        int inicio = 0;
        int fin;

        while (inicio < n) {
            fin = Math.min(inicio + salto, n);
            comparaciones++;

            if (datos[fin - 1] >= objetivo) {
                break;
            }

            inicio = fin;
        }

        if (inicio >= n) {
            return new Resultado(-1, comparaciones);
        }

        fin = Math.min(inicio + salto, n);

        for (int i = inicio; i < fin; i++) {
            comparaciones++;

            if (datos[i] == objetivo) {
                return new Resultado(i, comparaciones);
            }

            if (datos[i] > objetivo) {
                break;
            }
        }

        return new Resultado(-1, comparaciones);
    }

    // 4. BUSQUEDA POR INTERPOLACION
    // Promedio: O(log log n). Peor caso: O(n).

    public static int busquedaInterpolacion(
            int[] datos, int objetivo) {

        int izquierda = 0;
        int derecha = datos.length - 1;

        while (izquierda <= derecha
                && objetivo >= datos[izquierda]
                && objetivo <= datos[derecha]) {

            if (datos[izquierda] == datos[derecha]) {
                return datos[izquierda] == objetivo
                        ? izquierda : -1;
            }

            long numerador =
                    ((long) objetivo - datos[izquierda])
                    * (derecha - izquierda);

            int posicion = izquierda + (int) (
                    numerador /
                    ((long) datos[derecha] - datos[izquierda])
            );

            if (datos[posicion] == objetivo) {
                while (posicion > 0
                        && datos[posicion - 1] == objetivo) {
                    posicion--;
                }
                return posicion;
            }

            if (datos[posicion] < objetivo) {
                izquierda = posicion + 1;
            } else {
                derecha = posicion - 1;
            }
        }

        return -1;
    }

    // 5. BUSQUEDA EXPONENCIAL
    // Duplica el rango y luego utiliza busqueda binaria.
    // Complejidad: O(log n)

    private static int busquedaBinaria(
            int[] datos, int objetivo,
            int izquierda, int derecha) {

        int resultado = -1;

        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;

            if (datos[medio] == objetivo) {
                resultado = medio;
                derecha = medio - 1;
            } else if (datos[medio] < objetivo) {
                izquierda = medio + 1;
            } else {
                derecha = medio - 1;
            }
        }

        return resultado;
    }

    public static int busquedaExponencial(
            int[] datos, int objetivo) {

        int n = datos.length;

        if (n == 0) {
            return -1;
        }

        if (datos[0] == objetivo) {
            return 0;
        }

        int limite = 1;

        while (limite < n && datos[limite] < objetivo) {
            limite *= 2;
        }

        int izquierda = limite / 2;
        int derecha = Math.min(limite, n - 1);

        return busquedaBinaria(
                datos, objetivo, izquierda, derecha);
    }

    // MENU PRINCIPAL

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        do {
            System.out.println("\n================================");
            System.out.println("    EJERCICIOS DE BUSQUEDA");
            System.out.println("================================");
            System.out.println("1. Busqueda lineal");
            System.out.println("2. Lower Bound");
            System.out.println("3. Busqueda por saltos");
            System.out.println("4. Busqueda por interpolacion");
            System.out.println("5. Busqueda exponencial");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opcion: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Error: debes ingresar un numero.");
                scanner.next();
                continue;
            }

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    probarBusquedaLineal();
                    break;
                case 2:
                    probarLowerBound();
                    break;
                case 3:
                    probarBusquedaSaltos();
                    break;
                case 4:
                    probarInterpolacion();
                    break;
                case 5:
                    probarExponencial();
                    break;
                case 0:
                    System.out.println("Programa finalizado.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }

        } while (opcion != 0);

        scanner.close();
    }

    // PRUEBAS DEL EJERCICIO 1

    private static void probarBusquedaLineal() {
        Ticket[] tickets = {
            new Ticket(1, false),
            new Ticket(2, true),
            new Ticket(3, true)
        };

        System.out.println("\n--- BUSQUEDA LINEAL ---");
        System.out.println("Primer ticket critico: "
                + primerTicketCritico(tickets));
        System.out.println("Lista vacia: "
                + primerTicketCritico(new Ticket[0]));
    }

    // PRUEBAS DEL EJERCICIO 2

    private static void probarLowerBound() {
        int[] precios = {10, 20, 20, 30, 40};
        int objetivo = 20;

        int posicion = lowerBound(precios, objetivo);
        int[] nuevos = new int[precios.length + 1];

        System.arraycopy(precios, 0, nuevos, 0, posicion);
        nuevos[posicion] = objetivo;

        System.arraycopy(
                precios, posicion,
                nuevos, posicion + 1,
                precios.length - posicion
        );

        System.out.println("\n--- LOWER BOUND ---");
        System.out.println("Posicion de insercion: " + posicion);
        System.out.println("Precios: " + Arrays.toString(nuevos));
        System.out.println("Lista vacia: "
                + lowerBound(new int[0], 15));
    }

    // PRUEBAS DEL EJERCICIO 3

    private static void probarBusquedaSaltos() {
        int[] datos = {3, 7, 11, 15, 19, 23, 27, 31, 35};

        System.out.println("\n--- BUSQUEDA POR SALTOS ---");
        System.out.println("Buscar 23: "
                + busquedaSaltos(datos, 23));
        System.out.println("Buscar 100: "
                + busquedaSaltos(datos, 100));
        System.out.println("Lista vacia: "
                + busquedaSaltos(new int[0], 10));
    }

    // PRUEBAS DEL EJERCICIO 4

    private static void probarInterpolacion() {
        int[] ids = {10, 20, 30, 30, 30, 40, 50};

        System.out.println("\n--- BUSQUEDA POR INTERPOLACION ---");
        System.out.println("Buscar 30: "
                + busquedaInterpolacion(ids, 30));
        System.out.println("Buscar 99: "
                + busquedaInterpolacion(ids, 99));
        System.out.println("Lista vacia: "
                + busquedaInterpolacion(new int[0], 10));
        System.out.println("Todos iguales: "
                + busquedaInterpolacion(new int[]{5, 5, 5}, 5));
    }

    // PRUEBAS DEL EJERCICIO 5

    private static void probarExponencial() {
        int[] eventos = {2, 5, 8, 12, 17, 23, 31, 42, 56, 70};

        System.out.println("\n--- BUSQUEDA EXPONENCIAL ---");
        System.out.println("Buscar 42: "
                + busquedaExponencial(eventos, 42));
        System.out.println("Buscar 2: "
                + busquedaExponencial(eventos, 2));
        System.out.println("Buscar 99: "
                + busquedaExponencial(eventos, 99));
        System.out.println("Lista vacia: "
                + busquedaExponencial(new int[0], 10));
    }
}
