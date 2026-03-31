package dev.fixfis.server.whatsapp;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;

import static dev.fixfis.server.whatsapp.WhatsappApi.*;

public class Multipart {
    static void sendFile(String phone, String caption, File file) {
        try {
            String boundary = "----Boundary" + System.currentTimeMillis();
            String url = apiUrl + "/send/file";

            HttpURLConnection conn = (HttpURLConnection) new URI(url).toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("device-id", device_id);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);

            OutputStream os = conn.getOutputStream();

            // --- Parte 1: phone ---
            writePart(os, boundary, "phone", phone);

            // --- Parte 2: caption ---
            if (caption != null && !caption.isEmpty()) {
                writePart(os, boundary, "caption", caption);
            }

            // --- Parte 3: el archivo ---
            writeFilePart(os, boundary, "file", file);

            // --- Cerrar el multipart ---
            os.write(("\r\n--" + boundary + "--\r\n").getBytes());
            os.flush();
            os.close();

            // --- Leer respuesta ---
            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 400)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
            reader.close();

            System.out.println("Respuesta: " + response);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando archivo", e);
        }
    }

    // Escribe un campo de texto normal
    private static void writePart(OutputStream os, String boundary,
                                  String name, String value) throws Exception {
        String part = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"" + name + "\"\r\n"
                + "\r\n"
                + value + "\r\n";
        os.write(part.getBytes("UTF-8"));
    }

    // Escribe la parte del archivo con sus bytes
    private static void writeFilePart(OutputStream os, String boundary,
                                      String fieldName, File file) throws Exception {
        String header = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"" + fieldName
                + "\"; filename=\"" + file.getName() + "\"\r\n"
                + "Content-Type: application/octet-stream\r\n"
                + "\r\n";
        os.write(header.getBytes("UTF-8"));

        // Leer y escribir los bytes del archivo
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        fis.close();
    }

    static void main() {
        refreshToken();

        File archivo = new File("C:\\Users\\carca\\Documents\\Historia de usuario 01.docx");

        sendFile(
                "51906668576@s.whatsapp.net",  // phone con @s.whatsapp.net
                "Aquí tu caption",             // caption (puede ser "")
                archivo
        );
    }
}
