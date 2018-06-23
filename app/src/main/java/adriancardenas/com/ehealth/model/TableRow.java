package adriancardenas.com.ehealth.model;

/**
 * Created by Adrian on 23/06/2018.
 */

public class TableRow {
    String column1;
    String column2;

    public TableRow(String column1, String column2) {
        this.column1 = column1;
        this.column2 = column2;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }
}
