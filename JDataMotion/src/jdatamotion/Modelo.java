package jdatamotion;

import jdatamotion.sesions.SesionModelo;
import jdatamotion.sesions.Sesionizable;
import jdatamotion.sesions.Sesion;
import jdatamotion.excepcions.ExcepcionFormatoIdentificacionTemporal;
import jdatamotion.excepcions.ExcepcionArquivoModificado;
import jdatamotion.excepcions.ExcepcionComandoInutil;
import jdatamotion.excepcions.ExcepcionCambiarTipoAtributo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Observable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.InstanceComparator;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.StringToNominal;

/**
 *
 * @author Pablo Pérez Romaní
 */
public class Modelo extends Observable implements Sesionizable {

    private InstancesComparable atributos;
    private int indiceTemporal;
    private String direccionAoFicheiro;
    private byte[] hashCode;
    private int indiceAtributoNominal;

    public int getIndiceAtributoNominal() {
        return indiceAtributoNominal;
    }

    public void setHashCode(byte[] hashCode) {
        this.hashCode = hashCode;
    }

    public byte[] getHashCode() {
        return hashCode;
    }

    public void setAtributos(InstancesComparable atributos) {
        this.atributos = atributos;
        setChanged();
    }

    public String obterNomeAtributo(int columna) {
        return atributos.attribute(columna).name();
    }

    public void setIndiceAtributoNominal(int indiceAtributoNominal) {
        this.indiceAtributoNominal = indiceAtributoNominal;
    }

    public void setIndiceTemporal(int indiceTemporal) throws ExcepcionFormatoIdentificacionTemporal {
        if (indiceTemporal > -1) {
            int tipo = atributos.attribute(indiceTemporal).type();
            if (tipo != Attribute.NUMERIC && tipo != Attribute.STRING) {
                throw new ExcepcionFormatoIdentificacionTemporal(tipo);
            }
        }
        this.indiceTemporal = indiceTemporal;
        setChanged();
    }

    public Modelo() {
        super();
        atributos = null;
        indiceTemporal = -1;
        indiceAtributoNominal = -1;
    }

    public InstancesComparable getAtributos() {
        return atributos;
    }

    public void setDireccionAoFicheiro(String direccionAoFicheiro) {
        this.direccionAoFicheiro = direccionAoFicheiro;
    }

    public int getIndiceTemporal() {
        return indiceTemporal;
    }

    public String getDireccionAoFicheiro() {
        return direccionAoFicheiro;
    }

    public int obterNumInstancias() {
        return atributos.numInstances();
    }

    public int obterNumAtributos() {
        return atributos.numAttributes();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Modelo == false) {
            return false;
        }
        Modelo m = (Modelo) o;
        return EqualsBuilder.reflectionEquals(this, m, new String[]{"changed", "obs"});
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.atributos);
        hash = 53 * hash + this.indiceTemporal;
        hash = 53 * hash + Objects.hashCode(this.direccionAoFicheiro);
        hash = 53 * hash + Arrays.hashCode(this.hashCode);
        hash = 53 * hash + this.indiceAtributoNominal;
        return hash;
    }

    public ArrayList<ArrayList<Object>> obterArrayListStringDatos(boolean caracterParaNulos) {
        ArrayList<ArrayList<Object>> datos = new ArrayList<>(obterNumInstancias());
        int numInstancias = obterNumInstancias();
        int numAtributos = obterNumAtributos();
        for (int i = 0; i < numInstancias; i++) {
            ArrayList<Object> tupla = new ArrayList<>(obterNumAtributos());
            for (int j = 0; j < numAtributos; j++) {
                tupla.add(j, obterStringDato(i, j, caracterParaNulos));
            }
            datos.add(i, tupla);
        }
        return datos;
    }

    public String obterTipoAtributo(int indice) {
        String nome = "";
        switch (atributos.attribute(indice).type()) {
            case Attribute.DATE:
                nome = "data";
                break;
            case Attribute.NOMINAL:
                nome = "nominal";
                break;
            case Attribute.NUMERIC:
                nome = "numérico";
                break;
            case Attribute.RELATIONAL:
                nome = "relacional";
                break;
            case Attribute.STRING:
                nome = "string";
                break;
        }
        return nome;
    }

    public static String obterNomeTipo(int tipo) {
        String nome = "";
        switch (tipo) {
            case Attribute.DATE:
                nome = "data";
                break;
            case Attribute.NOMINAL:
                nome = "nominal";
                break;
            case Attribute.NUMERIC:
                nome = "numérico";
                break;
            case Attribute.RELATIONAL:
                nome = "relacional";
                break;
            case Attribute.STRING:
                nome = "string";
                break;
        }
        return nome;
    }

    public ArrayList<Attribute> obterArrayListAtributos() {
        ArrayList<Attribute> cabeceiras = new ArrayList<>();
        Enumeration e = getAtributos().enumerateAttributes();
        while (e.hasMoreElements()) {
            cabeceiras.add((Attribute) e.nextElement());
        }
        return cabeceiras;
    }

    public ArrayList<String> obterArrayListNomesAtributos() {
        int numAtributos = obterNumAtributos();
        ArrayList<String> cabeceiras = new ArrayList<>(numAtributos);
        Enumeration e = getAtributos().enumerateAttributes();
        for (int i = 0; i < numAtributos; i++) {
            cabeceiras.add(i, ((Attribute) e.nextElement()).name());
        }
        return cabeceiras;
    }

    public void restaurar() throws Exception {
        importarFicheiro(direccionAoFicheiro);
    }

    public byte[] resumirFicheiroSHA1(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream fis = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }
        return digest.digest();
    }

    public void update() {
        notifyObservers();
        clearChanged();
    }

    public ArrayList<Integer> obterIndicesAtributosNumericosNoModelo() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < obterNumAtributos(); i++) {
            if (atributos.attribute(i).isNumeric()) {
                indices.add(i);
            }
        }
        return indices;
    }

    public ArrayList<Integer> obterIndicesAtributosNominaisNoModelo() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < obterNumAtributos(); i++) {
            if (atributos.attribute(i).isNominal()) {
                indices.add(i);
            }
        }
        return indices;
    }

    public void importarFicheiro(String url) throws Exception {
        direccionAoFicheiro = url;
        hashCode = resumirFicheiroSHA1(new File(url));
        indiceTemporal = -1;
        indiceAtributoNominal = -1;
        String extension = "";
        int j = url.lastIndexOf('.');
        if (j >= 0) {
            extension = url.substring(j + 1);
        }
        switch (extension) {
            case "csv":
                CSVLoader loaderCSV = new CSVLoader();
                loaderCSV.setSource(new File(url));
                atributos = new InstancesComparable(loaderCSV.getDataSet());
                break;
            case "arff":
                ArffLoader loaderARFF = new ArffLoader();
                loaderARFF.setFile(new File(url));
                atributos = new InstancesComparable(loaderARFF.getDataSet());
                break;
        }
        setChanged();
    }

    public void exportarFicheiro(String path, String extension) throws IOException {
        switch (extension) {
            case "csv":
                CSVSaver saverCSV = new CSVSaver();
                saverCSV.setInstances(atributos);
                saverCSV.setFile(new File(path));
                saverCSV.writeBatch();
                break;
            case "arff":
                ArffSaver saverARFF = new ArffSaver();
                saverARFF.setInstances(atributos);
                saverARFF.setFile(new File(path));
                saverARFF.writeBatch();
                break;
        }
    }

    @Override
    public Sesion obterSesion() {
        SesionModelo s = new SesionModelo();
        ArrayList<Attribute> aux = new ArrayList<>();
        for (int i = 0; i < atributos.numAttributes(); i++) {
            aux.add(extraerCabeceira(atributos.attribute(i).name(), atributos.attribute(i).type(), atributos.attribute(i).enumerateValues()));
        }
        InstancesComparable cabeceras = new InstancesComparable(atributos.relationName(), aux, 0);
        s.setCabeceiras(cabeceras);
        s.setDireccionAoFicheiro(getDireccionAoFicheiro());
        s.setIndiceTemporal(getIndiceTemporal());
        s.setHash(getHashCode());
        s.setIndiceAtributoNominal(getIndiceAtributoNominal());
        return s;
    }

    @Override
    public void aplicarSesion(Sesion sesion) throws Exception {
        SesionModelo s = (SesionModelo) sesion;
        if (!Arrays.equals(resumirFicheiroSHA1(new File(s.getDireccionAoFicheiro())), s.getHash())) {
            throw new ExcepcionArquivoModificado(s.getDireccionAoFicheiro());
        }
        atributos = s.getCabeceiras();
        indiceTemporal = s.getIndiceTemporal();
        hashCode = s.getHash();
        direccionAoFicheiro = s.getDireccionAoFicheiro();
        importarFicheiro(s.getDireccionAoFicheiro());
        indiceAtributoNominal = s.getIndiceAtributoNominal();
    }

    private Attribute extraerCabeceira(String nome, int type, Object argumento) {
        Attribute attr = null;
        switch (type) {
            case Attribute.DATE:
                attr = new Attribute(nome, "dd-MM-yyyy");
                break;
            case Attribute.NOMINAL:
                ArrayList<String> myNomVals = new ArrayList<>();
                while (((Enumeration) argumento).hasMoreElements()) {
                    myNomVals.add((String) ((Enumeration) argumento).nextElement());
                }
                attr = new Attribute(nome, myNomVals);
                break;
            case Attribute.NUMERIC:
                attr = new Attribute(nome);
                break;
            case Attribute.RELATIONAL:
                attr = new Attribute(nome, (InstancesComparable) argumento);
                break;
            case Attribute.STRING:
                attr = new Attribute(nome, (ArrayList<String>) null);
                break;
        }
        return attr;
    }

    public void engadirDatos() {
        getAtributos().add(new DenseInstance(obterNumAtributos()));
        setChanged();
    }

    public Object obterDato(int fila, int columna) {
        Object valor = null;
        switch (getAtributos().attribute(columna).type()) {
            case Attribute.DATE:
            case Attribute.RELATIONAL:
            case Attribute.STRING:
                valor = getAtributos().get(fila).attribute(columna).value((int) getAtributos().get(fila).value(columna));
                break;
            case Attribute.NOMINAL:
                valor = (Double.isNaN(getAtributos().get(fila).value(columna)) ? "" : getAtributos().get(fila).attribute(columna).value((int) getAtributos().get(fila).value(columna)));
                break;
            case Attribute.NUMERIC:
                valor = getAtributos().get(fila).value(columna);
                break;
        }
        return valor;
    }

    public String obterStringDato(int fila, int columna, boolean caracterParaNulos) {
        String valor = getAtributos().get(fila).toString(columna);
        String dato = "";
        if ("?".equals(valor)) {
            if (caracterParaNulos) {
                dato = "?";
            }
        } else {
            if (valor.startsWith("'") && valor.endsWith("'")) {
                dato = valor.substring(1, valor.length() - 1).replace("\\", "");
            } else {
                dato = valor.replace("\\", "");
            }
        }
        return dato;
    }

    public void mudarDato(int fila, int columna, Object valor) throws Exception {
        try {
            String stringValor = valor.toString();
            if (obterDato(fila, columna).toString().equals(valor.toString()) || ("".equals(stringValor) && getAtributos().get(fila).isMissing(columna))) {
                throw new ExcepcionComandoInutil();
            }
            if ("".equals(stringValor)) {
                getAtributos().get(fila).setMissing(columna);
            } else {
                switch (getAtributos().attribute(columna).type()) {
                    case Attribute.DATE:

                        getAtributos().get(fila).setValue(columna, getAtributos().attribute(columna).parseDate(stringValor));
                        break;
                    case Attribute.NOMINAL:
                    case Attribute.RELATIONAL:
                    case Attribute.STRING:
                        getAtributos().get(fila).setValue(columna, stringValor);
                        break;
                    case Attribute.NUMERIC:
                        getAtributos().get(fila).setValue(columna, Double.parseDouble(stringValor));
                        break;
                }
            }
        } catch (NumberFormatException | ParseException | ExcepcionComandoInutil e) {
            throw e;
        } finally {
            setChanged();
        }
    }

    public void eliminarDatos(Integer[] datos) {
        ArrayList<Instance> candidatos = new ArrayList<>();
        for (Integer i : datos) {
            candidatos.add(getAtributos().get(i));
        }
        candidatos.stream().forEach((in) -> {
            getAtributos().remove(in);
        });
        setChanged();
    }

    public void mudarNomeRelacion(String nomeRelacion) {
        atributos.setRelationName(nomeRelacion);
        setChanged();
    }

    public void mudarTipo(int columnaTaboa, int novoTipo) throws Exception {
        try {
            String nomeRelacion = atributos.relationName();
            int antigoTipo = atributos.attribute(columnaTaboa).type();
            switch (antigoTipo) {
                case Attribute.STRING:
                    switch (novoTipo) {
                        case Attribute.NOMINAL:
                            StringToNominal filtro = new StringToNominal();
                            filtro.setOptions(new String[]{"-R", String.valueOf(columnaTaboa + 1)});
                            filtro.setInputFormat(atributos);
                            atributos = new InstancesComparable(Filter.useFilter(atributos, filtro));
                            break;
                        case Attribute.NUMERIC:
                            ArrayList<Attribute> temp = new ArrayList<>();
                            Enumeration e = atributos.enumerateInstances();
                            Attribute at;
                            boolean erroParseo = false;
                            for (int i = 0; i < atributos.numAttributes(); i++) {
                                if (i != columnaTaboa) {
                                    at = atributos.attribute(i);
                                } else {
                                    at = new Attribute(atributos.attribute(i).name());
                                }
                                temp.add(at);
                            }
                            InstancesComparable tempInstances = new InstancesComparable(atributos.relationName(), temp, 0);
                            while (e.hasMoreElements()) {
                                Instance in = (Instance) e.nextElement();
                                try {
                                    in.setValue(columnaTaboa, Double.parseDouble(in.stringValue(columnaTaboa)));
                                } catch (NumberFormatException ex) {
                                    in.setMissing(columnaTaboa);
                                    erroParseo = true;
                                }
                                tempInstances.add(in);
                            }
                            atributos = tempInstances;
                            if (erroParseo) {
                                throw new ExcepcionCambiarTipoAtributo(antigoTipo, novoTipo);
                            }
                            break;
                        case Attribute.DATE:
                            temp = new ArrayList<>();
                            e = atributos.enumerateInstances();
                            erroParseo = false;
                            for (int i = 0; i < atributos.numAttributes(); i++) {
                                if (i != columnaTaboa) {
                                    at = atributos.attribute(i);
                                } else {
                                    at = new Attribute(atributos.attribute(i).name(), "yyyy-MM-dd");
                                }
                                temp.add(at);
                            }
                            tempInstances = new InstancesComparable(atributos.relationName(), temp, 0);
                            while (e.hasMoreElements()) {
                                Instance in = (Instance) e.nextElement();
                                try {
                                    in.setValue(columnaTaboa, tempInstances.attribute(columnaTaboa).parseDate(in.stringValue(columnaTaboa)));
                                } catch (ParseException ex) {
                                    in.setMissing(columnaTaboa);
                                    erroParseo = true;
                                }
                                tempInstances.add(in);
                            }
                            atributos = tempInstances;
                            if (erroParseo) {
                                throw new ExcepcionCambiarTipoAtributo(antigoTipo, novoTipo);
                            }
                            break;
                    }
                    break;
                case Attribute.NOMINAL:
                    switch (novoTipo) {
                        case Attribute.NUMERIC:
                            mudarTipo(columnaTaboa, Attribute.STRING);
                            mudarTipo(columnaTaboa, Attribute.NUMERIC);
                            break;
                        case Attribute.STRING:
                            NominalToString filtro = new NominalToString();
                            filtro.setOptions(new String[]{"-C", String.valueOf(columnaTaboa + 1)});
                            filtro.setInputFormat(atributos);
                            atributos = new InstancesComparable(Filter.useFilter(atributos, filtro));
                            break;
                        case Attribute.DATE:
                            mudarTipo(columnaTaboa, Attribute.STRING);
                            mudarTipo(columnaTaboa, Attribute.DATE);
                            break;
                    }
                    if (columnaTaboa == indiceAtributoNominal) {
                        indiceAtributoNominal = -1;
                    }
                    break;
                case Attribute.DATE:
                    switch (novoTipo) {
                        case Attribute.NUMERIC:
                            mudarTipo(columnaTaboa, Attribute.STRING);
                            mudarTipo(columnaTaboa, Attribute.NUMERIC);
                            break;
                        case Attribute.STRING:
                            ArrayList<Attribute> temp = new ArrayList<>();
                            Attribute at;
                            for (int i = 0; i < atributos.numAttributes(); i++) {
                                if (i != columnaTaboa) {
                                    at = atributos.attribute(i);
                                } else {
                                    at = new Attribute(atributos.attribute(i).name(), (ArrayList<String>) null);
                                }
                                temp.add(at);
                            }
                            InstancesComparable tempInstances = new InstancesComparable(atributos.relationName(), temp, atributos.numInstances());
                            for (int i = 0; i < atributos.numInstances(); i++) {
                                tempInstances.add(atributos.instance(i));
                                tempInstances.instance(i).setValue(columnaTaboa, obterStringDato(i, columnaTaboa, false));
                            }
                            atributos = tempInstances;
                            break;
                        case Attribute.NOMINAL:
                            mudarTipo(columnaTaboa, Attribute.STRING);
                            mudarTipo(columnaTaboa, Attribute.NOMINAL);
                            break;
                    }
                    break;
                case Attribute.NUMERIC:
                    switch (novoTipo) {
                        case Attribute.NOMINAL:
                            NumericToNominal filtro = new NumericToNominal();
                            filtro.setOptions(new String[]{"-R", String.valueOf(columnaTaboa + 1)});
                            filtro.setInputFormat(atributos);
                            atributos = new InstancesComparable(Filter.useFilter(atributos, filtro));
                            break;
                        case Attribute.STRING:
                            mudarTipo(columnaTaboa, Attribute.NOMINAL);
                            mudarTipo(columnaTaboa, Attribute.STRING);
                            break;
                        case Attribute.DATE:
                            mudarTipo(columnaTaboa, Attribute.STRING);
                            mudarTipo(columnaTaboa, Attribute.DATE);
                            break;
                    }
                    break;
            }
            atributos.setRelationName(nomeRelacion);
        } finally {
            setChanged();
        }
    }

    public class InstancesComparable extends Instances {

        public InstancesComparable(Instances i) {
            super(i);
        }

        public InstancesComparable(String relationName, ArrayList<Attribute> aux, int i) {
            super(relationName, aux, i);
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof InstancesComparable == false) {
                return false;
            }
            InstancesComparable is = (InstancesComparable) o;
            Enumeration e1 = is.enumerateInstances();
            Enumeration e2 = enumerateInstances();
            InstanceComparator a = new InstanceComparator();
            if (!relationName().equals(is.relationName())) {
                return false;
            }
            while (e1.hasMoreElements() || e1.hasMoreElements()) {
                try {
                    Instance i1 = (Instance) e1.nextElement();
                    Instance i2 = (Instance) e2.nextElement();
                    if (a.compare(i1, i2) != 0) {
                        return false;
                    }
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
            e1 = is.enumerateAttributes();
            e2 = enumerateAttributes();
            while (e1.hasMoreElements() || e1.hasMoreElements()) {
                try {
                    Attribute a1 = (Attribute) e1.nextElement();
                    Attribute a2 = (Attribute) e2.nextElement();
                    if (!a1.equals(a2)) {
                        return false;
                    }
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            return hash;
        }
    }

}
