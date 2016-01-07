/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lue.board;

import java.util.ArrayList;

/**
 *
 * @author Lue Infoservices
 */
public class BoardSearchResponse {
    
    private ArrayList<Board> data=new ArrayList<Board>();
    
    private Page page=new Page();

    public ArrayList<Board> getData() {
        return data;
    }

    public void setData(ArrayList<Board> data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
    
    
    
    
}
