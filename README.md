# Automato
  - Construção autômato AFND
  - Construção autômato AFD
  - Analisador léxico
  - Fita de saída dos tokens que foram reconhecidos
  - Tabela de símbolos 

## Uso

  - Se quiser testar com outros tokens/regras, altere o arquivo "entrada_automato.txt" para construir o autômato AFD/AFND
  - Se quiser testar o reconhecimento de outros tokens, coloque suas alterações no arquivo "entrada_reconhece.txt"
## Funcionamento

  - Programa le tokens/regras do arquivo "entrada_automato.txt" e constroi o AFD e AFND
  - Progrma le tokens do arquivo "entrada_reconhece.txt" e faz análise montando tabela  de saída e tabela de símbolos
    
## Para rodar:
  - Necessário Java => 21.0.2
### Linux
```
javac -d ./out  ./*.java 
```
```
java -cp ./out  ./Main.java 
```
### Windows

```
javac -d .\out  *.java 
```
```
java -cp .\out .\Main.java 
```
