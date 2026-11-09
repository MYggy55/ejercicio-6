import java.time.Clock;
import java.time.LocalTime;
import java.util.Objects;

public final class WidgetReloj extends Widget implements Actualizable {
    private final Clock reloj;
    private LocalTime hora;

    public WidgetReloj(int id, String nombre, int memoriaKB) {
        this(id, nombre, memoriaKB, Clock.systemUTC());
    }

    public WidgetReloj(int id, String nombre, int memoriaKB, Clock reloj) {
        super(id, nombre, memoriaKB);
        this.reloj = Objects.requireNonNull(reloj, "El reloj no puede ser null.");
        this.hora = LocalTime.now(reloj).withNano(0);
    }

    @Override
    public String renderizar() {
        return "[RELOJ " + reloj.getZone() + "] " + getNombre() + ": " + hora;
    }

    @Override
    public String actualizar() {
        hora = LocalTime.now(reloj).withNano(0);
        return renderizar();
    }

    @Override
    protected String configuracion() {
        return "Zona: " + reloj.getZone() + " | Última lectura: " + hora;
    }
}
