CREATE TABLE tags (
  tag VARCHAR(50) NOT NULL
);
  
ALTER TABLE tags ADD CONSTRAINT tags_pk PRIMARY KEY ( tag );

CREATE TABLE recipe (
  recipe_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  recipe_title VARCHAR(200) NOT NULL,
  recipe CLOB(64 K) NOT NULL,
  recipe_date TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
  recipe_update TIMESTAMP );
  
ALTER TABLE recipe ADD CONSTRAINT recipe_pk PRIMARY KEY ( recipe_id );

CREATE TABLE recipe_tags (
  recipe_id INTEGER NOT NULL,
  tag VARCHAR(50) NOT NULL );
  
ALTER TABLE recipe_tags ADD CONSTRAINT recipe_tags_pk PRIMARY KEY ( recipe_id, tag );

ALTER TABLE recipe_tags ADD CONSTRAINT recipe_tags_fk_recipe FOREIGN KEY ( recipe_id ) REFERENCES recipe ( recipe_id );
ALTER TABLE recipe_tags ADD CONSTRAINT recipe_tags_fk_tags FOREIGN KEY ( tag ) REFERENCES tags ( tag ); 
