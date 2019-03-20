/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verificacao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author aluno
 */
public class Menu {

    int opcaoMarcada;
    Scanner input;
    File arquivo;

    Menu(Scanner input) throws FileNotFoundException, IOException {
        /*assim que menu é criado, vou verificar se já não existe um arquivo txt. 
         Se já existir, vou passar seus valores para o vetor local
         */
        arquivo = new File("Usuários.txt");
        boolean existe = arquivo.exists();
        if (!existe) {//se não existe arquivo, criaremos um
            try {
                arquivo.createNewFile();
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {//se já existe, vamos tentar passar para o vetor
            if (arquivo.length() != 0) {//antes de passar pro vetor, precisamos saber se ele não está vazio
                FileReader fr = new FileReader("Usuários.txt");
                BufferedReader br = new BufferedReader(fr);
                Usuario usuarioTempo;
                System.out.println(arquivo.length());
                while (br.ready()) {
                    usuarioTempo = new Usuario();
                    usuarioTempo.setLogin(br.readLine().split(": ")[1]);
                    usuarioTempo.setNome(br.readLine().split(": ")[1]);
                    usuarioTempo.setEmail(br.readLine().split(": ")[1]);
                    Main.admUsuarios.usuarios.add(usuarioTempo);
                }
                fr.close();
                br.close();
            }
        }
    }

    //Essa função é minha "saída" para excluir usuário. Toda vez que eu remover algum usuário, eu reescrevo o arquivo
    void recriarArquivo() throws IOException {
        if (arquivo.exists()) {//se o arquivo existe, excluo ele
            arquivo.delete();
            arquivo = new File("Usuários.txt");
            try {//e crio outro
                arquivo.createNewFile();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        //Agora que temos certeza de que ele existe, vamos preencher o arquivo
        FileWriter fw = new FileWriter("Usuários.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        for (Usuario usuario : Main.admUsuarios.usuarios) {
            bw.write("Login: " + usuario.getLogin());
            bw.newLine();
            bw.write("Nome: " + usuario.getNome());
            bw.newLine();
            bw.write("Email: " + usuario.getEmail());
            bw.newLine();
        }
        bw.close();
        fw.close();
    }

    void mostraInterface() throws IOException {
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("");
            System.out.println("Opção Descrição\n"
                    + "1 Incluir um novo usuário\n"
                    + "2 Alterar um usuário existente\n"
                    + "3 Remover um usuário existente\n"
                    + "4 Exibir um usuário existente\n"
                    + "5 Exibir todos os usuários existentes\n"
                    + "0 Sair\n"
                    + "Digite o número da opção:");
            
            //o usuário precisa digitar um número, pois não achei onde fazer o tratamento
            opcaoMarcada = Integer.parseInt(input.nextLine());
            
            System.out.println("OPÇÃO MARCADA = " + opcaoMarcada);
            switch (opcaoMarcada) {
                case 1:
                    incluirUsuario(input);
                    break;
                case 2:
                    alterarUsuario(input);
                    break;
                case 3:
                    System.out.println("Digite o login do usuário que você deseja remover");

                    removerUsuario(input.nextLine(), input);
                    break;
                case 4:
                    System.out.println("Digite o login do usuário que você deseja exibir");
                    exibirUsuario(input.nextLine());
                    break;
                case 5:
                    exibirTodos();
                    break;
                case 0:
                    sair();
                    break;
            }
        } while (!sair());
    }

    void incluirUsuario(Scanner input) throws IOException {
        FileWriter fw = new FileWriter("Usuários.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        String login, nome, email;
        Usuario usuarioAtual = null;
        boolean loginValido = false;
        /*
         Checa se o usuário já não existe.
         */
        do {
            System.out.println("Digite o login do novo usuário");
            login = input.nextLine();
            //Primeiro Login
            if (Main.admUsuarios.usuarios.isEmpty()) {
                usuarioAtual = new Usuario();
                usuarioAtual.setLogin(login);
                Main.admUsuarios.usuarios.add(usuarioAtual);
                loginValido = true;
                System.out.println("Login válido");
            } else { //Próximos Logins
                for (int i = 0; i < Main.admUsuarios.usuarios.size(); i++) {
                    //se já existir um usuário
                    if (null != Main.admUsuarios.usuarios.get(i) && login.equals(Main.admUsuarios.usuarios.get(i).getLogin())) {
                        System.out.println("Login já existente ou inválido. Por favor, digite outro");
                    } //caso não exista, cria um novo
                    else {
                        usuarioAtual = new Usuario();
                        usuarioAtual.setLogin(login);
                        Main.admUsuarios.usuarios.add(i, usuarioAtual);
                        System.out.println("Login válido");
                        loginValido = true;
                        break;
                    }

                }
            }
        } while (loginValido == false);
        //se o login for válido, vamos agora pegar o nome e o email
        if (usuarioAtual != null) {
            //pede os dados
            System.out.println("Digite seu nome");
            nome = input.nextLine();
            usuarioAtual.setNome(nome);
            System.out.println("Digite seu email");
            email = input.nextLine();
            usuarioAtual.setEmail(email);
            
            //Escrevendo o novo usuário no txt
            bw.write("Login: " + usuarioAtual.getLogin());
            bw.newLine();
            bw.write("Nome: " + usuarioAtual.getNome());
            bw.newLine();
            bw.write("Email: " + usuarioAtual.getEmail());
            bw.newLine();
            System.out.println("Usuário criado com sucesso!");
        }
        bw.close();
        fw.close();
    }

    void alterarUsuario(Scanner input) throws IOException {
        System.out.println("Alterando um usuário\nDigite o usuário que você quer alterar:");
        String loginDigitado;
        loginDigitado = input.nextLine();
        boolean loginExistente = false;
        do {
            for (int i = 0; i < Main.admUsuarios.usuarios.size(); i++) {
                //encontrou o login
                if (null != Main.admUsuarios.usuarios.get(i) && loginDigitado.equals(Main.admUsuarios.usuarios.get(i).getLogin())) {
                    incluirUsuario(input);
                    removerUsuario(loginDigitado, input);
                    loginExistente = true;
                    break;
                }
            }
            //se não existir o login
            if (!loginExistente) {
                System.out.println("Login não cadastrado\nDigite outro ou '0' para sair");
                loginDigitado = input.nextLine();
                System.out.println(loginDigitado);
                if ("0".equals(loginDigitado)) {
                    System.out.println("Saindo da edição de usuário...");
                    break;
                }
            }
        } while (!loginExistente);
    }

    void removerUsuario(String loginDigitado, Scanner input) throws FileNotFoundException, IOException {
        String loginRemover;
        /*esse if else me ajuda a remover um usuário na função alterar sem ter que perguntar de novo o login,
        pois em alterar, já se tem ele*/
        if (loginDigitado == null) {
            Scanner in = new Scanner(System.in);
            loginRemover = in.nextLine();
        } else {
            loginRemover = loginDigitado;
        }
        //se o vetor estivar vazio, não há usuários para remover
        if (Main.admUsuarios.usuarios.isEmpty()) {
            System.out.println("Não há usuários cadastrados para remover");
        } else {
            //apagando do vetor local
            Usuario usuarioTempo = null;
            for (int i = 0; i < Main.admUsuarios.usuarios.size(); i++) {
                if (Main.admUsuarios.usuarios.get(i).getLogin().equals(loginRemover)) {
                    usuarioTempo = Main.admUsuarios.usuarios.get(i);
                    Main.admUsuarios.usuarios.remove(i);
                    System.out.println("Usuário Removido");
                }
            }
            //apagando do txt
            recriarArquivo();
        }
    }

    void exibirUsuario(String loginDigitado) throws FileNotFoundException, IOException {
        boolean usuarioExiste = false;
        //vou percorrer o vetor para buscar os logins
        for (int i = 0; i < Main.admUsuarios.usuarios.size(); i++) {
            //se o login existir, já irei mostrar
            if (Main.admUsuarios.usuarios.get(i).getLogin().equals(loginDigitado)) {
                System.out.println("Login: " + Main.admUsuarios.usuarios.get(i).getLogin());
                System.out.println("Nome: " + Main.admUsuarios.usuarios.get(i).getNome());
                System.out.println("Email: " + Main.admUsuarios.usuarios.get(i).getEmail());
                usuarioExiste = true;
            }
        }
        if (usuarioExiste == false) {
            System.out.println("Usuário não cadastrado");
        }
    }

    void exibirTodos() throws IOException {
        //percorre o vetor de usuários e chama a função de exibir para cada um dos valores
        for (Usuario usuario : Main.admUsuarios.usuarios) {
            if (usuario != null) {
                exibirUsuario(usuario.getLogin());
            }
        }
    }

    boolean sair() {
        return true;
    }
}
