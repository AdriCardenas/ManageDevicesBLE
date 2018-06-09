package adriancardenas.com.ehealth.Database;

public class DatabaseMetadata {
    interface  TABLES{
        String WEIGHT = "weight";
        String HEART = "heart";
        String STEPS = "steps";
    }

    interface WEIGHT_COLUMS{
        String ID = "id";
        String VALUE = "value";
    }

    interface STEPS_COLUMS{
        String ID = "id";
        String VALUE = "value";
    }

    interface HEART_RATE_COLUMS{
        String ID = "id";
        String VALUE = "value";
    }
}
