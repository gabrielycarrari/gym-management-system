package src.javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.dao.CheckInDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;
import src.javafxmvc.model.domain.Aluno;

public class AnchorPaneGraficoController implements Initializable {

    @FXML
    private PieChart pieChartGenero;

    @FXML
    private CategoryAxis xAxis = new CategoryAxis();
    
    @FXML
    private NumberAxis yAxis = new NumberAxis();

    @FXML
    private BarChart<String, Number> barChartCheckIns = new BarChart<>(xAxis, yAxis);
        
    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");;
    private final Connection connection = database.conectar();
    private final AlunoDAO alunoDAO = new AlunoDAO();
    private final CheckInDAO checkinDAO = new CheckInDAO();

    private List<Aluno> listAlunos;
    ArrayList<PieChart.Data> pieChartGeneroData = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alunoDAO.setConnection(connection);
        checkinDAO.setConnection(connection);

        loadPieChartGenero();
        loadBarChartChekIns();
    }

    public void loadPieChartGenero(){
        listAlunos = alunoDAO.list();

        int numAlunos = listAlunos.size();
        int numFeminino = 0;
        int numMasculino = 0;

        for (Aluno aluno : listAlunos) {
            if (aluno.getGenero().equals("Feminino")) {
                numFeminino++;
            } else if (aluno.getGenero().equals("Masculino")) {
                numMasculino++;
            }
        }

        double pctFeminino = (double)numFeminino / numAlunos * 100;
        double pctMasculino = (double)numMasculino / numAlunos * 100;

        PieChart.Data generoFeminino = new PieChart.Data("Feminino (" + String.format("%.2f", pctFeminino) + "%)", pctFeminino);
        PieChart.Data generoMasculino = new PieChart.Data("Masculino (" + String.format("%.2f", pctMasculino) + "%)", pctMasculino);

        // Adicione os objetos PieChart.Data à lista de dados do gráfico de pizza
        pieChartGeneroData.add(generoFeminino);
        pieChartGeneroData.add(generoMasculino);

        // Defina os dados do gráfico de pizza
        pieChartGenero.setData(FXCollections.observableArrayList(pieChartGeneroData));
    }

    public void loadBarChartChekIns(){
        
        //barChartCheckIns.setTitle("Total de Check-Ins por Mês");
        
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Check-Ins");

        List<Object[]> monthList = checkinDAO.extractMonth();
        for (Object[] row : monthList) {
            int month = (int) row[0];
            int total = (int) row[1];
            String monthName = getMonthName(month);
            dataSeries.getData().add(new XYChart.Data<>(monthName, total));
        }

        ObservableList<XYChart.Series<String, Number>> chartData = FXCollections.observableArrayList();
        chartData.add(dataSeries);

        barChartCheckIns.setData(chartData);
    }

    private String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Janeiro";
            case 2:
                return "Fevereiro";
            case 3:
                return "Março";
            case 4:
                return "Abril";
            case 5:
                return "Maio";
            case 6:
                return "Junho";
            case 7:
                return "Julho";
            case 8:
                return "Agosto";
            case 9:
                return "Setembro";
            case 10:
                return "Outubro";
            case 11:
                return "Novembro";
            case 12:
                return "Dezembro";
            default:
                return "";
        }
    }
}
