package controllers;

import entities.CollabRequest;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mains.MainFX;
import services.CollabRequestService;

import java.io.IOException;
import java.sql.SQLException;

public class CollabRequestDetailsController {

    // ✅ Déclaration des éléments FXML
    @FXML private Text titleText;
    @FXML private Text locationText;
    @FXML private Text dateRangeText;
    @FXML private Text neededPeopleText;
    @FXML private Text salaryText;
    @FXML private Text publisherText;
    @FXML private TextArea descriptionArea;
    @FXML private Button applyButton;  // ✅ AJOUTÉ

    // ✅ Variables de classe
    private CollabRequest currentRequest;  // ✅ AJOUTÉ
    private CollabRequestService requestService = new CollabRequestService();

    /**
     * Définir les données de la demande à afficher
     */
    public void setRequestData(CollabRequest request) {
        this.currentRequest = request;
        displayRequestDetails();
    }

    /**
     * Afficher les détails de la demande
     */
    private void displayRequestDetails() {
        if (currentRequest == null) return;

        titleText.setText(currentRequest.getTitle());
        locationText.setText(currentRequest.getLocation());
        dateRangeText.setText(currentRequest.getStartDate() + " - " + currentRequest.getEndDate());
        neededPeopleText.setText(String.valueOf(currentRequest.getNeededPeople()));
        salaryText.setText(currentRequest.getSalary() + " DT/jour");
        descriptionArea.setText(currentRequest.getDescription());
        descriptionArea.setEditable(false);

        // Récupérer le nom du publisher (si disponible)
        try {
            // Récupérer depuis la base de données ou utiliser un nom par défaut
            publisherText.setText("Publié par Ali Ben Ahmed");
        } catch (Exception e) {
            publisherText.setText("Publié par un agriculteur");
        }
    }

    /**
     * Gérer le clic sur le bouton "Postuler"
     */
    @FXML
    private void handleApply() {
        try {
            // Charger le fichier FXML du modal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ApplyCollaboration.fxml"));
            Parent root = loader.load();

            // Récupérer le controller du modal
            ApplyCollaborationController controller = loader.getController();

            // Passer les données de la demande au controller
            controller.setRequestData(currentRequest.getId(), currentRequest.getTitle());

            // Créer une nouvelle fenêtre modale
            Stage modalStage = new Stage();
            modalStage.setTitle("Postuler à l'offre");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(applyButton.getScene().getWindow());
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);

            // Afficher le modal
            modalStage.showAndWait();

        } catch (IOException e) {
            showError("Erreur", "Impossible d'ouvrir le formulaire de candidature : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gérer le clic sur le bouton "Retour"
     */
    @FXML
    private void handleBack() {
        MainFX.showExploreCollaborations();
    }

    /**
     * Afficher une alerte d'erreur
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
