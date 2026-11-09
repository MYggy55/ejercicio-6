import java.util.Objects;

public abstract class Widget implements Renderizable, Comparable<Widget> {
    private final int id;
    private final String nombre;
    private final int memoriaKB;
    private boolean activo;

    protected Widget(int id, String nombre, int memoriaKB) {
        if (id <= 0 || memoriaKB <= 0) {
            throw new IllegalArgumentException("ID y memoria deben ser mayores que cero.");
        }
        this.id = id;
        this.nombre = validarTexto(nombre, "Nombre");
        this.memoriaKB = memoriaKB;
        this.activo = true;
    }

    protected static String validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return texto.trim();
    }

    protected abstract String configuracion();

    @Override
    public final int compareTo(Widget otro) {
        Objects.requireNonNull(otro, "El widget no puede ser null.");
        int comparacion = Integer.compare(memoriaKB, otro.memoriaKB);
        return comparacion == 0 ? Integer.compare(id, otro.id) : comparacion;
    }

    @Override
    public final String toString() {
        return "ID " + id + " | " + nombre + " | " + getClass().getSimpleName()
            + " | " + memoriaKB + " KB | " + (activo ? "Activo" : "Inactivo")
            + " | Clic: " + (this instanceof Interactivo ? "sí" : "no")
            + " | Actualización: " + (this instanceof Actualizable ? "sí" : "no")
            + " | " + configuracion();
    }

    public final int getId() { return id; }
    public final String getNombre() { return nombre; }
    public final int getMemoriaKB() { return memoriaKB; }
    public final boolean isActivo() { return activo; }
    public final void setActivo(boolean activo) { this.activo = activo; }
}
