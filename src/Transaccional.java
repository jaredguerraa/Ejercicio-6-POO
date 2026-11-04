public interface Transaccional {
    String cobrar(double monto, String referencia);
    String reintegrar(double monto, String referencia);
}
