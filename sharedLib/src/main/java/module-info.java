module Splitwsie.sharedLib.main {
	requires java.xml;
	requires org.slf4j;
	requires java.sql;
	requires static lombok;

	exports pt.isec.pd.splitwise.sharedLib.database;
	exports pt.isec.pd.splitwise.sharedLib.database.DAO;
	exports pt.isec.pd.splitwise.sharedLib.database.DTO.Expense;
	exports pt.isec.pd.splitwise.sharedLib.database.DTO.Group;
	exports pt.isec.pd.splitwise.sharedLib.database.DTO.Invite;
	exports pt.isec.pd.splitwise.sharedLib.database.DTO.Payment;
	exports pt.isec.pd.splitwise.sharedLib.database.DTO.User;
	exports pt.isec.pd.splitwise.sharedLib.database.Entity;
	exports pt.isec.pd.splitwise.sharedLib.network.request;
	exports pt.isec.pd.splitwise.sharedLib.network.request.Expense;
	exports pt.isec.pd.splitwise.sharedLib.network.request.Group;
	exports pt.isec.pd.splitwise.sharedLib.network.request.Invite;
	exports pt.isec.pd.splitwise.sharedLib.network.request.Payment;
	exports pt.isec.pd.splitwise.sharedLib.network.request.User;
	exports pt.isec.pd.splitwise.sharedLib.network.response;
	exports pt.isec.pd.splitwise.sharedLib.terminal;
}