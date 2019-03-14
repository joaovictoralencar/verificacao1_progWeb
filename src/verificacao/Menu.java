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
import java.util.ArrayList;

/**
 *
 * @author aluno
 */
public class Menu {

    int opcaoMarcada = 100000;

    Menu() throws FileNotFoundException, IOException {
        File arquivo = new File("Usuários.txt");
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
        if (arquivo.length() != 0) {
            for (int i = 0; i < 1; i++) {
                usuarioTempo = new Usuario();
                usuarioTempo.setLogin(br.readLine().split(": ")[1]);
                usuarioTempo.setNome(br.readLine().split(": ")[1]);
                usuarioTempo.setEmail(br.readLine().split(": ")[1]);
                Main.admUsuarios.usuarios.add(usuarioTempo);
            }
        }
    }

    void mostraInterface() throws IOException {
        Scanner input = new Scanner(System.in);

        do {
            System.out.println("Opção Descrição\n"
                    + "1 Incluir um novo usuário\n"
                    + "2 Alterar um usuário existente\n"
                    + "3 Remover um usuário existente\n"
                    + "4 Exibir um usuário existente\n"
                    + "5 Exibir todos os usuários existentes\n"
                    + "0 Sair\n"
                    + "Digite o número da opção:");
            opcaoMarcada = input.nextInt();
            switch (opcaoMarcada) {
                case 1:
                    incluirUsuario();
                    break;
                case 2:
                    alterarUsuario();
                    break;
                case 3:
                    System.out.println("Digite o login do usuário que você deseja remover");
                    removerUsuario(input.nextLine());
                    break;
                case 4:
                    System.out.println("Digite o login do usuário que você deseja remover");
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

    void incluirUsuario() throws IOException {
        FileWriter fw = new FileWriter("Usuários.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);

        Scanner input = new Scanner(System.in);
        String login, nome, email;
        System.out.println("Digite o login do novo usuário");
        Usuario usuarioAtual = null;
        boolean loginValido = false;
        /*
         Checa se o usuário já não existe.
         */
        do {
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
                    if (null != Main.admUsuarios.usuarios.get(i) && login.equals(Main.admUsuarios.usuarios.get(i).getLogin())) {
                        System.out.println("Login já existente ou inválido. Por favor, digite outro");
                        break;
                    } else if (Main.admUsuarios.usuarios.get(i) == null) {
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
            bw.newLine();
            bw.append("Login: " + usuarioAtual.getLogin());
            bw.newLine();
            bw.append("Nome: " + usuarioAtual.getNome());
            bw.newLine();
            bw.append("Email: " + usuarioAtual.getEmail());
            bw.newLine();
            System.out.println("Usuário criado com sucesso!");
        }
        bw.close();
        fw.close();
    }

    void alterarUsuario() throws IOException {
        System.out.println("Alterando um usuário\nDigite o usuário que você quer alterar:");
        Scanner input = new Scanner(System.in);
        String loginDigitado;
        loginDigitado = input.nextLine();
        boolean loginExistente = false;
        do {
            for (int i = 0; i < Main.admUsuarios.usuarios.size(); i++) {
                //encontrou o login
                if (null != Main.admUsuarios.usuarios.get(i) && loginDigitado.equals(Main.admUsuarios.usuarios.get(i).getLogin())) {
                    incluirUsuario();
                    removerUsuario(loginDigitado);
                    loginExistente = true;
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

    void removerUsuario(String loginDigitado) {

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
            for (int i = 0; i < Main.admUsuarios.usuarios.size(); i++) {
                if (Main.admUsuarios.usuarios.get(i).getLogin().equals(loginRemover)) {
                    Main.admUsuarios.usuarios.remove(i);
                }
            }
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
            System.out.println("TESTE");
        }
    }

    boolean sair() {
        return opcaoMarcada == 0;
    }
}
