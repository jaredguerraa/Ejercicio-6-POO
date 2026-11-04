public interface Auditable {
    String consultarEstado(String referencia);
    String consultarInformacion(String tipoConsulta, String datos);
}
