import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONObject;

/**
 * Conversor de Moedas em tempo real via console
 * Obtém taxas de conversão dinamicamente de uma API externa
 */
public class ConversorDeMoedas {
    // Chave da API Free Currency Converter
    private static final String API_KEY = "85d2383cfccb8387cfe9";
    private static final String BASE_URL = "https://free.currconv.com/api/v7/convert";
    
    // Cache para armazenar taxas por um período de tempo
    private static final Map<String, Double> taxasCache = new HashMap<>();
    private static final Map<String, Long> tempoCache = new HashMap<>();
    private static final long DURACAO_CACHE = 3600000; // 1 hora em milissegundos
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");
    
    public static void main(String[] args) {
        boolean executando = true;
        
        while (executando) {
            exibirMenu();
            
            try {
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                
                if (opcao == 0) {
                    System.out.println("\nObrigado por usar o Conversor de Moedas! Até logo!");
                    executando = false;
                } else if (opcao >= 1 && opcao <= 7) {
                    String deMoeda = "";
                    String paraMoeda = "";
                    String descricao = "";
                    
                    switch (opcao) {
                        case 1:
                            deMoeda = "BRL";
                            paraMoeda = "USD";
                            descricao = "Real para Dólar";
                            break;
                        case 2:
                            deMoeda = "USD";
                            paraMoeda = "BRL";
                            descricao = "Dólar para Real";
                            break;
                        case 3:
                            deMoeda = "BRL";
                            paraMoeda = "EUR";
                            descricao = "Real para Euro";
                            break;
                        case 4:
                            deMoeda = "EUR";
                            paraMoeda = "BRL";
                            descricao = "Euro para Real";
                            break;
                        case 5:
                            deMoeda = "USD";
                            paraMoeda = "EUR";
                            descricao = "Dólar para Euro";
                            break;
                        case 6:
                            deMoeda = "EUR";
                            paraMoeda = "USD";
                            descricao = "Euro para Dólar";
                            break;
                        case 7:
                            System.out.print("Digite o código da moeda de origem (ex: BRL, USD, EUR): ");
                            deMoeda = scanner.nextLine().toUpperCase().trim();
                            System.out.print("Digite o código da moeda de destino (ex: BRL, USD, EUR): ");
                            paraMoeda = scanner.nextLine().toUpperCase().trim();
                            descricao = deMoeda + " para " + paraMoeda;
                            break;
                    }
                    
                    realizarConversao(deMoeda, paraMoeda, descricao);
                } else {
                    System.out.println("Opção inválida! Por favor, escolha uma opção válida.");
                    pressioneEnterParaContinuar();
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, digite um número.");
                pressioneEnterParaContinuar();
            }
        }
        
        scanner.close();
    }
    
    /**
     * Exibe o menu principal do conversor
     */
    private static void exibirMenu() {
        limparTela();
        System.out.println("=".repeat(50));
        System.out.println("         CONVERSOR DE MOEDAS EM TEMPO REAL         ");
        System.out.println("=".repeat(50));
        
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        System.out.println("Data/Hora atual: " + agora.format(formatter));
        
        System.out.println("\nEscolha uma opção de conversão:");
        System.out.println("1. Converter de Real (BRL) para Dólar (USD)");
        System.out.println("2. Converter de Dólar (USD) para Real (BRL)");
        System.out.println("3. Converter de Real (BRL) para Euro (EUR)");
        System.out.println("4. Converter de Euro (EUR) para Real (BRL)");
        System.out.println("5. Converter de Dólar (USD) para Euro (EUR)");
        System.out.println("6. Converter de Euro (EUR) para Dólar (USD)");
        System.out.println("7. Conversão personalizada");
        System.out.println("0. Sair");
        System.out.println("=".repeat(50));
        System.out.print("\nDigite sua opção: ");
    }
    
    /**
     * Realiza a conversão de moeda com base nos parâmetros fornecidos
     */
    private static void realizarConversao(String deMoeda, String paraMoeda, String descricao) {
        try {
            System.out.print("\nDigite o valor em " + deMoeda + " para converter para " + paraMoeda + ": ");
            double valor = Double.parseDouble(scanner.nextLine().replace(",", ".").trim());
            
            // Obter taxa e calcular conversão
            double taxa = obterTaxaConversao(deMoeda, paraMoeda);
            
            if (taxa > 0) {
                double valorConvertido = valor * taxa;
                
                System.out.println("\n=== Resultado da Conversão (" + descricao + ") ===");
                System.out.println(df.format(valor) + " " + deMoeda + " = " + df.format(valorConvertido) + " " + paraMoeda);
                System.out.println("Taxa de conversão: 1 " + deMoeda + " = " + df.format(taxa) + " " + paraMoeda);
                System.out.println("Dados obtidos em tempo real.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido! Por favor, digite um número válido.");
        } catch (Exception e) {
            System.out.println("Erro ao realizar conversão: " + e.getMessage());
        }
        
        pressioneEnterParaContinuar();
    }
    
    /**
     * Obtém a taxa de conversão da API ou do cache se estiver válido
     */
    private static double obterTaxaConversao(String deMoeda, String paraMoeda) {
        String parMoeda = deMoeda + "_" + paraMoeda;
        long tempoAtual = System.currentTimeMillis();
        
        // Verifica se a taxa está em cache e se ainda é válida
        if (taxasCache.containsKey(parMoeda) && 
            tempoAtual - tempoCache.getOrDefault(parMoeda, 0L) < DURACAO_CACHE) {
            return taxasCache.get(parMoeda);
        }
        
        try {
            // Construir URL para a requisição
            String urlStr = BASE_URL + "?q=" + parMoeda + "&compact=ultra&apiKey=" + API_KEY;
            URL url = new URL(urlStr);
            
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            
            // Verificar se a requisição foi bem-sucedida
            int codigoResposta = conexao.getResponseCode();
            
            if (codigoResposta == HttpURLConnection.HTTP_OK) {
                BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                StringBuilder resposta = new StringBuilder();
                String linha;
                
                while ((linha = leitor.readLine()) != null) {
                    resposta.append(linha);
                }
                leitor.close();
                
                // Parsear a resposta JSON
                JSONObject json = new JSONObject(resposta.toString());
                
                // Verificar se o par de moedas foi encontrado
                if (json.has(parMoeda)) {
                    double taxa = json.getDouble(parMoeda);
                    
                    // Armazenar no cache
                    taxasCache.put(parMoeda, taxa);
                    tempoCache.put(parMoeda, tempoAtual);
                    
                    return taxa;
                } else {
                    System.out.println("Erro: Par de moedas " + parMoeda + " não encontrado.");
                }
            } else {
                System.out.println("Erro na requisição à API: " + codigoResposta);
            }
        } catch (IOException e) {
            System.out.println("Erro ao acessar a API: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
        
        return -1; // Indica erro na obtenção da taxa
    }
    
    /**
     * Limpa a tela do console
     */
    private static void limparTela() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Em caso de erro ao limpar a tela, apenas imprime várias linhas em branco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Pausa o programa até que o usuário pressione Enter
     */
    private static void pressioneEnterParaContinuar() {
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }
}