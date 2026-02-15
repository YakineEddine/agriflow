package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import mains.MainFX;
import entities.CollabRequest;
import services.CollabRequestService;
import validators.CollabRequestValidator;

import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Controller pour le formulaire de publication d'une demande
 */
public class PublishRequestController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField locationField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<Integer> neededPeopleCombo;

    @FXML
    private TextField salaryField;

    @FXML
    private TextArea descriptionArea;

    private CollabRequestService service = new CollabRequestService();

    @FXML
    public void initialize() {
        // Remplir le combo box avec des valeurs de 1 à 50
        for (int i = 1; i <= 50; i++) {
            neededPeopleCombo.getItems().add(i);
        }
        neededPeopleCombo.setValue(1);
    }

    @FXML
    private void handlePublish() {
        try {
            // Récupérer les valeurs
            String title = titleField.getText();
            String location = locationField.getText();
            String description = descriptionArea.getText();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            Integer neededPeople = neededPeopleCombo.getValue();
            String salaryText = salaryField.getText();

            // Validation des champs obligatoires
            if (!validateFields(title, location, description, startDate, endDate, neededPeople, salaryText)) {
                return;
            }

            // Validation avec le validator
            CollabRequestValidator.validateTitle(title);
            CollabRequestValidator.validateDescription(description);
            CollabRequestValidator.validateDates(startDate, endDate);
            CollabRequestValidator.validateNeededPeople(neededPeople);

            // Parser le salaire
            double salary = 0.0;
            try {
                salary = Double.parseDouble(salaryText);
                if (salary < 0) {
                    showError("Erreur de saisie", "Le salaire ne peut pas être négatif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Erreur de saisie", "Le salaire doit être un nombre valide.");
                return;
            }

            // Créer la demande avec TOUS les champs
            CollabRequest request = new CollabRequest();
            request.setRequesterId(1L); // ID utilisateur connecté (à remplacer par session)
            request.setTitle(title);
            request.setLocation(location);
            request.setDescription(description);
            request.setStartDate(startDate);
            request.setEndDate(endDate);
            request.setNeededPeople(neededPeople);
            request.setSalary(salary);
            request.setPublisher("Ali Ben Ahmed"); // Nom utilisateur connecté (à remplacer par session)
            request.setStatus("PENDING"); // ✅ STATUT PENDING par défaut

            // Sauvegarder
            long id = service.add(request);

            if (id > 0) {
                System.out.println("✅ Demande publiée avec l'ID: " + id + " (statut: PENDING)");

                showInfo("Succès",
                        "Votre demande a été soumise avec succès !\n\n" +
                                "Elle sera visible sur la page \"Explore Collaborations\" " +
                                "après validation par un administrateur.\n\n" +
                                "Vous pouvez suivre son statut dans \"Mes Demandes\".");

                MainFX.showMyRequests();
            } else {
                showError("Erreur", "Impossible de publier la demande.");
            }

        } catch (IllegalArgumentException e) {
            showError("Validation échouée", e.getMessage());
        } catch (SQLException e) {
            showError("Erreur de base de données", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Valide que tous les champs obligatoires sont remplis
     */
    private boolean validateFields(String title, String location, String description,
                                   LocalDate startDate, LocalDate endDate,
                                   Integer neededPeople, String salary) {

        if (title == null || title.trim().isEmpty()) {
            showWarning("Champ manquant", "Le titre est obligatoire.");
            return false;
        }

        if (location == null || location.trim().isEmpty()) {
            showWarning("Champ manquant", "Le lieu est obligatoire.");
            return false;
        }

        if (description == null || description.trim().isEmpty()) {
            showWarning("Champ manquant", "La description est obligatoire.");
            return false;
        }

        if (startDate == null) {
            showWarning("Champ manquant", "La date de début est obligatoire.");
            return false;
        }

        if (endDate == null) {
            showWarning("Champ manquant", "La date de fin est obligatoire.");
            return false;
        }

        if (neededPeople == null || neededPeople < 1) {
            showWarning("Champ manquant", "Le nombre de personnes doit être au moins 1.");
            return false;
        }

        if (salary == null || salary.trim().isEmpty()) {
            showWarning("Champ manquant", "Le salaire est obligatoire.");
            return false;
        }

        return true;
    }

    @FXML
    private void handleCancel() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Annuler la publication ?");
        confirm.setContentText("Les informations saisies seront perdues.");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            MainFX.showExploreCollaborations();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
