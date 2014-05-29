package jdatamotion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author usuario
 */
public class LectorFicheiros {

    private String url;

    public LectorFicheiros() {
    }

    public String getUrl() {
        return url;
    }

    public void inicializar(String url) {
        this.url = url;
    }

    public String lerSeguinte() throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(url))) {
            return br.readLine();
        }
    }
}
