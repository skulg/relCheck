
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



	public class DbHelper extends SQLiteOpenHelper {
		
		
		
		
		
		private static final String TEXT_TYPE = " TEXT";
		private static final String COMMA_SEP = ",";
	
		
		private static final String SQL_CREATE_ENTRIES =
		    "CREATE TABLE " + relationDBContract.RelationEntry.TABLE_NAME + " (" +
		    		relationDBContract.RelationEntry._ID + " INTEGER PRIMARY KEY," +
		    		relationDBContract.RelationEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
		    relationDBContract.RelationEntry.COLUMN_NAME_FIRSTTERM + TEXT_TYPE + COMMA_SEP +
		    relationDBContract.RelationEntry.COLUMN_NAME_SECONDTERM + TEXT_TYPE + COMMA_SEP +
		    relationDBContract.RelationEntry.COLUMN_NAME_THIRDTERM + TEXT_TYPE + COMMA_SEP +
		    relationDBContract.RelationEntry.COLUMN_NAME_WEIGHT + " REAL" + COMMA_SEP +
		    relationDBContract.RelationEntry.COLUMN_NAME_CONFIDENCE + " REAL" + COMMA_SEP +

		    // Any other options for the CREATE command
		    " )";

		private static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + relationDBContract.RelationEntry.TABLE_NAME;
		
	    // If you change the database schema, you must increment the database version.
	    public static final int DATABASE_VERSION = 1;
	    public static final String DATABASE_NAME = "RelationsDB.db";

	    public DbHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(SQL_CREATE_ENTRIES);
	    }
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // This database is only a cache for online data, so its upgrade policy is
	        // to simply to discard the data and start over
	        db.execSQL(SQL_DELETE_ENTRIES);
	        onCreate(db);
	    }
	    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        onUpgrade(db, oldVersion, newVersion);
	    }
	}