public final class WidgetTareas extends Widget implements Interactivo {
    private final String[] tareas;
    private int completadas;

    public WidgetTareas(int id, String nombre, int memoriaKB, String[] tareas) {
        super(id, nombre, memoriaKB);
        if (tareas == null || tareas.length == 0) {
            throw new IllegalArgumentException("Debe existir al menos una tarea.");
        }
        this.tareas = tareas.clone();
        for (int i = 0; i < this.tareas.length; i++) {
            this.tareas[i] = validarTexto(this.tareas[i], "Tarea");
        }
    }

    @Override
    public String renderizar() {
        StringBuilder texto = new StringBuilder("[TAREAS] " + getNombre());
        for (int i = 0; i < tareas.length; i++) {
            texto.append("\n  ").append(i < completadas ? "[x] " : "[ ] ").append(tareas[i]);
        }
        return texto.toString();
    }

    @Override
    public String hacerClic() {
        if (completadas == tareas.length) return "No quedan tareas pendientes.";
        String tarea = tareas[completadas];
        completadas++;
        return "Tarea completada: " + tarea;
    }

    @Override
    protected String configuracion() {
        return "Tareas: " + tareas.length + " | Completadas: " + completadas
            + " | Pendientes: " + (tareas.length - completadas);
    }
}
