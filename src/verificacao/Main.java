package verificacao;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author aluno
 */
public class Main {      

    static class admUsuarios {

        static int numUsuarios;
        static ArrayList<Usuario>usuarios = new ArrayList<>();
    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Menu m = new Menu();
        m.mostraInterface();
    }

}
