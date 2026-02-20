package controllers;

import entities.CollabApplication;
import entities.CollabRequest;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import mains.MainFX;
import services.CollabApplicationService;
import services.CollabRequestService;

import java.sql.SQLException;
import java.util.List;

public class ViewApplicationsController {

    @FXML private TableView<CollabApplication> applicationsTable;
    @FXML private TableColumn<CollabApplication, String> fullNameColumn;
    @FXML private TableColumn<CollabApplication, String> phoneColumn;
    @FXML private TableColumn<CollabApplication, String> emailColumn;
    @FXML private TableColumn<CollabApplication, Integer> experienceColumn;
    @FXML private TableColumn<CollabApplication, Double> expectedSalaryColumn;
    @FXML private TableColumn<CollabApplication, String> motivationColumn;
    @FXML private TableColumn<CollabApplication, String> statusColumn;

    @FXML private Text requestTitleText;
    @FXML private Text applicationsCountText;
    @FXML private VBox emptyStateBox;

    private CollabApplicationService applicationService = new CollabApplicationService();
    private CollabRequestService requestService = new CollabRequestService();

    private Long currentRequestId;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadData();
    }

    private void setupTableColumns() {
        // Configuration des colonnes
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        experienceColumn.setCellValueFactory(new PropertyValueFactory<>("yearsOfExperience"));
        expectedSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("expectedSalary"));
        motivationColumn.setCellValueFactory(new PropertyValueFactory<>("motivation"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Style pour l'expérience
        experienceColumn.setCellFactory(column -> new TableCell<CollabApplication, Integer>() {
            @Override
            protected void updateItem(Integer years, boolean empty) {
                super.updateItem(years, empty);
                if (empty || years == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(years + " ans");
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });

        // Style pour le salaire
        expectedSalaryColumn.setCellFactory(column -> new TableCell<CollabApplication, Double>() {
            @Override
            protected void updateItem(Double salary, boolean empty) {
                super.updateItem(salary, empty);
                if (empty || salary == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f DT", salary));
                    setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");
                }
            }
        });

        // Style pour la motivation (limiter le texte)
        motivationColumn.setCellFactory(column -> new TableCell<CollabApplication, String>() {
            @Override
            protected void updateItem(String motivation, boolean empty) {
                super.updateItem(motivation, empty);
                if (empty || motivation == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    String truncated = motivation.length() > 50
                            ? motivation.substring(0, 50) + "..."
                            : motivation;
                    setText(truncated);
                    setTooltip(new Tooltip(motivation)); // Afficher le texte complet au survol
                    setStyle("-fx-wrap-text: true;");
                }
            }
        });

        // Style pour le statut (badges colorés)
        statusColumn.setCellFactory(column -> new TableCell<CollabApplication, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    switch (status) {
                        case "PENDING":
                            setText("EN ATTENTE");
                            setStyle("-fx-background-color: #FFA726; -fx-text-fill: white; -fx-alignment: CENTER; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5 10;");
                            break;
                        case "APPROVED":
                            setText("ACCEPTÉE");
                            setStyle("-fx-background-color: #66BB6A; -fx-text-fill: white; -fx-alignment: CENTER; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5 10;");
                            break;
                        case "REJECTED":
                            setText("REJETÉE");
                            setStyle("-fx-background-color: #EF5350; -fx-text-fill: white; -fx-alignment: CENTER; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5 10;");
                            break;
                        default:
                            setText(status);
                            setStyle("-fx-alignment: CENTER;");
                    }
                }
            }
        });
    }

    private void loadData() {
        // Récupérer l'ID de la demande depuis MyRequestsController
        currentRequestId = MyRequestsController.getSelectedRequestId();

        if (currentRequestId == null) {
            showError("Erreur", "Aucune demande sélectionnée.");
            handleBack();
            return;
        }

        try {
            // Charger les informations de la demande
            CollabRequest request = requestService.findById(currentRequestId);
            if (request != null) {
                requestTitleText.setText(request.getTitle());
            }

            // Charger les candidatures
            List<CollabApplication> applications = applicationService.getApplicationsByRequestId(currentRequestId);

            if (applications.isEmpty()) {
                // Afficher le message "aucune candidature"
                applicationsTable.setVisible(false);
                applicationsTable.setManaged(false);
                emptyStateBox.setVisible(true);
                emptyStateBox.setManaged(true);
                applicationsCountText.setText("0 candidature");
            } else {
                // Afficher le tableau
                applicationsTable.setVisible(true);
                applicationsTable.setManaged(true);
                emptyStateBox.setVisible(false);
                emptyStateBox.setManaged(false);

                applicationsTable.getItems().clear();
                applicationsTable.getItems().addAll(applications);

                applicationsCountText.setText(applications.size() + " candidature" + (applications.size() > 1 ? "s" : ""));
            }

            System.out.println("✅ Chargé " + applications.size() + " candidature(s) pour la demande #" + currentRequestId);

        } catch (SQLException e) {
            showError("Erreur de chargement", "Impossible de charger les candidatures : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleApprove() {
        CollabApplication selected = applicationsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner une candidature à accepter.");
            return;
        }

        if ("APPROVED".equals(selected.getStatus())) {
            showInfo("Déjà acceptée", "Cette candidature a déjà été acceptée.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Accepter cette candidature ?");
        confirm.setContentText("Candidat : " + selected.getFullName() + "\n" +
                "Expérience : " + selected.getYearsOfExperience() + " ans\n" +
                "Salaire demandé : " + selected.getExpectedSalary() + " DT/jour");

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                applicationService.approveApplication(selected.getId());
                showInfo("Succès", "Candidature acceptée avec succès !\n\n" +
                        "Le candidat sera notifié de votre décision.");
                loadData(); // Rafraîchir la liste
            } catch (SQLException e) {
                showError("Erreur", "Impossible d'accepter la candidature.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleReject() {
        CollabApplication selected = applicationsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner une candidature à rejeter.");
            return;
        }

        if ("REJECTED".equals(selected.getStatus())) {
            showInfo("Déjà rejetée", "Cette candidature a déjà été rejetée.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Rejeter cette candidature ?");
        confirm.setContentText("Candidat : " + selected.getFullName());

        if (confirm.showAndWait().get() == ButtonType.OK) {
            try {
                applicationService.rejectApplication(selected.getId());
                showInfo("Candidature rejetée", "La candidature a été rejetée.");
                loadData(); // Rafraîchir la liste
            } catch (SQLException e) {
                showError("Erreur", "Impossible de rejeter la candidature.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRefresh() {
        System.out.println("🔄 Rafraîchissement de la liste...");
        loadData();
        showInfo("Rafraîchi", "La liste des candidatures a été mise à jour.");
    }

    @FXML
    private void handleBack() {
        MainFX.showMyRequests();
    }

    // Méthodes utilitaires
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