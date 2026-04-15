import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Servidor {
    private static final int PUERTO = 5000;
    private static DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        System.out.println("[SERVIDOR] Iniciando servidor en puerto " + PUERTO + "...");
        
        try (ServerSocket socketServidor = new ServerSocket(PUERTO)) {
            System.out.println("[SERVIDOR] Servidor listo esperando conexiones...\n");
            
            while (true) {
                Socket socketCliente = socketServidor.accept();
                System.out.println("[" + obtenerHora() + "] [SERVIDOR] Cliente conectado desde: " 
                    + socketCliente.getInetAddress().getHostAddress());
                
                // Manejar el cliente de forma sincrónica
                manejarCliente(socketCliente);
            }
        } catch (IOException errorIO) {
            System.err.println("[ERROR] Error en el servidor: " + errorIO.getMessage());
        }
    }

    private static void manejarCliente(Socket socketCliente) {
        try (BufferedReader lecturaCliente = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
             PrintWriter envioAlCliente = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()), true)) {

            String comandoRecibido;
            while ((comandoRecibido = lecturaCliente.readLine()) != null) {
                System.out.println("[" + obtenerHora() + "] [RECIBIDO] " + comandoRecibido);

                if (comandoRecibido.equalsIgnoreCase("salida")) {
                    System.out.println("[" + obtenerHora() + "] [SERVIDOR] Cliente solicitó desconexión\n");
                    envioAlCliente.println("Desconexión confirmada. ¡Hasta luego!");
                    break;
                }

                String respuestaServidor = procesarComando(comandoRecibido);
                System.out.println("[" + obtenerHora() + "] [ENVIANDO] " + respuestaServidor);
                envioAlCliente.println(respuestaServidor);
            }

            socketCliente.close();
            System.out.println("[" + obtenerHora() + "] [SERVIDOR] Conexión cerrada\n");

        } catch (IOException errorIO) {
            System.err.println("[ERROR] Error manejando cliente: " + errorIO.getMessage());
        }
    }

    private static String procesarComando(String mensajeCliente) {
        // Formato esperado: *COMANDO "contenido"
        if (mensajeCliente.startsWith("*")) {
            String[] partesComando = mensajeCliente.split(" ", 2);
            String tipoComando = partesComando[0].substring(1).toUpperCase();

            if (tipoComando.equals("VOCALES")) {
                if (partesComando.length > 1) {
                    String textoIngresado = partesComando[1].replaceAll("\"", "");
                    int totalVocales = contarVocales(textoIngresado);
                    return "El texto \"" + textoIngresado + "\" contiene " + totalVocales + " vocales.";
                } else {
                    return "Error: Formato incorrecto. Use: *VOCALES \"texto\"";
                }
            } else if (tipoComando.equals("CONSONANTES")) {
                if (partesComando.length > 1) {
                    String textoIngresado = partesComando[1].replaceAll("\"", "");
                    int totalConsonantes = contarConsonantes(textoIngresado);
                    return "El texto \"" + textoIngresado + "\" contiene " + totalConsonantes + " consonantes.";
                } else {
                    return "Error: Formato incorrecto. Use: *CONSONANTES \"texto\"";
                }
            } else if (tipoComando.equals("MAYUSCULAS")) {
                if (partesComando.length > 1) {
                    String textoIngresado = partesComando[1].replaceAll("\"", "");
                    return "Resultado: \"" + textoIngresado.toUpperCase() + "\"";
                } else {
                    return "Error: Formato incorrecto. Use: *MAYUSCULAS \"texto\"";
                }
            } else if (tipoComando.equals("LONGITUD")) {
                if (partesComando.length > 1) {
                    String textoIngresado = partesComando[1].replaceAll("\"", "");
                    return "La longitud de \"" + textoIngresado + "\" es: " + textoIngresado.length() + " caracteres.";
                } else {
                    return "Error: Formato incorrecto. Use: *LONGITUD \"texto\"";
                }
            } else {
                return "Comando desconocido: " + tipoComando;
            }
        } else {
            return "Echo: " + mensajeCliente;
        }
    }

    private static int contarVocales(String texto) {
        int contadorVocales = 0;
        String alfabetoVocales = "aeiouAEIOU";
        for (char caracterActual : texto.toCharArray()) {
            if (alfabetoVocales.indexOf(caracterActual) != -1) {
                contadorVocales++;
            }
        }
        return contadorVocales;
    }

    private static int contarConsonantes(String texto) {
        int contadorConsonantes = 0;
        String alfabetoConsonantes = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";
        for (char caracterActual : texto.toCharArray()) {
            if (alfabetoConsonantes.indexOf(caracterActual) != -1) {
                contadorConsonantes++;
            }
        }
        return contadorConsonantes;
    }

    private static String obtenerHora() {
        return LocalDateTime.now().format(formatoHora);
    }
}
