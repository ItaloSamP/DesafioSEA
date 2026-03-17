create index IX_5749100F on SEA_Subtask (taskId);
create index IX_C4A2FF39 on SEA_Subtask (uuid_[$COLUMN_LENGTH:75$]);

create index IX_1F679D6 on SEA_Task (groupId, userId, isDeleted);
create unique index IX_74E6F1B1 on SEA_Task (uuid_[$COLUMN_LENGTH:75$], groupId);