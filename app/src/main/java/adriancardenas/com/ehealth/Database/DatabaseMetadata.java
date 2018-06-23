package adriancardenas.com.ehealth.Database;

public class DatabaseMetadata {
    public interface  TABLES{
        String WEIGHT = "weight";
        String HEART = "heart";
        String STEPS = "steps";
        String DISTANCE = "distance";
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

    interface DISTANCE_COLUMN{
        String ID = "id";
        String VALUE = "value";
    }
}
