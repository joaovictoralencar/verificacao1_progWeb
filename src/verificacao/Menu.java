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

    int opcaoMarcada = 100000;
    Scanner input;
    File arquivo;

    Menu(Scanner input) throws FileNotFoundException, IOException {

        arquivo = new File("Usuários.txt");
        boolean existe = arquivo.exists();
        if (!existe) {
            try {
                arquivo.createNewFile();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        FileReader fr = new FileReader("Usuários.txt");
        BufferedReader br = new BufferedReader(fr);
        Usuario usuarioTempo;
        System.out.println(arquivo.length());
        if (arquivo.length() != 0) {
            while (br.ready()) {
                usuarioTempo = new Usuario();
                usuarioTempo.setLogin(br.readLine().split(": ")[1]);
                usuarioTempo.setNome(br.readLine().split(": ")[1]);
                usuarioTempo.setEmail(br.readLine().split(": ")[1]);
                Main.admUsuarios.usuarios.add(usuarioTempo);
            }
        }
        fr.close();
        br.close();
    }

    void recriarArquivo() throws IOException {
        if (arquivo.exists()) {
            arquivo.delete();
            arquivo = new File("Usuários.txt");
            try {
                System.out.println("RECRIEI VIU");
                arquivo.createNewFile();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        //preencher Arquivo
        FileWriter fw = new FileWriter("Usuários.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        for (int i = 0; i < Main.admUsuarios.usuarios.size(); i++) {

            bw.append("Login: " + Main.admUsuarios.usuarios.get(i).getLogin());
            bw.newLine();
            bw.append("Nome: " + Main.admUsuarios.usuarios.get(i).getNome());
            bw.newLine();
            bw.append("Email: " + Main.admUsuarios.usuarios.get(i).getEmail());
            bw.newLine();
        }
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

            //System.out.println(input);
            opcaoMarcada = Integer.parseInt(input.nextLine());
            //mexe aqui
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
        FileWriter fw = new FileWriter("Usuários.txt",true);
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

        if (usuarioAtual != null) {
            System.out.println("Digite seu nome");
            nome = input.nextLine();
            usuarioAtual.setNome(nome);
            System.out.println("Digite seu email");
            email = input.nextLine();
            usuarioAtual.setEmail(email);
            bw.append("Login: " + usuarioAtual.getLogin());
            bw.newLine();
            bw.append("Nome: " + usuarioAtual.getNome());
            bw.newLine();
            bw.append("Email: " + usuarioAtual.getEmail());
            bw.newLine();
            System.out.println("Usuário criado com sucesso!");
        }

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
                    recriarArquivo();
                    break;
                }
            }
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
        if (loginDigitado == null) {
            Scanner in = new Scanner(System.in);
            loginRemover = in.nextLine();
        } else {
            loginRemover = loginDigitado;
        }
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
        for (int i = 0; i < Main.admUsuarios.usuarios.size(); i++) {
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
        for (int i = 0; i < Main.admUsuarios.usuarios.size(); i++) {
            if (Main.admUsuarios.usuarios.get(i) != null) {
                exibirUsuario(Main.admUsuarios.usuarios.get(i).getLogin());
            }
        }
    }

    boolean sair() {
        return opcaoMarcada == 0;
    }
}
