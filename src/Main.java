import java.util.Scanner;

public class Main {
    private static Scanner scanner;
    private static GestorIntegraciones gestor;

    private Main() { }

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        gestor = new GestorIntegraciones();
        mostrarResultado(gestor.cargarIntegracionesIniciales());
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            Integer opcion = leerEntero("Opcion: ");
            if (opcion == null || opcion == 0) {
                continuar = false;
            } else {
                switch (opcion) {
                    case 1: listarIntegraciones(); break;
                    case 2: buscarIntegracion(); break;
                    case 3: verConfiguracion(); break;
                    case 4: ordenarCatalogo(); break;
                    case 5: solicitarCobro(); break;
                    case 6: solicitarReintegro(); break;
                    case 7: solicitarDocumento(); break;
                    case 8: solicitarConsulta(); break;
                    default: mostrarResultado("Opcion no valida");
                }
            }
        }
        System.out.println("Programa finalizado");
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n= INTEGRACIONES DE COMERCIO ELECTRÓNICO =");
        System.out.println("1. Listar integraciones y estado de conexión");
        System.out.println("2. Buscar integración");
        System.out.println("3. Ver configuración");
        System.out.println("4. Ordenar por latencia");
        System.out.println("5. Simular cobro");
        System.out.println("6. Simular reintegro");
        System.out.println("7. Generar documento simulado");
        System.out.println("8. Consultar estado, registros o cotización");
        System.out.println("0. Salir");
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : null;
    }
    private static Integer leerEntero(String mensaje) {
        Integer resultado = null;
        boolean pendiente = true;
        while (pendiente) {
            String entrada = leerTexto(mensaje);
            if (entrada == null) return null;
            try {
                resultado = Integer.valueOf(entrada);
                pendiente = false;
            } catch (NumberFormatException ex) {
                mostrarResultado("Escribe un numero entero valido");
            }
        }
        return resultado;
    }
    private static Double leerDecimal(String mensaje) {
        Double resultado = null;
        boolean pendiente = true;
        while (pendiente) {
            String entrada = leerTexto(mensaje);
            if (entrada == null) return null;
            try {
                resultado = Double.valueOf(entrada);
                if (!Double.isFinite(resultado)) throw new NumberFormatException();
                pendiente = false;
            } catch (NumberFormatException ex) {
                mostrarResultado("Escribe un decimal válido");
            }
        }
        return resultado;
    }
    private static void listarIntegraciones() { mostrarResultado(gestor.listarIntegracionesActivas()); }
    private static void buscarIntegracion() {
        String id = leerTexto("Identificador: ");
        if (id != null) mostrarResultado(gestor.consultarIntegracion(id));
    }
    private static void verConfiguracion() {
        String id = leerTexto("Identificador: ");
        if (id != null) mostrarResultado(gestor.consultarIntegracion(id, true));
    }
    private static void ordenarCatalogo() {
        gestor.ordenarPorLatencia();
        mostrarResultado("Catálogo ordenado por latencia ascendente");
        listarIntegraciones();
    }
    private static void solicitarCobro() {
        String id = leerTexto("API: ");
        if (id == null) return;
        Double monto = leerDecimal("Monto: ");
        if (monto == null) return;
        String referencia = leerTexto("Referencia: ");
        if (referencia != null) mostrarResultado(gestor.procesarCobro(id, monto, referencia));
    }
    private static void solicitarReintegro() {
        String id = leerTexto("API: ");
        if (id == null) return;
        Double monto = leerDecimal("Monto: ");
        if (monto == null) return;
        String referencia = leerTexto("Referencia original: ");
        if (referencia != null) mostrarResultado(gestor.procesarReintegro(id, monto, referencia));
    }
    private static void solicitarDocumento() {
        String id = leerTexto("API: ");
        if (id == null) return;
        String tipo = leerTexto("Tipo (GUIA / FACTURA_XML): ");
        if (tipo == null) return;
        String datos = leerTexto("Datos: ");
        if (datos != null) mostrarResultado(gestor.procesarDocumento(id, tipo, datos));
    }
    private static void solicitarConsulta() {
        String id = leerTexto("API: ");
        if (id == null) return;
        String tipo = leerTexto("Tipo (ESTADO / REGISTROS / COTIZACIÓN): ");
        if (tipo == null) return;
        String datos = leerTexto("Referencia o datos: ");
        if (datos != null) mostrarResultado(gestor.procesarConsulta(id, tipo, datos));
    }
    private static void mostrarResultado(String resultado) { System.out.println(resultado); }
}
