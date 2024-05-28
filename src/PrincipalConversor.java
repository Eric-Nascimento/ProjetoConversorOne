import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PrincipalConversor {
    public static void main(String[] args) throws IOException {
        Scanner leitura = new Scanner(System.in);

        Set<String> moedasDeInteresse = new HashSet<>();
        moedasDeInteresse.add("EUR");
        moedasDeInteresse.add("JPY");
        moedasDeInteresse.add("BRL");
        moedasDeInteresse.add("USD");
        moedasDeInteresse.add("CNY");
        moedasDeInteresse.add("RUB");


        int opcaoInicial = 0;

        while (opcaoInicial != 3){
            System.out.println();
            System.out.println("Digite a opção desejada para o menu de conversão:");
            System.out.println("1 - Consultar uma conversão");
            System.out.println("2 - Exibir histórico");
            System.out.println("3 - Sair");
            opcaoInicial = leitura.nextInt();
            leitura.nextLine();
            System.out.println();


            if (opcaoInicial == 1){
                System.out.println("** MOEDAS DISPONIVEIS PARA A CONVERSÃO **");
                System.out.println("BRL - Real Brasileiro");
                System.out.println("USD - Dolar Americano");
                System.out.println("JPY - Iene Japonês");
                System.out.println("EUR - Euro");
                System.out.println("CNY - Yuan Chinês");
                System.out.println("RUB = Rublo russo");
                System.out.println();
                System.out.println("Digite a sigla da moeda base: ");
                String moedaBase = leitura.nextLine().toUpperCase();
               // System.out.println(moedaBase);

                System.out.println("Digite a sigla da moeda para qual deseja converte-la: ");
                String moedaFinal = leitura.nextLine().toUpperCase();
                // System.out.println(moedaFinal);

                System.out.println("Digite a quantidade de moedas que deseja converter: ");
                double quantidadeMoedas = leitura.nextDouble();
                leitura.nextLine();
                // System.out.println(quantidadeMoedas);
                System.out.println();


                String key = "39984e065b78a78714ed8fc6";
                String endereco = "https://v6.exchangerate-api.com/v6/"+ key +"/latest/"+ moedaBase;

                try {
                    //request
                    URL url = new URL(endereco);
                    HttpURLConnection request = (HttpURLConnection) url.openConnection();
                    request.connect();

                    int responseCode = request.getResponseCode();
                    if (responseCode != 200) {
                        System.out.println("Erro na requisição: " + responseCode);
                        return;
                    }

                    // Convert to JSON
                    JsonParser jp = new JsonParser();
                    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
                    JsonObject jsonobj = root.getAsJsonObject();

                    // Accessing object
                    String req_result = jsonobj.get("result").getAsString();
                   // System.out.println("Status da requisição: " + req_result);

                    JsonObject taxaDeConversao = jsonobj.getAsJsonObject("conversion_rates");
                    if (taxaDeConversao.has(moedaFinal)){
                        double taxa = taxaDeConversao.get(moedaFinal).getAsDouble();
                        double valorConvertido = quantidadeMoedas * taxa;
                        System.out.printf("%.2f %s = %.2f %s%n", quantidadeMoedas, moedaBase, valorConvertido, moedaFinal);
                        System.out.println();

                        Conversao conversao = new Conversao(moedaBase, moedaFinal, quantidadeMoedas, valorConvertido);
                        conversao.salvarHistorico("historico.txt");

                    } else {
                        System.out.println("A moeda "+ moedaFinal + " não está disponivel!");
                        System.out.println();
                    }

                } catch (IOException e){
                    System.out.println("Erro de Io: "+ e.getMessage());
                    e.printStackTrace();
                }

            }else if (opcaoInicial == 2){
                System.out.println("** HISTÓRICO DE CONVERSÕES **");
                try (Scanner lerArquivo = new Scanner(new java.io.File("historico.txt"))){
                    while (lerArquivo.hasNextLine()){
                        System.out.println(lerArquivo.nextLine());
                    }
                } catch (IOException e) {
                    System.out.println("Erro ao ler arquivo de histórico: "+ e.getMessage());
                    e.printStackTrace();
                }
            }else if (opcaoInicial == 3){
                System.out.println("Programa finalizado!");
            }else {
                System.out.println("Opção invalida, por favor, digite uma opção valida!");
            }

        }
        leitura.close();
    }
}