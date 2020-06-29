CREATE TABLE category (
  id UUID PRIMARY KEY,
  name TEXT,
  description TEXT,
  parent_id UUID
);

CREATE TABLE category_closure (
  ancestor_id UUID,
  descendant_id UUID,
  PRIMARY KEY (ancestor_id, descendant_id)
);

ALTER TABLE category_closure
    ADD CONSTRAINT category_closure_ancestor_fk FOREIGN KEY (ancestor_id)
        REFERENCES category(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        NOT DEFERRABLE;

ALTER TABLE category_closure
    ADD CONSTRAINT category_closure_descendant_fk FOREIGN KEY (descendant_id)
        REFERENCES category(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        NOT DEFERRABLE;

ALTER TABLE category
    ADD CONSTRAINT category_parent_fk FOREIGN KEY (parent_id)
        REFERENCES category(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
        NOT DEFERRABLE;