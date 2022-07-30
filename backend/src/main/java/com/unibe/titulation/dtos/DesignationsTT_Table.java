package com.unibe.titulation.dtos;

import com.unibe.titulation.entities.DesignationTT;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class DesignationsTT_Table {

    @Valid
    @NotEmpty
    public List<DesignationTT> designationTTList;

    public DesignationsTT_Table() {
    }

    public DesignationsTT_Table(List<DesignationTT> designationTTList) {
        this.designationTTList = designationTTList;
    }

    public List<DesignationTT> getDesignationTTList() {
        return designationTTList;
    }

    public void setDesignationTTList(List<DesignationTT> designationTTList) {
        this.designationTTList = designationTTList;
    }
}
