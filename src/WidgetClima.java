public final class WidgetClima extends Widget implements Actualizable {
    private final String ciudad;
    private double temperatura;
    private int actualizaciones;

    public WidgetClima(int id, String nombre, int memoriaKB, String ciudad, double temperatura) {
        super(id, nombre, memoriaKB);
        this.ciudad = validarTexto(ciudad, "Ciudad");
        if (!Double.isFinite(temperatura) || temperatura < -100 || temperatura > 100) {
            throw new IllegalArgumentException("La temperatura simulada debe estar entre -100 y 100.");
        }
        this.temperatura = temperatura;
    }

    @Override
    public String renderizar() {
        return "[CLIMA SIMULADO] " + ciudad + ": " + temperatura + " °C";
    }

    @Override
    public String actualizar() {
        temperatura = temperatura >= 100 ? -100 : Math.min(100, temperatura + 0.5);
        actualizaciones++;
        return renderizar();
    }

    @Override
    protected String configuracion() {
        return "Ciudad: " + ciudad + " | Temperatura: " + temperatura + " °C | Actualizaciones: " + actualizaciones;
    }
}
