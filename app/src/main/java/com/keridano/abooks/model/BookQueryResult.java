package com.keridano.abooks.model;

import java.util.List;

/**
 * Created by kerid on 20/11/2017.
 */
public class BookQueryResult {

    private String      kind;
    private int         totalItems;
    private List<Book>  items;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<Book> getItems() {
        return items;
    }

    public void setItems(List<Book> items) {
        this.items = items;
    }

}
