import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class GestorIntegraciones {
    private final ArrayList<IntegracionAPI> integraciones;

    public GestorIntegraciones() { integraciones = new ArrayList<>(); }

    public String cargarIntegracionesIniciales() {
        if (!integraciones.isEmpty()) return "OMITIDA: el catalogo ya tiene integraciones";
        registrarIntegracion(new PasarelaPago("P01", "Tarjeta Uno", "sandbox-p01", true, 120, "GTQ", 2.5));
        registrarIntegracion(new PasarelaPago("P02", "Transferencia Dos", "sandbox-p02", true, 90, "GTQ", 1.0));
        registrarIntegracion(new PasarelaPago("P03", "Billetera Tres", "sandbox-p03", false, 180, "USD", 3.0));
        registrarIntegracion(new PasarelaPago("P04", "Tarjeta Cuatro", "sandbox-p04", true, 110, "USD", 2.0));
        registrarIntegracion(new ProveedorLogistica("L01", "Envios Centro", "sandbox-l01", true, 75, "Guatemala", 25));
        registrarIntegracion(new ProveedorLogistica("L02", "Paqueteria Norte", "sandbox-l02", true, 140, "Norte", 35));
        registrarIntegracion(new ProveedorLogistica("L03", "Ultima Milla", "sandbox-l03", false, 220, "Capital", 20));
        registrarIntegracion(new CertificadorFEL("F01", "FEL Uno", "sandbox-f01", true, 100, "1234567-8", "001"));
        registrarIntegracion(new CertificadorFEL("F02", "FEL Dos", "sandbox-f02", true, 135, "1234567-8", "002"));
        registrarIntegracion(new CertificadorFEL("F03", "FEL Tres", "sandbox-f03", false, 190, "1234567-8", "003"));
        return "Carga inicial completada: " + integraciones.size() + " integraciones";
    }

    public String registrarIntegracion(IntegracionAPI integracion) {
        if (integracion == null) return "OMITIDA: integracion nula";
        if (buscarPorIdentificador(integracion.getIdentificador()) != null) return "OMITIDA: identificador duplicado";
        integraciones.add(integracion);
        return "Registrada: " + integracion.getIdentificador();
    }

    public IntegracionAPI buscarPorIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) return null;
        String buscado = identificador.trim().toUpperCase(Locale.ROOT);
        for (IntegracionAPI integracion : integraciones)
            if (integracion.getIdentificador().equals(buscado)) return integracion;
        return null;
    }

    public String listarIntegracionesActivas() {
        StringBuilder sb = new StringBuilder("CATALOGO DE INTEGRACIONES (estado de conexion):\n");
        for (IntegracionAPI integracion : integraciones) sb.append(integracion).append('\n');
        return integraciones.isEmpty() ? "Catalogo vacio" : sb.toString();
    }

    public String consultarConfiguracion(String identificador) {
        IntegracionAPI integracion = buscarPorIdentificador(identificador);
        return integracion == null ? "NO_ENCONTRADA" : integracion.getConfiguracion();
    }

    public void ordenarPorLatencia() { Collections.sort(integraciones); }

    public String procesarCobro(String identificador, double monto, String referencia) {
        IntegracionAPI integracion = buscarPorIdentificador(identificador);
        if (integracion == null) return "NO_ENCONTRADA";
        if (!integracion.isConectado()) return "OMITIDA: integracion desconectada";
        if (!(integracion instanceof Transaccional)) return "OMITIDA: integracion no transaccional";
        return ejecutarCobro((Transaccional) integracion, monto, referencia);
    }

    public String procesarReintegro(String identificador, double monto, String referencia) {
        IntegracionAPI integracion = buscarPorIdentificador(identificador);
        if (integracion == null) return "NO_ENCONTRADA";
        if (!integracion.isConectado()) return "OMITIDA: integracion desconectada";
        if (!(integracion instanceof Transaccional)) return "OMITIDA: integracion no transaccional";
        return ejecutarReintegro((Transaccional) integracion, monto, referencia);
    }

    public String procesarDocumento(String identificador, String tipoDocumento, String datos) {
        IntegracionAPI integracion = buscarPorIdentificador(identificador);
        if (integracion == null) return "NO_ENCONTRADA";
        if (!integracion.isConectado()) return "OMITIDA: integracion desconectada";
        if (!(integracion instanceof Documentable)) return "OMITIDA: integracion no documentable";
        return emitirDocumento((Documentable) integracion, tipoDocumento, datos);
    }

    public String procesarConsulta(String identificador, String tipoConsulta, String datos) {
        IntegracionAPI integracion = buscarPorIdentificador(identificador);
        if (integracion == null) return "NO_ENCONTRADA";
        if (!integracion.isConectado()) return "OMITIDA: integracion desconectada";
        if (!(integracion instanceof Auditable)) return "OMITIDA: integracion no auditable";
        return consultarAuditoria((Auditable) integracion, tipoConsulta, datos);
    }

    private String ejecutarCobro(Transaccional servicio, double monto, String referencia) {
        return servicio.cobrar(monto, referencia);
    }
    private String ejecutarReintegro(Transaccional servicio, double monto, String referencia) {
        return servicio.reintegrar(monto, referencia);
    }
    private String emitirDocumento(Documentable servicio, String tipoDocumento, String datos) {
        return servicio.generarDocumento(tipoDocumento, datos);
    }
    private String consultarAuditoria(Auditable servicio, String tipoConsulta, String datos) {
        if ("ESTADO".equalsIgnoreCase(tipoConsulta)) return servicio.consultarEstado(datos);
        return servicio.consultarInformacion(tipoConsulta, datos);
    }

    public String consultarIntegracion(String identificador) {
        return consultarIntegracion(identificador, false);
    }
    public String consultarIntegracion(String identificador, boolean incluirConfiguracion) {
        IntegracionAPI integracion = buscarPorIdentificador(identificador);
        if (integracion == null) return "NO_ENCONTRADA";
        return integracion.toString() + (incluirConfiguracion ? "\nConfiguracion: " + integracion.getConfiguracion() : "");
    }
}
