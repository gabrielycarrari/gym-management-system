package src.javafxmvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.util.Callback;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


import java.net.URL;
import java.sql.Connection;

import src.javafxmvc.model.database.Database;
import src.javafxmvc.model.database.DatabaseFactory;
import src.javafxmvc.model.dao.PagamentoDAO;



public class AnchorPaneRelatorioController implements Initializable {
    @FXML
    private TableView<Object[]> tableViewPagamentos;
    @FXML
    private TableColumn<Object[], Integer> tableColumnIdAluno;
    @FXML
    private TableColumn<Object[], String> tableColumnNomeAluno;
    @FXML
    private TableColumn<Object[], Float> tableColumnValorTotal;
    @FXML
    private TextField textFieldValorTotal;
    @FXML
    private Button buttonPesquisarPags;
    @FXML
    private Button buttonImprimirPags;
    @FXML
    private Label labelErroValorTotal;
    
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final PagamentoDAO pagamentoDAO = new PagamentoDAO();

    private List<Object[]> listPagamentos;
    private ObservableList<Object[]> observableListPagamentos;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        pagamentoDAO.setConnection(connection);
    }
    
    @FXML
    public void handleButtonSearchPags(){
        cleanErrors();
        if(validateData()){
            tableColumnIdAluno.setCellValueFactory(cellData -> new SimpleIntegerProperty((Integer)cellData.getValue()[0]).asObject());
            tableColumnNomeAluno.setCellValueFactory(cellData -> new SimpleStringProperty((String)cellData.getValue()[1]));
            tableColumnValorTotal.setCellValueFactory(new PropertyValueFactory<>(""));
            tableColumnValorTotal.setCellFactory(column -> new TableCell<Object[], Float>() {
                @Override
                protected void updateItem(Float value, boolean empty) {
                    super.updateItem(value, empty);
                    if (empty || value == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", value));
                    }
                }
            });
            tableColumnValorTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Object[], Float>, ObservableValue<Float>>() {
                @Override
                public ObservableValue<Float> call(TableColumn.CellDataFeatures<Object[], Float> p) {
                    return new SimpleFloatProperty(((Number)p.getValue()[2]).floatValue()).asObject();
                }
            });

            listPagamentos = pagamentoDAO.listSumPagamentoPorAluno(Float.parseFloat(textFieldValorTotal.getText()));
            observableListPagamentos = FXCollections.observableArrayList(listPagamentos);
            tableViewPagamentos.setItems(observableListPagamentos);
        }
    }

    @FXML
    public void handleButtonPrintPags() throws JRException{
        if(validateData()){
            HashMap filtro = new HashMap();
            Float valorTotal = Float.parseFloat(textFieldValorTotal.getText());
        
            filtro.put("valorTotal", valorTotal);

            URL url = getClass().getResource("../reports/RelatorioTotalPagamentos.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, filtro, connection);//null: caso não existam filtros
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);//false: não deixa fechar a aplicação principal
            jasperViewer.setVisible(true);
            
        }
    }

    public boolean validateData(){
        // Verifica se o campo valor está vazio
        if(textFieldValorTotal.getText().isEmpty()){
            labelErroValorTotal.setText("Digite um valor!");
            return false;
        }
        
        // verifica se o usuário digitou um número válido
        try {
            Float.parseFloat(textFieldValorTotal.getText());
        } catch (NumberFormatException e) {
            labelErroValorTotal.setText("Digite um número válido. Por exemplo 150.00");
            return false;
        }
        return true;
    }

    public void cleanErrors() {
        labelErroValorTotal.setText("");
    }
}
