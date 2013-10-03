
Hemi.util.logger.addLogger(this, "Operation", "Operation Module", "622");

this.DoOperation = function(){
	/// TODO: The template contents should be provided by teh template service, not
	/// hard coded into the operation
	///
	var sName = this.getProperties().user_name;
	var sPass = this.getProperties().password;
	var oOrg = this.getProperties().organization;
	this.log(sName + "/" + sPass);
	var oUser = uwm.login(sName, sPass, oOrg);
	if(oUser != null){
		this.log("Logged in!");
		uwm.operation("ContinueWorkflow", {user:oUser}, 0, this.ruleName);
	}
	this.logError("Failed to log in");
}
this.SetRule = function(sRule){
	this.ruleName = sRule;
}