
package com.lue.pins;
import java.util.ArrayList;

/**
 *
 * @author Lue Infoservices
 */
public class PinSearchResponse {
  
    private ArrayList<PinData> data=new ArrayList<PinData>();
    
   private Page page=new Page();

    public ArrayList<PinData> getData() {
        return data;
    }

    public void setData(ArrayList<PinData> data) {
        this.data = data;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
