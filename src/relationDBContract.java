import android.provider.BaseColumns;


public final class relationDBContract {
	
	    

		// To prevent someone from accidentally instantiating the contract class,
	    // give it an empty constructor.
	    public relationDBContract() {}

	    /* Inner class that defines the table contents */
	    public static abstract class RelationEntry implements BaseColumns {
	        public static final String TABLE_NAME = "relation";
	        public static final String COLUMN_NAME_ENTRY_ID = "relationid";
	        public static final String COLUMN_NAME_FIRSTTERM = "firstTerm";
	        public static final String COLUMN_NAME_SECONDTERM = "secondTerm";
	        public static final String COLUMN_NAME_THIRDTERM = "thirdTerm";
	        public static final String COLUMN_NAME_WEIGHT = "weight";
	        public static final String COLUMN_NAME_CONFIDENCE = "confidence";
	        
	    }
	
}
