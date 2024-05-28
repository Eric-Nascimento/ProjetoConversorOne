import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Conversao {
    private String moedaBase;
    private  String moedaFinal;
    private double quantidade;
    private double valorConvertido;
    private Date dataHora;

    public Conversao(String moedaBase, String moedaFinal, double quantidade, double valorConvertido){
        this.moedaBase = moedaBase;
        this.moedaFinal = moedaFinal;
        this.quantidade = quantidade;
        this.valorConvertido = valorConvertido;
        this.dataHora = new Date();
    }

    public String toString(){
        SimpleDateFormat mascaraData = new  SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return String.format("Conversão de %.2f %s para %.2f %s (Data: %s)", quantidade, moedaBase, valorConvertido, moedaFinal, mascaraData.format(dataHora));
    }

    public void salvarHistorico(String arquivo){
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivo, true))) {
            escritor.write(this.toString());
            escritor.newLine();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
