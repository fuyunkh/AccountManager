package org.cote.accountmanager.data.sod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cote.accountmanager.data.ConnectionFactory;
import org.cote.accountmanager.data.DBFactory;
import org.cote.accountmanager.data.DBFactory.CONNECTION_TYPE;


public class SoDPolicyUtil {
	public static final Logger logger = Logger.getLogger(SoDPolicyUtil.class.getName());
	private static Map<String,List<String>> activityPermissions = new HashMap<String,List<String>>();
	public static List<String> getActivityPermissions(String activityUrn){
		if(activityPermissions.containsKey(activityUrn) == false){
			activityPermissions.put(activityUrn, new ArrayList<String>());
		}
		List<String> perms = activityPermissions.get(activityUrn);
		Connection conn = ConnectionFactory.getInstance().getConnection();
		CONNECTION_TYPE connectionType = DBFactory.getConnectionType(conn);
		String token = DBFactory.getParamToken(connectionType);
		
		if(perms.size() == 0){
			try{
				PreparedStatement statement = conn.prepareStatement("select distinct permissionUrn from groupEntitlementsCache WHERE groupurn = " + token);
				statement.setString(1, activityUrn);
				ResultSet rset = statement.executeQuery();
				while(rset.next()){
					perms.add(rset.getString(1));
				}
				rset.close();
				statement.close();

			}
			catch(SQLException sqe){
				logger.error(sqe.getMessage());
			}
			finally{
				try{
					conn.close();
				}
				catch(SQLException sqe){
					logger.error(sqe.getMessage());
				}
			}
		}
		return perms;
	}
	public static List<String> getActivityPermissionsForAccount(String activityUrn, String urn){
		return getActivityPermissionsForType("account", activityUrn, urn);
	}
	public static List<String> getActivityPermissionsForPerson(String activityUrn, String urn){
		return getActivityPermissionsForType("person", activityUrn, urn);
	}
	/// NOTE: This could differentiate between any and all permissions
	/// XXX - making this all XXX  At the moment, it's just ANY - but for all, the returned permission size must be greater than (but should be equal to) the activity permission size
	///
	private static List<String> getActivityPermissionsForType(String type, String activityUrn, String urn){
		List<String> perms = new ArrayList<String>();
		List<String> actPerms = getActivityPermissions(activityUrn);
		if(actPerms.size() == 0){
			logger.warn("Zero permissions found for " + activityUrn);
			return perms;
		}
		Connection conn = ConnectionFactory.getInstance().getConnection();
		CONNECTION_TYPE connectionType = DBFactory.getConnectionType(conn);
		String token = DBFactory.getParamToken(connectionType);
		
		
		try{
			StringBuffer buff = new StringBuffer();
			for(int i = 0; i < actPerms.size();i++){
				if(i > 0 ) buff.append(",");
				buff.append("'" + actPerms.get(i) + "'");
			}
			
			/// actPerms contains a list of permissions associated with an activity
			/// Look for permissions for the account where they are not directly associated with that activity (which, at the moment, may happen if a role is directly attached to an activity)
			///
			PreparedStatement statement = conn.prepareStatement("select distinct personUrn,accountUrn,permissionUrn from groupEntitlementsCache where permissionurn in (" + buff.toString() + ") AND groupurn <> " + token + " AND " + type + "urn = " + token + ";");
			
			statement.setString(1, activityUrn);
			statement.setString(2, urn);
			
			ResultSet rset = statement.executeQuery();
			while(rset.next()){
				perms.add(rset.getString(1));
			}
			rset.close();
			statement.close();

		}
		catch(SQLException sqe){
			logger.error(sqe.getMessage());
		}
		finally{
			try{
				conn.close();
			}
			catch(SQLException sqe){
				logger.error(sqe.getMessage());
			}
		}
		logger.info("Found " + perms.size() + " permissions for " + urn + " in activity " + activityUrn);
		if(perms.size() > 0 && perms.size() != actPerms.size()){
			logger.error("CASE ALL: Returned permission size was not equal to the activity permission size, so a negative match is being returned");
			perms.clear();
		}
		return perms;
	}
}