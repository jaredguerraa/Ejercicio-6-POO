import java.util.Locale;

public class PasarelaPago extends IntegracionAPI implements Transaccional, Auditable {
    private final String moneda;
    private final double comisionPorcentaje;

    public PasarelaPago(String identificador, String nombreProveedor, String configuracion,
                        boolean conectado, double latenciaPromedio, String moneda, double comisionPorcentaje) {
        super(identificador, nombreProveedor, configuracion, conectado, latenciaPromedio);
        this.moneda = textoObligatorio(moneda, "Moneda").toUpperCase(Locale.ROOT);
        if (!Double.isFinite(comisionPorcentaje) || comisionPorcentaje < 0 || comisionPorcentaje > 100)
            throw new IllegalArgumentException("Comision invalida");
        this.comisionPorcentaje = comisionPorcentaje;
    }
    public String getMoneda() { return moneda; }
    public double getComisionPorcentaje() { return comisionPorcentaje; }

    @Override
    public String cobrar(double monto, String referencia) {
        if (!isConectado()) return "OMITIDA: pasarela desconectada";
        if (!montoValido(monto) || referencia == null || referencia.trim().isEmpty()) return "OMITIDA: datos de cobro invalidos";
        return String.format(Locale.US, "SIMULADO: cobro de %.2f %s, referencia %s, comision %.2f%%",
                monto, moneda, referencia.trim(), comisionPorcentaje);
    }
    @Override
    public String reintegrar(double monto, String referencia) {
        if (!isConectado()) return "OMITIDA: pasarela desconectada";
        if (!montoValido(monto) || referencia == null || referencia.trim().isEmpty()) return "OMITIDA: datos de reintegro invalidos";
        // En el prototipo la validacion del saldo de la transaccion se simula.
        return String.format(Locale.US, "SIMULADO: reintegro solicitado de %.2f %s para %s (validacion externa simulada)",
                monto, moneda, referencia.trim());
    }
    @Override
    public String consultarEstado(String referencia) {
        if (!isConectado()) return "OMITIDA: pasarela desconectada";
        if (referencia == null || referencia.trim().isEmpty()) return "OMITIDA: referencia vacia";
        return "SIMULADO: consulta financiera de " + referencia.trim() + " recibida";
    }
    @Override
    public String consultarInformacion(String tipoConsulta, String datos) {
        if (!isConectado()) return "OMITIDA: pasarela desconectada";
        if ("REGISTROS".equalsIgnoreCase(tipoConsulta)) return "SIMULADO: registros financieros para " + datos;
        return "OMITIDA: tipo de consulta no soportado por pasarela";
    }
    @Override
    public String toString() { return super.toString() + " | Pago " + moneda + " | comision " + comisionPorcentaje + "%"; }
}
