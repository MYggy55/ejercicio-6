public final class Controlador {
    private final Dashboard dashboard;

    public Controlador(Dashboard dashboard) {
        if (dashboard == null) throw new IllegalArgumentException("El dashboard no puede ser null.");
        this.dashboard = dashboard;
    }

    public String listarActivos() { return dashboard.listar(true); }

    public String buscar(int id) { return describir(dashboard.buscar(id)); }
    public String buscar(String nombre) { return describir(dashboard.buscar(nombre)); }

    private String describir(Widget widget) {
        return widget.isActivo() ? renderizar(widget, widget.toString()) : widget.toString();
    }

    public String ordenarPorMemoria() {
        dashboard.ordenarPorMemoria();
        return "Inventario ordenado por memoria ascendente:\n" + dashboard.listar(false);
    }

    public String renderizarActivos() { return dashboard.renderizarActivos(); }
    public String hacerClic(int id) { return dashboard.hacerClic(id); }
    public String actualizarActivos() { return dashboard.actualizarActivos(); }

    public String cambiarEstado(int id, boolean activo) {
        dashboard.cambiarEstado(id, activo);
        return "Widget " + id + (activo ? " activado." : " desactivado.");
    }

    public String renderizar(Renderizable elemento) {
        return renderizar(elemento, "Vista del widget");
    }

    public String renderizar(Renderizable elemento, String encabezado) {
        if (elemento == null) throw new IllegalArgumentException("El elemento no puede ser null.");
        return encabezado + "\n" + elemento.renderizar();
    }
}
