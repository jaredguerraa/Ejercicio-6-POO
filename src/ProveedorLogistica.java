import java.util.Locale;

public class ProveedorLogistica extends IntegracionAPI implements Documentable, Auditable {
    private final String zonaCobertura;
    private final double tarifaBaseEnvio;

    public ProveedorLogistica(String identificador, String nombreProveedor, String configuracion,
                              boolean conectado, double latenciaPromedio, String zonaCobertura, double tarifaBaseEnvio) {
        super(identificador, nombreProveedor, configuracion, conectado, latenciaPromedio);
        this.zonaCobertura = textoObligatorio(zonaCobertura, "Cobertura");
        if (!Double.isFinite(tarifaBaseEnvio) || tarifaBaseEnvio < 0)
            throw new IllegalArgumentException("Tarifa de envio invalida");
        this.tarifaBaseEnvio = tarifaBaseEnvio;
    }
    public String getZonaCobertura() { return zonaCobertura; }
    public double getTarifaBaseEnvio() { return tarifaBaseEnvio; }
    @Override
    public String generarDocumento(String tipoDocumento, String datos) {
        if (!isConectado()) return "OMITIDA: proveedor desconectado";
        if (!"GUIA".equalsIgnoreCase(tipoDocumento) || datos == null || datos.trim().isEmpty())
            return "OMITIDA: documento no soportado o datos invalidos";
        return "SIMULADO: guia de envio generada para " + datos.trim();
    }
    @Override
    public String consultarEstado(String referencia) {
        if (!isConectado()) return "OMITIDA: proveedor desconectado";
        if (referencia == null || referencia.trim().isEmpty()) return "OMITIDA: referencia vacia";
        return "SIMULADO: seguimiento de paquete " + referencia.trim() + " en proceso";
    }
    @Override
    public String consultarInformacion(String tipoConsulta, String datos) {
        if (!isConectado()) return "OMITIDA: proveedor desconectado";
        if (datos == null || datos.trim().isEmpty()) return "OMITIDA: datos vacios";
        if ("COTIZACION".equalsIgnoreCase(tipoConsulta))
            return String.format(Locale.US, "SIMULADO: envio a %s desde Q%.2f (cobertura: %s)", datos.trim(), tarifaBaseEnvio, zonaCobertura);
        if ("REGISTROS".equalsIgnoreCase(tipoConsulta)) return "SIMULADO: registros de envios para " + datos.trim();
        return "OMITIDA: consulta no soportada";
    }
    @Override
    public String toString() { return super.toString() + " | Logistica " + zonaCobertura + " | tarifa Q" + tarifaBaseEnvio; }
}
