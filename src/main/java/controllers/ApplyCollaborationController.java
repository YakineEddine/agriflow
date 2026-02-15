package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import entities.CollabApplication;
import services.CollabApplicationService;

import java.sql.SQLException;

public class ApplyCollaborationController {

    @FXML private Text requestTitleText;
    @FXML private TextField fullNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private Slider experienceSlider;
    @FXML private Label experienceLabel;
    @FXML private TextField expectedSalaryField;
    @FXML private TextArea motivationArea;

    private long requestId;
    private String requestTitle;
    private CollabApplicationService service = new CollabApplicationService();

    @FXML
    public void initialize() {
        // Mettre à jour le label d'expérience quand le slider bouge
        if (experienceSlider != null && experienceLabel != null) {
            experienceSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                int years = newVal.intValue();
                experienceLabel.setText(years + (years <= 1 ? " an" : " ans"));
            });
        }
    }

    public void setRequestData(long requestId, String requestTitle) {
        this.requestId = requestId;
        this.requestTitle = requestTitle;
        if (requestTitleText != null) {
            requestTitleText.setText(requestTitle);
        }
    }

    @FXML
    private void handleSubmit() {
        // Validation
        if (!validateFields()) {
            return;
        }

        try {
            // Créer la candidature
            CollabApplication application = new CollabApplication();
            application.setCandidateId(1L); // ✅ CORRIGÉ : ID utilisateur connecté
            application.setRequestId(requestId);
            application.setFullName(fullNameField.getText().trim());
            application.setPhone(phoneField.getText().trim());
            application.setEmail(emailField.getText().trim());
            application.setYearsOfExperience((int) experienceSlider.getValue());
            application.setMotivation(motivationArea.getText().trim());
            application.setStatus("PENDING");

            // Salaire souhaité (optionnel)
            String salaryText = expectedSalaryField.getText().trim();
            if (!salaryText.isEmpty()) {
                try {
                    application.setExpectedSalary(Double.parseDouble(salaryText));
                } catch (NumberFormatException e) {
                    application.setExpectedSalary(0.0);
                }
            } else {
                application.setExpectedSalary(0.0);
            }

            // Sauvegarder
            long id = service.add(application);

            if (id > 0) {
                System.out.println("✅ Candidature envoyée avec l'ID: " + id);
                showSuccess();
                closeModal();
            } else {
                showError("Erreur", "Impossible d'envoyer la candidature.");
            }

        } catch (SQLException e) {
            showError("Erreur de base de données", e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        if (fullNameField.getText().trim().isEmpty()) {
            showWarning("Champ manquant", "Veuillez entrer votre nom complet.");
            fullNameField.requestFocus();
            return false;
        }

        if (phoneField.getText().trim().isEmpty()) {
            showWarning("Champ manquant", "Veuillez entrer votre numéro de téléphone.");
            phoneField.requestFocus();
            return false;
        }

        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showWarning("Champ manquant", "Veuillez entrer votre email.");
            emailField.requestFocus();
            return false;
        }

        // Validation simple de l'email
        if (!email.contains("@") || !email.contains(".")) {
            showWarning("Email invalide", "Veuillez entrer un email valide (ex: nom@example.com).");
            emailField.requestFocus();
            return false;
        }

        String motivation = motivationArea.getText().trim();
        if (motivation.isEmpty()) {
            showWarning("Champ manquant", "Veuillez écrire votre lettre de motivation.");
            motivationArea.requestFocus();
            return false;
        }

        if (motivation.length() < 50) {
            showWarning("Motivation trop courte",
                    "Votre lettre de motivation doit contenir au moins 50 caractères.\n" +
                            "Actuellement : " + motivation.length() + " caractères.");
            motivationArea.requestFocus();
            return false;
        }

        // Validation du salaire (si renseigné)
        String salaryText = expectedSalaryField.getText().trim();
        if (!salaryText.isEmpty()) {
            try {
                double salary = Double.parseDouble(salaryText);
                if (salary < 0) {
                    showWarning("Salaire invalide", "Le salaire ne peut pas être négatif.");
                    expectedSalaryField.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                showWarning("Salaire invalide", "Veuillez entrer un nombre valide.");
                expectedSalaryField.requestFocus();
                return false;
            }
        }

        return true;
    }

    @FXML
    private void handleCancel() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Annuler la candidature ?");
        confirm.setContentText("Les informations saisies seront perdues.");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            closeModal();
        }
    }

    private void showSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Candidature envoyée");
        alert.setHeaderText("✅ Succès !");
        alert.setContentText(
                "Votre candidature a été envoyée avec succès !\n\n" +
                        "Le propriétaire de l'offre examinera votre profil et vous contactera si vous êtes sélectionné.\n\n" +
                        "Vous pouvez suivre le statut de votre candidature dans \"Mes Candidatures\"."
        );
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeModal() {
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        stage.close();
    }
}