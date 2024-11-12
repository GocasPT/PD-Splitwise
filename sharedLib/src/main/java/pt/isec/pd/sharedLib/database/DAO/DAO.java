package pt.isec.pd.sharedLib.database.DAO;

import org.slf4j.Logger;
import pt.isec.pd.sharedLib.database.DataBaseManager;


public abstract class DAO {
	protected final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	protected final DataBaseManager dbManager;

	public DAO(DataBaseManager dbManager) {
		this.dbManager = dbManager;
	}
}
