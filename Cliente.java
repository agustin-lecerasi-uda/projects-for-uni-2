import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cliente {
    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;
    private static DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        System.out.println("[CLIENTE] Conectando con el servidor en " + HOST + ":" + PUERTO + "...\n");

        try (Socket socketCliente = new Socket(HOST, PUERTO);
             BufferedReader lecturaServidor = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
             PrintWriter envioAlServidor = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()), true);
             BufferedReader lecturaConsola = new BufferedReader(new InputStreamReader(System.in))) {

                
            System.out.println("[CLIENTE] Conectado al servidor exitosamente!\n");
            System.out.println("=== COMANDOS DISPONIBLES ===");
            System.out.println("*VOCALES \"texto\"          - Cuenta las vocales");
            System.out.println("*CONSONANTES \"texto\"      - Cuenta las consonantes");
            System.out.println("*MAYUSCULAS \"texto\"       - Convierte a mayúsculas");
            System.out.println("*LONGITUD \"texto\"         - Obtiene la longitud del texto");
            System.out.println("salida                     - Desconectar del servidor");
            System.out.println("============================\n");

            // Leer mensajes del usuario y enviarlos al servidor
            String comandoUsuario;
            while (true) {
                System.out.print("[" + obtenerHora() + "] [TÚ] ");
                comandoUsuario = lecturaConsola.readLine();

                if (comandoUsuario == null || comandoUsuario.isEmpty()) {
                    continue;
                }

                envioAlServidor.println(comandoUsuario);

                // Recibir respuesta del servidor
                String respuestaServidor = lecturaServidor.readLine();
                if (respuestaServidor != null) {
                    System.out.println("[" + obtenerHora() + "] [SERVIDOR] " + respuestaServidor);
                }

                if (comandoUsuario.equalsIgnoreCase("salida")) {
                    System.out.println("[" + obtenerHora() + "] [CLIENTE] Desconectando...\n");
                    break;
                }
            }

            socketCliente.close();
            System.out.println("[CLIENTE] Conexión cerrada.");

        } catch (ConnectException errorConexion) {
            System.err.println("[ERROR] No se pudo conectar al servidor. ¿Está ejecutándose?");
        } catch (IOException errorIO) {
            System.err.println("[ERROR] Error de comunicación: " + errorIO.getMessage());
        }
    }

    private static String obtenerHora() {
        return LocalDateTime.now().format(formatoHora);
    }
}
