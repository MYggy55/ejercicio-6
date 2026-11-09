public final class WidgetTexto extends Widget {
    private final String contenido;

    public WidgetTexto(int id, String nombre, int memoriaKB, String contenido) {
        super(id, nombre, memoriaKB);
        this.contenido = validarTexto(contenido, "Contenido");
    }

    @Override
    public String renderizar() { return "[TEXTO] " + getNombre() + ": " + contenido; }

    @Override
    protected String configuracion() { return "Contenido: " + contenido; }
}
