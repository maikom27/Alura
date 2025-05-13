Conversor de Moedas em Tempo Real
Este é um conversor de moedas via console que obtém taxas de câmbio em tempo real através de uma API externa. O programa oferece várias opções de conversão entre diferentes moedas, garantindo dados precisos e atualizados.
Funcionalidades

Interface de console interativa e amigável
6 opções pré-definidas de conversão entre moedas comuns (BRL, USD, EUR)
Opção para conversão personalizada entre quaisquer moedas
Obtenção de taxas de câmbio em tempo real via API
Sistema de cache para evitar requisições desnecessárias à API
Formatação adequada dos valores monetários
Exibição da taxa de conversão atual utilizada

Requisitos

Java 8 ou superior
Biblioteca org.json para processamento de JSON
Conexão com a internet para acesso à API

Como instalar e executar
1. Instalar as dependências
Adicione a biblioteca org.json ao seu projeto. Você pode fazer isso de várias formas:
Usando Maven:
Adicione ao seu arquivo pom.xml:
xml<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20210307</version>
</dependency>
Usando Gradle:
Adicione ao seu arquivo build.gradle:
gradleimplementation 'org.json:json:20210307'
Manualmente:
Baixe o arquivo JAR do site oficial e adicione-o ao classpath do seu projeto.
2. Compilar o código
festançajavac -cp .:json.jar ConversorDeMoedas.java
3. Execute o programa
festançajava -cp .:json.jar ConversorDeMoedas
Como usar

Executar o programa
Selecione uma das opções do menu:

Opções 1-6: Conversões pré-definidas entre Real, Dólar e Euro
Opção 7: Conversão personalizada (você informará os códigos das moedas)
Opção 0: Sair do programa


Digite o valor que deseja conversor
O resultado da conversão será exibido na tela
Pressione Enter para voltar ao menu principal

Códigos de moeda comuns

BRL: Real Brasileiro
USD: Dólar Americano
EUR: Euro
GBP: Libra Esterlina
JPY: Iene Japonês
CAD: Dólar Canadense
AUD: Dólar Australiano
CNY: Yuan Chinês
ARS: Peso Argentino
CLP: Peso Chileno

Sobre a API
Este programa utiliza a API Free Currency Converter para obter taxas de câmbio em tempo real. A API permite um número limitado de requisições gratuitas por hora/dia, então o programa implementa um sistema de cache para minimizar o número de chamadas à API.
Personalização
Você pode facilmente modificar o código para adicionar novas opções de conversão ao menu ou alterar o tempo de duração do cache (atualmente definido como 1 hora).