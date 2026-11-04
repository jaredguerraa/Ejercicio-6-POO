public class CertificadorFEL extends IntegracionAPI implements Documentable, Auditable {
    private final String nitEmisor;
    private final String establecimiento;

    public CertificadorFEL(String identificador, String nombreProveedor, String configuracion,
                           boolean conectado, double latenciaPromedio, String nitEmisor, String establecimiento) {
        super(identificador, nombreProveedor, configuracion, conectado, latenciaPromedio);
        this.nitEmisor = textoObligatorio(nitEmisor, "NIT emisor");
        this.establecimiento = textoObligatorio(establecimiento, "Establecimiento");
    }
    public String getNitEmisor() { return nitEmisor; }
    public String getEstablecimiento() { return establecimiento; }
    @Override
    public String generarDocumento(String tipoDocumento, String datos) {
        if (!isConectado()) return "OMITIDA: certificador desconectado";
        if (!"FACTURA_XML".equalsIgnoreCase(tipoDocumento) || datos == null || datos.trim().isEmpty())
            return "OMITIDA: documento no soportado o datos invalidos";
        String limpio = datos.trim().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return "SIMULADO: <factura nit=\"" + nitEmisor + "\" establecimiento=\"" + establecimiento + "\"><datos>" + limpio + "</datos></factura>";
    }
    @Override
    public String consultarEstado(String referencia) {
        if (!isConectado()) return "OMITIDA: certificador desconectado";
        if (referencia == null || referencia.trim().isEmpty()) return "OMITIDA: referencia vacia";
        return "SIMULADO: certificacion FEL " + referencia.trim() + " consultada";
    }
    @Override
    public String consultarInformacion(String tipoConsulta, String datos) {
        if (!isConectado()) return "OMITIDA: certificador desconectado";
        if ("REGISTROS".equalsIgnoreCase(tipoConsulta)) return "SIMULADO: registros FEL para " + datos;
        return "OMITIDA: consulta no soportada por certificador";
    }
    @Override
    public String toString() { return super.toString() + " | FEL NIT " + nitEmisor + " | establecimiento " + establecimiento; }
}
