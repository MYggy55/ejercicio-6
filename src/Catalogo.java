public final class Catalogo {
    private Catalogo() { }

    public static void cargar(Dashboard dashboard) {
        if (dashboard == null) throw new IllegalArgumentException("El dashboard no puede ser null.");
        dashboard.agregar(new WidgetTexto(1, "Bienvenida", 20, "Bienvenido al panel empresarial."));
        dashboard.agregar(new WidgetTexto(2, "Política de seguridad", 25, "No compartas tus credenciales."));
        dashboard.agregar(new WidgetClima(3, "Clima Guatemala", 180, "Guatemala", 22));
        dashboard.agregar(new WidgetClima(4, "Clima Quetzaltenango", 160, "Quetzaltenango", 15));
        dashboard.agregar(new WidgetTareas(5, "Tareas de soporte", 110,
            new String[]{"Revisar tickets", "Responder correos", "Actualizar manual"}));
        dashboard.agregar(new WidgetTareas(6, "Tareas de ventas", 95,
            new String[]{"Preparar cotización", "Llamar al cliente"}));
        dashboard.agregar(new WidgetAlerta(7, "Alerta de servidor", 55, "Revisar espacio en disco", "Alta"));
        dashboard.agregar(new WidgetAlerta(8, "Alerta de reunión", 45, "Reunión de equipo a las 10:00", "Media"));
        dashboard.agregar(new WidgetReloj(9, "Hora operativa", 30));
        dashboard.agregar(new WidgetReloj(10, "Hora de monitoreo", 35));
        dashboard.agregar(new WidgetTexto(11, "Indicador de servicio", 20, "Objetivo de disponibilidad: 99.9 %"));
        dashboard.agregar(new WidgetAlerta(12, "Alerta de respaldo", 50, "Verificar copia de seguridad", "Baja"));
    }
}
