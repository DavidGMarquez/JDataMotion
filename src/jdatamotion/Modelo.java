package jdatamotion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import jdatamotion.excepcions.ExcepcionArquivoModificado;
import jdatamotion.excepcions.ExcepcionCambiarTipoAtributo;
import jdatamotion.excepcions.ExcepcionComandoInutil;
import jdatamotion.excepcions.ExcepcionFormatoIdentificacionTemporal;
import jdatamotion.filtros.AbstractFilter;
import jdatamotion.filtros.Parameter;
import jdatamotion.sesions.Sesion;
import jdatamotion.sesions.SesionModelo;
import jdatamotion.sesions.Sesionizable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
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

    private InstancesComparable instancesComparable;
    private int indiceTemporal;
    private String direccionAoFicheiro;
    private byte[] hashCode;
    private int indiceAtributoNominal;
    private List<AbstractFilter> filtros;

    public int getIndiceAtributoNominal() {
        return indiceAtributoNominal;
    }

    public void setHashCode(byte[] hashCode) {
        this.hashCode = hashCode;
    }

    public byte[] getHashCode() {
        return hashCode;
    }

    public void setInstancesComparable(InstancesComparable instancesComparable) {
        this.instancesComparable = instancesComparable;
        setChanged();
    }

    public String obterNomeAtributo(int columna) {
        return instancesComparable.attribute(columna).name();
    }

    public void setIndiceAtributoNominal(int indiceAtributoNominal) {
        this.indiceAtributoNominal = indiceAtributoNominal;
    }

    public String[] getNomesAtributos() {
        String[] ats = new String[obterNumAtributos()];
        for (int i = 0; i < ats.length; i++) {
            ats[i] = instancesComparable.attribute(i).name();
        }
        return ats;
    }

    public Integer getIndiceAtributo(String nome) {
        for (int i = 0; i < instancesComparable.numAttributes(); i++) {
            if (instancesComparable.attribute(i).name().equals(nome)) {
                return i;
            }
        }
        return null;
    }

    public Attribute[] getAtributos() {
        Attribute[] ats = new Attribute[obterNumAtributos()];
        for (int i = 0; i < ats.length; i++) {
            ats[i] = instancesComparable.attribute(i);
        }
        return ats;
    }

    private boolean contenSoValoresPositivos(int indiceAtributo) {
        if (!instancesComparable.attribute(indiceAtributo).isNumeric()) {
            return false;
        }
        return instancesComparable.stream().noneMatch((instancesComparable1) -> (instancesComparable1.value(indiceAtributo) < 0.0));
    }

    public void setIndiceTemporal(int indiceTemporal) throws ExcepcionFormatoIdentificacionTemporal {
        if (indiceTemporal > -1) {
            int tipo = instancesComparable.attribute(indiceTemporal).type();
            if (!contenSoValoresPositivos(indiceTemporal)) {
                throw new ExcepcionFormatoIdentificacionTemporal(tipo);
            }
        }
        this.indiceTemporal = indiceTemporal;
        setChanged();
    }

    public Modelo() {
        super();
        instancesComparable = null;
        indiceTemporal = -1;
        indiceAtributoNominal = -1;
        filtros = new ArrayList<>();
    }

    public InstancesComparable getFilteredInstancesComparable() {
        InstancesComparable ins = instancesComparable;
        for (int i = 0; i < contarFiltros(); i++) {
            ins = getFiltro(i).filter(ins);
        }
        return ins;
    }

    public void desconfigurarAtributoFiltros() {
        filtros.stream().forEach((f) -> {
            f.setIndiceAtributoFiltrado(null);
        });
        setChanged();
    }

    public InstancesComparable getInstancesComparable() {
        return instancesComparable;
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
        return instancesComparable.numInstances();
    }

    public int obterNumAtributos() {
        return instancesComparable.numAttributes();
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
        hash = 53 * hash + Objects.hashCode(this.instancesComparable);
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
        switch (instancesComparable.attribute(indice).type()) {
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
        Enumeration e = instancesComparable.enumerateAttributes();
        while (e.hasMoreElements()) {
            cabeceiras.add((Attribute) e.nextElement());
        }
        return cabeceiras;
    }

    public ArrayList<String> obterArrayListNomesAtributos() {
        int numAtributos = obterNumAtributos();
        ArrayList<String> cabeceiras = new ArrayList<>(numAtributos);
        Enumeration e = instancesComparable.enumerateAttributes();
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

    public void setFiltros(List<AbstractFilter> filtros) {
        this.filtros = filtros;
        setChanged();
    }

    public void update() {
        notifyObservers();
        clearChanged();
    }

    public List<AbstractFilter> getFiltros() {
        return filtros;
    }

    public ArrayList<Integer> obterIndicesAtributosNumericosNoModelo() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < obterNumAtributos(); i++) {
            if (instancesComparable.attribute(i).isNumeric()) {
                indices.add(i);
            }
        }
        return indices;
    }

    public ArrayList<Integer> obterIndicesAtributosNominaisNoModelo() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < obterNumAtributos(); i++) {
            if (instancesComparable.attribute(i).isNominal()) {
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
        filtros = new ArrayList<>();
        String extension = "";
        int j = url.lastIndexOf('.');
        if (j >= 0) {
            extension = url.substring(j + 1);
        }
        switch (extension) {
            case "csv":
            case "arff":
                importarConExtension(url, extension);
                break;
            default:
                try {
                    importarConExtension(url, "arff");
                } catch (IOException e) {
                    importarConExtension(url, "csv");
                }
                break;
        }
        setChanged();
    }

    private void importarConExtension(String url, String extension) throws IOException {
        switch (extension) {
            case "arff":
                ArffLoader loaderARFF = new ArffLoader();
                loaderARFF.setFile(new File(url));
                instancesComparable = new InstancesComparable(loaderARFF.getDataSet());
                break;
            case "csv":
                CSVLoader loaderCSV = new CSVLoader();
                loaderCSV.setSource(new File(url));
                instancesComparable = new InstancesComparable(loaderCSV.getDataSet());
                break;
        }
    }

    public static Double getMedia(InstancesComparable instancesComparable, int indiceAtributo) {
        int numInstancesNonNaN = 0;
        Double dato,
                media = null;
        for (int i = 0; i < instancesComparable.numInstances(); i++) {
            dato = (Double) obterDato(instancesComparable, i, indiceAtributo);
            if (Double.compare(dato, Double.NaN) != 0) {
                numInstancesNonNaN++;
                if (media == null) {
                    media = dato;
                } else {
                    media += dato;
                }
            }
        }
        if (media != null) {
            media /= numInstancesNonNaN;
        }
        return media;
    }

    public static Double getMinimo(InstancesComparable instancesComparable, int indiceAtributo) {
        Double dato,
                min = null;
        for (int i = 0; i < instancesComparable.numInstances(); i++) {
            dato = (Double) obterDato(instancesComparable, i, indiceAtributo);
            if (Double.compare(dato, Double.NaN) != 0) {
                if (min == null) {
                    min = dato;
                } else if (dato < min) {
                    min = dato;
                }
            }
        }
        return min;
    }

    public static Double getMaximo(InstancesComparable instancesComparable, int indiceAtributo) {
        Double dato,
                max = null;
        for (int i = 0; i < instancesComparable.numInstances(); i++) {
            dato = (Double) obterDato(instancesComparable, i, indiceAtributo);
            if (Double.compare(dato, Double.NaN) != 0) {
                if (max == null) {
                    max = dato;
                } else if (dato > max) {
                    max = dato;
                }
            }
        }
        return max;
    }

    public static Double getVarianza(InstancesComparable instancesComparable, int indiceAtributo) {
        int numInstancesNonNaN = 0;
        Double dato,
                varianza = null,
                media = null;
        for (int i = 0; i < instancesComparable.numInstances(); i++) {
            dato = (Double) obterDato(instancesComparable, i, indiceAtributo);
            if (Double.compare(dato, Double.NaN) != 0) {
                numInstancesNonNaN++;
                if (media == null) {
                    media = dato;
                } else {
                    media += dato;
                }
            }
        }
        if (media != null) {
            media /= numInstancesNonNaN;
            numInstancesNonNaN = 0;
            varianza = 0.0;
            for (int i = 0; i < instancesComparable.numInstances(); i++) {
                dato = (Double) obterDato(instancesComparable, i, indiceAtributo);
                if (Double.compare(dato, Double.NaN) != 0) {
                    numInstancesNonNaN++;
                    varianza += Math.pow(dato - media, 2.0);
                }
            }
            varianza /= (numInstancesNonNaN - 1);
        }
        return varianza;
    }

    public static Double getDesviacionTipica(InstancesComparable instancesComparable, int indiceAtributo) {
        Double varianza = getVarianza(instancesComparable, indiceAtributo);
        return varianza != null ? Math.sqrt(varianza) : null;
    }

    public void configurarFiltro(int index, Parameter[] parametros) {
        filtros.get(index).setParameters(parametros);
        setChanged();
    }

    public void eliminarFiltro(int index) {
        filtros.remove(index);
        setChanged();
    }

    public void engadirFiltro(int index, AbstractFilter filtro) {
        filtros.add(index, filtro);
        setChanged();
    }

    public void substituirFiltro(int index, AbstractFilter filtro) {
        filtros.set(index, filtro);
        setChanged();
    }

    public AbstractFilter getFiltro(int index) {
        return filtros.get(index);
    }

    public int contarFiltros() {
        return filtros.size();
    }

    public void exportarFicheiro(String path, String extension) throws IOException {
        switch (extension) {
            case "csv":
                CSVSaver saverCSV = new CSVSaver();
                saverCSV.setInstances(getFilteredInstancesComparable());
                saverCSV.setFile(new File(path));
                saverCSV.writeBatch();
                break;
            case "arff":
                ArffSaver saverARFF = new ArffSaver();
                saverARFF.setInstances(getFilteredInstancesComparable());
                saverARFF.setFile(new File(path));
                saverARFF.writeBatch();
                break;
        }
    }

    @Override
    public Sesion obterSesion() {
        SesionModelo s = new SesionModelo();
        ArrayList<Attribute> aux = new ArrayList<>();
        for (int i = 0; i < instancesComparable.numAttributes(); i++) {
            aux.add(extraerCabeceira(instancesComparable.attribute(i).name(), instancesComparable.attribute(i).type(), instancesComparable.attribute(i).enumerateValues()));
        }
        InstancesComparable cabeceras = new InstancesComparable(instancesComparable.relationName(), aux, 0);
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
        instancesComparable = s.getCabeceiras();
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
        instancesComparable.add(new DenseInstance(obterNumAtributos()));
        setChanged();
    }

    public static Double getValorPercentil(InstancesComparable instancesComparable, int percentil, int indiceAtributo) {
        if (percentil > 100) {
            return instancesComparable.get(instancesComparable.numInstances() - 1).value(indiceAtributo);
        }
        Instances ins = new InstancesComparable(instancesComparable);
        Iterator<Instance> it = ins.iterator();
        while (it.hasNext()) {
            Instance instance = it.next();
            if (instance.isMissing(indiceAtributo)) {
                it.remove();
            }
        }
        if (ins.isEmpty()) {
            return null;
        }
        ins.sort(indiceAtributo);
        Double p, x, d;
        int e;
        x = 1.0 * ins.numInstances() * percentil / 100;
        d = x % 1;
        e = (int) Math.round(x - d);
        p = d != 0.0 ? ins.instance(e).value(indiceAtributo) : (ins.instance(e - 1).value(indiceAtributo) + ins.instance(e).value(indiceAtributo)) / 2;
        return p;
    }

    public static Object obterDato(InstancesComparable instancesComparable, int fila, int columna) {
        Object valor = null;
        switch (instancesComparable.attribute(columna).type()) {
            case Attribute.DATE:
            case Attribute.RELATIONAL:
            case Attribute.STRING:
                valor = instancesComparable.get(fila).attribute(columna).value((int) instancesComparable.get(fila).value(columna));
                break;
            case Attribute.NOMINAL:
                valor = (Double.isNaN(instancesComparable.get(fila).value(columna)) ? "" : instancesComparable.get(fila).attribute(columna).value((int) instancesComparable.get(fila).value(columna)));
                break;
            case Attribute.NUMERIC:
                valor = instancesComparable.get(fila).value(columna);
                break;
        }
        return valor;
    }

    public Object obterDato(int fila, int columna) {
        return obterDato(instancesComparable, fila, columna);
    }

    public String obterStringDato(int fila, int columna, boolean caracterParaNulos) {
        String valor = instancesComparable.get(fila).toString(columna);
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
        String stringValor = valor.toString();
        if (obterDato(fila, columna).toString().equals(valor.toString()) || ("".equals(stringValor) && instancesComparable.get(fila).isMissing(columna))) {
            setChanged();
            throw new ExcepcionComandoInutil();
        }
        if ("".equals(stringValor)) {
            instancesComparable.get(fila).setMissing(columna);
        } else {
            switch (instancesComparable.attribute(columna).type()) {
                case Attribute.DATE:

                    instancesComparable.get(fila).setValue(columna, instancesComparable.attribute(columna).parseDate(stringValor));
                    break;
                case Attribute.NOMINAL:
                case Attribute.RELATIONAL:
                case Attribute.STRING:
                    instancesComparable.get(fila).setValue(columna, stringValor);
                    break;
                case Attribute.NUMERIC:
                    instancesComparable.get(fila).setValue(columna, Double.parseDouble(stringValor));
                    break;
            }
        }
        setChanged();
    }

    public void eliminarDatos(Integer[] datos) {
        ArrayList<Instance> candidatos = new ArrayList<>();
        for (Integer i : datos) {
            candidatos.add(instancesComparable.get(i));
        }
        candidatos.stream().forEach((in) -> {
            instancesComparable.remove(in);
        });
        setChanged();
    }

    public void mudarNomeRelacion(String nomeRelacion) {
        instancesComparable.setRelationName(nomeRelacion);
        setChanged();
    }

    public void mudarTipo(int columnaTaboa, int novoTipo) throws Exception {
        try {
            String nomeRelacion = instancesComparable.relationName();
            int antigoTipo = instancesComparable.attribute(columnaTaboa).type();
            switch (antigoTipo) {
                case Attribute.STRING:
                    switch (novoTipo) {
                        case Attribute.NOMINAL:
                            StringToNominal filtro = new StringToNominal();
                            filtro.setOptions(new String[]{"-R", String.valueOf(columnaTaboa + 1)});
                            filtro.setInputFormat(instancesComparable);
                            instancesComparable = new InstancesComparable(Filter.useFilter(instancesComparable, filtro));
                            break;
                        case Attribute.NUMERIC:
                            ArrayList<Attribute> temp = new ArrayList<>();
                            Enumeration e = instancesComparable.enumerateInstances();
                            Attribute at;
                            boolean erroParseo = false;
                            for (int i = 0; i < instancesComparable.numAttributes(); i++) {
                                if (i != columnaTaboa) {
                                    at = instancesComparable.attribute(i);
                                } else {
                                    at = new Attribute(instancesComparable.attribute(i).name());
                                }
                                temp.add(at);
                            }
                            InstancesComparable tempInstances = new InstancesComparable(instancesComparable.relationName(), temp, 0);
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
                            instancesComparable = tempInstances;
                            if (erroParseo) {
                                throw new ExcepcionCambiarTipoAtributo(antigoTipo, novoTipo);
                            }
                            break;
                        case Attribute.DATE:
                            temp = new ArrayList<>();
                            e = instancesComparable.enumerateInstances();
                            erroParseo = false;
                            for (int i = 0; i < instancesComparable.numAttributes(); i++) {
                                if (i != columnaTaboa) {
                                    at = instancesComparable.attribute(i);
                                } else {
                                    at = new Attribute(instancesComparable.attribute(i).name(), "yyyy-MM-dd");
                                }
                                temp.add(at);
                            }
                            tempInstances = new InstancesComparable(instancesComparable.relationName(), temp, 0);
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
                            instancesComparable = tempInstances;
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
                            filtro.setInputFormat(instancesComparable);
                            instancesComparable = new InstancesComparable(Filter.useFilter(instancesComparable, filtro));
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
                            for (int i = 0; i < instancesComparable.numAttributes(); i++) {
                                if (i != columnaTaboa) {
                                    at = instancesComparable.attribute(i);
                                } else {
                                    at = new Attribute(instancesComparable.attribute(i).name(), (ArrayList<String>) null);
                                }
                                temp.add(at);
                            }
                            InstancesComparable tempInstances = new InstancesComparable(instancesComparable.relationName(), temp, instancesComparable.numInstances());
                            for (int i = 0; i < instancesComparable.numInstances(); i++) {
                                tempInstances.add(instancesComparable.instance(i));
                                tempInstances.instance(i).setValue(columnaTaboa, obterStringDato(i, columnaTaboa, false));
                            }
                            instancesComparable = tempInstances;
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
                            filtro.setInputFormat(instancesComparable);
                            instancesComparable = new InstancesComparable(Filter.useFilter(instancesComparable, filtro));
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
            instancesComparable.setRelationName(nomeRelacion);
        } finally {
            setChanged();
        }
    }

    public void engadirAtributo() {
        int indice = 0;
        boolean unico = false;
        String nomeColumna = "";
        while (!unico) {
            indice++;
            unico = true;
            nomeColumna = Vista.bundle.getString("novoAtributo");
            Enumeration e = instancesComparable.enumerateAttributes();
            while (e.hasMoreElements()) {
                Attribute a = (Attribute) e.nextElement();
                if (a.name().equals(nomeColumna + indice)) {
                    unico = false;
                    break;
                }
            }
        }
        instancesComparable.insertAttributeAt(new Attribute(nomeColumna + indice, (List<String>) null), instancesComparable.numAttributes());
        setChanged();
    }

    public void eliminarAtributo(int indiceAtributoNoModelo) {
        instancesComparable.deleteAttributeAt(indiceAtributoNoModelo);
        setChanged();
    }

    public void renomearAtributo(String nome, int indiceAtributoNoModelo) {
        instancesComparable.renameAttribute(indiceAtributoNoModelo, nome);
        setChanged();
    }

    public void intercambiarFiltros(int indiceA, int indiceB) {
        AbstractFilter aux = filtros.get(indiceA);
        filtros.set(indiceA, filtros.get(indiceB));
        filtros.set(indiceB, aux);
        setChanged();
    }

}
