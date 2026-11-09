import java.util.NoSuchElementException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Dashboard dashboard = new Dashboard();
        Catalogo.cargar(dashboard);
        Controlador controlador = new Controlador(dashboard);
        try (Scanner entrada = new Scanner(System.in)) {
            System.out.println("DASHBOARD MODULAR | MVC E INTERFACES");
            System.out.println("Catálogo inicial: " + dashboard.getCantidad() + " widgets activos.");
            System.out.println("Clima simulado; relojes UTC. Actualización manual desde el menú.");
            boolean continuar = true;
            while (continuar) {
                mostrarMenu();
                int opcion = leerEntero(entrada, "Opción: ");
                try {
                    switch (opcion) {
                        case 1 -> System.out.println(controlador.listarActivos());
                        case 2 -> System.out.println(controlador.buscar(leerEntero(entrada, "ID: ")));
                        case 3 -> System.out.println(controlador.buscar(leerTexto(entrada, "Nombre exacto: ")));
                        case 4 -> System.out.println(controlador.ordenarPorMemoria());
                        case 5 -> System.out.println(controlador.renderizarActivos());
                        case 6 -> System.out.println(controlador.hacerClic(leerEntero(entrada, "ID: ")));
                        case 7 -> System.out.println(controlador.actualizarActivos());
                        case 8 -> {
                            int id = leerEntero(entrada, "ID: ");
                            int estado = leerEntero(entrada, "1 = activar, 0 = desactivar: ");
                            if (estado != 0 && estado != 1) throw new IllegalArgumentException("Estado inválido.");
                            System.out.println(controlador.cambiarEstado(id, estado == 1));
                        }
                        case 9 -> continuar = false;
                        default -> System.out.println("Selecciona una opción entre 1 y 9.");
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
            System.out.println("Programa finalizado.");
        } catch (NoSuchElementException e) {
            System.out.println("Entrada finalizada. Programa cerrado.");
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n1. Listar widgets activos\n2. Buscar por ID\n3. Buscar por nombre"
            + "\n4. Ordenar por memoria (KB)\n5. Renderizar activos\n6. Hacer clic"
            + "\n7. Actualizar activos\n8. Activar o desactivar\n9. Salir");
    }

    private static String leerTexto(Scanner entrada, String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    private static int leerEntero(Scanner entrada, String mensaje) {
        Integer valor = null;
        while (valor == null) {
            try {
                valor = Integer.valueOf(leerTexto(entrada, mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida: escribe un entero dentro del rango de int.");
            }
        }
        return valor;
    }
}
