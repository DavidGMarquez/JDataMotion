package jdatamotion;

import java.io.BufferedInputStream;
import jdatamotion.filtros.FilterHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import jdatamotion.excepcions.ExcepcionArquivoModificado;
import jdatamotion.excepcions.ExcepcionCambiarTipoAtributo;
import jdatamotion.excepcions.ExcepcionComandoInutil;
import jdatamotion.excepcions.ExcepcionFormatoIdentificacionTemporal;
import jdatamotion.sesions.Sesion;
import jdatamotion.sesions.SesionModelo;
import jdatamotion.sesions.Sesionizable;
import jdatamotioncommon.ComparableInstances;
import jdatamotioncommon.Utils;
import jdatamotioncommon.filtros.IFilter;
import jdatamotioncommon.filtros.Parameter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
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

    private ComparableInstances instancesComparable;
    private int indiceTemporal;
    private String direccionAoFicheiro;
    private byte[] hashCodeFicheiro;
    private int indiceAtributoNominal;
    private List<FilterHandler> filtros;
    public static final String formatoTimeIdentificadorTemporal = "HH:mm:ss.SSS";

    public int getIndiceAtributoNominal() {
        return indiceAtributoNominal;
    }

    public byte[] getHashCodeFicheiro() {
        return hashCodeFicheiro;
    }

    public void setComparableInstances(ComparableInstances instancesComparable) {
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

    public static final String normalizarTime(String time) {
        String timeNormalizado = time.replace('?', '0');
        int n = 2 - StringUtils.countMatches(timeNormalizado, ":");
        for (int i = 0; i < n; i++) {
            timeNormalizado = "0:" + timeNormalizado;
        }
        if (!StringUtils.contains(timeNormalizado, ".")) {
            timeNormalizado = timeNormalizado + ".0";
        }
        return timeNormalizado;
    }

    private boolean contenDatasValidas(int indiceAtributo) {
        if (!instancesComparable.attribute(indiceAtributo).isString()) {
            return false;
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat(formatoTimeIdentificadorTemporal, Locale.getDefault());
        for (Instance ic : instancesComparable) {
            try {
                timeFormat.parse(normalizarTime(ic.stringValue(indiceAtributo)));
            } catch (ParseException pe) {
                return false;
            }
        }
        return true;
    }

    public void setIndiceTemporal(int indiceTemporal) throws ExcepcionFormatoIdentificacionTemporal {
        if (indiceTemporal > -1) {
            if (!contenSoValoresPositivos(indiceTemporal) && !contenDatasValidas(indiceTemporal)) {
                throw new ExcepcionFormatoIdentificacionTemporal(indiceTemporal, instancesComparable);
            }
        }
        this.indiceTemporal = indiceTemporal;
        setChanged();
    }

    public Modelo() {
        this(null, -1, null, null, -1, new ArrayList<>());
    }

    Modelo(ComparableInstances instancesComparable, int indiceTemporal, String direccionAoFicheiro, byte[] hashCode, int indiceAtributoNominal, List<FilterHandler> filtros) {
        super();
        this.instancesComparable = instancesComparable;
        this.indiceTemporal = indiceTemporal;
        this.direccionAoFicheiro = direccionAoFicheiro;
        this.hashCodeFicheiro = hashCode;
        this.indiceAtributoNominal = indiceAtributoNominal;
        this.filtros = filtros;
    }

    Modelo(Modelo modelo) {
        this(new ComparableInstances(modelo.instancesComparable), modelo.indiceTemporal, modelo.direccionAoFicheiro, ArrayUtils.clone(modelo.hashCodeFicheiro), modelo.indiceAtributoNominal, new ArrayList<>(modelo.filtros));
    }

    public ComparableInstances getComparableInstancesFiltradas() {
        ComparableInstances ins = instancesComparable;
        for (int i = 0; i < contarFiltros(); i++) {
            ins = getFiltro(i).filtrar(ins);
        }
        return ins;
    }

    public void desconfigurarAtributoFiltros() {
        filtros.stream().forEach((f) -> {
            f.setIndiceAtributoFiltrado(null);
        });
        setChanged();
    }

    public ComparableInstances getComparableInstances() {
        return instancesComparable;
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
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.instancesComparable);
        hash = 53 * hash + this.indiceTemporal;
        hash = 53 * hash + Objects.hashCode(this.direccionAoFicheiro);
        hash = 53 * hash + Arrays.hashCode(this.hashCodeFicheiro);
        hash = 53 * hash + this.indiceAtributoNominal;
        return hash;
    }

    public List<List<Object>> obterListaDatos(boolean caracterParaNulos) {
        List<List<Object>> datos = new ArrayList<>(obterNumInstancias());
        int numInstancias = obterNumInstancias();
        int numAtributos = obterNumAtributos();
        for (int i = 0; i < numInstancias; i++) {
            List<Object> tupla = new ArrayList<>(obterNumAtributos());
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

    public List<Attribute> obterListaAtributos() {
        List<Attribute> cabeceiras = new ArrayList<>();
        Enumeration e = instancesComparable.enumerateAttributes();
        while (e.hasMoreElements()) {
            cabeceiras.add((Attribute) e.nextElement());
        }
        return cabeceiras;
    }

    public List<String> obterListaNomesAtributos() {
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

    public void setFiltros(List<FilterHandler> filtros) {
        this.filtros = filtros;
        setChanged();
    }

    public void notifyChanged() {
        notifyObservers();
        clearChanged();
    }

    public List<FilterHandler> getFiltros() {
        return filtros;
    }

    public ArrayList<Integer> obterIndicesAtributosNumericos() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < obterNumAtributos(); i++) {
            if (instancesComparable.attribute(i).isNumeric()) {
                indices.add(i);
            }
        }
        return indices;
    }

    public ArrayList<Integer> obterIndicesAtributosNominais() {
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
        hashCodeFicheiro = resumirFicheiroSHA1(new File(url));
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
                instancesComparable = new ComparableInstances(loaderARFF.getDataSet());
                break;
            case "csv":
                CSVLoader loaderCSV = new CSVLoader();
                loaderCSV.setSource(new File(url));
                instancesComparable = new ComparableInstances(loaderCSV.getDataSet());
                break;
        }
    }

    public void configurarFiltro(int indice, Map<String, Parameter> parametros) {
        filtros.get(indice).setParameters(parametros);
        setChanged();
    }

    public void eliminarFiltro(int indice) {
        filtros.remove(indice);
        setChanged();
    }

    private static abstract class MultiClassLoader extends ClassLoader {

        private final Hashtable classes = new Hashtable();
        private char classNameReplacementChar;

        protected boolean monitorOn = false;
        protected boolean sourceMonitorOn = true;

        public MultiClassLoader() {
        }

        @Override
        public Class loadClass(String className) throws ClassNotFoundException {
            return (loadClass(className, true));
        }

        @Override
        public synchronized Class loadClass(String className,
                boolean resolveIt) throws ClassNotFoundException {

            Class result;
            byte[] classBytes;
            monitor(">> MultiClassLoader.loadClass(" + className + ", " + resolveIt + ")");

            result = (Class) classes.get(className);
            if (result != null) {
                monitor(">> returning cached result.");
                return result;
            }

            try {
                result = super.findSystemClass(className);
                monitor(">> returning system class (in CLASSPATH).");
                return result;
            } catch (ClassNotFoundException e) {
                monitor(">> Not a system class.");
            }

            classBytes = loadClassBytes(className);
            if (classBytes == null) {
                throw new ClassNotFoundException();
            }

            result = defineClass(className, classBytes, 0, classBytes.length);
            if (result == null) {
                throw new ClassFormatError();
            }

            if (resolveIt) {
                resolveClass(result);
            }

            classes.put(className, result);
            monitor(">> Returning newly loaded class.");
            return result;
        }

        public void setClassNameReplacementChar(char replacement) {
            classNameReplacementChar = replacement;
        }

        protected abstract byte[] loadClassBytes(String className);

        protected String formatClassName(String className) {
            if (classNameReplacementChar == '\u0000') {
                return className.replace('.', '/') + ".class";
            } else {
                return className.replace('.',
                        classNameReplacementChar) + ".class";
            }
        }

        protected void monitor(String text) {
            if (monitorOn) {
                print(text);
            }
        }

        protected void print(String text) {
            System.out.println(text);
        }

    }

    static class JarClassLoader extends MultiClassLoader {

        private final JarResources jarResources;

        public JarClassLoader(String jarName) {
            jarResources = new JarResources(jarName);
        }

        @Override
        protected byte[] loadClassBytes(String className) {
            className = formatClassName(className);
            return (jarResources.getResource(className));
        }

        private static final class JarResources {

            private final Hashtable htSizes = new Hashtable();
            private final Hashtable htJarContents = new Hashtable();

            private final String jarFileName;

            public JarResources(String jarFileName) {
                this.jarFileName = jarFileName;
                init();
            }

            public byte[] getResource(String name) {
                return (byte[]) htJarContents.get(name);
            }

            private void init() {
                try {
                    try (ZipFile zf = new ZipFile(jarFileName)) {
                        Enumeration e = zf.entries();
                        while (e.hasMoreElements()) {
                            ZipEntry ze = (ZipEntry) e.nextElement();

                            htSizes.put(ze.getName(), (int) ze.getSize());
                        }
                    }
                    FileInputStream fis = new FileInputStream(jarFileName);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    ZipInputStream zis = new ZipInputStream(bis);
                    ZipEntry ze;
                    while ((ze = zis.getNextEntry()) != null) {
                        if (ze.isDirectory()) {
                            continue;
                        }
                        int size = (int) ze.getSize();
                        if (size == -1) {
                            size = ((Integer) htSizes.get(ze.getName()));
                        }
                        byte[] b = new byte[(int) size];
                        int rb = 0;
                        int chunk;
                        while (((int) size - rb) > 0) {
                            chunk = zis.read(b, rb, (int) size - rb);
                            if (chunk == -1) {
                                break;
                            }
                            rb += chunk;
                        }
                        htJarContents.put(ze.getName(), b);
                    }
                } catch (NullPointerException | IOException e) {
                }
            }

        }
    }

    public void incluirFiltro(String jarUrl) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        JarClassLoader jarLoader = new JarClassLoader(jarUrl);
        JarFile jarFile = new JarFile(jarUrl);
        Enumeration ee = jarFile.entries();
        boolean valido = false;
        while (ee.hasMoreElements()) {
            JarEntry entry = (JarEntry) ee.nextElement();
            String name = entry.getName();
            if (name.endsWith(".class")) {
                Class c = jarLoader.loadClass(name.substring(0, name.lastIndexOf('.')).replaceAll("/", "."), true);
                Object o = c.newInstance();
                if (o instanceof IFilter) {
                    valido = true;
                    break;
                }
            }
        }
        if (valido) {
            Files.copy(Paths.get(jarUrl), Paths.get("filters\\" + jarUrl.substring(jarUrl.lastIndexOf("\\") + 1)), REPLACE_EXISTING);
            setChanged();
        }
    }

    public void engadirFiltro(int indice, IFilter filtro) {
        filtros.add(indice, new FilterHandler(null, filtro));
        setChanged();
    }

    public void substituirFiltro(int index, FilterHandler filtro) {
        filtros.set(index, filtro);
        setChanged();
    }

    public FilterHandler getFiltro(int index) {
        return filtros.get(index);
    }

    public int contarFiltros() {
        return filtros.size();
    }

    public void exportarFicheiro(String url, String extension) throws IOException {
        switch (extension) {
            case "csv":
                CSVSaver saverCSV = new CSVSaver();
                saverCSV.setInstances(getComparableInstancesFiltradas());
                saverCSV.setFile(new File(url));
                saverCSV.writeBatch();
                break;
            case "arff":
                ArffSaver saverARFF = new ArffSaver();
                saverARFF.setInstances(getComparableInstancesFiltradas());
                saverARFF.setFile(new File(url));
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
        ComparableInstances cabeceras = new ComparableInstances(instancesComparable.relationName(), aux, 0);
        s.setCabeceiras(cabeceras);
        s.setDireccionAoFicheiro(getDireccionAoFicheiro());
        s.setIndiceTemporal(getIndiceTemporal());
        s.setHash(getHashCodeFicheiro());
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
        hashCodeFicheiro = s.getHash();
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
                attr = new Attribute(nome, (ComparableInstances) argumento);
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

    public Object obterDato(int fila, int columna) {
        return Utils.obterDato(instancesComparable, fila, columna);
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
                    if (indiceTemporal == columna) {
                        try {
                            new SimpleDateFormat(formatoTimeIdentificadorTemporal, Locale.getDefault()).parse(normalizarTime(stringValor));
                        } catch (ParseException pe) {
                            throw new ExcepcionFormatoIdentificacionTemporal(columna, instancesComparable);
                        }
                    }
                    instancesComparable.get(fila).setValue(columna, stringValor);
                    break;
                case Attribute.NUMERIC:
                    if (indiceTemporal == columna && Double.parseDouble(stringValor) < 0.0) {
                        throw new ExcepcionFormatoIdentificacionTemporal(columna, instancesComparable);
                    }
                    instancesComparable.get(fila).setValue(columna, Double.parseDouble(stringValor));
                    break;
            }
        }
        setChanged();
    }

    public void eliminarDatos(Integer[] indices) {
        ArrayList<Instance> candidatos = new ArrayList<>();
        for (Integer i : indices) {
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
                            instancesComparable = new ComparableInstances(Filter.useFilter(instancesComparable, filtro));
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
                            ComparableInstances tempInstances = new ComparableInstances(instancesComparable.relationName(), temp, 0);
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
                            tempInstances = new ComparableInstances(instancesComparable.relationName(), temp, 0);
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
                            instancesComparable = new ComparableInstances(Filter.useFilter(instancesComparable, filtro));
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
                            ComparableInstances tempInstances = new ComparableInstances(instancesComparable.relationName(), temp, instancesComparable.numInstances());
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
                            instancesComparable = new ComparableInstances(Filter.useFilter(instancesComparable, filtro));
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
            nomeColumna = Vista.recursosIdioma.getString("novoAtributo");
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
        FilterHandler aux = filtros.get(indiceA);
        filtros.set(indiceA, filtros.get(indiceB));
        filtros.set(indiceB, aux);
        setChanged();
    }

    public void exportarFiltros(String url, Integer[] indicesFiltros) throws FileNotFoundException, IOException {
        ArrayList<FilterHandler> ffs = new ArrayList<>();
        for (Integer indicesFiltro : indicesFiltros) {
            ffs.add(filtros.get(indicesFiltro));
        }
        FileOutputStream saveFile;
        saveFile = new FileOutputStream(url);
        ObjectOutputStream save = new ObjectOutputStream(saveFile);
        save.writeObject(ffs);
    }

    @SuppressWarnings("unchecked")
    public void importarFiltros(String url) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream saveFile;
        saveFile = new FileInputStream(url);
        ObjectInputStream restore = new ObjectInputStream(saveFile);
        ArrayList<FilterHandler> ffs = (ArrayList<FilterHandler>) restore.readObject();
        ffs.stream().forEach((f) -> {
            f.setIndiceAtributoFiltrado(null);
            f.setSeleccionado(false);
            filtros.add(f);
        });
        setChanged();
    }

}
