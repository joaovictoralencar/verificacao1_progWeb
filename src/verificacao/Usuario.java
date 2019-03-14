/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package verificacao;

/**
 *
 * @author aluno
 */
public class Usuario {
    public Usuario() {
    }

    Usuario(String login, String nome, String email) {
        this.email = email;
        this.nome = nome;
        this.login = login;
    }

    private String nome;
    private String login;
    private String email;

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
