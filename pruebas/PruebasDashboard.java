import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

public class PruebasDashboard {
    private static final StringBuilder evidencia = new StringBuilder();
    private static int cantidad;

    private static void verificar(String caso, Object esperado, Object obtenido) {
        if (!esperado.equals(obtenido)) {
            throw new AssertionError(caso + ": esperado=" + esperado + ", obtenido=" + obtenido);
        }
        cantidad++;
        evidencia.append("OK | ").append(caso).append(" | esperado=").append(esperado)
            .append(" | obtenido=").append(obtenido).append("\n");
    }

    private static void rechazar(String caso, Class<? extends RuntimeException> tipo, Runnable accion) {
        String obtenido = "Sin excepción";
        try {
            accion.run();
        } catch (RuntimeException e) {
            obtenido = e.getClass().getSimpleName();
        }
        verificar(caso, tipo.getSimpleName(), obtenido);
    }

    private static final class WidgetNuevo extends Widget {
        private WidgetNuevo(int id) { super(id, "Widget futuro", 42); }
        @Override
        public String renderizar() { return "[FUTURO] Compatible sin cambios en Dashboard"; }
        @Override
        protected String configuracion() { return "Extensión de prueba"; }
    }

    public static void main(String[] args) throws Exception {
        Dashboard dashboard = new Dashboard();
        Controlador controlador = new Controlador(dashboard);
        verificar("Listado vacío", "No hay widgets para mostrar.", dashboard.listar(true));
        verificar("Render vacío", "No hay widgets activos.", dashboard.renderizarActivos());
        verificar("Actualización vacía", true, dashboard.actualizarActivos().startsWith("Widgets actualizados: 0"));
        Catalogo.cargar(dashboard);
        verificar("Carga de catálogo", 12, dashboard.getCantidad());
        verificar("Activos iniciales", 12L, dashboard.listar(true).lines().count());
        verificar("Búsqueda por ID", "Bienvenida", dashboard.buscar(1).getNombre());
        verificar("Búsqueda por nombre", 1, dashboard.buscar(" bienvenida ").getId());
        verificar("Configuración por ID", true, controlador.buscar(3).contains("Ciudad: Guatemala"));
        verificar("Configuración por nombre", true, controlador.buscar("CLIMA GUATEMALA").contains("Temperatura"));
        verificar("Render de texto", true, dashboard.renderizarActivos().contains("[TEXTO]"));
        verificar("Render de clima", true, dashboard.renderizarActivos().contains("[CLIMA SIMULADO]"));
        verificar("Render de tareas", true, dashboard.renderizarActivos().contains("[TAREAS]"));
        verificar("Render de alerta", true, dashboard.renderizarActivos().contains("[ALERTA"));
        verificar("Render de reloj", true, dashboard.renderizarActivos().contains("[RELOJ"));
        String textoAntes = dashboard.buscar(1).toString();
        verificar("Clic estático seguro", true, dashboard.hacerClic(1).contains("no admite clics"));
        verificar("Estático no cambia", textoAntes, dashboard.buscar(1).toString());
        verificar("Clic de tarea", "Tarea completada: Revisar tickets", dashboard.hacerClic(5));
        verificar("Tarea completada visible", true, dashboard.buscar(5).renderizar().contains("[x] Revisar tickets"));
        dashboard.hacerClic(5);
        dashboard.hacerClic(5);
        verificar("Tareas agotadas", "No quedan tareas pendientes.", dashboard.hacerClic(5));
        verificar("Atender alerta", true, dashboard.hacerClic(7).startsWith("Alerta atendida:"));
        verificar("Alerta idempotente", "La alerta ya fue atendida.", dashboard.hacerClic(7));
        verificar("Actualizables iniciales", true, dashboard.actualizarActivos().startsWith("Widgets actualizados: 4"));
        verificar("Clima actualizado", true, dashboard.buscar(3).renderizar().contains("22.5 °C"));
        dashboard.cambiarEstado(3, false);
        verificar("Activo desactivado", false, dashboard.buscar(3).isActivo());
        verificar("Inactivo excluido del listado", false, dashboard.listar(true).contains("Clima Guatemala"));
        verificar("Inactivo excluido del render", false, dashboard.renderizarActivos().contains("Guatemala"));
        verificar("Búsqueda incluye inactivos", true, controlador.buscar(3).contains("Inactivo"));
        verificar("Inactivo no se actualiza", true, dashboard.actualizarActivos().startsWith("Widgets actualizados: 3"));
        verificar("Temperatura inactiva intacta", true, dashboard.buscar(3).renderizar().contains("22.5 °C"));
        dashboard.cambiarEstado(5, false);
        rechazar("Clic en inactivo", IllegalStateException.class, () -> dashboard.hacerClic(5));
        dashboard.cambiarEstado(3, true);
        verificar("Reactivar widget", true, dashboard.buscar(3).isActivo());
        dashboard.ordenarPorMemoria();
        String orden = dashboard.listar(false);
        verificar("Orden inicial por memoria", true, orden.startsWith("ID 1 |"));
        verificar("Desempate por ID", true, orden.indexOf("ID 1 |") < orden.indexOf("ID 11 |"));
        verificar("Orden completo", true, orden.indexOf("ID 9 |") < orden.indexOf("ID 4 |")
            && orden.indexOf("ID 4 |") < orden.indexOf("ID 3 |"));
        verificar("Comparable ascendente", true, dashboard.buscar(1).compareTo(dashboard.buscar(3)) < 0);
        verificar("Comparable reflexivo", 0, dashboard.buscar(1).compareTo(dashboard.buscar(1)));
        Renderizable externo = () -> "Render de interfaz ajena al catálogo";
        verificar("Parámetro por interfaz", true, controlador.renderizar(externo).contains("interfaz ajena"));
        verificar("Sobrecarga con encabezado", "Título\nRender de interfaz ajena al catálogo", controlador.renderizar(externo,"Título"));
        Clock fijo = Clock.fixed(Instant.parse("2026-11-08T12:34:56Z"),ZoneOffset.UTC);
        WidgetReloj reloj = new WidgetReloj(40,"Reloj prueba",10,fijo);
        verificar("Constructor sobrecargado con Clock", true, reloj.renderizar().contains("12:34:56"));
        verificar("Actualización de reloj", true, reloj.actualizar().contains("12:34:56"));
        dashboard.agregar(new WidgetNuevo(99));
        verificar("Nueva subclase renderizada", true, dashboard.renderizarActivos().contains("[FUTURO]"));
        verificar("Catálogo ampliado", 13, dashboard.getCantidad());
        rechazar("ID duplicado", IllegalArgumentException.class, () -> dashboard.agregar(new WidgetTexto(1,"Otro",10,"x")));
        rechazar("Nombre duplicado", IllegalArgumentException.class, () -> dashboard.agregar(new WidgetTexto(100,"BIENVENIDA",10,"x")));
        verificar("Rechazos conservan cantidad", 13, dashboard.getCantidad());
        rechazar("Búsqueda ID inexistente", IllegalArgumentException.class, () -> dashboard.buscar(200));
        rechazar("Búsqueda nombre inexistente", IllegalArgumentException.class, () -> dashboard.buscar("No existe"));
        rechazar("Nombre vacío", IllegalArgumentException.class, () -> dashboard.buscar(" "));
        rechazar("Widget null", IllegalArgumentException.class, () -> dashboard.agregar(null));
        rechazar("ID inválido", IllegalArgumentException.class, () -> new WidgetTexto(0,"X",10,"x"));
        rechazar("Memoria inválida", IllegalArgumentException.class, () -> new WidgetTexto(100,"X",0,"x"));
        rechazar("Tareas vacías", IllegalArgumentException.class, () -> new WidgetTareas(100,"X",10,new String[0]));
        rechazar("Temperatura NaN", IllegalArgumentException.class, () -> new WidgetClima(100,"X",10,"Ciudad",Double.NaN));
        rechazar("Render null", IllegalArgumentException.class, () -> controlador.renderizar(null));
        evidencia.append("Total de verificaciones: ").append(cantidad).append("\nTodas las pruebas pasaron.\n");
        Files.writeString(Path.of(args.length > 0 ? args[0] : "Resultados_pruebas.txt"),evidencia,StandardCharsets.UTF_8);
    }
}
