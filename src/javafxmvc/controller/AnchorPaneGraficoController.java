package src.javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import src.javafxmvc.model.dao.AlunoDAO;
import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;
import src.javafxmvc.model.domain.Aluno;

public class AnchorPaneGraficoController implements Initializable {

    @FXML
    private PieChart pieChartGenero;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");;
    private final Connection connection = database.conectar();
    private final AlunoDAO alunoDAO = new AlunoDAO();

    private List<Aluno> listAlunos;
    ArrayList<PieChart.Data> pieChartGeneroData = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        alunoDAO.setConnection(connection);

        loadPieChartGenero();
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
}
