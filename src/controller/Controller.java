package controller;

import conection.Conexion;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Main;

import java.net.URL;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
public class Controller {

    private Conexion c = new Conexion();
    private String tablaSelecionada;
    private Pane view;
    private int SecuenciaSQL;


    @FXML
    private BorderPane IDBoreder;

    @FXML
    private TableView<LinkedList[]> table;

    @FXML // sirve para identificar que pestaña se ha abierto
    private Label idScena;

    // metodo para cargar un fxml dentro de otro
    private Pane getPage(String vi) {
        Pane vie = new BorderPane();
        try {
            URL fileUrl = Main.class.getResource("../fxml/" + vi + ".fxml");
            vie = new FXMLLoader().load(fileUrl);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la pagina");
            alert.showAndWait();
        }
        return vie;
    }

    // identifica el nombre de la tabla
    private int identificarSecuencia(String s) {
        if (s.equals("Eliminar Cervezas")) {
            return 0;
        }
        if (s.equals("Eliminar Liquido frio")) {
            return 1;
        }
        if (s.equals("Eliminar Mosto_sin_f")) {
            return 2;
        }
        if (s.equals("Insertar Cervezas")) {
            return 3;
        }
        if (s.equals("Insert Liquido frio")) {
            return 4;
        }
        if (s.equals("Insertar Mosto_sin_f")) {
            return 5;
        }
        if (s.equals("Modificar Cerveza")) {
            return 6;
        }
        if (s.equals("Mod. Liquido frio")) {
            return 7;
        }
        if (s.equals("Modificar Mosto_sin_f")) {
            return 8;
        } else {
            return 999;
        }
    }

    /* metodo para mostrar cualquier tabla
    para ello se le pasa el string con el nombre de la tabla
     */

    private void laGranSolucion(String nombreDeLaCosaABuscar) {
        // si la talb atiene dato sse los elimino
        int cont2 = table.getColumns().size();
        if (table.getColumns().size() > 0) {
            table.getColumns().remove(0, cont2);

        }
        LinkedList<String> listaNombresColumnas;
        LinkedList<String>[] datos;
        LinkedList<Objetos> dat = new LinkedList<Objetos>();
        // abro la conexion
        Conexion cn = new Conexion();
        try {
            cn.abrirConexion();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al abrrir la conexion");
            alert.showAndWait();
        }
        try {
            // cojo la lista de nombres y los datos de la tabla
            listaNombresColumnas = cn.obtenerNombreDeColumnas(nombreDeLaCosaABuscar);
            datos = cn.datosTabla(nombreDeLaCosaABuscar);
            // creo la lista con los objetos
            if (datos[0].size() > 0) {
                for (int i = 0; i < datos[0].size(); i++) {
                    dat.add(new Objetos());
                }
                for (int e = 0; e < datos.length; e++) {
                    int contador = 0;
                    Iterator it = datos[e].iterator();
                    while (it.hasNext()) {
                        String nombre = (String) it.next();
                        dat.get(contador).getDatos().add(nombre);
                        contador++;

                    }
                }
                ObservableList data = FXCollections.observableList(dat);
                table.setItems(data);
                //la tableview es table
                for (int i = 0; i < listaNombresColumnas.size(); i++) {
                    final int cont = i;
                    TableColumn veces = new TableColumn();
                    veces.setText(listaNombresColumnas.get(i));
                    veces.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Objetos, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<Objetos, String> p) {
                            return new SimpleStringProperty(String.valueOf(p.getValue().getDatos().get(cont)));
                        }
                    });
                    table.getColumns().add(veces);
                }
                //table.getColumns().setAll(veces);
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            }
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la tabla");
            alert.showAndWait();
        }
    }






    // sentencia 2
    private void s1() {
        int cont2 = table.getColumns().size();
        if (table.getColumns().size() > 0) {
            table.getColumns().remove(0, cont2);

        }
        LinkedList<String> listaNombresColumnas=new LinkedList<>();
        LinkedList<String>[] datos;
        LinkedList<Objetos> dat = new LinkedList<Objetos>();

        String sentencia = "select year(cec_fecha) as 'Año embotellamiento', msf_tiempo_fermentacion as 'Tiempo Fermentacion' , " +
                "mos_tiempo_maceracion as 'Tiempo Maceracion', agu_denominacion as 'Nombre Agua' " +
                "from cerveza_emb_cab inner join cerveza on cec_año=cer_año " +
                "and cec_numero_sec=cer_numero_sec " +
                "inner join mosto_sin_f on cer_id_material3=msf_id_material3 " +
                "and cer_id_levadura=msf_id_levadura and cer_id_material2=msf_id_material2 and cer_id_agua=msf_id_agua and cer_id_material=msf_id_material and cer_id_malta=msf_id_malta and cer_id_material1=msf_id_material1 and cer_id_lupulo=msf_id_lupulo and msf_tiempo_fermentacion > 12 " +
                "inner join levadura on msf_id_levadura=lev_id_levadura and " +
                "lev_tipo_lev like 'L' " +
                "inner join liquido_frio on msf_id_material2=lif_id_material2 and " +
                "msf_id_agua=lif_id_agua and msf_id_material=lif_id_material and msf_id_malta=lif_id_malta and msf_id_material1=lif_id_material1 and msf_id_lupulo=lif_id_lupulo " +
                "inner join liquido_dulce on lif_id_agua=lid_id_agua and " +
                "lif_id_material=lid_id_material and lif_id_malta=lid_id_malta and lif_id_material1=lid_id_material1 and lif_id_lupulo=lid_id_lupulo " +
                "inner join lupulo on lid_id_lupulo = lup_id_lupulo and " +
                "lup_denominacion like 'Hellertaur Perle' inner join mosto on mos_id_agua=lid_id_agua and " +
                "mos_id_material=lid_id_material and mos_id_malta=lid_id_malta and mos_id_material1=lid_id_material1 " +
                "inner join agua on mos_id_agua=agu_id_agua and agu_cal<3 inner join malta_molida on mos_id_malta=mam_id_malta and " +
                "mos_id_material1=mam_id_material inner join malta on mal_id_malta=mam_id_malta and " +
                "mal_denominacion like 'Trigo' " +
                "where cec_fecha <'2014/01/01' order by 1 asc, 4 desc;";
        try {
            c.abrirConexion();
            LinkedList<String>[] datosDeLaTabla = new LinkedList[4];
            for (int i = 0; i < 4; i++) {
                datosDeLaTabla[i] = new LinkedList<String>();
            }
            // nos creamos una lista para guardar los nombres de las columnas

            c.abrirConexion();
            Statement st = c.getCon().createStatement();
            ResultSet rs = st.executeQuery(sentencia);
            int posicion = 0;
            while (rs.next()) {
                for (int i = 0; i < 4; i++) {
                    datosDeLaTabla[i].add(rs.getString(i + 1));
                }
                posicion++;
            }
            datos = datosDeLaTabla;
            // creo la lista con los objetos

            for (int i = 0; i < datos[0].size(); i++) {
                dat.add(new Objetos());
            }
            for (int e = 0; e < datos.length; e++) {
                int contador = 0;
                Iterator it = datos[e].iterator();
                while (it.hasNext()) {
                    String nombre = (String) it.next();
                    dat.get(contador).getDatos().add(nombre);
                    contador++;

                }
            }
            ObservableList data = FXCollections.observableList(dat);
            table.setItems(data);
            listaNombresColumnas.add("Año Embotellamiento");
            listaNombresColumnas.add("Tiempo Fermentación");
            listaNombresColumnas.add("Tiempo Maceración");
            listaNombresColumnas.add("Nombre Agua");
            //la tableview es table
            for (int i = 0; i < 4; i++) {
                final int cont = i;
                TableColumn veces = new TableColumn();
                veces.setText(listaNombresColumnas.get(i));
                veces.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Objetos, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Objetos, String> p) {
                        return new SimpleStringProperty(String.valueOf(p.getValue().getDatos().get(cont)));
                    }
                });
                table.getColumns().add(veces);
            }
            //table.getColumns().setAll(veces);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        } catch (Exception throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la sentencia");
            alert.showAndWait();
        }


    }

    // sentencia 2
    private void s2() {
        LinkedList<String> listaNombresColumnas=new LinkedList<>();
        LinkedList<String>[] datos;
        LinkedList<Objetos> dat = new LinkedList<Objetos>();

        String sentencia = "\n" +
                "select lup_denominacion as 'Nombre lupulo', count(*) as 'Nº Cervezas embotelladas'  from cerveza_emb_cab inner join cerveza on cec_año=cer_año and cec_numero_sec=cer_numero_sec inner join mosto_sin_f on cer_id_material3=msf_id_material3 and cer_id_levadura=msf_id_levadura and cer_id_material2=msf_id_material2  and cer_id_agua=msf_id_agua and cer_id_material=msf_id_material  and cer_id_malta=msf_id_malta and cer_id_material1=msf_id_material1 and cer_id_lupulo=msf_id_lupulo inner join materiales as a on a.mat_id_material=msf_id_material3 and a.mat_denominacion like 'Barril Lacado'  inner join levadura on msf_id_levadura=lev_id_levadura and lev_tipo_lev like 'A' inner join liquido_frio on msf_id_material2=lif_id_material2 and msf_id_agua=lif_id_agua and msf_id_material=lif_id_material  and msf_id_malta=lif_id_malta and msf_id_material1=lif_id_material1 and msf_id_lupulo=lif_id_lupulo inner join liquido_dulce on lif_id_agua=lid_id_agua and lif_id_material=lid_id_material and lif_id_malta=lid_id_malta  and lif_id_material1=lid_id_material1 and lif_id_lupulo=lid_id_lupulo inner join lupulo on lid_id_lupulo = lup_id_lupulo  inner join cerveza_emb_lin on cel_fecha=cec_fecha and cel_numero=cec_numero inner join materiales as b on cel_id_material=b.mat_id_material  and b.mat_denominacion like 'Botella de cristal negro ahumado' where year(cec_fecha) = 2015 group by lup_denominacion " +
                "having count(*) >300 order by 1 asc, 2 asc ;";
        try {
            c.abrirConexion();
            LinkedList<String>[] datosDeLaTabla = new LinkedList[2];
            for (int i = 0; i < 2; i++) {
                datosDeLaTabla[i] = new LinkedList<String>();
            }
            // nos creamos una lista para guardar los nombres de las columnas

            c.abrirConexion();
            Statement st = c.getCon().createStatement();
            ResultSet rs = st.executeQuery(sentencia);
            int posicion = 0;
            while (rs.next()) {
                for (int i = 0; i < 2; i++) {
                    datosDeLaTabla[i].add(rs.getString(i + 1));
                }
                posicion++;
            }
            datos = datosDeLaTabla;
            // creo la lista con los objetos

            for (int i = 0; i < datos[0].size(); i++) {
                dat.add(new Objetos());
            }
            for (int e = 0; e < datos.length; e++) {
                int contador = 0;
                Iterator it = datos[e].iterator();
                while (it.hasNext()) {
                    String nombre = (String) it.next();
                    dat.get(contador).getDatos().add(nombre);
                    contador++;

                }
            }
            ObservableList data = FXCollections.observableList(dat);
            table.setItems(data);
            listaNombresColumnas.add("Nombre lupulo");
            listaNombresColumnas.add("Nº Cervezas embotelladas");
            //la tableview es table
            for (int i = 0; i < 2; i++) {
                final int cont = i;
                TableColumn veces = new TableColumn();
                veces.setText(listaNombresColumnas.get(i));
                veces.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Objetos, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Objetos, String> p) {
                        return new SimpleStringProperty(String.valueOf(p.getValue().getDatos().get(cont)));
                    }
                });
                table.getColumns().add(veces);
            }
            //table.getColumns().setAll(veces);
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        } catch (Exception throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la sentencia");
            alert.showAndWait();
        }


    }
    // sentencia 3
    private void s3(){
        String s=" update malta, mosto, malta_molida set mal_precio=mal_precio * 1.05 where mos_temperatura>80 and mos_id_malta=mam_id_malta and mos_id_material1=mam_id_material and mam_id_malta=mal_id_malta;";
        String s2=" update mosto, agua set agu_precio=agu_precio*1.05 where mos_temperatura>80 and mos_id_agua=agu_id_agua;";
        try {
            c.abrirConexion();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al abrrir la conexion");
            alert.showAndWait();
        }
        try {
            PreparedStatement ss = c.getCon().prepareStatement(s);
            ss.execute();
            PreparedStatement ss2 = c.getCon().prepareStatement(s2);
            ss2.execute();
            c.cerrarConexion();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la sentencia");
            alert.showAndWait();
        }
        // se vulve a mostrar la tabla de agua con los valores cambiados
        laGranSolucion("agua");
    }


    // eventos para mostrar las diferentes tablas
    @FXML
    void mostrarAgua(ActionEvent event) {

        tablaSelecionada = "agua";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarAguaMin(ActionEvent event) {
        tablaSelecionada = "agua_min";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarCerveza(ActionEvent event) {
        tablaSelecionada = "cerveza";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarCervezaEnCab(ActionEvent event) {
        tablaSelecionada = "cerveza_emb_cab";
        laGranSolucion(tablaSelecionada);

    }

    @FXML
    void mostrarCervezaEnLin(ActionEvent event) {
        tablaSelecionada = "cerveza_emb_lin";
        laGranSolucion(tablaSelecionada);

    }

    @FXML
    void mostrarLevadura(ActionEvent event) {
        tablaSelecionada = "levadura";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarLiquidoDulce(ActionEvent event) {
        tablaSelecionada = "liquido_dulce";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarLiquidoFrio(ActionEvent event) {
        tablaSelecionada = "liquido_frio";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarLupulo(ActionEvent event) {
        tablaSelecionada = "lupulo";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarMalta(ActionEvent event) {
        tablaSelecionada = "malta";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarMaltaMolida(ActionEvent event) {
        tablaSelecionada = "malta_molida";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarMateriales(ActionEvent event) {
        tablaSelecionada = "materiales";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarMosto(ActionEvent event) {
        tablaSelecionada = "mosto";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarMostoSinF(ActionEvent event) {
        tablaSelecionada = "mosto_sin_f";
        laGranSolucion(tablaSelecionada);
    }

    @FXML
    void mostrarPaises(ActionEvent event) {
        tablaSelecionada = "paises";
        laGranSolucion(tablaSelecionada);
    }


    // metodos para selecionar el mantenimiento a realizar
    @FXML
    void deleteCerveza(ActionEvent event) {
        IDBoreder.setRight(getPage("deletecerveza"));
    }

    @FXML
    void deleteLiquido(ActionEvent event) {
        IDBoreder.setRight(getPage("deleteliquidofrio"));
    }

    @FXML
    void deleteMosto(ActionEvent event) {
        IDBoreder.setRight(getPage("deletemostosinf"));
    }

    @FXML
    void insertCerveza(ActionEvent event) {
        IDBoreder.setRight(getPage("insertcerveza"));
    }

    @FXML
    void insertLiquido(ActionEvent event) {
        IDBoreder.setRight(getPage("insertliquidofrio"));
    }

    @FXML
    void insertMosto(ActionEvent event) {
        IDBoreder.setRight(getPage("insertmostosinf"));
    }

    @FXML
    void modifyCerveza(ActionEvent event) {

        IDBoreder.setRight(getPage("modifycerveza"));
    }

    @FXML
    void modifyLiquido(ActionEvent event) {

        IDBoreder.setRight(getPage("modifyliquidofrio"));
    }

    @FXML
    void modifyMosto(ActionEvent event) {

        IDBoreder.setRight(getPage("modifycarmostosinf"));
    }

    @FXML
    void MostrarSentencia1(ActionEvent event) {
        s1();
    }

    @FXML
    void MostrarSentencia2(ActionEvent event) { s2(); }

    @FXML
    void MostrarSentencia3(ActionEvent event) {
        s3();
    }

/*
    OBJETOS FXML DE LIQUIDO FRIO
 */

    @FXML
    private TextField lif_id_material2;

    @FXML
    private TextField lif_id_agua;

    @FXML
    private TextField lif_id_material;

    @FXML
    private TextField lif_id_malta;

    @FXML
    private TextField lif_id_material1;

    @FXML
    private TextField lif_id_lupulo;

    @FXML
    private TextField lif_metodo;

    @FXML
    private TextField lif_temperatura;

    /*
     OBJETOS DE FXML CERVEZA
         */
    @FXML
    private TextField cer_numero_sec;

    @FXML
    private TextField cer_id_material;

    @FXML
    private TextField cer_id_material3;

    @FXML
    private TextField cer_id_levadura;

    @FXML
    private TextField cer_id_material2;

    @FXML
    private TextField cer_id_agua;

    @FXML
    private TextField cer_id_material1;

    @FXML
    private TextField cer_id_lupulo;

    @FXML
    private TextField cer_id_malta;

    @FXML
    private TextField cer_año;

    @FXML
    private TextField cer_id_material4;

    @FXML
    private TextField cer_id_tiempo_trasvase;

    @FXML
    private TextField cer_numero_sec1;

    @FXML
    private TextField cer_año1;

    @FXML
    private TextField cer_tiempo_trasvase;
    /*
    OBJETOS DE FXML mosto
     */
    @FXML
    private TextField msf_id_material3;

    @FXML
    private TextField msf_id_levadura;

    @FXML
    private TextField msf_id_material2;

    @FXML
    private TextField msf_id_agua;

    @FXML
    private TextField msf_id_material;

    @FXML
    private TextField msf_id_malta;

    @FXML
    private TextField msf_id_material1;

    @FXML
    private TextField msf_id_lupulo;

    @FXML
    private TextField msf_tiempo_oxidacion;

    @FXML
    private TextField msf_tiempo_fermentacion;

    @FXML
    private TextField msf_temperatura;

    // metodo aceptar unico para todos los mantenimientos
    @FXML
    void aceptar(ActionEvent event) {
        try {
            c.abrirConexion();
        } catch (Exception e) {
            // mostrar mensaje de error al abrir la conexion
        }
        switch (identificarSecuencia(idScena.getText().toString())) {
            case 0:
                elimCerveza();
                break;
            case 1:
                eliminarLiquido();
                break;
            case 2:
                eleminarMosto();
                break;
            case 3:
                insertarCerveza();
                break;
            case 4:
                insertarLiquido();
                break;
            case 5:
                insertarMosto();
                break;
            case 6:
                modificarCerveza();
                break;
            case 7:
                modificarLiqudio();
                break;
            case 8:
                modificarMosto();
                break;
        }
    }


    // metodos propios de la gestiones de tablas
    private void elimCerveza() {

        try {
            c.abrirConexion();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al abrir la conexio");
            alert.showAndWait();
        }
        try {
            String s = "delete cerveza , cerveza_emb_cab , cerveza_emb_lin from cerveza , cerveza_emb_cab , cerveza_emb_lin where cer_año=cec_año and cer_numero_sec=cec_numero_sec and cec_numero=cel_numero and cec_fecha=cel_fecha and cer_año= ? and cer_numero_sec= ?;";
            PreparedStatement ss = c.getCon().prepareStatement(s);
            ss.setString(1, cer_año.getText().toString());
            ss.setString(2, cer_numero_sec.getText().toString());
            ss.execute();
            c.cerrarConexion();

        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
            alert.showAndWait();
        }
    }

    private void eliminarLiquido() {
        try {
            c.abrirConexion();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al abrrir la conexion");
            alert.showAndWait();
        }
        try {
            String s = "delete liquido_frio, mosto_sin_f, cerveza,cerveza_emb_cab , cerveza_emb_lin from liquido_frio, mosto_sin_f, cerveza,cerveza_emb_cab , cerveza_emb_lin where lif_id_material2= ? and lif_id_agua= ? and lif_id_material= ? and lif_id_malta= ? and lif_id_material1= ? and lif_id_lupulo= ? and lif_id_material2=msf_id_agua and lif_id_agua=msf_id_agua and lif_id_material=msf_id_material and lif_id_malta=msf_id_malta and lif_id_material1=msf_id_material1 and lif_id_lupulo=msf_id_lupulo and cer_id_material3=msf_id_material3 and cer_id_levadura=msf_id_levadura and cer_id_material2=msf_id_material2 and cer_id_material=msf_id_material and cer_id_malta=msf_id_malta and cer_id_material1=msf_id_material1 and cer_id_lupulo=msf_id_lupulo and cer_año=cec_año and cer_numero_sec=cec_numero_sec and cec_numero=cel_numero and cec_fecha=cel_fecha ;";
            PreparedStatement ss = c.getCon().prepareStatement(s);
            ss.setString(1, lif_id_material2.getText().toString());
            ss.setString(2, lif_id_agua.getText().toString());
            ss.setString(3, lif_id_material.getText().toString());
            ss.setString(4, lif_id_malta.getText().toString());
            ss.setString(5, lif_id_material1.getText().toString());
            ss.setString(6, lif_id_lupulo.getText().toString());
            ss.execute();
            c.cerrarConexion();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
            alert.showAndWait();
        }
    }

    private void eleminarMosto() {
        try {
            c.abrirConexion();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al abrrir la conexion");
            alert.showAndWait();
        }
        try {
            String s = "delete mosto_sin_f,cerveza,cerveza_emb_cab , cerveza_emb_lin  from mosto_sin_f,cerveza,cerveza_emb_cab , cerveza_emb_lin  where msf_id_material3= ? and msf_id_levadura= ? and msf_id_material2= ? and msf_id_agua= ? and msf_id_material= ? and msf_id_malta= ? and msf_id_material1= ? and msf_id_lupulo= ? and cer_id_material3=msf_id_material3 and cer_id_levadura=msf_id_levadura and cer_id_material2=msf_id_material2 and cer_id_material=msf_id_material and cer_id_malta=msf_id_malta and cer_id_material1=msf_id_material1 and cer_id_lupulo=msf_id_lupulo and cer_año=cec_año and cer_numero_sec=cec_numero_sec and cec_numero=cel_numero and cec_fecha=cel_fecha  ;";
            ;
            PreparedStatement ss = c.getCon().prepareStatement(s);
            ss.setString(1, msf_id_material3.getText().toString());
            ss.setString(2, msf_id_levadura.getText().toString());
            ss.setString(3, msf_id_material2.getText().toString());
            ss.setString(4, msf_id_agua.getText().toString());
            ss.setString(5, msf_id_material.getText().toString());
            ss.setString(6, msf_id_malta.getText().toString());
            ss.setString(7, msf_id_material1.getText().toString());
            ss.setString(8, msf_id_lupulo.getText().toString());
            ss.execute();
            c.cerrarConexion();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
            alert.showAndWait();
        }
    }

    private void insertarMosto() {
        try {
            c.abrirConexion();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al abrrir la conexion");
            alert.showAndWait();
        }
        try {
            String s = "insert into mosto_sin_f values(?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement ss = c.getCon().prepareStatement(s);
            ss.setString(1, msf_id_material3.getText().toString());
            ss.setString(2, msf_id_levadura.getText().toString());
            ss.setString(3, msf_id_material2.getText().toString());
            ss.setString(4, msf_id_agua.getText().toString());
            ss.setString(5, msf_id_material.getText().toString());
            ss.setString(6, msf_id_malta.getText().toString());
            ss.setString(7, msf_id_material1.getText().toString());
            ss.setString(8, msf_id_lupulo.getText().toString());
            ss.setString(9, msf_tiempo_oxidacion.getText().toString());
            ss.setString(10, msf_tiempo_fermentacion.getText().toString());
            ss.setString(11, msf_temperatura.getText().toString());
            ss.execute();
            c.cerrarConexion();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
            alert.showAndWait();
        }
    }

    private void insertarCerveza() {
        try {
            c.abrirConexion();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al abrrir la conexion");
            alert.showAndWait();
        }
        try {
            String s = "insert into cerveza values(?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement ss = c.getCon().prepareStatement(s);
            ss.setString(1, cer_año.getText().toString());
            ss.setString(2, cer_numero_sec.getText().toString());
            ss.setString(3, cer_id_material4.getText().toString());
            ss.setString(4, cer_id_material3.getText().toString());
            ss.setString(5, cer_id_levadura.getText().toString());
            ss.setString(6, cer_id_material2.getText().toString());
            ss.setString(7, cer_id_agua.getText().toString());
            ss.setString(8, cer_id_material.getText().toString());
            ss.setString(9, cer_id_malta.getText().toString());
            ss.setString(10, cer_id_material1.getText().toString());
            ss.setString(11, cer_id_lupulo.getText().toString());
            ss.setString(12, cer_id_tiempo_trasvase.getText().toString());
            ss.execute();
            c.cerrarConexion();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
            alert.showAndWait();
        }

    }

    private void insertarLiquido() {
        try {
            c.abrirConexion();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al abrrir la conexion");
            alert.showAndWait();
        }
        try {
            String s = "insert into liquido_frio values(?,?,?,?,?,?,?,?);";
            PreparedStatement ss = c.getCon().prepareStatement(s);
            ss.setString(1, lif_id_material2.getText().toString());
            ss.setString(2, lif_id_agua.getText().toString());
            ss.setString(3, lif_id_material.getText().toString());
            ss.setString(4, lif_id_malta.getText().toString());
            ss.setString(5, lif_id_material1.getText().toString());
            ss.setString(6, lif_id_lupulo.getText().toString());
            ss.setString(7, lif_metodo.getText().toString());
            ss.setString(8, lif_temperatura.getText().toString());

            ss.execute();
            c.cerrarConexion();
        } catch (SQLException throwables) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
            alert.showAndWait();
        }
    }

    private void modificarLiqudio() {
        if (lif_metodo.getText().toString().equals("") && lif_temperatura.getText().toString().equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerta");
            alert.setContentText("Alerta no se ha introducido ningun valor");
            alert.showAndWait();
        } else {
            if (!lif_metodo.getText().toString().equals(" ")) {
                String s = " update liquido_frio set lif_metodo= ? where lif_id_material2= ? and lif_id_agua= ? and lif_id_material= ? and lif_id_malta= ? and lif_id_material1= ? and lif_id_lupulo= ? ;";
                try {
                    c.abrirConexion();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al abrrir la conexion");
                    alert.showAndWait();
                }
                try {
                    PreparedStatement ss = c.getCon().prepareStatement(s);
                    ss.setString(1, "'" + lif_metodo.getText().toString() + "'");
                    ss.setString(2, lif_id_material2.getText().toString());
                    ss.setString(3, lif_id_agua.getText().toString());
                    ss.setString(4, lif_id_material.getText().toString());
                    ss.setString(5, lif_id_malta.getText().toString());
                    ss.setString(6, lif_id_material1.getText().toString());
                    ss.setString(7, lif_id_lupulo.getText().toString());
                    ss.execute();
                    c.cerrarConexion();
                } catch (SQLException throwables) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
                    alert.showAndWait();
                }
            }
            if (!lif_temperatura.getText().toString().equals("")) {
                String s = " update liquido_frio set lif_temperatura= ? where lif_id_material2= ? and lif_id_agua= ? and lif_id_material= ? and lif_id_malta= ? and lif_id_material1= ? and lif_id_lupulo= ? ;";
                try {
                    c.abrirConexion();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al abrrir la conexion");
                    alert.showAndWait();
                }
                try {
                    PreparedStatement ss = c.getCon().prepareStatement(s);
                    ss.setString(1, lif_temperatura.getText().toString());
                    ss.setString(2, lif_id_material2.getText().toString());
                    ss.setString(3, lif_id_agua.getText().toString());
                    ss.setString(4, lif_id_material.getText().toString());
                    ss.setString(5, lif_id_malta.getText().toString());
                    ss.setString(6, lif_id_material1.getText().toString());
                    ss.setString(7, lif_id_lupulo.getText().toString());
                    ss.execute();
                    c.cerrarConexion();
                } catch (SQLException throwables) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
                    alert.showAndWait();
                }
            }
        }
    }

    private void modificarCerveza() {
        boolean anio = false;
        boolean num = false;
        if (cer_año1.getText().toString().equals("")
                && cer_numero_sec1.getText().toString().equals("")
                && cer_tiempo_trasvase.getText().toString().equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerta");
            alert.setContentText("Alerta no se ha introducido ningun valor");
            alert.showAndWait();

        } else {

            if (!cer_año1.getText().toString().equals("") && !cer_año1.getText().toString().equals(" ")) {
                String s = " update cerveza , cerveza_emb_cab , cerveza_emb_lin set cer_año= ? , cec_año= ? where cer_año=cec_año and cer_numero_sec=cec_numero_sec and cec_numero=cel_numero and cec_fecha=cel_fecha and cer_año= ? and cer_numero_sec= ? ;";
                try {
                    c.abrirConexion();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al abrrir la conexion");
                    alert.showAndWait();
                }
                try {
                    PreparedStatement ss = c.getCon().prepareStatement(s);
                    ss.setString(1, cer_año1.getText().toString());
                    ss.setString(2, cer_año1.getText().toString());
                    ss.setString(3, cer_año.getText().toString());
                    ss.setString(4, cer_numero_sec.getText().toString());
                    ss.execute();
                    c.cerrarConexion();
                    anio = true;
                } catch (SQLException throwables) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
                    alert.showAndWait();
                }


            }
            if (!cer_numero_sec1.getText().toString().equals(" ") && !cer_numero_sec1.getText().toString().equals("")) {
                String s = " update cerveza , cerveza_emb_cab , cerveza_emb_lin set cer_numero_sec= ? , cec_numero_sec= ? where cer_año=cec_año and cer_numero_sec=cec_numero_sec and cec_numero=cel_numero and cec_fecha=cel_fecha and cer_año= ? and cer_numero_sec= ? ;";
                try {
                    c.abrirConexion();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al abrrir la conexion");
                    alert.showAndWait();
                }
                try {
                    PreparedStatement ss = c.getCon().prepareStatement(s);
                    ss.setString(1, cer_numero_sec1.getText().toString());
                    ss.setString(2, cer_numero_sec1.getText().toString());
                    if (anio = false) {
                        ss.setString(3, cer_año.getText().toString());
                    } else {
                        ss.setString(3, cer_año1.getText().toString());
                    }
                    ss.setString(4, cer_numero_sec.getText().toString());
                    ss.execute();
                    c.cerrarConexion();
                    num = true;
                } catch (SQLException throwables) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
                    alert.showAndWait();
                }

            }
            if (!cer_tiempo_trasvase.getText().toString().equals(" ") && !cer_tiempo_trasvase.getText().toString().equals("")) {
                String s = " update cerveza  set cer_tiempo_trasvase= ? where cer_año= ? and cer_numero_sec= ? ;";
                try {
                    c.abrirConexion();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al abrrir la conexion");
                    alert.showAndWait();
                }
                try {
                    PreparedStatement ss = c.getCon().prepareStatement(s);
                    ss.setString(1, cer_tiempo_trasvase.getText().toString());
                    if (anio = false) {
                        ss.setString(2, cer_año.getText().toString());
                    } else {
                        ss.setString(2, cer_año1.getText().toString());
                    }
                    if (num = false) {
                        ss.setString(3, cer_numero_sec.getText().toString());
                    } else {
                        ss.setString(3, cer_numero_sec1.getText().toString());
                    }
                    ss.execute();
                    c.cerrarConexion();
                } catch (SQLException throwables) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
                    alert.showAndWait();
                }
            }
        }
    }

    private void modificarMosto() {
        if (msf_tiempo_fermentacion.getText().toString().equals("")
                && msf_temperatura.getText().toString().equals("")
                && msf_tiempo_oxidacion.getText().toString().equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerta");
            alert.setContentText("Alerta no se ha introducido ningun valor");
            alert.showAndWait();

        } else {
            if (!msf_tiempo_oxidacion.getText().toString().equals(" ") && !msf_tiempo_oxidacion.getText().toString().equals("")) {
                String s = " update mosto_sin_f set msf_tiempo_oxidacion = ? where msf_id_material3 = ? and  msf_id_levadura = ? and  msf_id_material2 = ? and msf_id_agua = ? and msf_id_material = ? and msf_id_malta= ? and msf_id_material1= ? and msf_id_lupulo= ? ;";
                try {
                    c.abrirConexion();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al abrrir la conexion");
                    alert.showAndWait();
                }
                try {
                    PreparedStatement ss = c.getCon().prepareStatement(s);
                    ss.setString(1, msf_tiempo_oxidacion.getText().toString());
                    ss.setString(2, msf_id_material3.getText().toString());
                    ss.setString(3, msf_id_levadura.getText().toString());
                    ss.setString(4, msf_id_material2.getText().toString());
                    ss.setString(5, msf_id_agua.getText().toString());
                    ss.setString(6, msf_id_material.getText().toString());
                    ss.setString(7, msf_id_malta.getText().toString());
                    ss.setString(8, msf_id_material1.getText().toString());
                    ss.setString(9, msf_id_lupulo.getText().toString());
                    ss.execute();
                    c.cerrarConexion();
                } catch (SQLException throwables) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
                    alert.showAndWait();
                }
            }
            if (!msf_temperatura.getText().toString().equals(" ") && !msf_temperatura.getText().toString().equals("")) {
                String s = " update mosto_sin_f set msf_temperatura =  ? where msf_id_material3 = ? and  msf_id_levadura = ? and  msf_id_material2 = ? and msf_id_agua = ? and msf_id_material = ? and msf_id_malta= ? and msf_id_material1= ? and msf_id_lupulo= ? ;";
                try {
                    c.abrirConexion();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al abrrir la conexion");
                    alert.showAndWait();
                }
                try {
                    PreparedStatement ss = c.getCon().prepareStatement(s);
                    ss.setString(1, msf_temperatura.getText().toString());
                    ss.setString(2, msf_id_material3.getText().toString());
                    ss.setString(3, msf_id_levadura.getText().toString());
                    ss.setString(4, msf_id_material2.getText().toString());
                    ss.setString(5, msf_id_agua.getText().toString());
                    ss.setString(6, msf_id_material.getText().toString());
                    ss.setString(7, msf_id_malta.getText().toString());
                    ss.setString(8, msf_id_material1.getText().toString());
                    ss.setString(9, msf_id_lupulo.getText().toString());
                    ss.execute();
                    c.cerrarConexion();
                } catch (SQLException throwables) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
                    alert.showAndWait();
                }
            }
            if (!msf_tiempo_fermentacion.getText().toString().equals(" ") && !msf_tiempo_fermentacion.getText().toString().equals("")) {
                String s = " update mosto_sin_f set msf_tiempo_fermentacion = ? where msf_id_material3 = ? and  msf_id_levadura = ? and  msf_id_material2 = ? and msf_id_agua = ? and msf_id_material = ? and msf_id_malta= ? and msf_id_material1= ? and msf_id_lupulo= ? ;";
                try {
                    c.abrirConexion();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al abrrir la conexion");
                    alert.showAndWait();
                }
                try {
                    PreparedStatement ss = c.getCon().prepareStatement(s);
                    ss.setString(1, msf_tiempo_fermentacion.getText().toString());
                    ss.setString(2, msf_id_material3.getText().toString());
                    ss.setString(3, msf_id_levadura.getText().toString());
                    ss.setString(4, msf_id_material2.getText().toString());
                    ss.setString(5, msf_id_agua.getText().toString());
                    ss.setString(6, msf_id_material.getText().toString());
                    ss.setString(7, msf_id_malta.getText().toString());
                    ss.setString(8, msf_id_material1.getText().toString());
                    ss.setString(9, msf_id_lupulo.getText().toString());
                    ss.execute();
                    c.cerrarConexion();
                } catch (SQLException throwables) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("Error al cargar la sentencia, los datos pueden ser erroneos");
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    void salir(ActionEvent event) {
        closeStage(event);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
