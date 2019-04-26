package ru.smv.system.restaurant.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smv.system.restaurant.models.db.MenuEntity;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MenuDTO {
    private Long id;
    private String name;
    private String note;
    private String photoSrc;
    private LocalDate dateMenu;

    public MenuDTO(MenuEntity menu){
        this.id = menu.getId();
        this.name = menu.getName();
        this.note = menu.getNote();
        this.photoSrc = menu.getPhotoSrc();
        this.dateMenu = menu.getDateMenu();
    }


}
