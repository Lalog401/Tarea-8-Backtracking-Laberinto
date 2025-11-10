import java.io.*;
import java.util.*;

public class LaberintoBacktracking {

    static class Posicion {
        int fila, columna;
        public Posicion(int f, int c) {
            fila = f;
            columna = c;
        }
        @Override
        public String toString() {
            // Se suma 1 para mostrar las coordenadas desde (1,1)
            return "(" + (fila + 1) + "," + (columna + 1) + ")";
        }
    }

    static int filas, columnas;
    static char[][] laberinto;
    static boolean[][] visitado;
    static Stack<Posicion> camino = new Stack<>();

    public static void main(String[] args) {
        try {
            Scanner entrada = new Scanner(System.in);
            System.out.print("Escribe el nombre del archivo del laberinto (ejemplo: laberinto.txt): ");
            String nombreArchivo = entrada.nextLine().trim();
            entrada.close();

            // Leer el archivo con el laberinto
            File archivo = new File(nombreArchivo);
            Scanner sc = new Scanner(archivo, "UTF-8");

            // Limpia cualquier carácter extraño antes de convertir
            filas = Integer.parseInt(sc.nextLine().trim().replaceAll("[^0-9]", ""));
            columnas = Integer.parseInt(sc.nextLine().trim().replaceAll("[^0-9]", ""));
            laberinto = new char[filas][columnas];
            visitado = new boolean[filas][columnas];

            Posicion inicio = null, salida = null;

            // Leer el laberinto línea por línea (separado por comas)
            for (int i = 0; i < filas; i++) {
                String[] linea = sc.nextLine().trim().split(",");
                for (int j = 0; j < columnas; j++) {
                    laberinto[i][j] = linea[j].charAt(0);
                    if (laberinto[i][j] == 'E')
                        inicio = new Posicion(i, j);
                    else if (laberinto[i][j] == 'S')
                        salida = new Posicion(i, j);
                }
            }
            sc.close();

            if (inicio == null || salida == null) {
                System.out.println("Error: No se encontró la entrada (E) o la salida (S).");
                return;
            }

            System.out.println("\nLaberinto original:\n");
            imprimirLaberinto();

            // Resolver con backtracking
            if (resolver(inicio.fila, inicio.columna)) {
                System.out.println("\n¡Ruta encontrada!\n");
                marcarCamino();
                imprimirLaberinto();
                imprimirCamino();
            } else {
                System.out.println("\nNo existe ruta posible desde E hasta S.");
            }

        } catch (Exception e) {
            System.out.println("Error al leer o procesar el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static boolean resolver(int f, int c) {
        // Comprobaciones de límites y obstáculos
        if (f < 0 || f >= filas || c < 0 || c >= columnas)
            return false;
        if (laberinto[f][c] == '1' || visitado[f][c])
            return false;

        // Guardar y marcar posición
        visitado[f][c] = true;
        camino.push(new Posicion(f, c));

        // Comprobar si llegamos a la salida
        if (laberinto[f][c] == 'S')
            return true;

        // Movimientos posibles (arriba, abajo, izquierda, derecha)
        int[] df = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            if (resolver(f + df[i], c + dc[i]))
                return true;
        }

        // Retroceder si no hay camino
        camino.pop();
        return false;
    }

    static void marcarCamino() {
        // Marca el camino encontrado en el laberinto con '*'
        for (Posicion p : camino) {
            if (laberinto[p.fila][p.columna] == '0')
                laberinto[p.fila][p.columna] = '*';
        }
    }

    static void imprimirLaberinto() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(laberinto[i][j] + " ");
            }
            System.out.println();
        }
    }

    static void imprimirCamino() {
        System.out.println("\nCoordenadas de la ruta (desde la entrada hasta la salida):");
        for (Posicion p : camino) {
            System.out.print(p + " ");
        }
        System.out.println();
    }
}
