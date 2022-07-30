package com.unibe.titulation.dtos;

import com.unibe.titulation.entities.Reader;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class ReadersListTable {

    @Valid
    @NotEmpty
    public List<Reader> readers;

    public ReadersListTable() {
    }

    public ReadersListTable(List<Reader> readers) {
        this.readers = readers;
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public void setReaders(List<Reader> readers) {
        this.readers = readers;
    }
}
