package vn.edu.hcmus.fit.pmtoan;

/**
 * vn.edu.hcmus.fit.pmtoan
 * Create by pmtoan
 * Date 12/24/2021 - 2:28 PM
 * Description: ...
 */


public class Dictionary {
    private String slang;
    private String definition;

    public Dictionary(String slang, String definition){
        this.slang = slang;
        this.definition = definition;
    }

    public String getSlang() {
        return slang;
    }

    public void setSlang(String slang) {
        this.slang = slang;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }
}