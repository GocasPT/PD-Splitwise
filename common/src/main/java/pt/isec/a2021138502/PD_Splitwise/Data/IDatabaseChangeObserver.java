package pt.isec.a2021138502.PD_Splitwise.Data;

public interface IDatabaseChangeObserver {
	void onDatabaseChange(String query);
}
