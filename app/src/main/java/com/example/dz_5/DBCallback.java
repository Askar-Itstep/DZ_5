package com.example.dz_5;
import com.example.dz_5.entity.Album;
import com.example.dz_5.entity.DBEntity;
import java.util.List;

public interface DBCallback<T extends DBEntity> {
    void onSelectCollection(List<Album> collection);
    void onSelectSingleItem(T item);
    void onSave();
    void onUpdate();
    void onDelete();
}
