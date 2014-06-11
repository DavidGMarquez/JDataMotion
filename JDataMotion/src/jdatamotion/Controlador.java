package jdatamotion;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pablo Pérez Romaní
 */
class Controlador implements Sesionizable {

    public static final int IMPORTAR_FICHEIRO = 0;
    public static final int ABRIR_SESION = 1;
    public static final int GARDAR_SESION = 2;
    public static final int EXPORTAR_FICHEIRO = 3;
    public static final int MUDAR_DATO = 4;
    public static final int ENGADIR_DATOS = 5;
    public static final int ELIMINAR_DATOS = 6;
    public static final int DESFACER = 7;
    public static final int REFACER = 9;
    public static final int RESTAURAR = 10;
    public static final int RESTAURAR_CO_FICHEIRO = 11;
    public static final int MUDAR_INDICE_TEMPORAL = 12;
    public static final int MUDAR_TIPO = 13;
    public static final int MUDAR_NOME_RELACION = 14;
    public static final boolean debug = true;
    private Modelo meuModelo;
    private Vista minaVista;
    private XestorComandos xestorComandos;

    public Controlador() {
        xestorComandos = new XestorComandos();
    }

    public void inicializar(Modelo modelo, Vista vista) {
        meuModelo = modelo;
        minaVista = vista;
    }

    public void reset() {
        xestorComandos.vaciarPilaDesfacer();
        xestorComandos.vaciarPilaRefacer();
    }

    private void importarFicheiro(String url) {
        try {
            reset();
            minaVista.reset();
            xestorComandos.ExecutarComando(new ComandoImportarFicheiro(meuModelo, url));
        } catch (Exception ex) {
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void manexarEvento(int opcion, Object argumento) {
        switch (opcion) {
            case IMPORTAR_FICHEIRO:
                importarFicheiro((String) argumento);
                break;
            case ABRIR_SESION:
                abrirSesion((String) argumento);
                break;
            case GARDAR_SESION:
                gardarSesion((String) argumento);
                break;
            case EXPORTAR_FICHEIRO:
                exportarFicheiro((String) ((Object[]) argumento)[1], (String) ((Object[]) argumento)[0]);
                break;
            case MUDAR_DATO:
                mudarDato((int) ((Object[]) argumento)[0], (int) ((Object[]) argumento)[1], ((Object[]) argumento)[2]);
                break;
            case ENGADIR_DATOS:
                engadirDatos();
                break;
            case ELIMINAR_DATOS:
                eliminarDatos(((Integer[]) argumento));
                break;
            case DESFACER:
                desfacer();
                break;
            case REFACER:
                refacer();
                break;
            case RESTAURAR:
                restaurar();
                break;
            case MUDAR_INDICE_TEMPORAL:
                mudarIndiceTemporal((int) argumento);
                break;
            case MUDAR_TIPO:
                mudarTipo((int) ((Object[]) argumento)[0], (int) ((Object[]) argumento)[1]);
                break;
            case MUDAR_NOME_RELACION:
                mudarNomeRelacion(((String) argumento));
                break;
        }
        meuModelo.update();
    }

    private void mudarNomeRelacion(String nomeRelacion) {
        try {
            xestorComandos.ExecutarComando(new ComandoMudarNomeRelacion(meuModelo, nomeRelacion));
        } catch (Exception ex) {
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void desfacer() {
        try {
            xestorComandos.Desfacer();
        } catch (Exception ex) {
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void refacer() {
        try {
            xestorComandos.Refacer();
        } catch (Exception ex) {
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void gardarSesion(String path) {
        try {
            ArrayList<Sesion> sesions = new ArrayList<>();
            sesions.add(meuModelo.obterSesion());
            sesions.add(obterSesion());
            sesions.add(minaVista.obterSesion());
            FileOutputStream saveFile;
            saveFile = new FileOutputStream(path);
            ObjectOutputStream save = new ObjectOutputStream(saveFile);
            save.writeObject(sesions);
        } catch (FileNotFoundException ex) {
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void abrirSesion(String path) {
        try {
            minaVista.reset();
            reset();
            FileInputStream saveFile;
            saveFile = new FileInputStream(path);
            ObjectInputStream restore = new ObjectInputStream(saveFile);
            ArrayList<Sesion> sesions = (ArrayList<Sesion>) restore.readObject();
            for (Sesion s : sesions) {
                aplicarSesionEnObxectivo(s);
            }
        } catch (ExcepcionArquivoModificado ex) {
            minaVista.amosarDialogo("Erro: O arquivo '" + ex.getUrl() + "' foi modificado e as sesións construidas sobre él quedaron corruptas.\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            minaVista.amosarDialogo("Erro:\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void aplicarSesionEnObxectivo(Sesion s) throws Exception {
        if (s instanceof SesionControlador) {
            this.aplicarSesion(s);
        } else if (s instanceof SesionModelo) {
            meuModelo.aplicarSesion(s);
        } else if (s instanceof SesionVista) {
            minaVista.aplicarSesion(s);
        }
    }

    public Modelo getMeuModelo() {
        return meuModelo;
    }

    public Vista getMinaVista() {
        return minaVista;
    }

    private void exportarFicheiro(String path, String extension) {
        try {
            xestorComandos.ExecutarComando(new ComandoExportarFicheiro(meuModelo, path, extension));
        } catch (Exception ex) {
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void mudarDato(int numFila, int numColumna, Object dato) {
        try {
            xestorComandos.ExecutarComando(new ComandoMudarDato(meuModelo, numFila, numColumna, dato));
        } catch (ExcepcionComandoInutil e) {
        } catch (NumberFormatException e) {
            minaVista.amosarDialogo("Erro: o novo dato non pertence ao rango do atributo (" + meuModelo.obterTipoAtributo(numColumna) + ").\n" + e.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (ParseException ex) {
            minaVista.amosarDialogo("Erro: non se puido parsear correctamente o atributo (" + meuModelo.obterTipoAtributo(numColumna) + ").\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            minaVista.amosarDialogo("Erro:\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void engadirDatos() {
        try {
            xestorComandos.ExecutarComando(new ComandoEngadirDatos(meuModelo));
        } catch (NumberFormatException e) {
            minaVista.amosarDialogo("Erro: algún dato novo non pertence ao rango do seu atributo.\n" + e.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (ParseException ex) {
            minaVista.amosarDialogo("Erro: algún dato novo non se pode parsear correctamente.\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            minaVista.amosarDialogo("Erro:\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public XestorComandos getXestorComandos() {
        return xestorComandos;
    }

    private void eliminarDatos(Integer[] datos) {
        try {
            xestorComandos.ExecutarComando(new ComandoEliminarDatos(meuModelo, datos));
        } catch (Exception ex) {
            minaVista.amosarDialogo("Erro:\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public Sesion obterSesion() {
        SesionControlador s = new SesionControlador();
        s.setXestorComandos(getXestorComandos());
        return s;
    }

    public void setXestorComandos(XestorComandos xestorComandos) {
        this.xestorComandos = xestorComandos;
    }

    private void actualizarObxectivosComando(Comando c) {
        if (c.getObxectivo().getClass().equals(Modelo.class)) {
            c.setObxectivo(meuModelo);
        } else if (c.getObxectivo().getClass().equals(Controlador.class)) {
            c.setObxectivo(this);
        } else if (c.getObxectivo().getClass().equals(Vista.class)) {
            c.setObxectivo(minaVista);
        }
    }

    @Override
    public void aplicarSesion(Sesion sesion) throws Exception {
        SesionControlador s = (SesionControlador) sesion;
        setXestorComandos(s.getXestorComandos());
        ArrayList<ComandoDesfacible> comandos = new ArrayList<>(xestorComandos.getPilaDesfacer());
        for (Comando c : comandos) {
            actualizarObxectivosComando(c);
            c.Executar();
        }
        for (Comando c : xestorComandos.getPilaRefacer()) {
            actualizarObxectivosComando(c);
        }
    }

    private void restaurar() {
        try {
            xestorComandos.ExecutarComando(new ComandoRestaurar(this));
        } catch (Exception ex) {
            minaVista.amosarDialogo("Erro:\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
        }
    }

    private void mudarIndiceTemporal(int i) {
        try {
            xestorComandos.ExecutarComando(new ComandoMudarIndiceTemporal(meuModelo, i));
        } catch (ExcepcionFormatoIdentificacionTemporal ex) {
            minaVista.amosarDialogo("Erro: Non se pode empregar o tipo " + Modelo.obterNomeTipo(ex.getTipo()) + " para representar o índice temporal.\nEmpregue o tipo string para representar marcas de tempo ou o tipo numérico para representar unha orde.\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            minaVista.amosarDialogo("Erro:\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void mudarTipo(int columnaTaboa, int tipo) {
        try {
            xestorComandos.ExecutarComando(new ComandoMudarTipo(meuModelo, columnaTaboa, tipo));
        } catch (ExcepcionCambiarTipoAtributo ex) {
            minaVista.amosarDialogo("Erro: Non se pode converter algún dato de tipo " + Modelo.obterNomeTipo(ex.getTipoAntigo()) + " a " + Modelo.obterNomeTipo(ex.getTipoNovo()) + ". Utilizaranse campos nulos para a súa conversión.\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            minaVista.amosarDialogo("Erro:\n" + ex.getMessage(), Vista.ERROR_MESSAGE);
            if (Controlador.debug) {
                Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
