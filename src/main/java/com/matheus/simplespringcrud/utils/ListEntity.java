package com.matheus.simplespringcrud.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter(value = AccessLevel.NONE)
public class  ListEntity<T extends Entity> implements Entity{
    private int count;
    private List<T> content;

    public ListEntity(List<T> content) {
        this.content = content;
        this.count = content.size();
    }
}
