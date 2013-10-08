package org.cote.accountmanager.data.factory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.XMLGregorianCalendar;

import org.cote.accountmanager.data.ArgumentException;
import org.cote.accountmanager.data.DataAccessException;
import org.cote.accountmanager.data.DataRow;
import org.cote.accountmanager.data.DataTable;
import org.cote.accountmanager.data.Factories;
import org.cote.accountmanager.data.FactoryException;
import org.cote.accountmanager.data.query.QueryField;
import org.cote.accountmanager.data.query.QueryFields;
import org.cote.accountmanager.objects.AccountGroupType;
import org.cote.accountmanager.objects.AccountType;
import org.cote.accountmanager.objects.BaseGroupType;
import org.cote.accountmanager.objects.BucketGroupType;
import org.cote.accountmanager.objects.DataType;
import org.cote.accountmanager.objects.DirectoryGroupType;
import org.cote.accountmanager.objects.NameIdType;
import org.cote.accountmanager.objects.OrganizationType;
import org.cote.accountmanager.objects.ProcessingInstructionType;
import org.cote.accountmanager.objects.UserGroupType;
import org.cote.accountmanager.objects.UserType;
import org.cote.accountmanager.objects.types.ComparatorEnumType;
import org.cote.accountmanager.objects.types.FactoryEnumType;
import org.cote.accountmanager.objects.types.GroupEnumType;
import org.cote.accountmanager.objects.types.NameEnumType;
import org.cote.accountmanager.objects.types.OrganizationEnumType;
import org.cote.accountmanager.objects.types.SqlDataEnumType;

public class GroupFactory  extends NameIdFactory {
	public GroupFactory(){
		super();
		this.scopeToOrganization = true;
		this.tableNames.add("groups");
		factoryType = FactoryEnumType.GROUP;
	}
	
	protected void configureTableRestrictions(DataTable table){
		if(table.getName().equalsIgnoreCase("groups")){
			/// table.setRestrictSelectColumn("logicalid", true);
		}
	}
	
	@Override
	public <T> String getCacheKeyName(T obj){
		BaseGroupType t = (BaseGroupType)obj;
		return t.getName() + "-" + t.getGroupType().toString() + "-" + t.getParentId() + "-" + t.getOrganization().getId();
	}
	
	protected String getSelectTemplate(DataTable table, ProcessingInstructionType instruction){
		return table.getSelectFullTemplate();
	}
	public void initialize(Connection connection) throws FactoryException{
		super.initialize(connection);
		
	}
	
	protected void addDefaultGroups(OrganizationType organization)  throws FactoryException, ArgumentException
	{
		addDefaultDirectoryGroups(organization);
	}
	protected void addDefaultDirectoryGroups(OrganizationType organization) throws FactoryException, ArgumentException
	{
		if(organization == null || organization.getId() <= 0) throw new FactoryException("Invalid organization");
		DirectoryGroupType root_dir = newDirectoryGroup("Root", null, organization);
		
		//System.out.println(root_dir.getName() + ":" + root_dir.getGroupType() + ":" + root_dir.getParentId() + ":" + root_dir.getReferenceId());
		
		addGroup(root_dir);
		root_dir = getDirectoryByName("Root", organization);

		DirectoryGroupType home_dir = newDirectoryGroup("Home", root_dir, organization);
		addGroup(home_dir);
		DirectoryGroupType persons_dir = newDirectoryGroup("Persons", root_dir, organization);
		addGroup(persons_dir);

	}
	public UserGroupType newUserGroup(String group_name, BaseGroupType parent, OrganizationType organization) throws ArgumentException
	{
		return newUserGroup(null, group_name, parent, organization);
	}
	public UserGroupType newUserGroup(UserType owner, String group_name, BaseGroupType parent, OrganizationType organization) throws ArgumentException
	{
		if (parent != null) clearGroupCache(parent, false);
		return (UserGroupType)newGroup(owner, group_name, GroupEnumType.USER, parent, organization);
	}	
	public DirectoryGroupType newDirectoryGroup(String group_name, BaseGroupType parent, OrganizationType organization) throws ArgumentException
	{
		return newDirectoryGroup(null, group_name, parent, organization);
	}
	public DirectoryGroupType newDirectoryGroup(UserType owner, String group_name, BaseGroupType parent, OrganizationType organization) throws ArgumentException
	{
		if (parent != null) clearGroupCache(parent, false);
		return (DirectoryGroupType)newGroup(owner, group_name, GroupEnumType.DATA, parent, organization);
	}
	
	public BaseGroupType newGroup(String group_name, OrganizationType organization)
	{
		return newGroup(null,group_name, GroupEnumType.UNKNOWN, null, organization);
	}


	public BaseGroupType newGroup(String group_name, GroupEnumType Type, OrganizationType organization)
	{
		return newGroup(null, group_name, Type, null, organization);
	}
	public BaseGroupType newGroup(UserType owner, String group_name, GroupEnumType Type, OrganizationType organization)
	{
		return newGroup(owner, group_name, Type, null, organization);
	}
	public BaseGroupType newGroup(UserType owner, String group_name, GroupEnumType Type, BaseGroupType parent, OrganizationType organization)
	{
		///System.out.println("New Group Owner: " + (owner == null ? "Null": owner.getName() ));
		BaseGroupType new_group = newGroup(Type);
		if(owner != null) new_group.setOwnerId(owner.getId());
		new_group.setOrganization(organization);
		new_group.setName(group_name);
		if (parent != null) new_group.setParentId(parent.getId());
		return new_group;
	}
	protected BaseGroupType newGroup(GroupEnumType Type)
	{
		BaseGroupType new_group = null;
		switch (Type)
		{
			case BUCKET:
				new_group = new BucketGroupType();
				break;
			case DATA:
				new_group = new DirectoryGroupType();
				break;
			case ACCOUNT:
				new_group = new AccountGroupType();
				break;
			case USER:
				new_group = new UserGroupType();
				break;
			default:
				new_group = new BaseGroupType();
				break;
		}
		new_group.setGroupType(Type);
		new_group.setNameType(NameEnumType.GROUP);
		return new_group;
	}
	
	public boolean addGroup(BaseGroupType group) throws FactoryException, ArgumentException
	{
		if (group.getOrganization() == null || group.getOrganization().getId() <= 0) throw new FactoryException("Cannot add group without Organization.");
		try{
			DataRow row = prepareAdd(group, "groups");
			row.setCellValue("grouptype", group.getGroupType().toString());
			row.setCellValue("referenceid", group.getReferenceId());
			if (insertRow(row))
			{
				if(!bulkMode) clearGroupParentCache(group);
				return true;
			}
		}
		catch(DataAccessException dae){
			throw new FactoryException(dae.getMessage());
		}
		
		return false;
	}
	@Override
	protected NameIdType read(ResultSet rset, ProcessingInstructionType instruction) throws SQLException, FactoryException, ArgumentException
	{
		BaseGroupType new_group = newGroup(GroupEnumType.valueOf(rset.getString("grouptype")));
		new_group.setReferenceId(rset.getLong("referenceid"));
		return super.read(rset, new_group);
	}
	
	public boolean deleteDirectoryGroup(DirectoryGroupType directory) throws FactoryException, ArgumentException
	{
		if (directory == null) return true;
			//throw new ArgumentException("Null directory reference");
		

		
		logger.info("Deleting " + directory.getName());
		populate(directory);
		populateSubDirectories(directory);
		DirectoryGroupType[] sub_dirs = directory.getSubDirectories().toArray(new DirectoryGroupType[0]);
		for (int i = sub_dirs.length - 1; i >= 0; i--) deleteDirectoryGroup(sub_dirs[i]);
		Factories.getDataFactory().deleteDataInGroup(directory);
		return deleteGroup(directory);
	}
	public boolean deleteGroup(BaseGroupType group) throws FactoryException, ArgumentException
	{
		int deleted = deleteById(group.getId(), group.getOrganization().getId());
		clearGroupCache(group, true);
		return (deleted > 0);
	}
	public int deleteGroupsByUser(UserType map) throws FactoryException
	{
		/// , QueryFields.getFieldGroupType(GroupEnumType.valueOf(map.getNameType().toString())) 
		long[] ids = getIdByField(new QueryField[] { QueryFields.getFieldOwner(map.getId())}, map.getOrganization().getId());
		return deleteGroupsByIds(ids, map.getOrganization());
	}
	public int deleteGroupsByIds(long[] ids, OrganizationType organization) throws FactoryException
	{
		int deleted = deleteById(ids, organization.getId());
		if (deleted > 0)
		{
			Factories.getGroupParticipationFactory().deleteParticipations(ids, organization);
		}
		return deleted;
	}
	public UserGroupType getUserGroupByName(String name, OrganizationType organization) throws FactoryException, ArgumentException
	{
		return (UserGroupType)getGroupByName(name, GroupEnumType.USER, null, organization);
	}
	public UserGroupType getUserGroupByName(String name, BaseGroupType parent, OrganizationType organization) throws FactoryException, ArgumentException
	{
		return (UserGroupType)getGroupByName(name, GroupEnumType.USER, parent, organization);
	}	
	public DirectoryGroupType getDirectoryByName(String name, OrganizationType organization) throws FactoryException, ArgumentException
	{
		return (DirectoryGroupType)getGroupByName(name, GroupEnumType.DATA, null, organization);
	}
	public DirectoryGroupType getDirectoryByName(String name, DirectoryGroupType parent, OrganizationType organization) throws FactoryException, ArgumentException
	{
		return (DirectoryGroupType)getGroupByName(name, GroupEnumType.DATA, parent, organization);
	}
	public BaseGroupType getGroupByName(String name, GroupEnumType group_type, BaseGroupType parent, OrganizationType organization) throws FactoryException, ArgumentException
	{
		String key_name = name + "-" + group_type + "-" + (parent == null ? 0 : parent.getId()) + "-" + organization.getId();
	
		//logger.info("Getting " + key_name);
		
		NameIdType out_group = readCache(key_name);
		if (out_group != null) return (BaseGroupType)out_group;
		//System.out.println("Fetching " + key_name);
		QueryFields x = null;
		List<NameIdType> groups = getByField(new QueryField[] { QueryFields.getFieldName(name), QueryFields.getFieldParent(( parent != null ? parent.getId() : 0)), QueryFields.getFieldGroupType(group_type) }, organization.getId());

		if (groups.size() > 0)
		{
			addToCache(groups.get(0),key_name);
			return (BaseGroupType)groups.get(0);
		}
		return null;
	}

	public List<UserGroupType> getUserGroups(UserGroupType parent) throws FactoryException, ArgumentException
	{
		List<QueryField> fields = new ArrayList<QueryField>();
		fields.add(QueryFields.getFieldParent(parent.getId()));
		fields.add(QueryFields.getFieldGroupType(GroupEnumType.USER.toString()));
		return getUserGroups(fields, parent.getOrganization());
	}
	public List<UserGroupType> getUserGroups(List<QueryField> fields, OrganizationType organization) throws FactoryException, ArgumentException
	{
		List<NameIdType> in_list = getByField(fields.toArray(new QueryField[0]), organization.getId());
		return convertList(in_list);

	}

	public UserGroupType getUserGroupById(long id, OrganizationType organization) throws ArgumentException
	{
		return (UserGroupType)getGroupById(id, GroupEnumType.USER, organization);
	}
	public List<DirectoryGroupType> getDirectoryGroups(DirectoryGroupType parent) throws FactoryException, ArgumentException
	{
		List<QueryField> fields = new ArrayList<QueryField>();
		fields.add(QueryFields.getFieldParent(parent.getId()));
		fields.add(QueryFields.getFieldGroupType(GroupEnumType.DATA.toString()));
		return getDirectoryGroups(fields, parent.getOrganization());
	}
	public List<DirectoryGroupType> getDirectoryGroups(List<QueryField> fields, OrganizationType organization) throws FactoryException, ArgumentException
	{
		List<NameIdType> in_list = getByField(fields.toArray(new QueryField[0]), organization.getId());
		return convertList(in_list);

	}
	
	public DirectoryGroupType getDirectoryById(long id, OrganizationType organization) throws ArgumentException
	{
		return (DirectoryGroupType)getGroupById(id, GroupEnumType.DATA, organization);
	}
	public BaseGroupType getGroupById(long id, OrganizationType organization) throws ArgumentException
	{
		return getGroupById(id, GroupEnumType.UNKNOWN, organization);
	}
	public BaseGroupType getGroupById(long id, GroupEnumType group_type, OrganizationType organization) throws ArgumentException{

		NameIdType out_group = readCache(id);
		if (out_group != null) return (BaseGroupType)out_group;
		List<QueryField> fields = new ArrayList<QueryField>();
		fields.add(QueryFields.getFieldId(id));
		if(group_type != GroupEnumType.UNKNOWN) fields.add(QueryFields.getFieldGroupType(group_type));
		List<NameIdType> groups = null;
		try {
			groups = getByField(fields.toArray(new QueryField[0]), organization.getId().intValue());
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (groups != null && groups.size() > 0)
		{
			BaseGroupType group = (BaseGroupType)groups.get(0);
			String key_name = group.getName() + "-" + group.getGroupType().toString() + "-" + group.getParentId() + "-" + group.getOrganization().getId();
			addToCache(group,key_name);
			return (BaseGroupType)groups.get(0);
		}
		return null;
	}
	
	protected void clearGroupParentCache(BaseGroupType group) throws ArgumentException
	{
		if (group == null) return;
		if (group.getParentId() > 0 && haveCacheId(group.getParentId()))
		{
			clearGroupCache(getGroupById(group.getParentId(), group.getOrganization()), false);
		}
	}
	protected void clearGroupCache(BaseGroupType group, boolean clear_parent) throws ArgumentException{
		if (group == null) return;
		if (clear_parent) clearGroupParentCache(group);
		String key_name = group.getName() + "-" + group.getGroupType().toString() + "-" + group.getParentId() + "-" + group.getOrganization().getId();
		switch (group.getGroupType())
		{
			case DATA:
				DirectoryGroupType dgroup = (DirectoryGroupType)group;
				clearDirectoryGroupCache(dgroup);
				break;
			default:
				System.out.println("Group type " + group.getGroupType() + " is not supported for caching.");
				break;
		}
		removeFromCache(group, key_name);
	}
	protected void clearDirectoryGroupCache(DirectoryGroupType group){
		group.getSubDirectories().clear();
		group.setPopulated(false);
		group.getData().clear();
		group.setReadData(false);
	}

	public boolean updateGroup(BaseGroupType group) throws FactoryException, ArgumentException
	{
		clearGroupParentCache(group);
		return update(group);
	}
	
	@Override
	public void setFactoryFields(List<QueryField> fields, NameIdType map, ProcessingInstructionType instruction){
		BaseGroupType use_group = (BaseGroupType)map;
		fields.add(QueryFields.getFieldGroupType(use_group.getGroupType().toString()));
		fields.add(QueryFields.getFieldReferenceId(use_group.getReferenceId()));
	}
	public void populate(BaseGroupType group) throws FactoryException, ArgumentException
	{
		if (!isValid(group) || group.getPopulated()) return;
        if (group.getGroupType() == GroupEnumType.DATA)
        {
            DirectoryGroupType dirGroup = (DirectoryGroupType)group;
            //populateSubDirectories(dirGroup);
            if (dirGroup.getParentId() > 0)
            {
                dirGroup.setParentGroup(getDirectoryById(group.getParentId(), group.getOrganization()));
            }
            dirGroup.setPath(getPath(dirGroup,false));
        }
        group.setPopulated(true);
        updateToCache(group);
	}
	public void populateSubDirectories(DirectoryGroupType group) throws FactoryException, ArgumentException
	{
		if (group == null) throw new FactoryException("Null group reference");
		//if (group.ReadSubdirectories) return;
		group.getSubDirectories().clear();
		group.getSubDirectories().addAll(getDirectoryGroups(group));
		//group.SubDirectories.Sort(new DirectoryComparer());
	}
	protected void populateDirectoryData(DirectoryGroupType group) throws FactoryException, ArgumentException
	{
		if (group.getPopulated()) return;
		group.getData().clear();
		group.getData().addAll(Factories.getDataFactory().getDataListByGroup(group,true, 0, 0, group.getOrganization()));
		//group.Data.Sort(new DataComparer());
	}
	public DirectoryGroupType getRootDirectory(OrganizationType organization) throws FactoryException, ArgumentException
	{
		return getDirectoryByName("Root", organization);
	}
	public DirectoryGroupType getHomeDirectory(OrganizationType organization) throws FactoryException, ArgumentException
	{

		return getDirectoryByName("Home", getRootDirectory(organization),organization);
	}
	public DirectoryGroupType getPersonsDirectory(OrganizationType organization) throws FactoryException, ArgumentException
	{

		return getDirectoryByName("Persons", getRootDirectory(organization),organization);
	}
	public DirectoryGroupType getUserDirectory(UserType user) throws FactoryException, ArgumentException
	{

		return getDirectoryByName(user.getName(), getHomeDirectory(user.getOrganization()),user.getOrganization());
	}
	public DirectoryGroupType getCreateUserDirectory(UserType user, String directory_name) throws FactoryException, ArgumentException
	{
		return getCreateDirectory(user, directory_name, getUserDirectory(user), user.getOrganization());
	}
	public DirectoryGroupType getCreateDirectory(UserType owner, String directory_name, DirectoryGroupType parent, OrganizationType organization) throws FactoryException, ArgumentException
	{

		DirectoryGroupType vdir = getDirectoryByName(directory_name, parent, organization);
		if (vdir == null)
		{
			vdir = newDirectoryGroup(owner, directory_name, parent, organization);
			if (addGroup(vdir))
			{
				vdir = getDirectoryByName(directory_name, parent, organization);
			}
			else vdir = null;
		}
		return vdir;
	}
	public UserGroupType getCreateUserGroup(UserType owner, String directory_name, BaseGroupType parent, OrganizationType organization) throws FactoryException, ArgumentException
	{
		UserGroupType vdir = getUserGroupByName(directory_name, parent, organization);
		if (vdir == null)
		{
			vdir = newUserGroup(owner, directory_name, parent, organization);
			if (addGroup(vdir))
			{
				vdir = getUserGroupByName(directory_name, parent, organization);
			}
			else vdir = null;
		}
		return vdir;
	}
	public DirectoryGroupType getCreatePath(UserType user, String path, OrganizationType organization) throws FactoryException, ArgumentException
	{
		DirectoryGroupType dir = findGroup(user, path, organization);
		if(dir == null && makePath(user, path, organization)){
			dir = findGroup(user, path, organization);
		}
		return dir;
	}
	public DirectoryGroupType findGroup(UserType user, String path, OrganizationType organization) throws FactoryException, ArgumentException
	{
		
		DirectoryGroupType out_dir = null;
		if (path == null || path.length() == 0) throw new FactoryException("Invalid path");

		String[] paths = path.split("/");

		DirectoryGroupType nested_group = null;

		String name = null;
		if (paths.length == 0 || path.equals("/"))
		{
			return getRootDirectory(organization);
		}
		if(paths.length == 0) throw new FactoryException("Invalid path list from '" + path + "'");

		for (int i = 0; i < paths.length; i++)
		{
			name = paths[i];
			
			if (name.length() == 0 && i == 0)
			{
				nested_group = getRootDirectory(organization);
				if (paths.length == 1)
				{
					break;
				}
				name = paths[++i];
			}
			else if (name.equals("~") && user != null)
			{
				Factories.getUserFactory().populate(user);
				//System.out.println("GF User pop = " + user.getPopulated());
				//System.out.println("GF User Home Dir = " + (user.getHomeDirectory() != null));
				//System.out.println("Found Home Marker and paths length = " + paths.length);
				if(user.getHomeDirectory() == null) throw new FactoryException("User home directory not populated");
				nested_group = user.getHomeDirectory();
				if (paths.length == 1) break;
				name = paths[++i];
			}
			/*
			if (paths[i].indexOf('*') > -1)
			{
				DirectoryGroupType match = null;
				populate(nested_group);
				Pattern p = Pattern.compile("^" + paths[i].replace("*",".*"));
				for(int d= 0; d < nested_group.getSubDirectories().size(); d++){
					DirectoryGroupType dir = nested_group.getSubDirectories().get(d);
					Matcher m = p.matcher(dir.getName());
					
					if(m.matches()){
						match = dir;
						break;
					}
				}
				nested_group = match;
			}
			else
			*/
			nested_group = getDirectoryByName(paths[i], nested_group, organization);
		}
		out_dir = nested_group;

		return out_dir;
	}
	public boolean makePath(UserType user, String path, OrganizationType organization) throws FactoryException, ArgumentException
	{

		boolean ret = false;
		if (path == null || path.length() == 0) throw new FactoryException("Invalid path value");

		// Check if the path exists
		//
		DirectoryGroupType check_group = findGroup(user, path, organization);
		if (check_group != null) return false;

		String[] paths = path.split("/");
		if (paths.length == 0 || path.equals("/"))
		{
			return false;
		}
		DirectoryGroupType nested_group = null;
		DirectoryGroupType ref_group = null;
		String name =  null;
		for (int i = 0; i < paths.length; i++)
		{
			name = paths[i];
			if (name.length() == 0 && i == 0)
			{
				ref_group = getRootDirectory(organization);
				if (paths.length == 1)
				{
					throw new FactoryException("Invalid group name in makepath");
					//ret = false;
					//break;
				}
				name = paths[++i];
			}
			else if (name.equals("~") && user != null)
			{
				Factories.getUserFactory().populate(user);
				ref_group = user.getHomeDirectory();
				if (paths.length == 1)
				{
					throw new FactoryException("MakeDirectoryPath: Cannot create user home directory.  This can only be created through setAccountDefaults");
					//ret = false;
					//break;
				}
				name = paths[++i];
			}

			if (ref_group == null)
			{
				throw new FactoryException("MakeDirectoryPath: Invalid directory reference id");
				//ret = false;
				//break;
			}

			nested_group = getDirectoryByName(name, ref_group, organization);
			System.out.println("Pathing " + name + " - " + (nested_group == null ? "NEW" : nested_group.getId()));
			if (nested_group == null)
			{
				nested_group = newDirectoryGroup(user, name, ref_group, organization);
				if (addGroup(nested_group))
				{
					nested_group = getDirectoryByName(name, ref_group, organization);
					ref_group = nested_group;
					ret = true;
				}
				else
				{
					throw new FactoryException("MakeDirectoryPath: Unable to create directory: '" + name + "'");
					//ret = false;
					//break;
				}
			}
			else
			{
				ref_group = nested_group;
				ret = true;
			}

		}

		return ret;
	}
	public String getPath(DirectoryGroupType leaf_group) throws FactoryException, ArgumentException
	{
		return getPath(leaf_group, true);
	}
	protected String getPath(DirectoryGroupType leaf_group, boolean populate) throws FactoryException, ArgumentException
	{
		if(leaf_group == null) return null;
		List<DirectoryGroupType> group_list = new ArrayList<DirectoryGroupType>();
		group_list.add(leaf_group);
		if(populate) populate(leaf_group);
		while (leaf_group.getParentId() != null && leaf_group.getParentId() > 0)
		{
			DirectoryGroupType parentGroup = getById(leaf_group.getParentId(),leaf_group.getOrganization());
			group_list.add(parentGroup);
			leaf_group = parentGroup;
			populate(leaf_group);
		}
		Collections.reverse(group_list);
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < group_list.size(); i++)
		{
			DirectoryGroupType group = group_list.get(i);
			if (group.getName().equals("Root") && group.getParentId() == 0) continue;
			buff.append("/" + group.getName());
		}
		return buff.toString();
	}
	public int getCount(UserGroupType group) throws FactoryException
	{
		return getCountByField(this.getDataTables().get(0), new QueryField[]{QueryFields.getFieldParent(group.getId()),QueryFields.getFieldGroupType(GroupEnumType.USER)}, group.getOrganization().getId());
	}

	public List<UserGroupType>  getUserGroupListByParent(BaseGroupType parent, int startRecord, int recordCount, OrganizationType organization)  throws FactoryException, ArgumentException
	{
		return getUserGroupList(new QueryField[] { QueryFields.getFieldParent(parent.getId()),QueryFields.getFieldGroupType(GroupEnumType.USER) }, startRecord, recordCount, organization);
	}
	
	public List<UserGroupType>  getUserGroupListByParent(BaseGroupType parent, ProcessingInstructionType instruction, int startRecord, int recordCount, OrganizationType organization)  throws FactoryException, ArgumentException
	{
		return getUserGroupList(new QueryField[] { QueryFields.getFieldParent(parent.getId()),QueryFields.getFieldGroupType(GroupEnumType.USER) }, instruction, startRecord, recordCount,organization);
	}
	public List<UserGroupType>  getUserGroupList(QueryField[] fields, int startRecord, int recordCount, OrganizationType organization)  throws FactoryException, ArgumentException
	{
		ProcessingInstructionType instruction = new ProcessingInstructionType();
		instruction.setOrderClause("name ASC");
		return getUserGroupList(fields, instruction, startRecord,recordCount,organization);
	}
	public List<UserGroupType>  getUserGroupList(QueryField[] fields, ProcessingInstructionType instruction,int startRecord, int recordCount, OrganizationType organization)  throws FactoryException, ArgumentException
	{
		/// If pagination not 
		///
		if (instruction != null && startRecord >= 0 && recordCount > 0 && instruction.getPaginate() == false)
		{
			instruction.setPaginate(true);
			instruction.setStartIndex(startRecord);
			instruction.setRecordCount(recordCount);
		}
		return getUserGroupList(fields, instruction, organization);
	}
	public List<UserGroupType> getUserGroupList(QueryField[] fields, ProcessingInstructionType instruction,OrganizationType organization) throws FactoryException, ArgumentException
	{

		if(instruction == null) instruction = new ProcessingInstructionType();

		List<NameIdType> dataList = getByField(fields, instruction, organization.getId());
		return convertList(dataList);
	}
	
	public int getCount(DirectoryGroupType group) throws FactoryException
	{
		return getCountByField(this.getDataTables().get(0), new QueryField[]{QueryFields.getFieldParent(group.getId()),QueryFields.getFieldGroupType(GroupEnumType.DATA)}, group.getOrganization().getId());
	}
	
	public List<DirectoryGroupType>  getDirectoryListByParent(DirectoryGroupType parent, int startRecord, int recordCount, OrganizationType organization)  throws FactoryException, ArgumentException
	{
		return getDirectoryList(new QueryField[] { QueryFields.getFieldParent(parent.getId()),QueryFields.getFieldGroupType(GroupEnumType.DATA) }, startRecord, recordCount, organization);
	}
	
	public List<DirectoryGroupType>  getDirectoryListByParent(DirectoryGroupType parent, ProcessingInstructionType instruction, int startRecord, int recordCount, OrganizationType organization)  throws FactoryException, ArgumentException
	{
		return getDirectoryList(new QueryField[] { QueryFields.getFieldParent(parent.getId()),QueryFields.getFieldGroupType(GroupEnumType.DATA) }, instruction, startRecord, recordCount,organization);
	}
	public List<DirectoryGroupType>  getDirectoryList(QueryField[] fields, int startRecord, int recordCount, OrganizationType organization)  throws FactoryException, ArgumentException
	{
		ProcessingInstructionType instruction = new ProcessingInstructionType();
		instruction.setOrderClause("name ASC");
		return getDirectoryList(fields, instruction, startRecord,recordCount,organization);
	}
	public List<DirectoryGroupType>  getDirectoryList(QueryField[] fields, ProcessingInstructionType instruction,int startRecord, int recordCount, OrganizationType organization)  throws FactoryException, ArgumentException
	{
		/// If pagination not 
		///
		if (instruction != null && startRecord >= 0 && recordCount > 0 && instruction.getPaginate() == false)
		{
			instruction.setPaginate(true);
			instruction.setStartIndex(startRecord);
			instruction.setRecordCount(recordCount);
		}
		return getDirectoryList(fields, instruction, organization);
	}
	public List<DirectoryGroupType> getDirectoryList(QueryField[] fields, ProcessingInstructionType instruction,OrganizationType organization) throws FactoryException, ArgumentException
	{

		if(instruction == null) instruction = new ProcessingInstructionType();

		List<NameIdType> dataList = getByField(fields, instruction, organization.getId());
		return convertList(dataList);
	}

}
