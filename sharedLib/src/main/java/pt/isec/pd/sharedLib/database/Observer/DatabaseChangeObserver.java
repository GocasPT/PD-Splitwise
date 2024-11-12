package pt.isec.pd.sharedLib.database.Observer;

public interface DatabaseChangeObserver {
	void onDBChange(String query, Object... params);
}
