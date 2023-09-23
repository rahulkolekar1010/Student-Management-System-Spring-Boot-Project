package com.studentsystem.studentSystem.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageData {
    private Integer noOfPages;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private Integer currentPage;
}
