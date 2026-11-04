import java.util.Objects;

public abstract class IntegracionAPI implements Comparable<IntegracionAPI> {
    private final String identificador;
    private final String nombreProveedor;
    private String configuracion;
    private boolean conectado;
    private double latenciaPromedio;

    protected IntegracionAPI(String identificador, String nombreProveedor, String configuracion,
                             boolean conectado, double latenciaPromedio) {
        this.identificador = textoObligatorio(identificador, "Identificador").toUpperCase(java.util.Locale.ROOT);
        this.nombreProveedor = textoObligatorio(nombreProveedor, "Proveedor");
        this.configuracion = textoObligatorio(configuracion, "Configuracion");
        if (!Double.isFinite(latenciaPromedio) || latenciaPromedio < 0) {
            throw new IllegalArgumentException("Latencia invalida");
        }
        this.conectado = conectado;
        this.latenciaPromedio = latenciaPromedio;
    }

    protected static String textoObligatorio(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacio");
        }
        return texto.trim();
    }

    protected static boolean montoValido(double monto) {
        return Double.isFinite(monto) && monto > 0;
    }

    public String getIdentificador() { return identificador; }
    public String getNombreProveedor() { return nombreProveedor; }
    public String getConfiguracion() { return configuracion; }
    public boolean isConectado() { return conectado; }
    public double getLatenciaPromedio() { return latenciaPromedio; }

    public boolean setConfiguracion(String configuracion) {
        if (configuracion == null || configuracion.trim().isEmpty()) return false;
        this.configuracion = configuracion.trim();
        return true;
    }
    public void setConectado(boolean conectado) { this.conectado = conectado; }
    public boolean setLatenciaPromedio(double latenciaPromedio) {
        if (!Double.isFinite(latenciaPromedio) || latenciaPromedio < 0) return false;
        this.latenciaPromedio = latenciaPromedio;
        return true;
    }

    @Override
    public String toString() {
        return identificador + " | " + nombreProveedor + " | " +
               (conectado ? "CONECTADA" : "DESCONECTADA") + " | " +
               String.format(java.util.Locale.US, "%.2f ms", latenciaPromedio);
    }
    @Override
    public final boolean equals(Object otro) {
        if (this == otro) return true;
        if (!(otro instanceof IntegracionAPI)) return false;
        return identificador.equals(((IntegracionAPI) otro).identificador);
    }
    @Override
    public final int hashCode() { return Objects.hash(identificador); }
    @Override
    public int compareTo(IntegracionAPI otra) {
        int orden = Double.compare(latenciaPromedio, otra.latenciaPromedio);
        return orden != 0 ? orden : identificador.compareTo(otra.identificador);
    }
}
