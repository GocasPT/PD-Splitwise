package pt.isec.pd.sharedLib.database.Entity;

import java.io.Serializable;

public abstract class Entity implements Serializable {
	protected int id;

	public Entity(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
