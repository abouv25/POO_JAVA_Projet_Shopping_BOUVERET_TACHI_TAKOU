package modele;

public class QuestionSecurite {
    private String email;
    private String question;
    private String reponse;

    public QuestionSecurite(String email, String question, String reponse) {
        this.email = email;
        this.question = question;
        this.reponse = reponse;
    }

    public String getEmail() { return email; }
    public String getQuestion() { return question; }
    public String getReponse() { return reponse; }
}
