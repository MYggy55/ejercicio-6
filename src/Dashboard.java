import java.util.ArrayList;
import java.util.Collections;

public final class Dashboard {
    private final ArrayList<Widget> widgets;

    public Dashboard() { widgets = new ArrayList<>(); }

    public void agregar(Widget widget) {
        if (widget == null) throw new IllegalArgumentException("El widget no puede ser null.");
        for (Widget registrado : widgets) {
            if (registrado.getId() == widget.getId()) throw new IllegalArgumentException("ID repetido.");
            if (registrado.getNombre().equalsIgnoreCase(widget.getNombre())) {
                throw new IllegalArgumentException("Nombre repetido.");
            }
        }
        widgets.add(widget);
    }

    public Widget buscar(int id) {
        for (Widget widget : widgets) if (widget.getId() == id) return widget;
        throw new IllegalArgumentException("No existe un widget con ese ID.");
    }

    public Widget buscar(String nombre) {
        String buscado = Widget.validarTexto(nombre, "Nombre");
        for (Widget widget : widgets) {
            if (widget.getNombre().equalsIgnoreCase(buscado)) return widget;
        }
        throw new IllegalArgumentException("No existe un widget con ese nombre.");
    }

    public String listar(boolean soloActivos) {
        StringBuilder texto = new StringBuilder();
        for (Widget widget : widgets) {
            if (!soloActivos || widget.isActivo()) texto.append(widget).append("\n");
        }
        return texto.length() == 0 ? "No hay widgets para mostrar." : texto.toString();
    }

    public String renderizarActivos() {
        StringBuilder texto = new StringBuilder();
        for (Widget widget : widgets) {
            if (widget.isActivo()) texto.append(widget.renderizar()).append("\n");
        }
        return texto.length() == 0 ? "No hay widgets activos." : texto.toString();
    }

    public void ordenarPorMemoria() { Collections.sort(widgets); }

    public String hacerClic(int id) {
        Widget widget = buscar(id);
        if (!widget.isActivo()) throw new IllegalStateException("El widget está inactivo.");
        if (widget instanceof Interactivo interactivo) return interactivo.hacerClic();
        return "Este widget no admite clics; su estado se conserva.";
    }

    public String actualizarActivos() {
        StringBuilder texto = new StringBuilder();
        int actualizados = 0;
        for (Widget widget : widgets) {
            if (widget.isActivo() && widget instanceof Actualizable actualizable) {
                texto.append(actualizable.actualizar()).append("\n");
                actualizados++;
            }
        }
        return "Widgets actualizados: " + actualizados + "\n" + texto;
    }

    public void cambiarEstado(int id, boolean activo) { buscar(id).setActivo(activo); }
    public int getCantidad() { return widgets.size(); }
}
