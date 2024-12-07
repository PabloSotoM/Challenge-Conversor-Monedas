import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConversorDeMonedas {

    private static final String API_KEY = "9117ae84cbbd4c5c8c7080d5";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\nSeleccione la operación:");
            System.out.println("1. Dólar (USD) a Peso Argentino (ARS) 🇦🇷");
            System.out.println("2. Peso Argentino (ARS) a Dólar (USD) 💵");
            System.out.println("3. Dólar (USD) a Real Brasileño (BRL) 🇧🇷");
            System.out.println("4. Real Brasileño (BRL) a Dólar (USD) 💵");
            System.out.println("5. Dólar (USD) a Peso Colombiano (COP) 🇨🇴");
            System.out.println("6. Peso Colombiano (COP) a Dólar (USD) 💵");
            System.out.println("7. Salir 🛑");
            System.out.print("Ingrese el número de la operación: ");
            int opcion = scanner.nextInt();

            if (opcion == 7) {
                continuar = false;
                System.out.println("Saliendo del programa...");
                break;
            }

            System.out.print("Ingrese el valor a convertir: ");
            double cantidad = scanner.nextDouble();
            String desdeMoneda = "";
            String aMoneda = "";

            switch (opcion) {
                case 1 -> {
                    desdeMoneda = "USD";
                    aMoneda = "ARS";
                }
                case 2 -> {
                    desdeMoneda = "ARS";
                    aMoneda = "USD";
                }
                case 3 -> {
                    desdeMoneda = "USD";
                    aMoneda = "BRL";
                }
                case 4 -> {
                    desdeMoneda = "BRL";
                    aMoneda = "USD";
                }
                case 5 -> {
                    desdeMoneda = "USD";
                    aMoneda = "COP";
                }
                case 6 -> {
                    desdeMoneda = "COP";
                    aMoneda = "USD";
                }
                default -> {
                    System.out.println("Opción no válida.");
                    continue;
                }
            }

            double resultado = obtenerTasaCambioYConvertir(desdeMoneda, aMoneda, cantidad);
            if (resultado != -1) {
                System.out.printf("El resultado de la conversión es: %.2f %s\n", resultado, aMoneda);
            } else {
                System.out.println("Error al obtener la tasa de cambio. Inténtelo más tarde.");
            }
        }

        scanner.close();
    }

    private static double obtenerTasaCambioYConvertir(String desdeMoneda, String aMoneda, double cantidad) {
        try {
            String url = BASE_URL + desdeMoneda + "/" + aMoneda;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            if (jsonObject.get("result").getAsString().equals("success")) {
                double tasaCambio = jsonObject.get("conversion_rate").getAsDouble();
                return cantidad * tasaCambio;
            } else {
                System.out.println("Error en la respuesta de la API: " + jsonObject.get("error-type").getAsString());
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Excepción al realizar la solicitud: " + e.getMessage());
            return -1;
        }
    }
}
