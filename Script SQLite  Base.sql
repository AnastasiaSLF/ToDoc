CREATE TABLE Task(
	idTask              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,
	nameTask            TEXT NOT NULL ,
	dateCreationTask    NUMERIC NOT NULL ,
	idProject           INTEGER

	,CONSTRAINT Task_Project_FK FOREIGN KEY (idProject) REFERENCES Project(idProject)
);
