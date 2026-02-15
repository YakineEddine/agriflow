package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import mains.MainFX;
import entities.CollabRequest;
import services.CollabRequestService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MyRequestsController {

    @FXML
    private TableView<CollabRequest> requestsTable;

    @FXML
    private TableColumn<CollabRequest, String> titleColumn;

    @FXML
    private TableColumn<CollabRequest, LocalDate> startDateColumn;

    @FXML
    private TableColumn<CollabRequest, LocalDate> endDateColumn;

    @FXML
    private TableColumn<CollabRequest, String> statusColumn;

    @FXML
    private TableColumn<CollabRequest, Integer> neededPeopleColumn;

    private CollabRequestService service = new CollabRequestService();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadRequests();
    }

    private void setupTableColumns() {
        // Configurer les colonnes
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        neededPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("neededPeople"));

        // Style pour le statut
        statusColumn.setCellFactory(column -> new TableCell<CollabRequest, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "PENDING":
                            setStyle("-fx-background-color: #FFA726; -fx-text-fill: white; -fx-alignment: CENTER;");
                            break;
                        case "APPROVED":
                            setStyle("-fx-background-color: #66BB6A; -fx-text-fill: white; -fx-alignment: CENTER;");
                            break;
                        case "REJECTED":
                            setStyle("-fx-background-color: #EF5350; -fx-text-fill: white; -fx-alignment: CENTER;");
                            break;
                        default:
                            setStyle("-fx-alignment: CENTER;");
                    }
                }
            }
        });
    }

    private void loadRequests() {
        try {
            // Charger TOUTES les demandes (on filtrera par requester_id en production)
            List<CollabRequest> requests = service.findAll();

            if (requestsTable != null) {
                requestsTable.getItems().clear();
                requestsTable.getItems().addAll(requests);
            }

            System.out.println("✅ Chargé " + requests.size() + " demandes");

            if (requests.isEmpty()) {
                System.out.println("⚠️ Aucune demande trouvée dans la base de données");
            }

        } catch (SQLException e) {
            showError("Erreur lors du chargement des demandes", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewDetails() {
        CollabRequest selected = requestsTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            System.out.println("🔍 Affichage des détails de la demande #" + selected.getId());
            MainFX.showCollabRequestDetails(selected.getId());
        } else {
            showWarning("Aucune sélection", "Veuillez sélectionner une demande.");
        }
    }

    @FXML
    private void handleDelete() {
        CollabRequest selected = requestsTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmation");
            confirm.setHeaderText("Supprimer cette demande ?");
            confirm.setContentText("Êtes-vous sûr de vouloir supprimer \"" + selected.getTitle() + "\" ?");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                try {
                    service.delete(selected.getId());
                    showInfo("Succès", "La demande a été supprimée avec succès.");
                    loadRequests();
                } catch (SQLException e) {
                    showError("Erreur", "Impossible de supprimer la demande.");
                }
            }
        } else {
            showWarning("Aucune sélection", "Veuillez sélectionner une demande à supprimer.");
        }
    }

    @FXML
    private void handleBack() {
        MainFX.showExploreCollaborations();
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
