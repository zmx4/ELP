package org.tick.elp.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import org.tick.elp.Service.IUserDataStorage;
import org.tick.elp.Service.UserDataStorage;

import java.util.List;

public class CollectionController {

    @FXML
    private ListView<String> collectionListView;

    private final IUserDataStorage userDataStorage;
    private final ObservableList<String> collectionList;

    public CollectionController() {
        this.userDataStorage = UserDataStorage.getInstance();
        this.collectionList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        collectionListView.setItems(collectionList);
        collectionListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        loadCollections();
    }

    private void loadCollections() {
        collectionList.clear();
        List<String> words = userDataStorage.getCollections();
        if (words != null) {
            collectionList.addAll(words);
        }
    }

    @FXML
    protected void onRefreshButtonClick() {
        loadCollections();
    }

    @FXML
    protected void onDeleteButtonClick() {
        String selectedWord = collectionListView.getSelectionModel().getSelectedItem();
        if (selectedWord != null) {
            if (userDataStorage.removeCollection(selectedWord)) {
                collectionList.remove(selectedWord);
            }
        }
    }
}
