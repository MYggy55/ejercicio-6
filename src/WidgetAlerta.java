public final class WidgetAlerta extends Widget implements Interactivo {
    private final String mensaje;
    private final String nivel;
    private boolean atendida;

    public WidgetAlerta(int id, String nombre, int memoriaKB, String mensaje, String nivel) {
        super(id, nombre, memoriaKB);
        this.mensaje = validarTexto(mensaje, "Mensaje");
        this.nivel = validarTexto(nivel, "Nivel");
    }

    @Override
    public String renderizar() {
        return "[ALERTA " + nivel + "] " + mensaje + " | " + (atendida ? "Atendida" : "Pendiente");
    }

    @Override
    public String hacerClic() {
        if (atendida) return "La alerta ya fue atendida.";
        atendida = true;
        return "Alerta atendida: " + mensaje;
    }

    @Override
    protected String configuracion() {
        return "Nivel: " + nivel + " | Mensaje: " + mensaje + " | Atendida: " + atendida;
    }
}
