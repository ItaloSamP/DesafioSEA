create table SEA_Category (
	uuid_ VARCHAR(75) null,
	categoryId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null
);

create table SEA_Comment (
	uuid_ VARCHAR(75) null,
	commentId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	taskId LONG,
	text_ VARCHAR(75) null
);

create table SEA_Subtask (
	uuid_ VARCHAR(75) null,
	subtaskId LONG not null primary key,
	taskId LONG,
	createDate DATE null,
	modifiedDate DATE null,
	title VARCHAR(75) null,
	isCompleted BOOLEAN
);

create table SEA_Task (
	uuid_ VARCHAR(75) null,
	taskId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	title VARCHAR(75) null,
	description VARCHAR(75) null,
	isCompleted BOOLEAN,
	isDeleted BOOLEAN,
	dueDate DATE null,
	imageId LONG,
	categoryId LONG
);