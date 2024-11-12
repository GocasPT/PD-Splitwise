module Splitwsie.sharedLib.main {
	requires org.slf4j;
	requires java.sql;

	exports pt.isec.pd.sharedLib.database;
	exports pt.isec.pd.sharedLib.database.DAO;
	exports pt.isec.pd.sharedLib.database.DTO.Expense;
	exports pt.isec.pd.sharedLib.database.DTO.Group;
	exports pt.isec.pd.sharedLib.database.DTO.Invite;
	exports pt.isec.pd.sharedLib.database.DTO.Payment;
	exports pt.isec.pd.sharedLib.database.DTO.User;
	exports pt.isec.pd.sharedLib.database.Entity;
	exports pt.isec.pd.sharedLib.network.request;
	exports pt.isec.pd.sharedLib.network.request.Expense;
	exports pt.isec.pd.sharedLib.network.request.Group;
	exports pt.isec.pd.sharedLib.network.request.Invite;
	exports pt.isec.pd.sharedLib.network.request.Payment;
	exports pt.isec.pd.sharedLib.network.request.User;
	exports pt.isec.pd.sharedLib.network.response;
	exports pt.isec.pd.sharedLib.terminal;
}