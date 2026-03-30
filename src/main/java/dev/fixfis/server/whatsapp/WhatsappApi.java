package dev.fixfis.server.whatsapp;

public class WhatsappApi {

    static String apiUrl = "https://whatsapp.apisperu.com/api/v1";

    static ApiClient apiClient = new ApiClient(apiUrl);
    static String token = "";
    static String user = "fixfis";
    static String password = "Nayipfeo2007a";

    static String device_id = "fixfisdevice";

    static void refreshToken() {
        WhatsappApi.token = apiClient.<WhatsLogin>create("/auth/login").post(new WhatsLogin(user,password)).getJson().get("token").getAsString();
        System.out.println(apiClient.create("/app/reconnect").token(token).headers("device_id", device_id).getJson().toString());
    }

    static void sendText(SendMessage sendMessage) {

        String asString = apiClient.create("/send/message").token(token).headers("device_id", device_id).post(sendMessage).getJson().toString();

        System.out.println(asString);

    }

    static void main() {
        refreshToken();

        System.out.println(token);
        
        SendMessage sendMessage = new SendMessage();
        sendMessage.setMessage("hola, probando \n pene");
        sendMessage.setPhone("51906668576");
        sendText(sendMessage);

    }
}
