package jdatamotion;

import java.io.Serializable;

/**
 *
 * @author usuario
 */
public class FiltroExtension implements Serializable {

    public String nome;
    public String[] extensions;

    public FiltroExtension(String nome, String[] extensions) {
        this.nome = nome;
        this.extensions = extensions;
    }

    public String getNome() {
        return nome;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }
}
